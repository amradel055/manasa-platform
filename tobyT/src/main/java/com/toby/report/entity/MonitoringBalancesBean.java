/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import com.toby.entity.GlAccount;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class MonitoringBalancesBean {

    private GlAccount account;
    private BigDecimal balance;
    private Integer monitoringNumber;
    private List<MainTransactionStatementBean> mainTransactionStatementBeanMonitoringList;

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
     * @return the monitoringNumber
     */
    public Integer getMonitoringNumber() {
        return monitoringNumber;
    }

    /**
     * @param monitoringNumber the monitoringNumber to set
     */
    public void setMonitoringNumber(Integer monitoringNumber) {
        this.monitoringNumber = monitoringNumber;
    }

    /**
     * @return the mainTransactionStatementBeanMonitoringList
     */
    public List<MainTransactionStatementBean> getMainTransactionStatementBeanMonitoringList() {
        return mainTransactionStatementBeanMonitoringList;
    }

    /**
     * @param mainTransactionStatementBeanMonitoringList the mainTransactionStatementBeanMonitoringList to set
     */
    public void setMainTransactionStatementBeanMonitoringList(List<MainTransactionStatementBean> mainTransactionStatementBeanMonitoringList) {
        this.mainTransactionStatementBeanMonitoringList = mainTransactionStatementBeanMonitoringList;
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
}
