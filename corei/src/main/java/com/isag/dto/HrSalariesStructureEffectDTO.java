/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class HrSalariesStructureEffectDTO extends BaseEntityDTO {

    private Date effectDate;

    private BigDecimal unitEffectCount;

    private Integer status;

    private Integer salaryStructureId;

    private HrSalaryEffectDTO effectType;
    
    private Integer salaryEffectType;

    private SymbolDTO unitType;

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public BigDecimal getUnitEffectCount() {
        return unitEffectCount;
    }

    public void setUnitEffectCount(BigDecimal unitEffectCount) {
        this.unitEffectCount = unitEffectCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof HrSalariesStructureEffectDTO)) {
            return false;
        }
        HrSalariesStructureEffectDTO other = (HrSalariesStructureEffectDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.HrSalariesEffectDTO[ id=" + id + " ]";
    }

    /**
     * @return the salaryStructureId
     */
    public Integer getSalaryStructureId() {
        return salaryStructureId;
    }

    /**
     * @param salaryStructureId the salaryStructureId to set
     */
    public void setSalaryStructureId(Integer salaryStructureId) {
        this.salaryStructureId = salaryStructureId;
    }

    /**
     * @return the effectType
     */
    public HrSalaryEffectDTO getEffectType() {
        return effectType;
    }

    /**
     * @param effectType the effectType to set
     */
    public void setEffectType(HrSalaryEffectDTO effectType) {
        this.effectType = effectType;
    }

    /**
     * @return the unitType
     */
    public SymbolDTO getUnitType() {
        return unitType;
    }

    /**
     * @param unitType the unitType to set
     */
    public void setUnitType(SymbolDTO unitType) {
        this.unitType = unitType;
    }

    /**
     * @return the salaryEffectType
     */
    public Integer getSalaryEffectType() {
        return salaryEffectType;
    }

    /**
     * @param salaryEffectType the salaryEffectType to set
     */
    public void setSalaryEffectType(Integer salaryEffectType) {
        this.salaryEffectType = salaryEffectType;
    }

}
