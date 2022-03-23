/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

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
 * @author hhhh
 */
@Entity
@Table(name = "item_main_data_by_group_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemMainDataByGroupView.findAll", query = "SELECT i FROM ItemMainDataByGroupView i"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByRowNum", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.rowNum = :rowNum"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByItemId", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.itemId = :itemId"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByItemName", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.itemName = :itemName"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByItemCode", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.itemCode = :itemCode"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByCostAverage", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.costAverage = :costAverage"),
     @NamedQuery(name = "ItemMainDataByGroupView.findBySellPrice", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.sellPrice = :sellPrice"),
    
    @NamedQuery(name = "ItemMainDataByGroupView.findByBranchId", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.branchId = :branchId"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByInventoryId", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.inventoryId = :inventoryId"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByGroupCode", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.groupCode = :groupCode"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByGroupName", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.groupName = :groupName"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByGroupId", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.groupId = :groupId"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByInventoryName", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.inventoryName = :inventoryName"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByInventoryCode", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.inventoryCode = :inventoryCode"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByItemUnitName", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.itemUnitName = :itemUnitName"),
    @NamedQuery(name = "ItemMainDataByGroupView.findByQty", query = "SELECT i FROM ItemMainDataByGroupView i WHERE i.qty = :qty")})
public class ItemMainDataByGroupView implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "item_id")
    private Integer itemId;
    @Size(max = 45)
    @Column(name = "item_name")
    private String itemName;
    @Size(max = 45)
    @Column(name = "item_code")
    private String itemCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    @Column(name = "sell_price")
    private String sellPrice;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Size(max = 450)
    @Column(name = "group_code")
    private String groupCode;
    @Size(max = 45)
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "group_id")
    private Integer groupId;
    @Size(max = 45)
    @Column(name = "inventory_name")
    private String inventoryName;
    @Size(max = 45)
    @Column(name = "inventory_code")
    private String inventoryCode;
    @Size(max = 255)
    @Column(name = "item_unit_name")
    private String itemUnitName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qty")
    private BigDecimal qty;

    public ItemMainDataByGroupView() {
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

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
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

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * @return the sellPrice
     */
    public String getSellPrice() {
        return sellPrice;
    }

    /**
     * @param sellPrice the sellPrice to set
     */
    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }
    
}
