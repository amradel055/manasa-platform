/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.util.Date;

/**
 *
 * @author amr
 */
public class InvoiceDiscDTOApi {

    private Integer invId;
    private Integer invSerial;
    private Date invDate;
    private Integer iosId;
    private String iosName;
    private String iosCode;
    private String iosMobile;
    private Integer delegatorId;
    private Integer branchId;

    /**
     * @return the invId
     */
    public Integer getInvId() {
        return invId;
    }

    /**
     * @param invId the invId to set
     */
    public void setInvId(Integer invId) {
        this.invId = invId;
    }

    /**
     * @return the invSerial
     */
    public Integer getInvSerial() {
        return invSerial;
    }

    /**
     * @param invSerial the invSerial to set
     */
    public void setInvSerial(Integer invSerial) {
        this.invSerial = invSerial;
    }

    /**
     * @return the invDate
     */
    public Date getInvDate() {
        return invDate;
    }

    /**
     * @param invDate the invDate to set
     */
    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    /**
     * @return the iosId
     */
    public Integer getIosId() {
        return iosId;
    }

    /**
     * @param iosId the iosId to set
     */
    public void setIosId(Integer iosId) {
        this.iosId = iosId;
    }

    /**
     * @return the iosName
     */
    public String getIosName() {
        return iosName;
    }

    /**
     * @param iosName the iosName to set
     */
    public void setIosName(String iosName) {
        this.iosName = iosName;
    }

    /**
     * @return the iosCode
     */
    public String getIosCode() {
        return iosCode;
    }

    /**
     * @param iosCode the iosCode to set
     */
    public void setIosCode(String iosCode) {
        this.iosCode = iosCode;
    }

    /**
     * @return the iosMobile
     */
    public String getIosMobile() {
        return iosMobile;
    }

    /**
     * @param iosMobile the iosMobile to set
     */
    public void setIosMobile(String iosMobile) {
        this.iosMobile = iosMobile;
    }

    /**
     * @return the delegatorId
     */
    public Integer getDelegatorId() {
        return delegatorId;
    }

    /**
     * @param delegatorId the delegatorId to set
     */
    public void setDelegatorId(Integer delegatorId) {
        this.delegatorId = delegatorId;
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
