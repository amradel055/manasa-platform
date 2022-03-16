/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.CostCenter;
import com.isag.entity.HosDoctor;
import com.isag.entity.InstProjectGuide;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ahmed
 */
public class HosCheckingUpDetailEntity {
    private Integer id;
    private HosDoctor hosDoctor;
    private InstProjectGuide instProjectGuideId;
    private InstProjectGuide instProjectGuideIdBak;
    private String hosDoctorName;
    private BigDecimal value;
    private BigDecimal discountRatio;
    private BigDecimal bearReatio;
    private BigDecimal total;
    private Integer branchId;
    private Date creationDate;
    private Date modificationDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer companyId;
    private Boolean markEdit;
    /**
     * @return the discountRatio
     */
    public BigDecimal getDiscountRatio() {
        return discountRatio;
    }

    /**
     * @param discountRatio the discountRatio to set
     */
    public void setDiscountRatio(BigDecimal discountRatio) {
        this.discountRatio = discountRatio;
    }

    /**
     * @return the bearReatio
     */
    public BigDecimal getBearReatio() {
        return bearReatio;
    }

    /**
     * @param bearReatio the bearReatio to set
     */
    public void setBearReatio(BigDecimal bearReatio) {
        this.bearReatio = bearReatio;
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
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    /**
     * @return the hosDoctorName
     */
    public String getHosDoctorName() {
        return hosDoctorName;
    }

    /**
     * @param hosDoctorName the hosDoctorName to set
     */
    public void setHosDoctorName(String hosDoctorName) {
        this.hosDoctorName = hosDoctorName;
    }

    /**
     * @return the instProjectGuideId
     */
    public InstProjectGuide getInstProjectGuideId() {
        return instProjectGuideId;
    }

    /**
     * @param instProjectGuideId the instProjectGuideId to set
     */
    public void setInstProjectGuideId(InstProjectGuide instProjectGuideId) {
        this.instProjectGuideId = instProjectGuideId;
    }

    /**
     * @return the instProjectGuideIdBak
     */
    public InstProjectGuide getInstProjectGuideIdBak() {
        return instProjectGuideIdBak;
    }

    /**
     * @param instProjectGuideIdBak the instProjectGuideIdBak to set
     */
    public void setInstProjectGuideIdBak(InstProjectGuide instProjectGuideIdBak) {
        this.instProjectGuideIdBak = instProjectGuideIdBak;
    }
}
