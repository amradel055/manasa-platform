/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.entity.InvInventory;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InventoryDelegator;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class SalesJournalSearchBean {

    /**
     * @return the frompaymentTypeText
     */
    public String getFrompaymentTypeText() {
        return frompaymentTypeText;
    }

    /**
     * @param frompaymentTypeText the frompaymentTypeText to set
     */
    public void setFrompaymentTypeText(String frompaymentTypeText) {
        this.frompaymentTypeText = frompaymentTypeText;
    }

    /**
     * @return the topaymentTypeText
     */
    public String getTopaymentTypeText() {
        return topaymentTypeText;
    }

    /**
     * @param topaymentTypeText the topaymentTypeText to set
     */
    public void setTopaymentTypeText(String topaymentTypeText) {
        this.topaymentTypeText = topaymentTypeText;
    }

    private Integer branchId;
    private Integer type;
    private InvInventory fromInventoryName;
    private InvInventory toInventoryName;
    private InvOrganizationSite fromCustomer;
    private InvOrganizationSite toCustomer;
    private InventoryDelegator fromDelegator;
    private InventoryDelegator toDelegator;
    private Integer frompaymentType;
    private Integer topaymentType;
    private String frompaymentTypeText;
    private String topaymentTypeText;
    private InvPurchaseInvoice fromserial;
    private InvPurchaseInvoice toserial;
    private Integer isagUserId;
    private Boolean isReturn ;
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
     * @return the fromCustomer
     */
    public InvOrganizationSite getFromCustomer() {
        return fromCustomer;
    }

    /**
     * @param fromCustomer the fromCustomer to set
     */
    public void setFromCustomer(InvOrganizationSite fromCustomer) {
        this.fromCustomer = fromCustomer;
    }

    /**
     * @return the toCustomer
     */
    public InvOrganizationSite getToCustomer() {
        return toCustomer;
    }

    /**
     * @param toCustomer the toCustomer to set
     */
    public void setToCustomer(InvOrganizationSite toCustomer) {
        this.toCustomer = toCustomer;
    }

    /**
     * @return the fromserial
     */
   
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
     * @return the fromDelegator
     */
    public InventoryDelegator getFromDelegator() {
        return fromDelegator;
    }

    /**
     * @param fromDelegator the fromDelegator to set
     */
    public void setFromDelegator(InventoryDelegator fromDelegator) {
        this.fromDelegator = fromDelegator;
    }

    /**
     * @return the toDelegator
     */
    public InventoryDelegator getToDelegator() {
        return toDelegator;
    }

    /**
     * @param toDelegator the toDelegator to set
     */
    public void setToDelegator(InventoryDelegator toDelegator) {
        this.toDelegator = toDelegator;
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

    /**
     * @return the isagUserId
     */
    public Integer getIsagUserId() {
        return isagUserId;
    }

    /**
     * @param isagUserId the isagUserId to set
     */
    public void setIsagUserId(Integer isagUserId) {
        this.isagUserId = isagUserId;
    }

    /**
     * @return the isReturn
     */
    public Boolean getIsReturn() {
        if(isReturn == null){
            isReturn = Boolean.FALSE;
        }
        return isReturn;
    }

    /**
     * @param isReturn the isReturn to set
     */
    public void setIsReturn(Boolean isReturn) {
        this.isReturn = isReturn;
    }

}
