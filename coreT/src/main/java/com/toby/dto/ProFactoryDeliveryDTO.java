/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.Date;

/**
 *
 * @author User
 */
public class ProFactoryDeliveryDTO extends BaseEntityDTO {

    private Date date;
    private Date time;
    private InvInventoryDTO factoryId;
    private InvInventoryDTO gallaryId;
    private InvInvoiceDTO invPurchaseInvoiceId;

    public ProFactoryDeliveryDTO() {
    }

    public ProFactoryDeliveryDTO(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
        if (!(object instanceof ProFactoryDeliveryDTO)) {
            return false;
        }
        ProFactoryDeliveryDTO other = (ProFactoryDeliveryDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ccc.ProFactoryDelivery[ id=" + id + " ]";
    }

    /**
     * @return the factoryId
     */
    public InvInventoryDTO getFactoryId() {
        return factoryId;
    }

    /**
     * @param factoryId the factoryId to set
     */
    public void setFactoryId(InvInventoryDTO factoryId) {
        this.factoryId = factoryId;
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
     * @return the invPurchaseInvoiceId
     */
    public InvInvoiceDTO getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    /**
     * @param invPurchaseInvoiceId the invPurchaseInvoiceId to set
     */
    public void setInvPurchaseInvoiceId(InvInvoiceDTO invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }
    
}
