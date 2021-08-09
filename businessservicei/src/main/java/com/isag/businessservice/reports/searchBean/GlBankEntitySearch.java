/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.entity.Currency;
import com.isag.entity.GlAccount;
import com.isag.entity.GlBank;
import java.util.Date;

/**
 *
 * @author WIN7
 */
public class GlBankEntitySearch {

    private Integer branchId;
    private GlBank bankFrom;
    private GlBank bankTo;
    private Date dateFrom;
    private Date dateTo;
    private GlAccount accountFrom;
    private GlAccount accountTo;
    private Integer type;
    private Currency currencyTypeFrom;
    private Currency currencyTypeTo;

    public GlBank getBankFrom() {
        return bankFrom;
    }

    public void setBankFrom(GlBank bankFrom) {
        this.bankFrom = bankFrom;
    }

    public GlBank getBankTo() {
        return bankTo;
    }

    public void setBankTo(GlBank bankTo) {
        this.bankTo = bankTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public GlAccount getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(GlAccount accountFrom) {
        this.accountFrom = accountFrom;
    }

    public GlAccount getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(GlAccount accountTo) {
        this.accountTo = accountTo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Currency getCurrencyTypeFrom() {
        return currencyTypeFrom;
    }

    public void setCurrencyTypeFrom(Currency currencyTypeFrom) {
        this.currencyTypeFrom = currencyTypeFrom;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Currency getCurrencyTypeTo() {
        return currencyTypeTo;
    }

    public void setCurrencyTypeTo(Currency currencyTypeTo) {
        this.currencyTypeTo = currencyTypeTo;
    }
}
