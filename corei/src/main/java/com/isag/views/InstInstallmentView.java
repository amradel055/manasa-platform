/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "inst_installment_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstInstallmentView.findAll", query = "SELECT i FROM InstInstallmentView i"),
    @NamedQuery(name = "InstInstallmentView.findByRowNum", query = "SELECT i FROM InstInstallmentView i WHERE i.rowNum = :rowNum"),
    @NamedQuery(name = "InstInstallmentView.findByOrganizationSiteId", query = "SELECT i FROM InstInstallmentView i WHERE i.organizationSiteId = :organizationSiteId"),
    @NamedQuery(name = "InstInstallmentView.findByTotalValue", query = "SELECT i FROM InstInstallmentView i WHERE i.totalValue = :totalValue"),
    @NamedQuery(name = "InstInstallmentView.findByBranchId", query = "SELECT i FROM InstInstallmentView i WHERE i.branchId = :branchId"),
    @NamedQuery(name = "InstInstallmentView.findBySerial", query = "SELECT i FROM InstInstallmentView i WHERE i.serial = :serial"),
    @NamedQuery(name = "InstInstallmentView.findByProjectId", query = "SELECT i FROM InstInstallmentView i WHERE i.projectId = :projectId"),
    @NamedQuery(name = "InstInstallmentView.findByProjectName", query = "SELECT i FROM InstInstallmentView i WHERE i.projectName = :projectName"),
    @NamedQuery(name = "InstInstallmentView.findByItemId", query = "SELECT i FROM InstInstallmentView i WHERE i.itemId = :itemId"),
    @NamedQuery(name = "InstInstallmentView.findByItemName", query = "SELECT i FROM InstInstallmentView i WHERE i.itemName = :itemName"),
    @NamedQuery(name = "InstInstallmentView.findByDate", query = "SELECT i FROM InstInstallmentView i WHERE i.date = :date"),
    @NamedQuery(name = "InstInstallmentView.findByStartInstallment", query = "SELECT i FROM InstInstallmentView i WHERE i.startInstallment = :startInstallment"),
    @NamedQuery(name = "InstInstallmentView.findByInstallmentNumber", query = "SELECT i FROM InstInstallmentView i WHERE i.installmentNumber = :installmentNumber"),
    @NamedQuery(name = "InstInstallmentView.findByAdvancePayment", query = "SELECT i FROM InstInstallmentView i WHERE i.advancePayment = :advancePayment"),
    @NamedQuery(name = "InstInstallmentView.findByInstallmentValue", query = "SELECT i FROM InstInstallmentView i WHERE i.installmentValue = :installmentValue"),
    @NamedQuery(name = "InstInstallmentView.findByCustomerId", query = "SELECT i FROM InstInstallmentView i WHERE i.customerId = :customerId"),
    @NamedQuery(name = "InstInstallmentView.findByCustomerCode", query = "SELECT i FROM InstInstallmentView i WHERE i.customerCode = :customerCode"),
    @NamedQuery(name = "InstInstallmentView.findByCustomerName", query = "SELECT i FROM InstInstallmentView i WHERE i.customerName = :customerName")})
public class InstInstallmentView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "id")
    private Integer id;
    @Column(name = "organizationSiteId")
    private Integer organizationSiteId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalValue")
    private BigDecimal totalValue;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "projectId")
    private Integer projectId;
    @Size(max = 450)
    @Column(name = "projectName")
    private String projectName;
    @Column(name = "itemId")
    private Integer itemId;
    @Size(max = 450)
    @Column(name = "itemName")
    private String itemName;
    @Size(max = 26)
    @Column(name = "date")
    private String date;
    @Size(max = 26)
    @Column(name = "startInstallment")
    private String startInstallment;
    @Column(name = "installmentNumber")
    private Integer installmentNumber;
    @Column(name = "advancePayment")
    private BigDecimal advancePayment;
    @Column(name = "installmentValue")
    private BigDecimal installmentValue;
    @Column(name = "customerId")
    private Integer customerId;
    @Size(max = 45)
    @Column(name = "customerCode")
    private String customerCode;
    @Size(max = 45)
    @Column(name = "customerName")
    private String customerName;
    @Transient
    private Boolean disabled;

    public InstInstallmentView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartInstallment() {
        return startInstallment;
    }

    public void setStartInstallment(String startInstallment) {
        this.startInstallment = startInstallment;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public BigDecimal getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = advancePayment;
    }

    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
