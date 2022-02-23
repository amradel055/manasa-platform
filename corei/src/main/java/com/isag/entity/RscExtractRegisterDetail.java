/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "rsc_extract_register_detail")
@XmlRootElement

public class RscExtractRegisterDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;
   
  @Column(name = "contract_qty")
    private BigDecimal contractQty;
    @Column(name = "old_qty")
    private BigDecimal oldQty;
    @Column(name = "current_qty")
    private BigDecimal currentQty;
    @Column(name = "price_sell")
    private BigDecimal priceSell;
    @Column(name = "ramp_value")
    private BigDecimal rampValue;
    @Column(name = "current_value")
    private BigDecimal currentValue;
    @Column(name = "ramp_percentage")
    private BigDecimal rampPercentage;
    @Column(name = "return_ramp_percentage")
    private BigDecimal returnRampPercentage;
    @Column(name = "return_ramp")
    private BigDecimal returnRamp;
    @Column(name = "total_ramp")
    private BigDecimal totalRamp;
    @Column(name = "net_total_return_ramp")
    private BigDecimal netTotalReturnRamp;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;
    @Size(max = 100)
    @Column(name = "remarks")
    private String remarks;
    
     @Column(name = "serial")
    private Integer serial;
    @Column(name = "old_ramp")
    private BigDecimal oldRamp;
   
    @Column(name = "net_cost")
    private BigDecimal netCost;
    @Column(name = "value_after_discount")
    private BigDecimal valueAfterDiscount;
    @JoinColumn(name = "work_unit_id", referencedColumnName = "id")
    @ManyToOne
    private RscWorkUnitsGuide workUnitId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
      @Column(name = "excute_quantity")
     private BigDecimal excuteQuantity;
    @JoinColumn(name = "rsc_extract_register", referencedColumnName = "id")
    @ManyToOne
    private RscExtractRegister rscExtractRegister;
    

    public RscExtractRegisterDetail() {
    }

    public RscExtractRegisterDetail(Integer id) {
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

    public RscWorkUnitsGuide getWorkUnitId() {
        return workUnitId;
    }

    public void setWorkUnitId(RscWorkUnitsGuide workUnitId) {
        this.workUnitId = workUnitId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

   
    public RscExtractRegister getRscExtractRegister() {
        return rscExtractRegister;
    }

    public void setRscExtractRegister(RscExtractRegister rscExtractRegister) {
        this.rscExtractRegister = rscExtractRegister;
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
        if (!(object instanceof RscExtractRegisterDetail)) {
            return false;
        }
        RscExtractRegisterDetail other = (RscExtractRegisterDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscExtractRegisterDetail[ id=" + id + " ]";
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
     * @return the excuteQuantity
     */
    public BigDecimal getExcuteQuantity() {
        return excuteQuantity;
    }

    /**
     * @param excuteQuantity the excuteQuantity to set
     */
    public void setExcuteQuantity(BigDecimal excuteQuantity) {
        this.excuteQuantity = excuteQuantity;
    }
    
}
