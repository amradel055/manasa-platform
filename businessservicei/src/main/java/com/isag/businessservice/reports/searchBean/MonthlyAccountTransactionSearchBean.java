package com.isag.businessservice.reports.searchBean;

public class MonthlyAccountTransactionSearchBean {

    private Integer adminUnitId;
    private Integer costCenterId;
    private Integer accountIdFrom;
    private Integer accountIdTo;
    private Integer accountCodeFrom;
    private Integer accountCodeTo;
    private Integer branchId;

    /**
     * @return the adminUnitId
     */
    public Integer getAdminUnitId() {
        return adminUnitId;
    }

    /**
     * @param adminUnitId the adminUnitId to set
     */
    public void setAdminUnitId(Integer adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    /**
     * @return the costCenterId
     */
    public Integer getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the accountIdFrom
     */
    public Integer getAccountIdFrom() {
        return accountIdFrom;
    }

    /**
     * @param accountIdFrom the accountIdFrom to set
     */
    public void setAccountIdFrom(Integer accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    /**
     * @return the accountIdTo
     */
    public Integer getAccountIdTo() {
        return accountIdTo;
    }

    /**
     * @param accountIdTo the accountIdTo to set
     */
    public void setAccountIdTo(Integer accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    /**
     * @return the accountCodeFrom
     */
    public Integer getAccountCodeFrom() {
        return accountCodeFrom;
    }

    /**
     * @param accountCodeFrom the accountCodeFrom to set
     */
    public void setAccountCodeFrom(Integer accountCodeFrom) {
        this.accountCodeFrom = accountCodeFrom;
    }

    /**
     * @return the accountCodeTo
     */
    public Integer getAccountCodeTo() {
        return accountCodeTo;
    }

    /**
     * @param accountCodeTo the accountCodeTo to set
     */
    public void setAccountCodeTo(Integer accountCodeTo) {
        this.accountCodeTo = accountCodeTo;
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
