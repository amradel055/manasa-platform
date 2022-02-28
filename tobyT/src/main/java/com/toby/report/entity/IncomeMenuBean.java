/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author hq002
 */
public class IncomeMenuBean {

    private String accountName;
    private String accountGroup;
    private Integer id;
    private Integer accountNumber;
    private Integer level;
    private Integer parent;
    private Integer totalReference = 1;
    private BigDecimal value = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal ratio = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal debit = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal credit = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal firstDurationBalance = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal debitBalance = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal creditBalance = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal Balance = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal total = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private Integer colorReference;
    private Integer generalBudgetGuideDebitId;
    private Integer generalBudgetGuideCreditId;
    private boolean appearenceOfTotal;

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
     * @return the parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Integer parent) {
        this.parent = parent;
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
     * @return the debitBalance
     */
    public BigDecimal getDebitBalance() {
        return debitBalance;
    }

    /**
     * @param debitBalance the debitBalance to set
     */
    public void setDebitBalance(BigDecimal debitBalance) {
        this.debitBalance = debitBalance;
    }

    /**
     * @return the creditBalance
     */
    public BigDecimal getCreditBalance() {
        return creditBalance;
    }

    /**
     * @param creditBalance the creditBalance to set
     */
    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
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
     * @return the firstDurationBalance
     */
    public BigDecimal getFirstDurationBalance() {
        return firstDurationBalance;
    }

    /**
     * @param firstDurationBalance the firstDurationBalance to set
     */
    public void setFirstDurationBalance(BigDecimal firstDurationBalance) {
        this.firstDurationBalance = firstDurationBalance;
    }

    /**
     * @return the colorReference
     */
    public Integer getColorReference() {
        return colorReference;
    }

    /**
     * @param colorReference the colorReference to set
     */
    public void setColorReference(Integer colorReference) {
        this.colorReference = colorReference;
    }

    /**
     * @return the appearenceOfTotal
     */
    public boolean isAppearenceOfTotal() {
        return appearenceOfTotal;
    }

    /**
     * @param appearenceOfTotal the appearenceOfTotal to set
     */
    public void setAppearenceOfTotal(boolean appearenceOfTotal) {
        this.appearenceOfTotal = appearenceOfTotal;
    }

    /**
     * @return the accountGroup
     */
    public String getAccountGroup() {
        return accountGroup;
    }

    /**
     * @param accountGroup the accountGroup to set
     */
    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    /**
     * @return the totalReference
     */
    public Integer getTotalReference() {
        return totalReference;
    }

    /**
     * @param totalReference the totalReference to set
     */
    public void setTotalReference(Integer totalReference) {
        this.totalReference = totalReference;
    }

    /**
     * @return the generalBudgetGuideDebitId
     */
    public Integer getGeneralBudgetGuideDebitId() {
        return generalBudgetGuideDebitId;
    }

    /**
     * @param generalBudgetGuideDebitId the generalBudgetGuideDebitId to set
     */
    public void setGeneralBudgetGuideDebitId(Integer generalBudgetGuideDebitId) {
        this.generalBudgetGuideDebitId = generalBudgetGuideDebitId;
    }

    /**
     * @return the generalBudgetGuideCreditId
     */
    public Integer getGeneralBudgetGuideCreditId() {
        return generalBudgetGuideCreditId;
    }

    /**
     * @param generalBudgetGuideCreditId the generalBudgetGuideCreditId to set
     */
    public void setGeneralBudgetGuideCreditId(Integer generalBudgetGuideCreditId) {
        this.generalBudgetGuideCreditId = generalBudgetGuideCreditId;
    }
}
