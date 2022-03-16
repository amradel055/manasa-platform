/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventoryDelegator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class InvPurchaseInvoiceEntity extends BaseEntity {

    private Integer receivingNumber;
    private Date date, dueDate, supplierInvoiceDate, currencyDate;
    private BigDecimal rate;
    private int paymentType;
    private BigDecimal localExtraCost;
    private BigDecimal remoteExtraCost;
    private String supplierInvoiceNumber;
    private BigDecimal headDiscount;
    private Integer discountType;
    private int postFlag;
    private List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailCollection;
    private GlAdminUnit adminUnitId;
    private Integer branchId;
    private CostCenter costCenterId;
    private Currency currencyId;
    private Integer purchuseOrderId;
    private InvOrganizationSite supplierId;
    private String remarks;
    private Integer accountId;
    private InvInventory invInventory;
    private BigDecimal extraCost;
    private InventoryDelegator purchasePerson;
    private GlBank glBank;
    private Integer serial;
    private Boolean taxFlag = Boolean.FALSE;
    private BigDecimal actualWeight;
    private BigDecimal priceKilo;
    private BigDecimal totalActualWeight;
    private Boolean taxdiscflag;
    private BigDecimal taxdiscvalue;

//    private BigDecimal amountInv;
//    private BigDecimal amountInvLocal;
//    private BigDecimal amountInvSupplierLocal;
//    private BigDecimal amountInvSupplier;
    private List<InvReturnPurchaseEntity> invReturnPurchaseCollection;

    public Integer getReceivingNumber() {
        return receivingNumber;
    }

    public void setReceivingNumber(Integer receivingNumber) {
        this.receivingNumber = receivingNumber;
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

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getLocalExtraCost() {
        return localExtraCost;
    }

    public void setLocalExtraCost(BigDecimal localExtraCost) {
        this.localExtraCost = localExtraCost;
    }

    public BigDecimal getRemoteExtraCost() {
        return remoteExtraCost;
    }

    public void setRemoteExtraCost(BigDecimal remoteExtraCost) {
        this.remoteExtraCost = remoteExtraCost;
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

    public BigDecimal getHeadDiscount() {
        return headDiscount;
    }

    public void setHeadDiscount(BigDecimal headDiscount) {
        this.headDiscount = headDiscount;
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

    public List<InvPurchaseInvoiceDetailsEntity> getInvPurchaseInvoiceDetailCollection() {
        return invPurchaseInvoiceDetailCollection;
    }

    public void setInvPurchaseInvoiceDetailCollection(List<InvPurchaseInvoiceDetailsEntity> invPurchaseInvoiceDetailCollection) {
        this.invPurchaseInvoiceDetailCollection = invPurchaseInvoiceDetailCollection;
    }

    public GlAdminUnit getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(GlAdminUnit adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public CostCenter getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(CostCenter costCenterId) {
        this.costCenterId = costCenterId;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getPurchuseOrderId() {
        return purchuseOrderId;
    }

    public void setPurchuseOrderId(Integer purchuseOrderId) {
        this.purchuseOrderId = purchuseOrderId;
    }

    public InvOrganizationSite getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(InvOrganizationSite supplierId) {
        this.supplierId = supplierId;
    }

    public List<InvReturnPurchaseEntity> getInvReturnPurchaseCollection() {
        return invReturnPurchaseCollection;
    }

    public void setInvReturnPurchaseCollection(List<InvReturnPurchaseEntity> invReturnPurchaseCollection) {
        this.invReturnPurchaseCollection = invReturnPurchaseCollection;
    }

    public Date getCurrencyDate() {
        return currencyDate;
    }

    public void setCurrencyDate(Date currencyDate) {
        this.currencyDate = currencyDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public InvInventory getInvInventory() {
        return invInventory;
    }

    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
    }

    public BigDecimal getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public InventoryDelegator getPurchasePerson() {
        return purchasePerson;
    }

    public void setPurchasePerson(InventoryDelegator purchasePerson) {
        this.purchasePerson = purchasePerson;
    }

    public GlBank getGlBank() {
        return glBank;
    }

    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the taxFlag
     */
    public Boolean getTaxFlag() {
        return taxFlag;
    }

    /**
     * @param taxFlag the taxFlag to set
     */
    public void setTaxFlag(Boolean taxFlag) {
        this.taxFlag = taxFlag;
    }

    public Date getSupplierInvoiceDate() {
        return supplierInvoiceDate;
    }

    public void setSupplierInvoiceDate(Date supplierInvoiceDate) {
        this.supplierInvoiceDate = supplierInvoiceDate;
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
}
