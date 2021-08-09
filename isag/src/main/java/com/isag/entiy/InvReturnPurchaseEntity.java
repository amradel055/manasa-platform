/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Branch;
import com.isag.entity.CostCenter;
import com.isag.entity.Currency;
import com.isag.entity.GlBank;
import com.isag.entity.InvInventory;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvReturnPurchaseDetail;
import com.isag.entity.InventoryDelegator;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author WIN7
 */
public class InvReturnPurchaseEntity extends BaseEntity {

    /**
     * @return the paymentTypeText
     */
    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    /**
     * @param paymentTypeText the paymentTypeText to set
     */
    public void setPaymentTypeText(String paymentTypeText) {
        this.paymentTypeText = paymentTypeText;
    }

    private Integer paymentType;
    private String paymentTypeText;
    private Date date;
    private Date invoiceDate;
    private String remark;
    private Collection<InvReturnPurchaseDetail> invReturnPurchaseDetailCollection;
    private BigDecimal rate;
    private CostCenter costCenter;
    private InvOrganizationSite supplierId;
    private Branch branchId;
    private GlBank glBankId;
    private BigDecimal taxvalue;
    private Currency currencyId;
    private InvInventory invInventoryId;
    private InventoryDelegator salesPerson;
    private InvPurchaseInvoice purchaseInvoice;
    private InvPurchaseInvoice purchaseInvoiceTrans;
    private Boolean taxFlagFinal;
    
    private BigDecimal total;
    private BigDecimal discount;
    private BigDecimal totalNetAfterDiscount;
    private BigDecimal totalWithTaxValue;

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CostCenter getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Collection<InvReturnPurchaseDetail> getInvReturnPurchaseDetailCollection() {
        return invReturnPurchaseDetailCollection;
    }

    public void setInvReturnPurchaseDetailCollection(Collection<InvReturnPurchaseDetail> invReturnPurchaseDetailCollection) {
        this.invReturnPurchaseDetailCollection = invReturnPurchaseDetailCollection;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public InvOrganizationSite getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(InvOrganizationSite supplierId) {
        this.supplierId = supplierId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public GlBank getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(GlBank glBankId) {
        this.glBankId = glBankId;
    }

    public InvPurchaseInvoice getPurchaseInvoice() {
        return purchaseInvoice;
    }

    public void setPurchaseInvoice(InvPurchaseInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }

    public InvPurchaseInvoice getPurchaseInvoiceTrans() {
        return purchaseInvoiceTrans;
    }

    public void setPurchaseInvoiceTrans(InvPurchaseInvoice purchaseInvoiceTrans) {
        this.purchaseInvoiceTrans = purchaseInvoiceTrans;
    }

    public InventoryDelegator getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(InventoryDelegator salesPerson) {
        this.salesPerson = salesPerson;
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
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the totalNetAfterDiscount
     */
    public BigDecimal getTotalNetAfterDiscount() {
        return totalNetAfterDiscount;
    }

    /**
     * @param totalNetAfterDiscount the totalNetAfterDiscount to set
     */
    public void setTotalNetAfterDiscount(BigDecimal totalNetAfterDiscount) {
        this.totalNetAfterDiscount = totalNetAfterDiscount;
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
}
