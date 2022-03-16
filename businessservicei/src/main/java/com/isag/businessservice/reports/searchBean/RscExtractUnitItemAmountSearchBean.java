/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InstProjectGuideDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import java.util.Date;

/**
 *
 * @author khale
 */
public class RscExtractUnitItemAmountSearchBean {

    private Date dateFrom;
    private Date dateTo;
    private RscWorkUnitsGuideDTO rscWorkUnitsGuideFrom;
    private RscWorkUnitsGuideDTO rscWorkUnitsGuideTo;
    private InstProjectGuideDTO instProjectGuideFrom;
    private InstProjectGuideDTO instProjectGuideTo;
    private InvOrganizationSiteDTO supplierFrom;
    private InvOrganizationSiteDTO supplierTo;
    private InvOrganizationSiteDTO customerFrom;
    private InvOrganizationSiteDTO customerTo;

    /**
     * @return the crmEventDTOFrom
     */
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
     * @return the supplierFrom
     */
    public InvOrganizationSiteDTO getSupplierFrom() {
        return supplierFrom;
    }

    /**
     * @param supplierFrom the supplierFrom to set
     */
    public void setSupplierFrom(InvOrganizationSiteDTO supplierFrom) {
        this.supplierFrom = supplierFrom;
    }

    /**
     * @return the supplierTo
     */
    public InvOrganizationSiteDTO getSupplierTo() {
        return supplierTo;
    }

    /**
     * @param supplierTo the supplierTo to set
     */
    public void setSupplierTo(InvOrganizationSiteDTO supplierTo) {
        this.supplierTo = supplierTo;
    }

    /**
     * @return the customerFrom
     */
    public InvOrganizationSiteDTO getCustomerFrom() {
        return customerFrom;
    }

    /**
     * @param customerFrom the customerFrom to set
     */
    public void setCustomerFrom(InvOrganizationSiteDTO customerFrom) {
        this.customerFrom = customerFrom;
    }

    /**
     * @return the customerTo
     */
    public InvOrganizationSiteDTO getCustomerTo() {
        return customerTo;
    }

    /**
     * @param customerTo the customerTo to set
     */
    public void setCustomerTo(InvOrganizationSiteDTO customerTo) {
        this.customerTo = customerTo;
    }

    /**
     * @return the rscWorkUnitsGuideFrom
     */
    public RscWorkUnitsGuideDTO getRscWorkUnitsGuideFrom() {
        return rscWorkUnitsGuideFrom;
    }

    /**
     * @param rscWorkUnitsGuideFrom the rscWorkUnitsGuideFrom to set
     */
    public void setRscWorkUnitsGuideFrom(RscWorkUnitsGuideDTO rscWorkUnitsGuideFrom) {
        this.rscWorkUnitsGuideFrom = rscWorkUnitsGuideFrom;
    }

    /**
     * @return the rscWorkUnitsGuideTo
     */
    public RscWorkUnitsGuideDTO getRscWorkUnitsGuideTo() {
        return rscWorkUnitsGuideTo;
    }

    /**
     * @param rscWorkUnitsGuideTo the rscWorkUnitsGuideTo to set
     */
    public void setRscWorkUnitsGuideTo(RscWorkUnitsGuideDTO rscWorkUnitsGuideTo) {
        this.rscWorkUnitsGuideTo = rscWorkUnitsGuideTo;
    }

    /**
     * @return the instProjectGuideFrom
     */
    public InstProjectGuideDTO getInstProjectGuideFrom() {
        return instProjectGuideFrom;
    }

    /**
     * @param instProjectGuideFrom the instProjectGuideFrom to set
     */
    public void setInstProjectGuideFrom(InstProjectGuideDTO instProjectGuideFrom) {
        this.instProjectGuideFrom = instProjectGuideFrom;
    }

    /**
     * @return the instProjectGuideTo
     */
    public InstProjectGuideDTO getInstProjectGuideTo() {
        return instProjectGuideTo;
    }

    /**
     * @param instProjectGuideTo the instProjectGuideTo to set
     */
    public void setInstProjectGuideTo(InstProjectGuideDTO instProjectGuideTo) {
        this.instProjectGuideTo = instProjectGuideTo;
    }
}
