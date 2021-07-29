/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvPurchaseInvoiceDTO;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author m lapan
 */
public class InvoicesRegisteredMoreThanOnce {

    private Integer serial;
    private String stageName;
    private String itemName;
    private BigDecimal workNumber;
    private BigDecimal originNumber;
    private InvPurchaseInvoiceDTO serialSelected;
    private Date date;
    private Date receivedDate ;
    private BigDecimal subtraction;
    private Integer stageId;
    private Integer factoryId;

    /**
     * @return the stageName
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * @param stageName the stageName to set
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the workNumber
     */
    public BigDecimal getWorkNumber() {
        if (workNumber == null) {
            workNumber = BigDecimal.ZERO;
        }
        return workNumber;
    }

    /**
     * @param workNumber the workNumber to set
     */
    public void setWorkNumber(BigDecimal workNumber) {
        this.workNumber = workNumber;
    }

    /**
     * @return the originNumber
     */
    public BigDecimal getOriginNumber() {
        if (originNumber == null) {
            originNumber = BigDecimal.ZERO;
        }
        return originNumber;
    }

    /**
     * @param originNumber the originNumber to set
     */
    public void setOriginNumber(BigDecimal originNumber) {
        this.originNumber = originNumber;
    }

    /**
     * @return the serialSelected
     */
    public InvPurchaseInvoiceDTO getSerialSelected() {
        return serialSelected;
    }

    /**
     * @param serialSelected the serialSelected to set
     */
    public void setSerialSelected(InvPurchaseInvoiceDTO serialSelected) {
        this.serialSelected = serialSelected;
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
     * @return the subtraction
     */
    public BigDecimal getSubtraction() {
        if (subtraction == null) {
            subtraction = getWorkNumber().subtract(getOriginNumber());
        }
        return subtraction;
    }

    /**
     * @param subtraction the subtraction to set
     */
    public void setSubtraction(BigDecimal subtraction) {
        this.subtraction = subtraction;
    }

    /**
     * @return the stageId
     */
    public Integer getStageId() {
        return stageId;
    }

    /**
     * @param stageId the stageId to set
     */
    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    /**
     * @return the factoryId
     */
    public Integer getFactoryId() {
        return factoryId;
    }

    /**
     * @param factoryId the factoryId to set
     */
    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    /**
     * @return the receivedDate
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

}
