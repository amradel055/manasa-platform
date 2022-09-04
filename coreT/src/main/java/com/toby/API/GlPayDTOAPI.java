/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.math.BigDecimal;

/**
 *
 * @author amr
 */
public class GlPayDTOAPI  {

    private Integer bankId;
    private String bankName;
    private BigDecimal value;
    private Integer invoiceId;
    private Integer invoiceSerial;
    private Integer serial;
    private Integer id;
    private Integer index;
    private BigDecimal remain;
    private Integer transactionId;


    public GlPayDTOAPI() {
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        if(value == null){
            value = BigDecimal.ZERO;
        }
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
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
     * @return the remain
     */
    public BigDecimal getRemain() {
        if(remain == null){
            remain = BigDecimal.ZERO;
        }
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the invoiceSerial
     */
    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    /**
     * @param invoiceSerial the invoiceSerial to set
     */
    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    /**
     * @return the transactionId
     */
    public Integer getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the bankId
     */
    public Integer getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    

  

}
