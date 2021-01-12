/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hhhh
 */
@Entity
@Table(name = "inv_openning_balance_item_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findAll", query = "SELECT i FROM InvOpenningBalanceItemDetail i"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findById", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByUnitId", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.unitId = :unitId"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByScrewing", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.screwing = :screwing"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByQuantity", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByCost", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.cost = :cost"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByNet", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.net = :net"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByModificationDate", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByCreationDate", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.creationDate = :creationDate")})
public class InvOpenningBalanceItemDetail extends BaseEntity {

    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "net")
    private BigDecimal net;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "inv_openning_balance_item_id", referencedColumnName = "id")
    @ManyToOne
    private InvOpenningBalanceItem balanceItemId;

    public InvOpenningBalanceItemDetail() {
    }

    public InvOpenningBalanceItemDetail(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvOpenningBalanceItemDetail)) {
            return false;
        }
        InvOpenningBalanceItemDetail other = (InvOpenningBalanceItemDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial != null ? serial.toString() : "";
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal scrawing) {
        this.screwing = scrawing;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public InvOpenningBalanceItem getBalanceItemId() {
        return balanceItemId;
    }

    public void setBalanceItemId(InvOpenningBalanceItem balanceItemId) {
        this.balanceItemId = balanceItemId;
    }
}
