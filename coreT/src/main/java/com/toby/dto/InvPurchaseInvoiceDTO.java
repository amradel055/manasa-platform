/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.define.CompanyActivityClassEnum;
import com.toby.define.ScreenNameClassEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amr
 */
public class InvPurchaseInvoiceDTO extends BaseEntityDTO {

    private CompanyActivityClassEnum companyActivity;
    private ScreenNameClassEnum ScreenName;
    private Date date;
    private Date dateHegry;
    private InvInventoryDTO gallaryId;
    private Integer pricetype;
    private BigDecimal rate;
    private Integer paymentType;
    private Integer status;
    private InvItemDTO invParentItem;
    private BigDecimal amountInvSupplier;
    private Date dueDate;
    private String supplierInvoiceNumber;
    private BigDecimal discount;
    private BigDecimal discountValue;
    private Integer discountType;
    private Integer postFlag;
    private Boolean taxflag;
    private Boolean taxdiscflag;
    private BigDecimal taxdiscvalue;
    private Boolean taxFlagFinal;
    private Boolean type;
    private String remarks;
    private BigDecimal extraCost;
    private GlAdminUnitDTO adminUnitId;
    private Integer accountId;
    private CostCenterDTO costCenterId;
    private CurrencyDTO currencyId;
    private InventoryDelegatorDTO invDelegatorId;
    private InvInventoryDTO invInventoryId;
    private InvOrganizationSiteDTO organizationSiteId;
    private GlBankDTO glBankId;
    private SymbolDTO documentTypeId;
    private Date supplierInvoiceDate;
    private Integer serial;
    private Integer serialTax;
    private InvDetailDTO invPurchaseInvoiceDetailsSelected;
    private InvDetailDTO invPurchaseInvoiceDetails1;
    private Integer dueperiod;

    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal totalWeight = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    private BigDecimal taxvalue = BigDecimal.ZERO;
    private BigDecimal totalWithTaxValue = BigDecimal.ZERO;
    private BigDecimal finalNet = BigDecimal.ZERO;
    private BigDecimal totalNetAfterDiscount = BigDecimal.ZERO;

    private InventorySetupDTO inventorySetupDTO;

    private List<InvDetailDTO> invPurchaseInvoiceDetailList;
    private List<InvDetailDTO> invPurchaseInvoiceDetailsDeletedList;

    ////// load Dailog //////
    private List<InvDailogueData> invDailogueDataList;
    private InvDailogueData invDailogeDataSelected;

    public InvPurchaseInvoiceDTO() {
    }

