/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.math.BigDecimal;

/**
 *
 * @author IT Shop
 */
public class SalesOfItemsByCompany {
    
    private Integer gallaryId;
    private String gallaryName;
    private String companyName;
    private Integer companyCode;
    private BigDecimal totalSales;
    

    /**
     * @return the gallaryId
     */
    public Integer getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(Integer gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the gallaryName
     */
    public String getGallaryName() {
        return gallaryName;
    }

    /**
     * @param gallaryName the gallaryName to set
     */
    public void setGallaryName(String gallaryName) {
        this.gallaryName = gallaryName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the totalSales
     */
    public BigDecimal getTotalSales() {
        return totalSales;
    }

    /**
     * @param totalSales the totalSales to set
     */
    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * @return the companyCode
     */
    public Integer getCompanyCode() {
        return companyCode;
    }

    /**
     * @param companyCode the companyCode to set
     */
    public void setCompanyCode(Integer companyCode) {
        this.companyCode = companyCode;
    }
    
}
