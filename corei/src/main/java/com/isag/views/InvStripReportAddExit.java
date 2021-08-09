/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

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
@Table(name = "inv_strip_report_add_exit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvStripReportAddExit.findAll", query = "SELECT i FROM InvStripReportAddExit i")
    , @NamedQuery(name = "InvStripReportAddExit.findById", query = "SELECT i FROM InvStripReportAddExit i WHERE i.id = :id")
    , @NamedQuery(name = "InvStripReportAddExit.findByDate", query = "SELECT i FROM InvStripReportAddExit i WHERE i.date = :date")
    , @NamedQuery(name = "InvStripReportAddExit.findByInventoryId", query = "SELECT i FROM InvStripReportAddExit i WHERE i.inventoryId = :inventoryId")
    , @NamedQuery(name = "InvStripReportAddExit.findByInventoryCode", query = "SELECT i FROM InvStripReportAddExit i WHERE i.inventoryCode = :inventoryCode")
    , @NamedQuery(name = "InvStripReportAddExit.findByGroupId", query = "SELECT i FROM InvStripReportAddExit i WHERE i.groupId = :groupId")
    , @NamedQuery(name = "InvStripReportAddExit.findByGroupName", query = "SELECT i FROM InvStripReportAddExit i WHERE i.groupName = :groupName")
    , @NamedQuery(name = "InvStripReportAddExit.findByItemId", query = "SELECT i FROM InvStripReportAddExit i WHERE i.itemId = :itemId")
    , @NamedQuery(name = "InvStripReportAddExit.findByItemName", query = "SELECT i FROM InvStripReportAddExit i WHERE i.itemName = :itemName")
    , @NamedQuery(name = "InvStripReportAddExit.findByItemCode", query = "SELECT i FROM InvStripReportAddExit i WHERE i.itemCode = :itemCode")
    , @NamedQuery(name = "InvStripReportAddExit.findByItemUnitName", query = "SELECT i FROM InvStripReportAddExit i WHERE i.itemUnitName = :itemUnitName")
    , @NamedQuery(name = "InvStripReportAddExit.findByQtyin", query = "SELECT i FROM InvStripReportAddExit i WHERE i.qtyin = :qtyin")
    , @NamedQuery(name = "InvStripReportAddExit.findByQtyout", query = "SELECT i FROM InvStripReportAddExit i WHERE i.qtyout = :qtyout")
    , @NamedQuery(name = "InvStripReportAddExit.findByBalance", query = "SELECT i FROM InvStripReportAddExit i WHERE i.balance = :balance")
    , @NamedQuery(name = "InvStripReportAddExit.findByCostAverage", query = "SELECT i FROM InvStripReportAddExit i WHERE i.costAverage = :costAverage")
    , @NamedQuery(name = "InvStripReportAddExit.findByBalancevalue", query = "SELECT i FROM InvStripReportAddExit i WHERE i.balancevalue = :balancevalue")
    , @NamedQuery(name = "InvStripReportAddExit.findByBranchId", query = "SELECT i FROM InvStripReportAddExit i WHERE i.branchId = :branchId")})
public class InvStripReportAddExit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Size(max = 45)
    @Column(name = "inventory_code")
    private String inventoryCode;
    @Column(name = "group_id")
    private Integer groupId;
    @Size(max = 45)
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "item_id")
    private Integer itemId;
    @Size(max = 45)
    @Column(name = "item_name")
    private String itemName;
    @Size(max = 45)
    @Column(name = "item_code")
    private String itemCode;
    @Size(max = 255)
    @Column(name = "item_unit_name")
    private String itemUnitName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qtyin")
    private BigDecimal qtyin;
    @Column(name = "qtyout")
    private BigDecimal qtyout;
    @Column(name = "balance")
    private BigDecimal balance;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    @Column(name = "balancevalue")
    private BigDecimal balancevalue;
    @Column(name = "branch_id")
    private Integer branchId;

    public InvStripReportAddExit() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
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

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    public BigDecimal getBalancevalue() {
        return balancevalue;
    }

    public void setBalancevalue(BigDecimal balancevalue) {
        this.balancevalue = balancevalue;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
    
}
