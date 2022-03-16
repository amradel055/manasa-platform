/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class HrSalaryStructureAccuralDeductionEffectTypeDTO extends BaseEntityDTO {
  
    private Boolean active; 
    private Date modificationDate; 
    private Integer serial; 
    private HrSalaryStructureAccuralDeductionDTO hrSalaryStructureAccuralDeduction; 
    private HrSalaryEffectTypeDTO hrSalaryEffectType; 
    private HrSalaryEffectDTO hrSalaryEffectDTO; 
    private Integer branchId;  
    private Integer modifiedBy;
    private Integer type;

    
    
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
 
    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public HrSalaryStructureAccuralDeductionDTO getHrSalaryStructureAccuralDeduction() {
        return hrSalaryStructureAccuralDeduction;
    }

    public void setHrSalaryStructureAccuralDeduction(HrSalaryStructureAccuralDeductionDTO hrSalaryStructureAccuralDeduction) {
        this.hrSalaryStructureAccuralDeduction = hrSalaryStructureAccuralDeduction;
    }

    public HrSalaryEffectTypeDTO getHrSalaryEffectType() {
        return hrSalaryEffectType;
    }

    public void setHrSalaryEffectType(HrSalaryEffectTypeDTO hrSalaryEffectType) {
        this.hrSalaryEffectType = hrSalaryEffectType;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
 

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
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
        if (!(object instanceof HrSalaryStructureAccuralDeductionEffectTypeDTO)) {
            return false;
        }
        HrSalaryStructureAccuralDeductionEffectTypeDTO other = (HrSalaryStructureAccuralDeductionEffectTypeDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return   serial +" ";
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the hrSalaryEffectDTO
     */
    public HrSalaryEffectDTO getHrSalaryEffectDTO() {
        return hrSalaryEffectDTO;
    }

    /**
     * @param hrSalaryEffectDTO the hrSalaryEffectDTO to set
     */
    public void setHrSalaryEffectDTO(HrSalaryEffectDTO hrSalaryEffectDTO) {
        this.hrSalaryEffectDTO = hrSalaryEffectDTO;
    }
}
