/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author hq002
 */
public class FinancialCenterItemsMenuReprotBean {
    
    private Integer accountGroup;
    private Integer generalBudgetId;
    private Integer id;
    private Integer glAccountNumber;
    private BigDecimal value;
    private String itemName;
    private String accountName;

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
     * @return the accountGroup
     */
    public Integer getAccountGroup() {
        return accountGroup;
    }

    /**
     * @param accountGroup the accountGroup to set
     */
    public void setAccountGroup(Integer accountGroup) {
        this.accountGroup = accountGroup;
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
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the glAccountNumber
     */
    public Integer getGlAccountNumber() {
        return glAccountNumber;
    }

    /**
     * @param glAccountNumber the glAccountNumber to set
     */
    public void setGlAccountNumber(Integer glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    /**
     * @return the generalBudgetId
     */
    public Integer getGeneralBudgetId() {
        return generalBudgetId;
    }

    /**
     * @param generalBudgetId the generalBudgetId to set
     */
    public void setGeneralBudgetId(Integer generalBudgetId) {
        this.generalBudgetId = generalBudgetId;
    }
}
