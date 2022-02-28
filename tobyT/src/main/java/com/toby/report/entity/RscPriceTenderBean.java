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
public class RscPriceTenderBean {
   private String unitName;
   private String workUnitName;
    private BigDecimal quantityUnits;
    private BigDecimal priceUnit;
    private BigDecimal totalUnits;
    private BigDecimal totalQuantityUnit;
    private BigDecimal totalCost;
    private BigDecimal quantityIn;
    private BigDecimal profitValue;
    private BigDecimal profitPercentage;
    private BigDecimal priceUnitSell;
    private BigDecimal totalProfit;
    private BigDecimal totalSell;
    private String remark;

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
     * @return the workUnitName
     */
    public String getWorkUnitName() {
        return workUnitName;
    }

    /**
     * @param workUnitName the workUnitName to set
     */
    public void setWorkUnitName(String workUnitName) {
        this.workUnitName = workUnitName;
    }

    public BigDecimal getTotalUnits() {
        return totalUnits;
    }

    /**
     * @param totalUnits the totalUnits to set
     */
    public void setTotalUnits(BigDecimal totalUnits) {
        this.totalUnits = totalUnits;
    }

    /**
     * @return the totalQuantityUnit
     */
    public BigDecimal getTotalQuantityUnit() {
        return totalQuantityUnit;
    }

    /**
     * @param totalQuantityUnit the totalQuantityUnit to set
     */
    public void setTotalQuantityUnit(BigDecimal totalQuantityUnit) {
        this.totalQuantityUnit = totalQuantityUnit;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the quantityIn
     */
    public BigDecimal getQuantityIn() {
        return quantityIn;
    }

    /**
     * @param quantityIn the quantityIn to set
     */
    public void setQuantityIn(BigDecimal quantityIn) {
        this.quantityIn = quantityIn;
    }

    /**
     * @return the profitValue
     */
    public BigDecimal getProfitValue() {
        return profitValue;
    }

    /**
     * @param profitValue the profitValue to set
     */
    public void setProfitValue(BigDecimal profitValue) {
        this.profitValue = profitValue;
    }

    /**
     * @return the profitPercentage
     */
    public BigDecimal getProfitPercentage() {
        return profitPercentage;
    }

    /**
     * @param profitPercentage the profitPercentage to set
     */
    public void setProfitPercentage(BigDecimal profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    /**
     * @return the priceUnitSell
     */
    public BigDecimal getPriceUnitSell() {
        return priceUnitSell;
    }

    /**
     * @param priceUnitSell the priceUnitSell to set
     */
    public void setPriceUnitSell(BigDecimal priceUnitSell) {
        this.priceUnitSell = priceUnitSell;
    }

    /**
     * @return the totalProfit
     */
    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    /**
     * @param totalProfit the totalProfit to set
     */
    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    /**
     * @return the totalSell
     */
    public BigDecimal getTotalSell() {
        return totalSell;
    }

    /**
     * @param totalSell the totalSell to set
     */
    public void setTotalSell(BigDecimal totalSell) {
        this.totalSell = totalSell;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the quantityUnits
     */
    public BigDecimal getQuantityUnits() {
        return quantityUnits;
    }

    /**
     * @param quantityUnits the quantityUnits to set
     */
    public void setQuantityUnits(BigDecimal quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    /**
     * @return the priceUnit
     */
    public BigDecimal getPriceUnit() {
        return priceUnit;
    }

    /**
     * @param priceUnit the priceUnit to set
     */
    public void setPriceUnit(BigDecimal priceUnit) {
        this.priceUnit = priceUnit;
    }

   
}
