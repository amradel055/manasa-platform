/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.GlBank;
import com.isag.entity.HosDoctor;
import com.isag.entity.HosPatientRegistration;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ahmed
 */
public class HosCheckingUpEntity {

    private Integer id;
    private Date date;
    private GlBank glBank;
    private HosDoctor hosDoctor;
    private HosDoctor hosClinic;
    private HosPatientRegistration hosPatientRegistration;
    private String remark;
    private Integer branchId;
    private Date creationDate;
    private Date modificationDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer companyId;
    private BigDecimal value;
    private Integer serial;
    private Integer paymentType;
    private String paymentTypeText;
    private String literalGender;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the glBank
     */
    public GlBank getGlBank() {
        return glBank;
    }

    /**
     * @param glBank the glBank to set
     */
    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

    /**
     * @return the hosPatientRegistration
     */
    public HosPatientRegistration getHosPatientRegistration() {
        return hosPatientRegistration;
    }

    /**
     * @param hosPatientRegistration the hosPatientRegistration to set
     */
    public void setHosPatientRegistration(HosPatientRegistration hosPatientRegistration) {
        this.hosPatientRegistration = hosPatientRegistration;
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
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    /**
     * @return the createdBy
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the hosDoctor
     */
    public HosDoctor getHosDoctor() {
        return hosDoctor;
    }

    /**
     * @param hosDoctor the hosDoctor to set
     */
    public void setHosDoctor(HosDoctor hosDoctor) {
        this.hosDoctor = hosDoctor;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the hosClinic
     */
    public HosDoctor getHosClinic() {
        return hosClinic;
    }

    /**
     * @param hosClinic the hosClinic to set
     */
    public void setHosClinic(HosDoctor hosClinic) {
        this.hosClinic = hosClinic;
    }

    /**
     * @return the literalGender
     */
    public String getLiteralGender() {
        return literalGender;
    }

    /**
     * @param literalGender the literalGender to set
     */
    public void setLiteralGender(String literalGender) {
        this.literalGender = literalGender;
    }

    /**
     * @return the paymentType
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the paymentTypeText
     */
    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    /**
     * @param paymentTypeText the paymentTypeText to set
     */
    public void setPaymentTypeText(String paymentTypeText) {
        this.paymentTypeText = paymentTypeText;
    }

}
