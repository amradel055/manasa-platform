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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hhhh
 */
@Entity
@Table(name = "inv_openning_balance_item_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findAll", query = "SELECT i FROM InvOpenningBalanceItemDetail i"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findById", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByUnitId", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.unitId = :unitId"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByScrewing", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.screwing = :screwing"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByQuantity", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByCost", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.cost = :cost"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByNet", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.net = :net"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByModificationDate", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvOpenningBalanceItemDetail.findByCreationDate", query = "SELECT i FROM InvOpenningBalanceItemDetail i WHERE i.creationDate = :creationDate")})
public class InvOpenningBalanceItemDetail extends BaseEntity {

    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "net")
    private BigDecimal net;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "inv_openning_balance_item_id", referencedColumnName = "id")
    @ManyToOne
    private InvOpenningBalanceItem balanceItemId;
    @Size(max = 100)
    @Column(name = "item_barcode")
    private String itemBarcode;
    
    @Column(name = "disc1")
    private BigDecimal disc1;
    @Column(name = "disc2")
    private BigDecimal disc2;
    @Column(name = "disc3")
    private BigDecimal disc3;
    @Column(name = "add1")
    private BigDecimal add1;
    @Column(name = "add2")
    private BigDecimal add2;
    @Column(name = "add3")
    private BigDecimal add3;
    @Column(name = "carton_number")
    private BigDecimal cartonsNumber;
    @Column(name = "cartons_capacity")
    private BigDecimal cartonsCapacity;

    public InvOpenningBalanceItemDetail() {
    }

    public InvOpenningBalanceItemDetail(Integer id) {
        this.id = id;
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
        if (!(object instanceof InvOpenningBalanceItemDetail)) {
            return false;
        }
        InvOpenningBalanceItemDetail other = (InvOpenningBalanceItemDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial != null ? serial.toString() : "";
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal scrawing) {
        this.screwing = scrawing;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
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

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public InvOpenningBalanceItem getBalanceItemId() {
        return balanceItemId;
    }

    public void setBalanceItemId(InvOpenningBalanceItem balanceItemId) {
        this.balanceItemId = balanceItemId;
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

    /**
     * @return the disc1
     */
    public BigDecimal getDisc1() {
        return disc1;
    }

    /**
     * @param disc1 the disc1 to set
     */
    public void setDisc1(BigDecimal disc1) {
        this.disc1 = disc1;
    }

    /**
     * @return the disc2
     */
    public BigDecimal getDisc2() {
        return disc2;
    }

    /**
     * @param disc2 the disc2 to set
     */
    public void setDisc2(BigDecimal disc2) {
        this.disc2 = disc2;
    }

    /**
     * @return the disc3
     */
    public BigDecimal getDisc3() {
        return disc3;
    }

    /**
     * @param disc3 the disc3 to set
     */
    public void setDisc3(BigDecimal disc3) {
        this.disc3 = disc3;
    }

    /**
     * @return the add1
     */
    public BigDecimal getAdd1() {
        return add1;
    }

    /**
     * @param add1 the add1 to set
     */
    public void setAdd1(BigDecimal add1) {
        this.add1 = add1;
    }

    /**
     * @return the add2
     */
    public BigDecimal getAdd2() {
        return add2;
    }

    /**
     * @param add2 the add2 to set
     */
    public void setAdd2(BigDecimal add2) {
        this.add2 = add2;
    }

    /**
     * @return the add3
     */
    public BigDecimal getAdd3() {
        return add3;
    }

    /**
     * @param add3 the add3 to set
     */
    public void setAdd3(BigDecimal add3) {
        this.add3 = add3;
    }

    /**
     * @return the cartonsNumber
     */
    public BigDecimal getCartonsNumber() {
        return cartonsNumber;
    }

    /**
     * @param cartonsNumber the cartonsNumber to set
     */
    public void setCartonsNumber(BigDecimal cartonsNumber) {
        this.cartonsNumber = cartonsNumber;
    }

    /**
     * @return the cartonsCapacity
     */
    public BigDecimal getCartonsCapacity() {
        return cartonsCapacity;
    }

    /**
     * @param cartonsCapacity the cartonsCapacity to set
     */
    public void setCartonsCapacity(BigDecimal cartonsCapacity) {
        this.cartonsCapacity = cartonsCapacity;
    }
}
