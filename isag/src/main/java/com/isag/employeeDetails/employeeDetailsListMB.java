/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.employeeDetails;

import com.isag.businessservice.HrEmployeeDetailsViewService;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import com.isag.views.EmployeeDetailsSelectView;
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
@Named(value = "employeeDetailsListMB")
@ViewScoped
public class employeeDetailsListMB extends BaseListBean {

    @EJB
    HrEmployeeDetailsViewService hrEmployeeDetailsViewService;
    private List<EmployeeDetailsSelectView> employeeDetailsSelectViewList;
    private EmployeeDetailsSelectView detailsViewSelected;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "employeeDetailsListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (getDetailsViewSelected() != null) {
                try {
                    getEmployeeDetailsSelectViewList().remove(getDetailsViewSelected());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "employeeDetailsListMB", "delete");
        }
    }

    @Override
    public void filter() {
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AdddetailsViewSelected");

            exit("../hremployeesalaryvoucher/salaryVoucherForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "employeeDetailsListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getDetailsViewSelected() != null && getDetailsViewSelected().getEmployeeId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("detailsViewSelected", getDetailsViewSelected().getId());
                context.getSessionMap().put("ScreenMode", "EditdetailsViewSelected");
                exit("../hremployeesalaryvoucher/salaryVoucherForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "employeeDetailsListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setEmployeeDetailsSelectViewList(hrEmployeeDetailsViewService.findHrEmployeeDetailsView(getUserData().getUserBranch().getId()));
        } catch (Exception e) {
            saveError(e, "employeeDetailsListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the employeeDetailsSelectViewList
     */
    public List<EmployeeDetailsSelectView> getEmployeeDetailsSelectViewList() {
        if (employeeDetailsSelectViewList == null || employeeDetailsSelectViewList.isEmpty()) {

            employeeDetailsSelectViewList = new ArrayList<>();
        }
        return employeeDetailsSelectViewList;
    }

    /**
     * @param employeeDetailsSelectViewList the employeeDetailsSelectViewList to
     * set
     */
    public void setEmployeeDetailsSelectViewList(List<EmployeeDetailsSelectView> employeeDetailsSelectViewList) {
        this.employeeDetailsSelectViewList = employeeDetailsSelectViewList;
    }

    /**
     * @return the detailsViewSelected
     */
    public EmployeeDetailsSelectView getDetailsViewSelected() {
        return detailsViewSelected;
    }

    /**
     * @param detailsViewSelected the detailsViewSelected to set
     */
    public void setDetailsViewSelected(EmployeeDetailsSelectView detailsViewSelected) {
        this.detailsViewSelected = detailsViewSelected;
    }

}
