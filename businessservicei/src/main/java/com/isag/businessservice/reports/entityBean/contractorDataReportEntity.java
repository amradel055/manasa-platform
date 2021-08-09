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
 * @author hhhh
 */
public class contractorDataReportEntity {
    private String  name;
    private String  phone;
    private String  code;
    private Integer type;
    private String  fax;
    private String  email;
    private String  mobile;
    private String  address;
    private BigDecimal openBalance;
    private Date openBalanceDate;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the openBalance
     */
    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    /**
     * @param openBalance the openBalance to set
     */
    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    /**
     * @return the openBalanceDate
     */
    public Date getOpenBalanceDate() {
        return openBalanceDate;
    }

    /**
     * @param openBalanceDate the openBalanceDate to set
     */
    public void setOpenBalanceDate(Date openBalanceDate) {
        this.openBalanceDate = openBalanceDate;
    }
    
    
    
    
}
