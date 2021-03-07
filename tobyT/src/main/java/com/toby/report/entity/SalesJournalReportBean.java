/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hq002
 */
public class SalesJournalReportBean {

    private Integer serialNum;
    private Date date;
    private String invInventoryName;
    private String supplierName;
    private String delegatorName;
    private BigDecimal discount;
    private BigDecimal net;
    private BigDecimal sumTotalCost;
   
  
    public Integer getSerialNum() {
        return serialNum;
    }

    /**
     * @param serialNum the serialNum to set
     */
    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

  

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
     * @return the invInventoryName
     */
    public String getInvInventoryName() {
        return invInventoryName;
    }

    /**
     * @param invInventoryName the invInventoryName to set
     */
    public void setInvInventoryName(String invInventoryName) {
        this.invInventoryName = invInventoryName;
    }

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

   
   

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the delegatorName
     */
    public String getDelegatorName() {
        return delegatorName;
    }

    /**
     * @param delegatorName the delegatorName to set
     */
    public void setDelegatorName(String delegatorName) {
        this.delegatorName = delegatorName;
    }

    /**
     * @return the sumTotalCost
     */
    public BigDecimal getSumTotalCost() {
        return sumTotalCost;
    }

    /**
     * @param sumTotalCost the sumTotalCost to set
     */
    public void setSumTotalCost(BigDecimal sumTotalCost) {
        this.sumTotalCost = sumTotalCost;
    }

    /**
     * @return the sumafterDiscount
     */
   

   

   

   

}
