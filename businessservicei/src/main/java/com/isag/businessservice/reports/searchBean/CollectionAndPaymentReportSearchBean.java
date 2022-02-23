/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvItemDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class CollectionAndPaymentReportSearchBean {

    private Integer userId;
    private Integer branchId;
    private Integer status;
    private Date fromDate;
    private Date toDate;
    private InvOrganizationSiteDTO orgName;
    private InvOrganizationSiteDTO orgNameFrom;
    private InvOrganizationSiteDTO orgNameTo;
    private InstProjectGuideDTO projectGuideFrom;
    private InstProjectGuideDTO projectGuideTo;
    private InvItemDTO invItemDTOFrom;
    private InvItemDTO invItemDTOTo;

    private Boolean showReport;

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
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
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
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

    /**
     * @return the orgName
     */
    public InvOrganizationSiteDTO getOrgName() {
        return orgName;
    }

    /**
     * @param orgName the orgName to set
     */
    public void setOrgName(InvOrganizationSiteDTO orgName) {
        this.orgName = orgName;
    }

    /**
     * @return the orgNameFrom
     */
    public InvOrganizationSiteDTO getOrgNameFrom() {
        return orgNameFrom;
    }

    /**
     * @param orgNameFrom the orgNameFrom to set
     */
    public void setOrgNameFrom(InvOrganizationSiteDTO orgNameFrom) {
        this.orgNameFrom = orgNameFrom;
    }

    /**
     * @return the orgNameTo
     */
    public InvOrganizationSiteDTO getOrgNameTo() {
        return orgNameTo;
    }

    /**
     * @param orgNameTo the orgNameTo to set
     */
    public void setOrgNameTo(InvOrganizationSiteDTO orgNameTo) {
        this.orgNameTo = orgNameTo;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        CollectionAndPaymentReportSearchBean collection = (CollectionAndPaymentReportSearchBean) object;
        return branchId == collection.branchId
                && status == collection.status
                && fromDate == collection.fromDate
                && toDate == collection.toDate
                && orgName == collection.orgName
                && orgNameFrom == collection.orgNameFrom
                && orgNameTo == collection.orgNameTo
                && projectGuideFrom == collection.projectGuideFrom
                && projectGuideTo == collection.projectGuideTo;

    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the invItemDTOFrom
     */
    public InvItemDTO getInvItemDTOFrom() {
        return invItemDTOFrom;
    }

    /**
     * @param invItemDTOFrom the invItemDTOFrom to set
     */
    public void setInvItemDTOFrom(InvItemDTO invItemDTOFrom) {
        this.invItemDTOFrom = invItemDTOFrom;
    }

    /**
     * @return the invItemDTOTo
     */
    public InvItemDTO getInvItemDTOTo() {
        return invItemDTOTo;
    }

    /**
     * @param invItemDTOTo the invItemDTOTo to set
     */
    public void setInvItemDTOTo(InvItemDTO invItemDTOTo) {
        this.invItemDTOTo = invItemDTOTo;
    }
}
