/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import com.isag.entity.InvOrganizationSite;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class SalesDataReportViewBean {
    private String code;
    private String name;
    private String mobile;
    private BigDecimal commission;
   

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
     * @return the commission
     */
    public BigDecimal getCommission() {
        return commission;
    }

    /**
     * @param commission the commission to set
     */
    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

   

    

    
    
   
    
}
