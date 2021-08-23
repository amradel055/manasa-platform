/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import java.util.Date;
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
 * @author hq003
 */
@Entity
@Table(name = "symbol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Symbol.findAll", query = "SELECT s FROM Symbol s"),
    @NamedQuery(name = "Symbol.findById", query = "SELECT s FROM Symbol s WHERE s.id = :id"),
    @NamedQuery(name = "Symbol.findByName", query = "SELECT s FROM Symbol s WHERE s.name = :name"),
    @NamedQuery(name = "Symbol.findBySerial", query = "SELECT s FROM Symbol s WHERE s.serial = :serial"),
    @NamedQuery(name = "Symbol.findByCreationDate", query = "SELECT s FROM Symbol s WHERE s.creationDate = :creationDate"),
    @NamedQuery(name = "Symbol.findByModificationDate", query = "SELECT s FROM Symbol s WHERE s.modificationDate = :modificationDate")})
public class Symbol extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "serial")
    private int serial;
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
    private IsagCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IsagUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser modifiedBy;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @OneToMany(mappedBy = "designation")
    private Collection<Employee> employeeCollection;
    @OneToMany(mappedBy = "jobGrade")
    private Collection<Employee> employeeCollection1;
    @OneToMany(mappedBy = "religion")
    private Collection<Employee> employeeCollection2;
    @OneToMany(mappedBy = "maritalStatus")
    private Collection<Employee> employeeCollection3;
    @OneToMany(mappedBy = "nationality")
    private Collection<Employee> employeeCollection4;
    @OneToMany(mappedBy = "placeBirth")
    private Collection<Employee> employeeCollection5;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "arrange")
    private Integer arrange;
    @JoinColumn(name = "general_symbol_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralSymbol generalSymbolId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "symbol")
    private Collection<CompanyLanguage> companyLanguageCollection;
    @OneToMany(mappedBy = "countryId")
    private Collection<InvOrganizationSite> invOrganizationSiteCollection;
    @OneToMany(mappedBy = "regionId")
    private Collection<InvOrganizationSite> invOrganizationSiteCollection1;
    @OneToMany(mappedBy = "supplierType")
    private Collection<InvOrganizationSite> invOrganizationSiteCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lang")
    private Collection<DataDictionary> dataDictionaryCollection;
    @OneToMany(mappedBy = "lang")
    private Collection<IsagUser> isagUserCollection;

    public Symbol() {
    }

    public Symbol(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public GeneralSymbol getGeneralSymbolId() {
        return generalSymbolId;
    }

    public void setGeneralSymbolId(GeneralSymbol generalSymbolId) {
        this.generalSymbolId = generalSymbolId;
    }

    @XmlTransient
    public Collection<CompanyLanguage> getCompanyLanguageCollection() {
        return companyLanguageCollection;
    }

    public void setCompanyLanguageCollection(Collection<CompanyLanguage> companyLanguageCollection) {
        this.companyLanguageCollection = companyLanguageCollection;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection() {
        return invOrganizationSiteCollection;
    }

    public void setInvOrganizationSiteCollection(Collection<InvOrganizationSite> invOrganizationSiteCollection) {
        this.invOrganizationSiteCollection = invOrganizationSiteCollection;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection1() {
        return invOrganizationSiteCollection1;
    }

    public void setInvOrganizationSiteCollection1(Collection<InvOrganizationSite> invOrganizationSiteCollection1) {
        this.invOrganizationSiteCollection1 = invOrganizationSiteCollection1;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection2() {
        return invOrganizationSiteCollection2;
    }

    public void setInvOrganizationSiteCollection2(Collection<InvOrganizationSite> invOrganizationSiteCollection2) {
        this.invOrganizationSiteCollection2 = invOrganizationSiteCollection2;
    }

    @XmlTransient
    public Collection<DataDictionary> getDataDictionaryCollection() {
        return dataDictionaryCollection;
    }

    public void setDataDictionaryCollection(Collection<DataDictionary> dataDictionaryCollection) {
        this.dataDictionaryCollection = dataDictionaryCollection;
    }

    @XmlTransient
    public Collection<IsagUser> getIsagUserCollection() {
        return isagUserCollection;
    }

    public void setIsagUserCollection(Collection<IsagUser> isagUserCollection) {
        this.isagUserCollection = isagUserCollection;
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
        if (!(object instanceof Symbol)) {
            return false;
        }
        Symbol other = (Symbol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + serial;
    }

    /**
     * @return the arrange
     */
    public Integer getArrange() {
        return arrange;
    }

    /**
     * @param arrange the arrange to set
     */
    public void setArrange(Integer arrange) {
        this.arrange = arrange;
    }

   

    public Symbol(Integer id, int serial, Date creationDate) {
        this.id = id;
        this.serial = serial;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
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

    public GlAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection1() {
        return employeeCollection1;
    }

    public void setEmployeeCollection1(Collection<Employee> employeeCollection1) {
        this.employeeCollection1 = employeeCollection1;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection2() {
        return employeeCollection2;
    }

    public void setEmployeeCollection2(Collection<Employee> employeeCollection2) {
        this.employeeCollection2 = employeeCollection2;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection3() {
        return employeeCollection3;
    }

    public void setEmployeeCollection3(Collection<Employee> employeeCollection3) {
        this.employeeCollection3 = employeeCollection3;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection4() {
        return employeeCollection4;
    }

    public void setEmployeeCollection4(Collection<Employee> employeeCollection4) {
        this.employeeCollection4 = employeeCollection4;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection5() {
        return employeeCollection5;
    }

    public void setEmployeeCollection5(Collection<Employee> employeeCollection5) {
        this.employeeCollection5 = employeeCollection5;
    }

    

}
