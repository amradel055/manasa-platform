/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.CurrencyDTO;
import com.toby.dto.TobyUserDTO;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class GeneralJournalDetailsDTOAPI extends BaseEntityDTOAPI  {
    
    private Integer glAccount; 
    private Integer costCenter; 
    private Integer adminUnit; 
    private Integer generalJournal; 
    private Integer currency;
    private String currencyName;
    private BigDecimal debitAmount; 
    private BigDecimal creditamount;  
    private BigDecimal debitAmountLocal; 
    private BigDecimal creditamountLocal;
    private BigDecimal rate; 
    private Integer modifiedBy ; 
    private Date modificationDate;
    private String discribtion; 
    private String glAccountName;
    private String glAccountNumber;
    private String glCostCenterName;
    private String glCostCenterNumber;
    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournalDetailsDTOAPI)) {
            return false;
        }
        GeneralJournalDetailsDTOAPI other = (GeneralJournalDetailsDTOAPI) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + id + " ]";
    }

    public String getDiscribtion() {
        return discribtion;
    }

    public void setDiscribtion(String discribtion) {
        this.discribtion = discribtion;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditamount() {
        return creditamount;
    }

    public void setCreditamount(BigDecimal creditamount) {
        this.creditamount = creditamount;
    }

    /**
     * @return the debitAmountLocal
     */
    public BigDecimal getDebitAmountLocal() {
        return debitAmountLocal;
    }

    /**
     * @param debitAmountLocal the debitAmountLocal to set
     */
    public void setDebitAmountLocal(BigDecimal debitAmountLocal) {
        this.debitAmountLocal = debitAmountLocal;
    }

    /**
     * @return the creditamountLocal
     */
    public BigDecimal getCreditamountLocal() {
        return creditamountLocal;
    }

    /**
     * @param creditamountLocal the creditamountLocal to set
     */
    public void setCreditamountLocal(BigDecimal creditamountLocal) {
        this.creditamountLocal = creditamountLocal;
    }

    /**
     * @return the modifiedBy
     */
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return the glAccount
     */
    public Integer getGlAccount() {
        return glAccount;
    }

    /**
     * @param glAccount the glAccount to set
     */
    public void setGlAccount(Integer glAccount) {
        this.glAccount = glAccount;
    }

    /**
     * @return the costCenter
     */
    public Integer getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(Integer costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the adminUnit
     */
    public Integer getAdminUnit() {
        return adminUnit;
    }

    /**
     * @param adminUnit the adminUnit to set
     */
    public void setAdminUnit(Integer adminUnit) {
        this.adminUnit = adminUnit;
    }

    /**
     * @return the generalJournal
     */
    public Integer getGeneralJournal() {
        return generalJournal;
    }

    /**
     * @param generalJournal the generalJournal to set
     */
    public void setGeneralJournal(Integer generalJournal) {
        this.generalJournal = generalJournal;
    }

    /**
     * @return the currency
     */
    public Integer getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    /**
     * @return the glAccountName
     */
    public String getGlAccountName() {
        return glAccountName;
    }

    /**
     * @param glAccountName the glAccountName to set
     */
    public void setGlAccountName(String glAccountName) {
        this.glAccountName = glAccountName;
    }

    /**
     * @return the glAccountNumber
     */
    public String getGlAccountNumber() {
        return glAccountNumber;
    }

    /**
     * @param glAccountNumber the glAccountNumber to set
     */
    public void setGlAccountNumber(String glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    /**
     * @return the glCostCenterName
     */
    public String getGlCostCenterName() {
        return glCostCenterName;
    }

    /**
     * @param glCostCenterName the glCostCenterName to set
     */
    public void setGlCostCenterName(String glCostCenterName) {
        this.glCostCenterName = glCostCenterName;
    }

    /**
     * @return the glCostCenterNumber
     */
    public String getGlCostCenterNumber() {
        return glCostCenterNumber;
    }

    /**
     * @param glCostCenterNumber the glCostCenterNumber to set
     */
    public void setGlCostCenterNumber(String glCostCenterNumber) {
        this.glCostCenterNumber = glCostCenterNumber;
    }

    /**
     * @return the currencyName
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @param currencyName the currencyName to set
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
 

}
