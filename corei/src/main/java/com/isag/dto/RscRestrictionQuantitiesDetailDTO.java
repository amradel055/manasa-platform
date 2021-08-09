/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

public class RscRestrictionQuantitiesDetailDTO extends BaseEntityDTO {

    private Integer branchId;

    private Integer workUnitId;

    private BigDecimal contractQty;
    
    private BigDecimal excuteQty;
    
    private BigDecimal contractPrice;

    private RscWorkUnitsGuideDTO guideDTO;

    private RscWorkUnitsGuideDTO guideDTOBak;

    private Integer serial;

    private RscRestrictionQuantitiesDTO rscRestrictionQuantities;

    public RscRestrictionQuantitiesDetailDTO() {
    }

    public RscRestrictionQuantitiesDetailDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof RscRestrictionQuantitiesDetailDTO)) {
            return false;
        }
        RscRestrictionQuantitiesDetailDTO other = (RscRestrictionQuantitiesDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscRestrictionQuantitiesDetail[ id=" + id + " ]";
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
     * @return the workUnitId
     */
    public Integer getWorkUnitId() {
        return workUnitId;
    }

    /**
     * @param workUnitId the workUnitId to set
     */
    public void setWorkUnitId(Integer workUnitId) {
        this.workUnitId = workUnitId;
    }

    /**
     * @return the guideDTO
     */
    public RscWorkUnitsGuideDTO getGuideDTO() {
        return guideDTO;
    }

    /**
     * @param guideDTO the guideDTO to set
     */
    public void setGuideDTO(RscWorkUnitsGuideDTO guideDTO) {
        this.guideDTO = guideDTO;
    }

    /**
     * @return the guideDTOBak
     */
    public RscWorkUnitsGuideDTO getGuideDTOBak() {
        return guideDTOBak;
    }

    /**
     * @param guideDTOBak the guideDTOBak to set
     */
    public void setGuideDTOBak(RscWorkUnitsGuideDTO guideDTOBak) {
        this.guideDTOBak = guideDTOBak;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the rscRestrictionQuantities
     */
    public RscRestrictionQuantitiesDTO getRscRestrictionQuantities() {
        return rscRestrictionQuantities;
    }

    /**
     * @param rscRestrictionQuantities the rscRestrictionQuantities to set
     */
    public void setRscRestrictionQuantities(RscRestrictionQuantitiesDTO rscRestrictionQuantities) {
        this.rscRestrictionQuantities = rscRestrictionQuantities;
    }

    /**
     * @return the contractQty
     */
    public BigDecimal getContractQty() {
        return contractQty;
    }

    /**
     * @param contractQty the contractQty to set
     */
    public void setContractQty(BigDecimal contractQty) {
        this.contractQty = contractQty;
    }

    /**
     * @return the contractPrice
     */
    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    /**
     * @param contractPrice the contractPrice to set
     */
    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    /**
     * @return the excuteQty
     */
    public BigDecimal getExcuteQty() {
        if(excuteQty == null){
            excuteQty = BigDecimal.ZERO;
        }
        return excuteQty;
    }

    /**
     * @param excuteQty the excuteQty to set
     */
    public void setExcuteQty(BigDecimal excuteQty) {
        this.excuteQty = excuteQty;
    }

}
