/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mostafa
 */
public class RscContractExtractionWorkunitSearchBean {

    private Date fromDate;
    private Date toDate;
    private Integer branchId;

    private List<RscWorkUnitsGuideDTO> workUnitList;
    private List<InvOrganizationSiteDTO> siteList;
    private RscContractRegisterDTO contract;
    private InvOrganizationSiteDTO owner;

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
     * @return the workUnitList
     */
    public List<RscWorkUnitsGuideDTO> getWorkUnitList() {
        return workUnitList;
    }

    /**
     * @param workUnitList the workUnitList to set
     */
    public void setWorkUnitList(List<RscWorkUnitsGuideDTO> workUnitList) {
        this.workUnitList = workUnitList;
    }

    /**
     * @return the contract
     */
    public RscContractRegisterDTO getContract() {
        return contract;
    }

    /**
     * @param contract the contract to set
     */
    public void setContract(RscContractRegisterDTO contract) {
        this.contract = contract;
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
     * @return the siteList
     */
    public List<InvOrganizationSiteDTO> getSiteList() {
        return siteList;
    }

    /**
     * @param siteList the siteList to set
     */
    public void setSiteList(List<InvOrganizationSiteDTO> siteList) {
        this.siteList = siteList;
    }

    /**
     * @return the owner
     */
    public InvOrganizationSiteDTO getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(InvOrganizationSiteDTO owner) {
        this.owner = owner;
    }

}
