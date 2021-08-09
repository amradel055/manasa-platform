/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.CRMEventServiceDTO;
import com.isag.businessservice.CRMEventServiceImplDTO;
import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.CrmEventDTOConverter;
import com.isag.converter.EmployeeDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.CrmEventDTO;
import com.isag.dto.CrmEventEvaluationDTO;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.isag.AutoComplete;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author khale
 */
@Named(value = "crmEventsReportMB")
@ViewScoped
public class CrmEventsReportMB implements Serializable {

    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    private CrmEventsReportSearchBean crmEventsReportSearchBean;
    @EJB
    private EmployeeService employeeService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private AutoCompleteService autoCompleteService;
    private List<CrmEventDTO> serialFrom;
    private List<CrmEventDTO> serialTo;
    private List<CrmEventDTO> serialReturn;
    private List<InvOrganizationSiteDTO> customerIdFrom;
    private List<InvOrganizationSiteDTO> customerIdTo;
    private CrmEventDTOConverter crmEventDTOConverterFrom;
    private CrmEventDTOConverter crmEventDTOConverterTo;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterFrom;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterTo;
    private List<EmployeeDTO> empFrom;
    private List<EmployeeDTO> empTo;
    private EmployeeDTOConverter employeeDTOConverterFrom;
    private EmployeeDTOConverter employeeDTOConverterTo;
    private List<CrmEventEvaluationDTO> crmEventEvaluationDTOList;

