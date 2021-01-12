/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class StripReportViewBean {

    private String nameHoldPlace;
    private String groupName;
    private String itemCode;
    private String itemName;
    private String unitName;
    private BigDecimal qtyIn;
    private BigDecimal qtyOut;
    private BigDecimal balance;
    private BigDecimal costAverage;
    private BigDecimal balanceValue;
    private BigDecimal sellPrice;
    private BigDecimal weight;
    private Integer groupId;
    private Integer level;

    //  private String inventoryName;
    //private Integer groupId;
    // private String groupName;
    /**
     * @return the nameHoldPlace
     */
    public String getNameHoldPlace() {
        return nameHoldPlace;
    }

    /**
     * @param nameHoldPlace the nameHoldPlace to set
     */
    public void setNameHoldPlace(String nameHoldPlace) {
        this.nameHoldPlace = nameHoldPlace;
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
     * @return the unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName the unitName to set
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * @return the qtyIn
     */
    public BigDecimal getQtyIn() {
        return qtyIn;
    }

    /**
     * @param qtyIn the qtyIn to set
     */
    public void setQtyIn(BigDecimal qtyIn) {
        this.qtyIn = qtyIn;
    }

    /**
     * @return the qtyOut
     */
    public BigDecimal getQtyOut() {
        return qtyOut;
    }

    /**
     * @param qtyOut the qtyOut to set
     */
    public void setQtyOut(BigDecimal qtyOut) {
        this.qtyOut = qtyOut;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the costAverage
     */
    public BigDecimal getCostAverage() {
        return costAverage;
    }

    /**
     * @param costAverage the costAverage to set
     */
    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    /**
     * @return the balanceValue
     */
    public BigDecimal getBalanceValue() {
        return balanceValue;
    }

    /**
     * @param balanceValue the balanceValue to set
     */
    public void setBalanceValue(BigDecimal balanceValue) {
        this.balanceValue = balanceValue;
    }

    /**
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the sellPrice
     */
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    /**
     * @param sellPrice the sellPrice to set
     */
    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

}
