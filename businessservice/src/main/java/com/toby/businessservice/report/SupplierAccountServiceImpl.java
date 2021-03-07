/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.util.ArrayList;

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
public class SupplierAccountServiceImpl implements SupplierAccountService {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBean(SupplierAccountSearchBean supplierAccountSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", supplierAccountSearchBean.getBranchId());
//        params.put("type", supplierAccountSearchBean.getOrganizationType());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId and   ");

        int type = supplierAccountSearchBean.getOrganizationType();
        if (type == 0) {
            stringBuilder.append("(RIV.organizationType = 0 or RIV.organizationType = 2)");
        } else if (type == 1) {
            stringBuilder.append(" (RIV.organizationType = 1 or RIV.organizationType = 2)");
        } else if (type == 4) {
            stringBuilder.append(" RIV.organizationType = 4");
        }

        appendDate(supplierAccountSearchBean, stringBuilder, params);
        appendOrganization(supplierAccountSearchBean, stringBuilder, params);
        stringBuilder.append(" order by  RIV.date");

        List<OrganizationSiteStatementView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }
    
    @Override
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBeanById(SupplierAccountSearchBean supplierAccountSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", supplierAccountSearchBean.getBranchId());
//        params.put("type", supplierAccountSearchBean.getOrganizationType());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId and   ");

        int type = supplierAccountSearchBean.getOrganizationType();
        if (type == 0) {
            stringBuilder.append("(RIV.organizationType = 0 or RIV.organizationType = 2)");
        } else if (type == 1) {
            stringBuilder.append(" (RIV.organizationType = 1 or RIV.organizationType = 2)");
        } else if (type == 4) {
            stringBuilder.append(" RIV.organizationType = 4");
        }

        appendDate(supplierAccountSearchBean, stringBuilder, params);
        if (supplierAccountSearchBean.getFromorganizationNameDTO()!= null) {
            params.put("FromOrganization", supplierAccountSearchBean.getFromorganizationNameDTO().getId());
            stringBuilder.append(" AND RIV.organizationSiteId = :FromOrganization ");
        }
        stringBuilder.append(" order by  RIV.date");

        List<OrganizationSiteStatementView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendOrganization(SupplierAccountSearchBean supplierAccountSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (supplierAccountSearchBean.getFromorganizationNameDTO()!= null) {
            params.put("FromOrganization", supplierAccountSearchBean.getFromorganizationNameDTO().getCode());
            stringBuilder.append(" AND RIV.organizationCode >= :FromOrganization ");
        }

        if (supplierAccountSearchBean.getFromorganizationNameDTO() != null) {
            params.put("ToOrganization", supplierAccountSearchBean.getFromorganizationNameDTO().getCode());
            stringBuilder.append(" AND RIV.organizationCode <= :ToOrganization ");
        }

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void appendDate(SupplierAccountSearchBean supplierAccountSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (supplierAccountSearchBean.getDateFrom() != null) {
            params.put("fromDate", supplierAccountSearchBean.getDateFrom());
            stringBuilder.append(" AND RIV.date >= :fromDate");
        }

        if (supplierAccountSearchBean.getDateTo() != null) {
            params.put("toDate", supplierAccountSearchBean.getDateTo());
            stringBuilder.append(" AND RIV.date <= :toDate");
        }
    }

    @Override
    public List<OrganizationSiteStatementView> getAllOpeningBalanceBeforeBalanceBySearchBean(SupplierAccountSearchBean supplierAccountSearchBean) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", supplierAccountSearchBean.getBranchId());
        params.put("type", supplierAccountSearchBean.getOrganizationType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV.organizationSiteId , (SUM(RIV.adding) - SUM(RIV.exitt)) FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId and RIV.organizationType != :type ");
        appendOrganization(supplierAccountSearchBean, stringBuilder, params);
        appendDateBefore(supplierAccountSearchBean, stringBuilder, params);
        stringBuilder.append(" group by RIV.organizationSiteId");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        List<OrganizationSiteStatementView> statementViews = prepareList(res);
        return statementViews;
    }

    private List<OrganizationSiteStatementView> prepareList(List<Object[]> res) {
        List<OrganizationSiteStatementView> list = new ArrayList<>();
        OrganizationSiteStatementView siteStatementView;
        for (Object[] object : res) {
            siteStatementView = new OrganizationSiteStatementView();
            siteStatementView.setOrganizationSiteId((Integer) object[0]);
            siteStatementView.setOpenningBalance((BigDecimal) object[1]);

            list.add(siteStatementView);
        }

        return list;
    }

    private void appendDateBefore(SupplierAccountSearchBean supplierAccountSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (supplierAccountSearchBean.getDateFrom() != null) {
            params.put("fromDate", supplierAccountSearchBean.getDateFrom());
            stringBuilder.append(" AND RIV.date < :fromDate");
        }
    }
    
    public BigDecimal getAccountForOrganizationSite(Integer organizationSiteId) {
        Map<String, Object> params = new HashMap<>();

        params.put("organizationSiteId", organizationSiteId);
//        params.put("type", supplierAccountSearchBean.getOrganizationType());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT sum(RIV.exitt) - sum(RIV.adding) FROM OrganizationSiteStatementView RIV WHERE RIV.organizationSiteId = :organizationSiteId  ");

        List<BigDecimal> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details.get(0);
    }
}
