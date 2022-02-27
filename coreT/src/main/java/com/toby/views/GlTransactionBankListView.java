/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "gl_transaction_bank_list_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlTransactionBankListView.findAll", query = "SELECT g FROM GlTransactionBankListView g")
    , @NamedQuery(name = "GlTransactionBankListView.findByRowNum", query = "SELECT g FROM GlTransactionBankListView g WHERE g.rowNum = :rowNum")
    , @NamedQuery(name = "GlTransactionBankListView.findByBankFromId", query = "SELECT g FROM GlTransactionBankListView g WHERE g.bankFromId = :bankFromId")
    , @NamedQuery(name = "GlTransactionBankListView.findByBankFromName", query = "SELECT g FROM GlTransactionBankListView g WHERE g.bankFromName = :bankFromName")
    , @NamedQuery(name = "GlTransactionBankListView.findByBankFromCode", query = "SELECT g FROM GlTransactionBankListView g WHERE g.bankFromCode = :bankFromCode")
    , @NamedQuery(name = "GlTransactionBankListView.findByBankToId", query = "SELECT g FROM GlTransactionBankListView g WHERE g.bankToId = :bankToId")
    , @NamedQuery(name = "GlTransactionBankListView.findByBankToName", query = "SELECT g FROM GlTransactionBankListView g WHERE g.bankToName = :bankToName")
    , @NamedQuery(name = "GlTransactionBankListView.findByBankToCode", query = "SELECT g FROM GlTransactionBankListView g WHERE g.bankToCode = :bankToCode")
    , @NamedQuery(name = "GlTransactionBankListView.findByValueLocal", query = "SELECT g FROM GlTransactionBankListView g WHERE g.valueLocal = :valueLocal")
    , @NamedQuery(name = "GlTransactionBankListView.findByDate", query = "SELECT g FROM GlTransactionBankListView g WHERE g.date = :date")
    , @NamedQuery(name = "GlTransactionBankListView.findByRemark", query = "SELECT g FROM GlTransactionBankListView g WHERE g.remark = :remark")
    , @NamedQuery(name = "GlTransactionBankListView.findByBranchId", query = "SELECT g FROM GlTransactionBankListView g WHERE g.branchId = :branchId")
    , @NamedQuery(name = "GlTransactionBankListView.findByTransactionType", query = "SELECT g FROM GlTransactionBankListView g WHERE g.transactionType = :transactionType")})
public class GlTransactionBankListView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "bank_from_id")
    private Integer bankFromId;
    @Size(max = 45)
    @Column(name = "bank_from_name")
    private String bankFromName;
    @Size(max = 45)
    @Column(name = "bank_from_code")
    private String bankFromCode;
    @Column(name = "bank_to_id")
    private Integer bankToId;
    @Size(max = 45)
    @Column(name = "bank_to_name")
    private String bankToName;
    @Size(max = 45)
    @Column(name = "bank_to_code")
    private String bankToCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "transaction_type")
    private Integer transactionType;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Column(name = "id")
    private Integer id;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "payment_type")
    private Integer paymentType;
    
    public GlTransactionBankListView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getBankFromId() {
        return bankFromId;
    }

    public void setBankFromId(Integer bankFromId) {
        this.bankFromId = bankFromId;
    }

    public String getBankFromName() {
        return bankFromName;
    }

    public void setBankFromName(String bankFromName) {
        this.bankFromName = bankFromName;
    }

    public String getBankFromCode() {
        return bankFromCode;
    }

    public void setBankFromCode(String bankFromCode) {
        this.bankFromCode = bankFromCode;
    }

    public Integer getBankToId() {
        return bankToId;
    }

    public void setBankToId(Integer bankToId) {
        this.bankToId = bankToId;
    }

    public String getBankToName() {
        return bankToName;
    }

    public void setBankToName(String bankToName) {
        this.bankToName = bankToName;
    }

    public String getBankToCode() {
        return bankToCode;
    }

    public void setBankToCode(String bankToCode) {
        this.bankToCode = bankToCode;
    }

    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
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
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the paymentType
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
    
}
