/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author m lapan
 */
public class StatementOfBondsByBranche {

    private String galleryName;
    private String galleryCode;
    private String type;
    private String paymentType;
    private BigDecimal totalAmount;
    private Date dateFrom;
    private Date dateTo;

    /**
     * @return the galleryName
     */
    public String getGalleryName() {
        return galleryName;
    }

    /**
     * @param galleryName the galleryName to set
     */
    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    /**
     * @return the galleryCode
     */
    public String getGalleryCode() {
        return galleryCode;
    }

    /**
     * @param galleryCode the galleryCode to set
     */
    public void setGalleryCode(String galleryCode) {
        this.galleryCode = galleryCode;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the paymentType
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {

        if (totalAmount == null) {

            return BigDecimal.ZERO;
        }
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

}
