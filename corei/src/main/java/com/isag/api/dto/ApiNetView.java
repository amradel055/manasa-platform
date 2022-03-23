/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.api.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ahmedEssam
 */
public class ApiNetView {

    private Integer serial;
    private Date date;
    private String invDelegatorName;
    private String inventoryName;
    private String organizationSiteName;
    private String paymentTypeText;
    private String remarks;
    private String sumAfterDiscount;
    private BigDecimal NetBill;

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the invDelegatorName
     */
    public String getInvDelegatorName() {
        return invDelegatorName;
    }

    /**
     * @param invDelegatorName the invDelegatorName to set
     */
    public void setInvDelegatorName(String invDelegatorName) {
        this.invDelegatorName = invDelegatorName;
    }

    /**
     * @return the inventoryName
     */
    public String getInventoryName() {
        return inventoryName;
    }

    /**
     * @param inventoryName the inventoryName to set
     */
    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    /**
     * @return the organizationSiteName
     */
    public String getOrganizationSiteName() {
        return organizationSiteName;
    }

    /**
     * @param organizationSiteName the organizationSiteName to set
     */
    public void setOrganizationSiteName(String organizationSiteName) {
        this.organizationSiteName = organizationSiteName;
    }

    /**
     * @return the paymentTypeText
     */
    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    /**
     * @param paymentTypeText the paymentTypeText to set
     */
    public void setPaymentTypeText(String paymentTypeText) {
        this.paymentTypeText = paymentTypeText;
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
     * @return the sumAfterDiscount
     */
    public String getSumAfterDiscount() {
        return sumAfterDiscount;
    }

    /**
     * @param sumAfterDiscount the sumAfterDiscount to set
     */
    public void setSumAfterDiscount(String sumAfterDiscount) {
        this.sumAfterDiscount = sumAfterDiscount;
    }

    /**
     * @return the NetBill
     */
    public BigDecimal getNetBill() {
        return NetBill;
    }

    /**
     * @param NetBill the NetBill to set
     */
    public void setNetBill(BigDecimal NetBill) {
        this.NetBill = NetBill;
    }
}
