/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ahmed
 */
public class ItemSalesViewBean {
    
    private Integer index;
    private String itemCode;
    private String itemName;
    private String itemUnitName;
    private String detailUnitName;
    private String itemBarcode;
    private String groupName;
    private String supplierName;
    private Integer serialTax;
    private BigDecimal itemQuantity;
    private BigDecimal net;
    private BigDecimal cost;
    private BigDecimal discount;
    private BigDecimal detailItemQuantity;
    private BigDecimal chartPercentage;
    private Integer groupId;
    private Integer serial;
    private Date date;
    private BigDecimal numberThobe;
    private BigDecimal quantityOfOneUnit;
    private BigDecimal costAvarage;
    private BigDecimal fash;
    private BigDecimal costAvarageFashTotal;
    private BigDecimal totalQuantity;
    private BigDecimal totalcostAvarage;

    /**
     * @return the detailUnitName
     */
    public String getDetailUnitName() {
        return detailUnitName;
    }

    /**
     * @param detailUnitName the detailUnitName to set
     */
    public void setDetailUnitName(String detailUnitName) {
        this.detailUnitName = detailUnitName;
    }

    /**
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
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
     * @return the itemUnitName
     */
    public String getItemUnitName() {
        return itemUnitName;
    }

    /**
     * @param itemUnitName the itemUnitName to set
     */
    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    /**
     * @return the itemQuantity
     */
    public BigDecimal getItemQuantity() {
        if(itemQuantity == null){
            itemQuantity = BigDecimal.ZERO;
        }
        return itemQuantity;
    }

    /**
     * @param itemQuantity the itemQuantity to set
     */
    public void setItemQuantity(BigDecimal itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
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
     * @return the detailItemQuantity
     */
    public BigDecimal getDetailItemQuantity() {
        return detailItemQuantity;
    }

    /**
     * @param detailItemQuantity the detailItemQuantity to set
     */
    public void setDetailItemQuantity(BigDecimal detailItemQuantity) {
        this.detailItemQuantity = detailItemQuantity;
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
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the chartPercentage
     */
    public BigDecimal getChartPercentage() {
        return chartPercentage;
    }

    /**
     * @param chartPercentage the chartPercentage to set
     */
    public void setChartPercentage(BigDecimal chartPercentage) {
        this.chartPercentage = chartPercentage;
    }

    /**
     * @return the serialTax
     */
    public Integer getSerialTax() {
        return serialTax;
    }

    /**
     * @param serialTax the serialTax to set
     */
    public void setSerialTax(Integer serialTax) {
        this.serialTax = serialTax;
    }

    /**
     * @return the numberThobe
     */
    public BigDecimal getNumberThobe() {
        return numberThobe;
    }

    /**
     * @param numberThobe the numberThobe to set
     */
    public void setNumberThobe(BigDecimal numberThobe) {
        this.numberThobe = numberThobe;
    }

    /**
     * @return the quantityOfOneUnit
     */
    public BigDecimal getQuantityOfOneUnit() {
        return quantityOfOneUnit;
    }

    /**
     * @param quantityOfOneUnit the quantityOfOneUnit to set
     */
    public void setQuantityOfOneUnit(BigDecimal quantityOfOneUnit) {
        this.quantityOfOneUnit = quantityOfOneUnit;
    }

    /**
     * @return the costAvarage
     */
    public BigDecimal getCostAvarage() {
        return costAvarage;
    }

    /**
     * @param costAvarage the costAvarage to set
     */
    public void setCostAvarage(BigDecimal costAvarage) {
        this.costAvarage = costAvarage;
    }

    /**
     * @return the fash
     */
    public BigDecimal getFash() {
        if(fash == null){
            fash = BigDecimal.ZERO;
        }
        return fash;
    }

    /**
     * @param fash the fash to set
     */
    public void setFash(BigDecimal fash) {
        this.fash = fash;
    }

    /**
     * @return the costAvarageFashTotal
     */
    public BigDecimal getCostAvarageFashTotal() {
        return costAvarageFashTotal;
    }

    /**
     * @param costAvarageFashTotal the costAvarageFashTotal to set
     */
    public void setCostAvarageFashTotal(BigDecimal costAvarageFashTotal) {
        this.costAvarageFashTotal = costAvarageFashTotal;
    }

    /**
     * @return the totalQuantity
     */
    public BigDecimal getTotalQuantity() {
        totalQuantity = getItemQuantity().add(getFash());
        return totalQuantity;
    }

    /**
     * @param totalQuantity the totalQuantity to set
     */
    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    /**
     * @return the totalcostAvarage
     */
    public BigDecimal getTotalcostAvarage() {
        return totalcostAvarage;
    }

    /**
     * @param totalcostAvarage the totalcostAvarage to set
     */
    public void setTotalcostAvarage(BigDecimal totalcostAvarage) {
        this.totalcostAvarage = totalcostAvarage;
    }

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
