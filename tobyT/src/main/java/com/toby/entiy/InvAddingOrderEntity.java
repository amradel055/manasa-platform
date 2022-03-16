/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseOrder;
import com.toby.entity.InventoryDelegator;
import com.toby.views.PurchaseOrderNotLoadedFromInvAddingOrder;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class InvAddingOrderEntity extends BaseEntity {

    

    private Date date;
    private Date supplierDate;
    private String supplierInvoice;
    private Integer postFlag;
    private BigDecimal rate;
    private String remark;
    private List<InvAddingOrderDetailsEntity> invAddingOrderDetailList;
    private Integer branchId;
    private InvInventory invInventory;
    private InvOrganizationSite supplierId;
    private InventoryDelegator delegatorId;
    private InvPurchaseOrder invPurchaseOrderId;
    private PurchaseOrderNotLoadedFromInvAddingOrder purchaseOrderNLoaded;
    private PurchaseOrderNotLoadedFromInvAddingOrder purchaseOrderNLoadedTrans;
    private InvPurchaseOrder invPurchaseOrderTrans;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getSupplierDate() {
        return supplierDate;
    }

    public void setSupplierDate(Date supplierDate) {
        this.supplierDate = supplierDate;
    }

    public String getSupplierInvoice() {
        return supplierInvoice;
    }

    public void setSupplierInvoice(String supplierInvoice) {
        this.supplierInvoice = supplierInvoice;
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public InvPurchaseOrder getPurchaseOrderId() {
        return invPurchaseOrderId;
    }

    public void setPurchaseOrderId(InvPurchaseOrder purchaseOrderId) {
        this.invPurchaseOrderId = purchaseOrderId;
    }

    public List<InvAddingOrderDetailsEntity> getInvAddingOrderDetailList() {
        return invAddingOrderDetailList;
    }

    public void setInvAddingOrderDetailList(List<InvAddingOrderDetailsEntity> invAddingOrderDetailList) {
        this.invAddingOrderDetailList = invAddingOrderDetailList;
    }

    public InvOrganizationSite getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(InvOrganizationSite supplierId) {
        this.supplierId = supplierId;
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
     * @return the invInventory
     */
    public InvInventory getInvInventory() {
        return invInventory;
    }

    /**
     * @param invInventory the invInventory to set
     */
    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
    }

    /**
     * @return the invPurchaseOrderTrans
     */
    public InvPurchaseOrder getInvPurchaseOrderTrans() {
        return invPurchaseOrderTrans;
    }

    /**
     * @param invPurchaseOrderTrans the invPurchaseOrderTrans to set
     */
    public void setInvPurchaseOrderTrans(InvPurchaseOrder invPurchaseOrderTrans) {
        this.invPurchaseOrderTrans = invPurchaseOrderTrans;
    }

    /**
     * @return the purchaseOrderNLoaded
     */
    public PurchaseOrderNotLoadedFromInvAddingOrder getPurchaseOrderNLoaded() {
        return purchaseOrderNLoaded;
    }

    /**
     * @param purchaseOrderNLoaded the purchaseOrderNLoaded to set
     */
    public void setPurchaseOrderNLoaded(PurchaseOrderNotLoadedFromInvAddingOrder purchaseOrderNLoaded) {
        this.purchaseOrderNLoaded = purchaseOrderNLoaded;
    }

    /**
     * @return the purchaseOrderNLoadedTrans
     */
    public PurchaseOrderNotLoadedFromInvAddingOrder getPurchaseOrderNLoadedTrans() {
        return purchaseOrderNLoadedTrans;
    }

    /**
     * @param purchaseOrderNLoadedTrans the purchaseOrderNLoadedTrans to set
     */
    public void setPurchaseOrderNLoadedTrans(PurchaseOrderNotLoadedFromInvAddingOrder purchaseOrderNLoadedTrans) {
        this.purchaseOrderNLoadedTrans = purchaseOrderNLoadedTrans;
    }

    /**
     * @return the delegatorId
     */
    public InventoryDelegator getDelegatorId() {
        return delegatorId;
    }

    /**
     * @param delegatorId the delegatorId to set
     */
    public void setDelegatorId(InventoryDelegator delegatorId) {
        this.delegatorId = delegatorId;
    }
}
