/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RscPriceTenderDTO extends BaseEntityDTO {

    private String docNumber;
    private Date date;
    private Integer status;
    private String statusText;
    private String remark;
    private Integer serial;
    private Integer branchId;
    private InvOrganizationSiteDTO customerId;
    private Integer generalConsultant;
    private Integer projectManager;
    private Integer profissionalBussinessConsultant;
    private RscTenderDTO rscTenderDTO;
    private List<RscPriceTenderDetailWorkUnitDTO> RscPriceTenderDetailWorkUnitDTOList;
    private Boolean active; 
    

    public RscPriceTenderDTO() {
    }

    public RscPriceTenderDTO(Integer id) {
        this.id = id;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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
        if (!(object instanceof RscPriceTenderDTO)) {
            return false;
        }
        RscPriceTenderDTO other = (RscPriceTenderDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscTender[ id=" + id + " ]";
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
     * @return the generalConsultant
     */
    public Integer getGeneralConsultant() {
        return generalConsultant;
    }

    /**
     * @param generalConsultant the generalConsultant to set
     */
    public void setGeneralConsultant(Integer generalConsultant) {
        this.generalConsultant = generalConsultant;
    }

    /**
     * @return the projectManager
     */
    public Integer getProjectManager() {
        return projectManager;
    }

    /**
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(Integer projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * @return the profissionalBussinessConsultant
     */
    public Integer getProfissionalBussinessConsultant() {
        return profissionalBussinessConsultant;
    }

    /**
     * @param profissionalBussinessConsultant the profissionalBussinessConsultant to set
     */
    public void setProfissionalBussinessConsultant(Integer profissionalBussinessConsultant) {
        this.profissionalBussinessConsultant = profissionalBussinessConsultant;
    }

    /**
     * @return the customerId
     */
    public InvOrganizationSiteDTO getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSiteDTO customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the rscTenderDTO
     */
    public RscTenderDTO getRscTenderDTO() {
        return rscTenderDTO;
    }

    /**
     * @param rscTenderDTO the rscTenderDTO to set
     */
    public void setRscTenderDTO(RscTenderDTO rscTenderDTO) {
        this.rscTenderDTO = rscTenderDTO;
    }

    /**
     * @return the RscPriceTenderDetailWorkUnitDTOList
     */
    public List<RscPriceTenderDetailWorkUnitDTO> getRscPriceTenderDetailWorkUnitDTOList() {
         if (RscPriceTenderDetailWorkUnitDTOList == null || RscPriceTenderDetailWorkUnitDTOList.isEmpty()) {
            
                RscPriceTenderDetailWorkUnitDTOList = new ArrayList<>();
            }
       

        return RscPriceTenderDetailWorkUnitDTOList;
    }

    /**
     * @param RscPriceTenderDetailWorkUnitDTOList the RscPriceTenderDetailWorkUnitDTOList to set
     */
    public void setRscPriceTenderDetailWorkUnitDTOList(List<RscPriceTenderDetailWorkUnitDTO> RscPriceTenderDetailWorkUnitDTOList) {
        this.RscPriceTenderDetailWorkUnitDTOList = RscPriceTenderDetailWorkUnitDTOList;
    }

    /**
     * @return the statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * @param statusText the statusText to set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
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

    
}
