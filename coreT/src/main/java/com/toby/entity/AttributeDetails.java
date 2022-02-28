/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "attribute_details")
public class AttributeDetails extends BaseEntity {
    
    @JoinColumn(name = "head_id", referencedColumnName = "id")
    @ManyToOne()
    private Attribute headId;
    
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne()
    private InvItem itemId;
    
    @Column(name = "order_by")
    private Integer orderBy;
   

    @Column(name = "value")
    private String value;


    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne()
    private Branch branchId;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttributeDetails)) {
            return false;
        }
        AttributeDetails other = (AttributeDetails) object;
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
     * @return the headId
     */
    public Attribute getHeadId() {
        return headId;
    }

    /**
     * @param headId the headId to set
     */
    public void setHeadId(Attribute headId) {
        this.headId = headId;
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
     * @return the orderBy
     */
    public Integer getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
