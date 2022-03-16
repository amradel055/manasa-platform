/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

/**
 *
 * @author hq002
 */
public class GeneralBudgetReportSearchBean {
    private String itemName ; 
    private Integer companyId ;
    private Integer serialNumberFrom;
    private Integer serialNumberTo;
    
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getSerialNumberFrom() {
        return serialNumberFrom;
    }

    public void setSerialNumberFrom(Integer serialNumberFrom) {
        this.serialNumberFrom = serialNumberFrom;
    }

    public Integer getSerialNumberTo() {
        return serialNumberTo;
    }

    public void setSerialNumberTo(Integer serialNumberTo) {
        this.serialNumberTo = serialNumberTo;
    }
    
}
