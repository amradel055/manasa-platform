/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Symbol;
import java.math.BigDecimal;

/**
 *
 * @author WIN7
 */
public class InvBarcodeEntity {

    private Integer id;
    private Integer itemId;
    private String code;
    private BigDecimal price1;
    private BigDecimal screwing;
    private Symbol typeBarcode;
    private Symbol unitId;
    private String unitName;
    private Symbol paintColorId;
    private String paintColor;
    private Boolean markEdit = Boolean.FALSE;

    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public Symbol getTypeBarcode() {
        return typeBarcode;
    }

    public void setTypeBarcode(Symbol typeBarcode) {
        this.typeBarcode = typeBarcode;
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    /**
     * @return the paintColor
     */
    public String getPaintColor() {
        return paintColor;
    }

    /**
     * @param paintColor the paintColor to set
     */
    public void setPaintColor(String paintColor) {
        this.paintColor = paintColor;
    }

    /**
     * @return the paintColorId
     */
    public Symbol getPaintColorId() {
        return paintColorId;
    }

    /**
     * @param paintColorId the paintColorId to set
     */
    public void setPaintColorId(Symbol paintColorId) {
        this.paintColorId = paintColorId;
    }
}
