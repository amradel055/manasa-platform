/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;


import com.toby.bean.JournalDocumentArrangedReportBean;
import com.toby.dto.TobyUserDTO;
import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.GeneralJournalDetailsDTO;
import com.toby.dto.GlYearDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GlYear;
import com.toby.entity.Symbol;
import java.math.BigDecimal;
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
    @EJB
    private GeneraljournalDetailsService generaljournalDetailsService;
    @EJB
    private GeneraljournalDetailsServiceDTO generaljournalDetailsServiceDTO;

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
    public Long getTotalGeneralJournalForRevision(GlYearDTO year, Integer branchId, Date dateFrom, Date dateTo, Integer revisionAccount, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("dateYearFrom", year.getDateFrom());
        filters.put("dateYearTo", year.getDateTo());
        builder.append("SELECT count(e) FROM GeneralJournal e WHERE e.branchId.id = :branchId AND ( e.generalType.serial < 1000 OR e.generalType.serial > 2000) "
                + " AND e.generalData >= :dateYearFrom AND e.generalData <= :dateYearTo   ");

        if (dateFrom != null) {
            filters.put("dateFrom", dateFrom);
            builder.append(" AND e.generalData >= :dateFrom ");
        }

        if (dateTo != null) {
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

    @Override
    public List<GeneralJournalDTO> getGeneralJournalDTOByBranchIdForRevision(GlYearDTO year, Integer branchId, Date dateFrom, Date dateTo,
            Integer revisionAccount, int first,
            Integer pageSize, Map<String, Object> filters) {
        filters.put("branchId", branchId);
        filters.put("dateFrom", year.getDateFrom());
        filters.put("dateTo", year.getDateTo());
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT e FROM GeneralJournal e WHERE e.branchId.id = :branchId AND ( e.generalType.serial < 1000 OR e.generalType.serial > 2000 ) "
                + "AND e.generalData >= :dateFrom AND e.generalData <= :dateTo   ");

        if (dateFrom != null) {
            filters.put("dateFrom", dateFrom);
            builder.append(" AND e.generalData >= :dateFrom ");
        }

        if (dateTo != null) {
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
        if (pageSize != null) {
            details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        } else {
            details = dao.findListByQuery(builder.toString(), filters);
        }
        return returnListDTO(details);
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
    public List<GeneralJournalDTO> getGeneralJournalDTOByBranchId(GlYearDTO year, Integer branchId, int first, int pageSize, Map<String, Object> filters) {
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
    public synchronized GeneralJournalDTO addGeneralJournalDTO(GeneralJournalDTO generalJournalDTO, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", generalJournalDTO.getBranchId());
        params.put("companyId", generalJournalDTO.getCompanyId());
        params.put("glYear", generalJournalDTO.getGlYear().getId());
        Map<String, Object> params1 = new HashMap<>();
        params1.put("generalType", generalJournalDTO.getGeneralType().getId());
        params1.put("glYear", generalJournalDTO.getGlYear().getId());
        params1.put("branchId", generalJournalDTO.getBranchId());

        Integer generaldocumet = dao.findEntityByQuery("SELECT MAX(g.generalDecument) FROM GeneralJournal g  WHERE g.glYear.id =:glYear AND g.generalType.id =:generalType and g.branchId.id =:branchId  ", params1);

        if (generaldocumet == null) {
            generaldocumet = 1;

        } else {
            generaldocumet = generaldocumet + 1;
        }
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

        GeneralJournal entity = mapFromDTO(generalJournalDTO, tobyUser);
        entity = dao.updateEntity(entity);
        generalJournalDTO = mapTODTO(entity, false);
        return generalJournalDTO;
    }

    @Override
    public void addGeneralJournalDTOWithDetailFromExcel(List<GeneralJournalDTO> list, TobyUser tobyUser) {
        Integer generaldocumet = 1;
//        Integer serial = 1;
        Integer idDtail = 1;
        for (GeneralJournalDTO dto : list) {
            System.err.println("----------> " + dto.getSerial() + " <---------------------" + new Date());
            generaldocumet++;
            dto.setSerial(dto.getSerial());
            dto.setGeneralDecument(generaldocumet);
            GeneralJournal entity = mapFromDTO(dto, tobyUser);
            entity = dao.updateEntity(entity);
            generaljournalDetailsService.addGenDetalilsGlobal(dto.getDetailList(), tobyUser, entity.getId(), idDtail);
            idDtail = idDtail + 5;
        }

    }

    @Override
    public void addGeneralJournalDTOWithDetail(List<GeneralJournalDTO> list, TobyUser tobyUser) {
        Integer generaldocumet = 1;
        Integer serial = 1;
//        Integer id = 10000;
        Integer id = 1;
//        Integer id = 41000;
//        Integer id = 10000;
//        Integer id = 25500;
//        Integer id = 56000;
//        Integer idDtail = 45000;
        Integer idDtail = 1;
//        Integer idDtail = 200000;

//        Integer idDtail = 120000;
//        Integer idDtail = 280000;
        for (GeneralJournalDTO dto : list) {
            System.err.println("----------> " + id + " <---------------------" + new Date());
            generaldocumet++;
            serial++;
            dto.setSerial(serial);
            dto.setId(id);
            dto.setGeneralDecument(generaldocumet);
            GeneralJournal entity = mapFromDTO(dto, tobyUser);
            dao.saveEntity(entity);
            generaljournalDetailsService.addGenDetalilsGlobal(dto.getDetailList(), tobyUser, id, idDtail);
            idDtail = idDtail + 5;
            id++;
        }

    }

    @Override
    public void addGeneralJournalDTOWithDetail(GeneralJournalDTO dto, TobyUser tobyUser) {

        System.err.println("---------->  <---------------------" + new Date());

        GeneralJournal entity = mapFromDTO(dto, tobyUser);
        dao.saveEntity(entity);
        generaljournalDetailsService.addGenDetalilsGlobal(dto.getDetailList(), tobyUser);

    }

    @Override
    public void addGeneralJournalDTOWithDetail(GeneralJournalDTO dto, TobyUser tobyUser, Integer serial, Integer idHead, Integer idDetail) {

        System.err.println("----------> " + idDetail + " <---------------------" + new Date());

        dto.setSerial(serial);
        dto.setId(idHead);
        GeneralJournal entity = mapFromDTO(dto, tobyUser);
        dao.saveEntity(entity);
        generaljournalDetailsService.addGenDetalilsGlobal(dto.getDetailList(), tobyUser, idHead, idDetail);

    }

    @Override
    public void deleteGeneralJournalDTO(GeneralJournalDTO generalJournalDTO, TobyUser tobyUser) {

        GeneralJournal detailentity = mapFromDTO(generalJournalDTO, tobyUser);
        dao.deleteEntity(detailentity);

    }

    @Override
    public void addGeneralJournalDTOForCorrectence(List<GeneralJournalDTO> generalJournals, TobyUser tobyUser) {
        List<GeneralJournal> generalJournalList = returnListEntity(generalJournals, tobyUser);
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
    public synchronized void addGeneralJournalsDTOForReviewing(List<GeneralJournalDTO> generalJournalList, TobyUser tobyUser) {
        if (generalJournalList != null && !generalJournalList.isEmpty()) {
            for (GeneralJournalDTO gen : generalJournalList) {
                GeneralJournal generalJournal = mapFromDTO(gen, tobyUser);
                dao.updateEntity(generalJournal);
            }
        }
    }

    @Override
    public void updateGeneralJournalsDTOForReviewing(Date dateFrom, Date dateTo, Integer branchId, Integer postFlag, String postFlagReview) {
        //UPDATE `toby`.`general_journal` SET `post_flag` = '0', `post_flag_review` = NULL WHERE (`id` = '259');
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

    private List<GeneralJournal> returnListEntity(List<GeneralJournalDTO> list, TobyUser tobyUser) {
        List<GeneralJournal> entityList = new ArrayList<>();
        for (GeneralJournalDTO dto : list) {
            entityList.add(mapFromDTO(dto, tobyUser));
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

        TobyUserDTO tobyUserDTO = new TobyUserDTO();
        tobyUserDTO.setCreatedBy(entity.getCreatedBy().getId());
        tobyUserDTO.setCreatedDate(entity.getCreatedBy().getCreationDate());
        tobyUserDTO.setName(entity.getCreatedBy().getName());
        dTO.setCreated_by(tobyUserDTO);

        return dTO;
    }

    private GeneralJournal mapFromDTO(GeneralJournalDTO dTO, TobyUser tobyUser) {
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

        entity.setId(dTO.getId());
        entity.setGeneralNumber(dTO.getGeneralNumber());
        entity.setGeneralData(dTO.getGeneralData());
        entity.setGeneralStatement(dTO.getGeneralStatement());
        entity.setGeneralDecument(dTO.getGeneralDecument());
        entity.setPostFlagReview(dTO.getPostFlagReview());
        entity.setSerial(dTO.getSerial());
        entity.setMacId(dTO.getMacId());
        entity.setPost_flag(dTO.isPost_flag());

        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());

        return entity;
    }

    @Override
    public GeneralJournal convertFromDTO(GeneralJournalDTO dTO, TobyUser tobyUser) {
        return mapFromDTO(dTO, tobyUser);
    }

    public List<JournalDocumentArrangedReportBean> printVoutcher(Integer generalJournalId) {
        List<JournalDocumentArrangedReportBean> list = new ArrayList<>();
        if (generalJournalId != null) {
            GeneralJournal entity = dao.findEntityById(GeneralJournal.class, generalJournalId);
            List<GeneralJournalDetailsDTO> detailsList = generaljournalDetailsServiceDTO.getAllJournalDetailsForJorunal(generalJournalId);
            for (GeneralJournalDetailsDTO generalJournalDetails : detailsList) {
                JournalDocumentArrangedReportBean journalDocumentArrangedReportBean = new JournalDocumentArrangedReportBean();
                journalDocumentArrangedReportBean.setJournalNum(entity.getSerial());
                journalDocumentArrangedReportBean.setDocumentNum(entity.getGeneralDecument());
                journalDocumentArrangedReportBean.setJournalDate(entity.getGeneralData());
                journalDocumentArrangedReportBean.setUser(entity.getCreatedBy().getName());
                journalDocumentArrangedReportBean.setGeneralType(entity.getGeneralType() != null ? entity.getGeneralType().getName() : null);
//                if (summaryDebit.compareTo(summaryCredit) == 0) {
//                    journalDocumentArrangedReportBean.setPosting("موزون");
//                } else {
//                    journalDocumentArrangedReportBean.setPosting("غير موزون");
//                }
                if (entity != null && entity.isPost_flag()) {
                    journalDocumentArrangedReportBean.setPostFlag("مرحل");

                } else {
                    journalDocumentArrangedReportBean.setPostFlag("غير مرحل");
                }
                journalDocumentArrangedReportBean.setRemarks(entity.getGeneralStatement());
                journalDocumentArrangedReportBean.setAccountName(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getName() != null ? generalJournalDetails.getGlACCOUNTId().getName() : null) : null);
                journalDocumentArrangedReportBean.setAccountNumber(generalJournalDetails.getGlACCOUNTId() != null ? (generalJournalDetails.getGlACCOUNTId().getAccNumber() != null ? generalJournalDetails.getGlACCOUNTId().getAccNumber() : null) : null);
                journalDocumentArrangedReportBean.setAdminUnitName(generalJournalDetails.getAdminUnitId() != null ? (generalJournalDetails.getAdminUnitId().getName() != null ? generalJournalDetails.getAdminUnitId().getName() : null) : null);
                journalDocumentArrangedReportBean.setCostCenterName(generalJournalDetails.getCostCenterId() != null ? (generalJournalDetails.getCostCenterId().getName() != null ? generalJournalDetails.getCostCenterId().getName() : null) : null);
                journalDocumentArrangedReportBean.setCreditAmount(generalJournalDetails.getCreditamount() != null ? generalJournalDetails.getCreditamount() : BigDecimal.ZERO);
                journalDocumentArrangedReportBean.setDebitAmount(generalJournalDetails.getDebitAmount() != null ? generalJournalDetails.getDebitAmount() : BigDecimal.ZERO);
                journalDocumentArrangedReportBean.setDiscribtion(generalJournalDetails.getDiscribtion() != null ? generalJournalDetails.getDiscribtion() : null);
                journalDocumentArrangedReportBean.setCreationDate(generalJournalDetails.getCreatedDate() != null ? generalJournalDetails.getCreatedDate() : null);
                list.add(journalDocumentArrangedReportBean);
            }
            return list;
        }
        return null;
    }

}
