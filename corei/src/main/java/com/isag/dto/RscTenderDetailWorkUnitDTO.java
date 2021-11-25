/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author amr
 */
public class RscTenderDetailWorkUnitDTO extends BaseEntityDTO {

    private BigDecimal quantity;
    private Integer status;
    private String remark;
    private Integer serial;
    private Integer serialParent;
    private Integer branchId;
    private RscTenderDetailWorkUnitDTO parent;
    private Integer tenderId;
    private Integer workUnitId;
    private RscWorkUnitsGuideDTO guideDTO;
    private RscWorkUnitsGuideDTO guideDTOBak;
    private Boolean child = Boolean.FALSE ;
    private Integer index ;
    private BigDecimal total;
    private BigDecimal price;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private String remarks;
    private String feedback;
    
    

    public RscTenderDetailWorkUnitDTO() {
    }

    public RscTenderDetailWorkUnitDTO(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        if(quantity == null){
            quantity = BigDecimal.ONE;
        }
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    
    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public Integer getWorkUnitId() {
        return workUnitId;
    }

    public void setWorkUnitId(Integer workUnitId) {
        this.workUnitId = workUnitId;
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
        if (!(object instanceof RscTenderDetailWorkUnitDTO)) {
            return false;
        }
        RscTenderDetailWorkUnitDTO other = (RscTenderDetailWorkUnitDTO) object;
        if ((this.index == null && other.index != null) || (this.index != null && !this.index.equals(other.index))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.dto.RscTenderDetailWorkUnitDTO[ id=" + serial + " ]";
    }

    /**
     * @return the parent
     */
    public RscTenderDetailWorkUnitDTO getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(RscTenderDetailWorkUnitDTO parent) {
        this.parent = parent;
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
     * @return the serialParent
     */
    public Integer getSerialParent() {
        return serialParent;
    }

    /**
     * @param serialParent the serialParent to set
     */
    public void setSerialParent(Integer serialParent) {
        this.serialParent = serialParent;
    }

    /**
     * @return the child
     */
    public Boolean getChild() {
        return child;
    }

    /**
     * @param child the child to set
     */
    public void setChild(Boolean child) {
        this.child = child;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
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
     * @return the total
     */
    public BigDecimal getTotal() {
        if(total == null){
            total = BigDecimal.ZERO;
        }
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        if(price == null){
            price = BigDecimal.ZERO;
        }
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the priceTo
     */
    public BigDecimal getPriceTo() {
        if(priceTo == null){
            priceTo = BigDecimal.ZERO;
        }
        return priceTo;
    }

    /**
     * @param priceTo the priceTo to set
     */
    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    /**
     * @return the feedback
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * @return the priceFrom
     */
    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    /**
     * @param priceFrom the priceFrom to set
     */
    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }
    
}
