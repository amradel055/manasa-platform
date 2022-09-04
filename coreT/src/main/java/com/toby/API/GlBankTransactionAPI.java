/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class GlBankTransactionAPI extends BaseEntityDTOAPI {

    private Date date;

    private String remark;

    private BigDecimal customerBalance;

    private Integer customerId;

    private List<GlPayDTOAPI> glPayDTOAPIList;

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the customerBalance
     */
    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    /**
     * @param customerBalance the customerBalance to set
     */
    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the glPayDTOAPIList
     */
    public List<GlPayDTOAPI> getGlPayDTOAPIList() {
        return glPayDTOAPIList;
    }

    /**
     * @param glPayDTOAPIList the glPayDTOAPIList to set
     */
    public void setGlPayDTOAPIList(List<GlPayDTOAPI> glPayDTOAPIList) {
        this.glPayDTOAPIList = glPayDTOAPIList;
    }

}
