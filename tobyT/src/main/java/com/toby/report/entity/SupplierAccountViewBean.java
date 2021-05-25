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
public class SupplierAccountViewBean {

    private String organizationName;
    private Integer organizationId;
    private Integer serial;
    private Date date;
    private Date dateFromValue;

    private Date dateToValue;

    private String ScreenName;

    private BigDecimal adding;

    private BigDecimal openningBalance;
    private BigDecimal balance;
    private BigDecimal exitt; 
    private String glBankName;
    private String remark;
    private Boolean main;
    
    private Integer instContractSerial;

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
     * @return the glBankName
     */
    public String getGlBankName() {
        return glBankName;
    }

    /**
     * @param glBankName the glBankName to set
     */
    public void setGlBankName(String glBankName) {
        this.glBankName = glBankName;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the main
     */
    public Boolean getMain() {
        return main;
    }

    /**
     * @param main the main to set
     */
    public void setMain(Boolean main) {
        this.main = main;
    }

    /**
     * @return the instContractSerial
     */
    public Integer getInstContractSerial() {
        return instContractSerial;
    }

    /**
     * @param instContractSerial the instContractSerial to set
     */
    public void setInstContractSerial(Integer instContractSerial) {
        this.instContractSerial = instContractSerial;
    }

}
