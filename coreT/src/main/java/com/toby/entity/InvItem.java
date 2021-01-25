/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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

    /**
     * @return the proGroupId
     */
    public ProGroup getProGroupId() {
        return proGroupId;
    }

    /**
     * @param proGroupId the proGroupId to set
     */
    public void setProGroupId(ProGroup proGroupId) {
        this.proGroupId = proGroupId;
    }

    @Column(name = "code")
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
    @Column(name = "is_new")
    private Integer isNew;
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
    @JoinColumn(name = "pro_group_id", referencedColumnName = "id")
    @ManyToOne
    private ProGroup proGroupId;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "length")
    private BigDecimal length;
    @Column(name = "width")
    private BigDecimal width;
    @Column(name = "type_show")
    private Integer typeshow;
    @Column(name = "item_natural")
    private Integer itemNatural;
    @Column(name = "discount_rate")
    private BigDecimal discountrate;
    @Column(name = "discount_value")
    private BigDecimal discountvalue;
    @Column(name = "max_price_young")
    private BigDecimal maxpriceyoung;
    @Column(name = "max_price_men")
    private BigDecimal maxpricemen;
    @Column(name = "min_price_young")
    private BigDecimal minpriceyoung;
    @Column(name = "min_price_men")
    private BigDecimal minpricemen;
    @Column(name = "number_meters_young")
    private BigDecimal numbermetersyoung;
    @Column(name = "number_meters_men")
    private BigDecimal numbermetersmen;
    @Column(name = "number_meters_free_young")
    private BigDecimal numbermetersfreeyoung;
    @Column(name = "number_meters_free_men")
    private BigDecimal numbermetersfreemen;
    @Column(name = "bounse_price_young")
    private BigDecimal bounsepriceyoung;
    @Column(name = "bounse_price_men")
    private BigDecimal bounsepricemen;
    @Column(name = "commission_rate")
    private BigDecimal commissionrate;
    @Column(name = "commission_value")
    private BigDecimal commissionvalue;
    @Column(name = "price_edit")
    private BigDecimal price_edit;
    @Column(name = "is_inventory_item")
    private Integer isInventoryItem;
    @Column(name = "quantity")
    private BigDecimal quantity;

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
     * @return the typeshow
     */
    public Integer getTypeshow() {
        return typeshow;
    }

    /**
     * @param typeshow the typeshow to set
     */
    public void setTypeshow(Integer typeshow) {
        this.typeshow = typeshow;
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
     * @return the itemNatural
     */
    public Integer getItemNatural() {
        return itemNatural;
    }

    /**
     * @param itemNatural the itemNatural to set
     */
    public void setItemNatural(Integer itemNatural) {
        this.itemNatural = itemNatural;
    }

    /**
     * @return the isNew
     */
    public Integer getIsNew() {
        return isNew;
    }

    /**
     * @param isNew the isNew to set
     */
    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

}
