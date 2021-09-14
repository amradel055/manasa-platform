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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author WIN7
 */
@Entity
@Table(name = "branch")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Branch.findAll", query = "SELECT b FROM Branch b"),
    @NamedQuery(name = "Branch.findById", query = "SELECT b FROM Branch b WHERE b.id = :id"),
    @NamedQuery(name = "Branch.findByNameAr", query = "SELECT b FROM Branch b WHERE b.nameAr = :nameAr"),
    @NamedQuery(name = "Branch.findByNameEn", query = "SELECT b FROM Branch b WHERE b.nameEn = :nameEn"),
    @NamedQuery(name = "Branch.findByAddress1", query = "SELECT b FROM Branch b WHERE b.address1 = :address1"),
    @NamedQuery(name = "Branch.findByAddress2", query = "SELECT b FROM Branch b WHERE b.address2 = :address2"),
    @NamedQuery(name = "Branch.findByAddress3", query = "SELECT b FROM Branch b WHERE b.address3 = :address3"),
    @NamedQuery(name = "Branch.findByPhone", query = "SELECT b FROM Branch b WHERE b.phone = :phone"),
    @NamedQuery(name = "Branch.findByMobile", query = "SELECT b FROM Branch b WHERE b.mobile = :mobile"),
    @NamedQuery(name = "Branch.findByFax", query = "SELECT b FROM Branch b WHERE b.fax = :fax"),
    @NamedQuery(name = "Branch.findByEmail", query = "SELECT b FROM Branch b WHERE b.email = :email"),
    @NamedQuery(name = "Branch.findByIconPath", query = "SELECT b FROM Branch b WHERE b.iconPath = :iconPath"),
    @NamedQuery(name = "Branch.findByCreationDate", query = "SELECT b FROM Branch b WHERE b.creationDate = :creationDate"),
    @NamedQuery(name = "Branch.findByModificationDate", query = "SELECT b FROM Branch b WHERE b.modificationDate = :modificationDate"),
    @NamedQuery(name = "Branch.findBySerial", query = "SELECT b FROM Branch b WHERE b.serial = :serial"),
    @NamedQuery(name = "Branch.findByImage", query = "SELECT b FROM Branch b WHERE b.image = :image")})
public class Branch extends BaseEntity {

    @OneToMany(mappedBy = "branchId")

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
    @Basic(optional = false)
    @NotNull
    @Column(name = "serial")
    private int serial;
    @Size(max = 20)
    @Column(name = "company_activity")
    private String companyActivity;
    @OneToMany(mappedBy = "branchId")
    private Collection<Generalbudgetguide> generalbudgetguideCollection;
    @OneToMany(mappedBy = "branchId")
    private Collection<Employee> employeeCollection;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private IgmaUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser modifiedBy;
    @OneToMany(mappedBy = "branchId")
    private Collection<Department> departmentCollection;
    @OneToMany(mappedBy = "branchId")
    private Collection<GlAccount> glAccountCollection;
    @OneToMany(mappedBy = "branchId")
    private Collection<HrSponsor> hrSponsorCollection;

    @Basic(optional = false)
    @Column(name = "nameAr")
    private String nameAr;
    @Basic(optional = false)
    @Column(name = "nameEn")
    private String nameEn;
    @Column(name = "address1")
    private String address1;
    @Column(name = "address2")
    private String address2;
    @Column(name = "address3")
    private String address3;
    @Column(name = "phone")
    private String phone;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "fax")
    private String fax;
    @Column(name = "email")
    private String email;
    @Column(name = "iconPath")
    private String iconPath;

    @Column(name = "image")
    private String image;
    @Column(name = "tax_code")
    private String taxCode;
    @OneToMany(mappedBy = "branchId")
    private Collection<IgmaUser> isagUserCollection;


    public Branch() {
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @XmlTransient
    public Collection<IgmaUser> getIsagUserCollection() {
        return isagUserCollection;
    }

    public void setIsagUserCollection(Collection<IgmaUser> isagUserCollection) {
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
        if (!(object instanceof Branch)) {
            return false;
        }
        Branch other = (Branch) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.Branch[ id=" + id + " ]";
    }

    /**
     * @return the taxCode
     */
    public String getTaxCode() {
        return taxCode;
    }

    /**
     * @param taxCode the taxCode to set
     */
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    /**
     * @return the companyActivity
     */
   

    public Branch(Integer id) {
        this.id = id;
    }

    public Branch(Integer id, Date creationDate, int serial) {
        this.id = id;
        this.creationDate = creationDate;
        this.serial = serial;
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

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getCompanyActivity() {
        return companyActivity;
    }

    public void setCompanyActivity(String companyActivity) {
        this.companyActivity = companyActivity;
    }

    @XmlTransient
    public Collection<Generalbudgetguide> getGeneralbudgetguideCollection() {
        return generalbudgetguideCollection;
    }

    public void setGeneralbudgetguideCollection(Collection<Generalbudgetguide> generalbudgetguideCollection) {
        this.generalbudgetguideCollection = generalbudgetguideCollection;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
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

    

   

}
