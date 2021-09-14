/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.api.dto;

import java.util.Date;

/**
 *
 * @author ahmedEssam
 */
public class ApiInvInvoiceDTO {

    private Integer serial;
    private String fromDate;
    private String toDate;
    private Integer fromCustomer;
    private Integer toCustomer;
    private String  fromDelegator;
    private String  toDelegator;
    private Integer fromSerial;
    private Integer toSerial;
    private Integer type;

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
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the fromCustomer
     */
    public Integer getFromCustomer() {
        return fromCustomer;
    }

    /**
     * @param fromCustomer the fromCustomer to set
     */
    public void setFromCustomer(Integer fromCustomer) {
        this.fromCustomer = fromCustomer;
    }

    /**
     * @return the toCustomer
     */
    public Integer getToCustomer() {
        return toCustomer;
    }

    /**
     * @param toCustomer the toCustomer to set
     */
    public void setToCustomer(Integer toCustomer) {
        this.toCustomer = toCustomer;
    }

    /**
     * @return the fromDelegator
     */
    public String getFromDelegator() {
        return fromDelegator;
    }

    /**
     * @param fromDelegator the fromDelegator to set
     */
    public void setFromDelegator(String fromDelegator) {
        this.fromDelegator = fromDelegator;
    }

    /**
     * @return the toDelegator
     */
    public String getToDelegator() {
        return toDelegator;
    }

    /**
     * @param toDelegator the toDelegator to set
     */
    public void setToDelegator(String toDelegator) {
        this.toDelegator = toDelegator;
    }

    /**
     * @return the fromSerial
     */
    public Integer getFromSerial() {
        return fromSerial;
    }

    /**
     * @param fromSerial the fromSerial to set
     */
    public void setFromSerial(Integer fromSerial) {
        this.fromSerial = fromSerial;
    }

    /**
     * @return the toSerial
     */
    public Integer getToSerial() {
        return toSerial;
    }

    /**
     * @param toSerial the toSerial to set
     */
    public void setToSerial(Integer toSerial) {
        this.toSerial = toSerial;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }
}
