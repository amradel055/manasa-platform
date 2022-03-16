/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
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
 * @author hq003
 */
@Entity
@Table(name = "inventory_setup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventorySetup.findAll", query = "SELECT i FROM InventorySetup i"),
    @NamedQuery(name = "InventorySetup.findById", query = "SELECT i FROM InventorySetup i WHERE i.id = :id"),
    @NamedQuery(name = "InventorySetup.findByCompanyId", query = "SELECT i FROM InventorySetup i WHERE i.companyId = :companyId"),
    @NamedQuery(name = "InventorySetup.findByNegativeSell", query = "SELECT i FROM InventorySetup i WHERE i.negativeSell = :negativeSell"),
    @NamedQuery(name = "InventorySetup.findBySellBuy", query = "SELECT i FROM InventorySetup i WHERE i.sellBuy = :sellBuy"),
    @NamedQuery(name = "InventorySetup.findByInvSystem", query = "SELECT i FROM InventorySetup i WHERE i.invSystem = :invSystem"),
    @NamedQuery(name = "InventorySetup.findBySellAllow", query = "SELECT i FROM InventorySetup i WHERE i.sellAllow = :sellAllow"),
    @NamedQuery(name = "InventorySetup.findByTransfer", query = "SELECT i FROM InventorySetup i WHERE i.transfer = :transfer"),
    @NamedQuery(name = "InventorySetup.findByCreationDate", query = "SELECT i FROM InventorySetup i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InventorySetup.findByModificationDate", query = "SELECT i FROM InventorySetup i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InventorySetup.findByDateSystem", query = "SELECT i FROM InventorySetup i WHERE i.dateSystem = :dateSystem"),
    @NamedQuery(name = "InventorySetup.findByReservationPeriod", query = "SELECT i FROM InventorySetup i WHERE i.reservationPeriod = :reservationPeriod")})
public class InventorySetup extends BaseEntity {

    @Column(name = "negative_sell")
    private Boolean negativeSell;
    @Column(name = "sell_buy")
    private Boolean sellBuy;
    @Column(name = "inv_system")
    private Boolean invSystem;
    @Column(name = "sell_allow")
    private Boolean sellAllow;
    @Column(name = "transfer")
    private Boolean transfer;
    @Column(name = "date_system")
    private Boolean dateSystem;
    @Column(name = "reservation_period")
    private Integer reservationPeriod;    
    @Column(name = "item_coding")
    private Integer itemCoding;
    @Column(name = "default_sales_tax")
    private Boolean defaultSalesTax;
    @Column(name = "delivery_period")
    private Integer deliveryPeriod;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public InventorySetup() {
    }

    public InventorySetup(Integer id) {
        this.id = id;
    }

    public InventorySetup(Integer id, Date creationDate) {
        this.id = id;
    }

    public Boolean getNegativeSell() {
        return negativeSell;
    }

    public void setNegativeSell(Boolean negativeSell) {
        this.negativeSell = negativeSell;
    }

    public Boolean getSellBuy() {
        return sellBuy;
    }

    public void setSellBuy(Boolean sellBuy) {
        this.sellBuy = sellBuy;
    }

    public Boolean getInvSystem() {
        return invSystem;
    }

    public void setInvSystem(Boolean invSystem) {
        this.invSystem = invSystem;
    }

    public Boolean getSellAllow() {
        return sellAllow;
    }

    public void setSellAllow(Boolean sellAllow) {
        this.sellAllow = sellAllow;
    }

    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
        this.transfer = transfer;
    }

    public Boolean getDateSystem() {
        return dateSystem;
    }

    public void setDateSystem(Boolean dateSystem) {
        this.dateSystem = dateSystem;
    }

    public Integer getReservationPeriod() {
        return reservationPeriod;
    }

    public void setReservationPeriod(Integer reservationPeriod) {
        this.reservationPeriod = reservationPeriod;
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
        if (!(object instanceof InventorySetup)) {
            return false;
        }
        InventorySetup other = (InventorySetup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.InventorySetup[ id=" + id + " ]";
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Integer getItemCoding() {
        return itemCoding;
    }

    public void setItemCoding(Integer itemCoding) {
        this.itemCoding = itemCoding;
    }

    public Boolean getDefaultSalesTax() {
        return defaultSalesTax;
    }

    public void setDefaultSalesTax(Boolean defaultSalesTax) {
        this.defaultSalesTax = defaultSalesTax;
    }

    /**
     * @return the deliveryPeriod
     */
    public Integer getDeliveryPeriod() {
        return deliveryPeriod;
    }

    /**
     * @param deliveryPeriod the deliveryPeriod to set
     */
    public void setDeliveryPeriod(Integer deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

}
