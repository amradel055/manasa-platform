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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Entity
@Table(name = "employee_details_select_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmployeeDetailsSelectView.findAll", query = "SELECT e FROM EmployeeDetailsSelectView e"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findByEmployeeId", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.employeeId = :employeeId"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findByDesignation", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.designation = :designation"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findByBranchId", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.branchId = :branchId"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findByName", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.name = :name"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findByMonths", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.months = :months"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findBySumnetSalary", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.netSalary = :netSalary"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findBySumtotalAccural", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.totalAccural = :totalAccural"),
    @NamedQuery(name = "EmployeeDetailsSelectView.findBySumtotalDeduction", query = "SELECT e FROM EmployeeDetailsSelectView e WHERE e.totalDeduction = :totalDeduction")})
public class EmployeeDetailsSelectView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "id")
    private Integer Id;
    @Column(name = "employee_id")
    private Integer employeeId;
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    @Column(name = "branch_id")
    private Integer branchId;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "months")
    private Integer months;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "netSalary")
    private BigDecimal netSalary;
    @Column(name = "totalAccural")
    private BigDecimal totalAccural;
    @Column(name = "totalDeduction")
    private BigDecimal totalDeduction;

    public EmployeeDetailsSelectView() {
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        this.months = months;
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
     * @return the netSalary
     */
    public BigDecimal getNetSalary() {
        return netSalary;
    }

    /**
     * @param netSalary the netSalary to set
     */
    public void setNetSalary(BigDecimal netSalary) {
        this.netSalary = netSalary;
    }

    /**
     * @return the totalAccural
     */
    public BigDecimal getTotalAccural() {
        return totalAccural;
    }

    /**
     * @param totalAccural the totalAccural to set
     */
    public void setTotalAccural(BigDecimal totalAccural) {
        this.totalAccural = totalAccural;
    }

    /**
     * @return the totalDeduction
     */
    public BigDecimal getTotalDeduction() {
        return totalDeduction;
    }

    /**
     * @param totalDeduction the totalDeduction to set
     */
    public void setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    /**
     * @return the Id
     */
    public Integer getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(Integer Id) {
        this.Id = Id;
    }

}
