/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InvOrganizationSiteDTO;

public class contractorDataReportSearchBean {
    private InvOrganizationSiteDTO contractorCodeFrom;
    private InvOrganizationSiteDTO contractorCodeTo;
    private Integer branchId;

    /**
     * @return the contractorCodeFrom
     */
    public InvOrganizationSiteDTO getContractorCodeFrom() {
        return contractorCodeFrom;
    }

    /**
     * @param contractorCodeFrom the contractorCodeFrom to set
     */
    public void setContractorCodeFrom(InvOrganizationSiteDTO contractorCodeFrom) {
        this.contractorCodeFrom = contractorCodeFrom;
    }

    /**
     * @return the contractorCodeTo
     */
    public InvOrganizationSiteDTO getContractorCodeTo() {
        return contractorCodeTo;
    }

    /**
     * @param contractorCodeTo the contractorCodeTo to set
     */
    public void setContractorCodeTo(InvOrganizationSiteDTO contractorCodeTo) {
        this.contractorCodeTo = contractorCodeTo;
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
     * @return the contractorCodeFrom
     */
}
