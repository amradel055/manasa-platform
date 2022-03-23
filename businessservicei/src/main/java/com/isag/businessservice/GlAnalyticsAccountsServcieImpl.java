/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.GlAnalyticsAccounts;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class GlAnalyticsAccountsServcieImpl implements GlAnalyticsAccountsServcie {

    @EJB
    private GenericDAO dao;

    @Override
    public GlAnalyticsAccounts addGlAnalyticsAccounts(GlAnalyticsAccounts newGlAn) {
        return dao.updateEntity(newGlAn);
    }

    @Override
    public GlAnalyticsAccounts update(GlAnalyticsAccounts glAnalyticsAccounts) {
        return dao.updateEntity(glAnalyticsAccounts);
    }

    @Override
    public List<GlAnalyticsAccounts> getALLGlAnalyticsAccountsByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<GlAnalyticsAccounts> glAnaAcc = dao.findListByQuery("SELECT gaa FROM GlAnalyticsAccounts gaa WHERE gaa.branchId.id=:branchId and gaa.type = 0", params);
        return glAnaAcc;
    }

    @Override
    public void delete(GlAnalyticsAccounts glAnalyticsAccounts) {
        dao.deleteEntity(glAnalyticsAccounts);
    }

    @Override
    public List<GlAnalyticsAccounts> getALLGlAnalyticsAccountsByCompany() {
        List<GlAnalyticsAccounts> glAnaAcc = dao.findListByQuery("SELECT gaa FROM GlAnalyticsAccounts gaa WHERE gaa.companyId.id = NULL");
        return glAnaAcc;
    }

    @Override
    public void delete(Integer branchId) {
        dao.executeDeleteQuery("DELETE FROM GlAnalyticsAccounts gaa WHERE gaa.branchId.id =" + branchId + "");
    }

    @Override
    public void saveGlAnalyticsAccounts(List<GlAnalyticsAccounts> newGlAn) {
        for (GlAnalyticsAccounts analyticsAccount : newGlAn) {
            dao.updateEntity(analyticsAccount);
        }
    }

    @Override
    public GlAnalyticsAccounts getSpecificGlAnalyticAccount(Integer branchId, String account) {
        Map<String, Object> params = new HashMap();
        params.put("account", account);
        params.put("branchId", branchId);
        GlAnalyticsAccounts glAnaAcc = dao.findEntityByQuery("SELECT gaa FROM GlAnalyticsAccounts gaa WHERE gaa.branchId.id = :branchId AND gaa.code = :account", params);
        return glAnaAcc;
    }

    @Override
    public List<GlAnalyticsAccounts> getALLGlAnalyticsAccountsForMonitoringByBranch(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<GlAnalyticsAccounts> glAnaAcc = dao.findListByQuery("SELECT gaa FROM GlAnalyticsAccounts gaa WHERE gaa.branchId.id=:branchId and gaa.type =:type", params);
        return glAnaAcc;
    }

    @Override
    public Long getCountsForMonitoringByBranch(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        Long count = dao.findEntityByQuery("SELECT COUNT(gaa.id) FROM GlAnalyticsAccounts gaa WHERE gaa.branchId.id=:branchId and gaa.type =:type", params);
        return count;
    }

    @Override
    public List<GlAnalyticsAccounts> getALLNullMonitoringAccounts() {
        List<GlAnalyticsAccounts> glAnaAcc = dao.findListByQuery("SELECT gaa FROM GlAnalyticsAccounts gaa WHERE gaa.branchId IS NULL and gaa.type =1");
        return glAnaAcc;
    }

    @Override
    public List<GlAnalyticsAccounts> getALLNullPurchaseAndSalesAccounts() {
        List<GlAnalyticsAccounts> glAnaAcc = dao.findListByQuery("SELECT gaa FROM GlAnalyticsAccounts gaa WHERE gaa.branchId IS NULL and gaa.type =2");
        return glAnaAcc;
    }

    @Override
    public boolean checkGlAnalyticsExist(String code) {
        Map<String, Object> params = new HashMap();
        params.put("code", code);
        List<GlAnalyticsAccounts> glAnalyticsAccountseList = dao.findEntityByQuery("SELECT gaa FROM GlAnalyticsAccounts gaa WHERE gaa.code LIKE CONCAT('%',:code ,'%')", params);
        if (glAnalyticsAccountseList != null && !glAnalyticsAccountseList.isEmpty()) {
            return false;
        }
        return true;
    }

}