    public Date getDate() {
        if (date == null) {
            date = new Date();
        }
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getRate() {
        if (rate == null) {
            rate = BigDecimal.ONE;
        }
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmountInvSupplier() {
        return amountInvSupplier;
    }

    public void setAmountInvSupplier(BigDecimal amountInvSupplier) {
        this.amountInvSupplier = amountInvSupplier;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getSupplierInvoiceNumber() {
        return supplierInvoiceNumber;
    }

    public void setSupplierInvoiceNumber(String supplierInvoiceNumber) {
        this.supplierInvoiceNumber = supplierInvoiceNumber;
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
        if (discountType == null) {
            discountType = 0;
        }
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public GlAdminUnitDTO getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(GlAdminUnitDTO adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public CostCenterDTO getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(CostCenterDTO costCenterId) {
        this.costCenterId = costCenterId;
    }

    public CurrencyDTO getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(CurrencyDTO currencyId) {
        this.currencyId = currencyId;
    }

    public InventoryDelegatorDTO getInvDelegatorId() {
        return invDelegatorId;
    }

    public void setInvDelegatorId(InventoryDelegatorDTO invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
    }

    public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public GlBankDTO getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(GlBankDTO glBankId) {
        this.glBankId = glBankId;
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
        if (!(object instanceof InvPurchaseInvoiceDTO)) {
            return false;
        }
        InvPurchaseInvoiceDTO other = (InvPurchaseInvoiceDTO) object;
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
     * @return the extraCost
     */
    public BigDecimal getExtraCost() {
        return extraCost;
    }

    /**
     * @param extraCost the extraCost to set
     */
    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public SymbolDTO getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(SymbolDTO documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Boolean getTaxflag() {
        return taxflag;
    }

    public void setTaxflag(Boolean taxflag) {
        this.taxflag = taxflag;
    }

    public Date getSupplierInvoiceDate() {
        if (supplierInvoiceDate == null) {
            supplierInvoiceDate = new Date();
        }
        return supplierInvoiceDate;
    }

    public void setSupplierInvoiceDate(Date supplierInvoiceDate) {
        this.supplierInvoiceDate = supplierInvoiceDate;
    }

    /**
     * @return the serialTax
     */
    public Integer getSerialTax() {
        return serialTax;
    }

    /**
     * @param serialTax the serialTax to set
     */
    public void setSerialTax(Integer serialTax) {
        this.serialTax = serialTax;
    }

    /**
     * @return the taxFlagFinal
     */
    public Boolean getTaxFlagFinal() {
        return taxFlagFinal;
    }

    /**
     * @param taxFlagFinal the taxFlagFinal to set
     */
    public void setTaxFlagFinal(Boolean taxFlagFinal) {
        this.taxFlagFinal = taxFlagFinal;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the taxdiscflag
     */
    public Boolean getTaxdiscflag() {
        return taxdiscflag;
    }

    /**
     * @param taxdiscflag the taxdiscflag to set
     */
    public void setTaxdiscflag(Boolean taxdiscflag) {
        this.taxdiscflag = taxdiscflag;
    }

    /**
     * @return the taxdiscvalue
     */
    public BigDecimal getTaxdiscvalue() {
        return taxdiscvalue;
    }

    /**
     * @param taxdiscvalue the taxdiscvalue to set
     */
    public void setTaxdiscvalue(BigDecimal taxdiscvalue) {
        this.taxdiscvalue = taxdiscvalue;
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
     * @return the invPurchaseInvoiceDetailsDeletedList
     */
    public List<InvDetailDTO> getInvPurchaseInvoiceDetailsDeletedList() {
        return invPurchaseInvoiceDetailsDeletedList;
    }

    /**
     * @param invPurchaseInvoiceDetailsDeletedList the
     * invPurchaseInvoiceDetailsDeletedList to set
     */
    public void setInvPurchaseInvoiceDetailsDeletedList(List<InvDetailDTO> invPurchaseInvoiceDetailsDeletedList) {
        this.invPurchaseInvoiceDetailsDeletedList = invPurchaseInvoiceDetailsDeletedList;
    }

    /**
     * @return the totalQuatity
     */
    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    /**
     * @param totalQuatity the totalQuatity to set
     */
    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    /**
     * @return the totalWeight
     */
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    /**
     * @param totalWeight the totalWeight to set
     */
    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    /**
     * @return the totalNet
     */
    public BigDecimal getTotalNet() {
        return totalNet;
    }

    /**
     * @param totalNet the totalNet to set
     */
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    /**
     * @return the taxvalue
     */
    public BigDecimal getTaxvalue() {
        return taxvalue;
    }

    /**
     * @param taxvalue the taxvalue to set
     */
    public void setTaxvalue(BigDecimal taxvalue) {
        this.taxvalue = taxvalue;
    }

    /**
     * @return the totalWithTaxValue
     */
    public BigDecimal getTotalWithTaxValue() {
        return totalWithTaxValue;
    }

    /**
     * @param totalWithTaxValue the totalWithTaxValue to set
     */
    public void setTotalWithTaxValue(BigDecimal totalWithTaxValue) {
        this.totalWithTaxValue = totalWithTaxValue;
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
     * @return the companyActivity
     */
    public CompanyActivityClassEnum getCompanyActivity() {
        return companyActivity;
    }

    /**
     * @param companyActivity the companyActivity to set
     */
    public void setCompanyActivity(CompanyActivityClassEnum companyActivity) {
        this.companyActivity = companyActivity;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return the invPurchaseInvoiceDetailsSelected
     */
    public InvDetailDTO getInvPurchaseInvoiceDetailsSelected() {

        return invPurchaseInvoiceDetailsSelected;
    }

    /**
     * @param invPurchaseInvoiceDetailsSelected the
     * invPurchaseInvoiceDetailsSelected to set
     */
    public void setInvPurchaseInvoiceDetailsSelected(InvDetailDTO invPurchaseInvoiceDetailsSelected) {
        this.invPurchaseInvoiceDetailsSelected = invPurchaseInvoiceDetailsSelected;
    }

    /**
     * @return the invPurchaseInvoiceDetailList
     */
    public List<InvDetailDTO> getInvPurchaseInvoiceDetailList() {
        if (invPurchaseInvoiceDetailList == null) {
            invPurchaseInvoiceDetailList = new ArrayList<>();

        }
        return invPurchaseInvoiceDetailList;
    }

    /**
     * @param invPurchaseInvoiceDetailList the invPurchaseInvoiceDetailList to
     * set
     */
    public void setInvPurchaseInvoiceDetailList(List<InvDetailDTO> invPurchaseInvoiceDetailList) {
        this.invPurchaseInvoiceDetailList = invPurchaseInvoiceDetailList;
    }

    /**
     * @return the ScreenName
     */
    public ScreenNameClassEnum getScreenName() {
        return ScreenName;
    }

    /**
     * @param ScreenName the ScreenName to set
     */
    public void setScreenName(ScreenNameClassEnum ScreenName) {
        this.ScreenName = ScreenName;
    }

    /**
     * @return the pricetype
     */
    public Integer getPricetype() {
        return pricetype;
    }

    /**
     * @param pricetype the pricetype to set
     */
    public void setPricetype(Integer pricetype) {
        this.pricetype = pricetype;
    }

    /**
     * @return the invDailogeDataSelected
     */
    public InvDailogueData getInvDailogeDataSelected() {
        return invDailogeDataSelected;
    }

    /**
     * @param invDailogeDataSelected the invDailogeDataSelected to set
     */
    public void setInvDailogeDataSelected(InvDailogueData invDailogeDataSelected) {
        this.invDailogeDataSelected = invDailogeDataSelected;
    }

    /**
     * @return the inventorySetupDTO
     */
    public InventorySetupDTO getInventorySetupDTO() {
        if (inventorySetupDTO == null) {
            inventorySetupDTO = new InventorySetupDTO();
        }
        return inventorySetupDTO;
    }

    /**
     * @param inventorySetupDTO the inventorySetupDTO to set
     */
    public void setInventorySetupDTO(InventorySetupDTO inventorySetupDTO) {
        this.inventorySetupDTO = inventorySetupDTO;
    }

    /**
     * @return the dueperiod
     */
    public Integer getDueperiod() {
        return dueperiod;
    }

    /**
     * @param dueperiod the dueperiod to set
     */
    public void setDueperiod(Integer dueperiod) {
        this.dueperiod = dueperiod;
    }

    /**
     * @return the gallaryId
     */
    public InvInventoryDTO getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(InvInventoryDTO gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the invParentItem
     */
    public InvItemDTO getInvParentItem() {
        return invParentItem;
    }

    /**
     * @param invParentItem the invParentItem to set
     */
    public void setInvParentItem(InvItemDTO invParentItem) {
        this.invParentItem = invParentItem;
    }

    /**
     * @return the invPurchaseInvoiceDetails1
     */
    public InvDetailDTO getInvPurchaseInvoiceDetails1() {
        if (invPurchaseInvoiceDetails1 == null) {
            invPurchaseInvoiceDetails1 = new InvDetailDTO();
        }
        return invPurchaseInvoiceDetails1;
    }

    /**
     * @param invPurchaseInvoiceDetails1 the invPurchaseInvoiceDetails1 to set
     */
    public void setInvPurchaseInvoiceDetails1(InvDetailDTO invPurchaseInvoiceDetails1) {
        this.invPurchaseInvoiceDetails1 = invPurchaseInvoiceDetails1;
    }

    /**
     * @return the dateHegry
     */
    public Date getDateHegry() {
        return dateHegry;
    }

    /**
     * @param dateHegry the dateHegry to set
     */
    public void setDateHegry(Date dateHegry) {
        this.dateHegry = dateHegry;
    }

}
