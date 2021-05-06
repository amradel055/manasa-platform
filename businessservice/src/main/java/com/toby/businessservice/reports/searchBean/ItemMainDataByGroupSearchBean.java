/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.entity.InvGroup;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hhhh
 */
public class ItemMainDataByGroupSearchBean {

    

    private Integer branchId;

    private InvGroup fromGroup;
    private InvGroup toGroup;
    private List<String> groupSelected;
    private InvItem fromItem;
    private InvItem toItem;
    private InvInventory frominventory;
    private InvInventory toinventory;
    private Boolean showReport;
    private Date dateFrom;
    private Date dateTo;
    private int supplier ;
    
    
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
     * @return the fromGroup
     */
    public InvGroup getFromGroup() {
        return fromGroup;
    }

    /**
     * @param fromGroup the fromGroup to set
     */
    public void setFromGroup(InvGroup fromGroup) {
        this.fromGroup = fromGroup;
    }

    /**
     * @return the toGroup
     */
    public InvGroup getToGroup() {
        return toGroup;
    }

    /**
     * @param toGroup the toGroup to set
     */
    public void setToGroup(InvGroup toGroup) {
        this.toGroup = toGroup;
    }

    /**
     * @return the frominventory
     */
    public InvInventory getFrominventory() {
        return frominventory;
    }

    /**
     * @param frominventory the frominventory to set
     */
    public void setFrominventory(InvInventory frominventory) {
        this.frominventory = frominventory;
    }

    /**
     * @return the toinventory
     */
    public InvInventory getToinventory() {
        return toinventory;
    }

    /**
     * @param toinventory the toinventory to set
     */
    public void setToinventory(InvInventory toinventory) {
        this.toinventory = toinventory;
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
     * @return the supplier
     */
    public int getSupplier() {
        return supplier;
    }

    /**
     * @param supplier the supplier to set
     */
    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }
}
