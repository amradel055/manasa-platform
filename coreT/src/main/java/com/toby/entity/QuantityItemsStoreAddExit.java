/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H
 */
@Entity
@Table(name = "quantity_items_store_add_exit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuantityItemsStoreAddExit.findAll", query = "SELECT q FROM QuantityItemsStoreAddExit q"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByRowNum", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.rowNum = :rowNum"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByItemId", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.itemId = :itemId"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByItemCode", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.itemCode = :itemCode"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByItemName", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.itemName = :itemName"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByBranchId", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.branchId = :branchId"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByInventoryId", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.inventoryId = :inventoryId"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByInventoryName", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.inventoryName = :inventoryName"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByInventoryCode", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.inventoryCode = :inventoryCode"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByCostAverage", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.costAverage = :costAverage"),
    @NamedQuery(name = "QuantityItemsStoreAddExit.findByQty", query = "SELECT q FROM QuantityItemsStoreAddExit q WHERE q.qty = :qty")})
public class QuantityItemsStoreAddExit extends BaseEntity implements Serializable  {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "item_id")
    private Integer itemId;
    @Size(max = 45)
    @Column(name = "item_code")
    private String itemCode;
    @Size(max = 450)
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Size(max = 45)
    @Column(name = "inventory_name")
    private String inventoryName;
    @Size(max = 45)
    @Column(name = "inventory_code")
    private String inventoryCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    @Column(name = "qty")
    private BigDecimal qty;

    public QuantityItemsStoreAddExit() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
    
}
