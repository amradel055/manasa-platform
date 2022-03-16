package com.isag.report.entity;

import java.math.BigDecimal;

public class JournalDocumentDailyReportBean {

    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private Integer accountNumber;
    private String accountName;
    private String costCenterName;
    private String adminUnitName;
    private String discribtion;

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getAdminUnitName() {
        return adminUnitName;
    }

    public void setAdminUnitName(String adminUnitName) {
        this.adminUnitName = adminUnitName;
    }

    public String getDiscribtion() {
        return discribtion;
    }

    public void setDiscribtion(String discribtion) {
        this.discribtion = discribtion;
    }
}
