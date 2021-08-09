/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.InvItem;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class InvPurchaseInvoiceDetailsEntity extends BaseEntity {

    private String itemCode;
    private String itemName;
    private BigDecimal total;
    private BigDecimal extraCost;
    private BigDecimal valueAfterDiscount;
    private BigDecimal net;
    private BigDecimal quantity;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal cost;
    private BigDecimal firstCost;
    private Boolean costUpdated;
    private BigDecimal discount;
    private Integer discountType;
    private Integer branchId;
    private Integer invPurchaseInvoiceId;
    private Integer itemId;
    private Integer paintColorId;
    private String color;
    private Integer unitId;
    private InvItem invItem;
    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private Symbol unit;
    private Symbol paintColor;
    private String unitName;
    private BigDecimal screwing;
    private Boolean quantityDisabled;
    private Integer selectedId;
    private Integer transferFrom;
    private List<Symbol> units;
    private List<Symbol> paintColors;
    private String code;
    private Integer serial;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    public void setInvPurchaseInvoiceId(Integer invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public Symbol getUnit() {
        return unit;
    }

    public void setUnit(Symbol unit) {
        this.unit = unit;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the units
     */
    public List<Symbol> getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(List<Symbol> units) {
        this.units = units;
    }

    public BigDecimal getFirstCost() {
        return firstCost;
    }

    public void setFirstCost(BigDecimal firstCost) {
        this.firstCost = firstCost;
    }

    public Boolean getCostUpdated() {
        return costUpdated;
    }

    public void setCostUpdated(Boolean costUpdated) {
        this.costUpdated = costUpdated;
    }

    public Integer getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    public Integer getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
    }

    public Boolean getQuantityDisabled() {
        return quantityDisabled;
    }

    public void setQuantityDisabled(Boolean quantityDisabled) {
        this.quantityDisabled = quantityDisabled;
    }

    public BigDecimal getValueAfterDiscount() {
        return valueAfterDiscount;
    }

    public void setValueAfterDiscount(BigDecimal valueAfterDiscount) {
        this.valueAfterDiscount = valueAfterDiscount;
    }

    public BigDecimal getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetail() {
        return itemsBarcodesDetail;
    }

    public void setItemsBarcodesDetail(ItemsBarcodesDetailsView itemsBarcodesDetail) {
        this.itemsBarcodesDetail = itemsBarcodesDetail;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetailTrans() {
        return itemsBarcodesDetailTrans;
    }

    public void setItemsBarcodesDetailTrans(ItemsBarcodesDetailsView itemsBarcodesDetailTrans) {
        this.itemsBarcodesDetailTrans = itemsBarcodesDetailTrans;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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
     * @return the paintColorId
     */
    public Integer getPaintColorId() {
        return paintColorId;
    }

    /**
     * @param paintColorId the paintColorId to set
     */
    public void setPaintColorId(Integer paintColorId) {
        this.paintColorId = paintColorId;
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
     * @return the paintColor
     */
    public Symbol getPaintColor() {
        return paintColor;
    }

    /**
     * @param paintColor the paintColor to set
     */
    public void setPaintColor(Symbol paintColor) {
        this.paintColor = paintColor;
    }

    /**
     * @return the paintColors
     */
    public List<Symbol> getPaintColors() {
        return paintColors;
    }

    /**
     * @param paintColors the paintColors to set
     */
    public void setPaintColors(List<Symbol> paintColors) {
        this.paintColors = paintColors;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }
}
