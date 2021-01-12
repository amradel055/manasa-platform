/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.InvDelegatorSalesByMainItemsGroupSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.SalesDelegateView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class InvDelegatorSalesByMainItemsGroupImpl implements InvDelegatorSalesByMainItemsGroupService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<SalesDelegateView> findAllSalesDelegate(InvDelegatorSalesByMainItemsGroupSearchBean invDelegatorSalesByMainItemsGroupSearchBean) {

        Map<String, Object> params = new HashMap<>();

        params.put("branchId", invDelegatorSalesByMainItemsGroupSearchBean.getBranchId());
        params.put("typeView", invDelegatorSalesByMainItemsGroupSearchBean.isTypeView());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT SDV.delegatorName , SUM(SDV.cash), SUM(SDV.postpone) ,SUM(SDV.documentarycredit) FROM SalesDelegateView SDV WHERE SDV.branchId = :branchId ");
        appendOrgSiteForQueryBuilder(stringBuilder, params, invDelegatorSalesByMainItemsGroupSearchBean);
        appendDelegateForQueryBuilder(stringBuilder, params, invDelegatorSalesByMainItemsGroupSearchBean);
        appendInventoryForQueryBuilder(stringBuilder, params, invDelegatorSalesByMainItemsGroupSearchBean);
        appendDateForQueryBuilder(stringBuilder, params, invDelegatorSalesByMainItemsGroupSearchBean);
        appendInvoceForQueryBuilder(stringBuilder, params, invDelegatorSalesByMainItemsGroupSearchBean);
        stringBuilder.append(" and SDV.typeView = :typeView ");
        stringBuilder.append(" group by SDV.delegatorName");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        List<SalesDelegateView> delegateViews = prepareList(res);
        return delegateViews;
    }

    private List<SalesDelegateView> prepareList(List<Object[]> res) {
        List<SalesDelegateView> list = new ArrayList<>();
        SalesDelegateView salesDelegateView;
        for (Object[] object : res) {
            salesDelegateView = new SalesDelegateView();
            salesDelegateView.setDelegatorName((String) object[0]);
            salesDelegateView.setCash((BigDecimal) object[1]);
            salesDelegateView.setPostpone((BigDecimal) object[2]);
            salesDelegateView.setDocumentarycredit((BigDecimal) object[3]);
            list.add(salesDelegateView);
        }

        return list;
    }

    private void appendOrgSiteForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, InvDelegatorSalesByMainItemsGroupSearchBean invDelegatorSalesByMainItemsGroupSearchBean) {
        if (invDelegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteFrom() != null && invDelegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteFrom().getId() > 0) {
            params.put("organizationSiteIdFrom", invDelegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteFrom().getId());
            queryBuilder.append(" and SDV.organizationSiteId >= :organizationSiteIdFrom ");
        }
        if (invDelegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteTo() != null && invDelegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteTo().getId() > 0) {
            params.put("organizationSiteIdTo", invDelegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteTo().getId());
            queryBuilder.append(" and SDV.organizationSiteId <= :organizationSiteIdTo");
        }

    }

    private void appendDelegateForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, InvDelegatorSalesByMainItemsGroupSearchBean invDelegatorSalesByMainItemsGroupSearchBean) {
        if (invDelegatorSalesByMainItemsGroupSearchBean.getDelegateCodeFrom() != null && invDelegatorSalesByMainItemsGroupSearchBean.getDelegateCodeFrom().getCode() != null) {
            params.put("delegatorCodeFrom", (invDelegatorSalesByMainItemsGroupSearchBean.getDelegateCodeFrom().getCode()));
            queryBuilder.append(" and SDV.delegatorCode >= :delegatorCodeFrom ");
        }
        if (invDelegatorSalesByMainItemsGroupSearchBean.getDelegateCodeTo() != null && invDelegatorSalesByMainItemsGroupSearchBean.getDelegateCodeTo().getCode() != null) {
            params.put("delegatorCodeTo", (invDelegatorSalesByMainItemsGroupSearchBean.getDelegateCodeTo().getCode()));
            queryBuilder.append(" and SDV.delegatorCode <= :delegatorCodeTo");
        }
    }

    private void appendInventoryForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, InvDelegatorSalesByMainItemsGroupSearchBean invDelegatorSalesByMainItemsGroupSearchBean) {
        if (invDelegatorSalesByMainItemsGroupSearchBean.getInventoryIdFrom() != null && invDelegatorSalesByMainItemsGroupSearchBean.getInventoryIdFrom().getId() > 0) {
            params.put("invInventoryIdFrom", invDelegatorSalesByMainItemsGroupSearchBean.getInventoryIdFrom().getId());
            queryBuilder.append(" and SDV.invInventoryId >= :invInventoryIdFrom ");
        }
        if (invDelegatorSalesByMainItemsGroupSearchBean.getInventoryIdTo() != null && invDelegatorSalesByMainItemsGroupSearchBean.getInventoryIdTo().getId() > 0) {
            params.put("invInventoryIdTo", invDelegatorSalesByMainItemsGroupSearchBean.getInventoryIdTo().getId());
            queryBuilder.append(" and SDV.invInventoryId <= :invInventoryIdTo");
        }
    }

    private void appendDateForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, InvDelegatorSalesByMainItemsGroupSearchBean invDelegatorSalesByMainItemsGroupSearchBean) {
        if (invDelegatorSalesByMainItemsGroupSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(invDelegatorSalesByMainItemsGroupSearchBean.getDateFrom());
            queryBuilder.append(" and SDV.date >= '").append(formatDateFrom).append("'");
        }

        if (invDelegatorSalesByMainItemsGroupSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(invDelegatorSalesByMainItemsGroupSearchBean.getDateTo());
            queryBuilder.append(" and SDV.date <= '").append(formatDateTo).append("'");
        }
    }

    private void appendInvoceForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, InvDelegatorSalesByMainItemsGroupSearchBean invDelegatorSalesByMainItemsGroupSearchBean) {
        if (invDelegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberFrom() != null && invDelegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberFrom() > 0) {
            params.put("serailFrom", invDelegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberFrom());
            queryBuilder.append(" and SDV.serail >= :serailFrom ");
        }
        if (invDelegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberTo() != null && invDelegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberTo() > 0) {
            params.put("serailTo", invDelegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberTo());
            queryBuilder.append(" and SDV.serail <= :serailTo");
        }
    }

}
