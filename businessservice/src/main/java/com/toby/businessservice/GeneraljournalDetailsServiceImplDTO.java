/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.GeneralJournalDetailsDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
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
public class GeneraljournalDetailsServiceImplDTO implements GeneraljournalDetailsServiceDTO {
    
    private Integer maxSerial;
    @EJB
    private GenericDAO dao;
    @EJB
    private GeneraljournalDetailsServiceDTO detailsService;
    
    @Override
    public List<GeneralJournalDetailsDTO> getAllJournalDetailsForJorunal(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("journalId", id);
        List<GeneralJournalDetails> detail = new ArrayList<>();
        detail = dao.findListByQuery("SELECT d FROM GeneralJournalDetails d WHERE d.generalJournalId.id=:journalId", params);
        return returnListDTO(detail);
    }
    
    @Override
    public List<GeneralJournalDetailsDTO> addGenDetalils(List<GeneralJournalDetailsDTO> details, List<GeneralJournalDetailsDTO> generalListDeleted, TobyUser tobyUser) {
        List<GeneralJournalDetails> list = new ArrayList<>();
        GeneralJournalDetails gjd;
        if (details != null && !details.isEmpty()) {
            findTheMaximumSerial(details);
            for (GeneralJournalDetailsDTO journalDetails : details) {
                if (journalDetails.getCostCenterId() != null && journalDetails.getCostCenterId().getId() == -1) {
                    journalDetails.setCostCenterId(null);
                }
                if (journalDetails.getAdminUnitId() != null && journalDetails.getAdminUnitId().getId() == -1) {
                    journalDetails.setAdminUnitId(null);
                }
                fillSerial(journalDetails);
                GeneralJournalDetails generalJournalDetails = mapFromDTO(journalDetails, tobyUser);
                gjd = dao.updateEntity(generalJournalDetails);
                list.add(gjd);
            }
        }
        
        if (generalListDeleted != null && !generalListDeleted.isEmpty()) {
            for (GeneralJournalDetailsDTO journalDetails : generalListDeleted) {
                if (journalDetails.getId() != null) {
                    GeneralJournalDetails gjdEntity = mapFromDTO(journalDetails, tobyUser);
                    dao.deleteEntity(gjdEntity);
                }
            }
        }
        
        return returnListDTO(list);
    }
    
    @Override
    public Integer getMaxSerialByGeneral(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("journalId", id);
        Integer max = dao.findEntityByQuery("SELECT  MAX(d.serial) FROM GeneralJournalDetails d WHERE d.generalJournalId.id=:journalId", params);
        return max;
    }
    
    private void fillSerial(GeneralJournalDetailsDTO generalJournalDetails) {
        if (generalJournalDetails.getSerial() == null) {
            generalJournalDetails.setSerial(maxSerial);
            maxSerial++;
        }
    }
    
    private void findTheMaximumSerial(List<GeneralJournalDetailsDTO> generalJournalDetails) {
        maxSerial = detailsService.getMaxSerialByGeneral(generalJournalDetails.get(0).getGeneralJournalId().getId());
        
        if (maxSerial == null) {
            maxSerial = 1;
        } else {
            maxSerial = maxSerial + 1;
        }
    }
    
