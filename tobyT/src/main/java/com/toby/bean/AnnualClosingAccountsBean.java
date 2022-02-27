/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bean;

import com.toby.entity.GlAccount;
import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class AnnualClosingAccountsBean {

    private Integer id;
    private GlAccount account;
    private BigDecimal ratio;
    private BigDecimal amount;

    /**
     * @return the account
     */
    public GlAccount getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(GlAccount account) {
        this.account = account;
    }

    /**
     * @return the ratio
     */
    public BigDecimal getRatio() {
        return ratio;
    }

    /**
     * @param ratio the ratio to set
     */
    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
