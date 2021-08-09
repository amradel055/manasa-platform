/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_item_view")
@XmlRootElement

public class InvItemView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "unit_id")
    private Integer unitId;
    @Column(name = "type_cat")
    private Integer typeCat;
    @Column(name = "site_id")
    private Integer siteId;
    @Column(name = "brand_id")
    private Integer brandId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sell_price")
    private BigDecimal sellPrice;
    @Column(name = "undirect_cost")
    private BigDecimal undirectCost;

    @Column(name = "status_cat")
    private Integer statusCat;
    @Column(name = "contract_price")
    private BigDecimal contractPrice;
    @Column(name = "origin_country")
    private Integer originCountry;

    @Column(name = "paint_color")
    private Integer paintColor;
    @Column(name = "minimum_amount")
    private BigDecimal minimumAmount;
    @Column(name = "maxmum_amount")
    private BigDecimal maxmumAmount;
    @Column(name = "enamel_color")
    private Integer enamelColor;
    @Column(name = "last_cost")
    private BigDecimal lastCost;
    @Column(name = "stone")
    private Integer stone;
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    @Column(name = "date_create_cat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreateCat;
    @Column(name = "opening_cost")
    private BigDecimal openingCost;
    @Column(name = "stores_quality")
    private BigDecimal storesQuality;

    @Column(name = "weight_package")
    private BigDecimal weightPackage;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_by")
    private int createdBy;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "company_id")
    private int companyId;
    @Column(name = "issell")
    private Boolean issell;
    @Column(name = "ispurchase")
    private Boolean ispurchase;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "length")
    private BigDecimal length;
    @Column(name = "width")
    private BigDecimal width;
    @Column(name = "author")
    private Integer author;
    @Column(name = "addon1")
    private Integer addon1;
    @Column(name = "addon2")
    private Integer addon2;

    public InvItemView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getTypeCat() {
        return typeCat;
    }

    public void setTypeCat(Integer typeCat) {
        this.typeCat = typeCat;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
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

    public Integer getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(Integer originCountry) {
        this.originCountry = originCountry;
    }

    public Integer getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(Integer paintColor) {
        this.paintColor = paintColor;
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

    public Integer getEnamelColor() {
        return enamelColor;
    }

    public void setEnamelColor(Integer enamelColor) {
        this.enamelColor = enamelColor;
    }

    public BigDecimal getLastCost() {
        return lastCost;
    }

    public void setLastCost(BigDecimal lastCost) {
        this.lastCost = lastCost;
    }

    public Integer getStone() {
        return stone;
    }

    public void setStone(Integer stone) {
        this.stone = stone;
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


    public BigDecimal getWeightPackage() {
        return weightPackage;
    }

    public void setWeightPackage(BigDecimal weightPackage) {
        this.weightPackage = weightPackage;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public Integer getAddon1() {
        return addon1;
    }

    public void setAddon1(Integer addon1) {
        this.addon1 = addon1;
    }

    public Integer getAddon2() {
        return addon2;
    }

    public void setAddon2(Integer addon2) {
        this.addon2 = addon2;
    }

}
