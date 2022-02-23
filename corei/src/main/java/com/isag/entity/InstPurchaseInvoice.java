/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "inst_purchase_invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstPurchaseInvoice.findAll", query = "SELECT i FROM InstPurchaseInvoice i")
    , @NamedQuery(name = "InstPurchaseInvoice.findById", query = "SELECT i FROM InstPurchaseInvoice i WHERE i.id = :id")
    , @NamedQuery(name = "InstPurchaseInvoice.findByDate", query = "SELECT i FROM InstPurchaseInvoice i WHERE i.date = :date")
    , @NamedQuery(name = "InstPurchaseInvoice.findByCreationDate", query = "SELECT i FROM InstPurchaseInvoice i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "InstPurchaseInvoice.findByModificationDate", query = "SELECT i FROM InstPurchaseInvoice i WHERE i.modificationDate = :modificationDate")
    , @NamedQuery(name = "InstPurchaseInvoice.findBySerial", query = "SELECT i FROM InstPurchaseInvoice i WHERE i.serial = :serial")})
public class InstPurchaseInvoice extends BaseEntity{

    @OneToMany(mappedBy = "instPurchaseInvoiceId")
    private Collection<InstPurchaseInvoiceDetail> instPurchaseInvoiceDetailCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private IsagCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser modifiedBy;

    public InstPurchaseInvoice() {
    }

    public InstPurchaseInvoice(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    
    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IsagCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IsagCompany companyId) {
        this.companyId = companyId;
    }

    public IsagUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
    }

    public IsagUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IsagUser modifiedBy) {
        this.modifiedBy = modifiedBy;
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
        if (!(object instanceof InstPurchaseInvoice)) {
            return false;
        }
        InstPurchaseInvoice other = (InstPurchaseInvoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.InstPurchaseInvoice[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<InstPurchaseInvoiceDetail> getInstPurchaseInvoiceDetailCollection() {
        return instPurchaseInvoiceDetailCollection;
    }

    public void setInstPurchaseInvoiceDetailCollection(Collection<InstPurchaseInvoiceDetail> instPurchaseInvoiceDetailCollection) {
        this.instPurchaseInvoiceDetailCollection = instPurchaseInvoiceDetailCollection;
    }
    
}
