/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.*;
import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hq003
 */
public class InvAddingorderDTO extends BaseEntityDTO {

    private Date date;
    private String supplierInvoice;
    private Date supplierDate;
    private Integer postFlag;
    private String remark;
    private Collection<InvAddingorderDetail> invAddingorderDetailCollection;
    private InvOrganizationSiteDTO organizationSiteId;
    private InvPurchaseOrderDTO purchaseOrderId;
    private InvInventoryDTO invInventoryId;
    private InventoryDelegatorDTO invDelegatorId;
    private Integer status;
    private Boolean type;
    private Integer serial;

    public InvAddingorderDTO() {
    }

    public InvAddingorderDTO(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSupplierInvoice() {
        return supplierInvoice;
    }

    public void setSupplierInvoice(String supplierInvoice) {
        this.supplierInvoice = supplierInvoice;
    }

    public Date getSupplierDate() {
        return supplierDate;
    }

    public void setSupplierDate(Date supplierDate) {
        this.supplierDate = supplierDate;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlTransient
    public Collection<InvAddingorderDetail> getInvAddingorderDetailCollection() {
        return invAddingorderDetailCollection;
    }

    public void setInvAddingorderDetailCollection(Collection<InvAddingorderDetail> invAddingorderDetailCollection) {
        this.invAddingorderDetailCollection = invAddingorderDetailCollection;
    }

    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public InvPurchaseOrderDTO getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(InvPurchaseOrderDTO purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
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
        if (!(object instanceof InvAddingorderDTO)) {
            return false;
        }
        InvAddingorderDTO other = (InvAddingorderDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial+ "";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the invDelegatorId
     */
    public InventoryDelegatorDTO getInvDelegatorId() {
        return invDelegatorId;
    }

    /**
     * @param invDelegatorId the invDelegatorId to set
     */
    public void setInvDelegatorId(InventoryDelegatorDTO invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
    }

}
