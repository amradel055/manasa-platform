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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "generalbudgetguide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Generalbudgetguide.findAll", query = "SELECT g FROM Generalbudgetguide g")
    , @NamedQuery(name = "Generalbudgetguide.findById", query = "SELECT g FROM Generalbudgetguide g WHERE g.id = :id")
    , @NamedQuery(name = "Generalbudgetguide.findByNumber", query = "SELECT g FROM Generalbudgetguide g WHERE g.number = :number")
    , @NamedQuery(name = "Generalbudgetguide.findByNameAr", query = "SELECT g FROM Generalbudgetguide g WHERE g.nameAr = :nameAr")
    , @NamedQuery(name = "Generalbudgetguide.findByNameEn", query = "SELECT g FROM Generalbudgetguide g WHERE g.nameEn = :nameEn")
    , @NamedQuery(name = "Generalbudgetguide.findByCreationDate", query = "SELECT g FROM Generalbudgetguide g WHERE g.creationDate = :creationDate")
    , @NamedQuery(name = "Generalbudgetguide.findByModificationDate", query = "SELECT g FROM Generalbudgetguide g WHERE g.modificationDate = :modificationDate")
    , @NamedQuery(name = "Generalbudgetguide.findByAccGroup", query = "SELECT g FROM Generalbudgetguide g WHERE g.accGroup = :accGroup")})
public class Generalbudgetguide implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "number")
    private Integer number;
    @Size(max = 100)
    @Column(name = "nameAr")
    private String nameAr;
    @Size(max = 100)
    @Column(name = "nameEn")
    private String nameEn;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "acc_group")
    private String accGroup;
    @JoinColumn(name = "accountGroup", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountGroup;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser modifiedBy;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @OneToMany(mappedBy = "generalBudgetId")
    private Collection<GlAccount> glAccountCollection;
    @OneToMany(mappedBy = "generalBudgetIdCredit")
    private Collection<GlAccount> glAccountCollection1;
    @OneToMany(mappedBy = "generalBudgetIdDebit")
    private Collection<GlAccount> glAccountCollection2;

    public Generalbudgetguide() {
    }

    public Generalbudgetguide(Integer id) {
        this.id = id;
    }

    public Generalbudgetguide(Integer id, String accGroup) {
        this.id = id;
        this.accGroup = accGroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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

    public String getAccGroup() {
        return accGroup;
    }

    public void setAccGroup(String accGroup) {
        this.accGroup = accGroup;
    }

    public GlAccount getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(GlAccount accountGroup) {
        this.accountGroup = accountGroup;
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection() {
        return glAccountCollection;
    }

    public void setGlAccountCollection(Collection<GlAccount> glAccountCollection) {
        this.glAccountCollection = glAccountCollection;
    }

    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection1() {
        return glAccountCollection1;
    }

    public void setGlAccountCollection1(Collection<GlAccount> glAccountCollection1) {
        this.glAccountCollection1 = glAccountCollection1;
    }

    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection2() {
        return glAccountCollection2;
    }

    public void setGlAccountCollection2(Collection<GlAccount> glAccountCollection2) {
        this.glAccountCollection2 = glAccountCollection2;
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
        if (!(object instanceof Generalbudgetguide)) {
            return false;
        }
        Generalbudgetguide other = (Generalbudgetguide) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.Generalbudgetguide[ id=" + id + " ]";
    }
    
}
