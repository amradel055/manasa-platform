/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author Igma
 */
public class BankBalanceReportBeanNew {

    private String bankName;
    private String bankCode;
    private Integer bankId;
    private Integer accountIdTo;
    private Integer accountIdFrom;
    private BigDecimal credit = BigDecimal.ZERO;
    private BigDecimal debit = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal previousBalance = BigDecimal.ZERO;
    private BigDecimal localValue = BigDecimal.ZERO;
    private BigDecimal bankFromBalance = BigDecimal.ZERO;
    private BigDecimal bankToBalance = BigDecimal.ZERO;
    private String serial;
    private String itemName;
    private Integer itemSerial;

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the previousBalance
     */
    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    /**
     * @param previousBalance the previousBalance to set
     */
    public void setPreviousBalance(BigDecimal previousBalance) {
        this.previousBalance = previousBalance;
    }

    /**
     * @return the localValue
     */
    public BigDecimal getLocalValue() {
        return localValue;
    }

    /**
     * @param localValue the localValue to set
     */
    public void setLocalValue(BigDecimal localValue) {
        this.localValue = localValue;
    }

    /**
     * @return the bankId
     */
    public Integer getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the accountIdTo
     */
    public Integer getAccountIdTo() {
        return accountIdTo;
    }

    /**
     * @param accountIdTo the accountIdTo to set
     */
    public void setAccountIdTo(Integer accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    /**
     * @return the accountIdFrom
     */
    public Integer getAccountIdFrom() {
        return accountIdFrom;
    }

    /**
     * @param accountIdFrom the accountIdFrom to set
     */
    public void setAccountIdFrom(Integer accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the bankFromBalance
     */
    public BigDecimal getBankFromBalance() {
        return bankFromBalance;
    }

    /**
     * @param bankFromBalance the bankFromBalance to set
     */
    public void setBankFromBalance(BigDecimal bankFromBalance) {
        this.bankFromBalance = bankFromBalance;
    }

    /**
     * @return the bankToBalance
     */
    public BigDecimal getBankToBalance() {
        return bankToBalance;
    }

    /**
     * @param bankToBalance the bankToBalance to set
     */
    public void setBankToBalance(BigDecimal bankToBalance) {
        this.bankToBalance = bankToBalance;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the itemSerial
     */
    public Integer getItemSerial() {
        return itemSerial;
    }

    /**
     * @param itemSerial the itemSerial to set
     */
    public void setItemSerial(Integer itemSerial) {
        this.itemSerial = itemSerial;
    }

    /**
     * @return the bankCode
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
