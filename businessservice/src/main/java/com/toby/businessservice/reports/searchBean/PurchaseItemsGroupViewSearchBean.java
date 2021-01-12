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
import com.toby.entity.InventoryDelegator;
import java.util.Date;

/**
 *
 * @author ahmed
 */
public class PurchaseItemsGroupViewSearchBean {

    private Integer branchId;
    private InvInventory inventoryIdFrom;
    private InvInventory inventoryIdTo;
    private InventoryDelegator delegateCodeFrom;
    private InventoryDelegator delegateCodeTo;
    private Date dateFrom;
    private Date dateTo;
    private Integer invoiceNumberFrom;
    private Integer invoiceNumberTo;
    private Integer itemNumberFrom;
    private Integer itemNumberTo;
    private InvOrganizationSite invOrganizationSiteFrom;
    private InvOrganizationSite invOrganizationSiteTo;
    private InvItemDTO invItemForm;
    private InvItemDTO invItemTo;
    private Boolean type;
    private InvPurchaseInvoice salesInvoiceFrom;
    private InvPurchaseInvoice salesInvoiceTo;
    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
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
     * @return the invoiceNumberFrom
     */
    public Integer getInvoiceNumberFrom() {
        return invoiceNumberFrom;
    }

    /**
     * @param invoiceNumberFrom the invoiceNumberFrom to set
     */
    public void setInvoiceNumberFrom(Integer invoiceNumberFrom) {
        this.invoiceNumberFrom = invoiceNumberFrom;
    }

    /**
     * @return the invoiceNumberTo
     */
    public Integer getInvoiceNumberTo() {
        return invoiceNumberTo;
    }

    /**
     * @param invoiceNumberTo the invoiceNumberTo to set
     */
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

    /**
     * @return the invItemForm
     */
    public InvItemDTO getInvItemForm() {
        return invItemForm;
    }

    /**
     * @param invItemForm the invItemForm to set
     */
    public void setInvItemForm(InvItemDTO invItemForm) {
        this.invItemForm = invItemForm;
    }

    /**
     * @return the invItemTo
     */
    public InvItemDTO getInvItemTo() {
        return invItemTo;
    }

    /**
     * @param invItemTo the invItemTo to set
     */
    public void setInvItemTo(InvItemDTO invItemTo) {
        this.invItemTo = invItemTo;
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

}
