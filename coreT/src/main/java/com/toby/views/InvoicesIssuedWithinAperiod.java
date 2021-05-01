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
public class InvoicesIssuedWithinAperiod {
    
    private String invoiceNumber;
    private String status;
    private Date date;
    private Date dateFrom;
    private Date dateTo;
    private String clientSerial;
    private String clientCode;
    private String clientName;
    private BigDecimal invoiceValue;
    private BigDecimal bouncedBack;
    private BigDecimal payed;
    private BigDecimal remaining;
    private BigDecimal invoiceNet;

    /**
     * @return the invoiceNumber
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return the clientSerial
     */
    public String getClientSerial() {
        return clientSerial;
    }

    /**
     * @param clientSerial the clientSerial to set
     */
    public void setClientSerial(String clientSerial) {
        this.clientSerial = clientSerial;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the invoiceValue
     */
    public BigDecimal getInvoiceValue() {
        if (invoiceValue == null) {
            return BigDecimal.ZERO;
        }
        return invoiceValue;
    }

    /**
     * @param invoiceValue the invoiceValue to set
     */
    public void setInvoiceValue(BigDecimal invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    /**
     * @return the bouncedBack
     */
    public BigDecimal getBouncedBack() {
        if (bouncedBack == null) {
            return BigDecimal.ZERO;
        }
        return bouncedBack;
    }

    /**
     * @param bouncedBack the bouncedBack to set
     */
    public void setBouncedBack(BigDecimal bouncedBack) {
        this.bouncedBack = bouncedBack;
    }

    /**
     * @return the repaid
     */
    /**
     * @return the remaining
     */
    public BigDecimal getRemaining() {
        
        if (remaining == null) {
            remaining = getInvoiceNet().subtract(getPayed());
        }
        return remaining;
    }

    /**
     * @param remaining the remaining to set
     */
    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
    }

    /**
     * @return the invoiceNet
     */
    public BigDecimal getInvoiceNet() {
        if (invoiceNet == null) {
            return BigDecimal.ZERO;
        }
        return invoiceNet;
    }

    /**
     * @param invoiceNet the invoiceNet to set
     */
    public void setInvoiceNet(BigDecimal invoiceNet) {
        this.invoiceNet = invoiceNet;
    }

    /**
     * @return the clientCode
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * @param clientCode the clientCode to set
     */
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    /**
     * @return the payed
     */
    public BigDecimal getPayed() {
        if (payed == null) {
            return BigDecimal.ZERO;
        }
        return payed;
    }

    /**
     * @param payed the payed to set
     */
    public void setPayed(BigDecimal payed) {
        this.payed = payed;
    }
    
}
