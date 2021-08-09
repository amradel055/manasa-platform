/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amr
 */
public class RscTenderDTO extends BaseEntityDTO {

    private String docNumber;
    private Date date;
    private String statusText;
    private String remark;
    private String contractorName;
    private Integer serial;
    private Integer type;
    private Integer branchId;
    private SymbolDTO generalConsultant;
    private SymbolDTO projectManager;
    private SymbolDTO profissionalBussinessConsultant;
    private BigDecimal totalAmount;
    private BigDecimal totalValue;
    private BigDecimal totalAccural;
    private BigDecimal totalDeduction;
    private List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList;    
    private List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTOAccruaList;
    private List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTODeductionList;
    private List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTODeletedList;
    private List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTODeletedList;

    private InvOrganizationSiteDTO customerId;
    private Boolean active;

    public RscTenderDTO() {
    }

    public RscTenderDTO(Integer id) {
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof RscTenderDTO)) {
            return false;
        }
        RscTenderDTO other = (RscTenderDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + serial + "" + remark;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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
     * @return the generalConsultant
     */
    public SymbolDTO getGeneralConsultant() {
        return generalConsultant;
    }

    /**
     * @param generalConsultant the generalConsultant to set
     */
    public void setGeneralConsultant(SymbolDTO generalConsultant) {
        this.generalConsultant = generalConsultant;
    }

    /**
     * @return the projectManager
     */
    public SymbolDTO getProjectManager() {
        return projectManager;
    }

    /**
     * @param projectManager the projectManager to set
     */
    public void setProjectManager(SymbolDTO projectManager) {
        this.projectManager = projectManager;
    }

    /**
     * @return the profissionalBussinessConsultant
     */
    public SymbolDTO getProfissionalBussinessConsultant() {
        return profissionalBussinessConsultant;
    }

    /**
     * @param profissionalBussinessConsultant the
     * profissionalBussinessConsultant to set
     */
    public void setProfissionalBussinessConsultant(SymbolDTO profissionalBussinessConsultant) {
        this.profissionalBussinessConsultant = profissionalBussinessConsultant;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTOList
     */
    public List<RscTenderDetailWorkUnitDTO> getRscTenderDetailWorkUnitDTOList() {
        if (rscTenderDetailWorkUnitDTOList == null || rscTenderDetailWorkUnitDTOList.isEmpty()) {
            rscTenderDetailWorkUnitDTOList = new ArrayList<>();
        }
        return rscTenderDetailWorkUnitDTOList;
    }

    /**
     * @param rscTenderDetailWorkUnitDTOList the rscTenderDetailWorkUnitDTOList
     * to set
     */
    public void setRscTenderDetailWorkUnitDTOList(List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTOList) {
        this.rscTenderDetailWorkUnitDTOList = rscTenderDetailWorkUnitDTOList;
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
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the contractorName
     */
    public String getContractorName() {
        return contractorName;
    }

    /**
     * @param contractorName the contractorName to set
     */
    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    /**
     * @return the totalValue
     */
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the totalAccural
     */
    public BigDecimal getTotalAccural() {
        return totalAccural;
    }

    /**
     * @param totalAccural the totalAccural to set
     */
    public void setTotalAccural(BigDecimal totalAccural) {
        this.totalAccural = totalAccural;
    }

    /**
     * @return the totalDeduction
     */
    public BigDecimal getTotalDeduction() {
        return totalDeduction;
    }

    /**
     * @param totalDeduction the totalDeduction to set
     */
    public void setTotalDeduction(BigDecimal totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    /**
     * @return the rscTenderRscAccrualDeductionDTOAccruaList
     */
    public List<RscTenderRscAccrualDeductionDTO> getRscTenderRscAccrualDeductionDTOAccruaList() {
        if(rscTenderRscAccrualDeductionDTOAccruaList == null){
            rscTenderRscAccrualDeductionDTOAccruaList = new ArrayList<>();
        }
        return rscTenderRscAccrualDeductionDTOAccruaList;
    }

    /**
     * @param rscTenderRscAccrualDeductionDTOAccruaList the rscTenderRscAccrualDeductionDTOAccruaList to set
     */
    public void setRscTenderRscAccrualDeductionDTOAccruaList(List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTOAccruaList) {
        this.rscTenderRscAccrualDeductionDTOAccruaList = rscTenderRscAccrualDeductionDTOAccruaList;
    }

    /**
     * @return the rscTenderRscAccrualDeductionDTODeductionList
     */
    public List<RscTenderRscAccrualDeductionDTO> getRscTenderRscAccrualDeductionDTODeductionList() {
        if(rscTenderRscAccrualDeductionDTODeductionList == null){
            rscTenderRscAccrualDeductionDTODeductionList = new ArrayList<>();
        }
        return rscTenderRscAccrualDeductionDTODeductionList;
    }

    /**
     * @param rscTenderRscAccrualDeductionDTODeductionList the rscTenderRscAccrualDeductionDTODeductionList to set
     */
    public void setRscTenderRscAccrualDeductionDTODeductionList(List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTODeductionList) {
        this.rscTenderRscAccrualDeductionDTODeductionList = rscTenderRscAccrualDeductionDTODeductionList;
    }

    /**
     * @return the rscTenderDetailWorkUnitDTODeletedList
     */
    public List<RscTenderDetailWorkUnitDTO> getRscTenderDetailWorkUnitDTODeletedList() {
        if(rscTenderDetailWorkUnitDTODeletedList == null){
            rscTenderDetailWorkUnitDTODeletedList = new ArrayList<>();
        }
        return rscTenderDetailWorkUnitDTODeletedList;
    }

    /**
     * @param rscTenderDetailWorkUnitDTODeletedList the rscTenderDetailWorkUnitDTODeletedList to set
     */
    public void setRscTenderDetailWorkUnitDTODeletedList(List<RscTenderDetailWorkUnitDTO> rscTenderDetailWorkUnitDTODeletedList) {
        this.rscTenderDetailWorkUnitDTODeletedList = rscTenderDetailWorkUnitDTODeletedList;
    }

    /**
     * @return the rscTenderRscAccrualDeductionDTODeletedList
     */
    public List<RscTenderRscAccrualDeductionDTO> getRscTenderRscAccrualDeductionDTODeletedList() {
        if(rscTenderRscAccrualDeductionDTODeletedList == null){
            rscTenderRscAccrualDeductionDTODeletedList = new ArrayList<>();
        }
        return rscTenderRscAccrualDeductionDTODeletedList;
    }

    /**
     * @param rscTenderRscAccrualDeductionDTODeletedList the rscTenderRscAccrualDeductionDTODeletedList to set
     */
    public void setRscTenderRscAccrualDeductionDTODeletedList(List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTODeletedList) {
        this.rscTenderRscAccrualDeductionDTODeletedList = rscTenderRscAccrualDeductionDTODeletedList;
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
