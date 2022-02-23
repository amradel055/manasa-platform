/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import com.isag.define.CompanyActivityClassEnum;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    @Basic(optional = false)
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "image")
    private String image;
    @Column(name = "tax_code")
    private String taxCode;
    @Column(name = "company_activity")
    @Enumerated(EnumType.STRING)
    private CompanyActivityClassEnum companyActivity;
    @OneToMany(mappedBy = "branchId")
    private Collection<IsagUser> isagUserCollection;
    @OneToMany(mappedBy = "branchId")
    private Collection<GeneralBudgetGuide> generalbudgetguideCollection;

    @OneToMany(mappedBy = "branchId")
    private Collection<GlAccount> glAccountCollection;

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

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @XmlTransient
    public Collection<IsagUser> getIsagUserCollection() {
        return isagUserCollection;
    }

    public void setIsagUserCollection(Collection<IsagUser> isagUserCollection) {
        this.isagUserCollection = isagUserCollection;
    }

    @XmlTransient
    public Collection<GeneralBudgetGuide> getGeneralbudgetguideCollection() {
        return generalbudgetguideCollection;
    }

    public void setGeneralbudgetguideCollection(Collection<GeneralBudgetGuide> generalbudgetguideCollection) {
        this.generalbudgetguideCollection = generalbudgetguideCollection;
    }

    @XmlTransient
    public Collection<GlAccount> getGlAccountCollection() {
        return glAccountCollection;
    }

    public void setGlAccountCollection(Collection<GlAccount> glAccountCollection) {
        this.glAccountCollection = glAccountCollection;
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
    public CompanyActivityClassEnum getCompanyActivity() {
        return companyActivity;
    }

    /**
     * @param companyActivity the companyActivity to set
     */
    public void setCompanyActivity(CompanyActivityClassEnum companyActivity) {
        this.companyActivity = companyActivity;
    }

}
