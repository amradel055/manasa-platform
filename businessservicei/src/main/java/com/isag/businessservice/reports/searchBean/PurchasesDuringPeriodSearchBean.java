/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.entity.CostCenter;

import com.isag.entity.InvInventory;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class PurchasesDuringPeriodSearchBean {

    private Integer branchId;
    private Integer type;
    private Integer frompaymentType;
    private Integer topaymentType;
    private InvPurchaseInvoice fromserial;
    private InvPurchaseInvoice toserial;
    private CostCenter fromCostCenter;
    private CostCenter toCostCenter;
    private InvOrganizationSite fromorganizationName;
    private InvOrganizationSite toorganizationName;
    private InvInventory fromInventoryName;
    private InvInventory toInventoryName;
    private Date dateFrom;
    private Date dateTo;
    private Boolean showReport;

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

   
    /**
     * @return the fromCostCenter
     */
    public CostCenter getFromCostCenter() {
        return fromCostCenter;
    }

    /**
     * @param fromCostCenter the fromCostCenter to set
     */
    public void setFromCostCenter(CostCenter fromCostCenter) {
        this.fromCostCenter = fromCostCenter;
    }

    /**
     * @return the toCostCenter
     */
    public CostCenter getToCostCenter() {
        return toCostCenter;
    }

    /**
     * @param toCostCenter the toCostCenter to set
     */
    public void setToCostCenter(CostCenter toCostCenter) {
        this.toCostCenter = toCostCenter;
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
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the fromserial
     */
    public InvPurchaseInvoice getFromserial() {
        return fromserial;
    }

    /**
     * @param fromserial the fromserial to set
     */
    public void setFromserial(InvPurchaseInvoice fromserial) {
        this.fromserial = fromserial;
    }

    /**
     * @return the toserial
     */
    public InvPurchaseInvoice getToserial() {
        return toserial;
    }

    /**
     * @param toserial the toserial to set
     */
    public void setToserial(InvPurchaseInvoice toserial) {
        this.toserial = toserial;
    }

}
