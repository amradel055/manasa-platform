/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.entity.GlAdminUnit;

/**
 *
 * @author hq002
 */
public class AdminUnitSearchBean {

    String unitName;
    private GlAdminUnit unitCodeFrom;
    private GlAdminUnit unitCodeTo;
    Integer companyId ;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the unitCodeFrom
     */
    public GlAdminUnit getUnitCodeFrom() {
        return unitCodeFrom;
    }

    /**
     * @param unitCodeFrom the unitCodeFrom to set
     */
    public void setUnitCodeFrom(GlAdminUnit unitCodeFrom) {
        this.unitCodeFrom = unitCodeFrom;
    }

    /**
     * @return the unitCodeTo
     */
    public GlAdminUnit getUnitCodeTo() {
        return unitCodeTo;
    }

    /**
     * @param unitCodeTo the unitCodeTo to set
     */
    public void setUnitCodeTo(GlAdminUnit unitCodeTo) {
        this.unitCodeTo = unitCodeTo;
    }
    
}
