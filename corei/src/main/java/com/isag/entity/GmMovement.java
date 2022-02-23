/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "gm_movement")
public class GmMovement  extends BaseEntity{
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "customer_id_from", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite customerIdFrom;
    @JoinColumn(name = "customer_id_to", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite customerIdTo;
     @JoinColumn(name = "subscribtion_id_from", referencedColumnName = "id")
    @ManyToOne
    private GmSubscriptions subscribtionIdFrom;
      @JoinColumn(name = "subscribtion_id_to", referencedColumnName = "id")
    @ManyToOne
    private GmSubscriptions subscribtionIdTo;

    @Column(name = "days")
    private Integer days;
    @NotNull
    @Column(name = "start_day")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

  

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
     * @return the customerIdFrom
     */
    public InvOrganizationSite getCustomerIdFrom() {
        return customerIdFrom;
    }

    /**
     * @param customerIdFrom the customerIdFrom to set
     */
    public void setCustomerIdFrom(InvOrganizationSite customerIdFrom) {
        this.customerIdFrom = customerIdFrom;
    }

    /**
     * @return the customerIdTo
     */
    public InvOrganizationSite getCustomerIdTo() {
        return customerIdTo;
    }

    /**
     * @param customerIdTo the customerIdTo to set
     */
    public void setCustomerIdTo(InvOrganizationSite customerIdTo) {
        this.customerIdTo = customerIdTo;
    }

    /**
     * @return the subscribtionIdFrom
     */
    public GmSubscriptions getSubscribtionIdFrom() {
        return subscribtionIdFrom;
    }

    /**
     * @param subscribtionIdFrom the subscribtionIdFrom to set
     */
    public void setSubscribtionIdFrom(GmSubscriptions subscribtionIdFrom) {
        this.subscribtionIdFrom = subscribtionIdFrom;
    }

    /**
     * @return the subscribtionIdTo
     */
    public GmSubscriptions getSubscribtionIdTo() {
        return subscribtionIdTo;
    }

    /**
     * @param subscribtionIdTo the subscribtionIdTo to set
     */
    public void setSubscribtionIdTo(GmSubscriptions subscribtionIdTo) {
        this.subscribtionIdTo = subscribtionIdTo;
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
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    
   
}
