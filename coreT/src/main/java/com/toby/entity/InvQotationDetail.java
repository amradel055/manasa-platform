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
 * @author hq003
 */
@Entity
@Table(name = "inv_qotation_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvQotationDetail.findAll", query = "SELECT i FROM InvQotationDetail i"),
    @NamedQuery(name = "InvQotationDetail.findById", query = "SELECT i FROM InvQotationDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvQotationDetail.findByQuantity", query = "SELECT i FROM InvQotationDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvQotationDetail.findByPrice", query = "SELECT i FROM InvQotationDetail i WHERE i.price = :price"),
    @NamedQuery(name = "InvQotationDetail.findByDiscount", query = "SELECT i FROM InvQotationDetail i WHERE i.discount = :discount"),
    @NamedQuery(name = "InvQotationDetail.findByDiscountId", query = "SELECT i FROM InvQotationDetail i WHERE i.discountId = :discountId"),
    @NamedQuery(name = "InvQotationDetail.findByCreationDate", query = "SELECT i FROM InvQotationDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvQotationDetail.findByModificationDate", query = "SELECT i FROM InvQotationDetail i WHERE i.modificationDate = :modificationDate")})
public class InvQotationDetail extends BaseEntity {

   // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "discount_id")
    private Integer discountId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @JoinColumn(name = "qotation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvQotation qotationId;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;
    @Column(name = "status")
    private Integer status;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "weight")
    private BigDecimal weight;     
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;    
    @Column(name = "net")
    private BigDecimal net;

    public InvQotationDetail() {
    }

    public InvQotationDetail(Integer id) {
        this.id = id;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
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

    public InvQotation getQotationId() {
        return qotationId;
    }

    public void setQotationId(InvQotation qotationId) {
        this.qotationId = qotationId;
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
        if (!(object instanceof InvQotationDetail)) {
            return false;
        }
        InvQotationDetail other = (InvQotationDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.InvQotationDetail[ id=" + id + " ]";
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

    /**
     * @return the screwing
     */
    public BigDecimal getScrewing() {
        return screwing;
    }

    /**
     * @param screwing the screwing to set
     */
    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
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

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }
}
