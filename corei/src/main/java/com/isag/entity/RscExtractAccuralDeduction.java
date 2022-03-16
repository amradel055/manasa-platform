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
 * @author ahmed
 */
@Entity
@Table(name = "rsc_extract_accural_deduction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscExtractAccuralDeduction.findAll", query = "SELECT r FROM RscExtractAccuralDeduction r")
    , @NamedQuery(name = "RscExtractAccuralDeduction.findById", query = "SELECT r FROM RscExtractAccuralDeduction r WHERE r.id = :id")
    , @NamedQuery(name = "RscExtractAccuralDeduction.findByCreationDate", query = "SELECT r FROM RscExtractAccuralDeduction r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscExtractAccuralDeduction.findByModificationDate", query = "SELECT r FROM RscExtractAccuralDeduction r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscExtractAccuralDeduction.findByRscAccuralDeductionValue", query = "SELECT r FROM RscExtractAccuralDeduction r WHERE r.rscAccuralDeductionValue = :rscAccuralDeductionValue")
    , @NamedQuery(name = "RscExtractAccuralDeduction.findByRscAccuralDeductionPercentage", query = "SELECT r FROM RscExtractAccuralDeduction r WHERE r.rscAccuralDeductionPercentage = :rscAccuralDeductionPercentage")
    , @NamedQuery(name = "RscExtractAccuralDeduction.findBySerial", query = "SELECT r FROM RscExtractAccuralDeduction r WHERE r.serial = :serial")})
public class RscExtractAccuralDeduction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "rsc_accural_deduction_value")
    private BigDecimal rscAccuralDeductionValue;
    @Column(name = "rsc_accural_deduction_percentage")
    private BigDecimal rscAccuralDeductionPercentage;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "rsc_extract_register_id", referencedColumnName = "id")
    @ManyToOne
    private RscExtractRegister rscExtractRegisterId;    
    @JoinColumn(name = "rsc_accrual_deduction_id", referencedColumnName = "id")
    @ManyToOne
    private RscAccrualDeduction rscAccrualDeductionId;
    
    public RscExtractAccuralDeduction() {
    }

    public RscExtractAccuralDeduction(Integer id) {
        this.id = id;
    }

    public BigDecimal getRscAccuralDeductionValue() {
        return rscAccuralDeductionValue;
    }

    public void setRscAccuralDeductionValue(BigDecimal rscAccuralDeductionValue) {
        this.rscAccuralDeductionValue = rscAccuralDeductionValue;
    }

    public BigDecimal getRscAccuralDeductionPercentage() {
        return rscAccuralDeductionPercentage;
    }

    public void setRscAccuralDeductionPercentage(BigDecimal rscAccuralDeductionPercentage) {
        this.rscAccuralDeductionPercentage = rscAccuralDeductionPercentage;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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
        if (!(object instanceof RscExtractAccuralDeduction)) {
            return false;
        }
        RscExtractAccuralDeduction other = (RscExtractAccuralDeduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscExtractAccuralDeduction[ id=" + id + " ]";
    }

    /**
     * @return the rscExtractRegisterId
     */
    public RscExtractRegister getRscExtractRegisterId() {
        return rscExtractRegisterId;
    }

    /**
     * @param rscExtractRegisterId the rscExtractRegisterId to set
     */
    public void setRscExtractRegisterId(RscExtractRegister rscExtractRegisterId) {
        this.rscExtractRegisterId = rscExtractRegisterId;
    }

    /**
     * @return the rscAccrualDeductionId
     */
    public RscAccrualDeduction getRscAccrualDeductionId() {
        return rscAccrualDeductionId;
    }

    /**
     * @param rscAccrualDeductionId the rscAccrualDeductionId to set
     */
    public void setRscAccrualDeductionId(RscAccrualDeduction rscAccrualDeductionId) {
        this.rscAccrualDeductionId = rscAccrualDeductionId;
    }
    
    

}
