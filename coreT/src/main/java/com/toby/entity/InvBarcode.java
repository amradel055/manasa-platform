/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "inv_barcode")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvBarcode.findAll", query = "SELECT i FROM InvBarcode i"),
    @NamedQuery(name = "InvBarcode.findById", query = "SELECT i FROM InvBarcode i WHERE i.id = :id"),
    @NamedQuery(name = "InvBarcode.findByCode", query = "SELECT i FROM InvBarcode i WHERE i.code = :code"),
    @NamedQuery(name = "InvBarcode.findByPrice1", query = "SELECT i FROM InvBarcode i WHERE i.price1 = :price1"),
    @NamedQuery(name = "InvBarcode.findByScrewing", query = "SELECT i FROM InvBarcode i WHERE i.screwing = :screwing"),
    @NamedQuery(name = "InvBarcode.findByCreationDate", query = "SELECT i FROM InvBarcode i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvBarcode.findByModificationDate", query = "SELECT i FROM InvBarcode i WHERE i.modificationDate = :modificationDate")})
public class InvBarcode extends BaseEntity {

    @Size(max = 450)
    @Column(name = "code")
    private String code;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price1")
    private BigDecimal price1;
    @Column(name = "screwing")
    private BigDecimal screwing;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvItem itemId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "type_barcode", referencedColumnName = "id")
    @ManyToOne
    private Symbol typeBarcode;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @Column(name = "height")
    private BigDecimal height;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "length")
    private BigDecimal length;
    @Column(name = "width")
    private BigDecimal width;
    @JoinColumn(name = "paint_color", referencedColumnName = "id")
    @ManyToOne
    private Symbol paintColor;
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
    private Integer isinventoryitem;
    @JoinColumn(name = "type_show", referencedColumnName = "id")
    @ManyToOne
    private Symbol typeshow;
    @JoinColumn(name = "item_natural", referencedColumnName = "id")
    @ManyToOne
    private Symbol item_natural;

    public InvBarcode() {
    }

    public InvBarcode(Integer id) {
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

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvBarcode)) {
            return false;
        }
        InvBarcode other = (InvBarcode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.InvBarcode[ id=" + id + " ]";
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
     * @return the typeshow
     */
    public Symbol getTypeshow() {
        return typeshow;
    }

    /**
     * @param typeshow the typeshow to set
     */
    public void setTypeshow(Symbol typeshow) {
        this.typeshow = typeshow;
    }

    /**
     * @return the item_natural
     */
    public Symbol getItem_natural() {
        return item_natural;
    }

    /**
     * @param item_natural the item_natural to set
     */
    public void setItem_natural(Symbol item_natural) {
        this.item_natural = item_natural;
    }

}
