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
public class GlAdminUnitSearchBean {
    private Integer adminUnitFrom;
    private Integer adminUnitTo;
    private Integer branchId;

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
    public Integer getAdminUnitFrom() {
        return adminUnitFrom;
    }

    public void setAdminUnitFrom(Integer adminUnitFrom) {
        this.adminUnitFrom = adminUnitFrom;
    }

    public Integer getAdminUnitTo() {
        return adminUnitTo;
    }

    public void setAdminUnitTo(Integer adminUnitTo) {
        this.adminUnitTo = adminUnitTo;
    }
}
