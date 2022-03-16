/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

/**
 *
 * @author hq002
 */
public class GlAccountReportSearchBean {
    String AccountName ; 
    Integer accountNumberFrom ;
    Integer accountNumberTo ;
    Integer levelFrom;
    Integer levelTo ;
    Integer branchId ; 
    
    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String AccountName) {
        this.AccountName = AccountName;
    }

    public Integer getAccountNumberFrom() {
        return accountNumberFrom;
    }

    public void setAccountNumberFrom(Integer accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }

    public Integer getAccountNumberTo() {
        return accountNumberTo;
    }

    public void setAccountNumberTo(Integer accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }

    public Integer getLevelFrom() {
        return levelFrom;
    }

    public void setLevelFrom(Integer levelFrom) {
        this.levelFrom = levelFrom;
    }

    public Integer getLevelTo() {
        return levelTo;
    }

    public void setLevelTo(Integer levelTo) {
        this.levelTo = levelTo;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
}
