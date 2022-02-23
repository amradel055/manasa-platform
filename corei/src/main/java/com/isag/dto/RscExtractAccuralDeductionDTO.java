/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

public class RscExtractAccuralDeductionDTO extends BaseEntityDTO {

 
    private BigDecimal rscAccuralDeductionValue;

    private BigDecimal rscAccuralDeductionPercentage;
   
    private Integer serial;
    
    private Integer branchId;
   
  
    private RscAccrualDeductionDTO rscAccrualDeductionDTO;
    private RscAccrualDeductionDTO rscAccrualDeductionDTObak;
   
    private RscExtractRegisterDTO rscExtractRegisterId;
    

    public RscExtractAccuralDeductionDTO() {
    }

    public RscExtractAccuralDeductionDTO(Integer id) {
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

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RscExtractAccuralDeductionDTO)) {
            return false;
        }
        RscExtractAccuralDeductionDTO other = (RscExtractAccuralDeductionDTO) object;
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
     * @param rscExtractRegisterId the rscExtractRegisterId to set
     */
    public void setRscExtractRegisterId(RscExtractRegisterDTO rscExtractRegisterId) {
        this.rscExtractRegisterId = rscExtractRegisterId;
    }

    /**
     * @return the rscExtractRegisterId
     */
    public RscExtractRegisterDTO getRscExtractRegisterId() {
        return rscExtractRegisterId;
    }

    /**
     * @return the rscAccrualDeductionDTO
     */
    public RscAccrualDeductionDTO getRscAccrualDeductionDTO() {
        return rscAccrualDeductionDTO;
    }

    /**
     * @param rscAccrualDeductionDTO the rscAccrualDeductionDTO to set
     */
    public void setRscAccrualDeductionDTO(RscAccrualDeductionDTO rscAccrualDeductionDTO) {
        this.rscAccrualDeductionDTO = rscAccrualDeductionDTO;
    }

    /**
     * @return the rscAccrualDeductionDTObak
     */
    public RscAccrualDeductionDTO getRscAccrualDeductionDTObak() {
        return rscAccrualDeductionDTObak;
    }

    /**
     * @param rscAccrualDeductionDTObak the rscAccrualDeductionDTObak to set
     */
    public void setRscAccrualDeductionDTObak(RscAccrualDeductionDTO rscAccrualDeductionDTObak) {
        this.rscAccrualDeductionDTObak = rscAccrualDeductionDTObak;
    }
    
}
