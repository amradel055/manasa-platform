/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "items_barcodes_details_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemsBarcodesDetailsView.findAll", query = "SELECT i FROM ItemsBarcodesDetailsView i"),
    @NamedQuery(name = "ItemsBarcodesDetailsView.findByCode", query = "SELECT i FROM ItemsBarcodesDetailsView i WHERE i.barcode = :barcode"),
    @NamedQuery(name = "ItemsBarcodesDetailsView.findByName", query = "SELECT i FROM ItemsBarcodesDetailsView i WHERE i.name = :name"),
    @NamedQuery(name = "ItemsBarcodesDetailsView.findByNameEn", query = "SELECT i FROM ItemsBarcodesDetailsView i WHERE i.nameEn = :nameEn"),
    @NamedQuery(name = "ItemsBarcodesDetailsView.findBySellPrice", query = "SELECT i FROM ItemsBarcodesDetailsView i WHERE i.sellPrice = :sellPrice"),
    @NamedQuery(name = "ItemsBarcodesDetailsView.findByUnitId", query = "SELECT i FROM ItemsBarcodesDetailsView i WHERE i.unitId = :unitId"),
    @NamedQuery(name = "ItemsBarcodesDetailsView.findByScrewing", query = "SELECT i FROM ItemsBarcodesDetailsView i WHERE i.screwing = :screwing")})
public class ItemsBarcodesDetailsView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Size(max = 450)
    @Column(name = "barcode")
    private String barcode;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "name_en")
    private String nameEn;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sell_price")
    private BigDecimal sellPrice;
    @Column(name = "unit_id")
    private Integer unitId;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "length")
    private BigDecimal length;
    @Column(name = "addon1")
    private String addon1;
    @Column(name = "addon2")
    private String addon2;
    @Column(name = "paint_color")
    private Integer paintColor;
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    @Column(name = "unit_name")
    private String unitName;
    @Column(name = "paint_color_name")
    private String paintColorName;
    @Column(name = "is_inventory_item")
    private Integer isInventoryItem; 
    @Column(name = "min_price_men")
    private BigDecimal minPriceMen;
    @Column(name = "max_price_men")
    private BigDecimal maxPriceMen;
    @Column(name = "min_price_young")
    private BigDecimal minPriceYoung;
    @Column(name = "max_price_young")
    private BigDecimal maxPriceYoung;
    @Column(name = "discount_rate")
    private BigDecimal discountrate;
    @Column(name = "type_show")
    private Integer typeShow;

    @Override
    public String toString() {
        return name + " " + barcode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
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
     * @return the addon1
     */
    public String getAddon1() {
        return addon1;
    }

    /**
     * @param addon1 the addon1 to set
     */
    public void setAddon1(String addon1) {
        this.addon1 = addon1;
    }

    /**
     * @return the addon2
     */
    public String getAddon2() {
        return addon2;
    }

    /**
     * @param addon2 the addon2 to set
     */
    public void setAddon2(String addon2) {
        this.addon2 = addon2;
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

    /**
     * @return the isInventoryItem
     */
    public Integer getIsInventoryItem() {
        return isInventoryItem;
    }

    /**
     * @param isInventoryItem the isInventoryItem to set
     */
    public void setIsInventoryItem(Integer isInventoryItem) {
        this.isInventoryItem = isInventoryItem;
    }

    /**
     * @return the minPriceMen
     */
    public BigDecimal getMinPriceMen() {
        return minPriceMen;
    }

    /**
     * @param minPriceMen the minPriceMen to set
     */
    public void setMinPriceMen(BigDecimal minPriceMen) {
        this.minPriceMen = minPriceMen;
    }

    /**
     * @return the maxPriceMen
     */
    public BigDecimal getMaxPriceMen() {
        return maxPriceMen;
    }

    /**
     * @param maxPriceMen the maxPriceMen to set
     */
    public void setMaxPriceMen(BigDecimal maxPriceMen) {
        this.maxPriceMen = maxPriceMen;
    }

    /**
     * @return the minPriceYoung
     */
    public BigDecimal getMinPriceYoung() {
        return minPriceYoung;
    }

    /**
     * @param minPriceYoung the minPriceYoung to set
     */
    public void setMinPriceYoung(BigDecimal minPriceYoung) {
        this.minPriceYoung = minPriceYoung;
    }

    /**
     * @return the maxPriceYoung
     */
    public BigDecimal getMaxPriceYoung() {
        return maxPriceYoung;
    }

    /**
     * @param maxPriceYoung the maxPriceYoung to set
     */
    public void setMaxPriceYoung(BigDecimal maxPriceYoung) {
        this.maxPriceYoung = maxPriceYoung;
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
     * @return the typeShow
     */
    public Integer getTypeShow() {
        return typeShow;
    }

    /**
     * @param typeShow the typeShow to set
     */
    public void setTypeShow(Integer typeShow) {
        this.typeShow = typeShow;
    }
}
