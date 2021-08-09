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
@Table(name = "bank_transaction_view")
public class BankTransactionView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 255)
    @Column(name = "glValue")
    private BigDecimal glValue;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Column(name = "bankIdFrom")
    private Integer bankIdFrom;
    @Size(max = 255)
    @Column(name = "nameBankFrom")
    private String nameBankFrom;
    @Column(name = "bankIdTo")
    private Integer bankIdTo;
    @Size(max = 255)
    @Column(name = "nameBankTo")
    private String nameBankTo;
    @Column(name = "branchId")
    private Integer branchId;
    @Size(max = 255)
    @Column(name = "remark")
    private String remark;

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
     * @return the nameBankFrom
     */
    public String getNameBankFrom() {
        return nameBankFrom;
    }

    /**
     * @param nameBankFrom the nameBankFrom to set
     */
    public void setNameBankFrom(String nameBankFrom) {
        this.nameBankFrom = nameBankFrom;
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
     * @return the nameBankTo
     */
    public String getNameBankTo() {
        return nameBankTo;
    }

    /**
     * @param nameBankTo the nameBankTo to set
     */
    public void setNameBankTo(String nameBankTo) {
        this.nameBankTo = nameBankTo;
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
}
