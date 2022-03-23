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
@Table(name = "inv_return_purchase_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvReturnPurchaseDetail.findAll", query = "SELECT i FROM InvReturnPurchaseDetail i"),
    @NamedQuery(name = "InvReturnPurchaseDetail.findById", query = "SELECT i FROM InvReturnPurchaseDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvReturnPurchaseDetail.findByQuantity", query = "SELECT i FROM InvReturnPurchaseDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvReturnPurchaseDetail.findByUnitPrice", query = "SELECT i FROM InvReturnPurchaseDetail i WHERE i.unitPrice = :unitPrice"),
    @NamedQuery(name = "InvReturnPurchaseDetail.findByDiscount", query = "SELECT i FROM InvReturnPurchaseDetail i WHERE i.discount = :discount"),
    @NamedQuery(name = "InvReturnPurchaseDetail.findByDicountType", query = "SELECT i FROM InvReturnPurchaseDetail i WHERE i.dicountType = :dicountType"),
    @NamedQuery(name = "InvReturnPurchaseDetail.findByCreationDate", query = "SELECT i FROM InvReturnPurchaseDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvReturnPurchaseDetail.findByModificationDate", query = "SELECT i FROM InvReturnPurchaseDetail i WHERE i.modificationDate = :modificationDate")})
public class InvReturnPurchaseDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "dicount_type")
    private Integer dicountType;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_return_purchase_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvReturnPurchase invReturnPurchaseId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @Column(name = "net")
    private BigDecimal net;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @JoinColumn(name = "inv_invoice_purchase_detail_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoiceDetail invInvoicePurchaseDetailId;

    public InvReturnPurchaseDetail() {
    }

    public InvReturnPurchaseDetail(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDicountType() {
        return dicountType;
    }

    public void setDicountType(Integer dicountType) {
        this.dicountType = dicountType;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvReturnPurchase getInvReturnPurchaseId() {
        return invReturnPurchaseId;
    }

    public void setInvReturnPurchaseId(InvReturnPurchase invReturnPurchaseId) {
        this.invReturnPurchaseId = invReturnPurchaseId;
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
        if (!(object instanceof InvReturnPurchaseDetail)) {
            return false;
        }
        InvReturnPurchaseDetail other = (InvReturnPurchaseDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.InvReturnPurchaseDetail[ id=" + id + " ]";
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

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
    
    public InvPurchaseInvoiceDetail getInvInvoicePurchaseDetailId() {
        return invInvoicePurchaseDetailId;
    }

    public void setInvInvoicePurchaseDetailId(InvPurchaseInvoiceDetail invInvoicePurchaseDetailId) {
        this.invInvoicePurchaseDetailId = invInvoicePurchaseDetailId;
    }
}
