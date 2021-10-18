/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author WIN7
 */
@Entity
@Table(name = "inv_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvItem.findAll", query = "SELECT i FROM InvItem i"),
    @NamedQuery(name = "InvItem.findById", query = "SELECT i FROM InvItem i WHERE i.id = :id"),
    @NamedQuery(name = "InvItem.findByCode", query = "SELECT i FROM InvItem i WHERE i.code = :code"),
    @NamedQuery(name = "InvItem.findByName", query = "SELECT i FROM InvItem i WHERE i.name = :name"),
    @NamedQuery(name = "InvItem.findByNameen", query = "SELECT i FROM InvItem i WHERE i.nameen = :nameen"),
    @NamedQuery(name = "InvItem.findByTypeCat", query = "SELECT i FROM InvItem i WHERE i.typeCat = :typeCat"),
    @NamedQuery(name = "InvItem.findBySellPrice", query = "SELECT i FROM InvItem i WHERE i.sellPrice = :sellPrice"),
    @NamedQuery(name = "InvItem.findByUndirectCost", query = "SELECT i FROM InvItem i WHERE i.undirectCost = :undirectCost"),
    @NamedQuery(name = "InvItem.findByRemarks", query = "SELECT i FROM InvItem i WHERE i.remarks = :remarks"),
    @NamedQuery(name = "InvItem.findByStatusCat", query = "SELECT i FROM InvItem i WHERE i.statusCat = :statusCat"),
    @NamedQuery(name = "InvItem.findByContractPrice", query = "SELECT i FROM InvItem i WHERE i.contractPrice = :contractPrice"),
    @NamedQuery(name = "InvItem.findByStorageLocation", query = "SELECT i FROM InvItem i WHERE i.storageLocation = :storageLocation"),
    @NamedQuery(name = "InvItem.findByNickname", query = "SELECT i FROM InvItem i WHERE i.nickname = :nickname"),
    @NamedQuery(name = "InvItem.findByMinimumAmount", query = "SELECT i FROM InvItem i WHERE i.minimumAmount = :minimumAmount"),
    @NamedQuery(name = "InvItem.findByMaxmumAmount", query = "SELECT i FROM InvItem i WHERE i.maxmumAmount = :maxmumAmount"),
    @NamedQuery(name = "InvItem.findByLastCost", query = "SELECT i FROM InvItem i WHERE i.lastCost = :lastCost"),
    @NamedQuery(name = "InvItem.findByCostAverage", query = "SELECT i FROM InvItem i WHERE i.costAverage = :costAverage"),
    @NamedQuery(name = "InvItem.findByDateCreateCat", query = "SELECT i FROM InvItem i WHERE i.dateCreateCat = :dateCreateCat"),
    @NamedQuery(name = "InvItem.findByOpeningCost", query = "SELECT i FROM InvItem i WHERE i.openingCost = :openingCost"),
    @NamedQuery(name = "InvItem.findByStoresQuality", query = "SELECT i FROM InvItem i WHERE i.storesQuality = :storesQuality"),
    @NamedQuery(name = "InvItem.findByImage", query = "SELECT i FROM InvItem i WHERE i.image = :image"),
    @NamedQuery(name = "InvItem.findByWeightPackage", query = "SELECT i FROM InvItem i WHERE i.weightPackage = :weightPackage"),
    @NamedQuery(name = "InvItem.findByCreationDate", query = "SELECT i FROM InvItem i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvItem.findByModificationDate", query = "SELECT i FROM InvItem i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvItem.findByIssell", query = "SELECT i FROM InvItem i WHERE i.issell = :issell"),
    @NamedQuery(name = "InvItem.findByIspurchase", query = "SELECT i FROM InvItem i WHERE i.ispurchase = :ispurchase")})
public class InvItem extends BaseEntity {

