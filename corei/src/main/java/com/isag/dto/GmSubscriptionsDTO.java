/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.ReportManagement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author IT Shop
 */
public class GmSubscriptionsDTO extends BaseEntityDTO {
        private Boolean barcodeSelect;


        
    private InvOrganizationSiteDTO organizationSiteId;
    private BigDecimal customerBalance;
    private InvInventoryDTO invInventoryId;
    private Integer paymentType;
    private Date date;
    private ReportManagement reportManagement;
    private Date endDate;
    private Date startDate;
    private BigDecimal discount;
    private Integer discountType;
    private int postFlag;
    private Integer type;
    private Integer accountId;
    private String remarks;
    private BigDecimal extraCost;
    private Integer serial;
    private Boolean taxFlag;
    private Integer serialTax;
    private Boolean taxFlagFinal;
    private Integer generalJournalId;
    private Boolean status;
    private Boolean taxDiscFlag;
    private BigDecimal taxDiscValue;
    private BigDecimal payCash;
    private BigDecimal payNet;
    private Integer revision;
    private GlBankDTO glBankId;

    private Boolean isEdit;
    private BigDecimal payed;
    private BigDecimal remain;
    private BigDecimal totalNet;
    private BigDecimal totalBeforeDiscount;
    private BigDecimal totalNetAfterDiscount;
    private BigDecimal taxvalue;
    private BigDecimal finalNet;
    private BigDecimal totalWithTaxValue;
    private BigDecimal discountValue;
     private BigDecimal value;
     private GmItemDTO gmItemDTO ;

    private Boolean showCustomerDate = Boolean.TRUE;
    private InventoryDelegatorDTO invDelegatorId;


    private List<GmSubscriptionsDetailDTO> gmSubscriptionsDetailList;
    private List<GmSubscriptionsDetailDTO> gmSubscriptionsDetailDeletedList;
    private GmSubscriptionsDetailDTO gmSubscriptionsDetailDTOSelected;
    
    private String statusName;
    private Integer remainPeriod;
    private Date dateAfterTagmeed;

