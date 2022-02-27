/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import java.util.Date;

/**
 *
 * @author ahmed
 */
public class MainTransactionStatementSearchBean {

    private Date dateFrom;
    private Date dateTo;
    private Integer accountId;
    private Integer costCenterFrom;
    private Integer costCenterTo;
    private Integer adminUnitFrom;
    private Integer adminUnitTo;
    private Integer branchId;

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the costCenterFrom
     */
    public Integer getCostCenterFrom() {
        return costCenterFrom;
    }

    /**
     * @param costCenterFrom the costCenterFrom to set
     */
    public void setCostCenterFrom(Integer costCenterFrom) {
        this.costCenterFrom = costCenterFrom;
    }

    /**
     * @return the costCenterTo
     */
    public Integer getCostCenterTo() {
        return costCenterTo;
    }

    /**
     * @param costCenterTo the costCenterTo to set
     */
    public void setCostCenterTo(Integer costCenterTo) {
        this.costCenterTo = costCenterTo;
    }

    /**
     * @return the adminUnitFrom
     */
    public Integer getAdminUnitFrom() {
        return adminUnitFrom;
    }

    /**
     * @param adminUnitFrom the adminUnitFrom to set
     */
    public void setAdminUnitFrom(Integer adminUnitFrom) {
        this.adminUnitFrom = adminUnitFrom;
    }

    /**
     * @return the adminUnitTo
     */
    public Integer getAdminUnitTo() {
        return adminUnitTo;
    }

    /**
     * @param adminUnitTo the adminUnitTo to set
     */
    public void setAdminUnitTo(Integer adminUnitTo) {
        this.adminUnitTo = adminUnitTo;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

}
