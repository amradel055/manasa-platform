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
 * @author amr
 */
@Entity
@Table(name = "inv_reservation_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvReservationDetail.findAll", query = "SELECT i FROM InvReservationDetail i"),
    @NamedQuery(name = "InvReservationDetail.findById", query = "SELECT i FROM InvReservationDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvReservationDetail.findByModificationDate", query = "SELECT i FROM InvReservationDetail i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvReservationDetail.findByCreationDate", query = "SELECT i FROM InvReservationDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvReservationDetail.findByPrice", query = "SELECT i FROM InvReservationDetail i WHERE i.price = :price"),
    @NamedQuery(name = "InvReservationDetail.findByAmount", query = "SELECT i FROM InvReservationDetail i WHERE i.amount = :amount"),
    @NamedQuery(name = "InvReservationDetail.findByTotal", query = "SELECT i FROM InvReservationDetail i WHERE i.total = :total"),
    @NamedQuery(name = "InvReservationDetail.findByAdding", query = "SELECT i FROM InvReservationDetail i WHERE i.adding = :adding"),
    @NamedQuery(name = "InvReservationDetail.findByDiscount", query = "SELECT i FROM InvReservationDetail i WHERE i.discount = :discount"),
    @NamedQuery(name = "InvReservationDetail.findByNet", query = "SELECT i FROM InvReservationDetail i WHERE i.net = :net")})
public class InvReservationDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "adding")
    private BigDecimal adding;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "net")
    private BigDecimal net;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    @ManyToOne
    private InvReservation reservationId;
    @Column(name = "final_quantity")
    private BigDecimal finalQuantity;
    @Column(name = "status")
    private Integer status;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;    
    @Column(name = "weight")
    private BigDecimal weight;    
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;

    public InvReservationDetail() {
    }

    public InvReservationDetail(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAdding() {
        return adding;
    }

    public void setAdding(BigDecimal adding) {
        this.adding = adding;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public InvReservation getReservationId() {
        return reservationId;
    }

    public void setReservationId(InvReservation reservationId) {
        this.reservationId = reservationId;
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
        if (!(object instanceof InvReservationDetail)) {
            return false;
        }
        InvReservationDetail other = (InvReservationDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.InvReservationDetail[ id=" + id + " ]";
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
