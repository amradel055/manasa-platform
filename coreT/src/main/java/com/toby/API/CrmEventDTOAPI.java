/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.util.Date;

/**
 *
 * @author User
 */
public class CrmEventDTOAPI {

    private String subject;
    private Date date;
    private Integer id;
    private String activityType;
    private String status;
    private String assignedTo;
   
    private Integer serial;
    
   
    private String customerId;
    private String SymbolPriority;
   
   
    
    private String crmType;
    private Integer invoiceId;
    private String periority;
    private String gallarySelected;
    
    private String customerCode;

    public CrmEventDTOAPI() {
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
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
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the activityType
     */
    public String getActivityType() {
        return activityType;
    }

    /**
     * @param activityType the activityType to set
     */
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the assignedTo
     */
    public String getAssignedTo() {
        return assignedTo;
    }

    /**
     * @param assignedTo the assignedTo to set
     */
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the SymbolPriority
     */
    public String getSymbolPriority() {
        return SymbolPriority;
    }

    /**
     * @param SymbolPriority the SymbolPriority to set
     */
    public void setSymbolPriority(String SymbolPriority) {
        this.SymbolPriority = SymbolPriority;
    }

    /**
     * @return the crmType
     */
    public String getCrmType() {
        return crmType;
    }

    /**
     * @param crmType the crmType to set
     */
    public void setCrmType(String crmType) {
        this.crmType = crmType;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the periority
     */
    public String getPeriority() {
        return periority;
    }

    /**
     * @param periority the periority to set
     */
    public void setPeriority(String periority) {
        this.periority = periority;
    }

    /**
     * @return the gallarySelected
     */
    public String getGallarySelected() {
        return gallarySelected;
    }

    /**
     * @param gallarySelected the gallarySelected to set
     */
    public void setGallarySelected(String gallarySelected) {
        this.gallarySelected = gallarySelected;
    }


   

}
