/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class GeneralJournalDetailsDTO extends BaseEntityDTO {
    
     
   
    private GlAccountDTO glACCOUNTId; 
    private GlAccountDTO glAssistantAccount; 
    private CostCenterDTO costCenterId; 
    private GlAdminUnitDTO adminUnitId;
    private GeneralJournalDTO generalJournalId; 
    
    
    
    private Integer glAccount; 
    private Integer costCenter; 
    private Integer adminUnit; 
    private Integer generalJournal; 
    private Integer currency;
    private BigDecimal debitAmount; 
    private BigDecimal creditamount;  
    private BigDecimal debitAmountLocal; 
    private BigDecimal creditamountLocal;
    private Integer branchId; 
    private BigDecimal rate; 
    private CurrencyDTO currencyId;
    private Integer companyId;
    private Integer modifiedBy ; 
    private Date modificationDate;
    private TobyUserDTO created_By ;  
    private Integer serial;  
    private String discribtion; 
    private String glAccountName;
    private String glAccountNumber;
    private String glCostCenterName;
    private String glCostCenterNumber;
    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournalDetailsDTO)) {
            return false;
        }
        GeneralJournalDetailsDTO other = (GeneralJournalDetailsDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + id + " ]";
    }

    /**
     * @return the glACCOUNTId
     */
    public GlAccountDTO getGlACCOUNTId() {
        return glACCOUNTId;
    }

    /**
     * @param glACCOUNTId the glACCOUNTId to set
     */
    public void setGlACCOUNTId(GlAccountDTO glACCOUNTId) {
        this.glACCOUNTId = glACCOUNTId;
    }

    /**
     * @return the costCenterId
     */
    public CostCenterDTO getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(CostCenterDTO costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the adminUnitId
     */
    public GlAdminUnitDTO getAdminUnitId() {
        return adminUnitId;
    }

    /**
     * @param adminUnitId the adminUnitId to set
     */
    public void setAdminUnitId(GlAdminUnitDTO adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public String getDiscribtion() {
        return discribtion;
    }

    public void setDiscribtion(String discribtion) {
        this.discribtion = discribtion;
    }

    public GeneralJournalDTO getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(GeneralJournalDTO generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public CurrencyDTO getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(CurrencyDTO currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the glAssistantAccount
     */
    public GlAccountDTO getGlAssistantAccount() {
        return glAssistantAccount;
    }

    /**
     * @param glAssistantAccount the glAssistantAccount to set
     */
    public void setGlAssistantAccount(GlAccountDTO glAssistantAccount) {
        this.glAssistantAccount = glAssistantAccount;
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

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
     * @return the created_By
     */
    public TobyUserDTO getCreated_By() {
        return created_By;
    }

    /**
     * @param created_By the created_By to set
     */
    public void setCreated_By(TobyUserDTO created_By) {
        this.created_By = created_By;
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
 

}
