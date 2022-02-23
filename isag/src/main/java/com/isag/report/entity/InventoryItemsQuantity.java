/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class InventoryItemsQuantity {
    private String itemCode;
    private String itemName;
    private BigDecimal orderLimit;
    private BigDecimal inventory_1;
    private BigDecimal inventory_2;
    private BigDecimal inventory_3;
    private BigDecimal inventory_4;
    private BigDecimal inventory_5;
    
    /**
     * @return the inventory_1
     */
    public BigDecimal getInventory_1() {
        if(inventory_1 == null){
            inventory_1 = BigDecimal.ZERO;
        }
        return inventory_1;
    }

    /**
     * @param inventory_1 the inventory_1 to set
     */
    public void setInventory_1(BigDecimal inventory_1) {
        this.inventory_1 = inventory_1;
    }

    /**
     * @return the inventory_2
     */
    public BigDecimal getInventory_2() {
        if(inventory_2 == null){
            inventory_2 = BigDecimal.ZERO;
        }
        return inventory_2;
    }

    /**
     * @param inventory_2 the inventory_2 to set
     */
    public void setInventory_2(BigDecimal inventory_2) {
        this.inventory_2 = inventory_2;
    }

    /**
     * @return the inventory_3
     */
    public BigDecimal getInventory_3() {
        if(inventory_3 == null){
            inventory_3 = BigDecimal.ZERO;
        }
        return inventory_3;
    }

    /**
     * @param inventory_3 the inventory_3 to set
     */
    public void setInventory_3(BigDecimal inventory_3) {
        this.inventory_3 = inventory_3;
    }

    /**
     * @return the inventory_4
     */
    public BigDecimal getInventory_4() {
        if(inventory_4 == null){
            inventory_4 = BigDecimal.ZERO;
        }
        return inventory_4;
    }

    /**
     * @param inventory_4 the inventory_4 to set
     */
    public void setInventory_4(BigDecimal inventory_4) {
        this.inventory_4 = inventory_4;
    }

    /**
     * @return the inventory_5
     */
    public BigDecimal getInventory_5() {
        if(inventory_5 == null){
            inventory_5 = BigDecimal.ZERO;
        }
        return inventory_5;
    }

    /**
     * @param inventory_5 the inventory_5 to set
     */
    public void setInventory_5(BigDecimal inventory_5) {
        this.inventory_5 = inventory_5;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the orderLimit
     */
    public BigDecimal getOrderLimit() {
        if(orderLimit == null){
            orderLimit = BigDecimal.ZERO;
        }
        return orderLimit;
    }

    /**
     * @param orderLimit the orderLimit to set
     */
    public void setOrderLimit(BigDecimal orderLimit) {
        this.orderLimit = orderLimit;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

  
}
