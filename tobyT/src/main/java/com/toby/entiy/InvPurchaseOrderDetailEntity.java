/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.entity.TobyUser;
import com.toby.entity.Symbol;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class InvPurchaseOrderDetailEntity {

    private Integer id;
    private Integer serial;
    private Integer itemId;
    private String itemName;
    private Symbol unit;
    private String unitName;
    private String colorPaintId;
    private String FrameName;
    private String enamel;
    private String stones;
    private BigDecimal quantity;
    private BigDecimal cost;
    private BigDecimal total;
    private BigDecimal net;
    private BigDecimal discount;
    private Boolean markEdit = Boolean.FALSE;
    private Date creationDate;
    private TobyUser createdBy;

    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private BigDecimal screwing;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
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
     * @return the createdBy
     */
    public TobyUser getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(TobyUser createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the colorPaintId
     */
    public String getColorPaintId() {
        return colorPaintId;
    }

    /**
     * @param colorPaintId the colorPaintId to set
     */
    public void setColorPaintId(String colorPaintId) {
        this.colorPaintId = colorPaintId;
    }

    /**
     * @return the FrameName
     */
    public String getFrameName() {
        return FrameName;
    }

    /**
     * @param FrameName the FrameName to set
     */
    public void setFrameName(String FrameName) {
        this.FrameName = FrameName;
    }

    /**
     * @return the enamel
     */
    public String getEnamel() {
        return enamel;
    }

    /**
     * @param enamel the enamel to set
     */
    public void setEnamel(String enamel) {
        this.enamel = enamel;
    }

    /**
     * @return the stones
     */
    public String getStones() {
        return stones;
    }

    /**
     * @param stones the stones to set
     */
    public void setStones(String stones) {
        this.stones = stones;
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

    public Symbol getUnit() {
        return unit;
    }

    public void setUnit(Symbol unit) {
        this.unit = unit;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the unitName
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * @param unitName the unitName to set
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
