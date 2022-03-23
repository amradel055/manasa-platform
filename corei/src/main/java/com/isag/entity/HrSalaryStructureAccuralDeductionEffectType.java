/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
 * @author AhmedEssam
 */
@Entity
@Table(name = "hr_salary_structure_accural_deduction_effect_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrSalaryStructureAccuralDeductionEffectType.findAll", query = "SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h")
    , @NamedQuery(name = "HrSalaryStructureAccuralDeductionEffectType.findById", query = "SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.id = :id")
    , @NamedQuery(name = "HrSalaryStructureAccuralDeductionEffectType.findByActive", query = "SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.active = :active")
    , @NamedQuery(name = "HrSalaryStructureAccuralDeductionEffectType.findByCreationDate", query = "SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.creationDate = :creationDate")
    , @NamedQuery(name = "HrSalaryStructureAccuralDeductionEffectType.findByModificationDate", query = "SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.modificationDate = :modificationDate")
    , @NamedQuery(name = "HrSalaryStructureAccuralDeductionEffectType.findBySerial", query = "SELECT h FROM HrSalaryStructureAccuralDeductionEffectType h WHERE h.serial = :serial")})
public class HrSalaryStructureAccuralDeductionEffectType extends BaseEntity {

    @Column(name = "Active")
    private Boolean active;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "hr_salary_structure_accural_deduction", referencedColumnName = "id")
    @ManyToOne
    private HrSalaryStructureAccuralDeduction hrSalaryStructureAccuralDeduction;
    @JoinColumn(name = "hr_salary_effect_type", referencedColumnName = "id")
    @ManyToOne
    private HrSalaryEffectType hrSalaryEffectType;
    @JoinColumn(name = "hr_salary_effect_id", referencedColumnName = "id")
    @ManyToOne
    private HrSalaryEffect hrSalaryEffect;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public HrSalaryStructureAccuralDeductionEffectType() {
    }

    public HrSalaryStructureAccuralDeductionEffectType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public HrSalaryStructureAccuralDeduction getHrSalaryStructureAccuralDeduction() {
        return hrSalaryStructureAccuralDeduction;
    }

    public void setHrSalaryStructureAccuralDeduction(HrSalaryStructureAccuralDeduction hrSalaryStructureAccuralDeduction) {
        this.hrSalaryStructureAccuralDeduction = hrSalaryStructureAccuralDeduction;
    }

    public HrSalaryEffectType getHrSalaryEffectType() {
        return hrSalaryEffectType;
    }

    public void setHrSalaryEffectType(HrSalaryEffectType hrSalaryEffectType) {
        this.hrSalaryEffectType = hrSalaryEffectType;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof HrSalaryStructureAccuralDeductionEffectType)) {
            return false;
        }
        HrSalaryStructureAccuralDeductionEffectType other = (HrSalaryStructureAccuralDeductionEffectType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.HrSalaryStructureAccuralDeductionEffectType[ id=" + id + " ]";
    }

    /**
     * @return the hrSalaryEffect
     */
    public HrSalaryEffect getHrSalaryEffect() {
        return hrSalaryEffect;
    }

    /**
     * @param hrSalaryEffect the hrSalaryEffect to set
     */
    public void setHrSalaryEffect(HrSalaryEffect hrSalaryEffect) {
        this.hrSalaryEffect = hrSalaryEffect;
    }
    
}
