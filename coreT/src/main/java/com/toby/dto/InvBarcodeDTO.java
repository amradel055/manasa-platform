/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author H
 */
public class InvBarcodeDTO extends BaseEntityDTO{
   
    private String code;
    private BigDecimal price1;
    private BigDecimal screwing;
    private InvItemDTO itemId;
    private SymbolDTO typeBarcode;
    private SymbolDTO unitId;
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private SymbolDTO paintColor;
    private BigDecimal discountrate;
    private BigDecimal discountvalue;
    private BigDecimal maxpriceyoung;
    private BigDecimal maxpricemen;
    private BigDecimal minpriceyoung;
    private BigDecimal minpricemen;
    private BigDecimal numbermetersyoung;
    private BigDecimal numbermetersmen;
    private BigDecimal numbermetersfreeyoung;
    private BigDecimal numbermetersfreemen;
    private BigDecimal bounsepriceyoung;
    private BigDecimal bounsepricemen;
    private BigDecimal commissionrate;
    private BigDecimal commissionvalue;
    private BigDecimal price_edit;
    private Integer isinventoryitem;
    private SymbolDTO typeshow;
    private SymbolDTO item_natural;
    
    // barcode
    private List<InvBarcodeDTO> invBarcodeEntity;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the price1
     */
    public BigDecimal getPrice1() {
        return price1;
    }

    /**
     * @param price1 the price1 to set
     */
    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    /**
     * @return the screwing
     */
    public BigDecimal getScrewing() {
        return screwing;
    }

