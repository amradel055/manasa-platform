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
 * @author amr
 */
public class RscQotationPriceTenderDTO extends BaseEntityDTO {

   
    private String docNumber;
 
    private Date date;

    private Integer status;
    
    private String remark;

    private Integer serial;
   
    private Integer branchId;
   
    private InvOrganizationSiteDTO customerId;
   
    private String qotationStatus;
   
    private Integer postFlag;
    
    private RscPriceTenderDTO rscPriceTenderId;
    
    private RscGenaricLoad rscGenaricLoad;
    
    private List<RscQotationPriceTenderDetailWorkUnitDTO> rscQotationPriceTenderDetailWorkUnitDTOList;
    
    private InvOrganizationSiteDTO subContractor;
    
    private RscTenderDTO tenderId;
    
    private Boolean Active;

    public RscQotationPriceTenderDTO() {
    }

    public RscQotationPriceTenderDTO(Integer id) {
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
        if (!(object instanceof RscQotationPriceTenderDTO)) {
            return false;
        }
        RscQotationPriceTenderDTO other = (RscQotationPriceTenderDTO) object;
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
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the rscPriceTenderId
     */
    public RscPriceTenderDTO getRscPriceTenderId() {
        return rscPriceTenderId;
    }

    /**
     * @param rscPriceTenderId the rscPriceTenderId to set
     */
    public void setRscPriceTenderId(RscPriceTenderDTO rscPriceTenderId) {
        this.rscPriceTenderId = rscPriceTenderId;
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
     * @return the qotationStatus
     */
    public String getQotationStatus() {
        return qotationStatus;
    }

    /**
     * @param qotationStatus the qotationStatus to set
     */
    public void setQotationStatus(String qotationStatus) {
        this.qotationStatus = qotationStatus;
    }

    /**
     * @return the rscQotationPriceTenderDetailWorkUnitDTOList
     */
    public List<RscQotationPriceTenderDetailWorkUnitDTO> getRscQotationPriceTenderDetailWorkUnitDTOList() {
         if (rscQotationPriceTenderDetailWorkUnitDTOList == null || rscQotationPriceTenderDetailWorkUnitDTOList.isEmpty()) {
            
                rscQotationPriceTenderDetailWorkUnitDTOList = new ArrayList<>();
          
        }
        return rscQotationPriceTenderDetailWorkUnitDTOList;
    }

    /**
     * @param rscQotationPriceTenderDetailWorkUnitDTOList the rscQotationPriceTenderDetailWorkUnitDTOList to set
     */
    public void setRscQotationPriceTenderDetailWorkUnitDTOList(List<RscQotationPriceTenderDetailWorkUnitDTO> rscQotationPriceTenderDetailWorkUnitDTOList) {
        this.rscQotationPriceTenderDetailWorkUnitDTOList = rscQotationPriceTenderDetailWorkUnitDTOList;
    }

    /**
     * @return the rscGenaricLoad
     */
    public RscGenaricLoad getRscGenaricLoad() {
        return rscGenaricLoad;
    }

    /**
     * @param rscGenaricLoad the rscGenaricLoad to set
     */
    public void setRscGenaricLoad(RscGenaricLoad rscGenaricLoad) {
        this.rscGenaricLoad = rscGenaricLoad;
    }

    /**
     * @return the subContractor
     */
    public InvOrganizationSiteDTO getSubContractor() {
        return subContractor;
    }

    /**
     * @param subContractor the subContractor to set
     */
    public void setSubContractor(InvOrganizationSiteDTO subContractor) {
        this.subContractor = subContractor;
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
     * @return the Active
     */
    public Boolean getActive() {
        return Active;
    }

    /**
     * @param Active the Active to set
     */
    public void setActive(Boolean Active) {
        this.Active = Active;
    }
    
}
