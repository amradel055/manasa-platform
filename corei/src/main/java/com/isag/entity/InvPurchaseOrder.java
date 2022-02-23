/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
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
 * @author amr
 */
@Entity
@Table(name = "inv_purchase_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvPurchaseOrder.findAll", query = "SELECT i FROM InvPurchaseOrder i"),
    @NamedQuery(name = "InvPurchaseOrder.findById", query = "SELECT i FROM InvPurchaseOrder i WHERE i.id = :id"),
    @NamedQuery(name = "InvPurchaseOrder.findBySupplierReference", query = "SELECT i FROM InvPurchaseOrder i WHERE i.supplierReference = :supplierReference"),
    @NamedQuery(name = "InvPurchaseOrder.findByDate", query = "SELECT i FROM InvPurchaseOrder i WHERE i.date = :date"),
    @NamedQuery(name = "InvPurchaseOrder.findByRemarks", query = "SELECT i FROM InvPurchaseOrder i WHERE i.remarks = :remarks"),
    @NamedQuery(name = "InvPurchaseOrder.findByRate", query = "SELECT i FROM InvPurchaseOrder i WHERE i.rate = :rate"),
    @NamedQuery(name = "InvPurchaseOrder.findByDiscount", query = "SELECT i FROM InvPurchaseOrder i WHERE i.discount = :discount"),
    @NamedQuery(name = "InvPurchaseOrder.findByDiscountType", query = "SELECT i FROM InvPurchaseOrder i WHERE i.discountType = :discountType"),
    @NamedQuery(name = "InvPurchaseOrder.findByModificationDate", query = "SELECT i FROM InvPurchaseOrder i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvPurchaseOrder.findByCreationDate", query = "SELECT i FROM InvPurchaseOrder i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvPurchaseOrder.findByStatus", query = "SELECT i FROM InvPurchaseOrder i WHERE i.status = :status")})
public class InvPurchaseOrder extends BaseEntity {

    @Size(max = 45)
    @Column(name = "supplier_reference")
    private String supplierReference;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 450)
    @Column(name = "remarks")
    private String remarks;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "discount_type")
    private Integer discountType;
    @Column(name = "status")
    private Integer status;

    @Column(name = "serial")
    private Integer serial;    
    @Column(name = "type")
    private Integer type;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;
    @JoinColumn(name = "delegator_id", referencedColumnName = "id")
    @ManyToOne
    private InventoryDelegator delegatorId;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite supplierId;
    @OneToMany(mappedBy = "invPurchaseOrderId")
    private Collection<InvPurchaseOrderDetail> invPurchaseOrderDetailCollection;
    @OneToMany(mappedBy = "purchaseOrderId")
    private Collection<InvAddingorder> invAddingorderCollection;

    public InvPurchaseOrder() {
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public InventoryDelegator getDelegatorId() {
        return delegatorId;
    }

    public void setDelegatorId(InventoryDelegator delegatorId) {
        this.delegatorId = delegatorId;
    }

    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public InvOrganizationSite getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(InvOrganizationSite supplierId) {
        this.supplierId = supplierId;
    }

    @XmlTransient
    public Collection<InvPurchaseOrderDetail> getInvPurchaseOrderDetailCollection() {
        return invPurchaseOrderDetailCollection;
    }

    public void setInvPurchaseOrderDetailCollection(Collection<InvPurchaseOrderDetail> invPurchaseOrderDetailCollection) {
        this.invPurchaseOrderDetailCollection = invPurchaseOrderDetailCollection;
    }

    @XmlTransient
    public Collection<InvAddingorder> getInvAddingorderCollection() {
        return invAddingorderCollection;
    }

    public void setInvAddingorderCollection(Collection<InvAddingorder> invAddingorderCollection) {
        this.invAddingorderCollection = invAddingorderCollection;
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
        if (!(object instanceof InvPurchaseOrder)) {
            return false;
        }
        InvPurchaseOrder other = (InvPurchaseOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial + "";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        if(type == null){
            type = 0;
        }
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

}
