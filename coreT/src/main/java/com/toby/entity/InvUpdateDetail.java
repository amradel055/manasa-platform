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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_update_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvUpdateDetail.findAll", query = "SELECT i FROM InvUpdateDetail i")
    , @NamedQuery(name = "InvUpdateDetail.findById", query = "SELECT i FROM InvUpdateDetail i WHERE i.id = :id")
    , @NamedQuery(name = "InvUpdateDetail.findByModificationDate", query = "SELECT i FROM InvUpdateDetail i WHERE i.modificationDate = :modificationDate")
    , @NamedQuery(name = "InvUpdateDetail.findByCreationDate", query = "SELECT i FROM InvUpdateDetail i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "InvUpdateDetail.findByNewAmount", query = "SELECT i FROM InvUpdateDetail i WHERE i.newAmount = :newAmount")
    , @NamedQuery(name = "InvUpdateDetail.findByOldAmount", query = "SELECT i FROM InvUpdateDetail i WHERE i.oldAmount = :oldAmount")
    , @NamedQuery(name = "InvUpdateDetail.findByDifference", query = "SELECT i FROM InvUpdateDetail i WHERE i.difference = :difference")
    , @NamedQuery(name = "InvUpdateDetail.findByValue", query = "SELECT i FROM InvUpdateDetail i WHERE i.value = :value")
    , @NamedQuery(name = "InvUpdate.findBySerial", query = "SELECT i FROM InvUpdate i WHERE i.serial = :serial")
    , @NamedQuery(name = "InvUpdateDetail.findByItemBarcode", query = "SELECT i FROM InvUpdateDetail i WHERE i.itemBarcode = :itemBarcode")
    , @NamedQuery(name = "InvUpdateDetail.findByScrewing", query = "SELECT i FROM InvUpdateDetail i WHERE i.screwing = :screwing")})
public class InvUpdateDetail extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "new_amount")
    private BigDecimal newAmount;
    @Column(name = "old_amount")
    private BigDecimal oldAmount;
    @Column(name = "difference")
    private BigDecimal difference;
    @Column(name = "value")
    private BigDecimal value;
    @Size(max = 450)
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @JoinColumn(name = "inv_item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem invItemId;
    @JoinColumn(name = "inv_update_id", referencedColumnName = "id")
    @ManyToOne
    private InvUpdate invUpdateId;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;

    public InvUpdateDetail() {
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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

    public InvItem getInvItemId() {
        return invItemId;
    }

    public void setInvItemId(InvItem invItemId) {
        this.invItemId = invItemId;
    }

    public InvUpdate getInvUpdateId() {
        return invUpdateId;
    }

    public void setInvUpdateId(InvUpdate invUpdateId) {
        this.invUpdateId = invUpdateId;
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
        if (!(object instanceof InvUpdateDetail)) {
            return false;
        }
        InvUpdateDetail other = (InvUpdateDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.en.entity.InvUpdateDetail[ id=" + id + " ]";
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    
}