    private UserData userData = new UserData();

    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        completeInvOrganizationSiteDTOFrom("");
        completeInvOrganizationSiteDTOTo("");
        completeEmployeeDTOFrom("");
        completeEmployeeDTOTo("");
    }

    public void reset() {
        setSerialReturn(null);
    }

    public void search() {
        setSerialReturn(cRMEventServiceDTO.getEvents(getUserData().getUserBranch().getId(), getCrmEventsReportSearchBean()));
    }

    public void evaluationSearch() {
        setCrmEventEvaluationDTOList(cRMEventServiceDTO.findEvaluationCrmEvent(getUserData().getUserBranch().getId(), getCrmEventsReportSearchBean()));
    }

    public List<InvOrganizationSiteDTO> completeInvOrganizationSiteDTOFrom(String query) {
        return AutoComplete.completOrgSiteDTO(query, getCustomerIdFrom(query), getInvOrganizationSiteDTOConverterFrom());
    }

    public List<InvOrganizationSiteDTO> completeInvOrganizationSiteDTOTo(String query) {
        return AutoComplete.completOrgSiteDTO(query, getCustomerIdTo(query), getInvOrganizationSiteDTOConverterTo());
    }

    public List<EmployeeDTO> completeEmployeeDTOFrom(String query) {
        return AutoComplete.completeEmployee(query, getEmpFrom(query), getEmployeeDTOConverterFrom());
    }

    public List<EmployeeDTO> completeEmployeeDTOTo(String query) {
        return AutoComplete.completeEmployee(query, getEmpTo(query), getEmployeeDTOConverterTo());
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the crmEventsReportSearchBean
     */
    public CrmEventsReportSearchBean getCrmEventsReportSearchBean() {
        if (crmEventsReportSearchBean == null) {
            crmEventsReportSearchBean = new CrmEventsReportSearchBean();
        }
        return crmEventsReportSearchBean;
    }

    /**
     * @param crmEventsReportSearchBean the crmEventsReportSearchBean to set
     */
    public void setCrmEventsReportSearchBean(CrmEventsReportSearchBean crmEventsReportSearchBean) {
        this.crmEventsReportSearchBean = crmEventsReportSearchBean;
    }

    /**
     * @return the serialFrom
     */
    public List<CrmEventDTO> getSerialFrom() {
        if (serialFrom == null || serialFrom.isEmpty()) {
            serialFrom = cRMEventServiceDTO.getAllEvents(getUserData().getUserBranch().getId());
            setCrmEventDTOConverterFrom(new CrmEventDTOConverter(serialFrom));
        }
        return serialFrom;
    }

    /**
     * @param serialFrom the serialFrom to set
     */
    public void setSerialFrom(List<CrmEventDTO> serialFrom) {
        this.serialFrom = serialFrom;
    }

    /**
     * @return the serialTo
     */
    public List<CrmEventDTO> getSerialTo() {
        if (serialTo == null || serialTo.isEmpty()) {
            serialTo = cRMEventServiceDTO.getAllEvents(getUserData().getUserBranch().getId());
            setCrmEventDTOConverterTo(new CrmEventDTOConverter(serialTo));
        }
        return serialTo;
    }

    /**
     * @param serialTo the serialTo to set
     */
    public void setSerialTo(List<CrmEventDTO> serialTo) {
        this.serialTo = serialTo;
    }

    /**
     * @return the serialReturn
     */
    public List<CrmEventDTO> getSerialReturn() {
        return serialReturn;
    }

    /**
     * @param serialReturn the serialReturn to set
     */
    public void setSerialReturn(List<CrmEventDTO> serialReturn) {
        this.serialReturn = serialReturn;
    }

    /**
     * @return the crmEventDTOConverterFrom
     */
    public CrmEventDTOConverter getCrmEventDTOConverterFrom() {
        return crmEventDTOConverterFrom;
    }

    /**
     * @param crmEventDTOConverterFrom the crmEventDTOConverterFrom to set
     */
    public void setCrmEventDTOConverterFrom(CrmEventDTOConverter crmEventDTOConverterFrom) {
        this.crmEventDTOConverterFrom = crmEventDTOConverterFrom;
    }

    /**
     * @return the crmEventDTOConverterTo
     */
    public CrmEventDTOConverter getCrmEventDTOConverterTo() {
        return crmEventDTOConverterTo;
    }

    /**
     * @param crmEventDTOConverterTo the crmEventDTOConverterTo to set
     */
    public void setCrmEventDTOConverterTo(CrmEventDTOConverter crmEventDTOConverterTo) {
        this.crmEventDTOConverterTo = crmEventDTOConverterTo;
    }

    /**
     * @return the customerIdFrom
     */
    public List<InvOrganizationSiteDTO> getCustomerIdFrom(String query) {
        if (query.equals(null)) {
            query = "";
        }
        customerIdFrom = autoCompleteService.autoCompleteOrganizationSite(getUserData().getUserlogin().getBranchId().getId(), Boolean.FALSE, 5, query);
        setInvOrganizationSiteDTOConverterFrom(new InvOrganizationSiteDTOConverter((customerIdFrom)));

//            customerIdFrom = organizationSiteService.findLeads(getUserData().getUserBranch().getId() , null,null);
        return customerIdFrom;
    }

    /**
     * @param customerIdFrom the customerIdFrom to set
     */
    public void setCustomerIdFrom(List<InvOrganizationSiteDTO> customerIdFrom) {
        this.customerIdFrom = customerIdFrom;
    }

    /**
     * @return the customerIdTo
     */
    public List<InvOrganizationSiteDTO> getCustomerIdTo(String query) {
        if (query.equals(null)) {
            query = "";
        }
        customerIdTo = autoCompleteService.autoCompleteOrganizationSite(getUserData().getUserlogin().getBranchId().getId(), Boolean.FALSE, 5, query);
        setInvOrganizationSiteDTOConverterTo(new InvOrganizationSiteDTOConverter((customerIdTo)));

        //   customerIdTo = organizationSiteService.findLeads(getUserData().getUserBranch().getId(), null, null);
        return customerIdTo;
    }

    /**
     * @param customerIdTo the customerIdTo to set
     */
    public void setCustomerIdTo(List<InvOrganizationSiteDTO> customerIdTo) {
        this.customerIdTo = customerIdTo;
    }

    /**
     * @return the invOrganizationSiteDTOConverterFrom
     */
    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverterFrom() {
        return invOrganizationSiteDTOConverterFrom;
    }

    /**
     * @param invOrganizationSiteDTOConverterFrom the
     * invOrganizationSiteDTOConverterFrom to set
     */
    public void setInvOrganizationSiteDTOConverterFrom(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterFrom) {
        this.invOrganizationSiteDTOConverterFrom = invOrganizationSiteDTOConverterFrom;
    }

    /**
     * @return the invOrganizationSiteDTOConverterTo
     */
    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverterTo() {
        return invOrganizationSiteDTOConverterTo;
    }

    /**
     * @param invOrganizationSiteDTOConverterTo the
     * invOrganizationSiteDTOConverterTo to set
     */
    public void setInvOrganizationSiteDTOConverterTo(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterTo) {
        this.invOrganizationSiteDTOConverterTo = invOrganizationSiteDTOConverterTo;
    }

    /**
     * @return the empFrom
     */
    public List<EmployeeDTO> getEmpFrom(String query) {

        if (query.equals(null)) {
            query = "";
        }
        empFrom = autoCompleteService.autoCompleteEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(), getUserData().getUserlogin().getBranchId().getId(), query);
        setEmployeeDTOConverterFrom(new EmployeeDTOConverter(empFrom));
        // empFrom = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(), getUserData().getUserlogin().getBranchId().getId());

        return empFrom;
    }

    /**
     * @param empFrom the empFrom to set
     */
    public void setEmpFrom(List<EmployeeDTO> empFrom) {
        this.empFrom = empFrom;
    }

    /**
     * @return the empTo
     */
    public List<EmployeeDTO> getEmpTo(String query) {
        if (query.equals(null)) {
            query = "";
        }
        empTo = autoCompleteService.autoCompleteEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(), getUserData().getUserlogin().getBranchId().getId(), query);
        setEmployeeDTOConverterTo(new EmployeeDTOConverter(empTo));

        //  empTo = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(), getUserData().getUserlogin().getBranchId().getId());
        return empTo;
    }

    /**
     * @param empTo the empTo to set
     */
    public void setEmpTo(List<EmployeeDTO> empTo) {
        this.empTo = empTo;
    }

    /**
     * @return the employeeDTOConverterFrom
     */
    public EmployeeDTOConverter getEmployeeDTOConverterFrom() {
        return employeeDTOConverterFrom;
    }

    /**
     * @param employeeDTOConverterFrom the employeeDTOConverterFrom to set
     */
    public void setEmployeeDTOConverterFrom(EmployeeDTOConverter employeeDTOConverterFrom) {
        this.employeeDTOConverterFrom = employeeDTOConverterFrom;
    }

    /**
     * @return the employeeDTOConverterTo
     */
    public EmployeeDTOConverter getEmployeeDTOConverterTo() {
        return employeeDTOConverterTo;
    }

    /**
     * @param employeeDTOConverterTo the employeeDTOConverterTo to set
     */
    public void setEmployeeDTOConverterTo(EmployeeDTOConverter employeeDTOConverterTo) {
        this.employeeDTOConverterTo = employeeDTOConverterTo;
    }

    /**
     * @return the crmEventEvaluationDTOList
     */
    public List<CrmEventEvaluationDTO> getCrmEventEvaluationDTOList() {
        if (crmEventEvaluationDTOList == null || crmEventEvaluationDTOList.isEmpty()) {
            crmEventEvaluationDTOList = new ArrayList<>();
        }
        return crmEventEvaluationDTOList;
    }

    /**
     * @param crmEventEvaluationDTOList the crmEventEvaluationDTOList to set
     */
    public void setCrmEventEvaluationDTOList(List<CrmEventEvaluationDTO> crmEventEvaluationDTOList) {
        this.crmEventEvaluationDTOList = crmEventEvaluationDTOList;
    }

}
