/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hq003
 */
public class InvOrganizationSiteDTOAPI extends BaseEntityDTOAPI {

    private String code;
    private String name;
    private String mobile;
    private String email;
    private BigDecimal discount;
    private String remark;
    private Integer branchId;
    private String offerCompanyName;
    private BigDecimal length;
    private BigDecimal shoulder;
    private BigDecimal step;
    ////// API //////
    private Integer gallaryIdAPI;
    private Integer accountIdAPI;
    private Integer typeShow;
    

    public InvOrganizationSiteDTOAPI() {
    }

    public InvOrganizationSiteDTOAPI(Integer id) {
        this.id = id;
    }

    public InvOrganizationSiteDTOAPI(Integer id, String name, Date creationDate) {
        this.id = id;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvOrganizationSiteDTOAPI)) {
            return false;
        }
        InvOrganizationSiteDTOAPI other = (InvOrganizationSiteDTOAPI) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code + " " + (mobile == null ? "" : mobile);
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the length
     */
    public BigDecimal getLength() {
        if (length == null) {
            length = BigDecimal.ZERO;
        }
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /**
     * @return the shoulder
     */
    public BigDecimal getShoulder() {
        if (shoulder == null) {
            shoulder = BigDecimal.ZERO;
        }
        return shoulder;
    }

    /**
     * @param shoulder the shoulder to set
     */
    public void setShoulder(BigDecimal shoulder) {
        this.shoulder = shoulder;
    }

    /**
     * @return the step
     */
    public BigDecimal getStep() {
        if (step == null) {
            step = BigDecimal.ZERO;
        }
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(BigDecimal step) {
        this.step = step;
    }

    /**
     * @return the gallaryIdAPI
     */
    public Integer getGallaryIdAPI() {
        return gallaryIdAPI;
    }

    /**
     * @param gallaryIdAPI the gallaryIdAPI to set
     */
    public void setGallaryIdAPI(Integer gallaryIdAPI) {
        this.gallaryIdAPI = gallaryIdAPI;
    }

    /**
     * @return the accountIdAPI
     */
    public Integer getAccountIdAPI() {
        return accountIdAPI;
    }

    /**
     * @param accountIdAPI the accountIdAPI to set
     */
    public void setAccountIdAPI(Integer accountIdAPI) {
        this.accountIdAPI = accountIdAPI;
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
     * @return the offerCompanyName
     */
    public String getOfferCompanyName() {
        return offerCompanyName;
    }

    /**
     * @param offerCompanyName the offerCompanyName to set
     */
    public void setOfferCompanyName(String offerCompanyName) {
        this.offerCompanyName = offerCompanyName;
    }

    /**
     * @return the typeShow
     */
    public Integer getTypeShow() {
        return typeShow;
    }

    /**
     * @param typeShow the typeShow to set
     */
    public void setTypeShow(Integer typeShow) {
        this.typeShow = typeShow;
    }

  

}
