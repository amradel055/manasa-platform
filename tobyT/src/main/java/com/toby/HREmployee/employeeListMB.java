/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.HREmployee;

import com.toby.businessservice.EmployeeService;
import com.toby.dto.EmployeeDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author Ahmed Shaaban
 */
@Named(value = "employeeListMB")
@ViewScoped
public class employeeListMB extends BaseListBean {

    @EJB
    EmployeeService employeeService;
    private List<EmployeeDTO> employeeDTOList;
    private EmployeeDTO employeeDTOSelected;
    private EmployeeDTO employeeDTO;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "employeeListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getEmployeeDTOSelected() != null) {
                try {
                    employeeService.deleteEmployee(getEmployeeDTOSelected(), getUserData().getUser());
                    getEmployeeDTOList().remove(getEmployeeDTOSelected());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "employeeListMB", "delete");
        }
    }

    @Override
    public void filter() {
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddemployeeDTOSelected");

            if (getUri().contains("rememployee")) {
                exit("../rememployee/employeeForm.xhtml");
                } else if (getUri().contains("remdrivers")) {
                     exit("../remdrivers/employeeForm.xhtml");
            } else {
                exit("../hremployee/employeeForm.xhtml");
            }

            return "";
        } catch (Exception e) {
            saveError(e, "employeeListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getEmployeeDTOSelected() != null && getEmployeeDTOSelected().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("employeeDTOSelected", getEmployeeDTOSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditemployeeDTOSelected");
                if (getUri().contains("rememployee")) {
                     exit("../rememployee/employeeForm.xhtml");
                } else if (getUri().contains("remdrivers")) {
                     exit("../remdrivers/employeeForm.xhtml");
                } else {
                    exit("../hremployee/employeeForm.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "employeeListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setEmployeeDTOList(employeeService.getAllEmployeesListByCompanyId(getUserData().getCompany().getId()));
        } catch (Exception e) {
            saveError(e, "employeeListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public EmployeeDTO getEmployeeDTOSelected() {
        if (employeeDTOSelected == null) {

            employeeDTOSelected = new EmployeeDTO();

        }

        return employeeDTOSelected;
    }

    /**
     * @param employeeDTOSelected the employeeDTOSelected to set
     */
    public void setEmployeeDTOSelected(EmployeeDTO employeeDTOSelected) {
        this.employeeDTOSelected = employeeDTOSelected;
    }

    /**
     * @return the employeeDTO
     */
    public EmployeeDTO getEmployeeDTO() {
        if (employeeDTO == null) {
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
