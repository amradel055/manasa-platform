/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "quantity_items_store_by_date")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuantityItemsStoreByDate.findAll", query = "SELECT q FROM QuantityItemsStoreByDate q")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByRowNum", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.rowNum = :rowNum")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByItemId", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.itemId = :itemId")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByItemCode", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.itemCode = :itemCode")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByItemName", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.itemName = :itemName")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByBranchId", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.branchId = :branchId")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByInventoryId", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.inventoryId = :inventoryId")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByInventoryName", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.inventoryName = :inventoryName")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByInventoryCode", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.inventoryCode = :inventoryCode")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByScreenCode", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.screenCode = :screenCode")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByDate", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.date = :date")
    , @NamedQuery(name = "QuantityItemsStoreByDate.findByQty", query = "SELECT q FROM QuantityItemsStoreByDate q WHERE q.qty = :qty")})
public class QuantityItemsStoreByDate implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Column(name = "screen_code")
    private long screenCode;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qty")
    private BigDecimal qty;

    public QuantityItemsStoreByDate() {
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

    public long getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(long screenCode) {
        this.screenCode = screenCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
    
}
