/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

public class RscPriceTenderDetailWorkUnitDTO extends BaseEntityDTO {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Integer status;
    private String remark;
    private Integer serial;
    private BigDecimal quantityUnits;
    private BigDecimal priceUnit;
    private BigDecimal totalUnits;
    private BigDecimal profitValue;
    private BigDecimal profitPercentage;
    private BigDecimal priceUnitSell;
    private BigDecimal totalProfit;
    private BigDecimal totalSell;
    private RscPriceTenderDetailWorkUnitDTO parent;
    private RscWorkUnitsGuideDTO guideDTO;
    private RscWorkUnitsGuideDTO guideDTOBak;
    private Integer branchId;
    private Integer tenderId;
    private Integer workUnitId;
    private BigDecimal quantityIn;
    private BigDecimal totalCost;
    private BigDecimal totalQuantityUnit;
    
    public RscPriceTenderDetailWorkUnitDTO() {
    }

    public RscPriceTenderDetailWorkUnitDTO(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public RscPriceTenderDetailWorkUnitDTO getParent() {
        return parent;
    }

    public void setParent(RscPriceTenderDetailWorkUnitDTO parent) {
        this.parent = parent;
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
        if (!(object instanceof RscPriceTenderDetailWorkUnitDTO)) {
            return false;
        }
        RscPriceTenderDetailWorkUnitDTO other = (RscPriceTenderDetailWorkUnitDTO) object;
        if ((getIndex() == null && other.getIndex() != null) || (getIndex() != null && !getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscTenderDetailWorkUnit[ id=" + id + " ]";
    }

    /**
     * @return the quantityUnits
     */
    public BigDecimal getQuantityUnits() {
        return quantityUnits;
    }

    /**
     * @param quantityUnits the quantityUnits to set
     */
    public void setQuantityUnits(BigDecimal quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    /**
     * @return the priceUnit
     */
    public BigDecimal getPriceUnit() {
        return priceUnit;
    }

    /**
     * @param priceUnit the priceUnit to set
     */
    public void setPriceUnit(BigDecimal priceUnit) {
        this.priceUnit = priceUnit;
    }

    /**
     * @return the totalUnits
     */
    public BigDecimal getTotalUnits() {
        return totalUnits;
    }

    /**
     * @param totalUnits the totalUnits to set
     */
    public void setTotalUnits(BigDecimal totalUnits) {
        this.totalUnits = totalUnits;
    }

    /**
     * @return the profitValue
     */
    public BigDecimal getProfitValue() {
        return profitValue;
    }

    /**
     * @param profitValue the profitValue to set
     */
    public void setProfitValue(BigDecimal profitValue) {
        this.profitValue = profitValue;
    }

    /**
     * @return the profitPercentage
     */
    public BigDecimal getProfitPercentage() {
        return profitPercentage;
    }

    /**
     * @param profitPercentage the profitPercentage to set
     */
    public void setProfitPercentage(BigDecimal profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    /**
     * @return the priceUnitSell
     */
    public BigDecimal getPriceUnitSell() {
        return priceUnitSell;
    }

    /**
     * @param priceUnitSell the priceUnitSell to set
     */
    public void setPriceUnitSell(BigDecimal priceUnitSell) {
        this.priceUnitSell = priceUnitSell;
    }

    /**
     * @return the totalProfit
     */
    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    /**
     * @param totalProfit the totalProfit to set
     */
    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    /**
     * @return the totalSell
     */
    public BigDecimal getTotalSell() {
        return totalSell;
    }

    /**
     * @param totalSell the totalSell to set
     */
    public void setTotalSell(BigDecimal totalSell) {
        this.totalSell = totalSell;
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
     * @return the tenderId
     */
    public Integer getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
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
     * @return the quantityIn
     */
    public BigDecimal getQuantityIn() {
        return quantityIn;
    }

    /**
     * @param quantityIn the quantityIn to set
     */
    public void setQuantityIn(BigDecimal quantityIn) {
        this.quantityIn = quantityIn;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the totalQuantityUnit
     */
    public BigDecimal getTotalQuantityUnit() {
        return totalQuantityUnit;
    }

    /**
     * @param totalQuantityUnit the totalQuantityUnit to set
     */
    public void setTotalQuantityUnit(BigDecimal totalQuantityUnit) {
        this.totalQuantityUnit = totalQuantityUnit;
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

}
