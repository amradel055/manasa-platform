/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.InvItem;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.IsagUser;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author WIN7
 */
public class InvAddingOrderDetailsEntity extends BaseEntity {

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(String item) {
        this.item = item;
    }

    private BigDecimal quantity ;
    private BigDecimal mainQuantity;
    private BigDecimal useQuantity;
    private Integer invAddingOrderId;
    private Integer branchId;
    private Integer transferFrom;
    private InvPurchaseInvoiceDetail selectedId;
    private InvPurchaseInvoiceDetail selectedPurchaseId;
    private InvItem itemDataId;
    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private String unitName;
    private BigDecimal screwing;
    private BigDecimal cost; 
    private BigDecimal discount;
    private BigDecimal total;
    private BigDecimal valueAfterDiscount;
    private BigDecimal net;
    private Date creationDate;
    private IsagUser createdBy;
    private InvPurchaseOrderDetail purchaseOrderDetail;
    private InvPurchaseOrderDetail purchaseOrderDetailTrans;
    private InvItem itemId; 
    private String item; 
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getInvAddingOrderId() {
        return invAddingOrderId;
    }

    public void setInvAddingOrderId(Integer invAddingOrderId) {
        this.invAddingOrderId = invAddingOrderId;
    }

    public InvItem getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(InvItem itemDataId) {
        this.itemDataId = itemDataId;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetail() {
        return itemsBarcodesDetail;
    }

    public void setItemsBarcodesDetail(ItemsBarcodesDetailsView itemsBarcodesDetail) {
        this.itemsBarcodesDetail = itemsBarcodesDetail;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetailTrans() {
        return itemsBarcodesDetailTrans;
    }

    public void setItemsBarcodesDetailTrans(ItemsBarcodesDetailsView itemsBarcodesDetailTrans) {
        this.itemsBarcodesDetailTrans = itemsBarcodesDetailTrans;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public IsagUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @return the transferFrom
     */
    public Integer getTransferFrom() {
        return transferFrom;
    }

    /**
     * @param transferFrom the transferFrom to set
     */
    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    /**
     * @return the selectedId
     */
    public InvPurchaseInvoiceDetail getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(InvPurchaseInvoiceDetail selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
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
     * @return the valueAfterDiscount
     */
    public BigDecimal getValueAfterDiscount() {
        return valueAfterDiscount;
    }

    /**
     * @param valueAfterDiscount the valueAfterDiscount to set
     */
    public void setValueAfterDiscount(BigDecimal valueAfterDiscount) {
        this.valueAfterDiscount = valueAfterDiscount;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the mainQuantity
     */
    public BigDecimal getMainQuantity() {
        return mainQuantity;
    }

    /**
     * @param mainQuantity the mainQuantity to set
     */
    public void setMainQuantity(BigDecimal mainQuantity) {
        this.mainQuantity = mainQuantity;
    }

    /**
     * @return the selectedPurchaseId
     */
    public InvPurchaseInvoiceDetail getSelectedPurchaseId() {
        return selectedPurchaseId;
    }

    /**
     * @param selectedPurchaseId the selectedPurchaseId to set
     */
    public void setSelectedPurchaseId(InvPurchaseInvoiceDetail selectedPurchaseId) {
        this.selectedPurchaseId = selectedPurchaseId;
    }

    /**
     * @return the purchaseOrderDetail
     */
    public InvPurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    /**
     * @param purchaseOrderDetail the purchaseOrderDetail to set
     */
    public void setPurchaseOrderDetail(InvPurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    /**
     * @return the purchaseOrderDetailTrans
     */
    public InvPurchaseOrderDetail getPurchaseOrderDetailTrans() {
        return purchaseOrderDetailTrans;
    }

    /**
     * @param purchaseOrderDetailTrans the purchaseOrderDetailTrans to set
     */
    public void setPurchaseOrderDetailTrans(InvPurchaseOrderDetail purchaseOrderDetailTrans) {
        this.purchaseOrderDetailTrans = purchaseOrderDetailTrans;
    }

    /**
     * @return the itemId
     */
    public InvItem getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the useQuantity
     */
    public BigDecimal getUseQuantity() {
        return useQuantity;
    }

    /**
     * @param useQuantity the useQuantity to set
     */
    public void setUseQuantity(BigDecimal useQuantity) {
        this.useQuantity = useQuantity;
    }

    
}
