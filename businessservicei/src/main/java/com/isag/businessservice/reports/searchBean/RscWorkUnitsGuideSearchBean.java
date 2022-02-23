/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.RscWorkUnitsGuideDTO;

/**
 *
 * @author Mostafa
 */
public class RscWorkUnitsGuideSearchBean {
    
    private Integer branchId;
    private RscWorkUnitsGuideDTO rscWorkUnitsGuideDTOFrom; 
    private RscWorkUnitsGuideDTO rscWorkUnitsGuideDTOTo;
            
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
     * @return the rscWorkUnitsGuideDTOFrom
     */
    public RscWorkUnitsGuideDTO getRscWorkUnitsGuideDTOFrom() {
        return rscWorkUnitsGuideDTOFrom;
    }

    /**
     * @param rscWorkUnitsGuideDTOFrom the rscWorkUnitsGuideDTOFrom to set
     */
    public void setRscWorkUnitsGuideDTOFrom(RscWorkUnitsGuideDTO rscWorkUnitsGuideDTOFrom) {
        this.rscWorkUnitsGuideDTOFrom = rscWorkUnitsGuideDTOFrom;
    }

    /**
     * @return the rscWorkUnitsGuideDTOTo
     */
    public RscWorkUnitsGuideDTO getRscWorkUnitsGuideDTOTo() {
        return rscWorkUnitsGuideDTOTo;
    }

    /**
     * @param rscWorkUnitsGuideDTOTo the rscWorkUnitsGuideDTOTo to set
     */
    public void setRscWorkUnitsGuideDTOTo(RscWorkUnitsGuideDTO rscWorkUnitsGuideDTOTo) {
        this.rscWorkUnitsGuideDTOTo = rscWorkUnitsGuideDTOTo;
    }
    

}
