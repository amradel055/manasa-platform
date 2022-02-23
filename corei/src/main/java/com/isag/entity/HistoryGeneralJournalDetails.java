/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "_history_general_journal_details")

public class HistoryGeneralJournalDetails implements Serializable {


    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the companyId
     */
    public IsagCompany getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(IsagCompany companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the createdBy
     */
    public IsagUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the modifiedBy
     */
    public IsagUser getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(IsagUser modifiedBy) {
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
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne()
    private IsagCompany companyId;

    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne()
    private IsagUser createdBy;

    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne()
    private IsagUser modifiedBy;

    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "serial_new")
    private Integer serialNew;
    @Column(name = "glaccount_id")
    private Integer glACCOUNTId;
    @Column(name = "glaccount_id_new")
    private Integer glaccountIdNew;
    @Column(name = "gl_assistant_account_id")
    private Integer glAssistantAccount;
    @Column(name = "gl_assistant_account_id_new")
    private Integer glAssistantAccountIdNew;
    @Column(name = "cost_center_id")
    private Integer costCenterId;
    @Column(name = "cost_center_id_new")
    private Integer costCenterIdNew;
    @Column(name = "admin_unit_id")
    private Integer adminUnitId;
    @Column(name = "admin_unit_id_new")
    private Integer adminUnitIdNew;
    @Column(name = "debit_amount", columnDefinition = "Decimal(10,2)")
    private BigDecimal debitAmount;
    @Column(name = "debit_amount_new", columnDefinition = "Decimal(10,2)")
    private BigDecimal debitAmountNew;
    @Column(name = "credit_amount", columnDefinition = "Decimal(10,2)")
    private BigDecimal creditamount;
    @Column(name = "credit_amount_new", columnDefinition = "Decimal(10,2)")
    private BigDecimal creditAmountNew;
    @Column(name = "discribtion")
    private String discribtion;
    @Column(name = "discribtion_new")
    private String discribtionNew;
    @Column(name = "general_journal_id")
    private Integer generalJournalId;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "currency_rate")
    private BigDecimal rate; 
    @Column(name = "currency_rate_new")
    private BigDecimal rateNew; 
    @Column(name = "currency_id")
    private Integer currencyId;
    @Column(name = "currency_id_new")
    private Integer currencyIdNew;
    @Column(name = "action")
    private int rowStatus;    
    @Column(name = "modified_by_new")
    private Integer modifiedByNew;    
    @Column(name = "modification_date_new")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDateNew;  
    
