/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class GlBankTransactionReportEntity {

    private Date date;
    private String bankFrom;
    private String bankTo;
    private Integer bankIdFrom;
    private Integer bankIdTo;
    private String remark;
    private String source;
    private BigDecimal valueLocalImported;
    private BigDecimal valueLocalExported;

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
     * @return the bankFrom
     */
    public String getBankFrom() {
        return bankFrom;
    }

    /**
     * @param bankFrom the bankFrom to set
     */
    public void setBankFrom(String bankFrom) {
        this.bankFrom = bankFrom;
    }

    /**
     * @return the bankTo
     */
    public String getBankTo() {
        return bankTo;
    }

    /**
     * @param bankTo the bankTo to set
     */
    public void setBankTo(String bankTo) {
        this.bankTo = bankTo;
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
     * @return the bankIdFrom
     */
    public Integer getBankIdFrom() {
        return bankIdFrom;
    }

    /**
     * @param bankIdFrom the bankIdFrom to set
     */
    public void setBankIdFrom(Integer bankIdFrom) {
        this.bankIdFrom = bankIdFrom;
    }

    /**
     * @return the bankIdTo
     */
    public Integer getBankIdTo() {
        return bankIdTo;
    }

    /**
     * @param bankIdTo the bankIdTo to set
     */
    public void setBankIdTo(Integer bankIdTo) {
        this.bankIdTo = bankIdTo;
    }

    /**
     * @return the valueLocalImported
     */
    public BigDecimal getValueLocalImported() {
        return valueLocalImported;
    }

    /**
     * @param valueLocalImported the valueLocalImported to set
     */
    public void setValueLocalImported(BigDecimal valueLocalImported) {
        this.valueLocalImported = valueLocalImported;
    }

    /**
     * @return the valueLocalExported
     */
    public BigDecimal getValueLocalExported() {
        return valueLocalExported;
    }

    /**
     * @param valueLocalExported the valueLocalExported to set
     */
    public void setValueLocalExported(BigDecimal valueLocalExported) {
        this.valueLocalExported = valueLocalExported;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }
}
