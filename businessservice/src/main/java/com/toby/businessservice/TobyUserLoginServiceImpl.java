/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.TobyUserLoginBeanSearch;
import com.toby.core.GenericDAO;
import com.toby.entity.TobyUserLogin;
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
public class TobyUserLoginServiceImpl implements TobyUserLoginService {

    @PersistenceContext(unitName = "TOBY_PU")
    private EntityManager em;

    @EJB
    private GenericDAO dao;

    @Override
    public TobyUserLogin addTobyUserLogin(TobyUserLogin tobyUserLogin) {
        if (tobyUserLogin.getId() != null) {
            dao.updateEntity(tobyUserLogin);
        } else {
            dao.saveEntity(tobyUserLogin);
        }
        return tobyUserLogin;
    }

    @Override
    public void deletegetTobyUserLogin(TobyUserLogin tobyUserLogin) {
        dao.deleteEntity(tobyUserLogin);
    }

    @Override
    public List<TobyUserLogin> getALLgetTobyUserLoginByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(TobyUserLogin.class, companyId);
    }

    @Override
    public List<TobyUserLogin> getALLgetTobyUserLoginByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        return dao.findListByQuery("SELECT i FROM TobyUserLogin i WHERE i.branchId.id = :branchId", params);
    }

    @Override
    public List<TobyUserLogin> getALLgetTobyUserLoginByBranchIdAndUserCodeAndDate(TobyUserLoginBeanSearch tobyUserLoginBeanSearch) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUserLoginBeanSearch.getBranchId());

        StringBuilder queryBuilder = new StringBuilder();

        appendTobyUserLoginDateQueryBuilder(queryBuilder, tobyUserLoginBeanSearch);

        appendTobyUserLoginCodeQueryBuilder(queryBuilder, params, tobyUserLoginBeanSearch);
        String query = "SELECT i FROM TobyUserLogin i WHERE i.branchId.id = :branchId " + queryBuilder.toString();
        return dao.findListByQuery(query, params);
    }

    private void appendTobyUserLoginDateQueryBuilder(StringBuilder queryBuilder, TobyUserLoginBeanSearch tobyUserLoginBeanSearch) {

        if (tobyUserLoginBeanSearch.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(tobyUserLoginBeanSearch.getDateFrom());
            queryBuilder.append(" and i.dateLogin >= '").append(formatDateFrom).append("'");
        }

        if (tobyUserLoginBeanSearch.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(tobyUserLoginBeanSearch.getDateTo());
            queryBuilder.append(" and i.dateLogin <= '").append(formatDateTo).append(" 23:59:59'");
        }
    }

    private void appendTobyUserLoginCodeQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, TobyUserLoginBeanSearch tobyUserLoginBeanSearch) {
        if (tobyUserLoginBeanSearch.getUserCodeFrom() != null && tobyUserLoginBeanSearch.getUserCodeFrom() > 0) {
            params.put("codeFrom", tobyUserLoginBeanSearch.getUserCodeFrom());
            queryBuilder.append(" and i.userId.id >= :codeFrom ");
        }
        if (tobyUserLoginBeanSearch.getUserCodeTo() != null && tobyUserLoginBeanSearch.getUserCodeTo() > 0) {
            params.put("codeTo", tobyUserLoginBeanSearch.getUserCodeTo());
            queryBuilder.append(" and i.userId.id <= :codeTo");
        }
    }

    @Override
    public TobyUserLogin getTobyUserLogin(Integer tobyUserLoginId) {
        return dao.findEntityById(TobyUserLogin.class, tobyUserLoginId);
    }

    @Override
    public List<TobyUserLogin> findTobyUserLoginByUserId(Integer branchId, Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("userId", userId);
        return dao.findListByQuery("SELECT i FROM TobyUserLogin i WHERE i.branchId.id = :branchId"
                + " and i.userId.id = :userId", params);
    }

    @Override
    public boolean checkUserLogged(Integer userId, Integer branchId) {
        int state = (int) em.createNamedQuery("TobyUserLogin.checkUserlogged")
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
