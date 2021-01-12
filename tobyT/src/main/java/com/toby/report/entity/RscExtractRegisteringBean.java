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
public class RscExtractRegisteringBean {
   private String itemName;
   private String code;
   private String unitName;
   private BigDecimal contractQuantity;
   private BigDecimal oldQuantity;
   private BigDecimal currentQuantity;
   private BigDecimal priceSell;
   private BigDecimal currentValue;
   private BigDecimal calculateRampPercentage;
   private BigDecimal rampPercentage;
   private BigDecimal oldRamp;
   private BigDecimal returnRamp;
   private BigDecimal netTotalReturnRamp;
   private BigDecimal valueAfterDiscount;
   private BigDecimal discountValue;
   private BigDecimal discountPercentage;
   private BigDecimal netCost;
   private String remarks;

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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
     * @return the contractQuantity
     */
    public BigDecimal getContractQuantity() {
        return contractQuantity;
    }

    /**
     * @param contractQuantity the contractQuantity to set
     */
    public void setContractQuantity(BigDecimal contractQuantity) {
        this.contractQuantity = contractQuantity;
    }

    /**
     * @return the oldQuantity
     */
    public BigDecimal getOldQuantity() {
        return oldQuantity;
    }

    /**
     * @param oldQuantity the oldQuantity to set
     */
    public void setOldQuantity(BigDecimal oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    /**
     * @return the currentQuantity
     */
    public BigDecimal getCurrentQuantity() {
        return currentQuantity;
    }

    /**
     * @param currentQuantity the currentQuantity to set
     */
    public void setCurrentQuantity(BigDecimal currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    /**
     * @return the priceSell
     */
    public BigDecimal getPriceSell() {
        return priceSell;
    }

    /**
     * @param priceSell the priceSell to set
     */
    public void setPriceSell(BigDecimal priceSell) {
        this.priceSell = priceSell;
    }

    /**
     * @return the currentValue
     */
    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    /**
     * @param currentValue the currentValue to set
     */
    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * @return the calculateRampPercentage
     */
    public BigDecimal getCalculateRampPercentage() {
        return calculateRampPercentage;
    }

    /**
     * @param calculateRampPercentage the calculateRampPercentage to set
     */
    public void setCalculateRampPercentage(BigDecimal calculateRampPercentage) {
        this.calculateRampPercentage = calculateRampPercentage;
    }

    /**
     * @return the rampPercentage
     */
    public BigDecimal getRampPercentage() {
        return rampPercentage;
    }

    /**
     * @param rampPercentage the rampPercentage to set
     */
    public void setRampPercentage(BigDecimal rampPercentage) {
        this.rampPercentage = rampPercentage;
    }

    /**
     * @return the oldRamp
     */
    public BigDecimal getOldRamp() {
        return oldRamp;
    }

    /**
     * @param oldRamp the oldRamp to set
     */
    public void setOldRamp(BigDecimal oldRamp) {
        this.oldRamp = oldRamp;
    }

    /**
     * @return the returnRamp
     */
    public BigDecimal getReturnRamp() {
        return returnRamp;
    }

    /**
     * @param returnRamp the returnRamp to set
     */
    public void setReturnRamp(BigDecimal returnRamp) {
        this.returnRamp = returnRamp;
    }

    /**
     * @return the netTotalReturnRamp
     */
    public BigDecimal getNetTotalReturnRamp() {
        return netTotalReturnRamp;
    }

    /**
     * @param netTotalReturnRamp the netTotalReturnRamp to set
     */
    public void setNetTotalReturnRamp(BigDecimal netTotalReturnRamp) {
        this.netTotalReturnRamp = netTotalReturnRamp;
    }

    /**
     * @return the valueAfterDiscount
     */
    public BigDecimal getValueAfterDiscount() {
        return valueAfterDiscount;
    }

    /**
     * @param valueAfterDiscount the valueAfterDiscount to set
     */
    public void setValueAfterDiscount(BigDecimal valueAfterDiscount) {
        this.valueAfterDiscount = valueAfterDiscount;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return the discountPercentage
     */
    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * @param discountPercentage the discountPercentage to set
     */
    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    /**
     * @return the netCost
     */
    public BigDecimal getNetCost() {
        return netCost;
    }

    /**
     * @param netCost the netCost to set
     */
    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
