/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.define.CompanyActivityClassEnum;
import com.toby.define.ModuleClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.entity.ReportManagement;
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

    private CompanyActivityClassEnum companyActivity;
    private ScreenNameClassEnum ScreenName;
    private ModuleClassEnum moduleClassEnum;
    private Date date;
    private String name;
    private Date dateHegry;
    private Date dateEndOffer;
    private BigDecimal rate;
    private BigDecimal numberOfToob;
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
    private String invDelegatorName;
    private InvInventoryDTO invInventoryId;
    private String organizationSiteName;
    private InvOrganizationSiteDTO organizationSiteId;
    private InvOrganizationSiteDTO organizationSiteIdMain;
    private GlBankDTO glBankId;
    private SymbolDTO documentTypeId;
    private Date supplierInvoiceDate;
    private Integer serial;
    private Integer serialTax;
    private String address;
    private String customerNotice;
    private InvDetailDTO invPurchaseInvoiceDetailsSelected;
    private List<InvDetailDTO> invPurchaseInvoiceDetailListSelected;
    private Boolean isOld;

    private BigDecimal totalQuatity;
    private BigDecimal totalWeight;
    private BigDecimal totalNet;
    private BigDecimal taxvalue;
    private BigDecimal totalWithTaxValue;
    private BigDecimal finalNet;
    private BigDecimal totalNetAfterDiscount;
    private BigDecimal payed;
    private BigDecimal remain;
    private BigDecimal accountStatement;

    private InventorySetupDTO inventorySetupDTO;

    private List<InvDetailDTO> invPurchaseInvoiceDetailList;
    private List<InvDetailDTO> invPurchaseInvoiceDetailsDeletedList;
    private List<InvDetailDTO> invPurchaseInvoiceDetailBarcodeList;

    private Integer invPurchaseInvoiceId;
    private Integer proFactoryDeliveryId;
    private Integer invPurchaseOrderId;
    private Integer invAddingOrderId;
    private Integer invQotationId;
    private Integer loadedSerial;
    private Integer needLoadedSerial;
    private Integer parent;

    private String invInventoryNameFrom;
    private String invInventoryNameTo;
    private InvInventoryDTO invInventoryIdTo;

    private Integer itemsNumber;

    private Boolean isEdit;
    private Boolean showCustomerDate = Boolean.TRUE;

    private Map<Integer, BigDecimal> itemsMap = new HashMap<>();

    private ReportManagement reportManagement;
    private OfferCompanyDTO offerCompanyId;

    //report barcod or not
    private Boolean barcodeSelect;
    private Boolean checkData;

    ////// load Dailog //////
    private List<InvDailogueData> invDailogueDataList;
    private InvDailogueData invDailogeDataSelected;
    private List<InvDailogueData> invDailogeDataListSelected;

    private BigDecimal value;
    private BigDecimal valueLocal;
    private BigDecimal taxValue;
    private BigDecimal taxValueLocal;
    private BigDecimal taxDiscValue;
    private BigDecimal taxDiscValueLocal;

    //////// Cloth ////////
    private Integer pricetype;
    private Integer dueperiod;
    private InvInventoryDTO gallaryId;
    private InvItemDTO invParentItem;
    private Integer year;
    private Boolean proof;
    private String invoiceStatus;

    /// production //
    private Date time;
    private InvInventoryDTO gallary;
    private InvInventoryDTO factory;
    private InvDetailDTO invDetailDTOSelected;
    private List<InvDetailDTO> invStripDetailList;
    private long diffrentTime;
    private Integer invoiceSerial;
    private Integer offerType;
    private BigDecimal discHalala;

    /// report //
    private String gallaryName;
    private String customerCode;
    private String customerName;
    private String customerMobile;

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
//        if (paymentType == null) {
//            paymentType = 0;
//        }
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
        if (taxvalue == null) {
            return BigDecimal.ZERO;
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
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
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
     * @return the time
     */
    public Date getTime() {
        if (time == null) {
            time = new Date();
        }
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the gallary
     */
    public InvInventoryDTO getGallary() {
        return gallary;
    }

    /**
     * @param gallary the gallary to set
     */
    public void setGallary(InvInventoryDTO gallary) {
        this.gallary = gallary;
    }

    /**
     * @return the factory
     */
    public InvInventoryDTO getFactory() {
        return factory;
    }

    /**
     * @param factory the factory to set
     */
    public void setFactory(InvInventoryDTO factory) {
        this.factory = factory;
    }

    /**
     * @return the moduleClassEnum
     */
    public ModuleClassEnum getModuleClassEnum() {
        return moduleClassEnum;
    }

    /**
     * @param moduleClassEnum the moduleClassEnum to set
     */
    public void setModuleClassEnum(ModuleClassEnum moduleClassEnum) {
        this.moduleClassEnum = moduleClassEnum;
    }

    /**
     * @return the proof
     */
    public Boolean getProof() {
        if (proof == null) {
            proof = false;
        }
        return proof;
    }

    /**
     * @param proof the proof to set
     */
    public void setProof(Boolean proof) {
        this.proof = proof;
    }

    /**
     * @return the proFactoryDeliveryId
     */
    public Integer getProFactoryDeliveryId() {
        return proFactoryDeliveryId;
    }

    /**
     * @param proFactoryDeliveryId the proFactoryDeliveryId to set
     */
    public void setProFactoryDeliveryId(Integer proFactoryDeliveryId) {
        this.proFactoryDeliveryId = proFactoryDeliveryId;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
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
        if (taxDiscValue == null) {
            taxDiscValue = BigDecimal.ZERO;
        }
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
        if (valueLocal == null) {
            valueLocal = BigDecimal.ZERO;
        }
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
        if (taxDiscValueLocal == null) {
            taxDiscValueLocal = BigDecimal.ZERO;
        }
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
        if (taxValueLocal == null) {
            taxValueLocal = BigDecimal.ZERO;
        }
        return taxValueLocal;
    }

    /**
     * @param taxValueLocal the taxValueLocal to set
     */
    public void setTaxValueLocal(BigDecimal taxValueLocal) {
        this.taxValueLocal = taxValueLocal;
    }

    /**
     * @return the invDetailDTOSelected
     */
    public InvDetailDTO getInvDetailDTOSelected() {
        if (invDetailDTOSelected == null) {
            invDetailDTOSelected = new InvDetailDTO();
        }
        return invDetailDTOSelected;
    }

    /**
     * @param invDetailDTOSelected the invDetailDTOSelected to set
     */
    public void setInvDetailDTOSelected(InvDetailDTO invDetailDTOSelected) {
        this.invDetailDTOSelected = invDetailDTOSelected;
    }

    /**
     * @return the invoiceStatus
     */
    public String getInvoiceStatus() {
//        if (invoiceStatus == null) {
//            invoiceStatus = "فى الطريق";
//        }
        return invoiceStatus;
    }

    /**
     * @param invoiceStatus the invoiceStatus to set
     */
    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    /**
     * @return the invDailogeDataListSelected
     */
    public List<InvDailogueData> getInvDailogeDataListSelected() {
        return invDailogeDataListSelected;
    }

    /**
     * @param invDailogeDataListSelected the invDailogeDataListSelected to set
     */
    public void setInvDailogeDataListSelected(List<InvDailogueData> invDailogeDataListSelected) {
        this.invDailogeDataListSelected = invDailogeDataListSelected;
    }

    /**
     * @return the isOld
     */
    public Boolean getIsOld() {
        if (isOld == null) {
            isOld = Boolean.FALSE;
        }
        return isOld;
    }

    /**
     * @param isOld the isOld to set
     */
    public void setIsOld(Boolean isOld) {
        this.isOld = isOld;
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
     * @return the barcodeSelect
     */
    public Boolean getBarcodeSelect() {
        if (barcodeSelect == null) {
            barcodeSelect = Boolean.FALSE;
        }
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
        return checkData;
    }

    /**
     * @param checkData the checkData to set
     */
    public void setCheckData(Boolean checkData) {
        this.checkData = checkData;
    }

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
     * @return the showCustomerDate
     */
    public Boolean getShowCustomerDate() {
        if (showCustomerDate == null) {
            showCustomerDate = Boolean.TRUE;
        }
        return showCustomerDate;
    }

    /**
     * @param showCustomerDate the showCustomerDate to set
     */
    public void setShowCustomerDate(Boolean showCustomerDate) {
        this.showCustomerDate = showCustomerDate;
    }

    /**
     * @return the invStripDetailList
     */
    public List<InvDetailDTO> getInvStripDetailList() {
        if (invStripDetailList == null) {
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
     * @return the numberOfToob
     */
    public BigDecimal getNumberOfToob() {
        return numberOfToob;
    }

    /**
     * @param numberOfToob the numberOfToob to set
     */
    public void setNumberOfToob(BigDecimal numberOfToob) {
        this.numberOfToob = numberOfToob;
    }

    /**
     * @return the diffrentTime
     */
    public long getDiffrentTime() {
        return diffrentTime;
    }

    /**
     * @param diffrentTime the diffrentTime to set
     */
    public void setDiffrentTime(long diffrentTime) {
        this.diffrentTime = diffrentTime;
    }

    /**
     * @return the invoiceSerial
     */
    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    /**
     * @param invoiceSerial the invoiceSerial to set
     */
    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    /**
     * @return the gallaryName
     */
    public String getGallaryName() {
        return gallaryName;
    }

    /**
     * @param gallaryName the gallaryName to set
     */
    public void setGallaryName(String gallaryName) {
        this.gallaryName = gallaryName;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerMobile
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * @param customerMobile the customerMobile to set
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    /**
     * @return the offerType
     */
    public Integer getOfferType() {
        return offerType;
    }

    /**
     * @param offerType the offerType to set
     */
    public void setOfferType(Integer offerType) {
        this.offerType = offerType;
    }

    /**
     * @return the offerCompanyId
     */
    public OfferCompanyDTO getOfferCompanyId() {
        return offerCompanyId;
    }

    /**
     * @param offerCompanyId the offerCompanyId to set
     */
    public void setOfferCompanyId(OfferCompanyDTO offerCompanyId) {
        this.offerCompanyId = offerCompanyId;
    }

    /**
     * @return the customerNotice
     */
    public String getCustomerNotice() {
        return customerNotice;
    }

    /**
     * @param customerNotice the customerNotice to set
     */
    public void setCustomerNotice(String customerNotice) {
        this.customerNotice = customerNotice;
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

    /**
     * @return the discHalala
     */
    public BigDecimal getDiscHalala() {
        if (discHalala == null) {
            discHalala = BigDecimal.ZERO;
        }
        return discHalala;
    }

    /**
     * @param discHalala the discHalala to set
     */
    public void setDiscHalala(BigDecimal discHalala) {
        this.discHalala = discHalala;
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

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
