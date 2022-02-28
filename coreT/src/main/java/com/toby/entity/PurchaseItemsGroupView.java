/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
 * @author ahmed
 */
@Entity
@Table(name = "purchase_items_group_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseItemsGroupView.findAll", query = "SELECT s FROM PurchaseItemsGroupView s")
    , @NamedQuery(name = "PurchaseItemsGroupView.findBySerial", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.serial = :serial")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByInvInventoryId", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.invInventoryId = :invInventoryId")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByDate", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.date = :date")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByBranchId", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.branchId = :branchId")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByInvoiceNumber", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.invoiceNumber = :invoiceNumber")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByNet", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.net = :net")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByGroupName", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.groupName = :groupName")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByGroupId", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.groupId = :groupId")
    , @NamedQuery(name = "PurchaseItemsGroupView.findByItemNumber", query = "SELECT s FROM PurchaseItemsGroupView s WHERE s.itemNumber = :itemNumber")})
public class PurchaseItemsGroupView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "inv_inventory_id")
    private Integer invInventoryId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "invoiceNumber")
    private Integer invoiceNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "net")
    private BigDecimal net;
    @Size(max = 45)
    @Column(name = "groupName")
    private String groupName;
    @Column(name = "groupId")
    private Integer groupId;
    @Size(max = 45)
    @Column(name = "ItemNumber")
    private String itemNumber;
    @Column(name = "type")
    private Boolean type;

    public PurchaseItemsGroupView() {
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Integer getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(Integer invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
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

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * @return the rowNum
     */
    public Integer getRowNum() {
        return rowNum;
    }

    /**
     * @param rowNum the rowNum to set
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * @return the type
     */
    public Boolean getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Boolean type) {
        this.type = type;
    }

}
