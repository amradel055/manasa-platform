/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.reportmanagement;

import com.isag.businessservice.ReportManagementService;
import com.isag.entity.ReportManagement;
import com.isag.isag.BaseFormBean;
import com.isag.isag.UserData;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author amr
 */
@ViewScoped
@Named(value = "reportManagementBean")
public class ReportManagementBean extends BaseFormBean {

    private ReportManagement reportManagement;

    @EJB
    private ReportManagementService managementService;

    @Override
    public String save() {
        try {
            reportManagement.setBranchId(getUserData().getUserBranch());
            reportManagement.setCompanyId(getUserData().getCompany());

            if (reportManagement.getId() == null) {
                reportManagement.setCreationDate(new Date());
                reportManagement.setCreatedBy(getUserData().getUser());
            } else {
                reportManagement.setModificationDate(new Date());
                reportManagement.setModifiedBy(getUserData().getUser());
            }

            managementService.savereportManagement(reportManagement);

            return "";
        } catch (Exception e) {
            saveError(e, "ReportManagementBean", "save");
            return null;
        }
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            load();
        } catch (Exception e) {
            saveError(e, "ReportManagementBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            reportManagement = managementService.reportManagementByBranch(getUserData().getUserBranch().getId());
            if (reportManagement == null) {
                reportManagement = new ReportManagement();
            }
        } catch (Exception e) {
            saveError(e, "ReportManagementBean", "load");
        }
    }

    @Override
    public String getScreenName() {
        return "reportmanagement";
    }

    /**
     * @return the reportManagement
     */
    public ReportManagement getReportManagement() {

        if (reportManagement == null) {
            reportManagement = new ReportManagement();
        }
        return reportManagement;

    }

    /**
     * @param reportManagement the reportManagement to set
     */
    public void setReportManagement(ReportManagement reportManagement) {
        this.reportManagement = reportManagement;
    }

}