    @Transient
    private Boolean markEdit = Boolean.FALSE;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoryGeneralJournalDetails)) {
            return false;
        }
        HistoryGeneralJournalDetails other = (HistoryGeneralJournalDetails) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GeneralJornal[ id=" + getId() + " ]";
    }

    /**
     * @return the glACCOUNTId
     */
    public Integer getGlACCOUNTId() {
        return glACCOUNTId;
    }

    /**
     * @param glACCOUNTId the glACCOUNTId to set
     */
    public void setGlACCOUNTId(Integer glACCOUNTId) {
        this.glACCOUNTId = glACCOUNTId;
    }

    /**
     * @return the costCenterId
     */
    public Integer getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the adminUnitId
     */
    public Integer getAdminUnitId() {
        return adminUnitId;
    }

    /**
     * @param adminUnitId the adminUnitId to set
     */
    public void setAdminUnitId(Integer adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public String getDiscribtion() {
        return discribtion;
    }

    public void setDiscribtion(String discribtion) {
        this.discribtion = discribtion;
    }

    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(Integer generalJournalId) {
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

    public Boolean getMarkEdit() {
        return markEdit;
    }

    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    /**
     * @return the glAssistantAccount
     */
    public Integer getGlAssistantAccount() {
        return glAssistantAccount;
    }

    /**
     * @param glAssistantAccount the glAssistantAccount to set
     */
    public void setGlAssistantAccount(Integer glAssistantAccount) {
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
     * @return the serialNew
     */
    public Integer getSerialNew() {
        return serialNew;
    }

    /**
     * @param serialNew the serialNew to set
     */
    public void setSerialNew(Integer serialNew) {
        this.serialNew = serialNew;
    }

    /**
     * @return the glaccountIdNew
     */
    public Integer getGlaccountIdNew() {
        return glaccountIdNew;
    }

    /**
     * @param glaccountIdNew the glaccountIdNew to set
     */
    public void setGlaccountIdNew(Integer glaccountIdNew) {
        this.glaccountIdNew = glaccountIdNew;
    }

    /**
     * @return the glAssistantAccountIdNew
     */
    public Integer getGlAssistantAccountIdNew() {
        return glAssistantAccountIdNew;
    }

    /**
     * @param glAssistantAccountIdNew the glAssistantAccountIdNew to set
     */
    public void setGlAssistantAccountIdNew(Integer glAssistantAccountIdNew) {
        this.glAssistantAccountIdNew = glAssistantAccountIdNew;
    }

    /**
     * @return the costCenterIdNew
     */
    public Integer getCostCenterIdNew() {
        return costCenterIdNew;
    }

    /**
     * @param costCenterIdNew the costCenterIdNew to set
     */
    public void setCostCenterIdNew(Integer costCenterIdNew) {
        this.costCenterIdNew = costCenterIdNew;
    }

    /**
     * @return the adminUnitIdNew
     */
    public Integer getAdminUnitIdNew() {
        return adminUnitIdNew;
    }

    /**
     * @param adminUnitIdNew the adminUnitIdNew to set
     */
    public void setAdminUnitIdNew(Integer adminUnitIdNew) {
        this.adminUnitIdNew = adminUnitIdNew;
    }

    /**
     * @return the debitAmountNew
     */
    public BigDecimal getDebitAmountNew() {
        return debitAmountNew;
    }

    /**
     * @param debitAmountNew the debitAmountNew to set
     */
    public void setDebitAmountNew(BigDecimal debitAmountNew) {
        this.debitAmountNew = debitAmountNew;
    }

    /**
     * @return the creditAmountNew
     */
    public BigDecimal getCreditAmountNew() {
        return creditAmountNew;
    }

    /**
     * @param creditAmountNew the creditAmountNew to set
     */
    public void setCreditAmountNew(BigDecimal creditAmountNew) {
        this.creditAmountNew = creditAmountNew;
    }

    /**
     * @return the discribtionNew
     */
    public String getDiscribtionNew() {
        return discribtionNew;
    }

    /**
     * @param discribtionNew the discribtionNew to set
     */
    public void setDiscribtionNew(String discribtionNew) {
        this.discribtionNew = discribtionNew;
    }

    /**
     * @return the rateNew
     */
    public BigDecimal getRateNew() {
        return rateNew;
    }

    /**
     * @param rateNew the rateNew to set
     */
    public void setRateNew(BigDecimal rateNew) {
        this.rateNew = rateNew;
    }

    /**
     * @return the currencyIdNew
     */
    public Integer getCurrencyIdNew() {
        return currencyIdNew;
    }

    /**
     * @param currencyIdNew the currencyIdNew to set
     */
    public void setCurrencyIdNew(Integer currencyIdNew) {
        this.currencyIdNew = currencyIdNew;
    }

    /**
     * @return the rowStatus
     */
    public int getRowStatus() {
        return rowStatus;
    }

    /**
     * @param rowStatus the rowStatus to set
     */
    public void setRowStatus(int rowStatus) {
        this.rowStatus = rowStatus;
    }


    /**
     * @return the modificationDateNew
     */
    public Date getModificationDateNew() {
        return modificationDateNew;
    }

    /**
     * @param modificationDateNew the modificationDateNew to set
     */
    public void setModificationDateNew(Date modificationDateNew) {
        this.modificationDateNew = modificationDateNew;
    }

    /**
     * @return the modifiedByNew
     */
    public Integer getModifiedByNew() {
        return modifiedByNew;
    }

    /**
     * @param modifiedByNew the modifiedByNew to set
     */
    public void setModifiedByNew(Integer modifiedByNew) {
        this.modifiedByNew = modifiedByNew;
    }

}
