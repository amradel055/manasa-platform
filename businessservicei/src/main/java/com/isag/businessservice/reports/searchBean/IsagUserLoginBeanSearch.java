/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import java.util.Date;

/**
 *
 * @author WIN7
 */
public class IsagUserLoginBeanSearch {

    private Integer branchId;

    private String macId;
    private Date dateLogin;
    private String time;
    private String userName;
    private String isagUserCode;
    private Integer userCodeFrom;
    private Integer userCodeTo;
    private Date dateFrom;
    private Date dateTo;

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public Date getDateLogin() {
        return dateLogin;
    }

    public void setDateLogin(Date dateLogin) {
        this.dateLogin = dateLogin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsagUserCode() {
        return isagUserCode;
    }

    public void setIsagUserCode(String isagUserCode) {
        this.isagUserCode = isagUserCode;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getUserCodeFrom() {
        return userCodeFrom;
    }

    public void setUserCodeFrom(Integer userCodeFrom) {
        this.userCodeFrom = userCodeFrom;
    }

    public Integer getUserCodeTo() {
        return userCodeTo;
    }

    public void setUserCodeTo(Integer userCodeTo) {
        this.userCodeTo = userCodeTo;
    }
}
