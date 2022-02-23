/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GeneralJournalDTO;
import com.isag.dto.GlYearDTO;
import com.isag.dto.IsagUserDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GlYear;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class GeneraljournalServiceImplDTO implements GeneraljournalServiceDTO {

    @EJB
    private GenericDAO dao;

    @Override
    public Long getTotalGeneralJournal(GlYearDTO year, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("dateFrom", year.getDateFrom());
        filters.put("dateTo", year.getDateTo());
        builder.append("SELECT count(e) FROM GeneralJournal e WHERE e.branchId.id = :branchId AND e.generalData >= :dateFrom AND e.generalData <= :dateTo ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details;
    }
    
    @Override
    public Long getTotalGeneralJournalForRevision(GlYearDTO year, Integer branchId,Date dateFrom , Date dateTo, Integer revisionAccount , Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("dateYearFrom", year.getDateFrom());
        filters.put("dateYearTo", year.getDateTo());
        builder.append("SELECT count(e) FROM GeneralJournal e WHERE e.branchId.id = :branchId AND ( e.generalType.serial < 1000 OR e.generalType.serial > 2000) "
                + " AND e.generalData >= :dateYearFrom AND e.generalData <= :dateYearTo   ");
        
        if(dateFrom != null){
            filters.put("dateFrom", dateFrom);
            builder.append(" AND e.generalData >= :dateFrom ");
        }
        
        if(dateTo != null){
            filters.put("dateTo", dateTo);
            builder.append(" AND e.generalData <= :dateTo ");
        }
        
        if (revisionAccount != null && revisionAccount == 0) {
            builder.append("  AND ( e.post_flag IS NULL  OR e.post_flag = 0 )  ");
        } else if (revisionAccount != null && revisionAccount == 1) {
            builder.append("  AND  e.post_flag = 1   ");
        }
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.serial like CONCAT('%',:globalFilter ,'%') OR e.generalData  like CONCAT('%',:globalFilter ,'%') OR e.generalType.name like CONCAT('%',:globalFilter ,'%') OR e.generalDecument like CONCAT('%',:globalFilter ,'%')  OR e.createdBy.name like CONCAT('%',:globalFilter ,'%') OR e.generalStatement like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<GeneralJournalDTO> getGeneralJournalDTOByBranchId(GlYearDTO year, Integer branchId, int first, int pageSize,  Map<String, Object> filters) {
        filters.put("branchId", branchId);
        filters.put("dateFrom", year.getDateFrom());
        filters.put("dateTo", year.getDateTo());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM GeneralJournal e WHERE e.branchId.id = :branchId AND e.generalData >= :dateFrom AND e.generalData <= :dateTo  ");
        appendFilter(filters, branchId, builder);
        builder.append(" order by e.serial DESC");
        List<GeneralJournal> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(details);
    }

    @Override
    public List<GeneralJournalDTO> getGeneralJournalDTOByBranchIdForRevision(GlYearDTO year, Integer branchId,Date dateFrom , Date dateTo , 
            Integer revisionAccount, int first,
            Integer pageSize,  Map<String, Object> filters) {
        filters.put("branchId", branchId);
        filters.put("dateFrom", year.getDateFrom());
        filters.put("dateTo", year.getDateTo());
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT e FROM GeneralJournal e WHERE e.branchId.id = :branchId AND ( e.generalType.serial < 1000 OR e.generalType.serial > 2000 ) "
                + "AND e.generalData >= :dateFrom AND e.generalData <= :dateTo   ");
       
        if(dateFrom != null){
            filters.put("dateFrom", dateFrom);
            builder.append(" AND e.generalData >= :dateFrom ");
        }
        
        if(dateTo != null){
            filters.put("dateTo", dateTo);
            builder.append(" AND e.generalData <= :dateTo ");
        }        
        
        if (revisionAccount != null && revisionAccount == 0) {
            builder.append("  AND ( e.post_flag IS NULL  OR e.post_flag = 0 )  ");
        } else if (revisionAccount != null && revisionAccount == 1) {
            builder.append("  AND  e.post_flag = 1   ");
        }

        appendFilter(filters, branchId, builder);
        builder.append(" order by e.serial DESC");
        List<GeneralJournal> details = null;
        if(pageSize != null){
         details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        }else{
            details = dao.findListByQuery(builder.toString(), filters);
        }
        return returnListDTO(details);
    }

    @Override
    public synchronized GeneralJournalDTO addGeneralJournalDTO(GeneralJournalDTO generalJournalDTO, IsagUser isagUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", generalJournalDTO.getBranchId());
        params.put("companyId", generalJournalDTO.getCompanyId());
        params.put("glYear", generalJournalDTO.getGlYear().getId());
        Map<String, Object> params1 = new HashMap<>();
        params1.put("generalType", generalJournalDTO.getGeneralType().getId());
        params1.put("glYear", generalJournalDTO.getGlYear().getId());
        params1.put("branchId", generalJournalDTO.getBranchId());

        Integer generaldocumet = findMaxGenralDocuments(params1);
        Integer max = 0;
        synchronized (max) {
            try {
                max = dao.findEntityByQuery("SELECT MAX(g.serial) FROM GeneralJournal g  WHERE g.branchId.id =:branchId AND g.companyId.id =:companyId and g.glYear.id =:glYear  ", params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (max == null) {
            max = 0;
        }
        // generalJournal.setSerial(25);
        if (generalJournalDTO.getId() == null) {
            generalJournalDTO.setSerial(max + 1);
            if (generalJournalDTO.getGeneralType() != null && (generalJournalDTO.getGeneralType().getSerial() < 1000 || generalJournalDTO.getGeneralType().getSerial() > 2000)) {
                generalJournalDTO.setGeneralDecument(generaldocumet);
            }
        }

        GeneralJournal entity = mapFromDTO(generalJournalDTO, isagUser);
        entity = dao.updateEntity(entity);
        generalJournalDTO = mapTODTO(entity, false);
        return generalJournalDTO;
    }

    @Override
    public Integer findMaxGenralDocuments(Map<String, Object> params1) {
        Integer generaldocumet = dao.findEntityByQuery("SELECT MAX(g.generalDecument) FROM GeneralJournal g  WHERE g.glYear.id =:glYear AND g.generalType.id =:generalType and g.branchId.id =:branchId  ", params1);
        if (generaldocumet == null) {
            generaldocumet = 1;
            
        } else {
            generaldocumet = generaldocumet + 1;
        }
        return generaldocumet;
    }

    @Override
    public void deleteGeneralJournalDTO(GeneralJournalDTO generalJournalDTO, IsagUser isagUser) {

        GeneralJournal detailentity = mapFromDTO(generalJournalDTO, isagUser);
        dao.deleteEntity(detailentity);

    }

    @Override
    public void addGeneralJournalDTOForCorrectence(List<GeneralJournalDTO> generalJournals, IsagUser isagUser) {
        List<GeneralJournal> generalJournalList = returnListEntity(generalJournals, isagUser);
        for (GeneralJournal gj : generalJournalList) {
            dao.updateEntity(gj);
        }
    }

    @Override
    public List<GeneralJournalDTO> getALLGeneralJournalDTOByBranchIdAndYear(Integer selectedBranch, GlYearDTO year) {
        Map<String, Object> params = new HashMap<>();
        params.put("selectedBranch", selectedBranch);
        params.put("dateFrom", year.getDateFrom());
        params.put("dateTo", year.getDateTo());
        List<GeneralJournal> generalJournal = dao.findListByQuery("SELECT e FROM GeneralJournal e WHERE e.branchId.id = :selectedBranch AND e.generalData >= :dateFrom AND e.generalData <= :dateTo order by e.serial desc", params);
        return returnListDTO(generalJournal);
    }

    @Override
    public synchronized void addGeneralJournalsDTOForReviewing(List<GeneralJournalDTO> generalJournalList, IsagUser isagUser) {
        if (generalJournalList != null && !generalJournalList.isEmpty()) {
            for (GeneralJournalDTO gen : generalJournalList) {
                GeneralJournal generalJournal = mapFromDTO(gen, isagUser);
                dao.updateEntity(generalJournal);
            }
        }
    }

    @Override
    public void updateGeneralJournalsDTOForReviewing(Date dateFrom, Date dateTo, Integer branchId, Integer postFlag, String postFlagReview) {
        //UPDATE `isag`.`general_journal` SET `post_flag` = '0', `post_flag_review` = NULL WHERE (`id` = '259');
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE GeneralJournal e SET e.post_flag = ");
        if (postFlag == 1) {
            stringBuilder.append(true);
        } else {
            stringBuilder.append(false);
        }
        if (postFlagReview == null) {
            stringBuilder.append(", e.postFlagReview = ");
            stringBuilder.append(postFlagReview);
        } else {
            stringBuilder.append(", e.postFlagReview = '").append(postFlagReview).append("'");
        }
        //stringBuilder.append(postFlagReview);
        stringBuilder.append(" WHERE e.branchId.id = ");
        stringBuilder.append(branchId.toString());

        if (dateFrom != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
            stringBuilder.append(" and e.generalData >= '").append(formatDateFrom).append("'");
        }
        if (dateTo != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
            stringBuilder.append(" and e.generalData <= '").append(formatDateTo).append("'");
        }
        dao.executeDeleteQuery(stringBuilder.toString());

    }

    private List<GeneralJournalDTO> returnListDTO(List<GeneralJournal> list) {
        List<GeneralJournalDTO> dTOList = new ArrayList<>();
        for (GeneralJournal entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private List<GeneralJournal> returnListEntity(List<GeneralJournalDTO> list, IsagUser isagUser) {
        List<GeneralJournal> entityList = new ArrayList<>();
        for (GeneralJournalDTO dto : list) {
            entityList.add(mapFromDTO(dto, isagUser));
        }
        return entityList;
    }

    private GeneralJournalDTO mapTODTO(GeneralJournal entity, Boolean needDetail) {
        GeneralJournalDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private GeneralJournalDTO initMapToDTO(GeneralJournal entity) {

        GeneralJournalDTO dTO = new GeneralJournalDTO();
        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());
        if (entity.getGlYear() != null) {
            GlYearDTO glYearDTO = new GlYearDTO();
            glYearDTO.setId(entity.getGlYear().getId());
            glYearDTO.setName(entity.getGlYear().getName());
            glYearDTO.setDateFrom(entity.getGlYear().getDateFrom());
            glYearDTO.setDateTo(entity.getGlYear().getDateTo());
            glYearDTO.setYear(entity.getGlYear().getYear());
            dTO.setGlYear(glYearDTO);
        }

        if (entity.getGeneralType() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(entity.getGeneralType().getId());
            symbolDTO.setAccountId(entity.getGeneralType().getAccountId() != null ? entity.getGeneralType().getAccountId().getId() : null);
            symbolDTO.setName(entity.getGeneralType().getName());
            symbolDTO.setSerial(entity.getGeneralType().getSerial());
            symbolDTO.setGeneralSymbolId(entity.getGeneralType().getGeneralSymbolId() != null ? entity.getGeneralType().getGeneralSymbolId().getId() : null);
            dTO.setGeneralType(symbolDTO);
        }

        dTO.setGeneralData(entity.getGeneralData());
        dTO.setGeneralDecument(entity.getGeneralDecument());
        dTO.setGeneralStatement(entity.getGeneralStatement());
        dTO.setGeneralNumber(entity.getGeneralNumber());
        dTO.setPostFlagReview(entity.getPostFlagReview());
        dTO.setSerial(entity.getSerial());
        dTO.setMacId(entity.getMacId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setPost_flag(entity.isPost_flag());
        dTO.setUserModifyAbility(entity.isUserModifyAbility());
        if (entity.isPost_flag() && entity.getPostFlagReview() != null) {
            dTO.setPostFlagReview("تمت مراجعته");
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setMarkEdit(entity.getMarkEdit());

        IsagUserDTO isagUserDTO = new IsagUserDTO();
        isagUserDTO.setCreatedBy(entity.getCreatedBy().getId());
        isagUserDTO.setCreatedDate(entity.getCreatedBy().getCreationDate());
        isagUserDTO.setName(entity.getCreatedBy().getName());
        dTO.setCreated_by(isagUserDTO);

        return dTO;
    }

    private GeneralJournal mapFromDTO(GeneralJournalDTO dTO, IsagUser isagUser) {
        GeneralJournal entity = new GeneralJournal();

        if (dTO.getGeneralType() != null && dTO.getGeneralType().getId() != null) {
            Symbol parent = new Symbol();
            parent.setId(dTO.getGeneralType().getId());
            entity.setGeneralType(parent);
        }

        if (dTO.getGlYear() != null && dTO.getGlYear().getId() != null) {
            GlYear parent = new GlYear();
            parent.setId(dTO.getGlYear().getId());
            entity.setGlYear(parent);
        }

        entity.setGeneralNumber(dTO.getGeneralNumber());
        entity.setGeneralData(dTO.getGeneralData());
        entity.setGeneralStatement(dTO.getGeneralStatement());
        entity.setGeneralDecument(dTO.getGeneralDecument());
        entity.setPostFlagReview(dTO.getPostFlagReview());
        entity.setSerial(dTO.getSerial());
        entity.setMacId(dTO.getMacId());
        entity.setPost_flag(dTO.isPost_flag());

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());

        return entity;
    }

    @Override
    public GeneralJournal convertFromDTO(GeneralJournalDTO dTO, IsagUser isagUser) {
        return mapFromDTO(dTO, isagUser);
    }
}
