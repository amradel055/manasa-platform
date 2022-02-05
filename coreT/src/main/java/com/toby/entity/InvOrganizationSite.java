/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author hq003
 */
@Entity
@Table(name = "inv_organization_site")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvOrganizationSite.findAll", query = "SELECT i FROM InvOrganizationSite i")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByCode", query = "SELECT i FROM InvOrganizationSite i WHERE i.code = :code")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByName", query = "SELECT i FROM InvOrganizationSite i WHERE i.name = :name")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByType", query = "SELECT i FROM InvOrganizationSite i WHERE i.type = :type")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByPhone", query = "SELECT i FROM InvOrganizationSite i WHERE i.phone = :phone")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByFax", query = "SELECT i FROM InvOrganizationSite i WHERE i.fax = :fax")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByEmail", query = "SELECT i FROM InvOrganizationSite i WHERE i.email = :email")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByMobile", query = "SELECT i FROM InvOrganizationSite i WHERE i.mobile = :mobile")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByAddress", query = "SELECT i FROM InvOrganizationSite i WHERE i.address = :address")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByPostBox", query = "SELECT i FROM InvOrganizationSite i WHERE i.postBox = :postBox")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByDiscount", query = "SELECT i FROM InvOrganizationSite i WHERE i.discount = :discount")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByOpenBalanceDebit", query = "SELECT i FROM InvOrganizationSite i WHERE i.openBalanceDebit = :openBalanceDebit")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByOpenBalanceCredit", query = "SELECT i FROM InvOrganizationSite i WHERE i.openBalanceCredit = :openBalanceCredit")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByBalanceLimit", query = "SELECT i FROM InvOrganizationSite i WHERE i.balanceLimit = :balanceLimit")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByRemark", query = "SELECT i FROM InvOrganizationSite i WHERE i.remark = :remark")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByActive", query = "SELECT i FROM InvOrganizationSite i WHERE i.active = :active")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByZip", query = "SELECT i FROM InvOrganizationSite i WHERE i.zip = :zip")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByContactPerson", query = "SELECT i FROM InvOrganizationSite i WHERE i.contactPerson = :contactPerson")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByContractCode", query = "SELECT i FROM InvOrganizationSite i WHERE(:fromCode IS NULL OR i.code >= :fromCode) AND (:toCode IS NULL OR i.code <= :toCode) and i.type=4 AND i.branchId.id= :branchID")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByPhone2", query = "SELECT i FROM InvOrganizationSite i WHERE i.phone2 = :phone2")
    ,
    @NamedQuery(name = "InvOrganizationSite.findByOpenBalanceDate", query = "SELECT i FROM InvOrganizationSite i WHERE i.openBalanceDate = :openBalanceDate")})
