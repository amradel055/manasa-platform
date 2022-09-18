/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.math.BigDecimal;

/**
 *
 * @author Lenovo
 */
public class InvDetailsDTOAPI extends BaseEntityDTOAPI {

    private String name;
    private String code;
    private BigDecimal price;
    private String image;
    private Integer groupId;
    private Integer inventoryId;
    private String inventoryName;
    private String inventoryCode;
    private String unitName;

    private Integer itemId;
    private BigDecimal net;
    private BigDecimal quantity;
    private BigDecimal number;

    private BigDecimal quantityOfOneUnit;
    private BigDecimal availableQuantityRow;
    private BigDecimal discountValue;
    private BigDecimal discount;
    private Integer proof;
    private Integer progroupId;
    private Integer typeShow;
    private BigDecimal lastCost;
    
    
    private Integer unitId;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

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
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the number
     */
    public BigDecimal getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    /**
     * @return the inventoryId
     */
    public Integer getInventoryId() {
        return inventoryId;
    }

    /**
     * @param inventoryId the inventoryId to set
     */
    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    /**
     * @return the quantityOfOneUnit
     */
    public BigDecimal getQuantityOfOneUnit() {
        return quantityOfOneUnit;
    }

    /**
     * @param quantityOfOneUnit the quantityOfOneUnit to set
     */
    public void setQuantityOfOneUnit(BigDecimal quantityOfOneUnit) {
        this.quantityOfOneUnit = quantityOfOneUnit;
    }

    /**
     * @return the inventoryName
     */
    public String getInventoryName() {
        return inventoryName;
    }

    /**
     * @param inventoryName the inventoryName to set
     */
    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    /**
     * @return the availableQuantityRow
     */
    public BigDecimal getAvailableQuantityRow() {
        return availableQuantityRow;
    }

    /**
     * @param availableQuantityRow the availableQuantityRow to set
     */
    public void setAvailableQuantityRow(BigDecimal availableQuantityRow) {
        this.availableQuantityRow = availableQuantityRow;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
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
     * @return the proof
     */
    public Integer getProof() {
        return proof;
    }

    /**
     * @param proof the proof to set
     */
    public void setProof(Integer proof) {
        this.proof = proof;
    }

    /**
     * @return the progroupId
     */
    public Integer getProgroupId() {
        return progroupId;
    }

    /**
     * @param progroupId the progroupId to set
     */
    public void setProgroupId(Integer progroupId) {
        this.progroupId = progroupId;
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

    /**
     * @return the lastCost
     */
    public BigDecimal getLastCost() {
        return lastCost;
    }

    /**
     * @param lastCost the lastCost to set
     */
    public void setLastCost(BigDecimal lastCost) {
        this.lastCost = lastCost;
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

    /**
     * @return the inventoryCode
     */
    public String getInventoryCode() {
        return inventoryCode;
    }

    /**
     * @param inventoryCode the inventoryCode to set
     */
    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    /**
     * @return the unitId
     */
    public Integer getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

}
