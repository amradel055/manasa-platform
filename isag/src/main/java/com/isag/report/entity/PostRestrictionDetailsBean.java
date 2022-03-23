/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import com.isag.entity.CostCenter;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class PostRestrictionDetailsBean {
  
     private Integer glBankTransactionId ;
     private Integer accNumberDebit ;
     private Integer accNumberCredit ;
     private Integer accNumber ;
     private String accNameDebit ;
     private String accNameCredit ;
     private String accName ;
     private String remarks ;
     private BigDecimal value;
     private BigDecimal valueLocal;    
     private BigDecimal valueDebit;
     private BigDecimal valueCredit;
     private BigDecimal valueLocalDebit;    
     private BigDecimal valueLocalCredit;    
     private GlAccount glAccount;
     private CostCenter costCenter;
     private GlAdminUnit adminUnit;
     
    /**
     * @return the glBankTransactionId
     */
    public Integer getGlBankTransactionId() {
        return glBankTransactionId;
    }

    /**
     * @param glBankTransactionId the glBankTransactionId to set
     */
    public void setGlBankTransactionId(Integer glBankTransactionId) {
        this.glBankTransactionId = glBankTransactionId;
    }

    /**
     * @return the accNumberDebit
     */
    public Integer getAccNumberDebit() {
        return accNumberDebit;
    }

    /**
     * @param accNumberDebit the accNumberDebit to set
     */
    public void setAccNumberDebit(Integer accNumberDebit) {
        this.accNumberDebit = accNumberDebit;
    }

    /**
     * @return the accNumberCredit
     */
    public Integer getAccNumberCredit() {
        return accNumberCredit;
    }

    /**
     * @param accNumberCredit the accNumberCredit to set
     */
    public void setAccNumberCredit(Integer accNumberCredit) {
        this.accNumberCredit = accNumberCredit;
    }

    /**
     * @return the accNameDebit
     */
    public String getAccNameDebit() {
        return accNameDebit;
    }

    /**
     * @param accNameDebit the accNameDebit to set
     */
    public void setAccNameDebit(String accNameDebit) {
        this.accNameDebit = accNameDebit;
    }

    /**
     * @return the accNameCredit
     */
    public String getAccNameCredit() {
        return accNameCredit;
    }

    /**
     * @param accNameCredit the accNameCredit to set
     */
    public void setAccNameCredit(String accNameCredit) {
        this.accNameCredit = accNameCredit;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the valueLocal
     */
    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    /**
     * @param valueLocal the valueLocal to set
     */
    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
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
     * @return the accNumber
     */
    public Integer getAccNumber() {
        return accNumber;
    }

    /**
     * @param accNumber the accNumber to set
     */
    public void setAccNumber(Integer accNumber) {
        this.accNumber = accNumber;
    }

    /**
     * @return the valueDebit
     */
    public BigDecimal getValueDebit() {
        return valueDebit;
    }

    /**
     * @param valueDebit the valueDebit to set
     */
    public void setValueDebit(BigDecimal valueDebit) {
        this.valueDebit = valueDebit;
    }

    /**
     * @return the valueCredit
     */
    public BigDecimal getValueCredit() {
        return valueCredit;
    }

    /**
     * @param valueCredit the valueCredit to set
     */
    public void setValueCredit(BigDecimal valueCredit) {
        this.valueCredit = valueCredit;
    }

    /**
     * @return the valueLocalDebit
     */
    public BigDecimal getValueLocalDebit() {
        return valueLocalDebit;
    }

    /**
     * @param valueLocalDebit the valueLocalDebit to set
     */
    public void setValueLocalDebit(BigDecimal valueLocalDebit) {
        this.valueLocalDebit = valueLocalDebit;
    }

    /**
     * @return the valueLocalCredit
     */
    public BigDecimal getValueLocalCredit() {
        return valueLocalCredit;
    }

    /**
     * @param valueLocalCredit the valueLocalCredit to set
     */
    public void setValueLocalCredit(BigDecimal valueLocalCredit) {
        this.valueLocalCredit = valueLocalCredit;
    }

    /**
     * @return the accName
     */
    public String getAccName() {
        return accName;
    }

    /**
     * @param accName the accName to set
     */
    public void setAccName(String accName) {
        this.accName = accName;
    }

    /**
     * @return the glAccount
     */
    public GlAccount getGlAccount() {
        return glAccount;
    }

    /**
     * @param glAccount the glAccount to set
     */
    public void setGlAccount(GlAccount glAccount) {
        this.glAccount = glAccount;
    }

    /**
     * @return the costCenter
     */
    public CostCenter getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the adminUnit
     */
    public GlAdminUnit getAdminUnit() {
        return adminUnit;
    }

    /**
     * @param adminUnit the adminUnit to set
     */
    public void setAdminUnit(GlAdminUnit adminUnit) {
        this.adminUnit = adminUnit;
    }

    
}
