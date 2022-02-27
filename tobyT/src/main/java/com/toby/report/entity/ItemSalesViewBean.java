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
    private BigDecimal itemQuantity;
    private BigDecimal net;
    private BigDecimal cost;
    private BigDecimal discount;
    private BigDecimal detailItemQuantity;
    private BigDecimal chartPercentage;
    private Integer groupId;
    private Integer serial;
    private Date date;

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
}
