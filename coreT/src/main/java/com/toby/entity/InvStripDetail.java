/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kmelsayed
 */
@Entity
@Table(name = "inv_strip_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvStripDetail.findAll", query = "SELECT i FROM InvStripDetail i"),
    @NamedQuery(name = "InvStripDetail.findById", query = "SELECT i FROM InvStripDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvStripDetail.findByModificationDate", query = "SELECT i FROM InvStripDetail i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvStripDetail.findByCreationDate", query = "SELECT i FROM InvStripDetail i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvStripDetail.findByActualAmount", query = "SELECT i FROM InvStripDetail i WHERE i.actualAmount = :actualAmount"),
    @NamedQuery(name = "InvStripDetail.findByBookBalance", query = "SELECT i FROM InvStripDetail i WHERE i.bookBalance = :bookBalance"),
    @NamedQuery(name = "InvStripDetail.findByDifference", query = "SELECT i FROM InvStripDetail i WHERE i.difference = :difference")})
public class InvStripDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "actual_amount")
    private BigDecimal actualAmount;
    @Column(name = "book_balance")
    private BigDecimal bookBalance;
    @Column(name = "difference")
    private BigDecimal difference;
    @JoinColumn(name = "inv_item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem invItemId;
    @JoinColumn(name = "inv_strip_id", referencedColumnName = "id")
    @ManyToOne
    private InvStrip invStripId;
    @Column(name = "serial")
    private Integer serial;

    public InvStripDetail() {
    }

    public InvStripDetail(Integer id) {
        this.id = id;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getBookBalance() {
        return bookBalance;
    }

    public void setBookBalance(BigDecimal bookBalance) {
        this.bookBalance = bookBalance;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public InvItem getInvItemId() {
        return invItemId;
    }

    public void setInvItemId(InvItem invItemId) {
        this.invItemId = invItemId;
    }

    public InvStrip getInvStripId() {
        return invStripId;
    }

    public void setInvStripId(InvStrip invStripId) {
        this.invStripId = invStripId;
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
        if (!(object instanceof InvStripDetail)) {
            return false;
        }
        InvStripDetail other = (InvStripDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.InvStripDetail[ id=" + id + " ]";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

}
