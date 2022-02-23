/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Branch;
import com.isag.entity.InvItem;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvReturnPurchase;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;

/**
 *
 * @author WIN7
 */
public class InvReturnPurchaseDetailEntity extends BaseEntity {

    private BigDecimal screwing;
    private BigDecimal quantity;
    private BigDecimal loaddedQuantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private Integer dicountType;
    private Branch branchId;
    private InvReturnPurchase invReturnPurchaseId;
    private InvItem itemId;
    private String itemName;
    private Symbol unit;
    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private BigDecimal net;
    private BigDecimal total;
    private InvPurchaseInvoiceDetail purchaseInvoiceDetail;
    private InvPurchaseInvoiceDetail purchaseInvoiceDetailTrans;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDicountType() {
        return dicountType;
    }

    public void setDicountType(Integer dicountType) {
        this.dicountType = dicountType;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvReturnPurchase getInvReturnPurchaseId() {
        return invReturnPurchaseId;
    }

    public void setInvReturnPurchaseId(InvReturnPurchase invReturnPurchaseId) {
        this.invReturnPurchaseId = invReturnPurchaseId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getLoaddedQuantity() {
        return loaddedQuantity;
    }

    public void setLoaddedQuantity(BigDecimal loaddedQuantity) {
        this.loaddedQuantity = loaddedQuantity;
    }

    public InvPurchaseInvoiceDetail getPurchaseInvoiceDetail() {
        return purchaseInvoiceDetail;
    }

    public void setPurchaseInvoiceDetail(InvPurchaseInvoiceDetail purchaseInvoiceDetail) {
        this.purchaseInvoiceDetail = purchaseInvoiceDetail;
    }

    public InvPurchaseInvoiceDetail getPurchaseInvoiceDetailTrans() {
        return purchaseInvoiceDetailTrans;
    }

    public void setPurchaseInvoiceDetailTrans(InvPurchaseInvoiceDetail purchaseInvoiceDetailTrans) {
        this.purchaseInvoiceDetailTrans = purchaseInvoiceDetailTrans;
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

    public Symbol getUnit() {
        return unit;
    }

    public void setUnit(Symbol unit) {
        this.unit = unit;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
