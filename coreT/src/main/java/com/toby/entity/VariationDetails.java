/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "variation_product_details")
public class VariationDetails extends BaseEntity {

    @JoinColumn(name = "variation_product_id", referencedColumnName = "id")
    @ManyToOne()
    private Variation variationId;

    @JoinColumn(name = "attribute_detail_id", referencedColumnName = "id")
    @ManyToOne()
    private AttributeDetails attributeDetailsId;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne()
    private Branch branchId;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariationDetails)) {
            return false;
        }
        VariationDetails other = (VariationDetails) object;
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
    /**
     * @return the variationId
     */
    public Variation getVariationId() {
        return variationId;
    }

    /**
     * @param variationId the variationId to set
     */
    public void setVariationId(Variation variationId) {
        this.variationId = variationId;
    }

    /**
     * @return the attributeDetailsId
     */
    public AttributeDetails getAttributeDetailsId() {
        return attributeDetailsId;
    }

    /**
     * @param attributeDetailsId the attributeDetailsId to set
     */
    public void setAttributeDetailsId(AttributeDetails attributeDetailsId) {
        this.attributeDetailsId = attributeDetailsId;
    }

}
