/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class BalanceAccountMonthlyReportBean {
   private Integer journalNumber;
   private Integer documentNumber ; 
   private String documentType;
   private Date documentDate ;
   private String Statement;
   private BigDecimal debit ; 
   private BigDecimal credit ; 
   private Integer accountNumber;
   private String accountName;
   private BigDecimal totalDebit;
   private BigDecimal totalCredit;
   private String journal;
   private String costCenter;
   private String adminUnit;
   private String journalCreator;
   private Date journalDate;

    /**
     * @return the journalNumber
     */
    public Integer getJournalNumber() {
        return journalNumber;
    }

    /**
     * @param journalNumber the journalNumber to set
     */
    public void setJournalNumber(Integer journalNumber) {
        this.journalNumber = journalNumber;
    }

    /**
     * @return the documentNumber
     */
    public Integer getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(Integer documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @return the documentDate
     */
    public Date getDocumentDate() {
        return documentDate;
    }

    /**
     * @param documentDate the documentDate to set
     */
    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    /**
     * @return the Statement
     */
    public String getStatement() {
        return Statement;
    }

    /**
     * @param Statement the Statement to set
     */
    public void setStatement(String Statement) {
        this.Statement = Statement;
    }

    /**
     * @return the debit
     */
    public BigDecimal getDebit() {
        return debit;
    }

    /**
     * @param debit the debit to set
     */
    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    /**
     * @return the credit
     */
    public BigDecimal getCredit() {
        return credit;
    }

    /**
     * @param credit the credit to set
     */
    public void setCredit(BigDecimal credit) {
        this.credit = credit;
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
     * @return the totalDebit
     */
    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    /**
     * @param totalDebit the totalDebit to set
     */
    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    /**
     * @return the totalCredit
     */
    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    /**
     * @param totalCredit the totalCredit to set
     */
    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    /**
     * @return the journal
     */
    public String getJournal() {
        return journal;
    }

    /**
     * @param journal the journal to set
     */
    public void setJournal(String journal) {
        this.journal = journal;
    }

    /**
     * @return the costCenter
     */
    public String getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the adminUnit
     */
    public String getAdminUnit() {
        return adminUnit;
    }

    /**
     * @param adminUnit the adminUnit to set
     */
    public void setAdminUnit(String adminUnit) {
        this.adminUnit = adminUnit;
    }

    /**
     * @return the journalCreator
     */
    public String getJournalCreator() {
        return journalCreator;
    }

    /**
     * @param journalCreator the journalCreator to set
     */
    public void setJournalCreator(String journalCreator) {
        this.journalCreator = journalCreator;
    }

    /**
     * @return the journalDate
     */
    public Date getJournalDate() {
        return journalDate;
    }

    /**
     * @param journalDate the journalDate to set
     */
    public void setJournalDate(Date journalDate) {
        this.journalDate = journalDate;
    }
    
}
