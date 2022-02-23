/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class HrSalaryStructureAccuralDeductionDTO extends BaseEntityDTO {

    private BigDecimal value; 
    private Boolean salaryText;
    private RscAccrualDeductionDTO accuralDeductionId;
    private HrSalaryStructureDTO salaryStructureId;
    private List<HrSalaryStructureAccuralDeductionEffectTypeDTO> hrSalaryStructureAccuralEffectType;
    private List<HrSalaryStructureAccuralDeductionEffectTypeDTO> hrSalaryStructureDeductionEffectType;
    private List<HrSalaryStructureAccuralDeductionEffectTypeDTO> hrSalaryStructureAccuralDeductionEffectTypeDelete;

    public HrSalaryStructureAccuralDeductionDTO() {
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
        if (!(object instanceof HrSalaryStructureAccuralDeductionDTO)) {
            return false;
        }
        HrSalaryStructureAccuralDeductionDTO other = (HrSalaryStructureAccuralDeductionDTO) object;
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
    public RscAccrualDeductionDTO getAccuralDeductionId() {
        if (accuralDeductionId == null) {
            accuralDeductionId = new RscAccrualDeductionDTO();
        }
        return accuralDeductionId;
    }

    /**
     * @param accuralDeductionId the accuralDeductionId to set
     */
    public void setAccuralDeductionId(RscAccrualDeductionDTO accuralDeductionId) {
        this.accuralDeductionId = accuralDeductionId;
    } 

    /**
     * @return the salaryText
     */
    public Boolean getSalaryText() {
        return salaryText;
    }

    /**
     * @param salaryText the salaryText to set
     */
    public void setSalaryText(Boolean salaryText) {
        this.salaryText = salaryText;
    }

    /**
     * @return the salaryStructureId
     */
    public HrSalaryStructureDTO getSalaryStructureId() {
        return salaryStructureId;
    }

    /**
     * @param salaryStructureId the salaryStructureId to set
     */
    public void setSalaryStructureId(HrSalaryStructureDTO salaryStructureId) {
        this.salaryStructureId = salaryStructureId;
    }

    /**
     * @return the hrSalaryStructureAccuralDeductionEffectTypeDelete
     */
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> getHrSalaryStructureAccuralDeductionEffectTypeDelete() {
        if (hrSalaryStructureAccuralDeductionEffectTypeDelete == null) {
            hrSalaryStructureAccuralDeductionEffectTypeDelete = new ArrayList<>();
        }
        return hrSalaryStructureAccuralDeductionEffectTypeDelete;
    }

    /**
     * @param hrSalaryStructureAccuralDeductionEffectTypeDelete the
     * hrSalaryStructureAccuralDeductionEffectTypeDelete to set
     */
    public void setHrSalaryStructureAccuralDeductionEffectTypeDelete(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> hrSalaryStructureAccuralDeductionEffectTypeDelete) {
        this.hrSalaryStructureAccuralDeductionEffectTypeDelete = hrSalaryStructureAccuralDeductionEffectTypeDelete;
    }

    /**
     * @return the hrSalaryStructureAccuralEffectType
     */
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> getHrSalaryStructureAccuralEffectType() {
        if (hrSalaryStructureAccuralEffectType == null) {
            hrSalaryStructureAccuralEffectType = new ArrayList<>();
        }
        return hrSalaryStructureAccuralEffectType;
    }

    /**
     * @param hrSalaryStructureAccuralEffectType the
     * hrSalaryStructureAccuralEffectType to set
     */
    public void setHrSalaryStructureAccuralEffectType(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> hrSalaryStructureAccuralEffectType) {
        this.hrSalaryStructureAccuralEffectType = hrSalaryStructureAccuralEffectType;
    }

    /**
     * @return the hrSalaryStructureDeductionEffectType
     */
    public List<HrSalaryStructureAccuralDeductionEffectTypeDTO> getHrSalaryStructureDeductionEffectType() {
        if (hrSalaryStructureDeductionEffectType == null) {
            hrSalaryStructureDeductionEffectType = new ArrayList<>();
        }
        return hrSalaryStructureDeductionEffectType;
    }

    /**
     * @param hrSalaryStructureDeductionEffectType the
     * hrSalaryStructureDeductionEffectType to set
     */
    public void setHrSalaryStructureDeductionEffectType(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> hrSalaryStructureDeductionEffectType) {
        this.hrSalaryStructureDeductionEffectType = hrSalaryStructureDeductionEffectType;
    }

}
