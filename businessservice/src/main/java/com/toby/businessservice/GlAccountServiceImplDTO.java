/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.GlAccountDTO;
import com.toby.entity.Currency;
import com.toby.entity.GeneralBudgetGuide;
import com.toby.entity.GlAccount;
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
public class GlAccountServiceImplDTO implements GlAccountServiceDTO {

    @EJB
    private GenericDAO dao;

     @Override
    public List<GlAccountDTO> getBranchGLAccountsActiveWithException(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<GlAccount> glAccount = new ArrayList<>();
        glAccount = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.id NOT IN (SELECT glv.accountId FROM GlAccountView glv WHERE glv.branchId=:branchId) AND acc.branchId.id=:branchId AND acc.status=1 AND acc.type = 1 ORDER BY acc.accNumber ASC ", params);
        return returnListDTO(glAccount);
    }
    
    @Override
    public List<GlAccountDTO> getBranchGLAccountsActive(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<GlAccount> glAccount = new ArrayList<>();
        glAccount = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1  ORDER BY acc.accNumber ASC ", params);
        return returnListDTO(glAccount);
    }
    
     @Override
    public GlAccountDTO findGlAccountDTO(Integer glAccount) {
        if (glAccount != null) {
            GlAccount account = new GlAccount();
            account = dao.findEntityById(GlAccount.class, glAccount);
           return mapTODTO(account, false);
            
        } else {
            return null;
        }
    }
    
     @Override
    public GlAccount findGlAccount(Integer glAccount) {
        if (glAccount != null) {
            GlAccount account = new GlAccount();
            account = dao.findEntityById(GlAccount.class, glAccount);
           return account;
            
        } else {
            return null;
        }
    }
    
    private List<GlAccountDTO> returnListDTO(List<GlAccount> list) {
        List<GlAccountDTO> dTOList = new ArrayList<>();
        for (GlAccount entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private GlAccountDTO mapTODTO(GlAccount entity, Boolean needDetail) {
        GlAccountDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private GlAccountDTO initMapToDTO(GlAccount entity) {

        GlAccountDTO dTO = new GlAccountDTO();
        dTO.setId(entity.getId());
        if (entity.getParentAcc() != null) {
            GlAccountDTO glAccountDTO = new GlAccountDTO();
            glAccountDTO.setId(entity.getParentAcc().getId());
            dTO.setParentAcc(glAccountDTO);
        }

        dTO.setAccNumber(entity.getAccNumber());
        dTO.setName(entity.getName());
        dTO.setLevelAcc(entity.getLevelAcc());
        dTO.setType(entity.getType());
        dTO.setShotCode(entity.getShotCode());
        dTO.setAccGroup(entity.getAccGroup());
        dTO.setStatus(entity.getStatus());
        dTO.setCostCenter(entity.getCostCenter());
        dTO.setAdministrativeUnit(entity.getAdministrativeUnit());
        dTO.setAssistantAcc(entity.getAssistantAcc());
        dTO.setAccClass(entity.getAccClass());
        dTO.setGeneralBudgetId(entity.getGeneralBudgetId() != null ? entity.getGeneralBudgetId().getId() : null);
        dTO.setGeneralBudgetIdCredit(entity.getGeneralBudgetIdCredit() != null ? entity.getGeneralBudgetIdCredit().getId() : null);
        dTO.setGeneralBudgetIdDebit(entity.getGeneralBudgetIdDebit() != null ? entity.getGeneralBudgetIdDebit().getId() : null);
        dTO.setCurrencyId(entity.getCurrencyId() != null ? entity.getCurrencyId().getId() : null);

        dTO.setStatus(entity.getStatus());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private GlAccount mapFromDTO(GlAccountDTO dTO, TobyUser tobyUser) {
        GlAccount entity = new GlAccount();

        if (dTO.getParentAccId() != null) {
            GlAccount parent = new GlAccount();
            parent.setId(dTO.getParentAccId());
            entity.setParentAcc(parent);
        }

        if (dTO.getGeneralBudgetId() != null) {
            GeneralBudgetGuide parent = new GeneralBudgetGuide();
            parent.setId(dTO.getGeneralBudgetId());
            entity.setGeneralBudgetId(parent);
        }

        if (dTO.getGeneralBudgetIdCredit() != null) {
            GeneralBudgetGuide parent = new GeneralBudgetGuide();
            parent.setId(dTO.getGeneralBudgetIdCredit());
            entity.setGeneralBudgetIdCredit(parent);
        }

        if (dTO.getGeneralBudgetIdDebit() != null) {
            GeneralBudgetGuide parent = new GeneralBudgetGuide();
            parent.setId(dTO.getGeneralBudgetIdDebit());
            entity.setGeneralBudgetIdDebit(parent);
        }

        if (dTO.getCurrencyId() != null) {
            Currency parent = new Currency();
            parent.setId(dTO.getCurrencyId());
            entity.setCurrencyId(parent);
        }

        if (dTO.getParentAccId() != null) {
            GlAccount parent = new GlAccount();
            parent.setId(dTO.getParentAccId());
            entity.setParentAcc(parent);
        }
        entity.setAccNumber(dTO.getAccNumber());
        entity.setName(dTO.getName());
        entity.setLevelAcc(dTO.getLevelAcc());
        entity.setType(dTO.getType());
        entity.setShotCode(dTO.getShotCode());
        entity.setAccGroup(entity.getAccGroup());
        entity.setStatus(dTO.getStatus());
        entity.setCostCenter(dTO.getCostCenter());
        entity.setAdministrativeUnit(dTO.getAdministrativeUnit());
        entity.setAssistantAcc(dTO.getAssistantAcc());
        entity.setAccClass(dTO.getAccClass());

        entity.setStatus(dTO.getStatus());
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
