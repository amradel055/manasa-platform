/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Currency;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class InvPurchaseOrderEntity {

    private Integer id;
    private InvOrganizationSite supplierId;
    private Date date;
    private String supplierReference;
    private InventoryDelegator delegateId;
    private Currency currencyId;
    private BigDecimal rate;
    private BigDecimal discount;
    private Integer discountType;
    private String Remarks;
    private Collection<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList;
    private Boolean markEdit = Boolean.FALSE;
    private IsagUser createdBy;
    private Date creationDate;
    private Integer serial;

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
     * @return the supplierReference
     */
    public String getSupplierReference() {
        return supplierReference;
    }

    /**
     * @param supplierReference the supplierReference to set
     */
    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    /**
     * @return the delegateId
     */
    public InventoryDelegator getDelegateId() {
        return delegateId;
    }

    /**
     * @param delegateId the delegateId to set
     */
    public void setDelegateId(InventoryDelegator delegateId) {
        this.delegateId = delegateId;
    }

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the Remarks
     */
    public String getRemarks() {
        return Remarks;
    }

    /**
     * @param Remarks the Remarks to set
     */
    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    /**
     * @return the invPurchaseOrderDetailEntityList
     */
    public Collection<InvPurchaseOrderDetailEntity> getInvPurchaseOrderDetailEntityList() {
        return invPurchaseOrderDetailEntityList;
    }

    /**
     * @param invPurchaseOrderDetailEntityList the
     * invPurchaseOrderDetailEntityList to set
     */
    public void setInvPurchaseOrderDetailEntityList(Collection<InvPurchaseOrderDetailEntity> invPurchaseOrderDetailEntityList) {
        this.invPurchaseOrderDetailEntityList = invPurchaseOrderDetailEntityList;
    }

    /**
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
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
     * @return the createdBy
     */
    public IsagUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
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
     * @return the currencyId
     */
    public Currency getCurrencyId() {
        return currencyId;
    }

    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
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
     * @return the discountType
     */
    public Integer getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType the discountType to set
     */
    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public InvOrganizationSite getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(InvOrganizationSite supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }
}
