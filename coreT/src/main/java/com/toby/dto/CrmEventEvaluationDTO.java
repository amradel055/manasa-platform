/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.Date;



/**
 *
 * @author ahmed
 */
public class CrmEventEvaluationDTO extends BaseEntityDTO{

    private Integer counter;
    private Date startDate;
    private String statusName;
    private String assignedToName;

    
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

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the assignedToName
     */
    public String getAssignedToName() {
        return assignedToName;
    }

    /**
     * @param assignedToName the assignedToName to set
     */
    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    /**
     * @return the counter
     */
    public Integer getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(Integer counter) {
        this.counter = counter;
    }
    
       
}
