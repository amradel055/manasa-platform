/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author hq002
 */
@Entity
@Table(name = "bank_balance_view")
public class BankBalanceView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "glValue")
    private BigDecimal glValue;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Column(name = "bankIdFrom")
    private Integer bankIdFrom;
    @Column(name = "bankIdTo")
    private Integer bankIdTo;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "transactionType")
    private Integer transactionType;
    @Column(name = "gl_year")
    private Integer glYear;
    @Column(name = "serial")
    private String serial;
    @Size(max = 255)
    @Column(name = "remark")
    private String remark;
    @Size(max = 255)
    @Column(name = "bankNameTo")
    private String bankNameTo;
    @Size(max = 255)
    @Column(name = "bankNameFrom")
    private String bankNameFrom;
    @Column(name = "accountIdTo")
    private Integer accountIdTo;
    @Column(name = "accountIdFrom")
    private Integer accountIdFrom;
    @Column(name = "bankCodeFrom")
    private String bankCodeFrom;
    @Column(name = "bankCodeto")
    private String bankCodeto;

    /**
     * @return the rowNum
     */
    public Integer getRowNum() {
        return rowNum;
    }

    /**
     * @param rowNum the rowNum to set
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

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
