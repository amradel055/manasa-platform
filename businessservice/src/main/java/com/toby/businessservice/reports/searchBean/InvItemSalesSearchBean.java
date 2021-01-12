/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.entity.InvGroup;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InventoryDelegator;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class InvItemSalesSearchBean {

    private InventoryDelegator salesPersonFrom;
    private InventoryDelegator salesPersonTo;
    private Integer branchId;
    private Date dateFrom;
    private Date dateTo;
    private InvInventory inventoryFrom;
    private InvInventory inventoryTo;
    private Integer paymentTypeFrom;
    private Integer paymentTypeTo;
    private InvPurchaseInvoice salesInvoiceFrom;
    private InvPurchaseInvoice salesInvoiceTo;
    private Boolean type;
    private InvGroup groupFrom;
    private InvGroup groupTo;
    private InvOrganizationSite suplierFrom;
    private InvOrganizationSite suplierTo;
    private Boolean showReport;
    private Boolean shortReport = false;
    private StringBuilder stringBuilder;

    public InventoryDelegator getSalesPersonFrom() {
        return salesPersonFrom;
    }

    public void setSalesPersonFrom(InventoryDelegator salesPersonFrom) {
        this.salesPersonFrom = salesPersonFrom;
    }

    public InventoryDelegator getSalesPersonTo() {
        return salesPersonTo;
    }

    public void setSalesPersonTo(InventoryDelegator salesPersonTo) {
        this.salesPersonTo = salesPersonTo;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public InvInventory getInventoryFrom() {
        return inventoryFrom;
    }

    public void setInventoryFrom(InvInventory inventoryFrom) {
        this.inventoryFrom = inventoryFrom;
    }

    public InvInventory getInventoryTo() {
        return inventoryTo;
    }

    public void setInventoryTo(InvInventory inventoryTo) {
        this.inventoryTo = inventoryTo;
    }

    public Integer getPaymentTypeFrom() {
        return paymentTypeFrom;
    }

    public void setPaymentTypeFrom(Integer paymentTypeFrom) {
        this.paymentTypeFrom = paymentTypeFrom;
    }

    public Integer getPaymentTypeTo() {
        return paymentTypeTo;
    }

    public void setPaymentTypeTo(Integer paymentTypeTo) {
        this.paymentTypeTo = paymentTypeTo;
    }

    public InvPurchaseInvoice getSalesInvoiceFrom() {
        return salesInvoiceFrom;
    }

    public void setSalesInvoiceFrom(InvPurchaseInvoice salesInvoiceFrom) {
        this.salesInvoiceFrom = salesInvoiceFrom;
    }

    public InvPurchaseInvoice getSalesInvoiceTo() {
        return salesInvoiceTo;
    }

    public void setSalesInvoiceTo(InvPurchaseInvoice salesInvoiceTo) {
        this.salesInvoiceTo = salesInvoiceTo;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public InvGroup getGroupFrom() {
        return groupFrom;
    }

    public void setGroupFrom(InvGroup groupFrom) {
        this.groupFrom = groupFrom;
    }

    public InvGroup getGroupTo() {
        return groupTo;
    }

    public void setGroupTo(InvGroup groupTo) {
        this.groupTo = groupTo;
    }

    public InvOrganizationSite getSuplierFrom() {
        return suplierFrom;
    }

    public void setSuplierFrom(InvOrganizationSite suplierFrom) {
        this.suplierFrom = suplierFrom;
    }

    public InvOrganizationSite getSuplierTo() {
        return suplierTo;
    }

    public void setSuplierTo(InvOrganizationSite suplierTo) {
        this.suplierTo = suplierTo;
    }

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

    /**
     * @return the stringBuilder
     */
    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    /**
     * @param stringBuilder the stringBuilder to set
     */
    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    /**
     * @return the shortReport
     */
    public Boolean getShortReport() {
        return shortReport;
    }

    /**
     * @param shortReport the shortReport to set
     */
    public void setShortReport(Boolean shortReport) {
        this.shortReport = shortReport;
    }
}
