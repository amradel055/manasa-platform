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
 * @author User
 */
@Entity
@Table(name = "pro_factory_delivery_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProFactoryDeliveryDetail.findAll", query = "SELECT p FROM ProFactoryDeliveryDetail p")
    , @NamedQuery(name = "ProFactoryDeliveryDetail.findById", query = "SELECT p FROM ProFactoryDeliveryDetail p WHERE p.id = :id")
    , @NamedQuery(name = "ProFactoryDeliveryDetail.findByNumber", query = "SELECT p FROM ProFactoryDeliveryDetail p WHERE p.number = :number")
    , @NamedQuery(name = "ProFactoryDeliveryDetail.findByClothNumber", query = "SELECT p FROM ProFactoryDeliveryDetail p WHERE p.clothNumber = :clothNumber")
    , @NamedQuery(name = "ProFactoryDeliveryDetail.findBySerial", query = "SELECT p FROM ProFactoryDeliveryDetail p WHERE p.serial = :serial")
    , @NamedQuery(name = "ProFactoryDeliveryDetail.findByCreationDate", query = "SELECT p FROM ProFactoryDeliveryDetail p WHERE p.creationDate = :creationDate")
    , @NamedQuery(name = "ProFactoryDeliveryDetail.findByModificationDate", query = "SELECT p FROM ProFactoryDeliveryDetail p WHERE p.modificationDate = :modificationDate")})
public class ProFactoryDeliveryDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "number")
    private BigDecimal number;
    @Column(name = "cloth_number")
    private Integer clothNumber;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "pro_factory_delivery_id", referencedColumnName = "id")
    @ManyToOne
    private ProFactoryDelivery proFactoryDeliveryId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;
    @JoinColumn(name = "inv_item_parent_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem invItemParentId;
    @JoinColumn(name = "inv_purchase_invoice_detail_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoiceDetail invPurchaseInvoiceDetailId;

    public ProFactoryDeliveryDetail() {
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public Integer getClothNumber() {
        return clothNumber;
    }

    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public ProFactoryDelivery getProFactoryDeliveryId() {
        return proFactoryDeliveryId;
    }

    public void setProFactoryDeliveryId(ProFactoryDelivery proFactoryDeliveryId) {
        this.proFactoryDeliveryId = proFactoryDeliveryId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public InvItem getInvItemParentId() {
        return invItemParentId;
    }

    public void setInvItemParentId(InvItem invItemParentId) {
        this.invItemParentId = invItemParentId;
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
        if (!(object instanceof ProFactoryDeliveryDetail)) {
            return false;
        }
        ProFactoryDeliveryDetail other = (ProFactoryDeliveryDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ccc.ProFactoryDeliveryDetail[ id=" + id + " ]";
    }

    /**
     * @return the invPurchaseInvoiceDetailId
     */
    public InvPurchaseInvoiceDetail getInvPurchaseInvoiceDetailId() {
        return invPurchaseInvoiceDetailId;
    }

    /**
     * @param invPurchaseInvoiceDetailId the invPurchaseInvoiceDetailId to set
     */
    public void setInvPurchaseInvoiceDetailId(InvPurchaseInvoiceDetail invPurchaseInvoiceDetailId) {
        this.invPurchaseInvoiceDetailId = invPurchaseInvoiceDetailId;
    }

    /**
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
    
}
