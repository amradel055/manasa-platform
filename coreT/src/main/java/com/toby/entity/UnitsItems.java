/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H
 */
@Entity
@Table(name = "units_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnitsItems.findAll", query = "SELECT u FROM UnitsItems u"),
    @NamedQuery(name = "UnitsItems.findByRowNum", query = "SELECT u FROM UnitsItems u WHERE u.rowNum = :rowNum"),
    @NamedQuery(name = "UnitsItems.findByItemId", query = "SELECT u FROM UnitsItems u WHERE u.itemId = :itemId"),
    @NamedQuery(name = "UnitsItems.findByUnitId", query = "SELECT u FROM UnitsItems u WHERE u.unitId = :unitId"),
    @NamedQuery(name = "UnitsItems.findByUnitName", query = "SELECT u FROM UnitsItems u WHERE u.unitName = :unitName"),
    @NamedQuery(name = "UnitsItems.findByScrewing", query = "SELECT u FROM UnitsItems u WHERE u.screwing = :screwing"),
    @NamedQuery(name = "UnitsItems.findByMinPriceMen", query = "SELECT u FROM UnitsItems u WHERE u.minPriceMen = :minPriceMen"),
    @NamedQuery(name = "UnitsItems.findByMaxPriceMen", query = "SELECT u FROM UnitsItems u WHERE u.maxPriceMen = :maxPriceMen"),
    @NamedQuery(name = "UnitsItems.findByMinPriceYoung", query = "SELECT u FROM UnitsItems u WHERE u.minPriceYoung = :minPriceYoung"),
    @NamedQuery(name = "UnitsItems.findByMaxPriceYoung", query = "SELECT u FROM UnitsItems u WHERE u.maxPriceYoung = :maxPriceYoung")})
public class UnitsItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_id")
    private int itemId;
    @Column(name = "unit_id")
    private Integer unitId;
    @Size(max = 255)
    @Column(name = "unitName")
    private String unitName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "min_price_men")
    private BigDecimal minPriceMen;
    @Column(name = "max_price_men")
    private BigDecimal maxPriceMen;
    @Column(name = "min_price_young")
    private BigDecimal minPriceYoung;
    @Column(name = "max_price_young")
    private BigDecimal maxPriceYoung;
    @Column(name = "discount_rate")
    private BigDecimal discountrate;
    @Transient
    private String msg;

    public UnitsItems() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getScrewing() {
        if (screwing == null) {
            screwing = BigDecimal.ONE;
        }
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public BigDecimal getMinPriceMen() {
        if (minPriceMen == null) {
            minPriceMen = BigDecimal.ZERO;
        }
        return minPriceMen;
    }

    public void setMinPriceMen(BigDecimal minPriceMen) {
        this.minPriceMen = minPriceMen;
    }

    public BigDecimal getMaxPriceMen() {
        if (maxPriceMen == null) {
            maxPriceMen = BigDecimal.ZERO;
        }
        return maxPriceMen;
    }

    public void setMaxPriceMen(BigDecimal maxPriceMen) {
        this.maxPriceMen = maxPriceMen;
    }

    public BigDecimal getMinPriceYoung() {
        if (minPriceYoung == null) {
            minPriceYoung = BigDecimal.ZERO;
        }
        return minPriceYoung;
    }

    public void setMinPriceYoung(BigDecimal minPriceYoung) {
        this.minPriceYoung = minPriceYoung;
    }

    public BigDecimal getMaxPriceYoung() {
         if (maxPriceYoung == null) {
            maxPriceYoung = BigDecimal.ZERO;
        }
        return maxPriceYoung;
    }

    public void setMaxPriceYoung(BigDecimal maxPriceYoung) {
        this.maxPriceYoung = maxPriceYoung;
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

}
