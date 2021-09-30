/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ahmed
 */
public class InstContractDetailsEntity {

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    private Integer id;
    private Integer serial;
    private Integer status;
    private Date dueDate;
    private BigDecimal value;
    private BigDecimal payment;
    private Integer branchId;
    private Date creationDate;
    private Date modificationDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer companyId;
    private Integer index;
    private Boolean markEdit;
    private String remark;
    private BigDecimal payedValue;
    private BigDecimal balance;
    private Date payedDate;
    private String payedAccountName;

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
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return the createdBy
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the modifiedBy
     */
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    /**
     * @return the payment
     */
    public BigDecimal getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(BigDecimal payment) {
        this.payment = payment;
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
     * @return the payedValue
     */
    public BigDecimal getPayedValue() {
        if (payedValue == null) {
            payedValue = BigDecimal.ZERO;
        }
        return payedValue;
    }

    /**
     * @param payedValue the payedValue to set
     */
    public void setPayedValue(BigDecimal payedValue) {
        this.payedValue = payedValue;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the payedDate
     */
    public Date getPayedDate() {
        return payedDate;
    }

    /**
     * @param payedDate the payedDate to set
     */
    public void setPayedDate(Date payedDate) {
        this.payedDate = payedDate;
    }

    /**
     * @return the payedAccountName
     */
    public String getPayedAccountName() {
        return payedAccountName;
    }

    /**
     * @param payedAccountName the payedAccountName to set
     */
    public void setPayedAccountName(String payedAccountName) {
        this.payedAccountName = payedAccountName;
    }
}
