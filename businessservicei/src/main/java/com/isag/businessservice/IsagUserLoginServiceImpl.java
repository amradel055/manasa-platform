/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.IsagUserLoginBeanSearch;
import com.isag.core.GenericDAO;
import com.isag.entity.IsagUserLogin;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author WIN7
 */
@Stateless
public class IsagUserLoginServiceImpl implements IsagUserLoginService {

    @PersistenceContext(unitName = "ISAG_PU")
    private EntityManager em;

    @EJB
    private GenericDAO dao;

    @Override
    public IsagUserLogin addIsagUserLogin(IsagUserLogin isagUserLogin) {
        if (isagUserLogin.getId() != null) {
            dao.updateEntity(isagUserLogin);
        } else {
            dao.saveEntity(isagUserLogin);
        }
        return isagUserLogin;
    }

    @Override
    public void deletegetIsagUserLogin(IsagUserLogin isagUserLogin) {
        dao.deleteEntity(isagUserLogin);
    }

    @Override
    public List<IsagUserLogin> getALLgetIsagUserLoginByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(IsagUserLogin.class, companyId);
    }

    @Override
    public List<IsagUserLogin> getALLgetIsagUserLoginByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        return dao.findListByQuery("SELECT i FROM IsagUserLogin i WHERE i.branchId.id = :branchId", params);
    }

    @Override
    public List<IsagUserLogin> getALLgetIsagUserLoginByBranchIdAndUserCodeAndDate(IsagUserLoginBeanSearch isagUserLoginBeanSearch) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", isagUserLoginBeanSearch.getBranchId());

        StringBuilder queryBuilder = new StringBuilder();

        appendIsagUserLoginDateQueryBuilder(queryBuilder, isagUserLoginBeanSearch);

        appendIsagUserLoginCodeQueryBuilder(queryBuilder, params, isagUserLoginBeanSearch);
        String query = "SELECT i FROM IsagUserLogin i WHERE i.branchId.id = :branchId " + queryBuilder.toString();
        return dao.findListByQuery(query, params);
    }

    private void appendIsagUserLoginDateQueryBuilder(StringBuilder queryBuilder, IsagUserLoginBeanSearch isagUserLoginBeanSearch) {

        if (isagUserLoginBeanSearch.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(isagUserLoginBeanSearch.getDateFrom());
            queryBuilder.append(" and i.dateLogin >= '").append(formatDateFrom).append("'");
        }

        if (isagUserLoginBeanSearch.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(isagUserLoginBeanSearch.getDateTo());
            queryBuilder.append(" and i.dateLogin <= '").append(formatDateTo).append(" 23:59:59'");
        }
    }

    private void appendIsagUserLoginCodeQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, IsagUserLoginBeanSearch isagUserLoginBeanSearch) {
        if (isagUserLoginBeanSearch.getUserCodeFrom() != null && isagUserLoginBeanSearch.getUserCodeFrom() > 0) {
            params.put("codeFrom", isagUserLoginBeanSearch.getUserCodeFrom());
            queryBuilder.append(" and i.userId.id >= :codeFrom ");
        }
        if (isagUserLoginBeanSearch.getUserCodeTo() != null && isagUserLoginBeanSearch.getUserCodeTo() > 0) {
            params.put("codeTo", isagUserLoginBeanSearch.getUserCodeTo());
            queryBuilder.append(" and i.userId.id <= :codeTo");
        }
    }

    @Override
    public IsagUserLogin getIsagUserLogin(Integer isagUserLoginId) {
        return dao.findEntityById(IsagUserLogin.class, isagUserLoginId);
    }

    @Override
    public List<IsagUserLogin> findIsagUserLoginByUserId(Integer branchId, Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("userId", userId);
        return dao.findListByQuery("SELECT i FROM IsagUserLogin i WHERE i.branchId.id = :branchId"
                + " and i.userId.id = :userId", params);
    }

    @Override
    public boolean checkUserLogged(Integer userId, Integer branchId) {
        int state = (int) em.createNamedQuery("IsagUserLogin.checkUserlogged")
                .setParameter("userId", userId)
                .setParameter("branchId", branchId)
                .setMaxResults(1)
                .getSingleResult();
        if (state == 1) {
            return true;//logged
        } else {
            return false;//not logged
        }
    }
}
