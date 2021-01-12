/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.Date;

/**
 *
 * @author H
 */
public class PrintProProductDTO {
    private  String Msg ;
    private InvPurchaseInvoiceDTO1 fromInvoice;
    private InvPurchaseInvoiceDTO1 toInvoice;
    private Date toDate;
    private Date fromDate;

    /**
     * @return the fromInvoice
     */
    public InvPurchaseInvoiceDTO1 getFromInvoice() {
        return fromInvoice;
    }

    /**
     * @param fromInvoice the fromInvoice to set
     */
    public void setFromInvoice(InvPurchaseInvoiceDTO1 fromInvoice) {
        this.fromInvoice = fromInvoice;
    }

    /**
     * @return the toInvoice
     */
    public InvPurchaseInvoiceDTO1 getToInvoice() {
        return toInvoice;
    }

    /**
     * @param toInvoice the toInvoice to set
     */
    public void setToInvoice(InvPurchaseInvoiceDTO1 toInvoice) {
        this.toInvoice = toInvoice;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the Msg
     */
    public String getMsg() {
        return Msg;
    }

    /**
     * @param Msg the Msg to set
     */
    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    
}
