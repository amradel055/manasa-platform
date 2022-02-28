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
@Table(name = "inv_inventory_transaction_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvInventoryTransactionDetail.findAll", query = "SELECT i FROM InvInventoryTransactionDetail i"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findById", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findBySerial", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.serial = :serial"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByQuantity", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByCreationDate", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByModificationDate", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByFinalQuantity", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.finalQuantity = :finalQuantity"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByScrewing", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.screwing = :screwing"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByItemBarcode", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.itemBarcode = :itemBarcode"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByStatus", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.status = :status"),
    @NamedQuery(name = "InvInventoryTransactionDetail.findByTransferFrom", query = "SELECT i FROM InvInventoryTransactionDetail i WHERE i.transferFrom = :transferFrom")})
public class InvInventoryTransactionDetail extends BaseEntity{
    private static final long serialVersionUID = 1L;
   
    @Column(name = "serial")
    private Integer serial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Size(max = 450)
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "status")
    private Integer status;
    @Column(name = "transfer_from")
    private Integer transferFrom;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
   
    @JoinColumn(name = "selected_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoiceDetail selectedId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
   
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @JoinColumn(name = "inventory_transaction_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvInventoryTransaction inventoryTransactionId;
    @JoinColumn(name = "selected_purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseOrderDetail selectedPurchaseOrderId;
    @JoinColumn(name = "selected_purchase_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoiceDetail selectedPurchaseId;
@Column(name = "is_deleted")
    private Integer isdeleted;
    public InvInventoryTransactionDetail() {
    }

    public InvInventoryTransactionDetail(Integer id) {
        this.id = id;
    }

    public InvInventoryTransactionDetail(Integer id, Date creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }



    public InvPurchaseInvoiceDetail getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(InvPurchaseInvoiceDetail selectedId) {
        this.selectedId = selectedId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

   

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    public InvInventoryTransaction getInventoryTransactionId() {
        return inventoryTransactionId;
    }

    public void setInventoryTransactionId(InvInventoryTransaction inventoryTransactionId) {
        this.inventoryTransactionId = inventoryTransactionId;
    }

    public InvPurchaseOrderDetail getSelectedPurchaseOrderId() {
        return selectedPurchaseOrderId;
    }

    public void setSelectedPurchaseOrderId(InvPurchaseOrderDetail selectedPurchaseOrderId) {
        this.selectedPurchaseOrderId = selectedPurchaseOrderId;
    }

    public InvPurchaseInvoiceDetail getSelectedPurchaseId() {
        return selectedPurchaseId;
    }

    public void setSelectedPurchaseId(InvPurchaseInvoiceDetail selectedPurchaseId) {
        this.selectedPurchaseId = selectedPurchaseId;
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
        if (!(object instanceof InvInventoryTransactionDetail)) {
            return false;
        }
        InvInventoryTransactionDetail other = (InvInventoryTransactionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "newpackage.InvInventoryTransactionDetail[ id=" + id + " ]";
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