    @Column(name = "code",updatable = true)
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "nameen")
    private String nameen;
    @Column(name = "type_cat")
    private Integer typeCat;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sell_price")
    private BigDecimal sellPrice;
    @Column(name = "undirect_cost")
    private BigDecimal undirectCost;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "status_cat")
    private Integer statusCat;
    @Column(name = "contract_price")
    private BigDecimal contractPrice;
    @Column(name = "storage_location")
    private String storageLocation;
    @Column(name = "Nickname")
    private String nickname;
    @Column(name = "minimum_amount")
    private BigDecimal minimumAmount;
    @Column(name = "maxmum_amount")
    private BigDecimal maxmumAmount;
    @Column(name = "last_cost")
    private BigDecimal lastCost;
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    @Column(name = "date_create_cat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreateCat;
    @Column(name = "opening_cost")
    private BigDecimal openingCost;
    @Column(name = "stores_quality")
    private BigDecimal storesQuality;
    @Column(name = "image")
    private String image;
    @Column(name = "weight_package")
    private BigDecimal weightPackage;
    @Column(name = "issell")
    private Boolean issell;
    @Column(name = "ispurchase")
    private Boolean ispurchase;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol brandId;
    @JoinColumn(name = "enamel_color", referencedColumnName = "id")
    @ManyToOne
    private Symbol enamelColor;
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne
    private InvGroup groupId;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @JoinColumn(name = "origin_country", referencedColumnName = "id")
    @ManyToOne
    private Symbol originCountry;
    @JoinColumn(name = "paint_color", referencedColumnName = "id")
    @ManyToOne
    private Symbol paintColor;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private Symbol author;
    
    @JoinColumn(name = "addon1", referencedColumnName = "id")
    @ManyToOne
    private Symbol addon1;
    @JoinColumn(name = "addon2", referencedColumnName = "id")
    @ManyToOne
    private Symbol addon2;
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite siteId;
    @JoinColumn(name = "stone", referencedColumnName = "id")
    @ManyToOne
    private Symbol stone;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "length")
    private BigDecimal length;
    @Column(name = "width")
    private BigDecimal width;
    @Column(name = "is_inventory_item")
    private Integer isInventoryItem;
    @Column(name = "size")
    private String size;
    @JoinColumn(name = "sorting", referencedColumnName = "id")
    @ManyToOne
    private Symbol sorting;
    @Column(name = "cartons_capacity")
    private BigDecimal cartonsCapacity;
    @Column(name = "add1")
    private BigDecimal add1;
    @Column(name = "add2")
    private BigDecimal add2;
    @Column(name = "add3")
    private BigDecimal add3;
    @Column(name = "disc1")
    private BigDecimal disc1;
    @Column(name = "disc2")
    private BigDecimal disc2;
    @Column(name = "disc3")
    private BigDecimal disc3;
    @Column(name = "print_show")
    private Integer printShow;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
    
    @Transient
    private BigDecimal storeQuantity;

    public InvItem() {
    }

    public InvItem(Integer id) {
        this.id = id;
    }

    public InvItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Integer getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(Integer typeCat) {
        this.typeCat = typeCat;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getUndirectCost() {
        return undirectCost;
    }

    public void setUndirectCost(BigDecimal undirectCost) {
        this.undirectCost = undirectCost;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public BigDecimal getStoresQuality() {
        return storesQuality;
    }

    public void setStoresQuality(BigDecimal storesQuality) {
        this.storesQuality = storesQuality;
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

    public Boolean getIssell() {
        return issell;
    }

    public void setIssell(Boolean issell) {
        this.issell = issell;
    }

    public Boolean getIspurchase() {
        return ispurchase;
    }

    public void setIspurchase(Boolean ispurchase) {
        this.ispurchase = ispurchase;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Symbol getBrandId() {
        return brandId;
    }

    public void setBrandId(Symbol brandId) {
        this.brandId = brandId;
    }

    public Symbol getEnamelColor() {
        return enamelColor;
    }

    public void setEnamelColor(Symbol enamelColor) {
        this.enamelColor = enamelColor;
    }

    public InvGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(InvGroup groupId) {
        this.groupId = groupId;
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    public Symbol getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(Symbol originCountry) {
        this.originCountry = originCountry;
    }

    public Symbol getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(Symbol paintColor) {
        this.paintColor = paintColor;
    }

    public InvOrganizationSite getSiteId() {
        return siteId;
    }

    public void setSiteId(InvOrganizationSite siteId) {
        this.siteId = siteId;
    }

    public Symbol getStone() {
        return stone;
    }

    public void setStone(Symbol stone) {
        this.stone = stone;
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
        if (!(object instanceof InvItem)) {
            return false;
        }
        InvItem other = (InvItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
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

    public Symbol getAddon1() {
        return addon1;
    }

    public void setAddon1(Symbol addon1) {
        this.addon1 = addon1;
    }

    public Symbol getAddon2() {
        return addon2;
    }

    public void setAddon2(Symbol addon2) {
        this.addon2 = addon2;
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
    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

}
