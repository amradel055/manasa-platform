/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author IT Shop
 */
public class TarhilInvoiceDetail implements Cloneable{

    /**
     * @return the gallaryName
     */
    public String getGallaryName() {
        return gallaryName;
    }

    /**
     * @param gallaryName the gallaryName to set
     */
    public void setGallaryName(String gallaryName) {
        this.gallaryName = gallaryName;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the costCenterId
     */
    public Integer getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        if(totalCost == null){
            totalCost = BigDecimal.ZERO;
        }
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @return the costAccountId
     */
    public Integer getCostAccountId() {
        return costAccountId;
    }

    /**
     * @param costAccountId the costAccountId to set
     */
    public void setCostAccountId(Integer costAccountId) {
        this.costAccountId = costAccountId;
    }

    /**
     * @return the inventoryId
     */
    public Integer getInventoryId() {
        return inventoryId;
    }

    /**
     * @param inventoryId the inventoryId to set
     */
    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }
    
    private Integer costCenterId;
    private Integer inventoryId;
    private BigDecimal totalCost;
    private Integer costAccountId;
    private Integer serial;
    private String gallaryName;
}
