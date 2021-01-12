/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "inv_addingorder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvAddingorder.findAll", query = "SELECT i FROM InvAddingorder i"),
    @NamedQuery(name = "InvAddingorder.findById", query = "SELECT i FROM InvAddingorder i WHERE i.id = :id"),
    @NamedQuery(name = "InvAddingorder.findByDate", query = "SELECT i FROM InvAddingorder i WHERE i.date = :date"),
    @NamedQuery(name = "InvAddingorder.findBySupplierInvoice", query = "SELECT i FROM InvAddingorder i WHERE i.supplierInvoice = :supplierInvoice"),
    @NamedQuery(name = "InvAddingorder.findBySupplierDate", query = "SELECT i FROM InvAddingorder i WHERE i.supplierDate = :supplierDate"),
    @NamedQuery(name = "InvAddingorder.findByPostFlag", query = "SELECT i FROM InvAddingorder i WHERE i.postFlag = :postFlag"),
    @NamedQuery(name = "InvAddingorder.findByRemark", query = "SELECT i FROM InvAddingorder i WHERE i.remark = :remark"),
    @NamedQuery(name = "InvAddingorder.findByCreationDate", query = "SELECT i FROM InvAddingorder i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvAddingorder.findByModificationDate", query = "SELECT i FROM InvAddingorder i WHERE i.modificationDate = :modificationDate")})
public class InvAddingorder extends BaseEntity {

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
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseOrder purchaseOrderId;
    @JoinColumn(name = "inv_purchase_invoice_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoice invPurchaseInvoice;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
    @JoinColumn(name = "inv_delegator_id", referencedColumnName = "id")
    @ManyToOne
    private InventoryDelegator invDelegatorId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "type")
    private Integer type;
    @Column(name = "serial")
    private Integer serial;

    public InvAddingorder() {
    }

    public InvAddingorder(Integer id) {
        this.id = id;
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
        if (!(object instanceof InvAddingorder)) {
            return false;
        }
        InvAddingorder other = (InvAddingorder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial+ "";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the invDelegatorId
     */
    public InventoryDelegator getInvDelegatorId() {
        return invDelegatorId;
    }

    /**
     * @param invDelegatorId the invDelegatorId to set
     */
    public void setInvDelegatorId(InventoryDelegator invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
    }

    /**
     * @return the invPurchaseInvoice
     */
    public InvPurchaseInvoice getInvPurchaseInvoice() {
        return invPurchaseInvoice;
    }

    /**
     * @param invPurchaseInvoice the invPurchaseInvoice to set
     */
    public void setInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice) {
        this.invPurchaseInvoice = invPurchaseInvoice;
    }

}
