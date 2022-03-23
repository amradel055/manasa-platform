/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
 * @author AhmedEssam
 */
@Entity
@Table(name ="collectionpaymenttotals_temp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CollectionPaymentTotalsTemp.findAll", query = "SELECT t FROM CollectionPaymentTotalsTemp t")})
public class CollectionPaymentTotalsTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    public CollectionPaymentTotalsTemp() {
    }
    
    @Id 
    /* @Column(name = "rowNum")
    private Integer rowNum;*/
    @Column(name = "due_date_id")
    private Integer dueDateId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "instSerial")
    private Integer instSerial;
    @Size(max = 45)
    @Column(name = "customer_id")
    private Integer customerId;
    @Size(max = 45)
    @Column(name = "customer_name")
    private String customerName;
    @Size(max = 45)
    @Column(name = "customer_phone")
    private String customerPhone;
    @Column(name = "project_id")
    private Integer projectId;
    @Column(name = "projectName")
    private String projectName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "due_value")
    private BigDecimal dueValue;
    @Column(name = "status_text")
    private String statusText;
    @Column(name = "sum_dueValue")
    private BigDecimal sumDueValue;
    @Column(name = "user_Id")
    private Integer userId;
 
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "gl_bank_transaction_detail_temp",
            joinColumns =  {
                @JoinColumn(name = "installment_id"),
                @JoinColumn(name = "user_Id", referencedColumnName = "user_Id" ) 
            }
    ) 
    private List<GlBankTransactionDetailTemp> glBankTransaction;

    @Column(name = "pay")
    private BigDecimal pay;
    @Column(name = "remain")
    private BigDecimal remain;
    @Column(name = "payDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public BigDecimal getDueValue() {
        return dueValue;
    }

    public void setDueValue(BigDecimal dueValue) {
        this.dueValue = dueValue;
    }

    /**
     * @return the dueDateId
     */
    public Integer getDueDateId() {
        return dueDateId;
    }

    /**
     * @param dueDateId the dueDateId to set
     */
    public void setDueDateId(Integer dueDateId) {
        this.dueDateId = dueDateId;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
     * @return the instSerial
     */
    public Integer getInstSerial() {
        return instSerial;
    }

    /**
     * @param instSerial the instSerial to set
     */
    public void setInstSerial(Integer instSerial) {
        this.instSerial = instSerial;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the sumDueValue
     */
    public BigDecimal getSumDueValue() {
        return sumDueValue;
    }

    /**
     * @param sumDueValue the sumDueValue to set
     */
    public void setSumDueValue(BigDecimal sumDueValue) {
        this.sumDueValue = sumDueValue;
    }

    /**
     * @return the statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * @param statusText the statusText to set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    /**
     * @return the glBankTransaction
     */
    public List<GlBankTransactionDetailTemp> getGlBankTransaction() {
        return glBankTransaction;
    }

    /**
     * @param glBankTransaction the glBankTransaction to set
     */
    public void setGlBankTransaction(List<GlBankTransactionDetailTemp> glBankTransaction) {
        this.glBankTransaction = glBankTransaction;
    }

    /**
     * @return the pay
     */
    public BigDecimal getPay() {
        return pay;
    }

    /**
     * @param pay the pay to set
     */
    public void setPay(BigDecimal pay) {
        this.pay = pay;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dueDateId != null ? dueDateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CollectionPaymentTotalsTemp)) {
            return false;
        }
        CollectionPaymentTotalsTemp other = (CollectionPaymentTotalsTemp) object;
        if ((this.dueDateId == null && other.dueDateId != null) || (this.dueDateId != null && !this.dueDateId.equals(other.dueDateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return dueDateId + " ";
    }

    /**
     * @return the payDate
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * @param payDate the payDate to set
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

   

}
