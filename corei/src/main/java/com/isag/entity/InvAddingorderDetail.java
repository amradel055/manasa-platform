/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "inv_addingorder_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvAddingorderDetail.findAll", query = "SELECT i FROM InvAddingorderDetail i"),
    @NamedQuery(name = "InvAddingorderDetail.findById", query = "SELECT i FROM InvAddingorderDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvAddingorderDetail.findByQuantity", query = "SELECT i FROM InvAddingorderDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvAddingorderDetail.findByCreationDate", query = "SELECT i FROM InvAddingorderDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvAddingorderDetail.findByModificationDate", query = "SELECT i FROM InvAddingorderDetail i WHERE i.modificationDate = :modificationDate")})
public class InvAddingorderDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;
    @JoinColumn(name = "addingorder_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvAddingorder addingorderId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "screwing")
    private BigDecimal screwing;   
    @Column(name = "weight")
    private BigDecimal weight;
   @Column(name = "transfer_from")
    private Integer transferFrom;
   @JoinColumn(name = "selected_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoiceDetail selectedId;
    @JoinColumn(name = "selected_purchase_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoiceDetail selectedPurchaseId;    
    @JoinColumn(name = "selected_purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseOrderDetail selectedPurchaseOrderId;
    
    
    
    public InvAddingorderDetail() {
    }

    public InvAddingorderDetail(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public InvAddingorder getAddingorderId() {
        return addingorderId;
    }

    public void setAddingorderId(InvAddingorder addingorderId) {
        this.addingorderId = addingorderId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
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
        if (!(object instanceof InvAddingorderDetail)) {
            return false;
        }
        InvAddingorderDetail other = (InvAddingorderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial + "";
    }

    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    

    /**
     * @return the transferFrom
     */
    public Integer getTransferFrom() {
        return transferFrom;
    }

    /**
     * @param transferFrom the transferFrom to set
     */
    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    /**
     * @return the selectedId
     */
    public InvPurchaseInvoiceDetail getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(InvPurchaseInvoiceDetail selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @return the selectedPurchaseId
     */
    public InvPurchaseInvoiceDetail getSelectedPurchaseId() {
        return selectedPurchaseId;
    }

    /**
     * @param selectedPurchaseId the selectedPurchaseId to set
     */
    public void setSelectedPurchaseId(InvPurchaseInvoiceDetail selectedPurchaseId) {
        this.selectedPurchaseId = selectedPurchaseId;
    }
    
    public InvPurchaseOrderDetail getSelectedPurchaseOrderId() {
        return selectedPurchaseOrderId;
    }

    public void setSelectedPurchaseOrderId(InvPurchaseOrderDetail selectedPurchaseOrderId) {
        this.selectedPurchaseOrderId = selectedPurchaseOrderId;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return the unitId
     */
    public Symbol getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

}
