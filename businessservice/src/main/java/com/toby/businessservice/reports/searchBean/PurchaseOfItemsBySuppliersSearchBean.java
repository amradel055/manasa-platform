/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.dto.InvItemDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class PurchaseOfItemsBySuppliersSearchBean {

    private Integer branchId;
    private Boolean type;

    private InvOrganizationSite fromorganizationName;
    private InvOrganizationSite toorganizationName;
    private Date dateFrom;
    private Date dateTo;
    private InvItemDTO fromItemName;
    private InvItemDTO toItemName;
    private InvInventory fromInventoryName;
    private InvInventory toInventoryName;
    private Integer fromserial;
    private Integer toserial;
    private int frompaymentType;
    private int topaymentType;
    private InvPurchaseInvoice salesInvoiceFrom;
    private InvPurchaseInvoice salesInvoiceTo;

    private Boolean showReport;
    private Boolean shortReport = false;

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the fromorganizationName
     */
    public InvOrganizationSite getFromorganizationName() {
        return fromorganizationName;
    }

    /**
     * @param fromorganizationName the fromorganizationName to set
     */
    public void setFromorganizationName(InvOrganizationSite fromorganizationName) {
        this.fromorganizationName = fromorganizationName;
    }

    /**
     * @return the toorganizationName
     */
    public InvOrganizationSite getToorganizationName() {
        return toorganizationName;
    }

    /**
     * @param toorganizationName the toorganizationName to set
     */
    public void setToorganizationName(InvOrganizationSite toorganizationName) {
        this.toorganizationName = toorganizationName;
    }

    /**
     * @return the fromInventoryName
     */
    public InvInventory getFromInventoryName() {
        return fromInventoryName;
    }

    /**
     * @param fromInventoryName the fromInventoryName to set
     */
    public void setFromInventoryName(InvInventory fromInventoryName) {
        this.fromInventoryName = fromInventoryName;
    }

    /**
     * @return the toInventoryName
     */
    public InvInventory getToInventoryName() {
        return toInventoryName;
    }

    /**
     * @param toInventoryName the toInventoryName to set
     */
    public void setToInventoryName(InvInventory toInventoryName) {
        this.toInventoryName = toInventoryName;
    }

    public Integer getFromserial() {
        return fromserial;
    }

    /**
     * @param fromserial the fromserial to set
     */
    public void setFromserial(Integer fromserial) {
        this.fromserial = fromserial;
    }

    /**
     * @return the toserial
     */
    public Integer getToserial() {
        return toserial;
    }

    /**
     * @param toserial the toserial to set
     */
    public void setToserial(Integer toserial) {
        this.toserial = toserial;
    }

    /**
     * @return the frompaymentType
     */
    public Integer getFrompaymentType() {
        return frompaymentType;
    }

    /**
     * @param frompaymentType the frompaymentType to set
     */
    public void setFrompaymentType(Integer frompaymentType) {
        this.frompaymentType = frompaymentType;
    }

    /**
     * @return the topaymentType
     */
    public Integer getTopaymentType() {
        return topaymentType;
    }

    /**
     * @param topaymentType the topaymentType to set
     */
    public void setTopaymentType(Integer topaymentType) {
        this.topaymentType = topaymentType;
    }

    /**
     * @return the fromItemName
     */
    public InvItemDTO getFromItemName() {
        return fromItemName;
    }

    /**
     * @param fromItemName the fromItemName to set
     */
    public void setFromItemName(InvItemDTO fromItemName) {
        this.fromItemName = fromItemName;
    }

    /**
     * @return the toItemName
     */
    public InvItemDTO getToItemName() {
        return toItemName;
    }

    /**
     * @param toItemName the toItemName to set
     */
    public void setToItemName(InvItemDTO toItemName) {
        this.toItemName = toItemName;
    }

    /**
     * @return the type
     */
    public Boolean getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * @return the salesInvoiceFrom
     */
    public InvPurchaseInvoice getSalesInvoiceFrom() {
        return salesInvoiceFrom;
    }

    /**
     * @param salesInvoiceFrom the salesInvoiceFrom to set
     */
    public void setSalesInvoiceFrom(InvPurchaseInvoice salesInvoiceFrom) {
        this.salesInvoiceFrom = salesInvoiceFrom;
    }

    /**
     * @return the salesInvoiceTo
     */
    public InvPurchaseInvoice getSalesInvoiceTo() {
        return salesInvoiceTo;
    }

    /**
     * @param salesInvoiceTo the salesInvoiceTo to set
     */
    public void setSalesInvoiceTo(InvPurchaseInvoice salesInvoiceTo) {
        this.salesInvoiceTo = salesInvoiceTo;
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
