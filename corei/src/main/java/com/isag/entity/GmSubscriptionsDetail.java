/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author IT Shop
 */
@Entity
@Table(name = "gm_subscriptions_detail")
public class GmSubscriptionsDetail extends BaseEntity{

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "net")
    private BigDecimal net;
    @Column(name = "discount_type")
    private Integer discountType;
    @Column(name = "extra_cost")
    private BigDecimal extraCost;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "tax_value")
    private BigDecimal taxValue;
    @Column(name = "status")
    private Integer status;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;
    @Column(name = "addTotal")
    private BigDecimal addTotal;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @Column(name = "net_after_discount_head")
    private BigDecimal netAfterDiscountHead;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "gm_subscriptions_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GmSubscriptions gmSubscriptionsId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @JoinColumn(name = "item_symbol_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol itemSymbolId;
    
    public GmSubscriptionsDetail() {
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public BigDecimal getAddTotal() {
        return addTotal;
    }

    public void setAddTotal(BigDecimal addTotal) {
        this.addTotal = addTotal;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getNetAfterDiscountHead() {
        return netAfterDiscountHead;
    }

    public void setNetAfterDiscountHead(BigDecimal netAfterDiscountHead) {
        this.netAfterDiscountHead = netAfterDiscountHead;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public GmSubscriptions getGmSubscriptionsId() {
        return gmSubscriptionsId;
    }

    public void setGmSubscriptionsId(GmSubscriptions gmSubscriptionsId) {
        this.gmSubscriptionsId = gmSubscriptionsId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
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
        if (!(object instanceof GmSubscriptionsDetail)) {
            return false;
        }
        GmSubscriptionsDetail other = (GmSubscriptionsDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GmSubscriptionsDetail[ id=" + id + " ]";
    }

    /**
     * @return the itemSymbolId
     */
    public Symbol getItemSymbolId() {
        return itemSymbolId;
    }

    /**
     * @param itemSymbolId the itemSymbolId to set
     */
    public void setItemSymbolId(Symbol itemSymbolId) {
        this.itemSymbolId = itemSymbolId;
    }
    
}
