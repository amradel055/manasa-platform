/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.entity.BaseEntity;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "quantity_items_store")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuantityItemsStore.findAll", query = "SELECT q FROM QuantityItemsStore q"),
    @NamedQuery(name = "QuantityItemsStore.findByRowNum", query = "SELECT q FROM QuantityItemsStore q WHERE q.rowNum = :rowNum"),
    @NamedQuery(name = "QuantityItemsStore.findByItemId", query = "SELECT q FROM QuantityItemsStore q WHERE q.itemId = :itemId"),
    @NamedQuery(name = "QuantityItemsStore.findByBranchId", query = "SELECT q FROM QuantityItemsStore q WHERE q.branchId = :branchId"),
    @NamedQuery(name = "QuantityItemsStore.findByInventoryId", query = "SELECT q FROM QuantityItemsStore q WHERE q.inventoryId = :inventoryId"),
    @NamedQuery(name = "QuantityItemsStore.findByQty", query = "SELECT q FROM QuantityItemsStore q WHERE q.qty = :qty")})
public class QuantityItemsStore  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Column(name = "inventory_name")
    private String inventoryName;
    @Column(name = "inventory_code")
    private String inventoryCode;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_code")
    private String itemCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private BigDecimal qty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    
    public QuantityItemsStore() {
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public String toString() {
        return itemName + " " + itemCode;
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
}
