/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import com.isag.entity.GlAccount;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class SupplierDataReportViewBean {

    private String code;
    private String name;
    private String phone;
    private String parent;
    private BigDecimal balanceLimit;
    private BigDecimal openBalance;
    private Date openBalanceDate;
    private String delegator;
    private GlAccount accountId;
    private String accountName;
    private Integer accountNumber;
    private String nationId; 
    private String accountBankNumber; 
    private String sponsorName1; 
    private String sponsorPhone1; 
    private String sponsorName2; 
    private String sponsorPhone2; 

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
     * @return the parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(String parent) {
        this.parent = parent;
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
     * @return the delegator
     */
    public String getDelegator() {
        return delegator;
    }

    /**
     * @param delegator the delegator to set
     */
    public void setDelegator(String delegator) {
        this.delegator = delegator;
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
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the accountNumber
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
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
     * @return the openBalance
     */
    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    /**
     * @param openBalance the openBalance to set
     */
    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
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

   

}
