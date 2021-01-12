/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class InventoryDelegatorDTO1 extends BaseEntityDTO {
   
    private String code; 
    private String name; 
    private String mobile; 
    private String img; 
    private Integer type; 
    private BigDecimal allowDiscountLimit; 
    private BigDecimal targetSales; 
    private BigDecimal commission; 
    private Integer branchId;
    private Integer companyId; 
    private Integer modifiedBy;
    private Date modificationDate; 
    
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the allowDiscountLimit
     */
    public BigDecimal getAllowDiscountLimit() {
        return allowDiscountLimit;
    }

    /**
     * @param allowDiscountLimit the allowDiscountLimit to set
     */
    public void setAllowDiscountLimit(BigDecimal allowDiscountLimit) {
        this.allowDiscountLimit = allowDiscountLimit;
    }

    /**
     * @return the targetSales
     */
    public BigDecimal getTargetSales() {
        return targetSales;
    }

    /**
     * @param targetSales the targetSales to set
     */
    public void setTargetSales(BigDecimal targetSales) {
        this.targetSales = targetSales;
    }

    /**
     * @return the commission
     */
    public BigDecimal getCommission() {
        return commission;
    }

    /**
     * @param commission the commission to set
     */
    public void setCommission(BigDecimal commission) {
        this.commission = commission;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventoryDelegatorDTO1)) {
            return false;
        }
        InventoryDelegatorDTO1 other = (InventoryDelegatorDTO1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName() + " " + getCode();
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the modifiedBy
     */
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
