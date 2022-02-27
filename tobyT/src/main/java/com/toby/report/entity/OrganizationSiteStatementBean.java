/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class OrganizationSiteStatementBean {
    
    private String organizationName;
    private String organizationCode;
    private Integer serial;
    private Date date;
    private Date dateFromValue;
    private Integer organizationId;
    private Date dateToValue;
    
    private BigDecimal totalbalance;
    private BigDecimal totaladding;
    private BigDecimal totalexitt;
    private String ScreenName;
   
    private BigDecimal adding;
    
    private BigDecimal openningBalance;
    private BigDecimal balance;
    private BigDecimal localBalance;
    private BigDecimal exitt;

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

  

   
    /**
     * @return the ScreenName
     */
    public String getScreenName() {
        return ScreenName;
    }

    /**
     * @param ScreenName the ScreenName to set
     */
    public void setScreenName(String ScreenName) {
        this.ScreenName = ScreenName;
    }

   

    /**
     * @return the openningBalance
     */
    public BigDecimal getOpenningBalance() {
        return openningBalance;
    }

    /**
     * @param openningBalance the openningBalance to set
     */
    public void setOpenningBalance(BigDecimal openningBalance) {
        this.openningBalance = openningBalance;
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
     * @return the dateFromValue
     */
    public Date getDateFromValue() {
        return dateFromValue;
    }

    /**
     * @param dateFromValue the dateFromValue to set
     */
    public void setDateFromValue(Date dateFromValue) {
        this.dateFromValue = dateFromValue;
    }

    /**
     * @return the dateToValue
     */
    public Date getDateToValue() {
        return dateToValue;
    }

    /**
     * @param dateToValue the dateToValue to set
     */
    public void setDateToValue(Date dateToValue) {
        this.dateToValue = dateToValue;
    }

    

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

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
     * @return the organizationId
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the exitt
     */
    public BigDecimal getExitt() {
        return exitt;
    }

    /**
     * @param exitt the exitt to set
     */
    public void setExitt(BigDecimal exitt) {
        this.exitt = exitt;
    }

    /**
     * @return the adding
     */
    public BigDecimal getAdding() {
        return adding;
    }

    /**
     * @param adding the adding to set
     */
    public void setAdding(BigDecimal adding) {
        this.adding = adding;
    }

    /**
     * @return the organizationCode
     */
    public String getOrganizationCode() {
        return organizationCode;
    }

    /**
     * @param organizationCode the organizationCode to set
     */
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    /**
     * @return the totalbalance
     */
    public BigDecimal getTotalbalance() {
        return totalbalance;
    }

    /**
     * @param totalbalance the totalbalance to set
     */
    public void setTotalbalance(BigDecimal totalbalance) {
        this.totalbalance = totalbalance;
    }

    /**
     * @return the totaladding
     */
    public BigDecimal getTotaladding() {
        return totaladding;
    }

    /**
     * @param totaladding the totaladding to set
     */
    public void setTotaladding(BigDecimal totaladding) {
        this.totaladding = totaladding;
    }

    /**
     * @return the totalexitt
     */
    public BigDecimal getTotalexitt() {
        return totalexitt;
    }

    /**
     * @param totalexitt the totalexitt to set
     */
    public void setTotalexitt(BigDecimal totalexitt) {
        this.totalexitt = totalexitt;
    }

    /**
     * @return the localBalance
     */
    public BigDecimal getLocalBalance() {
        return localBalance;
    }

    /**
     * @param localBalance the localBalance to set
     */
    public void setLocalBalance(BigDecimal localBalance) {
        this.localBalance = localBalance;
    }

   

    

    
    
   
    
}
