/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Branch;
import com.isag.entity.InvItem;
import com.isag.entity.IsagUser;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class InvQuotationDetailsEntity extends BaseEntity {

    private InvItem invItem;
    
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer discountId;
    private Branch branchId;
    private BigDecimal total;
    private BigDecimal net;
    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private BigDecimal screwing;
    private IsagUser createdBy;
    private Date creationDate;
    private String Item;
    private String unit;
    private BigDecimal weight;
    private Integer paintColor;
    private String paintColorName;
    private BigDecimal length;

   
    

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
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

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public IsagUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the Item
     */
    public String getItem() {
        return Item;
    }

    /**
     * @param Item the Item to set
     */
    public void setItem(String Item) {
        this.Item = Item;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * @return the paintColor
     */
    public Integer getPaintColor() {
        return paintColor;
    }

    /**
     * @param paintColor the paintColor to set
     */
    public void setPaintColor(Integer paintColor) {
        this.paintColor = paintColor;
    }

    /**
     * @return the length
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /**
     * @return the paintColorName
     */
    public String getPaintColorName() {
        return paintColorName;
    }

    /**
     * @param paintColorName the paintColorName to set
     */
    public void setPaintColorName(String paintColorName) {
        this.paintColorName = paintColorName;
    }
 
}
