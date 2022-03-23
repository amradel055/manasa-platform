/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "_history_change_item_price")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoryChangeItemPrice.findAll", query = "SELECT h FROM HistoryChangeItemPrice h")
    , @NamedQuery(name = "HistoryChangeItemPrice.findById", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.id = :id")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByItemId", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.itemId = :itemId")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByPrice", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.price = :price")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByBranchId", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.branchId = :branchId")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByCompanyId", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.companyId = :companyId")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByCreatedBy", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.createdBy = :createdBy")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByModifiedBy", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.modifiedBy = :modifiedBy")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByCreationDate", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.creationDate = :creationDate")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByModificationDate", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.modificationDate = :modificationDate")
    , @NamedQuery(name = "HistoryChangeItemPrice.findByChagePriceDate", query = "SELECT h FROM HistoryChangeItemPrice h WHERE h.chagePriceDate = :chagePriceDate")})
public class HistoryChangeItemPrice extends BaseEntity {


    @Column(name = "item_id")
    private Integer itemId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "chage_price_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chagePriceDate;

    
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Date getChagePriceDate() {
        return chagePriceDate;
    }

    public void setChagePriceDate(Date chagePriceDate) {
        this.chagePriceDate = chagePriceDate;
    }

    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoryChangeItemPrice)) {
            return false;
        }
        HistoryChangeItemPrice other = (HistoryChangeItemPrice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.en.HistoryChangeItemPrice[ id=" + id + " ]";
    }
    
}
