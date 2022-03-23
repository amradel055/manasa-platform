/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Currency;
import com.isag.entity.GlAccount;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.Symbol;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class InvOrganizationSiteEntity extends BaseEntity {

    private String name;
    private Symbol supplierType;
    private Symbol contractorType;
    private Symbol regionId;
    private Integer delegateId;
    private InvOrganizationSite parent;
    private String postBox;
    private String address;
    private String email;
    private String contactPerson;
    private GlAccount accountId;
    private Currency currencyId;
    private BigDecimal balanceLimit;
    private BigDecimal openBalanceCredit;
    private BigDecimal openBalanceDebit;
    private BigDecimal discount;
    private String remarks;
    private String phone1;
    private String phone2;
    private String fax;
    private String mobile;
    private String zip;
    private String code;
    private Integer active;
    private Integer type;
    private String nationId;
    private String accountBankNumber;
    private String sponsorName1;
    private String sponsorPhone1;
    private String sponsorName2;
    private String sponsorPhone2;
    private String companyName;
    private Date birthdate;
    private Symbol workTypeId;
    private Symbol storeRegionId;
    private String legalAffairsRemark;

    private InventoryDelegator salesPerson;
    private Date openBalanceDate;
    private String taxNumber ;
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
     * @return the supplierType
     */
    public Symbol getSupplierType() {
        return supplierType;
    }

    /**
     * @param supplierType the supplierType to set
     */
    public void setSupplierType(Symbol supplierType) {
        this.supplierType = supplierType;
    }

    /**
     * @return the regionId
     */
    public Symbol getRegionId() {
        return regionId;
    }

    /**
     * @param regionId the regionId to set
     */
    public void setRegionId(Symbol regionId) {
        this.regionId = regionId;
    }

    /**
     * @return the delegateId
     */
    public Integer getDelegateId() {
        return delegateId;
    }

    /**
     * @param delegateId the delegateId to set
     */
    public void setDelegateId(Integer delegateId) {
        this.delegateId = delegateId;
    }

    /**
     * @return the parent
     */
    public InvOrganizationSite getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(InvOrganizationSite parent) {
        this.parent = parent;
    }

    /**
     * @return the postBox
     */
    public String getPostBox() {
        return postBox;
    }

    /**
     * @param postBox the postBox to set
     */
    public void setPostBox(String postBox) {
        this.postBox = postBox;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the contactPerson
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * @param contactPerson the contactPerson to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * @return the accountId
     */
    public GlAccount getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the currencyId
     */
    public Currency getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the balanceLimit
     */
    public BigDecimal getBalanceLimit() {
        return balanceLimit;
    }

    /**
     * @param balanceLimit the balanceLimit to set
     */
    public void setBalanceLimit(BigDecimal balanceLimit) {
        this.balanceLimit = balanceLimit;
    }

    /**
     * @return the openBalanceCredit
     */
    public BigDecimal getOpenBalanceCredit() {
        return openBalanceCredit;
    }

    /**
     * @param openBalanceCredit the openBalanceCredit to set
     */
    public void setOpenBalanceCredit(BigDecimal openBalanceCredit) {
        this.openBalanceCredit = openBalanceCredit;
    }

    /**
     * @return the openBalanceDebit
     */
    public BigDecimal getOpenBalanceDebit() {
        return openBalanceDebit;
    }

    /**
     * @param openBalanceDebit the openBalanceDebit to set
     */
    public void setOpenBalanceDebit(BigDecimal openBalanceDebit) {
        this.openBalanceDebit = openBalanceDebit;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the phone1
     */
    public String getPhone1() {
        return phone1;
    }

    /**
     * @param phone1 the phone1 to set
     */
    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    /**
     * @return the phone2
     */
    public String getPhone2() {
        return phone2;
    }

    /**
     * @param phone2 the phone2 to set
     */
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
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
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
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
     * @return the active
     */
    public Integer getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Integer active) {
        this.active = active;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    public InventoryDelegator getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(InventoryDelegator salesPerson) {
        this.salesPerson = salesPerson;
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
     * @return the nationId
     */
    public String getNationId() {
        return nationId;
    }

    /**
     * @param nationId the nationId to set
     */
    public void setNationId(String nationId) {
        this.nationId = nationId;
    }

    /**
     * @return the accountBankNumber
     */
    public String getAccountBankNumber() {
        return accountBankNumber;
    }

    /**
     * @param accountBankNumber the accountBankNumber to set
     */
    public void setAccountBankNumber(String accountBankNumber) {
        this.accountBankNumber = accountBankNumber;
    }

    /**
     * @return the sponsorName1
     */
    public String getSponsorName1() {
        return sponsorName1;
    }

    /**
     * @param sponsorName1 the sponsorName1 to set
     */
    public void setSponsorName1(String sponsorName1) {
        this.sponsorName1 = sponsorName1;
    }

    /**
     * @return the sponsorPhone1
     */
    public String getSponsorPhone1() {
        return sponsorPhone1;
    }

    /**
     * @param sponsorPhone1 the sponsorPhone1 to set
     */
    public void setSponsorPhone1(String sponsorPhone1) {
        this.sponsorPhone1 = sponsorPhone1;
    }

    /**
     * @return the sponsorName2
     */
    public String getSponsorName2() {
        return sponsorName2;
    }

    /**
     * @param sponsorName2 the sponsorName2 to set
     */
    public void setSponsorName2(String sponsorName2) {
        this.sponsorName2 = sponsorName2;
    }

    /**
     * @return the sponsorPhone2
     */
    public String getSponsorPhone2() {
        return sponsorPhone2;
    }

    /**
     * @param sponsorPhone2 the sponsorPhone2 to set
     */
    public void setSponsorPhone2(String sponsorPhone2) {
        this.sponsorPhone2 = sponsorPhone2;
    }

    /**
     * @return the birthdate
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * @param birthdate the birthdate to set
     */
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * @return the workTypeId
     */
    public Symbol getWorkTypeId() {
        return workTypeId;
    }

    /**
     * @param workTypeId the workTypeId to set
     */
    public void setWorkTypeId(Symbol workTypeId) {
        this.workTypeId = workTypeId;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the storeRegionId
     */
    public Symbol getStoreRegionId() {
        return storeRegionId;
    }

    /**
     * @param storeRegionId the storeRegionId to set
     */
    public void setStoreRegionId(Symbol storeRegionId) {
        this.storeRegionId = storeRegionId;
    }

    /**
     * @return the legalAffairsRemark
     */
    public String getLegalAffairsRemark() {
        return legalAffairsRemark;
    }

    /**
     * @param legalAffairsRemark the legalAffairsRemark to set
     */
    public void setLegalAffairsRemark(String legalAffairsRemark) {
        this.legalAffairsRemark = legalAffairsRemark;
    }

    /**
     * @return the openBalanceDate
     */
    public Date getOpenBalanceDate() {
        return openBalanceDate;
    }

    /**
     * @param openBalanceDate the openBalanceDate to set
     */
    public void setOpenBalanceDate(Date openBalanceDate) {
        this.openBalanceDate = openBalanceDate;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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

}
