/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.Branch;
import com.isag.entity.DataDictionary;
import com.isag.entity.GlAnalyticsAccounts;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class DDServiceImpl implements DDService {

    @EJB
    private GenericDAO dao;

    @EJB
    private GlAnalyticsAccountsServcie analyticsAccountsServcie;

    @Override
    public Map<String, String> getDDByCompanyAndLanguage(Integer lang_id, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lang", lang_id);
        List<DataDictionary> DDs;
        if (companyId == null) {
            DDs = dao.findListByQuery("SELECT dd FROM DataDictionary dd WHERE dd.lang.id=:lang AND dd.companyId IS NULL", params);
        } else {
            params.put("companyId", companyId);
            DDs = dao.findListByQuery("SELECT dd FROM DataDictionary dd WHERE dd.lang.id=:lang AND dd.companyId.id = :companyId", params);
        }
        Map<String, String> userDDs = new HashMap<>();
        for (DataDictionary DD : DDs) {
            userDDs.put(DD.getPropertyKey(), DD.getPropertyValue());
        }
        return userDDs;
    }

    @Override
    public List<DataDictionary> getDefaultDDsByLanguage(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("lang", id);
        List<DataDictionary> DDs = dao.findListByQuery("SELECT dd FROM DataDictionary dd WHERE dd.lang.id=:lang AND dd.companyId IS NULL", params);
        return DDs;
    }

    @Override
    public DataDictionary addDataDictionary(DataDictionary newDD) {
        return dao.updateEntity(newDD);
    }

    @Override
    public DataDictionary update(DataDictionary dataDictionary) {
        return dao.updateEntity(dataDictionary);
    }

    @Override
    public List<DataDictionary> getALLDataDictionaryByCompanyId(Integer id) {

        return dao.findEntityListByCompanyId(DataDictionary.class, id);
    }

    @Override
    public void saveAll(List<DataDictionary> dataDictionaryList) {
        for (DataDictionary x : dataDictionaryList) {
            dao.saveEntity(x);
        }
    }

    @Override
    public void saveAllWithGlAnalytics(List<DataDictionary> dataDictionaryList, List<GlAnalyticsAccounts> analyticsAccountsList) {
        for (DataDictionary x : dataDictionaryList) {
            dao.saveEntity(x);
        }
        for (GlAnalyticsAccounts gaa : analyticsAccountsList) {
            analyticsAccountsServcie.addGlAnalyticsAccounts(gaa);
        }

    }

}
