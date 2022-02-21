/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.Date;

public class OffersDTO extends BaseEntityDTO {

    private String name;

    private String describtion;

    private Date dateFrom;

    private Date dateTo;

    private Integer offerType;

    private Integer offerMin;

    private Integer offerMax;

    private Integer offerValue;

    private Integer offerLimit;

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
     * @return the describtion
     */
    public String getDescribtion() {
        return describtion;
    }

    /**
     * @param describtion the describtion to set
     */
    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the offerMax
     */
    public Integer getOfferMax() {
        return offerMax;
    }

    /**
     * @param offerMax the offerMax to set
     */
    public void setOfferMax(Integer offerMax) {
        this.offerMax = offerMax;
    }

    /**
     * @return the offerLimit
     */
    public Integer getOfferLimit() {
        return offerLimit;
    }

    /**
     * @param offerLimit the offerLimit to set
     */
    public void setOfferLimit(Integer offerLimit) {
        this.offerLimit = offerLimit;
    }

    /**
     * @return the offerType
     */
    public Integer getOfferType() {
        return offerType;
    }

    /**
     * @param offerType the offerType to set
     */
    public void setOfferType(Integer offerType) {
        this.offerType = offerType;
    }

    /**
     * @return the offerMin
     */
    public Integer getOfferMin() {
        return offerMin;
    }

    /**
     * @param offerMin the offerMin to set
     */
    public void setOfferMin(Integer offerMin) {
        this.offerMin = offerMin;
    }

    /**
     * @return the offerValue
     */
    public Integer getOfferValue() {
        return offerValue;
    }

    /**
     * @param offerValue the offerValue to set
     */
    public void setOfferValue(Integer offerValue) {
        this.offerValue = offerValue;
    }

}
