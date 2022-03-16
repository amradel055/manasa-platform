/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BankBalanceViewNew implements Serializable {

    private Date date;

    private BigDecimal glValue;
    private BigDecimal valueLocal;
    private Integer bankIdFrom;
    private Integer bankIdTo;
    private Integer branchId;
    private Integer transactionType;
    private Integer glYear;
    private String serial;

    private String remark;

    private String bankNameTo;

    private String bankNameFrom;
    private String accountIdTo;
    private String accountIdFrom;
    private String bankCodeFrom;
    private String bankCodeto;

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the glValue
     */
    public BigDecimal getGlValue() {
        return glValue;
    }

    /**
     * @param glValue the glValue to set
     */
    public void setGlValue(BigDecimal glValue) {
        this.glValue = glValue;
    }

    /**
     * @return the bankIdFrom
     */
    public Integer getBankIdFrom() {
        return bankIdFrom;
    }

    /**
     * @param bankIdFrom the bankIdFrom to set
     */
    public void setBankIdFrom(Integer bankIdFrom) {
        this.bankIdFrom = bankIdFrom;
    }

    /**
     * @return the bankIdTo
     */
    public Integer getBankIdTo() {
        return bankIdTo;
    }

    /**
     * @param bankIdTo the bankIdTo to set
     */
    public void setBankIdTo(Integer bankIdTo) {
        this.bankIdTo = bankIdTo;
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

    /**
     * @return the valueLocal
     */
    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    /**
     * @param valueLocal the valueLocal to set
     */
    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the transactionType
     */
    public Integer getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return the bankNameTo
     */
    public String getBankNameTo() {
        return bankNameTo;
    }

    /**
     * @param bankNameTo the bankNameTo to set
     */
    public void setBankNameTo(String bankNameTo) {
        this.bankNameTo = bankNameTo;
    }

    /**
     * @return the bankNameFrom
     */
    public String getBankNameFrom() {
        return bankNameFrom;
    }

    /**
     * @param bankNameFrom the bankNameFrom to set
     */
    public void setBankNameFrom(String bankNameFrom) {
        this.bankNameFrom = bankNameFrom;
    }

    /**
     * @return the accountIdTo
     */
    public String getAccountIdTo() {
        return accountIdTo;
    }

    /**
     * @param accountIdTo the accountIdTo to set
     */
    public void setAccountIdTo(String accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    /**
     * @return the accountIdFrom
     */
    public String getAccountIdFrom() {
        return accountIdFrom;
    }

    /**
     * @param accountIdFrom the accountIdFrom to set
     */
    public void setAccountIdFrom(String accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    /**
     * @return the glYear
     */
    public Integer getGlYear() {
        return glYear;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYear(Integer glYear) {
        this.glYear = glYear;
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
     * @return the bankCodeFrom
     */
    public String getBankCodeFrom() {
        return bankCodeFrom;
    }

    /**
     * @param bankCodeFrom the bankCodeFrom to set
     */
    public void setBankCodeFrom(String bankCodeFrom) {
        this.bankCodeFrom = bankCodeFrom;
    }

    /**
     * @return the bankCodeto
     */
    public String getBankCodeto() {
        return bankCodeto;
    }

    /**
     * @param bankCodeto the bankCodeto to set
     */
    public void setBankCodeto(String bankCodeto) {
        this.bankCodeto = bankCodeto;
    }
}
