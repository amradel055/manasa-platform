/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.api.dto;

import java.util.Date;

/**
 *
 * @author ahmedEssam
 */
public class DistributionApiSearch {

    private Date dateFrom;
    private Date dateTo;
    private Integer InventoryCodeFrom;
    private Integer InventoryCodeTo;
    private Integer customerCodeFrom;
    private Integer  customerCodeTo;
    private Boolean dateType;
    private Boolean inventoryType;
    private Integer  branchId;

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
     * @return the InventoryCodeFrom
     */
    public Integer getInventoryCodeFrom() {
        return InventoryCodeFrom;
    }

    /**
     * @param InventoryCodeFrom the InventoryCodeFrom to set
     */
    public void setInventoryCodeFrom(Integer InventoryCodeFrom) {
        this.InventoryCodeFrom = InventoryCodeFrom;
    }

    /**
     * @return the InventoryCodeTo
     */
    public Integer getInventoryCodeTo() {
        return InventoryCodeTo;
    }

    /**
     * @param InventoryCodeTo the InventoryCodeTo to set
     */
    public void setInventoryCodeTo(Integer InventoryCodeTo) {
        this.InventoryCodeTo = InventoryCodeTo;
    }

    /**
     * @return the customerCodeFrom
     */
    public Integer getCustomerCodeFrom() {
        return customerCodeFrom;
    }

    /**
     * @param customerCodeFrom the customerCodeFrom to set
     */
    public void setCustomerCodeFrom(Integer customerCodeFrom) {
        this.customerCodeFrom = customerCodeFrom;
    }

    /**
     * @return the customerCodeTo
     */
    public Integer getCustomerCodeTo() {
        return customerCodeTo;
    }

    /**
     * @param customerCodeTo the customerCodeTo to set
     */
    public void setCustomerCodeTo(Integer customerCodeTo) {
        this.customerCodeTo = customerCodeTo;
    }

    /**
     * @return the dateType
     */
    public Boolean getDateType() {
        return dateType;
    }

    /**
     * @param dateType the dateType to set
     */
    public void setDateType(Boolean dateType) {
        this.dateType = dateType;
    }

    /**
     * @return the inventoryType
     */
    public Boolean getInventoryType() {
        return inventoryType;
    }

    /**
     * @param inventoryType the inventoryType to set
     */
    public void setInventoryType(Boolean inventoryType) {
        this.inventoryType = inventoryType;
    }

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

    
    
}
