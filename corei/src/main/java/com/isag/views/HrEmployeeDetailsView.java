/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Entity
@Table(name = "hr_employee_details_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrEmployeeDetailsView.findAll", query = "SELECT h FROM HrEmployeeDetailsView h"),
    @NamedQuery(name = "HrEmployeeDetailsView.findByEmployeeId", query = "SELECT h FROM HrEmployeeDetailsView h WHERE h.employeeId = :employeeId"),
    @NamedQuery(name = "HrEmployeeDetailsView.findByDesignation", query = "SELECT h FROM HrEmployeeDetailsView h WHERE h.designation = :designation"),
    @NamedQuery(name = "HrEmployeeDetailsView.findByName", query = "SELECT h FROM HrEmployeeDetailsView h WHERE h.name = :name"),
    @NamedQuery(name = "HrEmployeeDetailsView.findByMonths", query = "SELECT h FROM HrEmployeeDetailsView h WHERE h.months = :months"),
    @NamedQuery(name = "HrEmployeeDetailsView.findByNetSalary", query = "SELECT h FROM HrEmployeeDetailsView h WHERE h.netSalary = :netSalary"),
    @NamedQuery(name = "HrEmployeeDetailsView.findByTotalAccural", query = "SELECT h FROM HrEmployeeDetailsView h WHERE h.totalAccural = :totalAccural"),
    @NamedQuery(name = "HrEmployeeDetailsView.findByTotalDeduction", query = "SELECT h FROM HrEmployeeDetailsView h WHERE h.totalDeduction = :totalDeduction")})
public class HrEmployeeDetailsView implements Serializable {

    @Column(name = "branch_id")
    private Integer branchId;

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "employee_id")
    private Integer employeeId;
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "months")
    private Integer months;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "netSalary")
    private BigDecimal netSalary;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalAccural")
    private int totalAccural;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalDeduction")
    private int totalDeduction;

    public HrEmployeeDetailsView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
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

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(BigDecimal netSalary) {
        this.netSalary = netSalary;
    }

    public int getTotalAccural() {
        return totalAccural;
    }

    public void setTotalAccural(int totalAccural) {
        this.totalAccural = totalAccural;
    }

    public int getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(int totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

}
