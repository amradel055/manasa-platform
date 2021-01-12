package com.toby.businessservice.reports.searchBean;

import java.util.Date;

public class BalanceAccountMonthlySearchBean {
    private Integer accountNumFrom;
    private Integer accountNumTo;
    private Integer costCenterForm;
    private Integer costCenterTo;
    private Integer adminUnitForm;
    private Integer adminUnitTo;
    private Date dateFrom;
    private Date dateTo;
    private Integer levelAccount;
    private Integer accountsType;
    private Integer branchId;

    public Integer getAccountNumFrom() {
        return accountNumFrom;
    }

    public void setAccountNumFrom(Integer accountNumFrom) {
        this.accountNumFrom = accountNumFrom;
    }

    public Integer getAccountNumTo() {
        return accountNumTo;
    }

    public void setAccountNumTo(Integer accountNumTo) {
        this.accountNumTo = accountNumTo;
    }

    public Integer getCostCenterForm() {
        return costCenterForm;
    }

    public void setCostCenterForm(Integer costCenterForm) {
        this.costCenterForm = costCenterForm;
    }

    public Integer getCostCenterTo() {
        return costCenterTo;
    }

    public void setCostCenterTo(Integer costCenterTo) {
        this.costCenterTo = costCenterTo;
    }

    public Integer getAdminUnitForm() {
        return adminUnitForm;
    }

    public void setAdminUnitForm(Integer adminUnitForm) {
        this.adminUnitForm = adminUnitForm;
    }

    public Integer getAdminUnitTo() {
        return adminUnitTo;
    }

    public void setAdminUnitTo(Integer adminUnitTo) {
        this.adminUnitTo = adminUnitTo;
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

    public Integer getLevelAccount() {
        return levelAccount;
    }

    public void setLevelAccount(Integer levelAccount) {
        this.levelAccount = levelAccount;
    }

    public Integer getAccountsType() {
        return accountsType;
    }

    public void setAccountsType(Integer accountsType) {
        this.accountsType = accountsType;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
}
