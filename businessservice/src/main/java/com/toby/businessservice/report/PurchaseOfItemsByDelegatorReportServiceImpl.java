/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.InvoiceMovementReportService;
import com.toby.businessservice.reports.searchBean.PurchaseOfItemsByDelegatorSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.InvInventory;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InventoryDelegator;
import com.toby.views.ItemSalesView;
import java.math.BigDecimal;
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
public class PurchaseOfItemsByDelegatorReportServiceImpl implements PurchaseOfItemsByDelegatorReportService {

    @EJB
    private GenericDAO dao;
    @EJB
    InvoiceMovementReportService invoiceMovementReportService;

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

//        if (purchaseOfItemsByDelegatorSearchBean.getDelegatorList() != null) {
//            params.put("fromDelegator", purchaseOfItemsByDelegatorSearchBean.getDelegatorList().getId());
//            appendQuery.append(" AND isv.delegatorId >= :fromDelegator");
//        }
//
//        if (purchaseOfItemsByDelegatorSearchBean.getToDelegatorName() != null) {
//            params.put("toDelegator", purchaseOfItemsByDelegatorSearchBean.getToDelegatorName().getId());
//            appendQuery.append(" AND isv.delegatorId <= :toDelegator");
//        }
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
//
//        if (purchaseOfItemsByDelegatorSearchBean.getInventoryList() != null) {
//            params.put("FromInventory", purchaseOfItemsByDelegatorSearchBean.getInventoryList().getId());
//            appendQuery.append(" AND isv.inventoryId >= :FromInventory");
//        }
//
//        if (purchaseOfItemsByDelegatorSearchBean.getToInventoryName() != null) {
//            params.put("ToInventory", purchaseOfItemsByDelegatorSearchBean.getToInventoryName().getId());
//            appendQuery.append(" AND isv.inventoryId <= :ToInventory");
//        }

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