public class InvOrganizationSite extends BaseEntity {

    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private Integer type;
    @Column(name = "phone")
    private String phone;
    @Column(name = "fax")
    private String fax;
    @Column(name = "email")
    private String email;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "address")
    private String address;
    @Column(name = "post_box")
    private String postBox;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "open_balance_debit")
    private BigDecimal openBalanceDebit;
    @Column(name = "open_balance_credit")
    private BigDecimal openBalanceCredit;
    @Column(name = "balance_limit")
    private BigDecimal balanceLimit;
    @Column(name = "remark")
    private String remark;
    @Column(name = "active")
    private Integer active;
    @Column(name = "zip")
    private String zip;
    @Column(name = "contact_person")
    private String contactPerson;
    @Column(name = "phone2")
    private String phone2;
    @Column(name = "open_balance_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date openBalanceDate;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol countryId;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;
    @JoinColumn(name = "delegator_id", referencedColumnName = "id")
    @ManyToOne
    private InventoryDelegator delegatorId;
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol regionId;
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Collection<InvOrganizationSite> invOrganizationSiteCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite parent;
    @JoinColumn(name = "supplier_type", referencedColumnName = "id")
    @ManyToOne
    private Symbol supplierType;
    @JoinColumn(name = "contractor_type", referencedColumnName = "id")
    @ManyToOne
    private Symbol contractorType;
    @JoinColumn(name = "offer_company_id", referencedColumnName = "id")
    @ManyToOne
    private OfferCompany offerCompanyId;
    @JoinColumn(name = "gallary_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory gallaryId;
    @Column(name = "length")
    private BigDecimal length;
    @Column(name = "shoulder")
    private BigDecimal shoulder;
    @Column(name = "step")
    private BigDecimal step;
    
    
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    @ManyToOne
    private Employee assignedTo;
    @JoinColumn(name = "lead_status", referencedColumnName = "id")
    @ManyToOne
    private Symbol leadStatus;
    @Size(max = 100)
    @Column(name = "url_site")
    private String urlSite;
    @Size(max = 100)
    @Column(name = "url_facebook")
    private String urlFacebook;
    @Size(max = 100)
    @Column(name = "url_instegram")
    private String urlInstegram;
    @Size(max = 100)
    @Column(name = "url_snapchat")
    private String urlSnapchat;
    @Size(max = 100)
    @Column(name = "url_linkedin")
    private String urlLinkedin;
    @Column(name = "category")
    private Integer category;
    @Column(name = "employee_number")
    private Integer employeeNumber;
    @Column(name = "call_first_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date callFirstDate;
    @Size(max = 100)
    @Column(name = "discription")
    private String discription;
    @JoinColumn(name = "project_name", referencedColumnName = "id")
    @ManyToOne
    private Symbol projectName;
    @Column(name = "tax_Number")
    private String taxNumber;    
    @JoinColumn(name = "Learn_about_company_from", referencedColumnName = "id")
    @ManyToOne
    private Symbol LearnAboutCompanyfrom;


    public InvOrganizationSite() {
    }

    public InvOrganizationSite(Integer id) {
        this.id = id;
    }

    public InvOrganizationSite(Integer id, String name, Date creationDate) {
        this.id = id;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostBox() {
        return postBox;
    }

    public void setPostBox(String postBox) {
        this.postBox = postBox;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getOpenBalanceDebit() {
        return openBalanceDebit;
    }

    public void setOpenBalanceDebit(BigDecimal openBalanceDebit) {
        this.openBalanceDebit = openBalanceDebit;
    }

    public BigDecimal getOpenBalanceCredit() {
        return openBalanceCredit;
    }

    public void setOpenBalanceCredit(BigDecimal openBalanceCredit) {
        this.openBalanceCredit = openBalanceCredit;
    }

    public BigDecimal getBalanceLimit() {
        return balanceLimit;
    }

    public void setBalanceLimit(BigDecimal balanceLimit) {
        this.balanceLimit = balanceLimit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public Date getOpenBalanceDate() {
        return openBalanceDate;
    }

    public void setOpenBalanceDate(Date openBalanceDate) {
        this.openBalanceDate = openBalanceDate;
    }

    public GlAccount getAccountId() {
        return accountId;
    }

   public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Symbol getCountryId() {
        return countryId;
    }

    public void setCountryId(Symbol countryId) {
        this.countryId = countryId;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public InventoryDelegator getDelegatorId() {
        return delegatorId;
    }

    public void setDelegatorId(InventoryDelegator delegatorId) {
        this.delegatorId = delegatorId;
    }

    public Symbol getRegionId() {
        return regionId;
    }

    public void setRegionId(Symbol regionId) {
        this.regionId = regionId;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection() {
        return invOrganizationSiteCollection;
    }

    public void setInvOrganizationSiteCollection(Collection<InvOrganizationSite> invOrganizationSiteCollection) {
        this.invOrganizationSiteCollection = invOrganizationSiteCollection;
    }

    public InvOrganizationSite getParent() {
        return parent;
    }

    public void setParent(InvOrganizationSite parent) {
        this.parent = parent;
    }

    public Symbol getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(Symbol supplierType) {
        this.supplierType = supplierType;
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
        if (!(object instanceof InvOrganizationSite)) {
            return false;
        }
        InvOrganizationSite other = (InvOrganizationSite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the contractorType
     */
    public Symbol getContractorType() {
        return contractorType;
    }

    /**
     * @param contractorType the contractorType to set
     */
    public void setContractorType(Symbol contractorType) {
        this.contractorType = contractorType;
    }

    /**
     * @return the offerCompanyId
     */
    public OfferCompany getOfferCompanyId() {
        return offerCompanyId;
    }

    /**
     * @param offerCompanyId the offerCompanyId to set
     */
    public void setOfferCompanyId(OfferCompany offerCompanyId) {
        this.offerCompanyId = offerCompanyId;
    }

    /**
     * @return the gallaryId
     */
    public InvInventory getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(InvInventory gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the length
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /**
     * @return the shoulder
     */
    public BigDecimal getShoulder() {
        return shoulder;
    }

    /**
     * @param shoulder the shoulder to set
     */
    public void setShoulder(BigDecimal shoulder) {
        this.shoulder = shoulder;
    }

    /**
     * @return the step
     */
    public BigDecimal getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(BigDecimal step) {
        this.step = step;
    }

    /**
     * @return the assignedTo
     */
    public Employee getAssignedTo() {
        return assignedTo;
    }

    /**
     * @param assignedTo the assignedTo to set
     */
    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * @return the leadStatus
     */
    public Symbol getLeadStatus() {
        return leadStatus;
    }

    /**
     * @param leadStatus the leadStatus to set
     */
    public void setLeadStatus(Symbol leadStatus) {
        this.leadStatus = leadStatus;
    }

    /**
     * @return the urlSite
     */
    public String getUrlSite() {
        return urlSite;
    }

    /**
     * @param urlSite the urlSite to set
     */
    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    /**
     * @return the urlFacebook
     */
    public String getUrlFacebook() {
        return urlFacebook;
    }

    /**
     * @param urlFacebook the urlFacebook to set
     */
    public void setUrlFacebook(String urlFacebook) {
        this.urlFacebook = urlFacebook;
    }

    /**
     * @return the urlInstegram
     */
    public String getUrlInstegram() {
        return urlInstegram;
    }

    /**
     * @param urlInstegram the urlInstegram to set
     */
    public void setUrlInstegram(String urlInstegram) {
        this.urlInstegram = urlInstegram;
    }

    /**
     * @return the urlSnapchat
     */
    public String getUrlSnapchat() {
        return urlSnapchat;
    }

    /**
     * @param urlSnapchat the urlSnapchat to set
     */
    public void setUrlSnapchat(String urlSnapchat) {
        this.urlSnapchat = urlSnapchat;
    }

    /**
     * @return the urlLinkedin
     */
    public String getUrlLinkedin() {
        return urlLinkedin;
    }

    /**
     * @param urlLinkedin the urlLinkedin to set
     */
    public void setUrlLinkedin(String urlLinkedin) {
        this.urlLinkedin = urlLinkedin;
    }

    /**
     * @return the category
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * @return the employeeNumber
     */
    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * @param employeeNumber the employeeNumber to set
     */
    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    /**
     * @return the callFirstDate
     */
    public Date getCallFirstDate() {
        return callFirstDate;
    }

    /**
     * @param callFirstDate the callFirstDate to set
     */
    public void setCallFirstDate(Date callFirstDate) {
        this.callFirstDate = callFirstDate;
    }

    /**
     * @return the discription
     */
    public String getDiscription() {
        return discription;
    }

    /**
     * @param discription the discription to set
     */
    public void setDiscription(String discription) {
        this.discription = discription;
    }

    /**
     * @return the projectName
     */
    public Symbol getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(Symbol projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the taxNumber
     */
    public String getTaxNumber() {
        return taxNumber;
    }

    /**
     * @param taxNumber the taxNumber to set
     */
    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    /**
     * @return the LearnAboutCompanyfrom
     */
    public Symbol getLearnAboutCompanyfrom() {
        return LearnAboutCompanyfrom;
    }

    /**
     * @param LearnAboutCompanyfrom the LearnAboutCompanyfrom to set
     */
    public void setLearnAboutCompanyfrom(Symbol LearnAboutCompanyfrom) {
        this.LearnAboutCompanyfrom = LearnAboutCompanyfrom;
    }

}
