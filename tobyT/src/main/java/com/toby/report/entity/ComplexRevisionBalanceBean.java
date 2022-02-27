package com.toby.report.entity;

import java.math.BigDecimal;

public class ComplexRevisionBalanceBean {

    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal debitBalance;
    private BigDecimal creditBalance;
    private Integer accountNumber;
    private Integer accountId;
    private String accountName;
    private String accountClass;
    private BigDecimal firstDurationBalance;
    private Integer costCenterCode;
    private String costCenterName;

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getDebitBalance() {
        return debitBalance;
    }

    public void setDebitBalance(BigDecimal debitBalance) {
        this.debitBalance = debitBalance;
    }

    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
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

    public BigDecimal getFirstDurationBalance() {
        return firstDurationBalance;
    }

    public void setFirstDurationBalance(BigDecimal firstDurationBalance) {
        this.firstDurationBalance = firstDurationBalance;
    }

    /**
     * @return the costCenterCode
     */
    public Integer getCostCenterCode() {
        return costCenterCode;
    }

    /**
     * @param costCenterCode the costCenterCode to set
     */
    public void setCostCenterCode(Integer costCenterCode) {
        this.costCenterCode = costCenterCode;
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
     * @return the accountClass
     */
    public String getAccountClass() {
        return accountClass;
    }

    /**
     * @param accountClass the accountClass to set
     */
    public void setAccountClass(String accountClass) {
        this.accountClass = accountClass;
    }

    /**
     * @return the accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
