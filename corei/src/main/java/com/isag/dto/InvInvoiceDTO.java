/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.define.CompanyActivityClassEnum;
import com.isag.define.ScreenNameClassEnum;
import com.isag.entity.ReportManagement;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amr
 */
public class InvInvoiceDTO extends BaseEntityDTO {

    private Boolean revision;
    private Integer typeActivity;
    private CompanyActivityClassEnum companyActivity;
    private ScreenNameClassEnum ScreenName;
    private Date date;
    private Date dateEndOffer;
    private BigDecimal rate;
    private Integer paymentType;
    private Integer status;
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
    private Integer type;
    private String remarks;
    private BigDecimal extraCost;
    private GlAdminUnitDTO adminUnitId;
    private Integer accountId;
    private CostCenterDTO costCenterId;
    private CurrencyDTO currencyId;
    private InventoryDelegatorDTO invDelegatorId;
    private String invInventoryNameFrom;
    private String invInventoryNameTo;
    private String invDelegatorName;
    private InvInventoryDTO invInventoryId;
    private InvInventoryDTO invInventoryIdTo;
    private String organizationSiteName;
    private InvOrganizationSiteDTO organizationSiteId;
    private InvOrganizationSiteDTO organizationSiteIdMain;
    private GlBankDTO glBankId;
    private SymbolDTO documentTypeId;
    private Date supplierInvoiceDate;
    private Integer serial;
    private Integer serialTax;
    private String address;
    private InvDetailDTO invPurchaseInvoiceDetailsSelected;
    private List<InvDetailDTO> invPurchaseInvoiceDetailListSelected;
    private InvMonthTargetDTO invMonthTargetDTO;

    private BigDecimal totalQuatityCost;
    private BigDecimal totalQuatity;
    private BigDecimal totalWeight;
    private BigDecimal totalNet;
    private BigDecimal taxvalue;
    private BigDecimal totalWithTaxValue;
    private BigDecimal finalNet;
    private BigDecimal totalNetAfterDiscount;
    private BigDecimal payed;
    private BigDecimal remain;
    private BigDecimal payCash;
    private BigDecimal payNet;
    private BigDecimal totalBeforeDiscount;
    private BigDecimal accountStatement;

    private InventorySetupDTO inventorySetupDTO;

    private List<InvDetailDTO> invPurchaseInvoiceDetailList;
    private List<InvDetailDTO> invStripDetailList;
    private List<InvDetailDTO> invPurchaseInvoiceDetailsDeletedList;
    private List<InvDetailDTO> invPurchaseInvoiceDetailBarcodeList;

    private Integer invPurchaseInvoiceId;
    private Integer invInvoiceSupplierNumber;
    private Integer invPurchaseOrderId;
    private Integer invAddingOrderId;
    private Integer invQotationId;
    private Integer loadedSerial;
    private Integer needLoadedSerial;
    private Integer parent;
    private Integer printType;

    private Boolean isEdit;
    private Boolean showCustomerDate = Boolean.TRUE;

    private Map<Integer, BigDecimal> itemsMap = new HashMap<>();

    private ReportManagement reportManagement;

    private Integer itemsNumber;
    private BigDecimal totalDiscountValue;
    private ItemsBarcodesDetailsView itemsBarcodesDetailsViewSelected;
    private BigDecimal totalAmount;
    private Integer typeRest;
    private Integer tableNumber;
    private Integer tableNumberTo;
    private Integer closeTable ;

    ////// load Dailog //////
    private List<InvDailogueData> invDailogueDataList;
    private List<InvDailogueData> invDailogueDataListSelected;
    private InvDailogueData invDailogeDataSelected;

    private BigDecimal value;
    private BigDecimal valueLocal;
    private BigDecimal taxValue;
    private BigDecimal taxValueLocal;
    private BigDecimal taxDiscValue;
    private BigDecimal taxDiscValueLocal;

    //report barcod or not
    private Boolean barcodeSelect;
    private Boolean checkData;
    
    // ceramic //
    private Integer look;
    private Integer isDone;
    private BigDecimal customerBalance;
    private BigDecimal totalNolon ;
    
    

