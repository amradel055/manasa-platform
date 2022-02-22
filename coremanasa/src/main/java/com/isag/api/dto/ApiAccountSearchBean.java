/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.api.dto;

/**
 *
 * @author ahmedEssam <ahmedEssam@isag.com>
 */
public class ApiAccountSearchBean {

    private String dateFrom;
    private String dateTo;
    private Boolean showReport;
    private String fromOrganizationSite;
    private String toOrganizationSite;
    private Integer organizationType;
    private Boolean zeroAmount;

    /**
     * @return the dateFrom
     */
    public String getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public String getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the showReport
     */
    public Boolean getShowReport() {
        return showReport;
    }

    /**
     * @param showReport the showReport to set
     */
    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

    /**
     * @return the fromOrganizationSite
     */
    public String getFromOrganizationSite() {
        return fromOrganizationSite;
    }

    /**
     * @param fromOrganizationSite the fromOrganizationSite to set
     */
    public void setFromOrganizationSite(String fromOrganizationSite) {
        this.fromOrganizationSite = fromOrganizationSite;
    }

    /**
     * @return the toOrganizationSite
     */
    public String getToOrganizationSite() {
        return toOrganizationSite;
    }

    /**
     * @param toOrganizationSite the toOrganizationSite to set
     */
    public void setToOrganizationSite(String toOrganizationSite) {
        this.toOrganizationSite = toOrganizationSite;
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
     * @return the zeroAmount
     */
    public Boolean getZeroAmount() {
        return zeroAmount;
    }

    /**
     * @param zeroAmount the zeroAmount to set
     */
    public void setZeroAmount(Boolean zeroAmount) {
        this.zeroAmount = zeroAmount;
    }

}
