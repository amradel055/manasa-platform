/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "offer_copoun")
@XmlRootElement
public class OfferCopoun extends BaseEntity{

    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endDate")
    private Date endDate;
    @Column(name = "bank_type")
    private Integer bankType;
    @Column(name = "repeat_number")
    private Integer repeatNumber;
    @Column(name = "expire")
    private Integer expire;
    @Column(name = "discount_type")
    private Integer discountType;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite customerId;
    @JoinColumn(name = "gallary_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory gallaryId;
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank bankId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @Column(name = "gallary_type")
    private Integer gallaryType;
    @Column(name = "type")
    private Integer type;

    public OfferCopoun() {
    }

    public OfferCopoun(Integer id) {
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
     * @return the customerId
     */
    public InvOrganizationSite getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSite customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the gallaryId
     */
    public InvInventory getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(InvInventory gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the bankId
     */
    public GlBank getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(GlBank bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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

}
