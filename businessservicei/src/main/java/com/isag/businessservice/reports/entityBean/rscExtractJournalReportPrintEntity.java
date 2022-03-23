/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.entityBean;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Mostafa
 */
public class rscExtractJournalReportPrintEntity {

    private BigDecimal currentQty;
    private String workUnitName;
    private String siteName;
    private Integer contractId;
    private String ownerName;
    private String workUnitNameDetail;
    private String workUnitCodeDetail;
    private Date extractDate;
    private Date fromDate;
    private Date toDate;

   
    /**
     * @return the currentQty
     */
    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    /**
     * @param currentQty the currentQty to set
     */
    public void setCurrentQty(BigDecimal currentQty) {
        this.currentQty = currentQty;
    }

    /**
     * @return the workUnitName
     */
    public String getWorkUnitName() {
        return workUnitName;
    }

    /**
     * @param workUnitName the workUnitName to set
     */
    public void setWorkUnitName(String workUnitName) {
        this.workUnitName = workUnitName;
    }

    /**
     * @return the siteName
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * @param siteName the siteName to set
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

   

    /**
     * @return the ownerName
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * @param ownerName the ownerName to set
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * @return the workUnitNameDetail
     */
    public String getWorkUnitNameDetail() {
        return workUnitNameDetail;
    }

    /**
     * @param workUnitNameDetail the workUnitNameDetail to set
     */
    public void setWorkUnitNameDetail(String workUnitNameDetail) {
        this.workUnitNameDetail = workUnitNameDetail;
    }

    /**
     * @return the extractDate
     */
    public Date getExtractDate() {
        return extractDate;
    }

    /**
     * @param extractDate the extractDate to set
     */
    public void setExtractDate(Date extractDate) {
        this.extractDate = extractDate;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the workUnitCodeDetail
     */
    public String getWorkUnitCodeDetail() {
        return workUnitCodeDetail;
    }

    /**
     * @param workUnitCodeDetail the workUnitCodeDetail to set
     */
    public void setWorkUnitCodeDetail(String workUnitCodeDetail) {
        this.workUnitCodeDetail = workUnitCodeDetail;
    }

}
