/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
 * @author IT Shop
 */
@Entity
@Table(name = "item_offer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemOffer.findAll", query = "SELECT i FROM ItemOffer i")
    , @NamedQuery(name = "ItemOffer.findById", query = "SELECT i FROM ItemOffer i WHERE i.id = :id")
    , @NamedQuery(name = "ItemOffer.findByCreationDate", query = "SELECT i FROM ItemOffer i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "ItemOffer.findByModificationDate", query = "SELECT i FROM ItemOffer i WHERE i.modificationDate = :modificationDate")
    , @NamedQuery(name = "ItemOffer.findByDateFrom", query = "SELECT i FROM ItemOffer i WHERE i.dateFrom = :dateFrom")
    , @NamedQuery(name = "ItemOffer.findByDateTo", query = "SELECT i FROM ItemOffer i WHERE i.dateTo = :dateTo")})
public class ItemOffer  extends BaseEntity {

    @Column(name = "date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public ItemOffer() {
    }
    
    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof ItemOffer)) {
            return false;
        }
        ItemOffer other = (ItemOffer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ItemOffer[ id=" + id + " ]";
    }
    
}
