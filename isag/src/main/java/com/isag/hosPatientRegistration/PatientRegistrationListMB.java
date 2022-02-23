/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.hosPatientRegistration;

import com.isag.organizationSite.*;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.PatientRegistrationService;
import com.isag.entity.HosPatientRegistration;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
import java.io.Serializable;
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
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq002
 */
@Named(value = "patientRegistrationListMB")
@ViewScoped
public class PatientRegistrationListMB extends BaseListBean implements Serializable {

    private List<HosPatientRegistration> hosPatientRegistrations = new ArrayList<>();
    private HosPatientRegistration hosPatientRegistration;
    private UserData userData;
    private Integer hosPatientRegistrationSelected;

    private HttpServletRequest request;
    private String url;
    @EJB
    private PatientRegistrationService patientRegistrationService;

    @Override
    @PostConstruct
    public void init() {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        load();
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));

            if (url.contains("company")) {
                hosPatientRegistrations = patientRegistrationService.getCompanyByBranchId(userData.getUserBranch().getId()); // its type = 0 
            } else {
                hosPatientRegistrations = patientRegistrationService.getAllHosPatientRegistrationByBranchId(userData.getUserBranch().getId(), 0);  // its type = 1 
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationListMB", "load");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (hosPatientRegistration != null) {
                try {
                    patientRegistrationService.deletePatientRegistration(hosPatientRegistration);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                    load();
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationListMB", "delete");
        }
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "Add");

            if (url.contains("PatientRegistration")) {
                exit("../PatientRegistration/PatientRegistrationForm.xhtml");
            }
            return "";
        } catch (Exception e) {
            saveError(e, "PatientRegistrationListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (hosPatientRegistrationSelected != null && hosPatientRegistrationSelected > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("hosPatientRegistrationSelected", hosPatientRegistrationSelected);
                context.getSessionMap().put("ScreenMode", "Edit");
                if (url.contains("PatientRegistration")) {
                    exit("../PatientRegistration/PatientRegistrationForm.xhtml");
                }
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "PatientRegistrationListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the hosPatientRegistrations
     */
    public List<HosPatientRegistration> getHosPatientRegistrations() {
        return hosPatientRegistrations;
    }

    /**
     * @param hosPatientRegistrations the hosPatientRegistrations to set
     */
    public void setHosPatientRegistrations(List<HosPatientRegistration> hosPatientRegistrations) {
        this.hosPatientRegistrations = hosPatientRegistrations;
    }

    /**
     * @return the hosPatientRegistration
     */
    public HosPatientRegistration getHosPatientRegistration() {
        return hosPatientRegistration;
    }

    /**
     * @param hosPatientRegistration the hosPatientRegistration to set
     */
    public void setHosPatientRegistration(HosPatientRegistration hosPatientRegistration) {
        this.hosPatientRegistration = hosPatientRegistration;
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
     * @return the hosPatientRegistrationSelected
     */
    public Integer getHosPatientRegistrationSelected() {
        return hosPatientRegistrationSelected;
    }

    /**
     * @param hosPatientRegistrationSelected the hosPatientRegistrationSelected
     * to set
     */
    public void setHosPatientRegistrationSelected(Integer hosPatientRegistrationSelected) {
        this.hosPatientRegistrationSelected = hosPatientRegistrationSelected;
    }

    /**
     * @return the patientRegistrationService
     */
    public PatientRegistrationService getPatientRegistrationService() {
        return patientRegistrationService;
    }

    /**
     * @param patientRegistrationService the patientRegistrationService to set
     */
    public void setPatientRegistrationService(PatientRegistrationService patientRegistrationService) {
        this.patientRegistrationService = patientRegistrationService;
    }
}
