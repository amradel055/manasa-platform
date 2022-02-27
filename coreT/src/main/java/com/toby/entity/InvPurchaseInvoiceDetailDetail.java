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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_purchase_invoice_detail_detail")
@XmlRootElement
public class InvPurchaseInvoiceDetailDetail extends BaseEntity {

    
    @Column(name = "is_deleted")
    private Integer isdeleted;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_purchase_invoice_detail_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoiceDetail invPurchaseInvoiceDetail;
    @JoinColumn(name = "pro_detail_type", referencedColumnName = "id")
    @ManyToOne
    private ProDetailType proDetailType;
    
    public InvPurchaseInvoiceDetailDetail() {
    }

    

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof InvPurchaseInvoiceDetailDetail)) {
            return false;
        }
        InvPurchaseInvoiceDetailDetail other = (InvPurchaseInvoiceDetailDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "";
    }

    /**
     * @return the isdeleted
     */
    public Integer getIsdeleted() {
        return isdeleted;
    }

    /**
     * @param isdeleted the isdeleted to set
     */
    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
    }

    /**
     * @return the invPurchaseInvoiceDetail
     */
    public InvPurchaseInvoiceDetail getInvPurchaseInvoiceDetail() {
        return invPurchaseInvoiceDetail;
    }

    /**
     * @param invPurchaseInvoiceDetail the invPurchaseInvoiceDetail to set
     */
    public void setInvPurchaseInvoiceDetail(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        this.invPurchaseInvoiceDetail = invPurchaseInvoiceDetail;
    }

    /**
     * @return the proDetailType
     */
    public ProDetailType getProDetailType() {
        return proDetailType;
    }

    /**
     * @param proDetailType the proDetailType to set
     */
    public void setProDetailType(ProDetailType proDetailType) {
        this.proDetailType = proDetailType;
    }

}