    public GmSubscriptionsDTO() {
    }

    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getDiscount() {
        if (discount == null) {
            discount = BigDecimal.ZERO;
        }
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public int getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(int postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getExtraCost() {
          if(extraCost==null){
        extraCost= BigDecimal.ZERO;
        }
        return extraCost;
    }

    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Boolean getTaxFlag() {
        return taxFlag;
    }

    public void setTaxFlag(Boolean taxFlag) {
        this.taxFlag = taxFlag;
    }

    public Integer getSerialTax() {
        return serialTax;
    }

    public void setSerialTax(Integer serialTax) {
        this.serialTax = serialTax;
    }

    public Boolean getTaxFlagFinal() {
        return taxFlagFinal;
    }

    public void setTaxFlagFinal(Boolean taxFlagFinal) {
        this.taxFlagFinal = taxFlagFinal;
    }

    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    public Boolean getStatus() {
        if(status == null){
          status = false ;
        }
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getTaxDiscFlag() {
        return taxDiscFlag;
    }

    public void setTaxDiscFlag(Boolean taxDiscFlag) {
        this.taxDiscFlag = taxDiscFlag;
    }

    public BigDecimal getTaxDiscValue() {
           if(taxDiscValue==null){
        taxDiscValue= BigDecimal.ZERO;
        }
        return taxDiscValue;
    }

    public void setTaxDiscValue(BigDecimal taxDiscValue) {
        this.taxDiscValue = taxDiscValue;
    }

    public BigDecimal getPayCash() {
            if(payCash==null){
        payCash= BigDecimal.ZERO;
        }
        return payCash;
    }

    public void setPayCash(BigDecimal payCash) {
        this.payCash = payCash;
    }

    public BigDecimal getPayNet() {
              if(payNet==null){
        payNet= BigDecimal.ZERO;
        }
        return payNet;
    }

    public void setPayNet(BigDecimal payNet) {
        this.payNet = payNet;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
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
        if (!(object instanceof GmSubscriptionsDTO)) {
            return false;
        }
        GmSubscriptionsDTO other = (GmSubscriptionsDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GmSubscriptions[ id=" + id + " ]";
    }

    /**
     * @return the glBankId
     */
    public GlBankDTO getGlBankId() {
        return glBankId;
    }

    /**
     * @param glBankId the glBankId to set
     */
    public void setGlBankId(GlBankDTO glBankId) {
        this.glBankId = glBankId;
    }

    /**
     * @return the isEdit
     */
    public Boolean getIsEdit() {
        return isEdit;
    }

    /**
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * @return the payed
     */
    public BigDecimal getPayed() {
        if(payed==null){
        payed= BigDecimal.ZERO;
        }
        return payed;
    }

    /**
     * @param payed the payed to set
     */
    public void setPayed(BigDecimal payed) {
        this.payed = payed;
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
           if(remain==null){
        remain= BigDecimal.ZERO;
        }
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the gmSubscriptionsDetailList
     */
    public List<GmSubscriptionsDetailDTO> getGmSubscriptionsDetailList() {
        if (gmSubscriptionsDetailList == null) {
            gmSubscriptionsDetailList = new ArrayList<>();
        }
        return gmSubscriptionsDetailList;
    }

    /**
     * @param gmSubscriptionsDetailList the gmSubscriptionsDetailList to set
     */
    public void setGmSubscriptionsDetailList(List<GmSubscriptionsDetailDTO> gmSubscriptionsDetailList) {
        this.gmSubscriptionsDetailList = gmSubscriptionsDetailList;
    }

    /**
     * @return the gmSubscriptionsDetailDeletedList
     */
    public List<GmSubscriptionsDetailDTO> getGmSubscriptionsDetailDeletedList() {
        if (gmSubscriptionsDetailDeletedList == null) {
            gmSubscriptionsDetailDeletedList = new ArrayList<>();
        }
        return gmSubscriptionsDetailDeletedList;
    }

    /**
     * @param gmSubscriptionsDetailDeletedList the
     * gmSubscriptionsDetailDeletedList to set
     */
    public void setGmSubscriptionsDetailDeletedList(List<GmSubscriptionsDetailDTO> gmSubscriptionsDetailDeletedList) {
        this.gmSubscriptionsDetailDeletedList = gmSubscriptionsDetailDeletedList;
    }

    /**
     * @return the totalNet
     */
    public BigDecimal getTotalNet() {
        if (totalNet == null) {
            totalNet = BigDecimal.ZERO;
        }
        return totalNet;
    }

    /**
     * @param totalNet the totalNet to set
     */
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    /**
     * @return the totalBeforeDiscount
     */
    public BigDecimal getTotalBeforeDiscount() {
        if (totalBeforeDiscount == null) {
            totalBeforeDiscount = BigDecimal.ZERO;
        }
        return totalBeforeDiscount;
    }

    /**
     * @param totalBeforeDiscount the totalBeforeDiscount to set
     */
    public void setTotalBeforeDiscount(BigDecimal totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    /**
     * @return the totalNetAfterDiscount
     */
    public BigDecimal getTotalNetAfterDiscount() {
         if (totalNetAfterDiscount == null) {
            totalNetAfterDiscount = BigDecimal.ZERO;
             }
        return totalNetAfterDiscount;
    }

    /**
     * @param totalNetAfterDiscount the totalNetAfterDiscount to set
     */
    public void setTotalNetAfterDiscount(BigDecimal totalNetAfterDiscount) {
        this.totalNetAfterDiscount = totalNetAfterDiscount;
    }

    /**
     * @return the taxvalue
     */
    public BigDecimal getTaxvalue() {
        if (taxvalue == null) {
            taxvalue = BigDecimal.ZERO;
        }
        return taxvalue;
    }

    /**
     * @param taxvalue the taxvalue to set
     */
    public void setTaxvalue(BigDecimal taxvalue) {
        this.taxvalue = taxvalue;
    }

    /**
     * @return the finalNet
     */
    public BigDecimal getFinalNet() {
        if (finalNet == null) {
            finalNet = BigDecimal.ZERO;
        }
        return finalNet;
    }

    /**
     * @param finalNet the finalNet to set
     */
    public void setFinalNet(BigDecimal finalNet) {
        this.finalNet = finalNet;
    }

    /**
     * @return the totalWithTaxValue
     */
    public BigDecimal getTotalWithTaxValue() {
        if (totalWithTaxValue == null) {
            totalWithTaxValue = BigDecimal.ZERO;
        }
        return totalWithTaxValue;
    }

    /**
     * @param totalWithTaxValue the totalWithTaxValue to set
     */
    public void setTotalWithTaxValue(BigDecimal totalWithTaxValue) {
        this.totalWithTaxValue = totalWithTaxValue;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
          if (discountValue == null) {
            discountValue = BigDecimal.ZERO;
        }
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
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
     * @return the showCustomerDate
     */
    public Boolean getShowCustomerDate() {
        return showCustomerDate;
    }

    /**
     * @param showCustomerDate the showCustomerDate to set
     */
    public void setShowCustomerDate(Boolean showCustomerDate) {
        this.showCustomerDate = showCustomerDate;
    }

    /**
     * @return the gmSubscriptionsDetailDTOSelected
     */
    public GmSubscriptionsDetailDTO getGmSubscriptionsDetailDTOSelected() {
        return gmSubscriptionsDetailDTOSelected;
    }

    /**
     * @param gmSubscriptionsDetailDTOSelected the
     * gmSubscriptionsDetailDTOSelected to set
     */
    public void setGmSubscriptionsDetailDTOSelected(GmSubscriptionsDetailDTO gmSubscriptionsDetailDTOSelected) {
        if (gmSubscriptionsDetailDTOSelected == null) {
            gmSubscriptionsDetailDTOSelected = new GmSubscriptionsDetailDTO();
        }
        this.gmSubscriptionsDetailDTOSelected = gmSubscriptionsDetailDTOSelected;
    }
         /**
     * @return the barcodeSelect
     */
    public Boolean getBarcodeSelect() {
        return barcodeSelect;
    }

    /**
     * @param barcodeSelect the barcodeSelect to set
     */
    public void setBarcodeSelect(Boolean barcodeSelect) {
        this.barcodeSelect = barcodeSelect;
    }

    /**
     * @return the reportManagement
     */
    public ReportManagement getReportManagement() {
        return reportManagement;
    }

    /**
     * @param reportManagement the reportManagement to set
     */
    public void setReportManagement(ReportManagement reportManagement) {
        this.reportManagement = reportManagement;
    }

    /**
     * @return the invDelegatorId
     */
    public InventoryDelegatorDTO getInvDelegatorId() {
        return invDelegatorId;
    }

    /**
     * @param invDelegatorId the invDelegatorId to set
     */
    public void setInvDelegatorId(InventoryDelegatorDTO invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
    }
    
    /**
     * @return the customerBalance
     */
    public BigDecimal getCustomerBalance() {
        if(customerBalance == null){
            customerBalance = BigDecimal.ZERO;
        }
        return customerBalance;
    }

    /**
     * @param customerBalance the customerBalance to set
     */
    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }
     public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
    }
    
    /**
     * @return the paymentType
     */
    public Integer getPaymentType() {
        if (paymentType == null) {
            paymentType = 0;
        }
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the remainPeriod
     */
    public Integer getRemainPeriod() {
        return remainPeriod;
    }

    /**
     * @param remainPeriod the remainPeriod to set
     */
    public void setRemainPeriod(Integer remainPeriod) {
        this.remainPeriod = remainPeriod;
    }

    /**
     * @return the dateAfterTagmeed
     */
    public Date getDateAfterTagmeed() {
        return dateAfterTagmeed;
    }

    /**
     * @param dateAfterTagmeed the dateAfterTagmeed to set
     */
    public void setDateAfterTagmeed(Date dateAfterTagmeed) {
        this.dateAfterTagmeed = dateAfterTagmeed;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
    
        return value ;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the gmItemDTO
     */
    public GmItemDTO getGmItemDTO() {
        return gmItemDTO;
    }

    /**
     * @param gmItemDTO the gmItemDTO to set
     */
    public void setGmItemDTO(GmItemDTO gmItemDTO) {
        this.gmItemDTO = gmItemDTO;
    }
}
