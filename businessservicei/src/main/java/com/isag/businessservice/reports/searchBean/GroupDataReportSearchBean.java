/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.entity.InvGroup;



/**
 *
 * @author hhhh
 */
public class GroupDataReportSearchBean {

    private Integer branchId;

    private InvGroup fromGroupName;
    private InvGroup toGroupName;

    private Boolean showReport;

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
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
     * @return the fromGroupName
     */
    public InvGroup getFromGroupName() {
        return fromGroupName;
    }

    /**
     * @param fromGroupName the fromGroupName to set
     */
    public void setFromGroupName(InvGroup fromGroupName) {
        this.fromGroupName = fromGroupName;
    }

    /**
     * @return the toGroupName
     */
    public InvGroup getToGroupName() {
        return toGroupName;
    }

    /**
     * @param toGroupName the toGroupName to set
     */
    public void setToGroupName(InvGroup toGroupName) {
        this.toGroupName = toGroupName;
    }

   

}
