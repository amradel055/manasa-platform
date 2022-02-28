/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.entity.Branch;
import com.toby.entity.InvGroup;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class ItemDataEntity implements Serializable {

    private Integer id;
    private String code;
    private String name;
    private String nameen;
    private InvOrganizationSite siteId;
    private BigDecimal undirectCost;
    private Integer statusCat;
    private BigDecimal contractPrice;
    private String storageLocation;
    private BigDecimal minimumAmount;
    private BigDecimal maxmumAmount;
    private BigDecimal lastCost;
    private BigDecimal costAverage;
    private Date dateCreateCat;
    private BigDecimal openingCost;
    private BigDecimal storesQuantity;
    private String image;
    private BigDecimal weightPackage;
    private Symbol brandId;
    private Symbol enamelColorId;
    private Symbol unitId;
    private String unitName;
    private Symbol addon1;
    private Symbol addon2;
    private InvGroup groupId;
    private Symbol originCountryId;
    private Symbol paintColor;
    private Branch branchId;
    private TobyCompany companyId;
    private String groupName;
    private String nickName;
    private BigDecimal itemSize;
    private Integer typeCat; // which means Item Kind
    private BigDecimal supplierId;
    private BigDecimal sellPrice;
    private String remarks;
    private Integer invBarcodeItemsId;
    private Symbol stoneId;
    private List<InvBarcodeEntity> invBarcodeEntity;
    private Boolean isSell;
    private Boolean isPurchase;
    private BigDecimal storeAmmount;
    private TobyUser createdBy;
    private Date creationDate;
    private TobyUser modifiedBy;
    private Date modifiedDate;

    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;

    private Boolean markEdit = Boolean.FALSE;
    
    
    private BigDecimal maxpriceyoung;
    private BigDecimal maxpricemen;
    private BigDecimal minpriceyoung;
    private BigDecimal minpricemen;
    private BigDecimal maxpriceyoungLast;
    private BigDecimal maxpricemenLast;
    private BigDecimal minpriceyoungLast;
    private BigDecimal minpricemenLast;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    public InvOrganizationSite getSiteId() {
        return siteId;
    }

    public void setSiteId(InvOrganizationSite siteId) {
        this.siteId = siteId;
    }

    public BigDecimal getUndirectCost() {
        return undirectCost;
    }

    public void setUndirectCost(BigDecimal undirectCost) {
        this.undirectCost = undirectCost;
    }

    public Integer getStatusCat() {
        return statusCat;
    }

    public void setStatusCat(Integer statusCat) {
        this.statusCat = statusCat;
    }

    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public BigDecimal getMaxmumAmount() {
        return maxmumAmount;
    }

    public void setMaxmumAmount(BigDecimal maxmumAmount) {
        this.maxmumAmount = maxmumAmount;
    }

    public BigDecimal getLastCost() {
        return lastCost;
    }

    public void setLastCost(BigDecimal lastCost) {
        this.lastCost = lastCost;
    }

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    public Date getDateCreateCat() {
        return dateCreateCat;
    }

    public void setDateCreateCat(Date dateCreateCat) {
        this.dateCreateCat = dateCreateCat;
    }

    public BigDecimal getOpeningCost() {
        return openingCost;
    }

    public void setOpeningCost(BigDecimal openingCost) {
        this.openingCost = openingCost;
    }

    public BigDecimal getStoresQuantity() {
        return storesQuantity;
    }

    public void setStoresQuantity(BigDecimal storesQuantity) {
        this.storesQuantity = storesQuantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getWeightPackage() {
        return weightPackage;
    }

    public void setWeightPackage(BigDecimal weightPackage) {
        this.weightPackage = weightPackage;
    }

    public Symbol getBrandId() {
        return brandId;
    }

    public void setBrandId(Symbol brandId) {
        this.brandId = brandId;
    }

    public Boolean getIsSell() {
        return isSell;
    }

    public void setIsSell(Boolean isSell) {
        this.isSell = isSell;
    }

    public Boolean getIsPurchase() {
        return isPurchase;
    }

    public void setIsPurchase(Boolean isPurchase) {
        this.isPurchase = isPurchase;
    }

    public BigDecimal getStoreAmmount() {
        return storeAmmount;
    }

    public void setStoreAmmount(BigDecimal storeAmmount) {
        this.storeAmmount = storeAmmount;
    }

    public Boolean getMarkEdit() {
        return markEdit;
    }

    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public TobyUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(TobyUser createdBy) {
        this.createdBy = createdBy;
    }

    public TobyUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(TobyUser modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public Symbol getEnamelColorId() {
        return enamelColorId;
    }

    public void setEnamelColorId(Symbol enamelColorId) {
        this.enamelColorId = enamelColorId;
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    public InvGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(InvGroup groupId) {
        this.groupId = groupId;
    }

    public Symbol getOriginCountryId() {
        return originCountryId;
    }

    public void setOriginCountryId(Symbol originCountryId) {
        this.originCountryId = originCountryId;
    }

    public Symbol getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(Symbol paintColor) {
        this.paintColor = paintColor;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public TobyCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(TobyCompany companyId) {
        this.companyId = companyId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public BigDecimal getItemSize() {
        return itemSize;
    }

    public void setItemSize(BigDecimal itemSize) {
        this.itemSize = itemSize;
    }

    public Integer getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(Integer typeCat) {
        this.typeCat = typeCat;
    }

    public BigDecimal getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(BigDecimal supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getInvBarcodeItemsId() {
        return invBarcodeItemsId;
    }

    public void setInvBarcodeItemsId(Integer invBarcodeItemsId) {
        this.invBarcodeItemsId = invBarcodeItemsId;
    }

    public Symbol getStoneId() {
        return stoneId;
    }

    public void setStoneId(Symbol stoneId) {
        this.stoneId = stoneId;
    }

    public List<InvBarcodeEntity> getInvBarcodeEntity() {
        return invBarcodeEntity;
    }

    public void setInvBarcodeEntity(List<InvBarcodeEntity> invBarcodeEntity) {
        this.invBarcodeEntity = invBarcodeEntity;
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
     * @return the maxpriceyoungLast
     */
    public BigDecimal getMaxpriceyoungLast() {
        return maxpriceyoungLast;
    }

    /**
     * @param maxpriceyoungLast the maxpriceyoungLast to set
     */
    public void setMaxpriceyoungLast(BigDecimal maxpriceyoungLast) {
        this.maxpriceyoungLast = maxpriceyoungLast;
    }

    /**
     * @return the maxpricemenLast
     */
    public BigDecimal getMaxpricemenLast() {
        return maxpricemenLast;
    }

    /**
     * @param maxpricemenLast the maxpricemenLast to set
     */
    public void setMaxpricemenLast(BigDecimal maxpricemenLast) {
        this.maxpricemenLast = maxpricemenLast;
    }

    /**
     * @return the minpriceyoungLast
     */
    public BigDecimal getMinpriceyoungLast() {
        return minpriceyoungLast;
    }

    /**
     * @param minpriceyoungLast the minpriceyoungLast to set
     */
    public void setMinpriceyoungLast(BigDecimal minpriceyoungLast) {
        this.minpriceyoungLast = minpriceyoungLast;
    }

    /**
     * @return the minpricemenLast
     */
    public BigDecimal getMinpricemenLast() {
        return minpricemenLast;
    }

    /**
     * @param minpricemenLast the minpricemenLast to set
     */
    public void setMinpricemenLast(BigDecimal minpricemenLast) {
        this.minpricemenLast = minpricemenLast;
    }
}
