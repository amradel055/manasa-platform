/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "inst_transaction_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstTransactionView.findAll", query = "SELECT c FROM InstTransactionView c"),
    @NamedQuery(name = "InstTransactionView.findByInstDetailId", query = "SELECT c FROM InstTransactionView c WHERE c.instDetailId = :instDetailId"),
    @NamedQuery(name = "InstTransactionView.findByDueDate", query = "SELECT c FROM InstTransactionView c WHERE c.dueDate = :dueDate"),
    @NamedQuery(name = "InstTransactionView.findByInstContractId", query = "SELECT c FROM InstTransactionView c WHERE c.instContractId = :instContractId"),
    @NamedQuery(name = "InstTransactionView.findBySerial", query = "SELECT c FROM InstTransactionView c WHERE c.serial = :serial"),
    @NamedQuery(name = "InstTransactionView.findByInstallmentValue", query = "SELECT c FROM InstTransactionView c WHERE c.installmentValue = :installmentValue"),
    @NamedQuery(name = "InstTransactionView.findByPaidValue", query = "SELECT c FROM InstTransactionView c WHERE c.paidValue = :paidValue"),
    @NamedQuery(name = "InstTransactionView.findByRemaining", query = "SELECT c FROM InstTransactionView c WHERE c.remaining = :remaining")})

public class InstTransactionView implements Serializable{

    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "instDetailId")
    private Integer instDetailId;
    @Column(name = "instContractId")
    private Integer instContractId;
    @Column(name = "dueDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "installmentValue")
    private BigDecimal installmentValue;
    @Column(name = "paidValue")
    private BigDecimal paidValue;
    @Column(name = "remaining")
    private BigDecimal remaining;
    
    public InstTransactionView() {
    }
    
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
     * @return the instDetailId
     */
    public Integer getInstDetailId() {
        return instDetailId;
    }

    /**
     * @param instDetailId the instDetailId to set
     */
    public void setInstDetailId(Integer instDetailId) {
        this.instDetailId = instDetailId;
    }

    /**
     * @return the instContractId
     */
    public Integer getInstContractId() {
        return instContractId;
    }

    /**
     * @param instContractId the instContractId to set
     */
    public void setInstContractId(Integer instContractId) {
        this.instContractId = instContractId;
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
     * @return the paidValue
     */
    public BigDecimal getPaidValue() {
        return paidValue;
    }

    /**
     * @param paidValue the paidValue to set
     */
    public void setPaidValue(BigDecimal paidValue) {
        this.paidValue = paidValue;
    }

    /**
     * @return the remaining
     */
    public BigDecimal getRemaining() {
        return remaining;
    }

    /**
     * @param remaining the remaining to set
     */
    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
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
    
    @Override
    public String toString() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(dueDate);
        return date + " " + remaining;
    }
}
