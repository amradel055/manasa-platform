/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.PurchaseOfItemsBySuppliersSearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.ItemSalesView;
import com.toby.views.ItemSalesViewGroup;
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
public class PurchaseOfItemsBySuppliersReportServiceImpl implements PurchaseOfItemsBySuppliersReportService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ItemSalesView> getAllPurchaseOfItemsBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", purchaseOfItemsBySuppliersSearchBean.getType());
        params.put("branchId", purchaseOfItemsBySuppliersSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT isv FROM ItemSalesView isv WHERE isv.branchId = :branchId AND isv.type = :type");

        appendDate(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSupplier(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSerial(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendInventory(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendItem(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendPaymentType(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        stringBuilder.append(" order by isv.date ASC,isv.itemId ASC");
        List<ItemSalesView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    @Override
    public List<ItemSalesViewGroup> getAllPurchaseOfItemsBySuppliersSearchBeanByGroup(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", purchaseOfItemsBySuppliersSearchBean.getType());
        params.put("branchId", purchaseOfItemsBySuppliersSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT isv FROM ItemSalesViewGroup isv WHERE isv.branchId = :branchId AND isv.type = :type");

        appendDate(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSupplier(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSerial(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendInventory(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendItem(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendPaymentType(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        stringBuilder.append(" order by isv.date ASC,isv.itemId ASC");
        List<ItemSalesViewGroup> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendDate(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getDateFrom() != null) {
            params.put("dateFrom", purchaseOfItemsBySuppliersSearchBean.getDateFrom());
            stringBuilder.append(" AND isv.date >= :dateFrom");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getDateTo() != null) {
            params.put("dateTo", purchaseOfItemsBySuppliersSearchBean.getDateTo());
            stringBuilder.append(" AND isv.date <= :dateTo");
        }

    }

    private void appendSupplier(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromorganizationName() != null) {
            params.put("fromorganization", purchaseOfItemsBySuppliersSearchBean.getFromorganizationName().getId());
            stringBuilder.append(" AND isv.organizationSiteId >= :fromorganization");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToorganizationName() != null) {
            params.put("toorganization", purchaseOfItemsBySuppliersSearchBean.getToorganizationName().getId());
            stringBuilder.append(" AND isv.organizationSiteId <= :toorganization");
        }

    }

    private void appendItem(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromItemName() != null) {
            params.put("FromItem", purchaseOfItemsBySuppliersSearchBean.getFromItemName().getCode());
            stringBuilder.append(" AND isv.itemCode >= :FromItem");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToItemName() != null) {
            params.put("ToItem", purchaseOfItemsBySuppliersSearchBean.getToItemName().getCode());
            stringBuilder.append(" AND isv.itemCode <= :ToItem");
        }

    }

    private void appendInventory(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromInventoryName() != null) {
            params.put("FromInventory", purchaseOfItemsBySuppliersSearchBean.getFromInventoryName().getId());
            stringBuilder.append(" AND isv.inventoryId >= :FromInventory");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToInventoryName() != null) {
            params.put("ToInventory", purchaseOfItemsBySuppliersSearchBean.getToInventoryName().getId());
            stringBuilder.append(" AND isv.inventoryId <= :ToInventory");
        }

    }

    private void appendSerial(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom() != null) {
            params.put("FromSerial", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom().getSerial());
            stringBuilder.append(" AND isv.serial >= :FromSerial ");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo() != null) {
            params.put("Toserial", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo().getSerial());
            stringBuilder.append(" AND isv.serial <= :Toserial ");
        }

    }

    private void appendPaymentType(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() >= 0) {
            params.put("frompaymentType", purchaseOfItemsBySuppliersSearchBean.getFrompaymentType());
            stringBuilder.append(" AND isv.paymentType >= :frompaymentType");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getTopaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getTopaymentType() >= 0) {
            params.put("topaymentType", purchaseOfItemsBySuppliersSearchBean.getTopaymentType());
            stringBuilder.append(" AND isv.paymentType <= :topaymentType");
        }
    }

    //------------------------------------------
    @Override
    public List<ItemSalesView> getAllSalesOfItemsBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", purchaseOfItemsBySuppliersSearchBean.getType());
        params.put("branchId", purchaseOfItemsBySuppliersSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT isv FROM ItemSalesView isv WHERE isv.branchId = :branchId AND isv.type = :type ");

        appendSalesDate(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSalesSupplier(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSalesSerial(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSalesInventory(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSalesItem(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSalesPaymentType(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);

        List<ItemSalesView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendSalesDate(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getDateFrom() != null) {
            params.put("dateFrom", purchaseOfItemsBySuppliersSearchBean.getDateFrom());
            stringBuilder.append(" AND isv.date >= :dateFrom");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getDateTo() != null) {
            params.put("dateTo", purchaseOfItemsBySuppliersSearchBean.getDateTo());
            stringBuilder.append(" AND isv.date <= :dateTo");
        }

    }

    private void appendSalesSupplier(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromorganizationName() != null) {
            params.put("fromorganization", purchaseOfItemsBySuppliersSearchBean.getFromorganizationName().getId());
            stringBuilder.append(" AND isv.supplierId >= :fromorganization");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToorganizationName() != null) {
            params.put("toorganization", purchaseOfItemsBySuppliersSearchBean.getToorganizationName().getId());
            stringBuilder.append(" AND isv.supplierId <= :toorganization");
        }

    }

    private void appendSalesItem(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromItemName() != null) {
            params.put("FromItem", purchaseOfItemsBySuppliersSearchBean.getFromItemName().getId());
            stringBuilder.append(" AND isv.itemId >= :FromItem");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToItemName() != null) {
            params.put("ToItem", purchaseOfItemsBySuppliersSearchBean.getToItemName().getId());
            stringBuilder.append(" AND isv.itemId <= :ToItem");
        }

    }

    private void appendSalesInventory(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromInventoryName() != null) {
            params.put("FromInventory", purchaseOfItemsBySuppliersSearchBean.getFromInventoryName().getId());
            stringBuilder.append(" AND isv.inventoryId >= :FromInventory");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToInventoryName() != null) {
            params.put("ToInventory", purchaseOfItemsBySuppliersSearchBean.getToInventoryName().getId());
            stringBuilder.append(" AND isv.inventoryId <= :ToInventory");
        }

    }

    private void appendSalesSerial(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom() != null) {
            params.put("FromSerial", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom().getSerial());
            stringBuilder.append(" AND isv.serial >= :FromSerial ");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo() != null) {
            params.put("Toserial", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo().getSerial());
            stringBuilder.append(" AND isv.serial <= :Toserial ");
        }

    }

    private void appendSalesPaymentType(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() >= 0) {
            params.put("frompaymentType", purchaseOfItemsBySuppliersSearchBean.getFrompaymentType());
            stringBuilder.append(" AND isv.paymentType >= :frompaymentType");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getTopaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getTopaymentType() >= 0) {
            params.put("topaymentType", purchaseOfItemsBySuppliersSearchBean.getTopaymentType());
            stringBuilder.append(" AND isv.paymentType <= :topaymentType");
        }
    }

    //-------------------------------------------
    @Override
    public List<ItemSalesView> getAllInvoiceDetailBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", purchaseOfItemsBySuppliersSearchBean.getBranchId());
        params.put("type", purchaseOfItemsBySuppliersSearchBean.getType());

        stringBuilder.append(""
                + "SELECT "
                + " sum(isv.itemQuantity), "
                + " sum(isv.net), "
                + " isv.itemId, "
                + " isv.itemName, "
                + " isv.itemCode, "
                + " isv.itemUnitName, "
                + " isv.organizationSiteId, "
                + " isv.cost, "
                + " isv.serial "
                + " FROM ItemSalesView isv WHERE isv.branchId = :branchId AND isv.type = :type  "
                + " AND isv.itemId is not null "
                + " AND isv.groupId is not null"
                + " AND isv.itemUnitName is not null "
                + " AND isv.itemQuantity is not null ");

        appendPurchaseInvoice(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendOrganizationSite(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendDate(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSupplier(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSerial(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendInventory(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendItem(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendPaymentType(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);

        stringBuilder.append(" group by "
                + " isv.itemCode, "
                + " isv.itemId, "
                + " isv.itemName, "
                + " isv.itemUnitName, "
                + " isv.organizationSiteId, "
                + " isv.cost, "
                + " isv.serial "
                + " order by isv.serial");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        List<ItemSalesView> details = prepareList(res);

        return details;
    }

    private void appendPurchaseInvoice(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom() != null) {
            params.put("salesInvoiceSerialFrom", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom().getSerial());
            stringBuilder.append(" AND isv.serial >= :salesInvoiceSerialFrom");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo() != null) {
            params.put("salesInvoiceSerialTo", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo().getSerial());
            stringBuilder.append(" AND isv.serial <= :salesInvoiceSerialTo");
        }

    }

    private void appendOrganizationSite(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromorganizationName() != null) {
            params.put("organizationSiteCodeFrom", purchaseOfItemsBySuppliersSearchBean.getFromorganizationName().getId());
            stringBuilder.append(" AND isv.organizationSiteId >= :organizationSiteCodeFrom");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToorganizationName() != null) {
            params.put("organizationSiteCodeTo", purchaseOfItemsBySuppliersSearchBean.getToorganizationName().getId());
            stringBuilder.append(" AND isv.organizationSiteId <= :organizationSiteCodeTo");
        }

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
            itemsDataView.setOrganizationSiteId((Integer) object[6]);
            itemsDataView.setCost((BigDecimal) object[7]);
            itemsDataView.setSerial((Integer) object[8]);

            list.add(itemsDataView);
        }

        return list;
    }
}
