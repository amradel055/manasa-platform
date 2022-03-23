/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class RscExtractRegisterDetailDTO extends BaseEntityDTO {

    private BigDecimal contractQty;

    private BigDecimal oldQty;

    private BigDecimal currentQty;

    private BigDecimal excuteQty;

    private BigDecimal priceSell;

    private BigDecimal rampValue;

    private BigDecimal currentValue;

    private BigDecimal rampPercentage;

    private BigDecimal returnRampPercentage;

    private BigDecimal returnRamp;

    private BigDecimal totalRamp;

    private BigDecimal netTotalReturnRamp;

    private BigDecimal discountValue;

    private BigDecimal discountPercentage;

    private BigDecimal valueAfterDiscount;
    private BigDecimal netCost;

    private String remarks;

    private Integer serial;

    private BigDecimal oldRamp;
    private BigDecimal netRamp;
    private RscWorkUnitsGuideDTO guideDTO;
    private RscWorkUnitsGuideDTO guideDTOBak;
    private Integer workUnitId;

    private Integer branchId;
    private RscExtractRegisterDTO rscExtractRegister;

    private RscExtractRegisterDetailDTO rscExtractRegisterDetail;

    private Boolean master;
    public RscExtractRegisterDetailDTO() {
    }

    public RscExtractRegisterDetailDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getContractQty() {
        return contractQty;
    }

    public void setContractQty(BigDecimal contractQty) {
        this.contractQty = contractQty;
    }

    public BigDecimal getOldQty() {
        return oldQty;
    }

    public void setOldQty(BigDecimal oldQty) {
        this.oldQty = oldQty;
    }

    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    public void setCurrentQty(BigDecimal currentQty) {
        this.currentQty = currentQty;
    }

    public BigDecimal getPriceSell() {
        return priceSell;
    }

    public void setPriceSell(BigDecimal priceSell) {
        this.priceSell = priceSell;
    }

    public BigDecimal getRampValue() {
        return rampValue;
    }

    public void setRampValue(BigDecimal rampValue) {
        this.rampValue = rampValue;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getRampPercentage() {
        return rampPercentage;
    }

    public void setRampPercentage(BigDecimal rampPercentage) {
        this.rampPercentage = rampPercentage;
    }

    public BigDecimal getReturnRampPercentage() {
        return returnRampPercentage;
    }

    public void setReturnRampPercentage(BigDecimal returnRampPercentage) {
        this.returnRampPercentage = returnRampPercentage;
    }

    public BigDecimal getReturnRamp() {
        return returnRamp;
    }

    public void setReturnRamp(BigDecimal returnRamp) {
        this.returnRamp = returnRamp;
    }

    public BigDecimal getTotalRamp() {
        return totalRamp;
    }

    public void setTotalRamp(BigDecimal totalRamp) {
        this.totalRamp = totalRamp;
    }

    public BigDecimal getNetTotalReturnRamp() {
        return netTotalReturnRamp;
    }

    public void setNetTotalReturnRamp(BigDecimal netTotalReturnRamp) {
        this.netTotalReturnRamp = netTotalReturnRamp;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getOldRamp() {
        return oldRamp;
    }

    public void setOldRamp(BigDecimal oldRamp) {
        this.oldRamp = oldRamp;
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
     * @return the rscExtractRegisterDetail
     */
    public RscExtractRegisterDetailDTO getRscExtractRegisterDetail() {
        return rscExtractRegisterDetail;
    }

    /**
     * @param rscExtractRegisterDetail the rscExtractRegisterDetail to set
     */
    public void setRscExtractRegisterDetail(RscExtractRegisterDetailDTO rscExtractRegisterDetail) {
        this.rscExtractRegisterDetail = rscExtractRegisterDetail;
    }

    /**
     * @return the valueAfterDiscount
     */
    public BigDecimal getValueAfterDiscount() {
        return valueAfterDiscount;
    }

    /**
     * @param valueAfterDiscount the valueAfterDiscount to set
     */
    public void setValueAfterDiscount(BigDecimal valueAfterDiscount) {
        this.valueAfterDiscount = valueAfterDiscount;
    }

    /**
     * @return the netCost
     */
    public BigDecimal getNetCost() {
        return netCost;
    }

    /**
     * @param netCost the netCost to set
     */
    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    /**
     * @return the rscExtractRegister
     */
    public RscExtractRegisterDTO getRscExtractRegister() {
        return rscExtractRegister;
    }

    /**
     * @param rscExtractRegister the rscExtractRegister to set
     */
    public void setRscExtractRegister(RscExtractRegisterDTO rscExtractRegister) {
        this.rscExtractRegister = rscExtractRegister;
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

    /**
     * @return the netRamp
     */
    public BigDecimal getNetRamp() {
        return netRamp;
    }

    /**
     * @param netRamp the netRamp to set
     */
    public void setNetRamp(BigDecimal netRamp) {
        this.netRamp = netRamp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RscExtractRegisterDetailDTO)) {
            return false;
        }
        RscExtractRegisterDetailDTO other = (RscExtractRegisterDetailDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getIndex() + " ";
    }

    /**
     * @return the master
     */
    public Boolean getMaster() {
        return master;
    }

    /**
     * @param master the master to set
     */
    public void setMaster(Boolean master) {
        this.master = master;
    }

}
