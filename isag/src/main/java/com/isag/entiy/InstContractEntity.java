/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.GlBank;
import com.isag.entity.InstProjectGuide;
import com.isag.views.InstPurchaseInvoiceContractDifrranceView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 *
 * @author ahmed
 */
public class InstContractEntity {

    private Integer id;
    private Integer serial;
    private InvOrganizationSiteDTO invOrganizationSite;
    private InstProjectGuide instProjectGuide;
    private GlBank glBank;
    private Date date;
    private Date startInstallment;
    private BigDecimal advancePayment;
    private BigDecimal deliveryPayment;
    private BigDecimal installmentValue;
    private BigDecimal installmentAmount;
    private BigDecimal totalValue;
    private BigDecimal murabahaRate;
    private BigDecimal amount;
    private Integer daysNumber;
    private Integer installmentNumber;
    private Integer branchId;
    private Date creationDate;
    private Date modificationDate;
    private Integer createdBy;
    private Integer modifiedBy;
    private Integer companyId;
    private String remark;
    private InstPurchaseInvoiceContractDifrranceView invItemId;
    private BigDecimal orignelAmount;
    private Boolean disabled;
    private Boolean cancel;
    private Integer postFlag;
    private BigDecimal MaintenanceDeposit;
    private Integer paymentMethod;

    private BigDecimal taxValue;
    private String detailsValue;
    private Boolean profitStatus;
    private BigDecimal priceWithoutTaxValue;
    private BigDecimal amountValue;
    private BigDecimal netValue;
    private BigDecimal priceWithTaxValue;    
    private BigDecimal totalValue2;
    private BigDecimal cartonNumberValue;
    private BigDecimal pricePerPieceValue;
    
    private Date startDate;
    private Date endDate;
    
    public InstContractEntity() {
    }


    public InstContractEntity(InstContractEntity instContractEntity) {
        this.id = instContractEntity.id;
        this.serial = instContractEntity.serial;
        this.invOrganizationSite = instContractEntity.invOrganizationSite;
        this.instProjectGuide = instContractEntity.instProjectGuide;
        this.glBank = instContractEntity.glBank;
        this.date = instContractEntity.date;
        this.startInstallment = instContractEntity.startInstallment;
        this.advancePayment = instContractEntity.advancePayment;
        this.deliveryPayment = instContractEntity.deliveryPayment;
        this.installmentValue = instContractEntity.installmentValue;
        this.totalValue = instContractEntity.totalValue;
        this.murabahaRate = instContractEntity.murabahaRate;
        this.amount = instContractEntity.amount;
        this.daysNumber = instContractEntity.daysNumber;
        this.installmentNumber = instContractEntity.installmentNumber;
        this.branchId = instContractEntity.branchId;
        this.creationDate = instContractEntity.creationDate;
        this.modificationDate = instContractEntity.modificationDate;
        this.createdBy = instContractEntity.createdBy;
        this.modifiedBy = instContractEntity.modifiedBy;
        this.companyId = instContractEntity.companyId;
        this.remark = instContractEntity.remark;
        this.invItemId = instContractEntity.invItemId;
        this.orignelAmount = instContractEntity.orignelAmount;
        this.disabled = instContractEntity.disabled;
        this.cancel = instContractEntity.cancel;
        this.postFlag = instContractEntity.postFlag;
        this.MaintenanceDeposit = instContractEntity.MaintenanceDeposit;
        this.paymentMethod = instContractEntity.paymentMethod;
        this.taxValue = instContractEntity.taxValue;
        this.profitStatus = instContractEntity.profitStatus;
        this.netValue = instContractEntity.netValue;
       
    }

       
    /**
     * @return the invOrganizationSite
     */
    public InvOrganizationSiteDTO getInvOrganizationSite() {
        return invOrganizationSite;
    }

    /**
     * @param invOrganizationSite the invOrganizationSite to set
     */
    public void setInvOrganizationSite(InvOrganizationSiteDTO invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
    }

    /**
     * @return the instProjectGuide
     */
    public InstProjectGuide getInstProjectGuide() {
        return instProjectGuide;
    }

