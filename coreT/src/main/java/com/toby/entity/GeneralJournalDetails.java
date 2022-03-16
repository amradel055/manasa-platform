/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "general_journal_details")

public class GeneralJournalDetails extends BaseEntity implements Serializable {

    @Column(name = "serial")
    private Integer serial;
    @ManyToOne()
    @JoinColumn(name = "glaccount_id", referencedColumnName = "id")
    private GlAccount glACCOUNTId;
    @ManyToOne()
    @JoinColumn(name = "gl_assistant_account_id", referencedColumnName = "id")
    private GlAccount glAssistantAccount;
    @ManyToOne()
    @JoinColumn(name = "cost_center_id", referencedColumnName = "id")
    private CostCenter costCenterId;
    @ManyToOne()
    @JoinColumn(name = "admin_unit_id", referencedColumnName = "id")
    private GlAdminUnit adminUnitId;
    @Column(name = "debit_amount", columnDefinition = "Decimal(10,2)")
    private BigDecimal debitAmount;
    @Column(name = "credit_amount", columnDefinition = "Decimal(10,2)")
    private BigDecimal creditamount;

    @Column(name = "debit_amount_local", columnDefinition = "Decimal(10,2)")
    private BigDecimal debitAmountLocal;
    @Column(name = "credit_amount_local", columnDefinition = "Decimal(10,2)")
    private BigDecimal creditamountLocal;
    @Column(name = "discribtion")
    private String discribtion;
    @ManyToOne()
    @JoinColumn(name = "general_journal_id", referencedColumnName = "id")
    private GeneralJournal generalJournalId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne()
    private Branch branchId;
    @Column(name = "currency_rate")
    private BigDecimal rate;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;

    @Transient
    private Integer index;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeneralJournalDetails)) {
            return false;
        }
        GeneralJournalDetails other = (GeneralJournalDetails) object;
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
    public GlAccount getGlACCOUNTId() {
        return glACCOUNTId;
    }

    /**
     * @param glACCOUNTId the glACCOUNTId to set
     */
    public void setGlACCOUNTId(GlAccount glACCOUNTId) {
        this.glACCOUNTId = glACCOUNTId;
    }

    /**
     * @return the costCenterId
     */
    public CostCenter getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(CostCenter costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the adminUnitId
     */
    public GlAdminUnit getAdminUnitId() {
        return adminUnitId;
    }

    /**
     * @param adminUnitId the adminUnitId to set
     */
    public void setAdminUnitId(GlAdminUnit adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public String getDiscribtion() {
        return discribtion;
    }

    public void setDiscribtion(String discribtion) {
        this.discribtion = discribtion;
    }

    public GeneralJournal getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(GeneralJournal generalJournalId) {
        this.generalJournalId = generalJournalId;
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the glAssistantAccount
     */
    public GlAccount getGlAssistantAccount() {
        return glAssistantAccount;
    }

    /**
     * @param glAssistantAccount the glAssistantAccount to set
     */
    public void setGlAssistantAccount(GlAccount glAssistantAccount) {
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
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

}