    private List<GeneralJournalDetailsDTO> returnListDTO(List<GeneralJournalDetails> list) {
        List<GeneralJournalDetailsDTO> dTOList = new ArrayList<>();
        for (GeneralJournalDetails entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }
    
    private GeneralJournalDetailsDTO mapTODTO(GeneralJournalDetails entity, Boolean needDetail) {
        GeneralJournalDetailsDTO dTO = initMapToDTO(entity);
        if (needDetail) {
            
        }
        return dTO;
    }
    
    private GeneralJournalDetailsDTO initMapToDTO(GeneralJournalDetails entity) {
        
        GeneralJournalDetailsDTO dTO = new GeneralJournalDetailsDTO();
        dTO.setId(entity.getId());
        
        dTO.setSerial(entity.getSerial());
        dTO.setDebitAmount(entity.getDebitAmount());
        dTO.setCreditamount(entity.getCreditamount());
        dTO.setDebitAmountLocal(entity.getDebitAmountLocal());
        dTO.setCreditamountLocal(entity.getCreditamountLocal());
        dTO.setDiscribtion(entity.getDiscribtion());
        dTO.setRate(entity.getRate());
        
        if (entity.getGlACCOUNTId() != null && entity.getGlACCOUNTId().getId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getGlACCOUNTId().getId());
            accountDTO.setName(entity.getGlACCOUNTId().getName());
            accountDTO.setAccNumber(entity.getGlACCOUNTId().getAccNumber());
            accountDTO.setLevelAcc(entity.getGlACCOUNTId().getLevelAcc());
            accountDTO.setStatus(entity.getGlACCOUNTId().getStatus());
            accountDTO.setShotCode(entity.getGlACCOUNTId().getShotCode());
            dTO.setGlACCOUNTId(accountDTO);
        }
        if (entity.getGlAssistantAccount() != null && entity.getGlAssistantAccount().getId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getGlAssistantAccount().getId());
            accountDTO.setName(entity.getGlAssistantAccount().getName());
            dTO.setGlAssistantAccount(accountDTO);
        }
        if (entity.getCostCenterId() != null && entity.getCostCenterId().getId() != null) {
            CostCenterDTO costCenterDTO = new CostCenterDTO();
            costCenterDTO.setId(entity.getCostCenterId().getId());
            costCenterDTO.setName(entity.getCostCenterId().getName());
            costCenterDTO.setCode(entity.getCostCenterId().getCode());
            dTO.setCostCenterId(costCenterDTO);
        }
        if (entity.getAdminUnitId() != null && entity.getAdminUnitId().getId() != null) {
            GlAdminUnitDTO glAdminUnitDTO = new GlAdminUnitDTO();
            glAdminUnitDTO.setId(entity.getAdminUnitId().getId());
            glAdminUnitDTO.setCode(entity.getAdminUnitId().getCode());
            glAdminUnitDTO.setName(entity.getAdminUnitId().getName());
            dTO.setAdminUnitId(glAdminUnitDTO);
        }
        if (entity.getGeneralJournalId() != null && entity.getGeneralJournalId().getId() != null) {
            GeneralJournalDTO generalJournalDTO = new GeneralJournalDTO();
            generalJournalDTO.setId(entity.getGeneralJournalId().getId());
            generalJournalDTO.setSerial(entity.getGeneralJournalId().getSerial());
            dTO.setGeneralJournalId(generalJournalDTO);
        }
        if (entity.getCurrencyId() != null && entity.getCurrencyId().getId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setName(entity.getCurrencyId().getName());
            currencyDTO.setSerial(entity.getCurrencyId().getSerial());
            dTO.setCurrencyId(currencyDTO);
        }
        
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        
        return dTO;
    }
    
    private GeneralJournalDetails mapFromDTO(GeneralJournalDetailsDTO dTO, TobyUser tobyUser) {
        GeneralJournalDetails entity = new GeneralJournalDetails();
        
        entity.setDebitAmount(dTO.getDebitAmount());
        entity.setCreditamount(dTO.getCreditamount());
        entity.setSerial(dTO.getSerial());
        entity.setDebitAmountLocal(dTO.getDebitAmountLocal());
        entity.setCreditamountLocal(dTO.getCreditamountLocal());
        entity.setDiscribtion(dTO.getDiscribtion());
        entity.setRate(dTO.getRate());
        
        if (dTO.getCurrencyId() != null && dTO.getCurrencyId().getId() != null) {
            Currency currency = new Currency();
            currency.setId(dTO.getCurrencyId().getId());
            entity.setCurrencyId(currency);
        }
        
        if (dTO.getGlACCOUNTId() != null && dTO.getGlACCOUNTId().getId() != null) {
            GlAccount account = new GlAccount();
            account.setId(dTO.getGlACCOUNTId().getId());
            entity.setGlACCOUNTId(account);
        }
        
        if (dTO.getGlAssistantAccount() != null && dTO.getGlAssistantAccount().getId() != null) {
            GlAccount account = new GlAccount();
            account.setId(dTO.getGlAssistantAccount().getId());
            entity.setGlAssistantAccount(account);
        }
        if (dTO.getCostCenterId() != null && dTO.getCostCenterId().getId() != null) {
            CostCenter costCenter = new CostCenter();
            costCenter.setId(dTO.getCostCenterId().getId());
            entity.setCostCenterId(costCenter);
        }
        if (dTO.getAdminUnitId() != null && dTO.getAdminUnitId().getId() != null) {
            GlAdminUnit glAdminUnit = new GlAdminUnit();
            glAdminUnit.setId(dTO.getAdminUnitId().getId());
            entity.setAdminUnitId(glAdminUnit);
        }
        if (dTO.getGeneralJournalId() != null && dTO.getGeneralJournalId().getId() != null) {
            GeneralJournal generalJournal = new GeneralJournal();
            generalJournal.setId(dTO.getGeneralJournalId().getId());
            entity.setGeneralJournalId(generalJournal);
        }
        
        if (dTO.getId()
                == null) {
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
}
