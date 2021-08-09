/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.Branch;
import com.isag.entity.InvGroup;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author amr
 */
public class InvItemDTO extends BaseEntityDTO {

    private String msg;
    private String code;
    private String name;
    private String nameen;
    private Integer typeCat;
    private BigDecimal sellPrice;
    private BigDecimal undirectCost;
    private String remarks;
    private Integer statusCat;
    private BigDecimal contractPrice;
    private String storageLocation;
    private String nickname;
    private BigDecimal minimumAmount;
    private BigDecimal maxmumAmount;
    private BigDecimal lastCost;
    private BigDecimal costAverage;
    private Date dateCreateCat;
    private BigDecimal openingCost;
    private BigDecimal storesQuality;
    private String image;
    private BigDecimal weightPackage;
    private Boolean issell;
    private Boolean ispurchase;
    private Branch branchId;
    private Symbol brandId;
    private Symbol enamelColor;
    private InvGroup groupId;
    private Symbol unitId;
    private Symbol originCountry;
    private Symbol paintColor;
    private Symbol author;
    private Symbol addon1;
    private Symbol addon2;
    private InvOrganizationSite siteId;
    private Symbol stone;
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private Integer printShow;


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
     * @return the nameen
     */
    public String getNameen() {
        return nameen;
    }

    /**
     * @param nameen the nameen to set
     */
    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    /**
     * @return the typeCat
     */
    public Integer getTypeCat() {
        return typeCat;
    }

    /**
     * @param typeCat the typeCat to set
     */
    public void setTypeCat(Integer typeCat) {
        this.typeCat = typeCat;
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
     * @return the undirectCost
     */
    public BigDecimal getUndirectCost() {
        return undirectCost;
    }

    /**
     * @param undirectCost the undirectCost to set
     */
    public void setUndirectCost(BigDecimal undirectCost) {
        this.undirectCost = undirectCost;
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
     * @return the statusCat
     */
    public Integer getStatusCat() {
        return statusCat;
    }

    /**
     * @param statusCat the statusCat to set
     */
    public void setStatusCat(Integer statusCat) {
        this.statusCat = statusCat;
    }

    /**
     * @return the contractPrice
     */
    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    /**
     * @param contractPrice the contractPrice to set
     */
    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    /**
     * @return the storageLocation
     */
    public String getStorageLocation() {
        return storageLocation;
    }

    /**
     * @param storageLocation the storageLocation to set
     */
    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the minimumAmount
     */
    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    /**
     * @param minimumAmount the minimumAmount to set
     */
    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    /**
     * @return the maxmumAmount
     */
    public BigDecimal getMaxmumAmount() {
        return maxmumAmount;
    }

    /**
     * @param maxmumAmount the maxmumAmount to set
     */
    public void setMaxmumAmount(BigDecimal maxmumAmount) {
        this.maxmumAmount = maxmumAmount;
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
     * @return the dateCreateCat
     */
    public Date getDateCreateCat() {
        return dateCreateCat;
    }

    /**
     * @param dateCreateCat the dateCreateCat to set
     */
    public void setDateCreateCat(Date dateCreateCat) {
        this.dateCreateCat = dateCreateCat;
    }

    /**
     * @return the openingCost
     */
    public BigDecimal getOpeningCost() {
        return openingCost;
    }

    /**
     * @param openingCost the openingCost to set
     */
    public void setOpeningCost(BigDecimal openingCost) {
        this.openingCost = openingCost;
    }

    /**
     * @return the storesQuality
     */
    public BigDecimal getStoresQuality() {
        return storesQuality;
    }

    /**
     * @param storesQuality the storesQuality to set
     */
    public void setStoresQuality(BigDecimal storesQuality) {
        this.storesQuality = storesQuality;
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
     * @return the weightPackage
     */
    public BigDecimal getWeightPackage() {
        return weightPackage;
    }

    /**
     * @param weightPackage the weightPackage to set
     */
    public void setWeightPackage(BigDecimal weightPackage) {
        this.weightPackage = weightPackage;
    }

    /**
     * @return the issell
     */
    public Boolean getIssell() {
        return issell;
    }

    /**
     * @param issell the issell to set
     */
    public void setIssell(Boolean issell) {
        this.issell = issell;
    }

    /**
     * @return the ispurchase
     */
    public Boolean getIspurchase() {
        return ispurchase;
    }

    /**
     * @param ispurchase the ispurchase to set
     */
    public void setIspurchase(Boolean ispurchase) {
        this.ispurchase = ispurchase;
    }

    /**
     * @return the brandId
     */
    public Symbol getBrandId() {
        return brandId;
    }

    /**
     * @param brandId the brandId to set
     */
    public void setBrandId(Symbol brandId) {
        this.brandId = brandId;
    }

    /**
     * @return the enamelColor
     */
    public Symbol getEnamelColor() {
        return enamelColor;
    }

    /**
     * @param enamelColor the enamelColor to set
     */
    public void setEnamelColor(Symbol enamelColor) {
        this.enamelColor = enamelColor;
    }

    /**
     * @return the groupId
     */
    public InvGroup getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(InvGroup groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the unitId
     */
    public Symbol getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the originCountry
     */
    public Symbol getOriginCountry() {
        return originCountry;
    }

    /**
     * @param originCountry the originCountry to set
     */
    public void setOriginCountry(Symbol originCountry) {
        this.originCountry = originCountry;
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
     * @return the author
     */
    public Symbol getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(Symbol author) {
        this.author = author;
    }

    /**
     * @return the addon1
     */
    public Symbol getAddon1() {
        return addon1;
    }

    /**
     * @param addon1 the addon1 to set
     */
    public void setAddon1(Symbol addon1) {
        this.addon1 = addon1;
    }

    /**
     * @return the addon2
     */
    public Symbol getAddon2() {
        return addon2;
    }

    /**
     * @param addon2 the addon2 to set
     */
    public void setAddon2(Symbol addon2) {
        this.addon2 = addon2;
    }

    /**
     * @return the siteId
     */
    public InvOrganizationSite getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(InvOrganizationSite siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the stone
     */
    public Symbol getStone() {
        return stone;
    }

    /**
     * @param stone the stone to set
     */
    public void setStone(Symbol stone) {
        this.stone = stone;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvItemDTO)) {
            return false;
        }
        InvItemDTO other = (InvItemDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the printShow
     */
    public Integer getPrintShow() {
        return printShow;
    }

    /**
     * @param printShow the printShow to set
     */
    public void setPrintShow(Integer printShow) {
        this.printShow = printShow;
    }
}
