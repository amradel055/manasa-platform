/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.OrganizationSiteStatementSearchBean;
import com.isag.core.GenericDAO;

import com.isag.views.OrganizationSiteStatementView;
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
 * @author hhhh
 */
@Stateless
public class OrganizationSiteStatmentServiceReportImpl implements OrganizationSiteStatementServiceReport {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", organizationSiteStatementSearchBean.getBranchId());
        params.put("type", organizationSiteStatementSearchBean.getOrganizationType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId and RIV.organizationType != :type ");
        appendDate(organizationSiteStatementSearchBean, params, stringBuilder);
        appendOrganization(organizationSiteStatementSearchBean, params, stringBuilder);
        List<OrganizationSiteStatementView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendOrganization(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean, Map<String, Object> params, StringBuilder queryBuilder) {
        //appendQuery = new StringBuilder();

        if (organizationSiteStatementSearchBean.getFromorganizationName() != null) {
//            params.put("FromOrganization", organizationSiteStatementSearchBean.getFromorganizationName().getId());
//            queryBuilder.append(" AND RIV.organizationSiteId >= :FromOrganization ");
            params.put("FromOrganization", organizationSiteStatementSearchBean.getFromorganizationName().getCode());
            queryBuilder.append(" AND RIV.organizationCode >= :FromOrganization ");
        }

        if (organizationSiteStatementSearchBean.getToorganizationName() != null) {
//            params.put("ToOrganization", organizationSiteStatementSearchBean.getToorganizationName().getId());
//            queryBuilder.append(" AND RIV.organizationSiteId <= :ToOrganization ");
            params.put("ToOrganization", organizationSiteStatementSearchBean.getToorganizationName().getCode());
            queryBuilder.append(" AND RIV.organizationCode <= :ToOrganization ");
        }

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void appendDate(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean, Map<String, Object> params,
            StringBuilder queryBuilder) {
        //  appendQuery = new StringBuilder();

        if (organizationSiteStatementSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(organizationSiteStatementSearchBean.getDateFrom());
            queryBuilder.append(" AND RIV.date >= '").append(formatDateFrom).append("'");
        }

        if (organizationSiteStatementSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(organizationSiteStatementSearchBean.getDateTo());
            queryBuilder.append(" AND RIV.date <= '").append(formatDateTo).append("'");
        }
    }

    private void appendOpenningDate(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean,
            Map<String, Object> params, StringBuilder queryBuilder) {
        //  appendQuery = new StringBuilder();

        if (organizationSiteStatementSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(organizationSiteStatementSearchBean.getDateFrom());
            queryBuilder.append(" AND RIV.date < '").append(formatDateFrom).append("'");
        }

//        if (organizationSiteStatementSearchBean.getDateTo() != null) {
//            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(organizationSiteStatementSearchBean.getDateTo());
//            queryBuilder.append(" AND RIV.date <= '").append(formatDateTo).append("'");
//        }
    }

// الارصدة
    @Override
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementBalancesSearchBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean, Integer type) {
        dao.executeNativeQuery("select sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", organizationSiteStatementSearchBean.getBranchId());
        params.put("organizationType", organizationSiteStatementSearchBean.getOrganizationType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT SUM(RIV.exitt),SUM(RIV.adding),RIV.openningBalance ,RIV.organizationName,RIV.organizationSiteId,RIV.organizationCode,RIV.organizationCurrencyId ");
        if (type == 1) {
            stringBuilder.append("RIV.date ");
        }
        stringBuilder.append(" FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId ");
        appendOrganization(organizationSiteStatementSearchBean, params, stringBuilder);
        appendDate(organizationSiteStatementSearchBean, params, stringBuilder);
        if( organizationSiteStatementSearchBean.getOrganizationType() == 4){
            stringBuilder.append(" AND RIV.organizationType = :organizationType ");
        }else{
        stringBuilder.append(" AND RIV.organizationType != :organizationType ");
        }
        stringBuilder.append(" group by RIV.organizationSiteId,RIV.openningBalance ,RIV.organizationName,RIV.organizationSiteId,RIV.organizationCode,RIV.organizationCurrencyId");
        if (type == 1) {
            stringBuilder.append(" RIV.date,");
        }
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        List<OrganizationSiteStatementView> siteStatementViews = prepareList(res, type);
        return siteStatementViews;
    }

    @Override
    public Map<Integer, BigDecimal> getAllOrganizationSiteStatementOpeningBalancesSearchBean(OrganizationSiteStatementSearchBean organizationSiteStatementSearchBean) {
        dao.executeNativeQuery("select sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        Map<Integer, BigDecimal> openningBalance = new HashMap<>();
        if (organizationSiteStatementSearchBean.getDateFrom() != null) {
            params.put("branchId", organizationSiteStatementSearchBean.getBranchId());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT SUM(RIV.exitt),SUM(RIV.adding),RIV.openningBalance ,RIV.organizationSiteId "
                    + " FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId");
            appendOrganization(organizationSiteStatementSearchBean, params, stringBuilder);
            appendOpenningDate(organizationSiteStatementSearchBean, params, stringBuilder);
             if(organizationSiteStatementSearchBean.getOrganizationType() == 1){
           stringBuilder.append(" AND RIV.organizationType in (0,2)");
             }else if(organizationSiteStatementSearchBean.getOrganizationType() == 0){
           stringBuilder.append(" AND RIV.organizationType in ( 1 , 2 )");
             }else{
              params.put("organizationType", organizationSiteStatementSearchBean.getOrganizationType());   
              stringBuilder.append(" AND RIV.organizationType = :organizationType ");

             }
          
            stringBuilder.append(" group by RIV.organizationSiteId,RIV.openningBalance ,RIV.organizationSiteId ");

            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

            OrganizationSiteStatementView organizationSiteStatementView;
            for (Object[] object : res) {
                organizationSiteStatementView = new OrganizationSiteStatementView();
                organizationSiteStatementView.setExitt((BigDecimal) object[0]);
                organizationSiteStatementView.setAdding((BigDecimal) object[1]);
                organizationSiteStatementView.setOpenningBalance((BigDecimal) object[2]);
                organizationSiteStatementView.setOrganizationSiteId((Integer) object[3]);
                if (organizationSiteStatementSearchBean.getOrganizationType() == 1) {
                     openningBalance.put(organizationSiteStatementView.getOrganizationSiteId(), organizationSiteStatementView.getExitt().subtract(organizationSiteStatementView.getAdding()));
                } else {
                     openningBalance.put(organizationSiteStatementView.getOrganizationSiteId(), organizationSiteStatementView.getAdding().subtract(organizationSiteStatementView.getExitt()));

                }
            }
        }

        return openningBalance;
    }

    private List<OrganizationSiteStatementView> prepareList(List<Object[]> res, Integer type) {
        List<OrganizationSiteStatementView> list = new ArrayList<>();
        OrganizationSiteStatementView organizationSiteStatementView;
        for (Object[] object : res) {
            organizationSiteStatementView = new OrganizationSiteStatementView();
            organizationSiteStatementView.setExitt((BigDecimal) object[0]);
            organizationSiteStatementView.setAdding((BigDecimal) object[1]);
            organizationSiteStatementView.setOpenningBalance((BigDecimal) object[2]);
            organizationSiteStatementView.setOrganizationName((String) object[3]);
            organizationSiteStatementView.setOrganizationSiteId((Integer) object[4]);
            organizationSiteStatementView.setOrganizationCode((String) object[5]);
            organizationSiteStatementView.setOrganizationCurrencyId((Integer) object[6]);
            if (type == 1) {
                organizationSiteStatementView.setDate((Date) object[7]);
            }
            list.add(organizationSiteStatementView);
        }

        return list;
    }

}
