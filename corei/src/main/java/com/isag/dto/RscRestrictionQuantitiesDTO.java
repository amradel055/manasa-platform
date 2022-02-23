/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class RscRestrictionQuantitiesDTO extends BaseEntityDTO {

    private static final long serialVersionUID = 1L;

    private Date date;

    private Integer branchId;

    private RscContractRegisterDTO contractId;

    private Integer serial;

    private RscTenderDTO tenderId;

    private RscRestrictionQuantitiesDTO restrictionId;

    private List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDTOList;
    
    private Boolean active;
    
    private InvOrganizationSiteDTO contractor;
    
    private InstProjectGuideDTO instProjectGuideDTO;
    
    private RscExtractRegisterDTO rscExtractRegisterDTO;

    public RscRestrictionQuantitiesDTO() {
    }

    public RscRestrictionQuantitiesDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RscRestrictionQuantitiesDTO)) {
            return false;
        }
        RscRestrictionQuantitiesDTO other = (RscRestrictionQuantitiesDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscRestrictionQuantities[ id=" + id + " ]";
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
     * @return the contractId
     */
    public RscContractRegisterDTO getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(RscContractRegisterDTO contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the tenderId
     */
    public RscTenderDTO getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(RscTenderDTO tenderId) {
        this.tenderId = tenderId;
    }

    /**
     * @return the restrictionId
     */
    public RscRestrictionQuantitiesDTO getRestrictionId() {
        return restrictionId;
    }

    /**
     * @param restrictionId the restrictionId to set
     */
    public void setRestrictionId(RscRestrictionQuantitiesDTO restrictionId) {
        this.restrictionId = restrictionId;
    }

    /**
     * @return the rscRestrictionQuantitiesDetailDTOList
     */
    public List<RscRestrictionQuantitiesDetailDTO> getRscRestrictionQuantitiesDetailDTOList() {
        if (rscRestrictionQuantitiesDetailDTOList == null || rscRestrictionQuantitiesDetailDTOList.isEmpty()) {

            rscRestrictionQuantitiesDetailDTOList = new ArrayList<>();

        }

        return rscRestrictionQuantitiesDetailDTOList;
    }

    /**
     * @param rscRestrictionQuantitiesDetailDTOList the
     * rscRestrictionQuantitiesDetailDTOList to set
     */
    public void setRscRestrictionQuantitiesDetailDTOList(List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDTOList) {
        this.rscRestrictionQuantitiesDetailDTOList = rscRestrictionQuantitiesDetailDTOList;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return the contractor
     */
    public InvOrganizationSiteDTO getContractor() {
        return contractor;
    }

    /**
     * @param contractor the contractor to set
     */
    public void setContractor(InvOrganizationSiteDTO contractor) {
        this.contractor = contractor;
    }

    /**
     * @return the instProjectGuideDTO
     */
    public InstProjectGuideDTO getInstProjectGuideDTO() {
        return instProjectGuideDTO;
    }

    /**
     * @param instProjectGuideDTO the instProjectGuideDTO to set
     */
    public void setInstProjectGuideDTO(InstProjectGuideDTO instProjectGuideDTO) {
        this.instProjectGuideDTO = instProjectGuideDTO;
    }

    /**
     * @return the rscExtractRegisterDTO
     */
    public RscExtractRegisterDTO getRscExtractRegisterDTO() {
        return rscExtractRegisterDTO;
    }

    /**
     * @param rscExtractRegisterDTO the rscExtractRegisterDTO to set
     */
    public void setRscExtractRegisterDTO(RscExtractRegisterDTO rscExtractRegisterDTO) {
        this.rscExtractRegisterDTO = rscExtractRegisterDTO;
    }

}
