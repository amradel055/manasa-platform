/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InstProjectGuideDTO;
import java.util.Date;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
public class InstallmentSearchBean {

    private Integer branchId;
    private InstProjectGuideDTO projectGuideFrom;
    private InstProjectGuideDTO projectGuideTo;
    private Date fromDate;
    private Date toDate;

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
     * @return the projectGuideFrom
     */
    public InstProjectGuideDTO getProjectGuideFrom() {
        return projectGuideFrom;
    }

    /**
     * @param projectGuideFrom the projectGuideFrom to set
     */
    public void setProjectGuideFrom(InstProjectGuideDTO projectGuideFrom) {
        this.projectGuideFrom = projectGuideFrom;
    }

    /**
     * @return the projectGuideTo
     */
    public InstProjectGuideDTO getProjectGuideTo() {
        return projectGuideTo;
    }

    /**
     * @param projectGuideTo the projectGuideTo to set
     */
    public void setProjectGuideTo(InstProjectGuideDTO projectGuideTo) {
        this.projectGuideTo = projectGuideTo;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

}
