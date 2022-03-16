/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventoryDelegator;
import java.util.Date;

/**
 *
 * @author ahmed
 */
public class InvDelegatorSalesByMainItemsGroupSearchBean {

    private Integer branchId;
    private InvInventory inventoryIdFrom;
    private InvInventory inventoryIdTo;
    private InventoryDelegator delegateCodeFrom;
    private InventoryDelegator delegateCodeTo;
    private Date dateFrom;
    private Date dateTo;
    private Integer invoiceNumberFrom;
    private Integer invoiceNumberTo;
    private InvOrganizationSite invOrganizationSiteFrom;
    private InvOrganizationSite invOrganizationSiteTo;
    private boolean typeView;

    private Integer itemNumberFrom;
    private Integer itemNumberTo;

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

    public Integer getInvoiceNumberFrom() {
        return invoiceNumberFrom;
    }

    public void setInvoiceNumberFrom(Integer invoiceNumberFrom) {
        this.invoiceNumberFrom = invoiceNumberFrom;
    }

    public Integer getInvoiceNumberTo() {
        return invoiceNumberTo;
    }

    public void setInvoiceNumberTo(Integer invoiceNumberTo) {
        this.invoiceNumberTo = invoiceNumberTo;
    }

    /**
     * @return the invOrganizationSiteFrom
     */
    public InvOrganizationSite getInvOrganizationSiteFrom() {
        return invOrganizationSiteFrom;
    }

    /**
     * @param invOrganizationSiteFrom the invOrganizationSiteFrom to set
     */
    public void setInvOrganizationSiteFrom(InvOrganizationSite invOrganizationSiteFrom) {
        this.invOrganizationSiteFrom = invOrganizationSiteFrom;
    }

    /**
     * @return the invOrganizationSiteTo
     */
    public InvOrganizationSite getInvOrganizationSiteTo() {
        return invOrganizationSiteTo;
    }

    /**
     * @param invOrganizationSiteTo the invOrganizationSiteTo to set
     */
    public void setInvOrganizationSiteTo(InvOrganizationSite invOrganizationSiteTo) {
        this.invOrganizationSiteTo = invOrganizationSiteTo;
    }

    /**
     * @return the delegateCodeFrom
     */
    public InventoryDelegator getDelegateCodeFrom() {
        return delegateCodeFrom;
    }

    /**
     * @param delegateCodeFrom the delegateCodeFrom to set
     */
    public void setDelegateCodeFrom(InventoryDelegator delegateCodeFrom) {
        this.delegateCodeFrom = delegateCodeFrom;
    }

    /**
     * @return the delegateCodeTo
     */
    public InventoryDelegator getDelegateCodeTo() {
        return delegateCodeTo;
    }

    /**
     * @param delegateCodeTo the delegateCodeTo to set
     */
    public void setDelegateCodeTo(InventoryDelegator delegateCodeTo) {
        this.delegateCodeTo = delegateCodeTo;
    }

    /**
     * @return the inventoryIdFrom
     */
    public InvInventory getInventoryIdFrom() {
        return inventoryIdFrom;
    }

    /**
     * @param inventoryIdFrom the inventoryIdFrom to set
     */
    public void setInventoryIdFrom(InvInventory inventoryIdFrom) {
        this.inventoryIdFrom = inventoryIdFrom;
    }

    /**
     * @return the inventoryIdTo
     */
    public InvInventory getInventoryIdTo() {
        return inventoryIdTo;
    }

    /**
     * @param inventoryIdTo the inventoryIdTo to set
     */
    public void setInventoryIdTo(InvInventory inventoryIdTo) {
        this.inventoryIdTo = inventoryIdTo;
    }

    /**
     * @return the typeView
     */
    public boolean isTypeView() {
        return typeView;
    }

    /**
     * @param typeView the typeView to set
     */
    public void setTypeView(boolean typeView) {
        this.typeView = typeView;
    }

    /**
     * @return the itemNumberFrom
     */
    public Integer getItemNumberFrom() {
        return itemNumberFrom;
    }

    /**
     * @param itemNumberFrom the itemNumberFrom to set
     */
    public void setItemNumberFrom(Integer itemNumberFrom) {
        this.itemNumberFrom = itemNumberFrom;
    }

    /**
     * @return the itemNumberTo
     */
    public Integer getItemNumberTo() {
        return itemNumberTo;
    }

    /**
     * @param itemNumberTo the itemNumberTo to set
     */
    public void setItemNumberTo(Integer itemNumberTo) {
        this.itemNumberTo = itemNumberTo;
    }

}
