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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "offers_details")
public class OffersDetails extends BaseEntity {

    @JoinColumn(name = "offer_id", referencedColumnName = "id")
    @ManyToOne()
    private Offers parentOfferId;

    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne()
    private InvItem itemId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "status")
    private Integer status;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne()
    private Branch branchId;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OffersDetails)) {
            return false;
        }
        OffersDetails other = (OffersDetails) object;
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
     * @return the parentOfferId
     */
    public Offers getParentOfferId() {
        return parentOfferId;
    }

    /**
     * @param parentOfferId the parentOfferId to set
     */
    public void setParentOfferId(Offers parentOfferId) {
        this.parentOfferId = parentOfferId;
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
     * @return the categoryId
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
