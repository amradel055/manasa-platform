/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mostafa
 */
public class InstContractDTO extends BaseEntityDTO {
    
    private Integer id;
    private InvOrganizationSiteDTO invOrganizationSiteDTO;
    private InstProjectGuideDTO instProjectGuideDTO;
    private GlBankDTO glBankDTO;
    private Date date;
    private Date startInstallment;
    private BigDecimal advancePayment;
    private BigDecimal deliveryPayment;
    private BigDecimal installmentValue;
    private BigDecimal totalValue;
    private BigDecimal finalValue;
    private BigDecimal amountValue;
    private BigDecimal murabahaValue;
    private Integer daysNumber;
    private Integer installmentNumber;
    private Integer branchId;
    private Date creationDate;
    private Date modificationDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer companyId;
    private String remark;
    private Integer serial;
    private InvItemDTO invItemId;
    private Boolean disabled;
    private List<InstContractProjectDTO> instContractProjectDTOList;
    private List<InstContractDetailDTO> instContractDetailDTOList;
    private List<InstProjectGuideDTO> instProjectGuideDTOList;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the invOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
        return invOrganizationSiteDTO;
    }

    /**
     * @param invOrganizationSiteDTO the invOrganizationSiteDTO to set
     */
    public void setInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        this.invOrganizationSiteDTO = invOrganizationSiteDTO;
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
     * @return the glBankDTO
     */
    public GlBankDTO getGlBankDTO() {
        return glBankDTO;
    }

    /**
     * @param glBankDTO the glBankDTO to set
     */
    public void setGlBankDTO(GlBankDTO glBankDTO) {
        this.glBankDTO = glBankDTO;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the startInstallment
     */
    public Date getStartInstallment() {
        return startInstallment;
    }

    /**
     * @param startInstallment the startInstallment to set
     */
    public void setStartInstallment(Date startInstallment) {
        this.startInstallment = startInstallment;
    }

    /**
     * @return the advancePayment
     */
    public BigDecimal getAdvancePayment() {
        return advancePayment;
    }

    /**
     * @param advancePayment the advancePayment to set
     */
    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = advancePayment;
    }

    /**
     * @return the deliveryPayment
     */
    public BigDecimal getDeliveryPayment() {
        return deliveryPayment;
    }

    /**
     * @param deliveryPayment the deliveryPayment to set
     */
    public void setDeliveryPayment(BigDecimal deliveryPayment) {
        this.deliveryPayment = deliveryPayment;
    }

    /**
     * @return the installmentValue
     */
    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    /**
     * @param installmentValue the installmentValue to set
     */
    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
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
     * @return the daysNumber
     */
    public Integer getDaysNumber() {
        return daysNumber;
    }

    /**
     * @param daysNumber the daysNumber to set
     */
    public void setDaysNumber(Integer daysNumber) {
        this.daysNumber = daysNumber;
    }

    /**
     * @return the installmentNumber
     */
    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    /**
     * @param installmentNumber the installmentNumber to set
     */
    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
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
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the modificationDate
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * @param modificationDate the modificationDate to set
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * @return the createdBy
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the modifiedBy
     */
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @param modifiedBy the modifiedBy to set
     */
    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * @return the finalValue
     */
    public BigDecimal getFinalValue() {
        return finalValue;
    }

    /**
     * @param finalValue the finalValue to set
     */
    public void setFinalValue(BigDecimal finalValue) {
        this.finalValue = finalValue;
    }

    /**
     * @return the amountValue
     */
    public BigDecimal getAmountValue() {
        return amountValue;
    }

    /**
     * @param amountValue the amountValue to set
     */
    public void setAmountValue(BigDecimal amountValue) {
        this.amountValue = amountValue;
    }

    /**
     * @return the murabahaValue
     */
    public BigDecimal getMurabahaValue() {
        return murabahaValue;
    }

    /**
     * @param murabahaValue the murabahaValue to set
     */
    public void setMurabahaValue(BigDecimal murabahaValue) {
        this.murabahaValue = murabahaValue;
    }

    /**
     * @return the invItemId
     */
    public InvItemDTO getInvItemId() {
        return invItemId;
    }

    /**
     * @param invItemId the invItemId to set
     */
    public void setInvItemId(InvItemDTO invItemId) {
        this.invItemId = invItemId;
    }

    /**
     * @return the disabled
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the instContractProjectDTOList
     */
    public List<InstContractProjectDTO> getInstContractProjectDTOList() {
        return instContractProjectDTOList;
    }

    /**
     * @param instContractProjectDTOList the instContractProjectDTOList to set
     */
    public void setInstContractProjectDTOList(List<InstContractProjectDTO> instContractProjectDTOList) {
        this.instContractProjectDTOList = instContractProjectDTOList;
    }

    /**
     * @return the instContractDetailDTOList
     */
    public List<InstContractDetailDTO> getInstContractDetailDTOList() {
        return instContractDetailDTOList;
    }

    /**
     * @param instContractDetailDTOList the instContractDetailDTOList to set
     */
    public void setInstContractDetailDTOList(List<InstContractDetailDTO> instContractDetailDTOList) {
        this.instContractDetailDTOList = instContractDetailDTOList;
    }

    /**
     * @return the instProjectGuideDTOList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideDTOList() {
        return instProjectGuideDTOList;
    }

    /**
     * @param instProjectGuideDTOList the instProjectGuideDTOList to set
     */
    public void setInstProjectGuideDTOList(List<InstProjectGuideDTO> instProjectGuideDTOList) {
        this.instProjectGuideDTOList = instProjectGuideDTOList;
    }
    
    
}
