/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.dto.CrmEventDTO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.InstProjectGuideDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import java.util.Date;

/**
 *
 * @author khale
 */
public class CrmEventsReportSearchBean {

    private CrmEventDTO crmEventDTOFrom;
    private CrmEventDTO crmEventDTOTo;
    private Date dateFrom;
    private Date dateTo;
    private Integer type;
    private InvOrganizationSiteDTO leadsFrom;
    private InvOrganizationSiteDTO leadsTo;
    private InstProjectGuideDTO instProjectGuideDTOFrom;
    private InstProjectGuideDTO instProjectGuideDTOTo;
    private EmployeeDTO empFrom;
    private EmployeeDTO empTo;
    
    

    /**
     * @return the crmEventDTOFrom
     */
    public CrmEventDTO getCrmEventDTOFrom() {
        return crmEventDTOFrom;
    }

    /**
     * @param crmEventDTOFrom the crmEventDTOFrom to set
     */
    public void setCrmEventDTOFrom(CrmEventDTO crmEventDTOFrom) {
        this.crmEventDTOFrom = crmEventDTOFrom;
    }

    /**
     * @return the crmEventDTOTo
     */
    public CrmEventDTO getCrmEventDTOTo() {
        return crmEventDTOTo;
    }
    

    /**
     * @param crmEventDTOTo the crmEventDTOTo to set
     */
    public void setCrmEventDTOTo(CrmEventDTO crmEventDTOTo) {
        this.crmEventDTOTo = crmEventDTOTo;
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
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the leadsFrom
     */
    public InvOrganizationSiteDTO getLeadsFrom() {
        return leadsFrom;
    }

    /**
     * @param leadsFrom the leadsFrom to set
     */
    public void setLeadsFrom(InvOrganizationSiteDTO leadsFrom) {
        this.leadsFrom = leadsFrom;
    }

    /**
     * @return the leadsTo
     */
    public InvOrganizationSiteDTO getLeadsTo() {
        return leadsTo;
    }

    /**
     * @param leadsTo the leadsTo to set
     */
    public void setLeadsTo(InvOrganizationSiteDTO leadsTo) {
        this.leadsTo = leadsTo;
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
     * @return the instProjectGuideDTOFrom
     */
    public InstProjectGuideDTO getInstProjectGuideDTOFrom() {
        return instProjectGuideDTOFrom;
    }

    /**
     * @param instProjectGuideDTOFrom the instProjectGuideDTOFrom to set
     */
    public void setInstProjectGuideDTOFrom(InstProjectGuideDTO instProjectGuideDTOFrom) {
        this.instProjectGuideDTOFrom = instProjectGuideDTOFrom;
    }

    /**
     * @return the instProjectGuideDTOTo
     */
    public InstProjectGuideDTO getInstProjectGuideDTOTo() {
        return instProjectGuideDTOTo;
    }

    /**
     * @param instProjectGuideDTOTo the instProjectGuideDTOTo to set
     */
    public void setInstProjectGuideDTOTo(InstProjectGuideDTO instProjectGuideDTOTo) {
        this.instProjectGuideDTOTo = instProjectGuideDTOTo;
    }

    /**
     * @return the customerIdFrom
     */
   
}