    @Override
    public List<ItemSalesView> getAllPurchaseOfItemsByDelegatorSearchBeanNew(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean) {
        List<ItemSalesView> list = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("type", purchaseOfItemsByDelegatorSearchBean.getType());
        params.put("branchId", purchaseOfItemsByDelegatorSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT isv FROM InvPurchaseInvoiceDetail isv WHERE isv.invPurchaseInvoiceId.branchId.id = :branchId AND isv.invPurchaseInvoiceId.type = :type ");

        stringBuilder.append(appendDateNew(purchaseOfItemsByDelegatorSearchBean, params));
        stringBuilder.append(appendDelegatorNew(purchaseOfItemsByDelegatorSearchBean));
        stringBuilder.append(appendInventoryNew(purchaseOfItemsByDelegatorSearchBean));

        List<InvPurchaseInvoiceDetail> details = dao.findListByQuery(stringBuilder.toString(), params);
        for (InvPurchaseInvoiceDetail detail : details) {
            ItemSalesView itemSalesView = new ItemSalesView();
            itemSalesView.setCost(detail.getCost());
            itemSalesView.setCostAvarage(detail.getCostAvarage());
//            itemSalesView.setCostAvarageFashTotal();
//            itemSalesView.setCostAvarageTotal();
            itemSalesView.setDate(detail.getInvPurchaseInvoiceId().getDate());
            itemSalesView.setDelegatorCode(detail.getInvPurchaseInvoiceId().getInvDelegatorId() != null ? detail.getInvPurchaseInvoiceId().getInvDelegatorId().getName() : "");
            itemSalesView.setDelegatorId(detail.getInvPurchaseInvoiceId().getInvDelegatorId() != null ? detail.getInvPurchaseInvoiceId().getInvDelegatorId().getId() : null);
            itemSalesView.setDetailItemQuantity(detail.getQuantity());
            itemSalesView.setDetailUnitName(detail.getUnitId() == null ? null : detail.getUnitId().getName());
            itemSalesView.setDiscount(detail.getDiscount());
//            itemSalesView.setFash();
            itemSalesView.setGallaryCode(detail.getInvPurchaseInvoiceId().getGalaryId() == null ? null : detail.getInvPurchaseInvoiceId().getGalaryId().getCode());
            itemSalesView.setGallaryId(detail.getInvPurchaseInvoiceId().getGalaryId() == null ? null : detail.getInvPurchaseInvoiceId().getGalaryId().getId());
            itemSalesView.setGallaryName(detail.getInvPurchaseInvoiceId().getGalaryId() == null ? null : detail.getInvPurchaseInvoiceId().getGalaryId().getName());
            itemSalesView.setGroupId(detail.getItemId().getGroupId() == null ? null : detail.getItemId().getGroupId().getId());
            itemSalesView.setGroupName(detail.getItemId().getGroupId() == null ? null : detail.getItemId().getGroupId().getName());
            itemSalesView.setInventoryCode(detail.getInvInventoryId() == null ? null : detail.getInvInventoryId().getCode());
            itemSalesView.setItemBarcode(detail.getItemBarcode());
            itemSalesView.setItemCode(detail.getItemBarcode());
            itemSalesView.setItemName(detail.getItemId().getName());
//            itemSalesView.setItemQuantity();
//            itemSalesView.setItemUnitName();
            itemSalesView.setNet(detail.getNetAfterDiscountHead());
            itemSalesView.setNumber(detail.getNumber());
            itemSalesView.setOrganizationSiteCode(detail.getInvPurchaseInvoiceId().getOrganizationSiteId() == null ? null : detail.getInvPurchaseInvoiceId().getOrganizationSiteId().getCode());
            itemSalesView.setOrganizationSiteId(detail.getInvPurchaseInvoiceId().getOrganizationSiteId() == null ? null : detail.getInvPurchaseInvoiceId().getOrganizationSiteId().getId());
            itemSalesView.setOrganizationSiteName(detail.getInvPurchaseInvoiceId().getOrganizationSiteId() == null ? null : detail.getInvPurchaseInvoiceId().getOrganizationSiteId().getName());
            itemSalesView.setProGroupId(detail.getItemId().getProGroupId() == null ? null : detail.getItemId().getProGroupId().getId());
            itemSalesView.setProGroupName(detail.getItemId().getProGroupId() == null ? null : detail.getItemId().getProGroupId().getName());
            itemSalesView.setSerial(detail.getInvPurchaseInvoiceId().getSerial());
            itemSalesView.setSupplierCode(detail.getInvPurchaseInvoiceId().getOrganizationSiteId() == null ? null : detail.getInvPurchaseInvoiceId().getOrganizationSiteId().getCode());
            itemSalesView.setSupplierId(detail.getInvPurchaseInvoiceId().getOrganizationSiteId() == null ? null : detail.getInvPurchaseInvoiceId().getOrganizationSiteId().getId());
            itemSalesView.setSupplierName(detail.getInvPurchaseInvoiceId().getOrganizationSiteId() == null ? null : detail.getInvPurchaseInvoiceId().getOrganizationSiteId().getName());
            itemSalesView.setType(detail.getInvPurchaseInvoiceId().getType());
            list.add(itemSalesView);
        }

        return list;
    }

    @Override
    public List<ItemSalesView> getAllPurchaseOfItemsByDelegatorSearchBeanNewHead(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean) {
        List<ItemSalesView> list = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("type", purchaseOfItemsByDelegatorSearchBean.getType());
        params.put("branchId", purchaseOfItemsByDelegatorSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT sum(isv.netAfterDiscountHead) , isv.invPurchaseInvoiceId.date , isv.itemBarcode , isv.itemId.name , isv.invPurchaseInvoiceId.serial "
                + " , isv.invPurchaseInvoiceId.id , isv.invPurchaseInvoiceId.invDelegatorId.id , isv.invPurchaseInvoiceId.invDelegatorId.code , isv.invPurchaseInvoiceId.organizationSiteId.id "
                + " , isv.invPurchaseInvoiceId.organizationSiteId.name , isv.invPurchaseInvoiceId.organizationSiteId.code  "
                + " , isv.invPurchaseInvoiceId.galaryId.id , isv.invPurchaseInvoiceId.galaryId.code , isv.invPurchaseInvoiceId.galaryId.name , isv.itemId.proGroupId.id "
                + " , isv.itemId.proGroupId.name , isv.itemId.groupId.id , isv.itemId.groupId.name , isv.invPurchaseInvoiceId.type "
                + " FROM InvPurchaseInvoiceDetail isv WHERE isv.invPurchaseInvoiceId.branchId.id = :branchId AND isv.invPurchaseInvoiceId.type = :type ");

        stringBuilder.append(appendDateNew(purchaseOfItemsByDelegatorSearchBean, params));
        stringBuilder.append(appendDelegatorNew(purchaseOfItemsByDelegatorSearchBean));
        stringBuilder.append(appendInventoryNew(purchaseOfItemsByDelegatorSearchBean));

        stringBuilder.append(" group by isv.invPurchaseInvoiceId.id ");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        for (Object[] object : res) {

            ItemSalesView itemSalesView = new ItemSalesView();

            itemSalesView.setNet(object[0] == null ? null : (BigDecimal) object[0]);
            itemSalesView.setDate(object[1] == null ? null : (Date) object[1]);

            itemSalesView.setItemBarcode(object[2] == null ? null : (String) object[2]);
            itemSalesView.setItemCode(object[3] == null ? null : (String) object[3]);
            itemSalesView.setItemName(object[3] == null ? null : (String) object[3]);

            itemSalesView.setSerial(object[4] == null ? null : (Integer) object[4]);
            itemSalesView.setNumber(object[5] == null ? null : invoiceMovementReportService.summitionTob((Integer) object[5]));

            itemSalesView.setDelegatorId(object[6] == null ? null : (Integer) object[6]);
            itemSalesView.setDelegatorCode(object[7] == null ? null : (String) object[7]);

            itemSalesView.setOrganizationSiteId(object[8] == null ? null : (Integer) object[8]);
            itemSalesView.setSupplierId(object[8] == null ? null : (Integer) object[8]);
            itemSalesView.setOrganizationSiteName(object[9] == null ? null : (String) object[9]);
            itemSalesView.setSupplierName(object[9] == null ? null : (String) object[9]);
            itemSalesView.setOrganizationSiteCode(object[10] == null ? null : (String) object[10]);
            itemSalesView.setSupplierCode(object[10] == null ? null : (String) object[10]);

            itemSalesView.setGallaryId(object[11] == null ? null : (Integer) object[11]);
            itemSalesView.setGallaryCode(object[12] == null ? null : (String) object[12]);
            itemSalesView.setInventoryCode(object[12] == null ? null : (String) object[12]);
            itemSalesView.setGallaryName(object[13] == null ? null : (String) object[13]);

            itemSalesView.setProGroupId(object[14] == null ? null : (Integer) object[14]);
            itemSalesView.setProGroupName(object[15] == null ? null : (String) object[15]);

            itemSalesView.setGroupId(object[16] == null ? null : (Integer) object[16]);
            itemSalesView.setGroupName(object[17] == null ? null : (String) object[17]);

            itemSalesView.setType(object[18] == null ? null : (Integer) object[18]);

            list.add(itemSalesView);

        }

        return list;
    }

    private String appendDateNew(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (purchaseOfItemsByDelegatorSearchBean.getDateFrom() != null) {
            params.put("dateFrom", purchaseOfItemsByDelegatorSearchBean.getDateFrom());
            appendQuery.append(" AND isv.invPurchaseInvoiceId.date >= :dateFrom");
        }

        if (purchaseOfItemsByDelegatorSearchBean.getDateTo() != null) {
            params.put("dateTo", purchaseOfItemsByDelegatorSearchBean.getDateTo());
            appendQuery.append(" AND isv.invPurchaseInvoiceId.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendDelegatorNew(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean) {
        StringBuilder appendQuery = new StringBuilder();
        StringBuilder query = new StringBuilder();
        query.append("0");

        if (purchaseOfItemsByDelegatorSearchBean.getDelegatorList() != null && !purchaseOfItemsByDelegatorSearchBean.getDelegatorList().isEmpty()) {
            for (InventoryDelegator delegator : purchaseOfItemsByDelegatorSearchBean.getDelegatorList()) {
                query.append(",").append(delegator.getId());
            }
            appendQuery.append(" AND isv.invPurchaseInvoiceId.invDelegatorId.id in ( ").append(query).append(" )");
        }

        return appendQuery.toString();
    }

    private String appendInventoryNew(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean) {
        StringBuilder appendQuery = new StringBuilder();
        StringBuilder query = new StringBuilder();
        query.append("0");
        if (purchaseOfItemsByDelegatorSearchBean.getInventoryList() != null) {
            for (InvInventory inventory : purchaseOfItemsByDelegatorSearchBean.getInventoryList()) {
                query.append(",").append(inventory.getId());
            }
            appendQuery.append(" AND isv.invPurchaseInvoiceId.invInventoryId.id in ( ").append(query).append(" )");
        }
        return appendQuery.toString();
    }

}
