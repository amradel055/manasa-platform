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
 * @author FreeComp
 */
public class OfferCopounDTO extends BaseEntityDTO{

    private String code;
    private Date startDate;
    private Date endDate;
    private Integer bankType;
    private Integer repeatNumber;
    private Integer expire;
    private Integer discountType;
    private BigDecimal discountValue;
    private InvOrganizationSiteDTO customerId;
    private InvInventoryDTO gallaryId;
    private GlBankDTO bankId;
    private String customerCode;
    private Integer gallaryType;
    
    
    private Integer number;

    public OfferCopounDTO() {
    }

    public OfferCopounDTO(Integer id) {
        this.id = id;
    }

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
     * @return the bankType
     */
    public Integer getBankType() {
        return bankType;
    }

    /**
     * @param bankType the bankType to set
     */
    public void setBankType(Integer bankType) {
        this.bankType = bankType;
    }

    /**
     * @return the repeatNumber
     */
    public Integer getRepeatNumber() {
        return repeatNumber;
    }

    /**
     * @param repeatNumber the repeatNumber to set
     */
    public void setRepeatNumber(Integer repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    /**
     * @return the expire
     */
    public Integer getExpire() {
        return expire;
    }

    /**
     * @param expire the expire to set
     */
    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    /**
     * @return the discountType
     */
    public Integer getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType the discountType to set
     */
    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
    
    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the customerId
     */
    public InvOrganizationSiteDTO getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSiteDTO customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the gallaryId
     */
    public InvInventoryDTO getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(InvInventoryDTO gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the bankId
     */
    public GlBankDTO getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(GlBankDTO bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the gallaryType
     */
    public Integer getGallaryType() {
        return gallaryType;
    }

    /**
     * @param gallaryType the gallaryType to set
     */
    public void setGallaryType(Integer gallaryType) {
        this.gallaryType = gallaryType;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

}
