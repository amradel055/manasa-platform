/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GlAccountDTO;
import com.isag.entity.Currency;
import com.isag.entity.GeneralBudgetGuide;
import com.isag.entity.GlAccount;
import com.isag.entity.IsagUser;
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
        //   glAccount = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.id NOT IN (SELECT glv.accountId FROM GlAccountView glv WHERE glv.branchId=:branchId) AND acc.branchId.id=:branchId AND acc.status=1 AND acc.type = 1 ORDER BY acc.accNumber ASC ", params);
        glAccount = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.status=1 AND acc.type = 1 ORDER BY acc.accNumber ASC ", params);
        return returnListDTO(glAccount);
    }
/**
 
 اعدادات الحسابات
بحث : 
الحساب
رقم حساب النقدية	
خزينة معرض زايد 1202003

 
رقم حساب المخازن	
بضاعة اول المدة  1213001

 
رقم حساب المبيعات	
مبيعات نقدي و اجله 4101001

 
رقم حساب مردودات المبيعات	
مردوادات المبيعات 3101004

 
رقم حساب المشتريات	
المشتريات بغرض الببع 3101001

 
رقم حساب مردودات المشتريات	
مردودات المشتريات 4101003

 
رقم حساب التالف والفاقد	
مردوادات المبيعات 3101004

 
رقم ضبط المخزون فائض	
مردوادات المبيعات 3101004

 
رقم ضبط المخزون عجز	
مردوادات المبيعات 3101004

 
رقم حساب تكلفة البضاعة المباعة	
بضاعة اول المدة  1213001

 
رقم حساب العينات والهدايا	
مصروفات اخري  3408016

 
رقم حساب التكاليف الاضافية	
مصروفات اخري  3408016

 
رقم حساب الصرف	
مردوادات المبيعات 3101004

 
رقم حساب الشبكة	
بنك مصر ( فيزا ) 1201001

 
حساب ضريبة المبيعات	
ض.ق.م للمبيعات  2113001

 
رقم حساب مبيعات التقسيط	
مبيعات نقدي و اجله 4101001

 

 */
    @Override
    public GlAccountDTO getGlAccountForBank(Integer branchId, Integer type) {
//        type 0 -> BANK  ;  1 -> Customer   ; 2 -> Supplier ; 3 -> invPurchase ; 4 -> invSales ; 5 -> FixedAsset ;
//          6 -> glExpenses ; 7 -> مردودات المشتريات; 8 -> مردودات المبيعات
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        if (type == 0) {
            params.put("accNumber", 1201001);
        } else if (type == 1) {
            params.put("accNumber", 1202001);
        } else if (type == 2) {
            params.put("accNumber", 2102001);
        }else if (type == 3) {
            params.put("accNumber", 3101001);
        }else if (type == 4) {
            params.put("accNumber", 4101001);
        }else if (type == 5) {
            params.put("accNumber", 1101001);
        }else if (type == 6) {
            params.put("accNumber", 3101001);
        }else if (type == 7) {
            params.put("accNumber", 4101002);
        }else if (type == 8) {
            params.put("accNumber", 3101002);
        }
        List<GlAccount> glAccountList = new ArrayList<>();
        glAccountList = dao.findListByQuery("SELECT acc FROM GlAccount acc WHERE acc.branchId.id=:branchId AND acc.accNumber = :accNumber  ", params);
        if (glAccountList != null && !glAccountList.isEmpty()) {
            return mapTODTO(glAccountList.get(0), Boolean.FALSE);
        }
        return null;
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

    private GlAccount mapFromDTO(GlAccountDTO dTO, IsagUser isagUser) {
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
}
