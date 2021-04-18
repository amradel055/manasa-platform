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
public class ClientsNoMovment {

    private String clientNumber;
    private String clientName;
    private String phoneNumber;
    private Date lastInvoiceDate;
    private BigDecimal totalInvoices;

    /**
     * @return the clientNumber
     */
    public String getClientNumber() {
        return clientNumber;
    }

    /**
     * @param clientNumber the clientNumber to set
     */
    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
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
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the lastInvoiceDate
     */
    public Date getLastInvoiceDate() {
        return lastInvoiceDate;
    }

    /**
     * @param lastInvoiceDate the lastInvoiceDate to set
     */
    public void setLastInvoiceDate(Date lastInvoiceDate) {
        this.lastInvoiceDate = lastInvoiceDate;
    }

    /**
     * @return the totalInvoices
     */
    public BigDecimal getTotalInvoices() {
        return totalInvoices;
    }

    /**
     * @param totalInvoices the totalInvoices to set
     */
    public void setTotalInvoices(BigDecimal totalInvoices) {
        this.totalInvoices = totalInvoices;
    }

}
