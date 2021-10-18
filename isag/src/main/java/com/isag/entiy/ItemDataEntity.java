/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.dto.InvInventoryDTO;
import com.isag.entity.Branch;
import com.isag.entity.InvGroup;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private IsagCompany companyId;
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
    private IsagUser createdBy;
    private Date creationDate;
    private IsagUser modifiedBy;
    private Date modifiedDate;

    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;
    private Integer isinventoryitem;

    private Boolean markEdit = Boolean.FALSE;
    
    
    private Symbol sorting;
    private String size;
    private BigDecimal cartonsCapacity;
    private BigDecimal add1;
    private BigDecimal add2;
    private BigDecimal add3;
    private BigDecimal disc1;
    private BigDecimal disc2;
    private BigDecimal disc3;
    
    private BigDecimal storeQuantity;
    private Integer printShow;
    private InvInventoryDTO invInventoryId;

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

    public IsagUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
    }

    public IsagUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IsagUser modifiedBy) {
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

    public IsagCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IsagCompany companyId) {
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
        if (invBarcodeEntity == null) {
            invBarcodeEntity = new ArrayList<>();
        }
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
     * @return the sorting
     */
    public Symbol getSorting() {
        return sorting;
    }

    /**
     * @param sorting the sorting to set
     */
    public void setSorting(Symbol sorting) {
        this.sorting = sorting;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the cartonsCapacity
     */
    public BigDecimal getCartonsCapacity() {
        return cartonsCapacity;
    }

    /**
     * @param cartonsCapacity the cartonsCapacity to set
     */
    public void setCartonsCapacity(BigDecimal cartonsCapacity) {
        this.cartonsCapacity = cartonsCapacity;
    }

    /**
     * @return the add2
     */
    public BigDecimal getAdd2() {
        return add2;
    }

    /**
     * @param add2 the add2 to set
     */
    public void setAdd2(BigDecimal add2) {
        this.add2 = add2;
    }

    /**
     * @return the add3
     */
    public BigDecimal getAdd3() {
        return add3;
    }

    /**
     * @param add3 the add3 to set
     */
    public void setAdd3(BigDecimal add3) {
        this.add3 = add3;
    }

    /**
     * @return the disc1
     */
    public BigDecimal getDisc1() {
        return disc1;
    }

    /**
     * @param disc1 the disc1 to set
     */
    public void setDisc1(BigDecimal disc1) {
        this.disc1 = disc1;
    }

    /**
     * @return the disc2
     */
    public BigDecimal getDisc2() {
        return disc2;
    }

    /**
     * @param disc2 the disc2 to set
     */
    public void setDisc2(BigDecimal disc2) {
        this.disc2 = disc2;
    }

    /**
     * @return the disc3
     */
    public BigDecimal getDisc3() {
        return disc3;
    }

    /**
     * @param disc3 the disc3 to set
     */
    public void setDisc3(BigDecimal disc3) {
        this.disc3 = disc3;
    }

    /**
     * @return the add1
     */
    public BigDecimal getAdd1() {
        return add1;
    }

    /**
     * @param add1 the add1 to set
     */
    public void setAdd1(BigDecimal add1) {
        this.add1 = add1;
    }

    /**
     * @return the storeQuantity
     */
    public BigDecimal getStoreQuantity() {
        return storeQuantity;
    }

    /**
     * @param storeQuantity the storeQuantity to set
     */
    public void setStoreQuantity(BigDecimal storeQuantity) {
        this.storeQuantity = storeQuantity;
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

    /**
     * @return the invInventoryId
     */
    public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
    }
}
