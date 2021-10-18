/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author hq004
 */
@Entity
@Table(name = "igma_company")
@NamedQueries({
    @NamedQuery(name = "IgmaCompany.findAll", query = "SELECT i FROM IgmaCompany i")})
public class IgmaCompany extends BaseEntity {

    @Lob
    @Column(name = "logo")
    private byte[] logo;
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
    @Column(name = "company_id")
    private Integer companyId;
    @OneToMany(mappedBy = "companyId")
    private Collection<Symbol> symbolCollection;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IgmaUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser modifiedBy;
    @OneToMany(mappedBy = "companyId")
    private Collection<Generalbudgetguide> generalbudgetguideCollection;
    @OneToMany(mappedBy = "companyId")
    private Collection<GeneralSymbol> generalSymbolCollection;
    @OneToMany(mappedBy = "companyId")
    private Collection<Employee> employeeCollection;
    @OneToMany(mappedBy = "companyId")
    private Collection<Branch> branchCollection;
    @OneToMany(mappedBy = "companyId")
    private Collection<IgmaRole> isagRoleCollection;
    @OneToMany(mappedBy = "companyId")
    private Collection<Department> departmentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyId")
    private Collection<GlAccount> glAccountCollection;
    @OneToMany(mappedBy = "companyId")
    private Collection<HrSponsor> hrSponsorCollection;

    @Size(max = 200)
    @Column(name = "name")
    private String name;

    @Size(max = 200)
    @Column(name = "name_en")
    private String nameEn;

    @Size(max = 200)
    @Column(name = "address_en")
    private String addressEn;

    @Size(max = 3)
    @Column(name = "code")
    private String code;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyId", fetch = FetchType.LAZY)
    private List<IgmaUser> isagUserList;

    @Size(max = 200)
    @Column(name = "core_business")
    private String core_business;

    @Size(max = 200)
    @Column(name = "responsible")
    private String responsible;

    @Size(max = 200)
    @Column(name = "phone")
    private String phone;

    @Size(max = 200)
    @Column(name = "address")
    private String address;

    @Size(max = 450)
    @Column(name = "image")
    private String image;

    @Size(max = 200)
    @Column(name = "fax")
    private String fax;

    @Column(name = "row_count_list")
    private Integer rowCountList;

    @Column(name = "row_count_master_details")
    private Integer rowCountMasterDetails;

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getCore_business() {
        return core_business;
    }

    public void setCore_business(String core_business) {
        this.core_business = core_business;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<IgmaUser> getIgmaUserList() {
        return isagUserList;
    }

    public void setIgmaUserList(List<IgmaUser> isagUserList) {
        this.isagUserList = isagUserList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the rowCountMasterDetails
     */
    public Integer getRowCountMasterDetails() {
        return rowCountMasterDetails;
    }

    /**
     * @param rowCountMasterDetails the rowCountMasterDetails to set
     */
    public void setRowCountMasterDetails(Integer rowCountMasterDetails) {
        this.rowCountMasterDetails = rowCountMasterDetails;
    }

    /**
     * @return the rowCountList
     */
    public Integer getRowCountList() {
        return rowCountList;
    }

    /**
     * @param rowCountList the rowCountList to set
     */
    public void setRowCountList(Integer rowCountList) {
        this.rowCountList = rowCountList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the nameEn
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn the nameEn to set
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * @return the addressEn
     */
    public String getAddressEn() {
        return addressEn;
    }

    /**
     * @param addressEn the addressEn to set
     */
    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public IgmaCompany() {
    }

    public IgmaCompany(Integer id) {
        this.id = id;
    }

    public IgmaCompany(Integer id, Date creationDate) {
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

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @XmlTransient
    public Collection<Symbol> getSymbolCollection() {
        return symbolCollection;
    }

    public void setSymbolCollection(Collection<Symbol> symbolCollection) {
        this.symbolCollection = symbolCollection;
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

    @XmlTransient
    public Collection<Generalbudgetguide> getGeneralbudgetguideCollection() {
        return generalbudgetguideCollection;
    }

    public void setGeneralbudgetguideCollection(Collection<Generalbudgetguide> generalbudgetguideCollection) {
        this.generalbudgetguideCollection = generalbudgetguideCollection;
    }

    @XmlTransient
    public Collection<GeneralSymbol> getGeneralSymbolCollection() {
        return generalSymbolCollection;
    }

    public void setGeneralSymbolCollection(Collection<GeneralSymbol> generalSymbolCollection) {
        this.generalSymbolCollection = generalSymbolCollection;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @XmlTransient
    public Collection<Branch> getBranchCollection() {
        return branchCollection;
    }

    public void setBranchCollection(Collection<Branch> branchCollection) {
        this.branchCollection = branchCollection;
    }

    @XmlTransient
    public Collection<IgmaRole> getIgmaRoleCollection() {
        return isagRoleCollection;
    }

    public void setIgmaRoleCollection(Collection<IgmaRole> isagRoleCollection) {
        this.isagRoleCollection = isagRoleCollection;
    }

    @XmlTransient
    public Collection<Department> getDepartmentCollection() {
        return departmentCollection;
    }

    public void setDepartmentCollection(Collection<Department> departmentCollection) {
        this.departmentCollection = departmentCollection;
    }

    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection() {
        return glAccountCollection;
    }

    public void setGlAccountCollection(Collection<GlAccount> glAccountCollection) {
        this.glAccountCollection = glAccountCollection;
    }

    @XmlTransient
    public Collection<HrSponsor> getHrSponsorCollection() {
        return hrSponsorCollection;
    }

    public void setHrSponsorCollection(Collection<HrSponsor> hrSponsorCollection) {
        this.hrSponsorCollection = hrSponsorCollection;
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
        if (!(object instanceof IgmaCompany)) {
            return false;
        }
        IgmaCompany other = (IgmaCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.IgmaCompany[ id=" + id + " ]";
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
}