    /**
     * @param screwing the screwing to set
     */
    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @return the itemId
     */
    public InvItemDTO getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(InvItemDTO itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the typeBarcode
     */
    public SymbolDTO getTypeBarcode() {
        return typeBarcode;
    }

    /**
     * @param typeBarcode the typeBarcode to set
     */
    public void setTypeBarcode(SymbolDTO typeBarcode) {
        this.typeBarcode = typeBarcode;
    }

    /**
     * @return the unitId
     */
    public SymbolDTO getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(SymbolDTO unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the height
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(BigDecimal height) {
        this.height = height;
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
     * @return the width
     */
    public BigDecimal getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    /**
     * @return the paintColor
     */
    public SymbolDTO getPaintColor() {
        return paintColor;
    }

    /**
     * @param paintColor the paintColor to set
     */
    public void setPaintColor(SymbolDTO paintColor) {
        this.paintColor = paintColor;
    }

    /**
     * @return the discountrate
     */
    public BigDecimal getDiscountrate() {
        return discountrate;
    }

    /**
     * @param discountrate the discountrate to set
     */
    public void setDiscountrate(BigDecimal discountrate) {
        this.discountrate = discountrate;
    }

    /**
     * @return the discountvalue
     */
    public BigDecimal getDiscountvalue() {
        return discountvalue;
    }

    /**
     * @param discountvalue the discountvalue to set
     */
    public void setDiscountvalue(BigDecimal discountvalue) {
        this.discountvalue = discountvalue;
    }

    /**
     * @return the maxpriceyoung
     */
    public BigDecimal getMaxpriceyoung() {
        return maxpriceyoung;
    }

    /**
     * @param maxpriceyoung the maxpriceyoung to set
     */
    public void setMaxpriceyoung(BigDecimal maxpriceyoung) {
        this.maxpriceyoung = maxpriceyoung;
    }

    /**
     * @return the maxpricemen
     */
    public BigDecimal getMaxpricemen() {
        return maxpricemen;
    }

    /**
     * @param maxpricemen the maxpricemen to set
     */
    public void setMaxpricemen(BigDecimal maxpricemen) {
        this.maxpricemen = maxpricemen;
    }

    /**
     * @return the minpriceyoung
     */
    public BigDecimal getMinpriceyoung() {
        return minpriceyoung;
    }

    /**
     * @param minpriceyoung the minpriceyoung to set
     */
    public void setMinpriceyoung(BigDecimal minpriceyoung) {
        this.minpriceyoung = minpriceyoung;
    }

    /**
     * @return the minpricemen
     */
    public BigDecimal getMinpricemen() {
        return minpricemen;
    }

    /**
     * @param minpricemen the minpricemen to set
     */
    public void setMinpricemen(BigDecimal minpricemen) {
        this.minpricemen = minpricemen;
    }

    /**
     * @return the numbermetersyoung
     */
    public BigDecimal getNumbermetersyoung() {
        return numbermetersyoung;
    }

    /**
     * @param numbermetersyoung the numbermetersyoung to set
     */
    public void setNumbermetersyoung(BigDecimal numbermetersyoung) {
        this.numbermetersyoung = numbermetersyoung;
    }

    /**
     * @return the numbermetersmen
     */
    public BigDecimal getNumbermetersmen() {
        return numbermetersmen;
    }

    /**
     * @param numbermetersmen the numbermetersmen to set
     */
    public void setNumbermetersmen(BigDecimal numbermetersmen) {
        this.numbermetersmen = numbermetersmen;
    }

    /**
     * @return the numbermetersfreeyoung
     */
    public BigDecimal getNumbermetersfreeyoung() {
        return numbermetersfreeyoung;
    }

    /**
     * @param numbermetersfreeyoung the numbermetersfreeyoung to set
     */
    public void setNumbermetersfreeyoung(BigDecimal numbermetersfreeyoung) {
        this.numbermetersfreeyoung = numbermetersfreeyoung;
    }

    /**
     * @return the numbermetersfreemen
     */
    public BigDecimal getNumbermetersfreemen() {
        return numbermetersfreemen;
    }

    /**
     * @param numbermetersfreemen the numbermetersfreemen to set
     */
    public void setNumbermetersfreemen(BigDecimal numbermetersfreemen) {
        this.numbermetersfreemen = numbermetersfreemen;
    }

    /**
     * @return the bounsepriceyoung
     */
    public BigDecimal getBounsepriceyoung() {
        return bounsepriceyoung;
    }

    /**
     * @param bounsepriceyoung the bounsepriceyoung to set
     */
    public void setBounsepriceyoung(BigDecimal bounsepriceyoung) {
        this.bounsepriceyoung = bounsepriceyoung;
    }

    /**
     * @return the bounsepricemen
     */
    public BigDecimal getBounsepricemen() {
        return bounsepricemen;
    }

    /**
     * @param bounsepricemen the bounsepricemen to set
     */
    public void setBounsepricemen(BigDecimal bounsepricemen) {
        this.bounsepricemen = bounsepricemen;
    }

    /**
     * @return the commissionrate
     */
    public BigDecimal getCommissionrate() {
        return commissionrate;
    }

    /**
     * @param commissionrate the commissionrate to set
     */
    public void setCommissionrate(BigDecimal commissionrate) {
        this.commissionrate = commissionrate;
    }

    /**
     * @return the commissionvalue
     */
    public BigDecimal getCommissionvalue() {
        return commissionvalue;
    }

    /**
     * @param commissionvalue the commissionvalue to set
     */
    public void setCommissionvalue(BigDecimal commissionvalue) {
        this.commissionvalue = commissionvalue;
    }

    /**
     * @return the price_edit
     */
    public BigDecimal getPrice_edit() {
        return price_edit;
    }

    /**
     * @param price_edit the price_edit to set
     */
    public void setPrice_edit(BigDecimal price_edit) {
        this.price_edit = price_edit;
    }

    /**
     * @return the isinventoryitem
     */
    public Integer getIsinventoryitem() {
        return isinventoryitem;
    }

    /**
     * @param isinventoryitem the isinventoryitem to set
     */
    public void setIsinventoryitem(Integer isinventoryitem) {
        this.isinventoryitem = isinventoryitem;
    }

    /**
     * @return the typeshow
     */
    public SymbolDTO getTypeshow() {
        return typeshow;
    }

    /**
     * @param typeshow the typeshow to set
     */
    public void setTypeshow(SymbolDTO typeshow) {
        this.typeshow = typeshow;
    }

    /**
     * @return the item_natural
     */
    public SymbolDTO getItem_natural() {
        return item_natural;
    }

    /**
     * @param item_natural the item_natural to set
     */
    public void setItem_natural(SymbolDTO item_natural) {
        this.item_natural = item_natural;
    }

    /**
     * @return the invBarcodeEntity
     */
    public List<InvBarcodeDTO> getInvBarcodeEntity() {
        return invBarcodeEntity;
    }

    /**
     * @param invBarcodeEntity the invBarcodeEntity to set
     */
    public void setInvBarcodeEntity(List<InvBarcodeDTO> invBarcodeEntity) {
        this.invBarcodeEntity = invBarcodeEntity;
    }
}
