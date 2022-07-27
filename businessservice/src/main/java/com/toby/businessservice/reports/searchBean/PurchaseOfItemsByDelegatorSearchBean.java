/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.dto.InvItemDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InventoryDelegator;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hhhh
 */
public class PurchaseOfItemsByDelegatorSearchBean {

    private Integer branchId;
    private Integer type;
    private List<InventoryDelegator> delegatorList;
    private Date dateFrom;
    private Date dateTo;
    private InvItemDTO fromItemName;
    private InvItemDTO toItemName;
    private List<InvInventory>   inventoryList;
    private Integer fromserial;
    private Integer toserial;
    private int frompaymentType;
    private int topaymentType;
    private Boolean showReport;
    private InvPurchaseInvoice salesInvoiceFrom;
    private InvPurchaseInvoice salesInvoiceTo;
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

    /**
     * @return the delegatorList
     */
    public List<InventoryDelegator> getDelegatorList() {
        return delegatorList;
    }

    /**
     * @param delegatorList the delegatorList to set
     */
    public void setDelegatorList(List<InventoryDelegator> delegatorList) {
        this.delegatorList = delegatorList;
    }

    /**
     * @return the inventoryList
     */
    public List<InvInventory> getInventoryList() {
        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

}
