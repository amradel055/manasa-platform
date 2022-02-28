/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user4
 */
@Entity
@Table(name = "inv_inventory_transaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvInventoryTransaction.findAll", query = "SELECT i FROM InvInventoryTransaction i"),
    @NamedQuery(name = "InvInventoryTransaction.findById", query = "SELECT i FROM InvInventoryTransaction i WHERE i.id = :id"),
    @NamedQuery(name = "InvInventoryTransaction.findBySerial", query = "SELECT i FROM InvInventoryTransaction i WHERE i.serial = :serial"),
    @NamedQuery(name = "InvInventoryTransaction.findByDate", query = "SELECT i FROM InvInventoryTransaction i WHERE i.date = :date"),
    @NamedQuery(name = "InvInventoryTransaction.findBySupplierInvoice", query = "SELECT i FROM InvInventoryTransaction i WHERE i.supplierInvoice = :supplierInvoice"),
    @NamedQuery(name = "InvInventoryTransaction.findBySupplierDate", query = "SELECT i FROM InvInventoryTransaction i WHERE i.supplierDate = :supplierDate"),
    @NamedQuery(name = "InvInventoryTransaction.findByPostFlag", query = "SELECT i FROM InvInventoryTransaction i WHERE i.postFlag = :postFlag"),
    @NamedQuery(name = "InvInventoryTransaction.findByRemark", query = "SELECT i FROM InvInventoryTransaction i WHERE i.remark = :remark"),
    @NamedQuery(name = "InvInventoryTransaction.findByCreationDate", query = "SELECT i FROM InvInventoryTransaction i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvInventoryTransaction.findByModificationDate", query = "SELECT i FROM InvInventoryTransaction i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvInventoryTransaction.findByType", query = "SELECT i FROM InvInventoryTransaction i WHERE i.type = :type"),
    @NamedQuery(name = "InvInventoryTransaction.findByStatus", query = "SELECT i FROM InvInventoryTransaction i WHERE i.status = :status"),
    @NamedQuery(name = "InvInventoryTransaction.findByInvDelegatorId", query = "SELECT i FROM InvInventoryTransaction i WHERE i.invDelegatorId = :invDelegatorId")})
public class InvInventoryTransaction extends BaseEntity{

    @Column(name = "serial")
    private Integer serial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 45)
    @Column(name = "supplier_invoice")
    private String supplierInvoice;
    @Column(name = "supplier_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date supplierDate;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "type")
    private Integer type;
    @Column(name = "status")
    private Integer status;
    @Column(name = "inv_delegator_id")
    private Integer invDelegatorId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseOrder purchaseOrderId;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
    @JoinColumn(name = "inv_purchaseinvoice_Id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoice invpurchaseinvoiceId;
    @Column(name = "is_deleted")
    private Integer isdeleted;
    public InvInventoryTransaction() {
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSupplierInvoice() {
        return supplierInvoice;
    }

    public void setSupplierInvoice(String supplierInvoice) {
        this.supplierInvoice = supplierInvoice;
    }

    public Date getSupplierDate() {
        return supplierDate;
    }

    public void setSupplierDate(Date supplierDate) {
        this.supplierDate = supplierDate;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInvDelegatorId() {
        return invDelegatorId;
    }

    public void setInvDelegatorId(Integer invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvOrganizationSite getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSite organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public InvPurchaseOrder getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(InvPurchaseOrder purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
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
        if (!(object instanceof InvInventoryTransaction)) {
            return false;
        }
        InvInventoryTransaction other = (InvInventoryTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.omar.InvInventoryTransaction[ id=" + id + " ]";
    }

    /**
     * @return the invpurchaseinvoiceId
     */
    public InvPurchaseInvoice getInvpurchaseinvoiceId() {
        return invpurchaseinvoiceId;
    }

    /**
     * @param invpurchaseinvoiceId the invpurchaseinvoiceId to set
     */
    public void setInvpurchaseinvoiceId(InvPurchaseInvoice invpurchaseinvoiceId) {
        this.invpurchaseinvoiceId = invpurchaseinvoiceId;
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

    
}
