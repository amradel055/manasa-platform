/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Branch;
import com.isag.entity.Currency;
import com.isag.entity.GlAccount;
import com.isag.entity.HosPatientRegistration;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.Symbol;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class HosPatientRegistrationEntity extends BaseEntity {

    private String code;
    private String name;
    private Integer type;
    private String phone;
    private String fax;
    private String email;
    private String mobile;
    private String address;
    private String postBox;
    private BigDecimal discount;
    private BigDecimal discountRatio;
    private BigDecimal bearRatio;
    private BigDecimal openBalanceDebit;
    private BigDecimal openBalanceCredit;
    private BigDecimal balanceLimit;
    private String remark;
    private boolean active;
    private String zip;
    private String contactPerson;
    private String phone2;
    private Date openBalanceDate;
    private GlAccount accountId;
    private Branch branchId;
    private Symbol countryId;
    private Currency currencyId;
    private HosPatientRegistration parent;
    private Symbol regionId;
    private Symbol supplierType;
    private String nationalId;
    private String patientHistory;
    private Date birthDay;
    private Symbol nationalityId;
    private Integer age;
    private BigDecimal height;
    private BigDecimal weight;
    private Integer gender;
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

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the countryId
     */
    public Symbol getCountryId() {
        return countryId;
    }

    /**
     * @param countryId the countryId to set
     */
    public void setCountryId(Symbol countryId) {
        this.countryId = countryId;
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
     * @return the parent
     */
    public HosPatientRegistration getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(HosPatientRegistration parent) {
        this.parent = parent;
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
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the discountRatio
     */
    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    /**
     * @param discountRatio the discountRatio to set
     */
    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    /**
     * @return the bearRatio
     */
    public BigDecimal getBearRatio() {
        return bearRatio;
    }

    /**
     * @param bearRatio the bearRatio to set
     */
    public void setBearRatio(BigDecimal bearRatio) {
        this.bearRatio = bearRatio;
    }

    /**
     * @return the nationalId
     */
    public String getNationalId() {
        return nationalId;
    }

    /**
     * @param nationalId the nationalId to set
     */
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    /**
     * @return the patientHistory
     */
    public String getPatientHistory() {
        return patientHistory;
    }

    /**
     * @param patientHistory the patientHistory to set
     */
    public void setPatientHistory(String patientHistory) {
        this.patientHistory = patientHistory;
    }

    /**
     * @return the birthDay
     */
    public Date getBirthDay() {
        return birthDay;
    }

    /**
     * @param birthDay the birthDay to set
     */
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return the gender
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * @return the height
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    /**
     * @return the nationalityId
     */
    public Symbol getNationalityId() {
        return nationalityId;
    }

    /**
     * @param nationalityId the nationalityId to set
     */
    public void setNationalityId(Symbol nationalityId) {
        this.nationalityId = nationalityId;
    }

}
