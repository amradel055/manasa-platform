/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.dto.EmployeeDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import java.util.Date;

/**
 *
 * @author khale
 */
public class CrmLeadsSearchBean {
    
    private InvOrganizationSiteDTO codeFrom;
    private InvOrganizationSiteDTO codeTo;
    private Date dateFrom;
    private Date dateTo;
    private EmployeeDTO empFrom;
    private EmployeeDTO empTo;
    private SymbolDTO sourceListFrom;
    private SymbolDTO sourceListTo;
 
    public InvOrganizationSiteDTO getCodeFrom() {
        return codeFrom;
    }

    /**
     * @param codeFrom the codeFrom to set
     */
    public void setCodeFrom(InvOrganizationSiteDTO codeFrom) {
        this.codeFrom = codeFrom;
    }

    /**
     * @return the codeTo
     */
    public InvOrganizationSiteDTO getCodeTo() {
        return codeTo;
    }

    /**
     * @param codeTo the codeTo to set
     */
    public void setCodeTo(InvOrganizationSiteDTO codeTo) {
        this.codeTo = codeTo;
    }

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

    /**
     * @return the sourceListFrom
     */
    public SymbolDTO getSourceListFrom() {
        return sourceListFrom;
    }

    /**
     * @param sourceListFrom the sourceListFrom to set
     */
    public void setSourceListFrom(SymbolDTO sourceListFrom) {
        this.sourceListFrom = sourceListFrom;
    }

    /**
     * @return the sourceListTo
     */
    public SymbolDTO getSourceListTo() {
        return sourceListTo;
    }

    /**
     * @param sourceListTo the sourceListTo to set
     */
    public void setSourceListTo(SymbolDTO sourceListTo) {
        this.sourceListTo = sourceListTo;
    }

   
}
