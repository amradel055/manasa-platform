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
@Table(name = "inst_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstView.findAll", query = "SELECT c FROM InstView c"),
    @NamedQuery(name = "InstView.findByBranchId", query = "SELECT c FROM InstView c WHERE c.branchId = :branchId"),
    @NamedQuery(name = "InstView.findByOrganizationSiteId", query = "SELECT c FROM InstView c WHERE c.organizationSiteId = :organizationSiteId"),
    @NamedQuery(name = "InstView.findByTotalValue", query = "SELECT c FROM InstView c WHERE c.totalValue = :totalValue"),
    @NamedQuery(name = "InstView.findBySerial", query = "SELECT c FROM InstView c WHERE c.serial = :serial"),
    @NamedQuery(name = "InstView.findByDueDate", query = "SELECT c FROM InstView c WHERE c.dueDate = :dueDate"),
    @NamedQuery(name = "InstView.findByStatus", query = "SELECT c FROM InstView c WHERE c.status = :status"),
    @NamedQuery(name = "InstView.findByCustomerId", query = "SELECT c FROM InstView c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "InstView.findByCustomerCode", query = "SELECT c FROM InstView c WHERE c.customerCode = :customerCode"),
    @NamedQuery(name = "InstView.findByCustomerName", query = "SELECT c FROM InstView c WHERE c.customerName = :customerName"),
    @NamedQuery(name = "InstView.findByInstSerial", query = "SELECT c FROM InstView c WHERE c.instSerial = :instSerial"),
    @NamedQuery(name = "InstView.findByProjectId", query = "SELECT c FROM InstView c WHERE c.projectId = :projectId"),
    @NamedQuery(name = "InstView.findByProjectName", query = "SELECT c FROM InstView c WHERE c.projectName = :projectName"),

    @NamedQuery(name = "InstView.findByValue", query = "SELECT c FROM InstView c WHERE c.value = :value")})
public class InstView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "organizationSiteId")
    private Integer organizationSiteId;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "totalValue")
    private BigDecimal totalValue;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "instSerial")
    private Integer instSerial;
    @Column(name = "dueDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "customerId")
    private Integer customerId;
    @Column(name = "projectId")
    private Integer projectId;
    @Size(max = 45)
    @Column(name = "customerCode")
    private String customerCode;
    @Size(max = 45)
    @Column(name = "customerName")
    private String customerName;
    @Size(max = 45)
    @Column(name = "projectName")
    private String projectName;
    @Column(name = "value")
    private BigDecimal value;

    public InstView() {
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
     * @return the organizationSiteId
     */
    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
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

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * @return the projectId
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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
