/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class MainTransactionStatementBean {

    private String accountName;
    private Integer accountNumber;
    private Integer id;
    private Integer level;
    private Integer parentId;
    private BigDecimal ratio = BigDecimal.ZERO;
    private BigDecimal debit = BigDecimal.ZERO;
    private BigDecimal credit = BigDecimal.ZERO;
    private BigDecimal Balance = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal value = BigDecimal.ZERO;
    private List<SubAccountSummaryReport> subAccountSummaryReportList;

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
     * @return the ratio
     */
    public BigDecimal getRatio() {
        return ratio;
    }

    /**
     * @param ratio the ratio to set
     */
    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
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
     * @return the Balance
     */
    public BigDecimal getBalance() {
        return Balance;
    }

    /**
     * @param Balance the Balance to set
     */
    public void setBalance(BigDecimal Balance) {
        this.Balance = Balance;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

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
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
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
     * @return the parentId
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the subAccountSummaryReportList
     */
    public List<SubAccountSummaryReport> getSubAccountSummaryReportList() {
        return subAccountSummaryReportList;
    }

    /**
     * @param subAccountSummaryReportList the subAccountSummaryReportList to set
     */
    public void setSubAccountSummaryReportList(List<SubAccountSummaryReport> subAccountSummaryReportList) {
        this.subAccountSummaryReportList = subAccountSummaryReportList;
    }

}
