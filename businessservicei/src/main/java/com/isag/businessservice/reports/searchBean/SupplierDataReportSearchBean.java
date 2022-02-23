/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InvOrganizationSite;

/**
 *
 * @author hhhh
 */
public class SupplierDataReportSearchBean {

    private Integer branchId;

    private Integer type;

    private InvOrganizationSiteDTO fromorganizationName;
    private InvOrganizationSiteDTO toorganizationName;

    private Boolean showReport;

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

    /**
     * @return the fromorganizationName
     */
    public InvOrganizationSiteDTO getFromorganizationName() {
        return fromorganizationName;
    }

    /**
     * @param fromorganizationName the fromorganizationName to set
     */
    public void setFromorganizationName(InvOrganizationSiteDTO fromorganizationName) {
        this.fromorganizationName = fromorganizationName;
    }

    /**
     * @return the toorganizationName
     */
    public InvOrganizationSiteDTO getToorganizationName() {
        return toorganizationName;
    }

    /**
     * @param toorganizationName the toorganizationName to set
     */
    public void setToorganizationName(InvOrganizationSiteDTO toorganizationName) {
        this.toorganizationName = toorganizationName;
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

}
