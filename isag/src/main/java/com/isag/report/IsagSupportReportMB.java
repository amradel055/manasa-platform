/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.IsagScreenService;
import com.isag.businessservice.IsagSupportService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.searchBean.IsagSupportSearchBean;
import com.isag.converter.EmployeeDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.IsagScreenDTOConverter;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.IsagScreenDTO;
import com.isag.dto.IsagSupportDTO;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author khale
 */
@Named(value = "isagSupportReportMB")
@ViewScoped
public class IsagSupportReportMB implements Serializable {

    private IsagSupportDTO support;
    private IsagScreenDTOConverter isagScreenConverter;

    private List<EmployeeDTO> empFrom;
    private List<EmployeeDTO> empTo;
    private EmployeeDTOConverter employeeDTOConverterFrom;
    private EmployeeDTOConverter employeeDTOConverterTo;
    private List<InvOrganizationSiteDTO> customerIdFrom;
    private List<InvOrganizationSiteDTO> customerIdTo;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterFrom;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterTo;
    private List<IsagSupportDTO> supportreturn;
     @EJB
    private EmployeeService employeeService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    
    
    private UserData userData = new UserData();

  
    @EJB
    private IsagScreenService isagScreenService;
    private List<IsagScreenDTO> isagScreenList;

    @EJB
    private IsagSupportService isagSupportService;
    private IsagSupportSearchBean isagSupportSearchBean;

   
    @PostConstruct
    public void init(){
    getEmpFrom();
    }
    
    public void reset() {
        setSupport(new IsagSupportDTO());
    }
    
    public void search(){
        setSupportreturn(isagSupportService.getIsagSupportByBranch(getUserData().getUserlogin().getBranchId().getId(), getIsagSupportSearchBean()));
    }

    
  

   
    /**
     * @return the support
     */
    public IsagSupportDTO getSupport() {
        if (support == null) {
            support = new IsagSupportDTO();
        }
        return support;
    }

    public List<IsagScreenDTO> findScreenBymodeule() {
        if (support != null && support.getModule() != null) {
            setIsagScreenList(isagScreenService.getAllScreens(support.getModule()));
            setIsagScreenConverter(new IsagScreenDTOConverter(isagScreenList));
        }
        return getIsagScreenList();
    }

    /**
     * @param support the support to set
     */
    public void setSupport(IsagSupportDTO support) {
        this.support = support;
    }

    /**
     * @return the isagScreenConverter
     */
    public IsagScreenDTOConverter getIsagScreenConverter() {
        return isagScreenConverter;
    }

    /**
     * @param isagScreenConverter the isagScreenConverter to set
     */
    public void setIsagScreenConverter(IsagScreenDTOConverter isagScreenConverter) {
        this.isagScreenConverter = isagScreenConverter;
    }

    /**
     * @return the isagScreenList
     */
    public List<IsagScreenDTO> getIsagScreenList() {
        if (isagScreenList == null || isagScreenList.isEmpty()) {
            isagScreenList = new ArrayList<>();
        }
        return isagScreenList;
    }

    /**
     * @param isagScreenList the isagScreenList to set
     */
    public void setIsagScreenList(List<IsagScreenDTO> isagScreenList) {
        this.isagScreenList = isagScreenList;
    }

   
   
    /**
     * @return the empFrom
     */
    public List<EmployeeDTO> getEmpFrom() {
        if(empFrom == null || empFrom.isEmpty()){
            empFrom = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(),getUserData().getUserlogin().getBranchId().getId());
            setEmployeeDTOConverterFrom(new EmployeeDTOConverter(empFrom));
        }
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
    public List<EmployeeDTO> getEmpTo() {
         if(empTo == null || empTo.isEmpty()){
            empTo = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(),getUserData().getUserlogin().getBranchId().getId());
            setEmployeeDTOConverterTo(new EmployeeDTOConverter(empTo));
        }
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
     * @return the customerIdFrom
     */
    public List<InvOrganizationSiteDTO> getCustomerIdFrom() {
        if(customerIdFrom == null || customerIdFrom.isEmpty()){
            customerIdFrom = organizationSiteService.findLeads(getUserData().getUserBranch().getId() , null,null);
            setInvOrganizationSiteDTOConverterFrom(new InvOrganizationSiteDTOConverter((customerIdFrom)));
        }
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
    public List<InvOrganizationSiteDTO> getCustomerIdTo() {
         if(customerIdTo == null || customerIdTo.isEmpty()){
            customerIdTo = organizationSiteService.findLeads(getUserData().getUserBranch().getId(),null,null);
            setInvOrganizationSiteDTOConverterTo(new InvOrganizationSiteDTOConverter((customerIdTo)));
        }
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
     * @param invOrganizationSiteDTOConverterFrom the invOrganizationSiteDTOConverterFrom to set
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
     * @param invOrganizationSiteDTOConverterTo the invOrganizationSiteDTOConverterTo to set
     */
    public void setInvOrganizationSiteDTOConverterTo(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterTo) {
        this.invOrganizationSiteDTOConverterTo = invOrganizationSiteDTOConverterTo;
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
     * @return the isagSupportSearchBean
     */
    public IsagSupportSearchBean getIsagSupportSearchBean() {
        if(isagSupportSearchBean == null){
        isagSupportSearchBean = new IsagSupportSearchBean();
        }
        return isagSupportSearchBean;
    }

    /**
     * @param isagSupportSearchBean the isagSupportSearchBean to set
     */
    public void setIsagSupportSearchBean(IsagSupportSearchBean isagSupportSearchBean) {
        this.isagSupportSearchBean = isagSupportSearchBean;
    }

    /**
     * @return the supportreturn
     */
    public List<IsagSupportDTO> getSupportreturn() {
        return supportreturn;
    }

    /**
     * @param supportreturn the supportreturn to set
     */
    public void setSupportreturn(List<IsagSupportDTO> supportreturn) {
        this.supportreturn = supportreturn;
    }

   

}
