/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author Lenovo
 */
public class GmFreezeDto  extends BaseEntityDTO {
       private InvOrganizationSiteDTO customerId;
       private Date dateto;
       private Date datefrom;
       private Integer number;
       
       private Integer mustDays;
       private Integer takeDays;
       private Integer remainDays;
       private GmSubscriptionsDTO subscriptionId ;


    /**
     * @return the customerId
     */
    public InvOrganizationSiteDTO getCustomerId() {
        if(customerId == null){
            setCustomerId(new InvOrganizationSiteDTO());
        }
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSiteDTO customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the dateto
     */
    public Date getDateto() {
        return dateto;
    }

    /**
     * @param dateto the dateto to set
     */
    public void setDateto(Date dateto) {
        this.dateto = dateto;
    }

    /**
     * @return the datefrom
     */
    public Date getDatefrom() {
        return datefrom;
    }

    /**
     * @param datefrom the datefrom to set
     */
    public void setDatefrom(Date datefrom) {
        this.datefrom = datefrom;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * @return the mustDays
     */
    public Integer getMustDays() {
        return mustDays;
    }

    /**
     * @param mustDays the mustDays to set
     */
    public void setMustDays(Integer mustDays) {
        this.mustDays = mustDays;
    }

    /**
     * @return the takeDays
     */
    public Integer getTakeDays() {
        return takeDays;
    }

    /**
     * @param takeDays the takeDays to set
     */
    public void setTakeDays(Integer takeDays) {
        this.takeDays = takeDays;
    }

    /**
     * @return the remainDays
     */
    public Integer getRemainDays() {
        return remainDays;
    }

    /**
     * @param remainDays the remainDays to set
     */
    public void setRemainDays(Integer remainDays) {
        this.remainDays = remainDays;
    }

    /**
     * @return the subscriptionId
     */
    public GmSubscriptionsDTO getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * @param subscriptionId the subscriptionId to set
     */
    public void setSubscriptionId(GmSubscriptionsDTO subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
