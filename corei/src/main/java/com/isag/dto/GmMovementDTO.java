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
public class GmMovementDTO  extends BaseEntityDTO {
    
       private InvOrganizationSiteDTO customerIdFrom;
       private InvOrganizationSiteDTO customerIdTo;
       private Date date;
       private Date stratDate;
       private Integer days;
       private GmSubscriptionsDTO subscriptionIdFrom ;
       private GmSubscriptionsDTO subscriptionIdTo ;

    /**
     * @return the customerIdFrom
     */
    public InvOrganizationSiteDTO getCustomerIdFrom() {
        if(customerIdFrom == null){
          customerIdFrom = new InvOrganizationSiteDTO();
        }
        return customerIdFrom;
    }

    /**
     * @param customerIdFrom the customerIdFrom to set
     */
    public void setCustomerIdFrom(InvOrganizationSiteDTO customerIdFrom) {
        
        this.customerIdFrom = customerIdFrom;
    }

    /**
     * @return the customerIdTo
     */
    public InvOrganizationSiteDTO getCustomerIdTo() {
        return customerIdTo;
    }

    /**
     * @param customerIdTo the customerIdTo to set
     */
    public void setCustomerIdTo(InvOrganizationSiteDTO customerIdTo) {
        this.customerIdTo = customerIdTo;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        if(date == null){
        date = new Date();
        }
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the stratDate
     */
    public Date getStratDate() {
        return stratDate;
    }

    /**
     * @param stratDate the stratDate to set
     */
    public void setStratDate(Date stratDate) {
        this.stratDate = stratDate;
    }

    /**
     * @return the days
     */
    public Integer getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * @return the subscriptionIdFrom
     */
    public GmSubscriptionsDTO getSubscriptionIdFrom() {
        return subscriptionIdFrom;
    }

    /**
     * @param subscriptionIdFrom the subscriptionIdFrom to set
     */
    public void setSubscriptionIdFrom(GmSubscriptionsDTO subscriptionIdFrom) {
        this.subscriptionIdFrom = subscriptionIdFrom;
    }

    /**
     * @return the subscriptionIdTo
     */
    public GmSubscriptionsDTO getSubscriptionIdTo() {
        if(subscriptionIdTo == null){
        subscriptionIdTo = new GmSubscriptionsDTO();
        }
        return subscriptionIdTo;
    }

    /**
     * @param subscriptionIdTo the subscriptionIdTo to set
     */
    public void setSubscriptionIdTo(GmSubscriptionsDTO subscriptionIdTo) {
        this.subscriptionIdTo = subscriptionIdTo;
    }


}