    public InvInvoiceDTO() {
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
        if (postFlag == null) {
            postFlag = 0;
        }
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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
        if (!(object instanceof InvInvoiceDTO)) {
            return false;
        }
        InvInvoiceDTO other = (InvInvoiceDTO) object;
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
        if (taxflag == null) {
            taxflag = false;
        }
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
        if (taxdiscflag == null) {
            taxdiscflag = false;
        }
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
        if (taxdiscvalue == null) {
            taxdiscvalue = BigDecimal.ZERO;
        }
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
        if (invPurchaseInvoiceDetailsDeletedList == null) {
            invPurchaseInvoiceDetailsDeletedList = new ArrayList<>();
        }
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
     * @return the taxvalue
     */
    public BigDecimal getTaxvalue() {
        if(taxvalue == null){
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
     * @return the inventorySetupDTO
     */
    public InventorySetupDTO getInventorySetupDTO() {
        return inventorySetupDTO;
    }

    /**
     * @param inventorySetupDTO the inventorySetupDTO to set
     */
    public void setInventorySetupDTO(InventorySetupDTO inventorySetupDTO) {
        this.inventorySetupDTO = inventorySetupDTO;
    }

    /**
     * @return the invDailogueDataList
     */
    public List<InvDailogueData> getInvDailogueDataList() {
        if (invDailogueDataList == null) {
            invDailogueDataList = new ArrayList<>();
        }
        return invDailogueDataList;
    }

    /**
     * @param invDailogueDataList the invDailogueDataList to set
     */
    public void setInvDailogueDataList(List<InvDailogueData> invDailogueDataList) {
        this.invDailogueDataList = invDailogueDataList;
    }

    /**
     * @return the invDailogeDataSelected
     */
    public InvDailogueData getInvDailogeDataSelected() {
        if(invDailogeDataSelected == null){
            invDailogeDataSelected = new InvDailogueData();
        }
        return invDailogeDataSelected;
    }

    /**
     * @param invDailogeDataSelected the invDailogeDataSelected to set
     */
    public void setInvDailogeDataSelected(InvDailogueData invDailogeDataSelected) {
        this.invDailogeDataSelected = invDailogeDataSelected;
    }

    /**
     * @return the invPurchaseOrderId
     */
    public Integer getInvPurchaseOrderId() {
        return invPurchaseOrderId;
    }

    /**
     * @param invPurchaseOrderId the invPurchaseOrderId to set
     */
    public void setInvPurchaseOrderId(Integer invPurchaseOrderId) {
        this.invPurchaseOrderId = invPurchaseOrderId;
    }

    /**
     * @return the invAddingOrderId
     */
    public Integer getInvAddingOrderId() {
        return invAddingOrderId;
    }

    /**
     * @param invAddingOrderId the invAddingOrderId to set
     */
    public void setInvAddingOrderId(Integer invAddingOrderId) {
        this.invAddingOrderId = invAddingOrderId;
    }

    /**
     * @return the invPurchaseInvoiceDetailListSelected
     */
    public List<InvDetailDTO> getInvPurchaseInvoiceDetailListSelected() {
        return invPurchaseInvoiceDetailListSelected;
    }

    /**
     * @param invPurchaseInvoiceDetailListSelected the
     * invPurchaseInvoiceDetailListSelected to set
     */
    public void setInvPurchaseInvoiceDetailListSelected(List<InvDetailDTO> invPurchaseInvoiceDetailListSelected) {
        this.invPurchaseInvoiceDetailListSelected = invPurchaseInvoiceDetailListSelected;
    }

    /**
     * @return the parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * @return the itemsMap
     */
    public Map<Integer, BigDecimal> getItemsMap() {
        return itemsMap;
    }

    /**
     * @param itemsMap the itemsMap to set
     */
    public void setItemsMap(Map<Integer, BigDecimal> itemsMap) {
        this.itemsMap = itemsMap;
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
     * @return the payed
     */
    public BigDecimal getPayed() {
        if (payed == null) {
            payed = BigDecimal.ZERO;
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
        if (remain == null) {
            remain = BigDecimal.ZERO;
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
     * @return the dateEndOffer
     */
    public Date getDateEndOffer() {
        return dateEndOffer;
    }

    /**
     * @param dateEndOffer the dateEndOffer to set
     */
    public void setDateEndOffer(Date dateEndOffer) {
        this.dateEndOffer = dateEndOffer;
    }

    /**
     * @return the organizationSiteIdMain
     */
    public InvOrganizationSiteDTO getOrganizationSiteIdMain() {
        return organizationSiteIdMain;
    }

    /**
     * @param organizationSiteIdMain the organizationSiteIdMain to set
     */
    public void setOrganizationSiteIdMain(InvOrganizationSiteDTO organizationSiteIdMain) {
        this.organizationSiteIdMain = organizationSiteIdMain;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the loadedSerial
     */
    public Integer getLoadedSerial() {
        return loadedSerial;
    }

    /**
     * @param loadedSerial the loadedSerial to set
     */
    public void setLoadedSerial(Integer loadedSerial) {
        this.loadedSerial = loadedSerial;
    }

    /**
     * @return the needLoadedSerial
     */
    public Integer getNeedLoadedSerial() {
        return needLoadedSerial;
    }

    /**
     * @param needLoadedSerial the needLoadedSerial to set
     */
    public void setNeedLoadedSerial(Integer needLoadedSerial) {
        this.needLoadedSerial = needLoadedSerial;
    }

    /**
     * @return the organizationSiteName
     */
    public String getOrganizationSiteName() {
        return organizationSiteName;
    }

    /**
     * @param organizationSiteName the organizationSiteName to set
     */
    public void setOrganizationSiteName(String organizationSiteName) {
        this.organizationSiteName = organizationSiteName;
    }

    /**
     * @return the invDelegatorName
     */
    public String getInvDelegatorName() {
        return invDelegatorName;
    }

    /**
     * @param invDelegatorName the invDelegatorName to set
     */
    public void setInvDelegatorName(String invDelegatorName) {
        this.invDelegatorName = invDelegatorName;
    }

    /**
     * @return the invPurchaseInvoiceId
     */
    public Integer getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    /**
     * @param invPurchaseInvoiceId the invPurchaseInvoiceId to set
     */
    public void setInvPurchaseInvoiceId(Integer invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    /**
     * @return the invQotationId
     */
    public Integer getInvQotationId() {
        return invQotationId;
    }

    /**
     * @param invQotationId the invQotationId to set
     */
    public void setInvQotationId(Integer invQotationId) {
        this.invQotationId = invQotationId;
    }

    /**
     * @return the isEdit
     */
    public Boolean getIsEdit() {
        if (isEdit == null) {
            isEdit = false;
        }
        return isEdit;
    }

    /**
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the taxValue
     */
    public BigDecimal getTaxValue() {
        if (taxValue == null) {
            return BigDecimal.ZERO;
        }
        return taxValue;
    }

    /**
     * @param taxValue the taxValue to set
     */
    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

    /**
     * @return the taxDiscValue
     */
    public BigDecimal getTaxDiscValue() {
        return taxDiscValue;
    }

    /**
     * @param taxDiscValue the taxDiscValue to set
     */
    public void setTaxDiscValue(BigDecimal taxDiscValue) {
        this.taxDiscValue = taxDiscValue;
    }

    /**
     * @return the valueLocal
     */
    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    /**
     * @param valueLocal the valueLocal to set
     */
    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    /**
     * @return the taxDiscValueLocal
     */
    public BigDecimal getTaxDiscValueLocal() {
        return taxDiscValueLocal;
    }

    /**
     * @param taxDiscValueLocal the taxDiscValueLocal to set
     */
    public void setTaxDiscValueLocal(BigDecimal taxDiscValueLocal) {
        this.taxDiscValueLocal = taxDiscValueLocal;
    }

    /**
     * @return the taxValueLocal
     */
    public BigDecimal getTaxValueLocal() {
        return taxValueLocal;
    }

    /**
     * @param taxValueLocal the taxValueLocal to set
     */
    public void setTaxValueLocal(BigDecimal taxValueLocal) {
        this.taxValueLocal = taxValueLocal;
    }

    /**
     * @return the payCash
     */
    public BigDecimal getPayCash() {
        return payCash;
    }

    /**
     * @param payCash the payCash to set
     */
    public void setPayCash(BigDecimal payCash) {
        this.payCash = payCash;
    }

    /**
     * @return the payNet
     */
    public BigDecimal getPayNet() {
        if (payNet == null) {
            payNet = BigDecimal.ZERO;
        }
        return payNet;
    }

    /**
     * @param payNet the payNet to set
     */
    public void setPayNet(BigDecimal payNet) {
        this.payNet = payNet;
    }

    /**
     * @return the itemsNumber
     */
    public Integer getItemsNumber() {
        if (itemsNumber == null) {
            itemsNumber = 0;
        }
        return itemsNumber;
    }

    /**
     * @param isAdding
     * @param itemsNumber the itemsNumber to set
     */
    public void setItemsNumber(Boolean isAdding) {
        Integer itemsNumber1 = 1;
        if (isAdding) {
            itemsNumber1 = getItemsNumber() + itemsNumber1;
        } else {
            itemsNumber1 = getItemsNumber() - itemsNumber1;
        }
        this.itemsNumber = itemsNumber1;
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
     * @return the totalDiscountValue
     */
    public BigDecimal getTotalDiscountValue() {
        totalDiscountValue = getTotalBeforeDiscount().subtract(getFinalNet());
        return totalDiscountValue;
    }

    /**
     * @param totalDiscountValue the totalDiscountValue to set
     */
    public void setTotalDiscountValue(BigDecimal totalDiscountValue) {
        this.totalDiscountValue = totalDiscountValue;
    }

    /**
     * @return the invInventoryNameFrom
     */
    public String getInvInventoryNameFrom() {
        return invInventoryNameFrom;
    }

    /**
     * @param invInventoryNameFrom the invInventoryNameFrom to set
     */
    public void setInvInventoryNameFrom(String invInventoryNameFrom) {
        this.invInventoryNameFrom = invInventoryNameFrom;
    }

    /**
     * @return the invInventoryNameTo
     */
    public String getInvInventoryNameTo() {
        return invInventoryNameTo;
    }

    /**
     * @param invInventoryNameTo the invInventoryNameTo to set
     */
    public void setInvInventoryNameTo(String invInventoryNameTo) {
        this.invInventoryNameTo = invInventoryNameTo;
    }

    /**
     * @return the invInventoryIdTo
     */
    public InvInventoryDTO getInvInventoryIdTo() {
        return invInventoryIdTo;
    }

    /**
     * @param invInventoryIdTo the invInventoryIdTo to set
     */
    public void setInvInventoryIdTo(InvInventoryDTO invInventoryIdTo) {
        this.invInventoryIdTo = invInventoryIdTo;
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
     * @return the checkData
     */
    public Boolean getCheckData() {
        if (checkData == null) {
            checkData = Boolean.FALSE;
        }
        return checkData;
    }

    /**
     * @param checkData the checkData to set
     */
    public void setCheckData(Boolean checkData) {
        this.checkData = checkData;
    }

    /**
     * @return the invPurchaseInvoiceDetailBarcodeList
     */
    public List<InvDetailDTO> getInvPurchaseInvoiceDetailBarcodeList() {
        if (invPurchaseInvoiceDetailBarcodeList == null) {
            invPurchaseInvoiceDetailBarcodeList = new ArrayList<>();
        }
        return invPurchaseInvoiceDetailBarcodeList;
    }

    /**
     * @param invPurchaseInvoiceDetailBarcodeList the
     * invPurchaseInvoiceDetailBarcodeList to set
     */
    public void setInvPurchaseInvoiceDetailBarcodeList(List<InvDetailDTO> invPurchaseInvoiceDetailBarcodeList) {
        this.invPurchaseInvoiceDetailBarcodeList = invPurchaseInvoiceDetailBarcodeList;
    }

    /**
     * @return the invMonthTargetDTO
     */
    public InvMonthTargetDTO getInvMonthTargetDTO() {
        if (invMonthTargetDTO == null) {
            invMonthTargetDTO = new InvMonthTargetDTO();
        }
        return invMonthTargetDTO;
    }

    /**
     * @param invMonthTargetDTO the invMonthTargetDTO to set
     */
    public void setInvMonthTargetDTO(InvMonthTargetDTO invMonthTargetDTO) {
        this.invMonthTargetDTO = invMonthTargetDTO;
    }

    /**
<<<<<<< HEAD
     * @return the itemsBarcodesDetailsViewSelected
     */
    public ItemsBarcodesDetailsView getItemsBarcodesDetailsViewSelected() {
        return itemsBarcodesDetailsViewSelected;
    }

    /**
     * @param itemsBarcodesDetailsViewSelected the
     * itemsBarcodesDetailsViewSelected to set
     */
    public void setItemsBarcodesDetailsViewSelected(ItemsBarcodesDetailsView itemsBarcodesDetailsViewSelected) {
        this.itemsBarcodesDetailsViewSelected = itemsBarcodesDetailsViewSelected;
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
     * @return the typeRest
     */
    public Integer getTypeRest() {
        return typeRest;
    }

    /**
     * @param typeRest the typeRest to set
     */
    public void setTypeRest(Integer typeRest) {
        this.typeRest = typeRest;
    }

    /**
     * @return the tableNumber
     */
    public Integer getTableNumber() {
        return tableNumber;
    }

    /**
     * @param tableNumber the tableNumber to set
     */
    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    /**
     * @return the tableNumberTo
     */
    public Integer getTableNumberTo() {
        return tableNumberTo;
    }

    /**
     * @param tableNumberTo the tableNumberTo to set
     */
    public void setTableNumberTo(Integer tableNumberTo) {
        this.tableNumberTo = tableNumberTo;
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
     * @return the closeTable
     */
    public Integer getCloseTable() {
        return closeTable;
    }

    /**
     * @param closeTable the closeTable to set
     */
    public void setCloseTable(Integer closeTable) {
        this.closeTable = closeTable;
    }
    
    public List<InvDailogueData> getInvDailogueDataListSelected() {
        return invDailogueDataListSelected;
    }

    /**
     * @param invDailogueDataListSelected the invDailogueDataListSelected to set
     */
    public void setInvDailogueDataListSelected(List<InvDailogueData> invDailogueDataListSelected) {
        this.invDailogueDataListSelected = invDailogueDataListSelected;
    }

    /**
     * @return the invStripDetailList
     */
    public List<InvDetailDTO> getInvStripDetailList() {
        if(invStripDetailList == null){
            invStripDetailList = new ArrayList<>();
        }
        return invStripDetailList;
    }

    /**
     * @param invStripDetailList the invStripDetailList to set
     */
    public void setInvStripDetailList(List<InvDetailDTO> invStripDetailList) {
        this.invStripDetailList = invStripDetailList;
    }

    /**
     * @return the totalQuatityCost
     */
    public BigDecimal getTotalQuatityCost() {
        return totalQuatityCost;
    }

    /**
     * @param totalQuatityCost the totalQuatityCost to set
     */
    public void setTotalQuatityCost(BigDecimal totalQuatityCost) {
        this.totalQuatityCost = totalQuatityCost;
    }

    /**
     * @return the typeActivity
     */
    public Integer getTypeActivity() {
        return typeActivity;
    }

    /**
     * @param typeActivity the typeActivity to set
     */
    public void setTypeActivity(Integer typeActivity) {
        this.typeActivity = typeActivity;
    }

    /**
     * @return the revision
     */
    public Boolean getRevision() {
        if(revision == null){
            revision = Boolean.FALSE;
        }
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Boolean revision) {
        this.revision = revision;
    }

    /**
     * @return the invInvoiceSupplierNumber
     */
    public Integer getInvInvoiceSupplierNumber() {
        return invInvoiceSupplierNumber;
    }

    /**
     * @param invInvoiceSupplierNumber the invInvoiceSupplierNumber to set
     */
    public void setInvInvoiceSupplierNumber(Integer invInvoiceSupplierNumber) {
        this.invInvoiceSupplierNumber = invInvoiceSupplierNumber;
    }

    /**
     * @return the look
     */
    public Integer getLook() {
        return look;
    }

    /**
     * @param look the look to set
     */
    public void setLook(Integer look) {
        this.look = look;
    }

    /**
     * @return the isDone
     */
    public Integer getIsDone() {
        return isDone;
    }

    /**
     * @param isDone the isDone to set
     */
    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
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

    /**
     * @return the totalNolon
     */
    public BigDecimal getTotalNolon() {
        return totalNolon;
    }

    /**
     * @param totalNolon the totalNolon to set
     */
    public void setTotalNolon(BigDecimal totalNolon) {
        this.totalNolon = totalNolon;
    }

    /**
     * @return the printType
     */
    public Integer getPrintType() {
        return printType;
    }

    /**
     * @param printType the printType to set
     */
    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    /**
     * @return the accountStatement
     */
    public BigDecimal getAccountStatement() {
        return accountStatement;
    }

    /**
     * @param accountStatement the accountStatement to set
     */
    public void setAccountStatement(BigDecimal accountStatement) {
        this.accountStatement = accountStatement;
    }

}