    /**
     * @param instProjectGuide the instProjectGuide to set
     */
    public void setInstProjectGuide(InstProjectGuide instProjectGuide) {
        this.instProjectGuide = instProjectGuide;
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
     * @return the glBank
     */
    public GlBank getGlBank() {
        return glBank;
    }

    /**
     * @param glBank the glBank to set
     */
    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
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
     * @return the murabahaRate
     */
    public BigDecimal getMurabahaRate() {
        return murabahaRate;
    }

    /**
     * @param murabahaRate the murabahaRate to set
     */
    public void setMurabahaRate(BigDecimal murabahaRate) {
        this.murabahaRate = murabahaRate;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        if(amount == null){
            amount = BigDecimal.ONE;
        }
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the invItemId
     */
    public InstPurchaseInvoiceContractDifrranceView getInvItemId() {
        return invItemId;
    }

    /**
     * @param invItemId the invItemId to set
     */
    public void setInvItemId(InstPurchaseInvoiceContractDifrranceView invItemId) {
        this.invItemId = invItemId;
    }

    /**
     * @return the orignelAmount
     */
    public BigDecimal getOrignelAmount() {
        return orignelAmount;
    }

    /**
     * @param orignelAmount the orignelAmount to set
     */
    public void setOrignelAmount(BigDecimal orignelAmount) {
        this.orignelAmount = orignelAmount;
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
     * @return the cancel
     */
    public Boolean getCancel() {
        return cancel;
    }

    /**
     * @param cancel the cancel to set
     */
    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
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
     * @return the MaintenanceDeposit
     */
    public BigDecimal getMaintenanceDeposit() {
        return MaintenanceDeposit;
    }

    /**
     * @param MaintenanceDeposit the MaintenanceDeposit to set
     */
    public void setMaintenanceDeposit(BigDecimal MaintenanceDeposit) {
        this.MaintenanceDeposit = MaintenanceDeposit;
    }

    /**
     * @return the paymentMethod
     */
    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod the paymentMethod to set
     */
    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return the taxValue
     */
    public BigDecimal getTaxValue() { 
        return taxValue ;
    }

    /**
     * @param taxValue the taxValue to set
     */
    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

    /**
     * @return the detailsValue
     */
    public String getDetailsValue() {
        if (profitStatus) {
            detailsValue = "هامش ارباح بيع بطاقات بالتقسيط مسبقة الدفع " + getInvItemId().getName();
        } else {
            detailsValue = "تكلفه شراء بطاقات مسبقة الدفع " + getInvItemId().getName();
        }
        return detailsValue;
    }

    /**
     * @param detailsValue the detailsValue to set
     */
    public void setDetailsValue(String detailsValue) {
        this.detailsValue = detailsValue;
    }

    /**
     * @return the priceWithoutTaxValue
     */
    public BigDecimal getPriceWithoutTaxValue() {
        //return priceWithoutTaxValue = getTotalValue();
        return priceWithoutTaxValue;
    }

    /**
     * @param priceWithoutTaxValue the priceWithoutTaxValue to set
     */
    public void setPriceWithoutTaxValue(BigDecimal priceWithoutTaxValue) {
        this.priceWithoutTaxValue = priceWithoutTaxValue;
    }

    /**
     * @return the profitStatus
     */
    public Boolean getProfitStatus() {
        return profitStatus;
    }

    /**
     * @param profitStatus the profitStatus to set
     */
    public void setProfitStatus(Boolean profitStatus) {
        this.profitStatus = profitStatus;
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
    
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InstContractEntity)) {
            return false;
        }
        InstContractEntity other = (InstContractEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial != null ? serial.toString() : "";
    }

    /**
     * @return the installmentAmount
     */
    public BigDecimal getInstallmentAmount() {
        if(installmentValue != null && installmentNumber != null){
         installmentAmount = installmentValue.divide(BigDecimal.valueOf(installmentNumber.longValue()),2,RoundingMode.UP);
        }
        return installmentAmount;
    }

    /**
     * @param installmentAmount the installmentAmount to set
     */
    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    /**
     * @return the netValue
     */
    public BigDecimal getNetValue() {
        return netValue;
    }

    /**
     * @param netValue the netValue to set
     */
    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    /**
     * @return the priceWithTaxValue
     */
    public BigDecimal getPriceWithTaxValue() {
        return priceWithTaxValue;
    }

    /**
     * @param priceWithTaxValue the priceWithTaxValue to set
     */
    public void setPriceWithTaxValue(BigDecimal priceWithTaxValue) {
        this.priceWithTaxValue = priceWithTaxValue;
    }

    /**
     * @return the totalValue2
     */
    public BigDecimal getTotalValue2() {
        return totalValue2;
    }

    /**
     * @param totalValue2 the totalValue2 to set
     */
    public void setTotalValue2(BigDecimal totalValue2) {
        this.totalValue2 = totalValue2;
    }

    /**
     * @return the cartonNumberValue
     */
    public BigDecimal getCartonNumberValue() {
        return cartonNumberValue;
    }

    /**
     * @param cartonNumberValue the cartonNumberValue to set
     */
    public void setCartonNumberValue(BigDecimal cartonNumberValue) {
        this.cartonNumberValue = cartonNumberValue;
    }

    /**
     * @return the pricePerPieceValue
     */
    public BigDecimal getPricePerPieceValue() {
        return pricePerPieceValue;
    }

    /**
     * @param pricePerPieceValue the pricePerPieceValue to set
     */
    public void setPricePerPieceValue(BigDecimal pricePerPieceValue) {
        this.pricePerPieceValue = pricePerPieceValue;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
