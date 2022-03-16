/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InvOrganizationSiteDTO;
import java.util.Date;

/**
 *
 * @author Mostafa
 */
public class RscExtractReportSearchBean {

    private InvOrganizationSiteDTO invOrganizationSiteDTOFromContractor;
    private InvOrganizationSiteDTO invOrganizationSiteDTOToContractor;
    private InvOrganizationSiteDTO invOrganizationSiteDTOFromCustomer;
    private InvOrganizationSiteDTO invOrganizationSiteDTOToCustomer;
    private Date dateFrom;
    private Date dateTo;
    private String extractFrom;
    private String extractTo;
    private String test;
    private Boolean shortExtract;

    /**
     * @return the invOrganizationSiteDTOFromContractor
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTOFromContractor() {
        return invOrganizationSiteDTOFromContractor;
    }

    /**
     * @param invOrganizationSiteDTOFromContractor the
     * invOrganizationSiteDTOFromContractor to set
     */
    public void setInvOrganizationSiteDTOFromContractor(InvOrganizationSiteDTO invOrganizationSiteDTOFromContractor) {
        this.invOrganizationSiteDTOFromContractor = invOrganizationSiteDTOFromContractor;
    }

    /**
     * @return the invOrganizationSiteDTOToContractor
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTOToContractor() {
        return invOrganizationSiteDTOToContractor;
    }

    /**
     * @param invOrganizationSiteDTOToContractor the
     * invOrganizationSiteDTOToContractor to set
     */
    public void setInvOrganizationSiteDTOToContractor(InvOrganizationSiteDTO invOrganizationSiteDTOToContractor) {
        this.invOrganizationSiteDTOToContractor = invOrganizationSiteDTOToContractor;
    }

    /**
     * @return the invOrganizationSiteDTOFromCustomer
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTOFromCustomer() {
        return invOrganizationSiteDTOFromCustomer;
    }

    /**
     * @param invOrganizationSiteDTOFromCustomer the
     * invOrganizationSiteDTOFromCustomer to set
     */
    public void setInvOrganizationSiteDTOFromCustomer(InvOrganizationSiteDTO invOrganizationSiteDTOFromCustomer) {
        this.invOrganizationSiteDTOFromCustomer = invOrganizationSiteDTOFromCustomer;
    }

    /**
     * @return the invOrganizationSiteDTOToCustomer
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTOToCustomer() {
        return invOrganizationSiteDTOToCustomer;
    }

    /**
     * @param invOrganizationSiteDTOToCustomer the
     * invOrganizationSiteDTOToCustomer to set
     */
    public void setInvOrganizationSiteDTOToCustomer(InvOrganizationSiteDTO invOrganizationSiteDTOToCustomer) {
        this.invOrganizationSiteDTOToCustomer = invOrganizationSiteDTOToCustomer;
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
     * @return the extractFrom
     */
    public String getExtractFrom() {
        return extractFrom;
    }

    /**
     * @param extractFrom the extractFrom to set
     */
    public void setExtractFrom(String extractFrom) {
        this.extractFrom = extractFrom;
    }

    /**
     * @return the extractTo
     */
    public String getExtractTo() {
        return extractTo;
    }

    /**
     * @param extractTo the extractTo to set
     */
    public void setExtractTo(String extractTo) {
        this.extractTo = extractTo;
    }

    /**
     * @return the shortExtract
     */
    public Boolean getShortExtract() {
        return shortExtract;
    }

    /**
     * @param shortExtract the shortExtract to set
     */
    public void setShortExtract(Boolean shortExtract) {
        this.shortExtract = shortExtract;
    }

    /**
     * @return the test
     */
    public String getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(String test) {
        this.test = test;
    }

}
