/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "governorates")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Governorates.findAll", query = "SELECT g FROM Governorates g")
    , @NamedQuery(name = "Governorates.findById", query = "SELECT g FROM Governorates g WHERE g.id = :id")
    , @NamedQuery(name = "Governorates.findByName", query = "SELECT g FROM Governorates g WHERE g.name = :name")
    , @NamedQuery(name = "Governorates.findByCreationDate", query = "SELECT g FROM Governorates g WHERE g.creationDate = :creationDate")
    , @NamedQuery(name = "Governorates.findByModificationDate", query = "SELECT g FROM Governorates g WHERE g.modificationDate = :modificationDate")})
public class Governorates extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @OneToMany(mappedBy = "governorateId")
    private Collection<States> statesCollection;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser modifiedBy;

    public Governorates() {
    }

    public Governorates(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @XmlTransient
    public Collection<States> getStatesCollection() {
        return statesCollection;
    }

    public void setStatesCollection(Collection<States> statesCollection) {
        this.statesCollection = statesCollection;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IgmaCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IgmaCompany companyId) {
        this.companyId = companyId;
    }

    public IgmaUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IgmaUser createdBy) {
        this.createdBy = createdBy;
    }

    public IgmaUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IgmaUser modifiedBy) {
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
        if (!(object instanceof Governorates)) {
            return false;
        }
        Governorates other = (Governorates) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Governorates[ id=" + id + " ]";
    }
    
}
