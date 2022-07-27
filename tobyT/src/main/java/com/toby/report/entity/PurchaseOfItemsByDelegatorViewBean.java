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
 * @author hhhh
 */
public class PurchaseOfItemsByDelegatorViewBean {

    private String organizationName;
    private Integer organizationId;
    private String delegatorName;
    private Integer level;
    private Integer delegatorId;
    private Integer serial;
    private Integer index;
    private Date date;
    private String itemCode;
    private String itemName;
    private String itemUnitName;
    private String groupName;
    private BigDecimal itemQuantity;
    private BigDecimal cost;
    private BigDecimal discount;
    private BigDecimal net;
    private BigDecimal thobeNumber;
    private Date dateFromValue;
    private Date dateToValue;

    private String detailUnitName;
    private String itemBarcode;
    private BigDecimal detailItemQuantity;
    private Integer groupId;
    private BigDecimal chartPercentage;

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
     * @return the dateFromValue
     */
    public Date getDateFromValue() {
        return dateFromValue;
    }

    /**
     * @param dateFromValue the dateFromValue to set
     */
    public void setDateFromValue(Date dateFromValue) {
        this.dateFromValue = dateFromValue;
    }

    /**
     * @return the dateToValue
     */
    public Date getDateToValue() {
        return dateToValue;
    }

    /**
     * @param dateToValue the dateToValue to set
     */
    public void setDateToValue(Date dateToValue) {
        this.dateToValue = dateToValue;
    }

    /**
     * @return the organizationId
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the delegatorName
     */
    public String getDelegatorName() {
        return delegatorName;
    }

    /**
     * @param delegatorName the delegatorName to set
     */
    public void setDelegatorName(String delegatorName) {
        this.delegatorName = delegatorName;
    }

    /**
     * @return the delegatorId
     */
    public Integer getDelegatorId() {
        return delegatorId;
    }

    /**
     * @param delegatorId the delegatorId to set
     */
    public void setDelegatorId(Integer delegatorId) {
        this.delegatorId = delegatorId;
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
     * @return the thobeNumber
     */
    public BigDecimal getThobeNumber() {
        return thobeNumber;
    }

    /**
     * @param thobeNumber the thobeNumber to set
     */
    public void setThobeNumber(BigDecimal thobeNumber) {
        this.thobeNumber = thobeNumber;
    }

}
