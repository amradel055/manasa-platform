/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.InvoiceMovementReportService;
import com.toby.businessservice.reports.searchBean.PurchaseOfItemsBySuppliersSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.views.ItemSalesView;
import com.toby.views.ItemSalesViewGroup;
import com.toby.views.SalesOfItemsByCompany;
import java.math.BigDecimal;
import java.text.DateFormat;
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
public class PurchaseOfItemsBySuppliersReportServiceImpl implements PurchaseOfItemsBySuppliersReportService {

    @EJB
    private GenericDAO dao;
    @EJB
    InvoiceMovementReportService movementReportService;

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
        appendInventoryNew(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendItem(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendPaymentType(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        stringBuilder.append(" order by isv.date ASC,isv.itemId ASC");
        List<ItemSalesView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    @Override    //مبيعات الاصناف حسب العملاء  
    public List<ItemSalesView> getAllPurchaseOfItemsBySuppliersSearchBeanNew(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, Integer invoiceType) {
        if (purchaseOfItemsBySuppliersSearchBean.getGalleryFrom() != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("type", purchaseOfItemsBySuppliersSearchBean.getType());
            params.put("branchId", purchaseOfItemsBySuppliersSearchBean.getBranchId());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" SELECT ipid FROM InvPurchaseInvoiceDetail ipid WHERE ipid.invPurchaseInvoiceId.branchId.id = :branchId AND ipid.invPurchaseInvoiceId.type = :type ");

            if (invoiceType != null) {
                params.put("invoiceType", invoiceType);
                stringBuilder.append(" AND ipid.invPurchaseInvoiceId.invoiceType = :invoiceType ");
            }
            appendDateNew(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
            appendSupplierNew(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
            appendSerial(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
//        appendInventory(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
            appendGallary(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
            appendItem(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
            appendPaymentType(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
            stringBuilder.append(" order by ipid.invPurchaseInvoiceId.date ASC,ipid.itemId ASC");
            List<InvPurchaseInvoiceDetail> details = dao.findListByQuery(stringBuilder.toString(), params);
            return prepareItemSalesViewList(details);
        }
        return null;
    }

    private List<ItemSalesView> prepareItemSalesViewList(List<InvPurchaseInvoiceDetail> entityList) {
        List<ItemSalesView> list = new ArrayList<>();
        ItemSalesView itemsDataView;
        for (InvPurchaseInvoiceDetail entity : entityList) {
            itemsDataView = new ItemSalesView();
            itemsDataView.setItemQuantity(entity.getQuantity());
            itemsDataView.setNet(entity.getNetAfterDiscountHead());
            itemsDataView.setItemId(entity.getItemId().getId());
            itemsDataView.setItemName(entity.getItemId().getName());
            itemsDataView.setItemCode(entity.getItemId().getCode());
            itemsDataView.setItemUnitName(entity.getUnitId().getName());
            itemsDataView.setOrganizationSiteId(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getId());
            itemsDataView.setCost(entity.getCost());
            itemsDataView.setSerial(entity.getInvPurchaseInvoiceId().getSerial());

            if (entity.getItemId() != null && entity.getItemId().getUnitId() != null) {
                itemsDataView.setItemUnitName(entity.getItemId().getUnitId().getName());
            }
            itemsDataView.setDate(entity.getInvPurchaseInvoiceId().getDate());
            itemsDataView.setItemBarcode(entity.getItemId().getName());
            itemsDataView.setDetailItemQuantity(entity.getQuantity());
            itemsDataView.setDiscount(entity.getDiscount());
            itemsDataView.setSerialTax(entity.getInvPurchaseInvoiceId().getSerialTax());
            itemsDataView.setOrganizationSiteCode(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getCode());
            itemsDataView.setOrganizationSiteName(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getName());

            list.add(itemsDataView);
        }

        return list;
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
        appendInventoryNew(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
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

    private void appendDateNew(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getDateFrom() != null) {
//            params.put("dateFrom", purchaseOfItemsBySuppliersSearchBean.getDateFrom());
//            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.date >= :dateFrom");
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.date >= '").append(dateFormat.format(purchaseOfItemsBySuppliersSearchBean.getDateFrom())).append("'");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getDateTo() != null) {
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
//            params.put("dateTo", purchaseOfItemsBySuppliersSearchBean.getDateTo());
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.date <= '").append(dateFormat.format(purchaseOfItemsBySuppliersSearchBean.getDateTo())).append("'");
            
//            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.date <= :dateTo");
        }

    }

    private void appendSupplier(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromorganizationName() != null) {
            params.put("fromorganization", purchaseOfItemsBySuppliersSearchBean.getFromorganizationName().getId());
            stringBuilder.append(" AND isv.supplierId >= :fromorganization");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToorganizationName() != null) {
            params.put("toorganization", purchaseOfItemsBySuppliersSearchBean.getToorganizationName().getId());
            stringBuilder.append(" AND isv.supplierId <= :toorganization");
        }

    }

    private void appendSupplierNew(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromorganizationName() != null) {
            params.put("fromorganization", purchaseOfItemsBySuppliersSearchBean.getFromorganizationName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.organizationSiteId.code >= :fromorganization");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToorganizationName() != null) {
            params.put("toorganization", purchaseOfItemsBySuppliersSearchBean.getToorganizationName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.organizationSiteId.code <= :toorganization");
        }

    }

    private void appendItem(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromItemName() != null) {
            params.put("FromItem", purchaseOfItemsBySuppliersSearchBean.getFromItemName().getCode());
            stringBuilder.append(" AND ipid.itemId.code >= :FromItem");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToItemName() != null) {
            params.put("ToItem", purchaseOfItemsBySuppliersSearchBean.getToItemName().getCode());
            stringBuilder.append(" AND ipid.itemId.code <= :ToItem");
        }

    }

    private void appendInventory(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromInventoryName() != null) {
            params.put("FromInventory", purchaseOfItemsBySuppliersSearchBean.getFromInventoryName().getId());
            stringBuilder.append(" AND ipid.invInventoryId.id >= :FromInventory");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToInventoryName() != null) {
            params.put("ToInventory", purchaseOfItemsBySuppliersSearchBean.getToInventoryName().getId());
            stringBuilder.append(" AND ipid.invInventoryId.id <= :ToInventory");
        }

    }

    private void appendGallary(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getGalleryFrom() != null
                && !purchaseOfItemsBySuppliersSearchBean.getGalleryFrom().isEmpty()) {
            StringBuilder list = null;
            for (InvInventoryDTO gallary : purchaseOfItemsBySuppliersSearchBean.getGalleryFrom()) {
                if (list == null) {
                    list = new StringBuilder();
                    list.append(gallary.getId());
                } else {
                    list.append(" , ").append(gallary.getId());
                }
            }
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.galaryId.id in (").append(list.toString()).append(")");
        }

    }

    private void appendInventoryNew(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

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
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.serial >= :FromSerial ");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo() != null) {
            params.put("Toserial", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo().getSerial());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.serial <= :Toserial ");
        }

    }

    private void appendPaymentType(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() >= 0) {
            params.put("frompaymentType", purchaseOfItemsBySuppliersSearchBean.getFrompaymentType());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.paymentType >= :frompaymentType");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getTopaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getTopaymentType() >= 0) {
            params.put("topaymentType", purchaseOfItemsBySuppliersSearchBean.getTopaymentType());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.paymentType <= :topaymentType");
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
        appendInventoryNew(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
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

    public List<ItemSalesViewGroup> getAllPurchaseOfItemsBySuppliersSearchBeanByGroupCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", purchaseOfItemsBySuppliersSearchBean.getType());
        params.put("branchId", purchaseOfItemsBySuppliersSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("SELECT ipid.invPurchaseInvoiceId.serial as serial ,ipid.invPurchaseInvoiceId.date as date , ipid.invPurchaseInvoiceId.organizationSiteId.name as name ");
//        stringBuilder.append(" , ipid.itemId.code as code , ipid.itemId.name as itemname , (SUM(IFNULL(ipid.number,0))) as number  ");
        stringBuilder.append(" SELECT (SUM(IFNULL(ipid.number,0))) as number ,(SUM(IFNULL(ipid.cost,0))) as cost ,(SUM(IFNULL(ipid.discount,0))) as discount,(SUM(IFNULL(ipid.net,0))) as net ");
//        stringBuilder.append(" , ipid.itemId.code as code , ipid.itemId.name as itemname , (SUM(IFNULL(ipid.number,0))) as number ,(SUM(IFNULL(ipid.cost,0))) as cost ,(SUM(IFNULL(ipid.discount,0))) as discount,(SUM(IFNULL(ipid.net,0))) as net ");
        stringBuilder.append(" FROM InvPurchaseInvoiceDetail ipid WHERE ipid.invPurchaseInvoiceId.branchId.id = :branchId AND ipid.invPurchaseInvoiceId.type = :type");

        appendDateCustomer(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSupplierCustomer(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendSerialCustomer(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendInventoryCustomer(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendItemCustomer(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        appendPaymentTypeCustomer(purchaseOfItemsBySuppliersSearchBean, stringBuilder, params);
        stringBuilder.append(" GROUP BY ipid.invPurchaseInvoiceId.organizationSiteId.id order by ipid.invPurchaseInvoiceId.date ,ipid.invPurchaseInvoiceId.serial");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        return prepareListCustomer(res);
    }

    private void appendDateCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getDateFrom() != null) {
            params.put("dateFrom", purchaseOfItemsBySuppliersSearchBean.getDateFrom());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.date >= :dateFrom");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getDateTo() != null) {
            params.put("dateTo", purchaseOfItemsBySuppliersSearchBean.getDateTo());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.date <= :dateTo");
        }

    }

    private void appendSupplierCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromorganizationName() != null) {
            params.put("fromorganization", purchaseOfItemsBySuppliersSearchBean.getFromorganizationName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.organizationSiteId.code >= :fromorganization");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToorganizationName() != null) {
            params.put("toorganization", purchaseOfItemsBySuppliersSearchBean.getToorganizationName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.organizationSiteId.code <= :toorganization");
        }

    }

    private void appendItemCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromItemName() != null) {
            params.put("FromItem", purchaseOfItemsBySuppliersSearchBean.getFromItemName().getCode());
            stringBuilder.append(" AND ipid.itemId.code >= :FromItem");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToItemName() != null) {
            params.put("ToItem", purchaseOfItemsBySuppliersSearchBean.getToItemName().getCode());
            stringBuilder.append(" AND ipid.itemId.code <= :ToItem");
        }

    }

    private void appendInventoryCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromInventoryName() != null) {
            params.put("FromInventory", purchaseOfItemsBySuppliersSearchBean.getFromInventoryName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.invInventoryId.code >= :FromInventory");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToInventoryName() != null) {
            params.put("ToInventory", purchaseOfItemsBySuppliersSearchBean.getToInventoryName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.invInventoryId.code <= :ToInventory");
        }

    }

    private void appendGallaryCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFromInventoryName() != null) {
            params.put("FromInventory", purchaseOfItemsBySuppliersSearchBean.getFromInventoryName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.galaryId.code >= :FromInventory");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getToInventoryName() != null) {
            params.put("ToInventory", purchaseOfItemsBySuppliersSearchBean.getToInventoryName().getCode());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.galaryId.code <= :ToInventory");
        }

    }

    private void appendSerialCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom() != null) {
            params.put("FromSerial", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceFrom().getSerial());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.serial >= :FromSerial ");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo() != null) {
            params.put("Toserial", purchaseOfItemsBySuppliersSearchBean.getSalesInvoiceTo().getSerial());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.serial <= :Toserial ");
        }

    }

    private void appendPaymentTypeCustomer(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() >= 0) {
            params.put("frompaymentType", purchaseOfItemsBySuppliersSearchBean.getFrompaymentType());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.paymentType >= :frompaymentType");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getTopaymentType() != null && purchaseOfItemsBySuppliersSearchBean.getTopaymentType() >= 0) {
            params.put("topaymentType", purchaseOfItemsBySuppliersSearchBean.getTopaymentType());
            stringBuilder.append(" AND ipid.invPurchaseInvoiceId.paymentType <= :topaymentType");
        }
    }

    private List<ItemSalesViewGroup> prepareListCustomer(List<Object[]> res) {
        List<ItemSalesViewGroup> list = new ArrayList<>();
        ItemSalesViewGroup itemSalesViewGroup;
        for (Object[] object : res) {
            itemSalesViewGroup = new ItemSalesViewGroup();

            itemSalesViewGroup.setSerial((Integer) object[0]);
            itemSalesViewGroup.setDate((Date) object[1]);
            itemSalesViewGroup.setOrganizationSiteName((String) object[2]);
            itemSalesViewGroup.setItemCode((String) object[3]);
            itemSalesViewGroup.setItemName((String) object[4]);
            itemSalesViewGroup.setItemUnitName((String) object[5]);
            itemSalesViewGroup.setItemQuantity((BigDecimal) object[6]);
            itemSalesViewGroup.setCost((BigDecimal) object[7]);
            itemSalesViewGroup.setDiscount((BigDecimal) object[8]);
            itemSalesViewGroup.setNet((BigDecimal) object[9]);
            list.add(itemSalesViewGroup);
        }

        return list;
    }

    @Override
    public List<SalesOfItemsByCompany> getAllSalesOfItemsByCompany(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT inv.id , inv.name , oc.name as name , oc.code code , sum(ipid.net) as sum \n"
                + "        FROM `inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "        LEFT JOIN inv_organization_site ios on ios.id = ipi.organization_site_id\n"
                + "        LEFT JOIN offer_company oc on oc.id = ios.offer_company_id\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = ipi.galary_Id\n"
                + "        WHERE\n"
                + "        `ipid`.`item_id` IS NOT NULL AND ipi.type = 4 \n"
                + "        AND `ipi`.`branch_id` = 182 and ios.offer_company_id is not null\n");
        if (purchaseOfItemsBySuppliersSearchBean.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            stringBuilder.append(" AND ipi.date >= '").append(dateFormat.format(purchaseOfItemsBySuppliersSearchBean.getDateFrom())).append("'");
        }

        if (purchaseOfItemsBySuppliersSearchBean.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            stringBuilder.append(" AND ipi.date <= '").append(dateFormat.format(purchaseOfItemsBySuppliersSearchBean.getDateTo())).append("'");
        }
        if (purchaseOfItemsBySuppliersSearchBean.getFromInventoryDTOName() != null) {
            stringBuilder.append(" AND ipi.galary_Id = ").append(purchaseOfItemsBySuppliersSearchBean.getFromInventoryDTOName().getId());
        }

        stringBuilder.append("    GROUP BY ios.offer_company_id");
        List<Object[]> details = dao.executeNativeQuery(stringBuilder.toString());
        return prepareListSalesOfItemsByCompany(details);
    }

    private List<SalesOfItemsByCompany> prepareListSalesOfItemsByCompany(List<Object[]> res) {
        List<SalesOfItemsByCompany> list = new ArrayList<>();
        if (res != null && res.size() > 0) {
            SalesOfItemsByCompany salesOfItemsByCompany;
            salesOfItemsByCompany = new SalesOfItemsByCompany();
            SalesOfItemsByCompany salesOfItemsByCompanyMain = new SalesOfItemsByCompany();
            salesOfItemsByCompanyMain.setGallaryId((Integer) res.get(0)[0]);
            salesOfItemsByCompanyMain.setGallaryName((String) res.get(0)[1]);
            BigDecimal total = BigDecimal.ZERO;
            for (Object[] object : res) {
                salesOfItemsByCompany = new SalesOfItemsByCompany();
                salesOfItemsByCompany.setCompanyName((String) object[2]);
                salesOfItemsByCompany.setCompanyCode((Integer) object[3]);
                salesOfItemsByCompany.setTotalSales((BigDecimal) object[4]);
                total = total.add(salesOfItemsByCompany.getTotalSales());
                list.add(salesOfItemsByCompany);
            }
            salesOfItemsByCompanyMain.setTotalSales(total);
            list.add(0, salesOfItemsByCompanyMain);;
        }
        return list;
    }

}
