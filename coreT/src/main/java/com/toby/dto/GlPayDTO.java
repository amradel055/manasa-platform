/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author amr
 */
public class GlPayDTO  {

    private GlBankDTO bank;
    private BigDecimal value;
    private Integer invoiceId;
    private Integer serial;
    private Integer id;
    private BigDecimal remain;


    public GlPayDTO() {
    }

    /**
     * @return the bank
     */
    public GlBankDTO getBank() {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(GlBankDTO bank) {
        this.bank = bank;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
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

    

  

}
