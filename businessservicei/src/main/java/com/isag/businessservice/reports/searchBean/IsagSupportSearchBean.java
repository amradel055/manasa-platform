/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.EmployeeDTO;
import java.util.Date;

/**
 *
 * @author khale
 */
public class IsagSupportSearchBean {

    
    private Date dateFrom;
    private Date dateTo;
   
   
    private EmployeeDTO empFrom;
    private EmployeeDTO empTo;
    
    

    /**
     * @return the crmEventDTOFrom
     */
  
    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the type
     */
    

    /**
     * @return the empFrom
     */
    public EmployeeDTO getEmpFrom() {
        return empFrom;
    }

    /**
     * @param empFrom the empFrom to set
     */
    public void setEmpFrom(EmployeeDTO empFrom) {
        this.empFrom = empFrom;
    }

    /**
     * @return the empTo
     */
    public EmployeeDTO getEmpTo() {
        return empTo;
    }

    /**
     * @param empTo the empTo to set
     */
    public void setEmpTo(EmployeeDTO empTo) {
        this.empTo = empTo;
    }

   
   
}
