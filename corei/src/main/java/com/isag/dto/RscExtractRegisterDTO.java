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
 * @author ahmed
 */
public class RscExtractRegisterDTO extends BaseEntityDTO {

    private Integer serial;

    private Date date;

    private Date receivedDate;

    private Date clientAcceptedDate;

    private BigDecimal netExtractPrice;

    private BigDecimal netOldExtracts;

    private Boolean valid;

    private Boolean closed;

    private String docNumber;

    private String remarks;

    private Date profissionaiAcceptedDate;

    private BigDecimal remain;

    private RscContractRegisterDTO contractId;

    private RscRestrictionQuantitiesDTO restrictionId;
    
    private List<RscRestrictionQuantitiesDTO> restrictionIdList;

    private RscTenderDTO tenderId;

    private RscExtractRegisterDTO oldExtract;

    private InvOrganizationSiteDTO siteId;

    private List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailDTOList;

    private List<RscExtractAccuralDeductionDTO> rscExtractAccuralList;

    private List<RscExtractAccuralDeductionDTO> rscExtractDeductionList;

    private List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailWorkUnitDeletedDTOList;
    private List<RscExtractAccuralDeductionDTO> rscExtractAccrualDeletedDTOList;
    private List<RscExtractAccuralDeductionDTO> rscExtractDeductionDeletedDTOList;

    private BigDecimal totalDetailNet;

    private BigDecimal netcost = BigDecimal.ZERO;

    private BigDecimal totalAccrualNetpercentage = BigDecimal.ZERO;
    private BigDecimal totalAccrualNetvalue = BigDecimal.ZERO;
    private BigDecimal totalDeductionNetpercentage = BigDecimal.ZERO;
    private BigDecimal totalDeductionNetvalue = BigDecimal.ZERO;
    private BigDecimal totalFinal = BigDecimal.ZERO;
    private BigDecimal netExtract;
    private InstProjectGuideDTO instProjectGuideDTO;
    private InvOrganizationSiteDTO customerId;
    private BigDecimal percentage;
    private BigDecimal totalBeforePercentage;
    private Integer postFlag;
    private String uri;
    private Boolean rowExpansion;
    private BigDecimal totalPercentage;
     private InvInventoryDTO invInventoryId;

    public RscExtractRegisterDTO() {
    }

    public RscExtractRegisterDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getClientAcceptedDate() {
        return clientAcceptedDate;
    }

    public void setClientAcceptedDate(Date clientAcceptedDate) {
        this.clientAcceptedDate = clientAcceptedDate;
    }

    public BigDecimal getNetExtractPrice() {
        if(netExtractPrice==null){
            netExtractPrice = BigDecimal.ZERO;
        }
        return netExtractPrice;
    }

