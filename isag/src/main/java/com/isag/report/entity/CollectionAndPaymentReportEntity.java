/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import com.isag.entity.InvInventory;
import com.isag.entity.InvPurchaseInvoiceDetail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hq002
 */
public class CollectionAndPaymentReportEntity {

    private Date dueDate;
    private Date paymentDate;
    private String phone;
    private String orgName;
    private Integer orgId;
    private Integer progId;
    private String progName;
    private BigDecimal value;
    private String status;
    private BigDecimal dueamount = BigDecimal.ZERO;
    private BigDecimal remain = BigDecimal.ZERO;
    private BigDecimal payee = BigDecimal.ZERO;
    private Integer serial;
    private BigDecimal totalValue = BigDecimal.ZERO;
    private BigDecimal installmentValue = BigDecimal.ZERO;

    private Collection<GLBankEntity> GLBankEntityList = new ArrayList<>();

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
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the orgName
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * @return the dueamount
     */
    public BigDecimal getDueamount() {
        return dueamount;
    }

    /**
     * @param dueamount the dueamount to set
     */
    public void setDueamount(BigDecimal dueamount) {
        this.dueamount = dueamount;
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the payee
     */
    public BigDecimal getPayee() {
        return payee;
    }

    /**
     * @param payee the payee to set
     */
    public void setPayee(BigDecimal payee) {
        this.payee = payee;
    }

    /**
     * @return the paymentDate
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the GLBankEntityList
     */
    public Collection<GLBankEntity> getGLBankEntityList() {
        return GLBankEntityList;
    }

    /**
     * @param GLBankEntityList the GLBankEntityList to set
     */
    public void setGLBankEntityList(Collection<GLBankEntity> GLBankEntityList) {
        this.GLBankEntityList = GLBankEntityList;
    }

    /**
     * @return the progId
     */
    public Integer getProgId() {
        return progId;
    }

    /**
     * @param progId the progId to set
     */
    public void setProgId(Integer progId) {
        this.progId = progId;
    }

    /**
     * @return the progName
     */
    public String getProgName() {
        return progName;
    }

    /**
     * @param progName the progName to set
     */
    public void setProgName(String progName) {
        this.progName = progName;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the installmentValue
     */
    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    /**
     * @param installmentValue the installmentValue to set
     */
    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
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
     * @return the totalValue
     */
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the orgId
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * @param orgId the orgId to set
     */
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
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

}
