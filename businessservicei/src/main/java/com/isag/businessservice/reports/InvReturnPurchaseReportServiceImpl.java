/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.InvReturnPurchaseSearchBean;
import com.isag.core.GenericDAO;
import com.isag.views.ReturnInvoiceView;
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
public class InvReturnPurchaseReportServiceImpl implements InvReturnPurchaseReportService {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<ReturnInvoiceView> getAllReturnInvoiceViewByInvReturnPurchaseSearchBean(InvReturnPurchaseSearchBean invReturnPurchaseSearchBean) {

        Map<String, Object> params = new HashMap<>();
        params.put("type", invReturnPurchaseSearchBean.getType());
        params.put("branchId", invReturnPurchaseSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM ReturnInvoiceView RIV WHERE RIV.branchId = :branchId AND RIV.type = :type ");

        stringBuilder.append(appendDate(invReturnPurchaseSearchBean, params));
        stringBuilder.append(appendSupplier(invReturnPurchaseSearchBean, params));
        stringBuilder.append(appendInventory(invReturnPurchaseSearchBean, params));
        stringBuilder.append(appendReturnInvoice(invReturnPurchaseSearchBean, params));
        stringBuilder.append(appendPaymentType(invReturnPurchaseSearchBean, params));

        List<ReturnInvoiceView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private String appendDate(InvReturnPurchaseSearchBean invReturnPurchaseSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (invReturnPurchaseSearchBean.getDateFrom() != null) {
            params.put("dateFrom", invReturnPurchaseSearchBean.getDateFrom());
            appendQuery.append(" AND RIV.date >= :dateFrom");
        }

        if (invReturnPurchaseSearchBean.getDateTo() != null) {
            params.put("dateTo", invReturnPurchaseSearchBean.getDateTo());
            appendQuery.append(" AND RIV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendSupplier(InvReturnPurchaseSearchBean invReturnPurchaseSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (invReturnPurchaseSearchBean.getSuplierFrom() != null) {
            params.put("supplierCodeFrom", invReturnPurchaseSearchBean.getSuplierFrom().getCode());
            appendQuery.append(" AND RIV.supplierCode >= :supplierCodeFrom");
        }

        if (invReturnPurchaseSearchBean.getSuplierTo() != null) {
            params.put("supplierCodeTo", invReturnPurchaseSearchBean.getSuplierTo().getCode());
            appendQuery.append(" AND RIV.supplierCode <= :supplierCodeTo");
        }

        return appendQuery.toString();
    }

    private String appendInventory(InvReturnPurchaseSearchBean invReturnPurchaseSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (invReturnPurchaseSearchBean.getInventoryFrom() != null) {
            params.put("inventoryCodeFrom", invReturnPurchaseSearchBean.getInventoryFrom().getCode());
            appendQuery.append(" AND RIV.inventoryCode >= :inventoryCodeFrom");
        }

        if (invReturnPurchaseSearchBean.getInventoryTo() != null) {
            params.put("inventoryCodeTo", invReturnPurchaseSearchBean.getInventoryTo().getCode());
            appendQuery.append(" AND RIV.inventoryCode <= :inventoryCodeTo");
        }

        return appendQuery.toString();
    }

    private String appendReturnInvoice(InvReturnPurchaseSearchBean invReturnPurchaseSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (invReturnPurchaseSearchBean.getInvReturnPurchaseFrom() != null) {
            params.put("serialFrom", invReturnPurchaseSearchBean.getInvReturnPurchaseFrom().getSerial());
            appendQuery.append(" AND RIV.serial >= :serialFrom");
        }

        if (invReturnPurchaseSearchBean.getInvReturnPurchaseTo() != null) {
            params.put("serialTo", invReturnPurchaseSearchBean.getInvReturnPurchaseTo().getSerial());
            appendQuery.append(" AND RIV.serial <= :serialTo");
        }

        return appendQuery.toString();
    }

    private String appendPaymentType(InvReturnPurchaseSearchBean invReturnPurchaseSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (invReturnPurchaseSearchBean.getPaymentTypeFrom() != null) {
            params.put("paymentTypeFrom", invReturnPurchaseSearchBean.getPaymentTypeFrom());
            appendQuery.append(" AND RIV.paymentType >= :paymentTypeFrom");
        }

        if (invReturnPurchaseSearchBean.getPaymentTypeTo() != null) {
            params.put("paymentTypeTo", invReturnPurchaseSearchBean.getPaymentTypeTo());
            appendQuery.append(" AND RIV.paymentType <= :paymentTypeTo");
        }

        return appendQuery.toString();
    }

}
