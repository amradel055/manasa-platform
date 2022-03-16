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
public class ItemCardReportEntity {

    private Integer serial;
    private Integer id;
    private Date date;
    private String transType;
    private BigDecimal qtyin;
    private BigDecimal qtyout;
    private BigDecimal balance;
    private String inventoryCode;
    private String inventoryName;
    private Integer itemId;
    private String itemCode;
    private String itemName;
    private String itemUnitName;
    private String groupName;
    private BigDecimal totalQtyin;
    private BigDecimal totalQtyout;

    private BigDecimal openingBalance;

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public BigDecimal getQtyin() {
        return qtyin;
    }

    public void setQtyin(BigDecimal qtyin) {
        this.qtyin = qtyin;
    }

    public BigDecimal getQtyout() {
        return qtyout;
    }

    public void setQtyout(BigDecimal qtyout) {
        this.qtyout = qtyout;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the totalQtyin
     */
    public BigDecimal getTotalQtyin() {
        return totalQtyin;
    }

    /**
     * @param totalQtyin the totalQtyin to set
     */
    public void setTotalQtyin(BigDecimal totalQtyin) {
        this.totalQtyin = totalQtyin;
    }

    /**
     * @return the totalQtyout
     */
    public BigDecimal getTotalQtyout() {
        return totalQtyout;
    }

    /**
     * @param totalQtyout the totalQtyout to set
     */
    public void setTotalQtyout(BigDecimal totalQtyout) {
        this.totalQtyout = totalQtyout;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
}
