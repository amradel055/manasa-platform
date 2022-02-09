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

@Entity
@Table(name = "variation_product")
public class Variation extends BaseEntity {

    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne()
    private InvItem itemId;

    @Column(name = "image")
    private String image;

    @Column(name = "describtion")
    private String describtion;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "default")
    private Integer defaultVariation;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne()
    private Branch branchId;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Variation)) {
            return false;
        }
        Variation other = (Variation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + id + " ]";
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

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the describtion
     */
    public String getDescribtion() {
        return describtion;
    }

    /**
     * @param describtion the describtion to set
     */
    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the salePrice
     */
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    /**
     * @param salePrice the salePrice to set
     */
    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * @return the defaultVariation
     */
    public Integer getDefaultVariation() {
        return defaultVariation;
    }

    /**
     * @param defaultVariation the defaultVariation to set
     */
    public void setDefaultVariation(Integer defaultVariation) {
        this.defaultVariation = defaultVariation;
    }

}
