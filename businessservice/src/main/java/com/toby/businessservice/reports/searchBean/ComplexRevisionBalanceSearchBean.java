package com.toby.businessservice.reports.searchBean;

import java.util.Date;

public class ComplexRevisionBalanceSearchBean {

    private Date periodFrom ;
    private Date periodTo;
    private Date dateFrom;
    private Date dateTo;
    private Integer levelFrom;
    private Integer levelTo;
    private Integer accountFrom   ;
    private Integer accountTo  ;
    private Integer costCenterFrom;
    private Integer costCenterTo;
    private Integer adminUnitFrom;
    private Integer adminUnitTo;
    private Integer branchId;
    private boolean postFlag = false;
    private boolean isOpenningBalance = false;

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
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

    public Integer getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Integer getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Integer accountTo) {
        this.accountTo = accountTo;
    }

    public Integer getCostCenterFrom() {
        return costCenterFrom;
    }

    public void setCostCenterFrom(Integer costCenterFrom) {
        this.costCenterFrom = costCenterFrom;
    }

    public Integer getCostCenterTo() {
        return costCenterTo;
    }

    public void setCostCenterTo(Integer costCenterTo) {
        this.costCenterTo = costCenterTo;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
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
     * @return the postFlag
     */
    public boolean isPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(boolean postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the isOpenningBalance
     */
    public boolean isIsOpenningBalance() {
        return isOpenningBalance;
    }

    /**
     * @param isOpenningBalance the isOpenningBalance to set
     */
    public void setIsOpenningBalance(boolean isOpenningBalance) {
        this.isOpenningBalance = isOpenningBalance;
    }

    
}
