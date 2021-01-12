/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import com.toby.businessservice.reports.entityBean.MonthlyAccountTransactionBean;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author hq002
 */
public class MonthlyAccountTransactionReportBean {
    
    private Integer accountNumber ; 
    private String accountName ;
    private Collection<MonthlyAccountTransactionBean> monthlyAccountTransactionBeans = new ArrayList<>() ;

    public MonthlyAccountTransactionReportBean(Integer accountNumber , String accountName ,Collection<MonthlyAccountTransactionBean> monthlyAccountTransactionBeans ){
        this.accountName = accountName ; 
        this.accountNumber = accountNumber;
        this.monthlyAccountTransactionBeans = monthlyAccountTransactionBeans;
    }
    /**
     * @return the accountNumber
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
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
     * @return the monthlyAccountTransactionBeans
     */
    public Collection<MonthlyAccountTransactionBean> getMonthlyAccountTransactionBeans() {
        return monthlyAccountTransactionBeans;
    }

    /**
     * @param monthlyAccountTransactionBeans the monthlyAccountTransactionBeans to set
     */
    public void setMonthlyAccountTransactionBeans(Collection<MonthlyAccountTransactionBean> monthlyAccountTransactionBeans) {
        this.monthlyAccountTransactionBeans = monthlyAccountTransactionBeans;
    }


    
}
