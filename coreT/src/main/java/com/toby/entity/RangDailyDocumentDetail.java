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
 * @author FreeComp
 */
@Entity
@Table(name = "rang_daily_document_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RangDailyDocumentDetail.findAll", query = "SELECT r FROM RangDailyDocumentDetail r"),
    @NamedQuery(name = "RangDailyDocumentDetail.findById", query = "SELECT r FROM RangDailyDocumentDetail r WHERE r.id = :id"),
    @NamedQuery(name = "RangDailyDocumentDetail.findByRangYear", query = "SELECT r FROM RangDailyDocumentDetail r WHERE r.rangYear = :rangYear"),
    @NamedQuery(name = "RangDailyDocumentDetail.findByRangFrom", query = "SELECT r FROM RangDailyDocumentDetail r WHERE r.rangFrom = :rangFrom"),
    @NamedQuery(name = "RangDailyDocumentDetail.findByRangTo", query = "SELECT r FROM RangDailyDocumentDetail r WHERE r.rangTo = :rangTo"),
    @NamedQuery(name = "RangDailyDocumentDetail.findByDeletedDate", query = "SELECT r FROM RangDailyDocumentDetail r WHERE r.deletedDate = :deletedDate"),
    @NamedQuery(name = "RangDailyDocumentDetail.findByCreationDate", query = "SELECT r FROM RangDailyDocumentDetail r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "RangDailyDocumentDetail.findByModificationDate", query = "SELECT r FROM RangDailyDocumentDetail r WHERE r.modificationDate = :modificationDate")})
public class RangDailyDocumentDetail extends BaseEntity{

    @Column(name = "rang_year")
    private Integer rangYear;
    @Column(name = "rang_from")
    private Integer rangFrom;
    @Column(name = "rang_to")
    private Integer rangTo;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.DATE)
    private Date deletedDate;
    @JoinColumn(name = "rang_daily_document_id", referencedColumnName = "id")
    @ManyToOne
    private RangDailyDocument rangDailyDocumentId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser deletedBy;

    public RangDailyDocumentDetail() {
    }

    public RangDailyDocumentDetail(Integer id) {
        this.id = id;
    }

    public Integer getRangYear() {
        return rangYear;
    }

    public void setRangYear(Integer rangYear) {
        this.rangYear = rangYear;
    }

    public Integer getRangFrom() {
        return rangFrom;
    }

    public void setRangFrom(Integer rangFrom) {
        this.rangFrom = rangFrom;
    }

    public Integer getRangTo() {
        return rangTo;
    }

    public void setRangTo(Integer rangTo) {
        this.rangTo = rangTo;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public RangDailyDocument getRangDailyDocumentId() {
        return rangDailyDocumentId;
    }

    public void setRangDailyDocumentId(RangDailyDocument rangDailyDocumentId) {
        this.rangDailyDocumentId = rangDailyDocumentId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public TobyUser getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(TobyUser deletedBy) {
        this.deletedBy = deletedBy;
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
        if (!(object instanceof RangDailyDocumentDetail)) {
            return false;
        }
        RangDailyDocumentDetail other = (RangDailyDocumentDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RangDailyDocumentDetail[ id=" + id + " ]";
    }
    
}
