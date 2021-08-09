/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventoryDelegator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hq002
 */
public class InvQuotationEntity extends BaseEntity {

    private Date date;
    private Date endDate;
    private InvOrganizationSite organizationSite;
    private InventoryDelegator invDelegator;
    private String remark; 
    private BigDecimal discount;
    private Boolean discountType;
    private BigDecimal afterDiscount;
    
    private BigDecimal totalDiscount;
    private Integer branchId;
    private Integer postFlag;
    private List<InvQuotationDetailsEntity> invQuotationDetailsEntitys;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public InvOrganizationSite getOrganizationSite() {
        return organizationSite;
    }

    public void setOrganizationSite(InvOrganizationSite organizationSite) {
        this.organizationSite = organizationSite;
    }

    public InventoryDelegator getInvDelegator() {
        return invDelegator;
    }

    public void setInvDelegator(InventoryDelegator invDelegator) {
        this.invDelegator = invDelegator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

   
    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public List<InvQuotationDetailsEntity> getInvQuotationDetailsEntitys() {
        return invQuotationDetailsEntitys;
    }

    public void setInvQuotationDetailsEntitys(List<InvQuotationDetailsEntity> invQuotationDetailsEntitys) {
        this.invQuotationDetailsEntitys = invQuotationDetailsEntitys;
    }

    /**
     * @return the totalDiscount
     */
    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * @param totalDiscount the totalDiscount to set
     */
    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the discountType
     */
    public Boolean getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType the discountType to set
     */
    public void setDiscountType(Boolean discountType) {
        this.discountType = discountType;
    }

    /**
     * @return the afterDiscount
     */
    public BigDecimal getAfterDiscount() {
        return afterDiscount;
    }

    /**
     * @param afterDiscount the afterDiscount to set
     */
    public void setAfterDiscount(BigDecimal afterDiscount) {
        this.afterDiscount = afterDiscount;
    }
}
