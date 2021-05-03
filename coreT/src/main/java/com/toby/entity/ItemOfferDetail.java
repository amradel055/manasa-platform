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
 * @author IT Shop
 */
@Entity
@Table(name = "item_offer_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemOfferDetail.findAll", query = "SELECT i FROM ItemOfferDetail i")
    , @NamedQuery(name = "ItemOfferDetail.findById", query = "SELECT i FROM ItemOfferDetail i WHERE i.id = :id")
    , @NamedQuery(name = "ItemOfferDetail.findByCreationDate", query = "SELECT i FROM ItemOfferDetail i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "ItemOfferDetail.findByModificationDate", query = "SELECT i FROM ItemOfferDetail i WHERE i.modificationDate = :modificationDate")})
public class ItemOfferDetail  extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @Column(name = "discount_rate")
    private BigDecimal discountRate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;
    @JoinColumn(name = "item_offer", referencedColumnName = "id")
    @ManyToOne
    private ItemOffer itemOffer;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "cost_befor")
    private BigDecimal costBefor;
    @Column(name = "cost_after")
    private BigDecimal costAfter;

    public ItemOfferDetail() {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemOfferDetail)) {
            return false;
        }
        ItemOfferDetail other = (ItemOfferDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ItemOffer[ id=" + id + " ]";
    }

    /**
     * @return the itemOffer
     */
    public ItemOffer getItemOffer() {
        return itemOffer;
    }

    /**
     * @param itemOffer the itemOffer to set
     */
    public void setItemOffer(ItemOffer itemOffer) {
        this.itemOffer = itemOffer;
    }

    /**
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    /**
     * @return the discountRate
     */
    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    /**
     * @param discountRate the discountRate to set
     */
    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return the costBefor
     */
    public BigDecimal getCostBefor() {
        return costBefor;
    }

    /**
     * @param costBefor the costBefor to set
     */
    public void setCostBefor(BigDecimal costBefor) {
        this.costBefor = costBefor;
    }

    /**
     * @return the costAfter
     */
    public BigDecimal getCostAfter() {
        return costAfter;
    }

    /**
     * @param costAfter the costAfter to set
     */
    public void setCostAfter(BigDecimal costAfter) {
        this.costAfter = costAfter;
    }
    
}
