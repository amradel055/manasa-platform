/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.entity.InventoryDelegator;


/**
 *
 * @author hhhh
 */
public class SalesDataReportSearchBean {

    private Integer branchId;

    private Integer type;

    private InventoryDelegator fromName;
    private InventoryDelegator toName;

    private Boolean showReport;

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
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

    /**
     * @return the fromName
     */
    public InventoryDelegator getFromName() {
        return fromName;
    }

    /**
     * @param fromName the fromName to set
     */
    public void setFromName(InventoryDelegator fromName) {
        this.fromName = fromName;
    }

    /**
     * @return the toName
     */
    public InventoryDelegator getToName() {
        return toName;
    }

    /**
     * @param toName the toName to set
     */
    public void setToName(InventoryDelegator toName) {
        this.toName = toName;
    }

}
