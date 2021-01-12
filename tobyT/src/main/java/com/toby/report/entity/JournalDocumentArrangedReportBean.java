/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class JournalDocumentArrangedReportBean {

    private Integer journalNum;
    private Integer documentNum;
    private Date journalDate;
    private Date creationDate;
    private String revision;
    private String posting;
    private BigDecimal amount;
    private String user;
    private String remarks;
    private String postFlag;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private Integer accountNumber;
    private String accountName;
    private String costCenterName;
    private String adminUnitName;
    private String discribtion;
    private String generalType;

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

    /**
     * @return the revision
     */
    public String getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * @return the posting
     */
    public String getPosting() {
        return posting;
    }

    /**
     * @param posting the posting to set
     */
    public void setPosting(String posting) {
        this.posting = posting;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
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
     * @return the postFlag
     */
    public String getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(String postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the debitAmount
     */
    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    /**
     * @param debitAmount the debitAmount to set
     */
    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    /**
     * @return the creditAmount
     */
    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    /**
     * @param creditAmount the creditAmount to set
     */
    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
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
     * @return the costCenterName
     */
    public String getCostCenterName() {
        return costCenterName;
    }

    /**
     * @param costCenterName the costCenterName to set
     */
    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    /**
     * @return the adminUnitName
     */
    public String getAdminUnitName() {
        return adminUnitName;
    }

    /**
     * @param adminUnitName the adminUnitName to set
     */
    public void setAdminUnitName(String adminUnitName) {
        this.adminUnitName = adminUnitName;
    }

    /**
     * @return the discribtion
     */
    public String getDiscribtion() {
        return discribtion;
    }

    /**
     * @param discribtion the discribtion to set
     */
    public void setDiscribtion(String discribtion) {
        this.discribtion = discribtion;
    }

    /**
     * @return the journalNum
     */
    public Integer getJournalNum() {
        return journalNum;
    }

    /**
     * @param journalNum the journalNum to set
     */
    public void setJournalNum(Integer journalNum) {
        this.journalNum = journalNum;
    }

    /**
     * @return the documentNum
     */
    public Integer getDocumentNum() {
        return documentNum;
    }

    /**
     * @param documentNum the documentNum to set
     */
    public void setDocumentNum(Integer documentNum) {
        this.documentNum = documentNum;
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

    

    /**
     * @return the generalType
     */
    public String getGeneralType() {
        return generalType;
    }

    /**
     * @param generalType the generalType to set
     */
    public void setGeneralType(String generalType) {
        this.generalType = generalType;
    }

}
