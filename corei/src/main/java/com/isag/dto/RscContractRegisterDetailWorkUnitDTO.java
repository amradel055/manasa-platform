/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

public class RscContractRegisterDetailWorkUnitDTO extends BaseEntityDTO {

    private String remark;

    private Integer serial;

    private BigDecimal quantity;

    private BigDecimal price;

    private BigDecimal total;

    private RscWorkUnitsGuideDTO guideDTO;

    private RscWorkUnitsGuideDTO guideDTOBak;

    private Integer workUnitId;

    public RscContractRegisterDetailWorkUnitDTO() {
    }

    public RscContractRegisterDetailWorkUnitDTO(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RscContractRegisterDetailWorkUnitDTO)) {
            return false;
        }
        RscContractRegisterDetailWorkUnitDTO other = (RscContractRegisterDetailWorkUnitDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscTenderDetailWorkUnit[ id=" + id + " ]";
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
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
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

}
