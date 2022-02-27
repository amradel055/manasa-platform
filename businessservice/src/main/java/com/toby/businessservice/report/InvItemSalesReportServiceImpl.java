/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.ItemSalesView;
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
public class InvItemSalesReportServiceImpl implements InvItemSalesReportService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ItemSalesView> getAllInvPurchaseInvoiceDetailsByInvItemSalesSearchBean(InvItemSalesSearchBean invItemSalesSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", invItemSalesSearchBean.getType());
        params.put("branchId", invItemSalesSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT isv FROM ItemSalesView isv WHERE isv.branchId = :branchId AND isv.type = :type ");

        appendDate(invItemSalesSearchBean, stringBuilder, params);
        appendSalesInvoice(invItemSalesSearchBean, stringBuilder, params);
        appendInventory(invItemSalesSearchBean, stringBuilder, params);
        appendDelegator(invItemSalesSearchBean, stringBuilder, params);
        appendPaymentType(invItemSalesSearchBean, stringBuilder, params);

        stringBuilder.append(" order by isv.date , isv.serial ");
        List<ItemSalesView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendDate(InvItemSalesSearchBean invItemSalesSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (invItemSalesSearchBean.getDateFrom() != null) {
            params.put("dateFrom", invItemSalesSearchBean.getDateFrom());
            stringBuilder.append(" AND isv.date >= :dateFrom");
        }

        if (invItemSalesSearchBean.getDateTo() != null) {
            params.put("dateTo", invItemSalesSearchBean.getDateTo());
            stringBuilder.append(" AND isv.date <= :dateTo");
        }

    }

    private void appendSalesInvoice(InvItemSalesSearchBean invItemSalesSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (invItemSalesSearchBean.getSalesInvoiceFrom() != null) {
            params.put("salesInvoiceSerialFrom", invItemSalesSearchBean.getSalesInvoiceFrom().getSerial());
            stringBuilder.append(" AND isv.serial >= :salesInvoiceSerialFrom");
        }

        if (invItemSalesSearchBean.getSalesInvoiceTo() != null) {
            params.put("salesInvoiceSerialTo", invItemSalesSearchBean.getSalesInvoiceTo().getSerial());
            stringBuilder.append(" AND isv.serial <= :salesInvoiceSerialTo");
        }

    }

    private void appendInventory(InvItemSalesSearchBean invItemSalesSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (invItemSalesSearchBean.getInventoryFrom() != null) {
            params.put("inventoryCodeFrom", invItemSalesSearchBean.getInventoryFrom().getCode());
            stringBuilder.append(" AND isv.inventoryCode >= :inventoryCodeFrom");
        }

        if (invItemSalesSearchBean.getInventoryTo() != null) {
            params.put("inventoryCodeTo", invItemSalesSearchBean.getInventoryTo().getCode());
            stringBuilder.append(" AND isv.inventoryCode <= :inventoryCodeTo");
        }

    }

    private void appendDelegator(InvItemSalesSearchBean invItemSalesSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (invItemSalesSearchBean.getSalesPersonFrom() != null) {
            params.put("delegatorCodeFrom", invItemSalesSearchBean.getSalesPersonFrom().getCode());
            stringBuilder.append(" AND isv.delegatorCode >= :delegatorCodeFrom");
        }

        if (invItemSalesSearchBean.getSalesPersonTo() != null) {
            params.put("delegatorCodeTo", invItemSalesSearchBean.getSalesPersonTo().getCode());
            stringBuilder.append(" AND isv.delegatorCode <= :delegatorCodeTo");
        }

    }

    private void appendPaymentType(InvItemSalesSearchBean invItemSalesSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (invItemSalesSearchBean.getPaymentTypeFrom() != null) {
            params.put("paymentTypeFrom", invItemSalesSearchBean.getPaymentTypeFrom());
            stringBuilder.append(" AND isv.paymentType >= :paymentTypeFrom");
        }

        if (invItemSalesSearchBean.getPaymentTypeTo() != null) {
            params.put("paymentTypeTo", invItemSalesSearchBean.getPaymentTypeTo());
            stringBuilder.append(" AND isv.paymentType <= :paymentTypeTo");
        }
    }

    @Override
    public List<ItemSalesView> getAllInvoiceDetailByInvItemSalesSearchBean(InvItemSalesSearchBean invItemSalesSearchBean) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", invItemSalesSearchBean.getBranchId());
        params.put("type", invItemSalesSearchBean.getType());

        stringBuilder.append(""
                + "SELECT "
                + " sum(isv.itemQuantity), "
                + " sum(isv.net), "
                + " isv.itemId, "
                + " isv.itemName, "
                + " isv.itemCode, "
                + " isv.itemUnitName, "
                + " isv.groupId, "
                + " isv.groupName, "
                + " isv.serial "
                + " FROM ItemSalesView isv WHERE isv.branchId = :branchId AND isv.type = :type  "
                + " AND isv.itemId is not null "
                + " AND isv.groupId is not null"
                + " AND isv.itemUnitName is not null "
                + " AND isv.itemQuantity is not null ");

        appendDate(invItemSalesSearchBean, stringBuilder, params);
        appendSalesInvoice(invItemSalesSearchBean, stringBuilder, params);
        appendInventory(invItemSalesSearchBean, stringBuilder, params);
        appendDelegator(invItemSalesSearchBean, stringBuilder, params);
        appendPaymentType(invItemSalesSearchBean, stringBuilder, params);
        stringBuilder.append(" group by "
                + " isv.itemCode, "
                + " isv.itemId, "
                + " isv.itemName, "
                + " isv.itemUnitName, "
                + " isv.groupId, "
                + " isv.groupName, "
                + " isv.serial "
                + " order by isv.serial ");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        List<ItemSalesView> details = prepareList(res);

        return details;
    }

    private List<ItemSalesView> prepareList(List<Object[]> res) {
        List<ItemSalesView> list = new ArrayList<>();
        ItemSalesView itemsDataView;
        for (Object[] object : res) {
            itemsDataView = new ItemSalesView();
            itemsDataView.setItemQuantity((BigDecimal) object[0]);
            itemsDataView.setNet((BigDecimal) object[1]);
            itemsDataView.setItemId((Integer) object[2]);
            itemsDataView.setItemName((String) object[3]);
            itemsDataView.setItemCode((String) object[4]);
            itemsDataView.setItemUnitName((String) object[5]);
            itemsDataView.setGroupId((Integer) object[6]);
            itemsDataView.setGroupName((String) object[7]);
            itemsDataView.setSerial((Integer) object[8]);

            list.add(itemsDataView);
        }

        return list;
    }
}
