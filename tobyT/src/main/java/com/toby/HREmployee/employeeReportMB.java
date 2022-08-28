/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.HREmployee;

import com.toby.businessservice.EmployeeService;
import com.toby.businessservice.reports.searchBean.EmployeeReportSearchBean;
import com.toby.converter.EmployeeDTOConverter;
import com.toby.dto.EmployeeDTO;
import com.toby.toby.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Ahmed Shaaban
 */
@Named(value = "employeeReportMB")
@ViewScoped
public class employeeReportMB implements Serializable {

    @EJB
    EmployeeService employeeService;
    private List<EmployeeDTO> employeeDTOList;
    private EmployeeDTO employeeDTO;
    private List<EmployeeDTO> empFrom;
    private List<EmployeeDTO> empTo;
    private EmployeeDTOConverter employeeDTOConverterFrom;
    private EmployeeDTOConverter employeeDTOConverterTo;
    private EmployeeReportSearchBean employeeReportSearchBean;
    private List<EmployeeDTO> employeeReturn;

    private UserData userData = new UserData();

    @PostConstruct
    public void init() {
        getEmpFrom();
    }

    public void search() {
        setEmployeeReturn(employeeService.getEmployeebyBranchId(getUserData().getUserlogin().getBranchId().getId(), getEmployeeReportSearchBean()));
    }

    /**
     * @return the employeeDTOList
     */
    public List<EmployeeDTO> getEmployeeDTOList() {
        if (employeeDTOList == null || employeeDTOList.isEmpty()) {
            employeeDTOList = new ArrayList<>();
        }
        return employeeDTOList;
    }

    /**
     * @param employeeDTOList the employeeDTOList to set
     */
    public void setEmployeeDTOList(List<EmployeeDTO> employeeDTOList) {
        this.employeeDTOList = employeeDTOList;
    }

    /**
     * @return the employeeDTOSelected
     */
    /**
     * @return the empFrom
     */
    public List<EmployeeDTO> getEmpFrom() {
          if (empFrom == null || empFrom.isEmpty()) {
            empFrom = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId());
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
        if (empTo == null || empTo.isEmpty()) {
            empTo = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId());
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
     * @return the employeeReportSearchBean
     */
    public EmployeeReportSearchBean getEmployeeReportSearchBean() {
        if(employeeReportSearchBean == null){
        employeeReportSearchBean = new EmployeeReportSearchBean();
        }
        return employeeReportSearchBean;
    }

    /**
     * @param employeeReportSearchBean the employeeReportSearchBean to set
     */
    public void setEmployeeReportSearchBean(EmployeeReportSearchBean employeeReportSearchBean) {
        this.employeeReportSearchBean = employeeReportSearchBean;
    }

    /**
     * @return the employeeReturn
     */
    public List<EmployeeDTO> getEmployeeReturn() {
         if (employeeReturn == null || employeeReturn.isEmpty())
        {
        employeeReturn = new ArrayList<>();
        }
        return employeeReturn;
    }

    /**
     * @param employeeReturn the employeeReturn to set
     */
    public void setEmployeeReturn(List<EmployeeDTO> employeeReturn) {
        this.employeeReturn = employeeReturn;
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
     * @return the employeeDTO
     */
    public EmployeeDTO getEmployeeDTO() {
         if ( employeeDTO == null){
         employeeDTO = new EmployeeDTO();
        
        }
        return employeeDTO;
    }

    /**
     * @param employeeDTO the employeeDTO to set
     */
    public void setEmployeeDTO(EmployeeDTO employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

}
