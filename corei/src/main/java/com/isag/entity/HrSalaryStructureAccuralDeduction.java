/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Entity
@Table(name = "hr_salary_structure_accural_deduction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrSalaryStructureAccuralDeduction.findAll", query = "SELECT h FROM HrSalaryStructureAccuralDeduction h"),
    @NamedQuery(name = "HrSalaryStructureAccuralDeduction.findById", query = "SELECT h FROM HrSalaryStructureAccuralDeduction h WHERE h.id = :id"),
    @NamedQuery(name = "HrSalaryStructureAccuralDeduction.findByCreationDate", query = "SELECT h FROM HrSalaryStructureAccuralDeduction h WHERE h.creationDate = :creationDate"),
    @NamedQuery(name = "HrSalaryStructureAccuralDeduction.findByModificationDate", query = "SELECT h FROM HrSalaryStructureAccuralDeduction h WHERE h.modificationDate = :modificationDate"),
    @NamedQuery(name = "HrSalaryStructureAccuralDeduction.findByValue", query = "SELECT h FROM HrSalaryStructureAccuralDeduction h WHERE h.value = :value")})
public class HrSalaryStructureAccuralDeduction extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value; 
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId; 
    @JoinColumn(name = "accural_deduction_id", referencedColumnName = "id")
    @ManyToOne
    private RscAccrualDeduction accuralDeductionId;
    @JoinColumn(name = "salary_structure_id", referencedColumnName = "id")
    @ManyToOne
    private HrSalaryStructure salaryStructureId;

    public HrSalaryStructureAccuralDeduction() {
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HrSalaryStructureAccuralDeduction)) {
            return false;
        }
        HrSalaryStructureAccuralDeduction other = (HrSalaryStructureAccuralDeduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.HrEmployeeAccuralDeduction[ id=" + id + " ]";
    }

    /**
     * @return the accuralDeductionId
     */
    public RscAccrualDeduction getAccuralDeductionId() {
        return accuralDeductionId;
    }

    /**
     * @param accuralDeductionId the accuralDeductionId to set
     */
    public void setAccuralDeductionId(RscAccrualDeduction accuralDeductionId) {
        this.accuralDeductionId = accuralDeductionId;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }
 
    /**
     * @return the salaryStructureId
     */
    public HrSalaryStructure getSalaryStructureId() {
        return salaryStructureId;
    }

    /**
     * @param salaryStructureId the salaryStructureId to set
     */
    public void setSalaryStructureId(HrSalaryStructure salaryStructureId) {
        this.salaryStructureId = salaryStructureId;
    }

}
