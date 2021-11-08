/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "inst_purchase_invoice_detail")
public class InstPurchaseInvoiceDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "item_id")
    private Integer itemId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private TobyCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser createdBy;
    @JoinColumn(name = "inst_purchase_invoice_id", referencedColumnName = "id")
    @ManyToOne
    private InstPurchaseInvoice instPurchaseInvoiceId;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser modifiedBy;
     @Column(name = "table_type")
    private Integer tableType;
  
    public InstPurchaseInvoiceDetail() {
    }

    public InstPurchaseInvoiceDetail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public TobyCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(TobyCompany companyId) {
        this.companyId = companyId;
    }

    public TobyUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(TobyUser createdBy) {
        this.createdBy = createdBy;
    }

    public InstPurchaseInvoice getInstPurchaseInvoiceId() {
        return instPurchaseInvoiceId;
    }

    public void setInstPurchaseInvoiceId(InstPurchaseInvoice instPurchaseInvoiceId) {
        this.instPurchaseInvoiceId = instPurchaseInvoiceId;
    }

    public TobyUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(TobyUser modifiedBy) {
        this.modifiedBy = modifiedBy;
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
        if (!(object instanceof InstPurchaseInvoiceDetail)) {
            return false;
        }
        InstPurchaseInvoiceDetail other = (InstPurchaseInvoiceDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.InstPurchaseInvoiceDetail[ id=" + id + " ]";
    }

    /**
     * @return the tableType
     */
    public Integer getTableType() {
        return tableType;
    }

    /**
     * @param tableType the tableType to set
     */
    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

}
