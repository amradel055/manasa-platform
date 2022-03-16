/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_price_sell_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvPriceSellItem.findAll", query = "SELECT i FROM InvPriceSellItem i")
    , @NamedQuery(name = "InvPriceSellItem.findById", query = "SELECT i FROM InvPriceSellItem i WHERE i.id = :id")
    , @NamedQuery(name = "InvPriceSellItem.findByValue", query = "SELECT i FROM InvPriceSellItem i WHERE i.value = :value")
    , @NamedQuery(name = "InvPriceSellItem.findByDate", query = "SELECT i FROM InvPriceSellItem i WHERE i.date = :date")
    , @NamedQuery(name = "InvPriceSellItem.findByModificationDate", query = "SELECT i FROM InvPriceSellItem i WHERE i.modificationDate = :modificationDate")
    , @NamedQuery(name = "InvPriceSellItem.findByCreationDate", query = "SELECT i FROM InvPriceSellItem i WHERE i.creationDate = :creationDate")})
public class InvPriceSellItem extends BaseEntity {

    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;

    public InvPriceSellItem() {
    }


    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        if (!(object instanceof InvPriceSellItem)) {
            return false;
        }
        InvPriceSellItem other = (InvPriceSellItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.InvPriceSellItem[ id=" + id + " ]";
    }
    
}
