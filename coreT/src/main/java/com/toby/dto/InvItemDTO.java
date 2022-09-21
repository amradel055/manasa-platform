/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.InvOrganizationSite;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private SymbolDTO brandId;
    private SymbolDTO enamelColor;
    private InvGroupDTO groupId;
    private SymbolDTO unitId;
    private SymbolDTO originCountry;
    private SymbolDTO paintColor;
    private SymbolDTO author;
    private SymbolDTO addon1;
    private SymbolDTO addon2;
    private InvOrganizationSite siteId;
    private SymbolDTO stone;
    private BigDecimal height;
    private BigDecimal weight;
    private BigDecimal length;
    private BigDecimal width;

    private Integer typeshow;
    private Integer itemNatural;
    private BigDecimal discountrate;
    private BigDecimal discountvalue;
    private BigDecimal maxpriceyoung;
    private BigDecimal maxpricemen;
    private BigDecimal minpriceyoung;
    private BigDecimal minpricemen;
    private BigDecimal numbermetersyoung;
    private BigDecimal numbermetersmen;
    private BigDecimal numbermetersfreeyoung;
    private BigDecimal numbermetersfreemen;
    private BigDecimal bounsepriceyoung;
    private BigDecimal bounsepricemen;
    private BigDecimal commissionrate;
    private BigDecimal commissionvalue;
    private BigDecimal price_edit;
    private Integer isinventoryitem;
    private Integer isOfferItem;
    private BigDecimal quantity;
    private ProGroupDTO proGroupId;
    private Integer typeItem;
    private Integer priceType;
    private Integer isOfferItemFinal;

    //barcode    
    private List<InvBarcodeDTO> invBarcodeEntity;
    private List<InvBarcodeDTO> invBarcodeDeletedList;
    private List<AttributeDTO> atrributesList;
    private List<VariationDTO> variationsList;

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
     * @return the groupId
     */
    public InvGroupDTO getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(InvGroupDTO groupId) {
        this.groupId = groupId;
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
     * @return the brandId
     */
    public SymbolDTO getBrandId() {
        return brandId;
    }

    /**
     * @param brandId the brandId to set
     */
    public void setBrandId(SymbolDTO brandId) {
        this.brandId = brandId;
    }

    /**
     * @return the enamelColor
     */
    public SymbolDTO getEnamelColor() {
        return enamelColor;
    }

    /**
     * @param enamelColor the enamelColor to set
     */
    public void setEnamelColor(SymbolDTO enamelColor) {
        this.enamelColor = enamelColor;
    }

    /**
     * @return the unitId
     */
    public SymbolDTO getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(SymbolDTO unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the originCountry
     */
    public SymbolDTO getOriginCountry() {
        return originCountry;
    }

    /**
     * @param originCountry the originCountry to set
     */
    public void setOriginCountry(SymbolDTO originCountry) {
        this.originCountry = originCountry;
    }

    /**
     * @return the paintColor
     */
    public SymbolDTO getPaintColor() {
        return paintColor;
    }

    /**
     * @param paintColor the paintColor to set
     */
    public void setPaintColor(SymbolDTO paintColor) {
        this.paintColor = paintColor;
    }

    /**
     * @return the author
     */
    public SymbolDTO getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(SymbolDTO author) {
        this.author = author;
    }

    /**
     * @return the addon1
     */
    public SymbolDTO getAddon1() {
        return addon1;
    }

    /**
     * @param addon1 the addon1 to set
     */
    public void setAddon1(SymbolDTO addon1) {
        this.addon1 = addon1;
    }

    /**
     * @return the addon2
     */
    public SymbolDTO getAddon2() {
        return addon2;
    }

    /**
     * @param addon2 the addon2 to set
     */
    public void setAddon2(SymbolDTO addon2) {
        this.addon2 = addon2;
    }

    /**
     * @return the stone
     */
    public SymbolDTO getStone() {
        return stone;
    }

    /**
     * @param stone the stone to set
     */
    public void setStone(SymbolDTO stone) {
        this.stone = stone;
    }

    /**
     * @return the discountrate
     */
    public BigDecimal getDiscountrate() {
        if (discountrate == null) {
            discountrate = BigDecimal.ZERO;
        }
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
        if (discountvalue == null) {
            discountvalue = BigDecimal.ZERO;
        }
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
        if (maxpriceyoung == null) {
            maxpriceyoung = BigDecimal.ZERO;
        }
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
        if (maxpricemen == null) {
            maxpricemen = BigDecimal.ZERO;
        }
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
        if (minpriceyoung == null) {
            minpriceyoung = BigDecimal.ZERO;
        }
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
        if (minpricemen == null) {
            minpricemen = BigDecimal.ZERO;
        }
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
        if (numbermetersyoung == null) {
            numbermetersyoung = BigDecimal.ZERO;
        }
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
        if (numbermetersmen == null) {
            numbermetersmen = BigDecimal.ZERO;
        }
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
        if (numbermetersfreeyoung == null) {
            numbermetersfreeyoung = BigDecimal.ZERO;
        }
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
        if (numbermetersfreemen == null) {
            numbermetersfreemen = BigDecimal.ZERO;
        }
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
        if (bounsepriceyoung == null) {
            bounsepriceyoung = BigDecimal.ZERO;
        }
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
        if (bounsepricemen == null) {
            bounsepricemen = BigDecimal.ZERO;
        }
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
        if (commissionrate == null) {
            commissionrate = BigDecimal.ZERO;
        }
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
        if (commissionvalue == null) {
            commissionvalue = BigDecimal.ZERO;
        }
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
     * @return the invBarcodeEntity
     */
    public List<InvBarcodeDTO> getInvBarcodeEntity() {
        if (invBarcodeEntity == null) {
            invBarcodeEntity = new ArrayList<>();
        }
        return invBarcodeEntity;
    }

    /**
     * @param invBarcodeEntity the invBarcodeEntity to set
     */
    public void setInvBarcodeEntity(List<InvBarcodeDTO> invBarcodeEntity) {
        this.invBarcodeEntity = invBarcodeEntity;
    }

    /**
     * @return the invBarcodeDeletedList
     */
    public List<InvBarcodeDTO> getInvBarcodeDeletedList() {
        return invBarcodeDeletedList;
    }

    /**
     * @param invBarcodeDeletedList the invBarcodeDeletedList to set
     */
    public void setInvBarcodeDeletedList(List<InvBarcodeDTO> invBarcodeDeletedList) {
        this.invBarcodeDeletedList = invBarcodeDeletedList;
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
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        if (quantity == null) {
            quantity = BigDecimal.ZERO;
        }
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the proGroupId
     */
    public ProGroupDTO getProGroupId() {
        return proGroupId;
    }

    /**
     * @param proGroupId the proGroupId to set
     */
    public void setProGroupId(ProGroupDTO proGroupId) {
        this.proGroupId = proGroupId;
    }

    /**
     * @return the typeItem
     */
    public Integer getTypeItem() {
        return typeItem;
    }

    /**
     * @param typeItem the typeItem to set
     */
    public void setTypeItem(Integer typeItem) {
        this.typeItem = typeItem;
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
     * @return the isOfferItem
     */
    public Integer getIsOfferItem() {
        return isOfferItem;
    }

    /**
     * @param isOfferItem the isOfferItem to set
     */
    public void setIsOfferItem(Integer isOfferItem) {
        this.isOfferItem = isOfferItem;
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
     * @return the isOfferItemFinal
     */
    public Integer getIsOfferItemFinal() {
        return isOfferItemFinal;
    }

    /**
     * @param isOfferItemFinal the isOfferItemFinal to set
     */
    public void setIsOfferItemFinal(Integer isOfferItemFinal) {
        this.isOfferItemFinal = isOfferItemFinal;
    }

}
