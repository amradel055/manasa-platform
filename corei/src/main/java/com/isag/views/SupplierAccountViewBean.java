/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

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
    private Integer instContractSerial;
    private String glBankName;
    private String remark;
    private Boolean main;
    private String supplierInvoiceNumber;
    private BigDecimal totalBeforeFracture;
    private BigDecimal fractureNumber;
    private BigDecimal valueFracture;
    
    private Integer idNumber;
    private Boolean look;
    private Boolean isDone;

    /**
     * @return the adding
     */
    public BigDecimal getAdding() {
        if(adding == null){
            adding = BigDecimal.ZERO;
        }
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
        if(exitt == null){
            exitt = BigDecimal.ZERO;
        }
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
        if(openningBalance == null){
            openningBalance = BigDecimal.ZERO;
        }
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
     * @return the supplierInvoiceNumber
     */
    public String getSupplierInvoiceNumber() {
        return supplierInvoiceNumber;
    }

    /**
     * @param supplierInvoiceNumber the supplierInvoiceNumber to set
     */
    public void setSupplierInvoiceNumber(String supplierInvoiceNumber) {
        this.supplierInvoiceNumber = supplierInvoiceNumber;
    }

    /**
     * @return the totalBeforeFracture
     */
    public BigDecimal getTotalBeforeFracture() {
        return totalBeforeFracture;
    }

    /**
     * @param totalBeforeFracture the totalBeforeFracture to set
     */
    public void setTotalBeforeFracture(BigDecimal totalBeforeFracture) {
        this.totalBeforeFracture = totalBeforeFracture;
    }

    /**
     * @return the fractureNumber
     */
    public BigDecimal getFractureNumber() {
        return fractureNumber;
    }

    /**
     * @param fractureNumber the fractureNumber to set
     */
    public void setFractureNumber(BigDecimal fractureNumber) {
        this.fractureNumber = fractureNumber;
    }

    /**
     * @return the valueFracture
     */
    public BigDecimal getValueFracture() {
        return valueFracture;
    }

    /**
     * @param valueFracture the valueFracture to set
     */
    public void setValueFracture(BigDecimal valueFracture) {
        this.valueFracture = valueFracture;
    }

    /**
     * @return the idNumber
     */
    public Integer getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber the idNumber to set
     */
    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return the look
     */
    public Boolean getLook() {
        if(look == null){
            look = Boolean.FALSE;
        }
        return look;
    }

    /**
     * @param look the look to set
     */
    public void setLook(Boolean look) {
        this.look = look;
    }

    /**
     * @return the isDone
     */
    public Boolean getIsDone() {
        if(isDone == null){
            isDone = Boolean.FALSE;
        }
        return isDone;
    }

    /**
     * @param isDone the isDone to set
     */
    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

}
