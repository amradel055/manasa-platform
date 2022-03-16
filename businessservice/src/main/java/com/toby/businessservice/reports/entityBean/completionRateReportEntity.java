/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.entityBean;

import java.math.BigDecimal;

/**
 *
 * @author Mostafa
 */
public class completionRateReportEntity {
    private Integer workUnitId;
    private BigDecimal contractQty;
    private BigDecimal currentQty;
    private String workUnitName;
    private String siteName;
    private Integer contractId;
    private BigDecimal completionRate;
    private String ownerName;
   
  
    

    /**
     * @return the workUnitId
     */
    public Integer getWorkUnitId() {
        return workUnitId;
    }

    /**
     * @param workUnitId the workUnitId to set
     */
    public void setWorkUnitId(Integer workUnitId) {
        this.workUnitId = workUnitId;
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
    
    
    
    
    
    
}