    public void setNetExtractPrice(BigDecimal netExtractPrice) {
        this.netExtractPrice = netExtractPrice;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getProfissionaiAcceptedDate() {
        return profissionaiAcceptedDate;
    }

    public void setProfissionaiAcceptedDate(Date profissionaiAcceptedDate) {
        this.profissionaiAcceptedDate = profissionaiAcceptedDate;
    }

    public RscExtractRegisterDTO getOldExtract() {
        return oldExtract;
    }

    public void setOldExtract(RscExtractRegisterDTO oldExtract) {
        this.oldExtract = oldExtract;
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
     * @return the siteId
     */
    public InvOrganizationSiteDTO getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(InvOrganizationSiteDTO siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
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
     * @return the RscExtractRegisterDetailDTOList
     */
    public List<RscExtractRegisterDetailDTO> getRscExtractRegisterDetailDTOList() {
        if (rscExtractRegisterDetailDTOList == null || rscExtractRegisterDetailDTOList.isEmpty()) {

            rscExtractRegisterDetailDTOList = new ArrayList<>();

        }

        return rscExtractRegisterDetailDTOList;
    }

    /**
     * @param RscExtractRegisterDetailDTOList the
     * RscExtractRegisterDetailDTOList to set
     */
    public void setRscExtractRegisterDetailDTOList(List<RscExtractRegisterDetailDTO> RscExtractRegisterDetailDTOList) {
        this.rscExtractRegisterDetailDTOList = RscExtractRegisterDetailDTOList;
    }

    /**
     * @return the rscExtractAccuralList
     */
    public List<RscExtractAccuralDeductionDTO> getRscExtractAccuralList() {
        if (rscExtractAccuralList == null || rscExtractAccuralList.isEmpty()) {
            rscExtractAccuralList = new ArrayList<>();
        }
        return rscExtractAccuralList;
    }

    /**
     * @param rscExtractAccuralList the rscExtractAccuralList to set
     */
    public void setRscExtractAccuralList(List<RscExtractAccuralDeductionDTO> rscExtractAccuralList) {
        this.rscExtractAccuralList = rscExtractAccuralList;
    }

    /**
     * @return the rscExtractDeductionList
     */
    public List<RscExtractAccuralDeductionDTO> getRscExtractDeductionList() {
        if (rscExtractDeductionList == null || rscExtractDeductionList.isEmpty()) {
            rscExtractDeductionList = new ArrayList<>();
        }
        return rscExtractDeductionList;
    }

    /**
     * @param rscExtractDeductionList the rscExtractDeductionList to set
     */
    public void setRscExtractDeductionList(List<RscExtractAccuralDeductionDTO> rscExtractDeductionList) {
        this.rscExtractDeductionList = rscExtractDeductionList;
    }

    /**
     * @return the rscExtractRegisterDetailWorkUnitDeletedDTOList
     */
    public List<RscExtractRegisterDetailDTO> getRscExtractRegisterDetailWorkUnitDeletedDTOList() {
        return rscExtractRegisterDetailWorkUnitDeletedDTOList;
    }

    /**
     * @param rscExtractRegisterDetailWorkUnitDeletedDTOList the
     * rscExtractRegisterDetailWorkUnitDeletedDTOList to set
     */
    public void setRscExtractRegisterDetailWorkUnitDeletedDTOList(List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailWorkUnitDeletedDTOList) {
        this.rscExtractRegisterDetailWorkUnitDeletedDTOList = rscExtractRegisterDetailWorkUnitDeletedDTOList;
    }

    /**
     * @return the rscExtractAccrualDeletedDTOList
     */
    public List<RscExtractAccuralDeductionDTO> getRscExtractAccrualDeletedDTOList() {
        return rscExtractAccrualDeletedDTOList;
    }

    /**
     * @param rscExtractAccrualDeletedDTOList the
     * rscExtractAccrualDeletedDTOList to set
     */
    public void setRscExtractAccrualDeletedDTOList(List<RscExtractAccuralDeductionDTO> rscExtractAccrualDeletedDTOList) {
        this.rscExtractAccrualDeletedDTOList = rscExtractAccrualDeletedDTOList;
    }

    /**
     * @return the rscExtractDeductionDeletedDTOList
     */
    public List<RscExtractAccuralDeductionDTO> getRscExtractDeductionDeletedDTOList() {
        return rscExtractDeductionDeletedDTOList;
    }

    /**
     * @param rscExtractDeductionDeletedDTOList the
     * rscExtractDeductionDeletedDTOList to set
     */
    public void setRscExtractDeductionDeletedDTOList(List<RscExtractAccuralDeductionDTO> rscExtractDeductionDeletedDTOList) {
        this.rscExtractDeductionDeletedDTOList = rscExtractDeductionDeletedDTOList;
    }

    /**
     * @return the totalDetailNet
     */
    public BigDecimal getTotalDetailNet() {
        if (totalDetailNet == null) {
            totalDetailNet = BigDecimal.ZERO;
        }
        return totalDetailNet;
    }

    /**
     * @param totalDetailNet the totalDetailNet to set
     */
    public void setTotalDetailNet(BigDecimal totalDetailNet) {
        this.totalDetailNet = totalDetailNet;
    }

    /**
     * @return the netcost
     */
    public BigDecimal getNetcost() {
        return netcost;
    }

    /**
     * @param netcost the netcost to set
     */
    public void setNetcost(BigDecimal netcost) {
        this.netcost = netcost;
    }

    /**
     * @return the totalAccrualNetpercentage
     */
    public BigDecimal getTotalAccrualNetpercentage() {
        return totalAccrualNetpercentage;
    }

    /**
     * @param totalAccrualNetpercentage the totalAccrualNetpercentage to set
     */
    public void setTotalAccrualNetpercentage(BigDecimal totalAccrualNetpercentage) {
        this.totalAccrualNetpercentage = totalAccrualNetpercentage;
    }

    /**
     * @return the totalAccrualNetvalue
     */
    public BigDecimal getTotalAccrualNetvalue() {
        if (totalAccrualNetvalue == null) {
            totalAccrualNetvalue = BigDecimal.ZERO;
        }
        return totalAccrualNetvalue;
    }

    /**
     * @param totalAccrualNetvalue the totalAccrualNetvalue to set
     */
    public void setTotalAccrualNetvalue(BigDecimal totalAccrualNetvalue) {
        this.totalAccrualNetvalue = totalAccrualNetvalue;
    }

    /**
     * @return the totalDeductionNetpercentage
     */
    public BigDecimal getTotalDeductionNetpercentage() {
        return totalDeductionNetpercentage;
    }

    /**
     * @param totalDeductionNetpercentage the totalDeductionNetpercentage to set
     */
    public void setTotalDeductionNetpercentage(BigDecimal totalDeductionNetpercentage) {
        this.totalDeductionNetpercentage = totalDeductionNetpercentage;
    }

    /**
     * @return the totalDeductionNetvalue
     */
    public BigDecimal getTotalDeductionNetvalue() {
        if (totalDeductionNetvalue == null) {
            totalDeductionNetvalue = BigDecimal.ZERO;
        }
        return totalDeductionNetvalue;
    }

    /**
     * @param totalDeductionNetvalue the totalDeductionNetvalue to set
     */
    public void setTotalDeductionNetvalue(BigDecimal totalDeductionNetvalue) {
        this.totalDeductionNetvalue = totalDeductionNetvalue;
    }

    /**
     * @return the totalFinal
     */
    public BigDecimal getTotalFinal() {
        return totalFinal;
    }

    /**
     * @param totalFinal the totalFinal to set
     */
    public void setTotalFinal(BigDecimal totalFinal) {
        this.totalFinal = totalFinal;
    }

    /**
     * @return the netOldExtracts
     */
    public BigDecimal getNetOldExtracts() {
        return netOldExtracts;
    }

    /**
     * @param netOldExtracts the netOldExtracts to set
     */
    public void setNetOldExtracts(BigDecimal netOldExtracts) {
        this.netOldExtracts = netOldExtracts;
    }

    /**
     * @return the netExtract
     */
    public BigDecimal getNetExtract() {
        if (netExtract == null) {
            netExtract = BigDecimal.ZERO;
        }
        return netExtract;
    }

    /**
     * @param netExtract the netExtract to set
     */
    public void setNetExtract(BigDecimal netExtract) {
        this.netExtract = netExtract;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RscExtractRegisterDTO)) {
            return false;
        }
        RscExtractRegisterDTO other = (RscExtractRegisterDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getIndex() + "  ";
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
     * @return the percentage
     */
    public BigDecimal getPercentage() {
        if (percentage == null) {
            percentage = BigDecimal.ZERO;
        }
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
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
     * @return the totalBeforepercentage
     */
    public BigDecimal getTotalBeforePercentage() {
        return totalBeforePercentage;
    }

    /**
     * @param totalBeforePercentage the totalBeforePercentage to set
     */
    public void setTotalBeforePercentage(BigDecimal totalBeforePercentage) {
        this.totalBeforePercentage = totalBeforePercentage;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the rowExpansion
     */
    public Boolean getRowExpansion() {
        return rowExpansion;
    }

    /**
     * @param rowExpansion the rowExpansion to set
     */
    public void setRowExpansion(Boolean rowExpansion) {
        this.rowExpansion = rowExpansion;
    }

    /**
     * @return the totalPercentage
     */
    public BigDecimal getTotalPercentage() {
        return totalPercentage;
    }

    /**
     * @param totalPercentage the totalPercentage to set
     */
    public void setTotalPercentage(BigDecimal totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    /**
     * @return the invInventoryId
     */
    public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
    }
    
    public List<RscRestrictionQuantitiesDTO> getRestrictionIdList() {
        return restrictionIdList;
    }

    /**
     * @param restrictionIdList the restrictionIdList to set
     */
    public void setRestrictionIdList(List<RscRestrictionQuantitiesDTO> restrictionIdList) {
        this.restrictionIdList = restrictionIdList;
    }
}
