/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import java.math.BigDecimal;

/**
 *
 * @author hhhh
 */
public class InvStripReportViewBean {

    private String itemCode;
    private String itemName;
    private String unitName;
    private BigDecimal qtyIn;
    private BigDecimal qtyOut;
    private BigDecimal balance;
    private BigDecimal costAverage;
    private BigDecimal balanceValue;
    private String inventoryName;
    private String groupName;

    public InvStripReportViewBean() {
    }

    public InvStripReportViewBean(String itemCode, String itemName, String unitName, BigDecimal qtyIn,
            BigDecimal qtyOut, BigDecimal balance, BigDecimal costAverage, BigDecimal balanceValue, String inventoryName, String groupName) {
        this.balance = balance;
        this.balanceValue = balanceValue;
        this.costAverage = costAverage;
        this.groupName = groupName;
        this.inventoryName = inventoryName;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.qtyIn = qtyIn;
        this.qtyOut = qtyOut;
        this.unitName = unitName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getQtyIn() {
        return qtyIn;
    }

    public void setQtyIn(BigDecimal qtyIn) {
        this.qtyIn = qtyIn;
    }

    public BigDecimal getQtyOut() {
        return qtyOut;
    }

    public void setQtyOut(BigDecimal qtyOut) {
        this.qtyOut = qtyOut;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    public BigDecimal getBalanceValue() {
        return balanceValue;
    }

    public void setBalanceValue(BigDecimal balanceValue) {
        this.balanceValue = balanceValue;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
