/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "rang_daily_document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RangDailyDocument.findAll", query = "SELECT r FROM RangDailyDocument r"),
    @NamedQuery(name = "RangDailyDocument.findById", query = "SELECT r FROM RangDailyDocument r WHERE r.id = :id"),
    @NamedQuery(name = "RangDailyDocument.findByName", query = "SELECT r FROM RangDailyDocument r WHERE r.name = :name"),
    @NamedQuery(name = "RangDailyDocument.findByDeletedDate", query = "SELECT r FROM RangDailyDocument r WHERE r.deletedDate = :deletedDate"),
    @NamedQuery(name = "RangDailyDocument.findByCreationDate", query = "SELECT r FROM RangDailyDocument r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "RangDailyDocument.findByModificationDate", query = "SELECT r FROM RangDailyDocument r WHERE r.modificationDate = :modificationDate")})
public class RangDailyDocument extends BaseEntity{

    @Size(max = 450)
    @Column(name = "name")
    private String name;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.DATE)
    private Date deletedDate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser deletedBy;
    @OneToMany(mappedBy = "rangDailyDocumentId")
    private Set<RangDailyDocumentDetail> rangDailyDocumentDetailSet;

    public RangDailyDocument() {
    }

    public RangDailyDocument(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IsagUser getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(IsagUser deletedBy) {
        this.deletedBy = deletedBy;
    }

    @XmlTransient
    public Set<RangDailyDocumentDetail> getRangDailyDocumentDetailSet() {
        return rangDailyDocumentDetailSet;
    }

    public void setRangDailyDocumentDetailSet(Set<RangDailyDocumentDetail> rangDailyDocumentDetailSet) {
        this.rangDailyDocumentDetailSet = rangDailyDocumentDetailSet;
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
        if (!(object instanceof RangDailyDocument)) {
            return false;
        }
        RangDailyDocument other = (RangDailyDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RangDailyDocument[ id=" + id + " ]";
    }
    
}
