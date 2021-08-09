/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.entityBean;

import java.math.BigDecimal;

public class completionRateReportPrintEntity {
    private BigDecimal currentQty;
    private String workUnitName;
    private String siteName;
    private Integer contractId;
    private String ownerName;
    private String workUnitNameDetail;
    private BigDecimal contractQty;
    private BigDecimal completionRate;

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
     * @return the contractQty
     */
    public BigDecimal getContractQty() {
        return contractQty;
    }

    /**
     * @param contractQty the contractQty to set
     */
    public void setContractQty(BigDecimal contractQty) {
        this.contractQty = contractQty;
    }

    /**
     * @return the completionRate
     */
    public BigDecimal getCompletionRate() {
        return completionRate;
    }

    /**
     * @param completionRate the completionRate to set
     */
    public void setCompletionRate(BigDecimal completionRate) {
        this.completionRate = completionRate;
    }


}
