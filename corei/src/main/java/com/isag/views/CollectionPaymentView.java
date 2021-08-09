/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import com.isag.entity.GlBankTransaction;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
 * @author amr
 */
@Entity
@Table(name = "collection_payment_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CollectionPaymentView.findAll", query = "SELECT c FROM CollectionPaymentView c")
    , @NamedQuery(name = "CollectionPaymentView.findByDueDate", query = "SELECT c FROM CollectionPaymentView c WHERE c.dueDate = :dueDate")
    , @NamedQuery(name = "CollectionPaymentView.findByCodeCustomer", query = "SELECT c FROM CollectionPaymentView c WHERE c.codeCustomer = :codeCustomer")
    , @NamedQuery(name = "CollectionPaymentView.findByCustomerName", query = "SELECT c FROM CollectionPaymentView c WHERE c.customerName = :customerName")
    , @NamedQuery(name = "CollectionPaymentView.findByCustomerPhone", query = "SELECT c FROM CollectionPaymentView c WHERE c.customerPhone = :customerPhone")
    , @NamedQuery(name = "CollectionPaymentView.findByProjectId", query = "SELECT c FROM CollectionPaymentView c WHERE c.projectId = :projectId")
    , @NamedQuery(name = "CollectionPaymentView.findByDueValue", query = "SELECT c FROM CollectionPaymentView c WHERE c.dueValue = :dueValue")
    , @NamedQuery(name = "CollectionPaymentView.findByBranchId", query = "SELECT c FROM CollectionPaymentView c WHERE c.branchId = :branchId")
    , @NamedQuery(name = "CollectionPaymentView.findByStatus", query = "SELECT c FROM CollectionPaymentView c WHERE c.status = :status")})
public class CollectionPaymentView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
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
    @Column(name = "code_customer")
    private String codeCustomer;
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
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "status")
    private Integer status;
    @Transient
    private List<GlBankTransaction> glBankTransacionList;

    public CollectionPaymentView() {
    }
    
    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCodeCustomer() {
        return codeCustomer;
    }

    public void setCodeCustomer(String codeCustomer) {
        this.codeCustomer = codeCustomer;
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
     * @return the glBankTransacionList
     */
    public List<GlBankTransaction> getGlBankTransacionList() {
        return glBankTransacionList;
    }

    /**
     * @param glBankTransacionList the glBankTransacionList to set
     */
    public void setGlBankTransacionList(List<GlBankTransaction> glBankTransacionList) {
        this.glBankTransacionList = glBankTransacionList;
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
    
}
