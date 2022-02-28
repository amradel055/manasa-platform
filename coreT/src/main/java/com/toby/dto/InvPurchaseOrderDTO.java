/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */

public class InvPurchaseOrderDTO extends BaseEntityDTO {

    private String supplierReference;
   
    private Date date;
   
    private String remarks;
    
    private BigDecimal rate;

    private BigDecimal discount;
  
    private Integer discountType;
    
    private Integer status;
    private Integer serial;
    
    private Currency currencyId;
    private InventoryDelegatorDTO delegatorId;
    private InvInventoryDTO invInventoryId;
    private InvOrganizationSiteDTO supplierId;
    private Collection<InvPurchaseOrderDetail> invPurchaseOrderDetailCollection;
    private Collection<InvAddingorder> invAddingorderCollection;

    public InvPurchaseOrderDTO() {
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    

    @XmlTransient
    public Collection<InvPurchaseOrderDetail> getInvPurchaseOrderDetailCollection() {
        return invPurchaseOrderDetailCollection;
    }

    public void setInvPurchaseOrderDetailCollection(Collection<InvPurchaseOrderDetail> invPurchaseOrderDetailCollection) {
        this.invPurchaseOrderDetailCollection = invPurchaseOrderDetailCollection;
    }

    @XmlTransient
    public Collection<InvAddingorder> getInvAddingorderCollection() {
        return invAddingorderCollection;
    }

    public void setInvAddingorderCollection(Collection<InvAddingorder> invAddingorderCollection) {
        this.invAddingorderCollection = invAddingorderCollection;
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
        if (!(object instanceof InvPurchaseOrderDTO)) {
            return false;
        }
        InvPurchaseOrderDTO other = (InvPurchaseOrderDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial + "";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the delegatorId
     */
    public InventoryDelegatorDTO getDelegatorId() {
        return delegatorId;
    }

    /**
     * @param delegatorId the delegatorId to set
     */
    public void setDelegatorId(InventoryDelegatorDTO delegatorId) {
        this.delegatorId = delegatorId;
    }

    /**
     * @return the invInventoryId
     */
    public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    /**
     * @return the supplierId
     */
    public InvOrganizationSiteDTO getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(InvOrganizationSiteDTO supplierId) {
        this.supplierId = supplierId;
    }

}
