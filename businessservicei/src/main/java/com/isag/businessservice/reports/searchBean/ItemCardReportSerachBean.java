/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InvItemDTO;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private List<String> groupSelected;
    private InvItem fromItem;
    private InvItem toItem;
    private BigDecimal saveAmount;

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
     * @return the groupSelected
     */
    public List<String> getGroupSelected() {
        return groupSelected;
    }

    /**
     * @param groupSelected the groupSelected to set
     */
    public void setGroupSelected(List<String> groupSelected) {
        this.groupSelected = groupSelected;
    }

    /**
     * @return the fromItem
     */
    public InvItem getFromItem() {
        return fromItem;
    }

    /**
     * @param fromItem the fromItem to set
     */
    public void setFromItem(InvItem fromItem) {
        this.fromItem = fromItem;
    }

    /**
     * @return the toItem
     */
    public InvItem getToItem() {
        return toItem;
    }

    /**
     * @param toItem the toItem to set
     */
    public void setToItem(InvItem toItem) {
        this.toItem = toItem;
    }

    /**
     * @return the saveAmount
     */
    public BigDecimal getSaveAmount() {
        if(saveAmount == null){
            saveAmount = BigDecimal.ZERO;
        }
        return saveAmount;
    }

    /**
     * @param saveAmount the saveAmount to set
     */
    public void setSaveAmount(BigDecimal saveAmount) {
        this.saveAmount = saveAmount;
    }
}
