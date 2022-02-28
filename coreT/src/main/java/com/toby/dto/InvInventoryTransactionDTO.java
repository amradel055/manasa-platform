/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.InvInventoryTransaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author user4
 */
public class InvInventoryTransactionDTO extends BaseEntityDTO {

    private static final long serialVersionUID = 1L;
    private Integer serial;
    private Date date;
    private String supplierInvoice;
    private Date supplierDate;
    private Integer postFlag;
    private String remark;
    private Integer type;
    private Integer status;
    private Integer invDelegatorId;
    private InvOrganizationSiteDTO organizationSiteId;
    private InvPurchaseOrderDTO purchaseOrderId;
    private InvInventoryDTO invInventoryId;
    private List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetailDTOList;
    private InvPurchaseInvoiceDTO1 invpurchaseinvoiceId;
    private BigDecimal sumQuantity;
    private Integer isdeleted;

    

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInvDelegatorId() {
        return invDelegatorId;
    }

    public void setInvDelegatorId(Integer invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
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
        if (!(object instanceof InvInventoryTransactionDTO)) {
            return false;
        }
        InvInventoryTransactionDTO other = (InvInventoryTransactionDTO) object;
         if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
        
       
        
        
    }

    @Override
    public String toString() {
        return "com.toby.omar.InvInventoryTransaction[ id=" + id + " ]";
    }

    /**
     * @return the organizationSiteId
     */
    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the purchaseOrderId
     */
    public InvPurchaseOrderDTO getPurchaseOrderId() {
        return purchaseOrderId;
    }

    /**
     * @param purchaseOrderId the purchaseOrderId to set
     */
    public void setPurchaseOrderId(InvPurchaseOrderDTO purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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
     * @return the invInventoryTransactionDetailDTOList
     */
    public List<InvInventoryTransactionDetailDTO> getInvInventoryTransactionDetailDTOList() {
//        if(invInventoryTransactionDetailDTOList==null ||invInventoryTransactionDetailDTOList.isEmpty()){
//        
//        invInventoryTransactionDetailDTOList = new ArrayList<>();
//        }
//       
        return invInventoryTransactionDetailDTOList;
    }

    /**
     * @param invInventoryTransactionDetailDTOList the
     * invInventoryTransactionDetailDTOList to set
     */
    public void setInvInventoryTransactionDetailDTOList(List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetailDTOList) {
        this.invInventoryTransactionDetailDTOList = invInventoryTransactionDetailDTOList;
    }

    /**
     * @return the invpurchaseinvoiceId
     */
    public InvPurchaseInvoiceDTO1 getInvpurchaseinvoiceId() {
        return invpurchaseinvoiceId;
    }

    /**
     * @param invpurchaseinvoiceId the invpurchaseinvoiceId to set
     */
    public void setInvpurchaseinvoiceId(InvPurchaseInvoiceDTO1 invpurchaseinvoiceId) {
        this.invpurchaseinvoiceId = invpurchaseinvoiceId;
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

}
