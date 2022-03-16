/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;


import com.toby.entity.InvOrganizationSite;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class OrganizationSiteStatementSearchBean {

    private Integer branchId;
private Integer organizationType;
private InvOrganizationSite fromorganizationName;
    private InvOrganizationSite toorganizationName;
   private Date dateFrom;
    private Date dateTo;
    private Boolean showReport;
    

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

   
    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the organizationType
     */
    public Integer getOrganizationType() {
        return organizationType;
    }

    /**
     * @param organizationType the organizationType to set
     */
    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return the fromorganizationName
     */
    public InvOrganizationSite getFromorganizationName() {
        return fromorganizationName;
    }

    /**
     * @param fromorganizationName the fromorganizationName to set
     */
    public void setFromorganizationName(InvOrganizationSite fromorganizationName) {
        this.fromorganizationName = fromorganizationName;
    }

    /**
     * @return the toorganizationName
     */
    public InvOrganizationSite getToorganizationName() {
        return toorganizationName;
    }

    /**
     * @param toorganizationName the toorganizationName to set
     */
    public void setToorganizationName(InvOrganizationSite toorganizationName) {
        this.toorganizationName = toorganizationName;
    }
}
