/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
 * @author amr
 */
@Entity
@Table(name = "inv_purchase_order_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvPurchaseOrderDetail.findAll", query = "SELECT i FROM InvPurchaseOrderDetail i"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findById", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findByQuantity", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findByPrice", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.price = :price"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findByDiscountRate", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.discountRate = :discountRate"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findByTotal", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.total = :total"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findByModificationDate", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findByCreationDate", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvPurchaseOrderDetail.findByFinalQuantity", query = "SELECT i FROM InvPurchaseOrderDetail i WHERE i.finalQuantity = :finalQuantity")})
public class InvPurchaseOrderDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "discount_rate")
    private BigDecimal discountRate;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;
    @JoinColumn(name = "inv_purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseOrder invPurchaseOrderId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "net")
    private BigDecimal net;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "screwing")
    private BigDecimal screwing;    
    @Column(name = "weight")
    private BigDecimal weight;

    public InvPurchaseOrderDetail() {
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvPurchaseOrder getInvPurchaseOrderId() {
        return invPurchaseOrderId;
    }

    public void setInvPurchaseOrderId(InvPurchaseOrder invPurchaseOrderId) {
        this.invPurchaseOrderId = invPurchaseOrderId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvPurchaseOrderDetail)) {
            return false;
        }
        InvPurchaseOrderDetail other = (InvPurchaseOrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return itemId.getName() +" "+itemId.getCode() ;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
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
}
