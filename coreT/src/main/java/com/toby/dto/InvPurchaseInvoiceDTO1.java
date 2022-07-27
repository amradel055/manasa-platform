/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class InvPurchaseInvoiceDTO1 extends BaseEntityDTO {

 

    private Date date;
    private BigDecimal rate;
    private Integer paymentType;
    private Integer status;
    private BigDecimal actualWeight;
    private BigDecimal priceKilo;
    private BigDecimal totalActualWeight;
    private BigDecimal amountInvSupplier;
    private Date dueDate;
    private String supplierInvoiceNumber;
    private BigDecimal discount;
    private Integer discountType;
    private Integer postFlag;
    private Boolean taxflag;
    private Boolean taxdiscflag;
    private BigDecimal taxdiscvalue;
    private Boolean taxFlagFinal;
    private Boolean type;
    private String remarks;
    private BigDecimal extraCost;
    private GlAdminUnitDTO adminUnitDTO;
    private Integer accountId;
    private CostCenterDTO costCenterDTO;
    private Integer currencyId;
    private InventoryDelegatorDTO inventoryDelegatorDTO;
    private InvInventoryDTO invInventoryDTO;
    private InvInventoryDTO gallary;
    private InvOrganizationSiteDTO organizationSiteIdDTO;
    private GlBankDTO glBankId;
    private SymbolDTO documentTypeId;
    private GeneralJournalDTO generalJournalDTO;
    private Date supplierInvoiceDate;
    private Integer serial;
    private Integer serialTax;
    private Date recievedate;
    private Integer dueperiod;
    private Integer recieved;
    private BigDecimal sumTotal;
    private BigDecimal sumQuantity;
    private Integer proof;
    private Integer customeraccept;
    private Integer detectPrice;
    private Integer isdeleted;
    private Integer proFactoryDeliveryId;
    private Integer proFactoryDeliverySerial;
    private String createdByName;
    private Integer invoiceType;

    private List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDetailDTOList;

    public InvPurchaseInvoiceDTO1() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getRate() {
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

    /**
     * @return the paymentType
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvPurchaseInvoiceDTO1)) {
            return false;
        }
        InvPurchaseInvoiceDTO1 other = (InvPurchaseInvoiceDTO1) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
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
     * @return the actualWeight
     */
    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    /**
     * @param actualWeight the actualWeight to set
     */
    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
    }

    /**
     * @return the priceKilo
     */
    public BigDecimal getPriceKilo() {
        return priceKilo;
    }

    /**
     * @param priceKilo the priceKilo to set
     */
    public void setPriceKilo(BigDecimal priceKilo) {
        this.priceKilo = priceKilo;
    }

    /**
     * @return the totalActualWeight
     */
    public BigDecimal getTotalActualWeight() {
        return totalActualWeight;
    }

    /**
     * @param totalActualWeight the totalActualWeight to set
     */
    public void setTotalActualWeight(BigDecimal totalActualWeight) {
        this.totalActualWeight = totalActualWeight;
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
     * @return the accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the currencyId
     */
    public Integer getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public SymbolDTO getDocumentTypeId() {
        return documentTypeId;
    }

    /**
     * @param documentTypeId the documentTypeId to set
     */
    public void setDocumentTypeId(SymbolDTO documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public InvOrganizationSiteDTO getOrganizationSiteIdDTO() {
        return organizationSiteIdDTO;
    }

    /**
     * @param organizationSiteIdDTO the organizationSiteIdDTO to set
     */
    public void setOrganizationSiteIdDTO(InvOrganizationSiteDTO organizationSiteIdDTO) {
        this.organizationSiteIdDTO = organizationSiteIdDTO;
    }

    /**
     * @return the adminUnitDTO
     */
    public GlAdminUnitDTO getAdminUnitDTO() {
        return adminUnitDTO;
    }

    /**
     * @param adminUnitDTO the adminUnitDTO to set
     */
    public void setAdminUnitDTO(GlAdminUnitDTO adminUnitDTO) {
        this.adminUnitDTO = adminUnitDTO;
    }

    /**
     * @return the costCenterDTO
     */
    public CostCenterDTO getCostCenterDTO() {
        return costCenterDTO;
    }

    /**
     * @param costCenterDTO the costCenterDTO to set
     */
    public void setCostCenterDTO(CostCenterDTO costCenterDTO) {
        this.costCenterDTO = costCenterDTO;
    }

    /**
     * @return the inventoryDelegatorDTO
     */
    public InventoryDelegatorDTO getInventoryDelegatorDTO() {
        return inventoryDelegatorDTO;
    }

    /**
     * @param inventoryDelegatorDTO the inventoryDelegatorDTO to set
     */
    public void setInventoryDelegatorDTO(InventoryDelegatorDTO inventoryDelegatorDTO) {
        this.inventoryDelegatorDTO = inventoryDelegatorDTO;
    }

    /**
     * @return the invInventoryDTO
     */
    public InvInventoryDTO getInvInventoryDTO() {
        return invInventoryDTO;
    }

    /**
     * @param invInventoryDTO the invInventoryDTO to set
     */
    public void setInvInventoryDTO(InvInventoryDTO invInventoryDTO) {
        this.invInventoryDTO = invInventoryDTO;
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
     * @return the generalJournalDTO
     */
    public GeneralJournalDTO getGeneralJournalDTO() {
        return generalJournalDTO;
    }

    /**
     * @param generalJournalDTO the generalJournalDTO to set
     */
    public void setGeneralJournalDTO(GeneralJournalDTO generalJournalDTO) {
        this.generalJournalDTO = generalJournalDTO;
    }

    /**
     * @return the invPurchaseInvoiceDetailDTOList
     */
    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailDTOList() {

        return invPurchaseInvoiceDetailDTOList;
    }

    /**
     * @param invPurchaseInvoiceDetailDTOList the
     * invPurchaseInvoiceDetailDTOList to set
     */
    public void setInvPurchaseInvoiceDetailDTOList(List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDetailDTOList) {
        this.invPurchaseInvoiceDetailDTOList = invPurchaseInvoiceDetailDTOList;
    }

    /**
     * @return the recievedate
     */
    public Date getRecievedate() {
        return recievedate;
    }

    /**
     * @param recievedate the recievedate to set
     */
    public void setRecievedate(Date recievedate) {
        this.recievedate = recievedate;
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
     * @return the recieved
     */
    public Integer getRecieved() {
        return recieved;
    }

    /**
     * @param recieved the recieved to set
     */
    public void setRecieved(Integer recieved) {
        this.recieved = recieved;
    }

    /**
     * @return the sumTotal
     */
    public BigDecimal getSumTotal() {
        return sumTotal;
    }

    /**
     * @param sumTotal the sumTotal to set
     */
    public void setSumTotal(BigDecimal sumTotal) {
        this.sumTotal = sumTotal;
    }

    /**
     * @return the sumQuantity
     */
    public BigDecimal getSumQuantity() {
        return sumQuantity;
    }

    /**
     * @param sumQuantity the sumQuantity to set
     */
    public void setSumQuantity(BigDecimal sumQuantity) {
        this.sumQuantity = sumQuantity;
    }

    /**
     * @return the proof
     */
    public Integer getProof() {
        return proof;
    }

    /**
     * @param proof the proof to set
     */
    public void setProof(Integer proof) {
        this.proof = proof;
    }

    /**
     * @return the customeraccept
     */
    public Integer getCustomeraccept() {
        return customeraccept;
    }

    /**
     * @param customeraccept the customeraccept to set
     */
    public void setCustomeraccept(Integer customeraccept) {
        this.customeraccept = customeraccept;
    }

    /**
     * @return the detectPrice
     */
    public Integer getDetectPrice() {
        return detectPrice;
    }

    /**
     * @param detectPrice the detectPrice to set
     */
    public void setDetectPrice(Integer detectPrice) {
        this.detectPrice = detectPrice;
    }

    /**
     * @return the isdeleted
     */
    public Integer getIsdeleted() {
        return isdeleted;
    }

    /**
     * @param isdeleted the isdeleted to set
     */
    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
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
     * @return the createdByName
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * @param createdByName the createdByName to set
     */
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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
     * @return the proFactoryDeliverySerial
     */
    public Integer getProFactoryDeliverySerial() {
        return proFactoryDeliverySerial;
    }

    /**
     * @param proFactoryDeliverySerial the proFactoryDeliverySerial to set
     */
    public void setProFactoryDeliverySerial(Integer proFactoryDeliverySerial) {
        this.proFactoryDeliverySerial = proFactoryDeliverySerial;
    }

    /**
     * @return the invoiceType
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }


}
