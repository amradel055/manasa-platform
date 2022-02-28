/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author hq002
 */

public class InventoryDelegatorDTO extends BaseEntityDTO {

    private String code;
    private String name;
    private String mobile;
    private String img;
    private Integer type;
    private BigDecimal allowDiscountLimit;
    private BigDecimal targetSales;
    private BigDecimal commission;
    private InvInventoryDTO gallaryDTO;
    

    public InventoryDelegatorDTO() {
    }

    public InventoryDelegatorDTO(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAllowDiscountLimit() {
        return allowDiscountLimit;
    }

    public void setAllowDiscountLimit(BigDecimal allowDiscountLimit) {
        this.allowDiscountLimit = allowDiscountLimit;
    }

    public BigDecimal getTargetSales() {
        return targetSales;
    }

    public void setTargetSales(BigDecimal targetSales) {
        this.targetSales = targetSales;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

   

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(object instanceof InventoryDelegatorDTO)) {
            return false;
        }
        InventoryDelegatorDTO other = (InventoryDelegatorDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the gallaryDTO
     */
    public InvInventoryDTO getGallaryDTO() {
        return gallaryDTO;
    }

    /**
     * @param gallaryDTO the gallaryDTO to set
     */
    public void setGallaryDTO(InvInventoryDTO gallaryDTO) {
        this.gallaryDTO = gallaryDTO;
    }

}
