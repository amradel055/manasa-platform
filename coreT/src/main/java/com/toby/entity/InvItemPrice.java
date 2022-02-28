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
import javax.persistence.Table;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_item_price")
public class InvItemPrice extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "min_price_men")
    private BigDecimal minPriceMen;
    @Column(name = "max_price_men")
    private BigDecimal maxPriceMen;
    @Column(name = "min_price_young")
    private BigDecimal minPriceYoung;
    @Column(name = "max_price_young")
    private BigDecimal maxPriceYoung;
    @JoinColumn(name = "gallary_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory gallaryId;    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;    
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;

    public InvItemPrice() {
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
        if (!(object instanceof InvItemPrice)) {
            return false;
        }
        InvItemPrice other = (InvItemPrice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.InvNotice[ id=" + id + " ]";
    }

    /**
     * @return the minPriceMen
     */
    public BigDecimal getMinPriceMen() {
        return minPriceMen;
    }

    /**
     * @param minPriceMen the minPriceMen to set
     */
    public void setMinPriceMen(BigDecimal minPriceMen) {
        this.minPriceMen = minPriceMen;
    }

    /**
     * @return the maxPriceMen
     */
    public BigDecimal getMaxPriceMen() {
        return maxPriceMen;
    }

    /**
     * @param maxPriceMen the maxPriceMen to set
     */
    public void setMaxPriceMen(BigDecimal maxPriceMen) {
        this.maxPriceMen = maxPriceMen;
    }

    /**
     * @return the minPriceYoung
     */
    public BigDecimal getMinPriceYoung() {
        return minPriceYoung;
    }

    /**
     * @param minPriceYoung the minPriceYoung to set
     */
    public void setMinPriceYoung(BigDecimal minPriceYoung) {
        this.minPriceYoung = minPriceYoung;
    }

    /**
     * @return the maxPriceYoung
     */
    public BigDecimal getMaxPriceYoung() {
        return maxPriceYoung;
    }

    /**
     * @param maxPriceYoung the maxPriceYoung to set
     */
    public void setMaxPriceYoung(BigDecimal maxPriceYoung) {
        this.maxPriceYoung = maxPriceYoung;
    }

    /**
     * @return the gallaryId
     */
    public InvInventory getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(InvInventory gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the itemId
     */
    public InvItem getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

}
