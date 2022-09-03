/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.AttributeDTO;
import com.toby.dto.VariationDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author amr
 */
public class InvItemDTOApi {

    private Integer id;
    private Integer branchId;
    private Integer companyId;
    private String msg;
    private String name;
    private String code;
    private String remarks;
    private String image;
    private Integer groupId;
    private Integer inventoryId;
    private Integer proGroupId;
    private Integer typeShow;
    private Integer isInventoryItem;
    private Integer itemType;
    private Integer priceType;
    private Integer unitId;
    private String unitName;
    private String supplierId;
    private BigDecimal price;
    private BigDecimal costAverage;
    private BigDecimal sellPrice;
    private BigDecimal minPriceMen;
    private BigDecimal maxPriceMen;
    private BigDecimal minPriceYoung;
    private BigDecimal maxPriceYoung;
    private BigDecimal discountrate;
    private BigDecimal numberMetersYoung;
    private BigDecimal numberMetersMen;
    private BigDecimal numberMetersFreeYoung;
    private BigDecimal numberMetersFreeMen;
    private BigDecimal quantityOfUnit;
    private List<AttributeDTO> atrributesList;
    private List<VariationDTO> variationsList;
    private Integer customerId;
    private BigDecimal discountRow;

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
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
     * @return the atrributesList
     */
    public List<AttributeDTO> getAtrributesList() {
        return atrributesList;
    }

    /**
     * @param atrributesList the atrributesList to set
     */
    public void setAtrributesList(List<AttributeDTO> atrributesList) {
        this.atrributesList = atrributesList;
    }

    /**
     * @return the variationsList
     */
    public List<VariationDTO> getVariationsList() {
        return variationsList;
    }

    /**
     * @param variationsList the variationsList to set
     */
    public void setVariationsList(List<VariationDTO> variationsList) {
        this.variationsList = variationsList;
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
     * @return the costAverage
     */
    public BigDecimal getCostAverage() {
        return costAverage;
    }

    /**
     * @param costAverage the costAverage to set
     */
    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    /**
     * @return the sellPrice
     */
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    /**
     * @param sellPrice the sellPrice to set
     */
    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
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
     * @return the numberMetersYoung
     */
    public BigDecimal getNumberMetersYoung() {
        return numberMetersYoung;
    }

    /**
     * @param numberMetersYoung the numberMetersYoung to set
     */
    public void setNumberMetersYoung(BigDecimal numberMetersYoung) {
        this.numberMetersYoung = numberMetersYoung;
    }

    /**
     * @return the numberMetersMen
     */
    public BigDecimal getNumberMetersMen() {
        return numberMetersMen;
    }

    /**
     * @param numberMetersMen the numberMetersMen to set
     */
    public void setNumberMetersMen(BigDecimal numberMetersMen) {
        this.numberMetersMen = numberMetersMen;
    }

    /**
     * @return the numberMetersFreeYoung
     */
    public BigDecimal getNumberMetersFreeYoung() {
        return numberMetersFreeYoung;
    }

    /**
     * @param numberMetersFreeYoung the numberMetersFreeYoung to set
     */
    public void setNumberMetersFreeYoung(BigDecimal numberMetersFreeYoung) {
        this.numberMetersFreeYoung = numberMetersFreeYoung;
    }

    /**
     * @return the numberMetersFreeMen
     */
    public BigDecimal getNumberMetersFreeMen() {
        return numberMetersFreeMen;
    }

    /**
     * @param numberMetersFreeMen the numberMetersFreeMen to set
     */
    public void setNumberMetersFreeMen(BigDecimal numberMetersFreeMen) {
        this.numberMetersFreeMen = numberMetersFreeMen;
    }

    /**
     * @return the proGroupId
     */
    public Integer getProGroupId() {
        return proGroupId;
    }

    /**
     * @param proGroupId the proGroupId to set
     */
    public void setProGroupId(Integer proGroupId) {
        this.proGroupId = proGroupId;
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
     * @return the itemType
     */
    public Integer getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the priceType
     */
    public Integer getPriceType() {
        return priceType;
    }

    /**
     * @param priceType the priceType to set
     */
    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
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
     * @return the supplierId
     */
    public String getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return the quantityOfUnit
     */
    public BigDecimal getQuantityOfUnit() {
        return quantityOfUnit;
    }

    /**
     * @param quantityOfUnit the quantityOfUnit to set
     */
    public void setQuantityOfUnit(BigDecimal quantityOfUnit) {
        this.quantityOfUnit = quantityOfUnit;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the discountRow
     */
    public BigDecimal getDiscountRow() {
        return discountRow;
    }

    /**
     * @param discountRow the discountRow to set
     */
    public void setDiscountRow(BigDecimal discountRow) {
        this.discountRow = discountRow;
    }

}
