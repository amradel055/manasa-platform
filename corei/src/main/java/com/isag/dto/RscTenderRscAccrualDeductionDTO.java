/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author AhmedEssam
 */
public class RscTenderRscAccrualDeductionDTO extends BaseEntityDTO{
    
   
    private RscAccrualDeductionDTO rscAccrualDeductionId;
    private RscAccrualDeductionDTO rscAccrualDeductionIdBak;
    private RscTenderDTO rscTenderId;
    private BigDecimal percentage;
 

    public RscAccrualDeductionDTO getRscAccrualDeductionId() {
        return rscAccrualDeductionId;
    }

    public void setRscAccrualDeductionId(RscAccrualDeductionDTO rscAccrualDeductionId) {
        this.rscAccrualDeductionId = rscAccrualDeductionId;
    }

    public RscTenderDTO getRscTenderId() {
        return rscTenderId;
    }

    public void setRscTenderId(RscTenderDTO rscTenderId) {
        this.rscTenderId = rscTenderId;
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
        if (!(object instanceof RscTenderRscAccrualDeductionDTO)) {
            return false;
        }
        RscTenderRscAccrualDeductionDTO other = (RscTenderRscAccrualDeductionDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscTenderRscAccrualDeduction[ id=" + id + " ]";
    }

    /**
     * @return the percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the rscAccrualDeductionIdBak
     */
    public RscAccrualDeductionDTO getRscAccrualDeductionIdBak() {
        return rscAccrualDeductionIdBak;
    }

    /**
     * @param rscAccrualDeductionIdBak the rscAccrualDeductionIdBak to set
     */
    public void setRscAccrualDeductionIdBak(RscAccrualDeductionDTO rscAccrualDeductionIdBak) {
        this.rscAccrualDeductionIdBak = rscAccrualDeductionIdBak;
    }
    
}
