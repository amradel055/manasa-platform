/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

public class RscAccrualDeductionDTO extends BaseEntityDTO {

    private String rscAccrualDeductionName;

    private Integer serial;

    private String rscAccrualDeductionCode;

    private Integer rscAccrualDeductionType;

    private Integer branchId;
    
    private Boolean active;

    public RscAccrualDeductionDTO() {
    }

    public RscAccrualDeductionDTO(Integer id) {
        this.id = id;
    }

    public String getRscAccrualDeductionName() {
        return rscAccrualDeductionName;
    }

    public void setRscAccrualDeductionName(String rscAccrualDeductionName) {
        this.rscAccrualDeductionName = rscAccrualDeductionName;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getRscAccrualDeductionCode() {
        return rscAccrualDeductionCode;
    }

    public void setRscAccrualDeductionCode(String rscAccrualDeductionCode) {
        this.rscAccrualDeductionCode = rscAccrualDeductionCode;
    }

    public Integer getRscAccrualDeductionType() {
        return rscAccrualDeductionType;
    }

    public void setRscAccrualDeductionType(Integer rscAccrualDeductionType) {
        this.rscAccrualDeductionType = rscAccrualDeductionType;
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
        if (!(object instanceof RscAccrualDeductionDTO)) {
            return false;
        }
        RscAccrualDeductionDTO other = (RscAccrualDeductionDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + rscAccrualDeductionCode + rscAccrualDeductionName + "";
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
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

}
