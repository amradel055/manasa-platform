/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
 * @author FreeComp
 */
@Entity
@Table(name = "general_symbol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GeneralSymbol.findAll", query = "SELECT g FROM GeneralSymbol g"),
    @NamedQuery(name = "GeneralSymbol.findById", query = "SELECT g FROM GeneralSymbol g WHERE g.id = :id"),
    @NamedQuery(name = "GeneralSymbol.findByName", query = "SELECT g FROM GeneralSymbol g WHERE g.name = :name"),
    @NamedQuery(name = "GeneralSymbol.findBySerial", query = "SELECT g FROM GeneralSymbol g WHERE g.serial = :serial"),
    @NamedQuery(name = "GeneralSymbol.findByCreationDate", query = "SELECT g FROM GeneralSymbol g WHERE g.creationDate = :creationDate"),
    @NamedQuery(name = "GeneralSymbol.findByModificationDate", query = "SELECT g FROM GeneralSymbol g WHERE g.modificationDate = :modificationDate")})
public class GeneralSymbol extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IgmaUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser modifiedBy;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "serial")
    private Integer serial;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generalSymbolId")
    private Set<Symbol> symbolSet;

    public GeneralSymbol() {
    }

    public GeneralSymbol(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    @XmlTransient
    public Set<Symbol> getSymbolSet() {
        return symbolSet;
    }

    public void setSymbolSet(Set<Symbol> symbolSet) {
        this.symbolSet = symbolSet;
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
        if (!(object instanceof GeneralSymbol)) {
            return false;
        }
        GeneralSymbol other = (GeneralSymbol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial + " " + name;
    }

   

    public GeneralSymbol(Integer id, Date creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

   

}
