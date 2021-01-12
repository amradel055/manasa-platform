/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class MenuItemsViewBean {

    private Integer id;
    private String item;
    private String accountGroup;
    private String higherAccountGroup;
    private BigDecimal balance = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private BigDecimal ratio;
    private BigDecimal totalGroupBalance = BigDecimal.valueOf(0).setScale(2, BigDecimal.ROUND_UP);
    private Integer similarGroupId;
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
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(String item) {
        this.item = item;
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
     * @return the accountGroup
     */
    public String getAccountGroup() {
        return accountGroup;
    }

    /**
     * @param accountGroup the accountGroup to set
     */
    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    /**
     * @return the totalGroupBalance
     */
    public BigDecimal getTotalGroupBalance() {
        return totalGroupBalance;
    }

    /**
     * @param totalGroupBalance the totalGroupBalance to set
     */
    public void setTotalGroupBalance(BigDecimal totalGroupBalance) {
        this.totalGroupBalance = totalGroupBalance;
    }

    /**
     * @return the similarGroupId
     */
    public Integer getSimilarGroupId() {
        return similarGroupId;
    }

    /**
     * @param similarGroupId the similarGroupId to set
     */
    public void setSimilarGroupId(Integer similarGroupId) {
        this.similarGroupId = similarGroupId;
    }

    /**
     * @return the higherAccountGroup
     */
    public String getHigherAccountGroup() {
        return higherAccountGroup;
    }

    /**
     * @param higherAccountGroup the higherAccountGroup to set
     */
    public void setHigherAccountGroup(String higherAccountGroup) {
        this.higherAccountGroup = higherAccountGroup;
    }

}
