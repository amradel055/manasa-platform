/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "post_restrictions_details_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostRestrictionsDetailsView.findAll", query = "SELECT p FROM PostRestrictionsDetailsView p")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByRowNum", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.rowNum = :rowNum")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByGlBankTransactionId", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.glBankTransactionId = :glBankTransactionId")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByAccountDebitId", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.accountDebitId = :accountDebitId")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByAccountCreditId", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.accountCreditId = :accountCreditId")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByCostCenterId", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.costCenterId = :costCenterId")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByAdminUnitId", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.adminUnitId = :adminUnitId")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByAccNumberDebit", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.accNumberDebit = :accNumberDebit")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByAccNumberCredit", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.accNumberCredit = :accNumberCredit")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByAccNameDebit", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.accNameDebit = :accNameDebit")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByAccNameCredit", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.accNameCredit = :accNameCredit")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByValue", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.value = :value")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByValueLocal", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.valueLocal = :valueLocal")
    ,
    @NamedQuery(name = "PostRestrictionsDetailsView.findByRemarks", query = "SELECT p FROM PostRestrictionsDetailsView p WHERE p.remarks = :remarks")})
public class PostRestrictionsDetailsView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "gl_bank_transaction_id")
    private Integer glBankTransactionId;
    @Column(name = "account_debit_id")
    private Integer accountDebitId;
    @Column(name = "account_credit_id")
    private Integer accountCreditId;
    @Column(name = "second_account_credit_id")
    private Integer secondAccountCreditId;
    @Column(name = "cost_center_id")
    private Integer costCenterId;
    @Column(name = "admin_unit_id")
    private Integer adminUnitId;
    @Column(name = "acc_number_debit")
    private Integer accNumberDebit;
    @Column(name = "acc_number_credit")
    private Integer accNumberCredit;
    @Column(name = "second_acc_number_credit")
    private Integer secondAccNumberCredit;
    @Size(max = 45)
    @Column(name = "acc_name_debit")
    private String accNameDebit;
    @Size(max = 45)
    @Column(name = "acc_name_credit")
    private String accNameCredit;
    @Size(max = 45)
    @Column(name = "second_acc_name_credit")
    private String secondAccNameCredit;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Column(name = "second_value")
    private BigDecimal secondValue;
    @Column(name = "tax_value")
    private BigDecimal taxValue;
    @Column(name = "tax_value_local")
    private BigDecimal taxValueLocal;
    @Size(max = 450)
    @Column(name = "remarks")
    private String remarks;
    @Basic(optional = false)
    @NotNull
    @Column(name = "module_code")
    private Integer moduleCode;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "transaction_type")
    private Integer transactionType;

    @Transient
    private BigDecimal taxDiscValue;
    @Transient
    private BigDecimal taxDiscValueLocal;
    @Transient
    private String errorMsg;
    @Transient
    private Integer secondAccountDebitId;
    @Transient
    private String secondAccNameDebit;
    @Transient
    private Integer secondAccNumberDebit;
    @Transient
    private Integer thirdAccountCreditId;
    @Transient
    private String thirdAccNameCredit;
    @Transient
    private Integer thirdAccNumberCredit;
    @Transient
    private Integer thirdAccountDebitId;
    @Transient
    private String thirdAccNameDebit;
    @Transient
    private Integer thirdAccNumberDebit;

    public PostRestrictionsDetailsView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getGlBankTransactionId() {
        return glBankTransactionId;
    }

    public void setGlBankTransactionId(Integer glBankTransactionId) {
        this.glBankTransactionId = glBankTransactionId;
    }

    public Integer getAccountDebitId() {
        return accountDebitId;
    }

    public void setAccountDebitId(Integer accountDebitId) {
        this.accountDebitId = accountDebitId;
    }

    public Integer getAccountCreditId() {
        return accountCreditId;
    }

    public void setAccountCreditId(Integer accountCreditId) {
        this.accountCreditId = accountCreditId;
    }

    public Integer getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    public Integer getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(Integer adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public Integer getAccNumberDebit() {
        return accNumberDebit;
    }

    public void setAccNumberDebit(Integer accNumberDebit) {
        this.accNumberDebit = accNumberDebit;
    }

    public Integer getAccNumberCredit() {
        return accNumberCredit;
    }

    public void setAccNumberCredit(Integer accNumberCredit) {
        this.accNumberCredit = accNumberCredit;
    }

    public String getAccNameDebit() {
        return accNameDebit;
    }

    public void setAccNameDebit(String accNameDebit) {
        this.accNameDebit = accNameDebit;
    }

    public String getAccNameCredit() {
        return accNameCredit;
    }

    public void setAccNameCredit(String accNameCredit) {
        this.accNameCredit = accNameCredit;
    }

    public BigDecimal getValue() {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValueLocal() {
        if (valueLocal == null) {
            valueLocal = BigDecimal.ZERO;
        }
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the moduleCode
     */
    public Integer getModuleCode() {
        return moduleCode;
    }

    /**
     * @param moduleCode the moduleCode to set
     */
    public void setModuleCode(Integer moduleCode) {
        this.moduleCode = moduleCode;
    }

    /**
     * @return the taxValue
     */
    public BigDecimal getTaxValue() {
        if (taxValue == null) {
            taxValue = BigDecimal.ZERO;
        }
        return taxValue;
    }

    /**
     * @param taxValue the taxValue to set
     */
    public void setTaxValue(BigDecimal taxValue) {

        this.taxValue = taxValue;
    }

    /**
     * @return the taxValueLocal
     */
    public BigDecimal getTaxValueLocal() {
        if (taxValueLocal == null) {
            taxValueLocal = BigDecimal.ZERO;
        }
        return taxValueLocal;
    }

    /**
     * @param taxValueLocal the taxValueLocal to set
     */
    public void setTaxValueLocal(BigDecimal taxValueLocal) {
        this.taxValueLocal = taxValueLocal;
    }

    /**
     * @return the secondValue
     */
    public BigDecimal getSecondValue() {
        if (secondValue == null) {
            secondValue = BigDecimal.ZERO;
        }
        return secondValue;
    }

    /**
     * @param secondValue the secondValue to set
     */
    public void setSecondValue(BigDecimal secondValue) {
        this.secondValue = secondValue;
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
     * @return the secondAccountCreditId
     */
    public Integer getSecondAccountCreditId() {
        return secondAccountCreditId;
    }

    /**
     * @param secondAccountCreditId the secondAccountCreditId to set
     */
    public void setSecondAccountCreditId(Integer secondAccountCreditId) {
        this.secondAccountCreditId = secondAccountCreditId;
    }

    /**
     * @return the secondAccNumberCredit
     */
    public Integer getSecondAccNumberCredit() {
        return secondAccNumberCredit;
    }

    /**
     * @param secondAccNumberCredit the secondAccNumberCredit to set
     */
    public void setSecondAccNumberCredit(Integer secondAccNumberCredit) {
        this.secondAccNumberCredit = secondAccNumberCredit;
    }

    /**
     * @return the secondAccNameCredit
     */
    public String getSecondAccNameCredit() {
        return secondAccNameCredit;
    }

    /**
     * @param secondAccNameCredit the secondAccNameCredit to set
     */
    public void setSecondAccNameCredit(String secondAccNameCredit) {
        this.secondAccNameCredit = secondAccNameCredit;
    }

    /**
     * @return the taxDiscValue
     */
    public BigDecimal getTaxDiscValue() {
        if (taxDiscValue == null) {
            taxDiscValue = BigDecimal.ZERO;
        }
        return taxDiscValue;
    }

    /**
     * @param taxDiscValue the taxDiscValue to set
     */
    public void setTaxDiscValue(BigDecimal taxDiscValue) {
        this.taxDiscValue = taxDiscValue;
    }

    /**
     * @return the taxDiscValueLocal
     */
    public BigDecimal getTaxDiscValueLocal() {
        if (taxDiscValueLocal == null) {
            taxDiscValueLocal = BigDecimal.ZERO;
        }
        return taxDiscValueLocal;
    }

    /**
     * @param taxDiscValueLocal the taxDiscValueLocal to set
     */
    public void setTaxDiscValueLocal(BigDecimal taxDiscValueLocal) {
        this.taxDiscValueLocal = taxDiscValueLocal;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the secondAccountDebitId
     */
    public Integer getSecondAccountDebitId() {
        return secondAccountDebitId;
    }

    /**
     * @param secondAccountDebitId the secondAccountDebitId to set
     */
    public void setSecondAccountDebitId(Integer secondAccountDebitId) {
        this.secondAccountDebitId = secondAccountDebitId;
    }

    /**
     * @return the thirdAccountCreditId
     */
    public Integer getThirdAccountCreditId() {
        return thirdAccountCreditId;
    }

    /**
     * @param thirdAccountCreditId the thirdAccountCreditId to set
     */
    public void setThirdAccountCreditId(Integer thirdAccountCreditId) {
        this.thirdAccountCreditId = thirdAccountCreditId;
    }

    /**
     * @return the thirdAccNameCredit
     */
    public String getThirdAccNameCredit() {
        return thirdAccNameCredit;
    }

    /**
     * @param thirdAccNameCredit the thirdAccNameCredit to set
     */
    public void setThirdAccNameCredit(String thirdAccNameCredit) {
        this.thirdAccNameCredit = thirdAccNameCredit;
    }

    /**
     * @return the thirdAccNumberCredit
     */
    public Integer getThirdAccNumberCredit() {
        return thirdAccNumberCredit;
    }

    /**
     * @param thirdAccNumberCredit the thirdAccNumberCredit to set
     */
    public void setThirdAccNumberCredit(Integer thirdAccNumberCredit) {
        this.thirdAccNumberCredit = thirdAccNumberCredit;
    }

    /**
     * @return the secondAccNameDebit
     */
    public String getSecondAccNameDebit() {
        return secondAccNameDebit;
    }

    /**
     * @param secondAccNameDebit the secondAccNameDebit to set
     */
    public void setSecondAccNameDebit(String secondAccNameDebit) {
        this.secondAccNameDebit = secondAccNameDebit;
    }

    /**
     * @return the secondAccNumberDebit
     */
    public Integer getSecondAccNumberDebit() {
        return secondAccNumberDebit;
    }

    /**
     * @param secondAccNumberDebit the secondAccNumberDebit to set
     */
    public void setSecondAccNumberDebit(Integer secondAccNumberDebit) {
        this.secondAccNumberDebit = secondAccNumberDebit;
    }

    /**
     * @return the thirdAccountDebitId
     */
    public Integer getThirdAccountDebitId() {
        return thirdAccountDebitId;
    }

    /**
     * @param thirdAccountDebitId the thirdAccountDebitId to set
     */
    public void setThirdAccountDebitId(Integer thirdAccountDebitId) {
        this.thirdAccountDebitId = thirdAccountDebitId;
    }

    /**
     * @return the thirdAccNameDebit
     */
    public String getThirdAccNameDebit() {
        return thirdAccNameDebit;
    }

    /**
     * @param thirdAccNameDebit the thirdAccNameDebit to set
     */
    public void setThirdAccNameDebit(String thirdAccNameDebit) {
        this.thirdAccNameDebit = thirdAccNameDebit;
    }

    /**
     * @return the thirdAccNumberDebit
     */
    public Integer getThirdAccNumberDebit() {
        return thirdAccNumberDebit;
    }

    /**
     * @param thirdAccNumberDebit the thirdAccNumberDebit to set
     */
    public void setThirdAccNumberDebit(Integer thirdAccNumberDebit) {
        this.thirdAccNumberDebit = thirdAccNumberDebit;
    }

}
