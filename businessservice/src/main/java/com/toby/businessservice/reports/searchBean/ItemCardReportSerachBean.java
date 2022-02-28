/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.dto.InvItemDTO;
import com.toby.entity.InvInventory;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class ItemCardReportSerachBean {

    private Date dateFrom;
    private Date dateTo;
    private InvInventory inventoryFrom;
    private InvInventory inventoryTo;
    private InvItemDTO invItemForm;
    private InvItemDTO invItemTo;
    private Integer branchId;
    private Integer invoiceType;

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

    public InvItemDTO getInvItemForm() {
        return invItemForm;
    }

    public void setInvItemForm(InvItemDTO invItemForm) {
        this.invItemForm = invItemForm;
    }

    public InvItemDTO getInvItemTo() {
        return invItemTo;
    }

    public void setInvItemTo(InvItemDTO invItemTo) {
        this.invItemTo = invItemTo;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the invoiceType
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }
}
