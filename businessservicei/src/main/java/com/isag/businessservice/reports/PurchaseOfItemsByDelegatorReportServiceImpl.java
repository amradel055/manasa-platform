/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.isag.businessservice.reports.searchBean.PurchaseOfItemsByDelegatorSearchBean;
import com.isag.businessservice.reports.searchBean.PurchaseOfItemsBySuppliersSearchBean;
import com.isag.core.GenericDAO;
import com.isag.views.ItemSalesView;
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
public class PurchaseOfItemsByDelegatorReportServiceImpl implements PurchaseOfItemsByDelegatorReportService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ItemSalesView> getAllPurchaseOfItemsByDelegatorSearchBean(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", purchaseOfItemsByDelegatorSearchBean.getType());
        params.put("branchId", purchaseOfItemsByDelegatorSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT isv FROM ItemSalesView isv WHERE isv.branchId = :branchId AND isv.type = :type ");

        stringBuilder.append(appendDate(purchaseOfItemsByDelegatorSearchBean, params));
        stringBuilder.append(appendDelegator(purchaseOfItemsByDelegatorSearchBean, params));
        stringBuilder.append(appendSerial(purchaseOfItemsByDelegatorSearchBean, params));
        stringBuilder.append(appendInventory(purchaseOfItemsByDelegatorSearchBean, params));
        stringBuilder.append(appendItem(purchaseOfItemsByDelegatorSearchBean, params));
        stringBuilder.append(appendPaymentType(purchaseOfItemsByDelegatorSearchBean, params));

        List<ItemSalesView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private String appendDate(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (purchaseOfItemsByDelegatorSearchBean.getDateFrom() != null) {
            params.put("dateFrom", purchaseOfItemsByDelegatorSearchBean.getDateFrom());
            appendQuery.append(" AND isv.date >= :dateFrom");
        }

        if (purchaseOfItemsByDelegatorSearchBean.getDateTo() != null) {
            params.put("dateTo", purchaseOfItemsByDelegatorSearchBean.getDateTo());
            appendQuery.append(" AND isv.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendDelegator(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (purchaseOfItemsByDelegatorSearchBean.getFromDelegatorName() != null) {
            params.put("fromDelegator", purchaseOfItemsByDelegatorSearchBean.getFromDelegatorName().getId());
            appendQuery.append(" AND isv.delegatorId >= :fromDelegator");
        }

        if (purchaseOfItemsByDelegatorSearchBean.getToDelegatorName() != null) {
            params.put("toDelegator", purchaseOfItemsByDelegatorSearchBean.getToDelegatorName().getId());
            appendQuery.append(" AND isv.delegatorId <= :toDelegator");
        }

        return appendQuery.toString();
    }

    private String appendItem(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (purchaseOfItemsByDelegatorSearchBean.getFromItemName() != null) {
            params.put("FromItem", purchaseOfItemsByDelegatorSearchBean.getFromItemName().getCode());
            appendQuery.append(" AND isv.itemCode >= :FromItem");
        }

        if (purchaseOfItemsByDelegatorSearchBean.getToItemName() != null) {
            params.put("ToItem", purchaseOfItemsByDelegatorSearchBean.getToItemName().getCode());
            appendQuery.append(" AND isv.itemCode <= :ToItem");
        }

        return appendQuery.toString();
    }

    private String appendInventory(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (purchaseOfItemsByDelegatorSearchBean.getFromInventoryName() != null) {
            params.put("FromInventory", purchaseOfItemsByDelegatorSearchBean.getFromInventoryName().getId());
            appendQuery.append(" AND isv.inventoryId >= :FromInventory");
        }

        if (purchaseOfItemsByDelegatorSearchBean.getToInventoryName() != null) {
            params.put("ToInventory", purchaseOfItemsByDelegatorSearchBean.getToInventoryName().getId());
            appendQuery.append(" AND isv.inventoryId <= :ToInventory");
        }

        return appendQuery.toString();
    }

    private String appendSerial(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (purchaseOfItemsByDelegatorSearchBean.getSalesInvoiceFrom() != null) {
            params.put("FromSerial", purchaseOfItemsByDelegatorSearchBean.getSalesInvoiceFrom().getSerial());
            appendQuery.append(" AND isv.serial >= :FromSerial ");
        }

        if (purchaseOfItemsByDelegatorSearchBean.getSalesInvoiceTo() != null) {
            params.put("Toserial", purchaseOfItemsByDelegatorSearchBean.getSalesInvoiceTo().getSerial());
            appendQuery.append(" AND isv.serial <= :Toserial ");
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String appendPaymentType(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (purchaseOfItemsByDelegatorSearchBean.getFrompaymentType() != null && purchaseOfItemsByDelegatorSearchBean.getFrompaymentType() >= 0) {
            params.put("frompaymentType", purchaseOfItemsByDelegatorSearchBean.getFrompaymentType());
            appendQuery.append(" AND isv.paymentType >= :frompaymentType");
        }

        if (purchaseOfItemsByDelegatorSearchBean.getTopaymentType() != null && purchaseOfItemsByDelegatorSearchBean.getTopaymentType() >= 0) {
            params.put("topaymentType", purchaseOfItemsByDelegatorSearchBean.getTopaymentType());
            appendQuery.append(" AND isv.paymentType <= :topaymentType");
        }
        return appendQuery.toString();
    }

}
