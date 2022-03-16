/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.CostCenter;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlBank;
import com.isag.entity.InvInventory;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventoryDelegator;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class SalesInvoiceEntity extends BaseEntity {

    private Date date;
    private Integer discountType;
    private BigDecimal headDiscount;
    private InventoryDelegator salesPerson;
    private Integer branchId;
    private CostCenter costCenterId;
    private GlBank glBank;
    private InvInventory invInventory;
    private GlAdminUnit adminUnitId;
    private InvOrganizationSite customer;
    private int paymentType;
    private Date dueDate;
    
    private String remarks;
    private Integer serial;
    private Integer serialTax;
    
    private Boolean taxFlag = Boolean.FALSE;
    private Boolean taxFlagFinal = Boolean.FALSE;
    private BigDecimal actualWeight;
    private BigDecimal priceKilo;
    private BigDecimal totalActualWeight;
    private Boolean taxdiscflag;
    private BigDecimal taxdiscvalue;

    
    private List<SalesInvoiceDetailsEntity> salesInvoiceDetailsEntityList;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getHeadDiscount() {
        return headDiscount;
    }

    public void setHeadDiscount(BigDecimal headDiscount) {
        this.headDiscount = headDiscount;
    }

    public InventoryDelegator getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(InventoryDelegator salesPerson) {
        this.salesPerson = salesPerson;
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

    public GlBank getGlBank() {
        return glBank;
    }

    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<SalesInvoiceDetailsEntity> getSalesInvoiceDetailsEntityList() {
        return salesInvoiceDetailsEntityList;
    }

    public void setSalesInvoiceDetailsEntityList(List<SalesInvoiceDetailsEntity> salesInvoiceDetailsEntityList) {
        this.salesInvoiceDetailsEntityList = salesInvoiceDetailsEntityList;
    }

    public InvInventory getInvInventory() {
        return invInventory;
    }

    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
    }

    public InvOrganizationSite getCustomer() {
        return customer;
    }

    public void setCustomer(InvOrganizationSite customer) {
        this.customer = customer;
    }

    public GlAdminUnit getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(GlAdminUnit adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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