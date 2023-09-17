/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.schoolstages;

import com.isag.businessservice.CompanyService;
import com.isag.entity.IgmaCompany;
import com.isag.isag.BaseFormBean;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import com.isag.businessservice.SchoolStagesGuideService;
import com.isag.entity.SchoolStagesGuide;
import javax.faces.application.FacesMessage;

/**
 *
 * @author hq002
 */
@Named(value = "schoolStagesFormBean")
@ViewScoped
public class SchoolStagesFormBean extends BaseFormBean implements Serializable {

    private String name;
    private SchoolStagesGuide schoolStagesGuide;
    private IgmaCompany company;
    
    private Integer stageSelected;

    /**
     * Creates a new instance of NewJSFManagedBean
     */
    @EJB
    private SchoolStagesGuideService schoolStagesGuideService;

    @EJB
    private CompanyService companyService;

    public SchoolStagesFormBean() {

    }

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "init");
        }
    }
    
    @Override
    public void load() {
        try {
            if (getScreenMode() != null && (getScreenMode().length()) > 0) {

                if (getUserData().getCompany() != null) {
                        setCompanySelected(getUserData().getCompany());
                    } 
                
                 if (getScreenMode().equalsIgnoreCase("EditStage")) {
                    
                    try {
                        stageSelected = (Integer) (getContext().getSessionMap().get("stageSelected"));
                        setSchoolStagesGuide(schoolStagesGuideService.findschoolStagesGuideById(stageSelected));
                        setCompanySelected(getSchoolStagesGuide().getCompanyId());
                        setName(getSchoolStagesGuide().getName());
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "load");
        }
    }

    @Override
    public String save() {
        try {

            if (getName() != null && !getName().isEmpty()) {
                schoolStagesGuide.setName(getName());

                IgmaCompany companySelected = getCompanySelected() != null ? getCompanySelected() : getUserData().getUser().getCompanyId();
                schoolStagesGuide.setCompanyId(companySelected);
                schoolStagesGuide.setModifiedBy(getUserData().getUser());
                schoolStagesGuide.setModificationDate(new Date());
                if (getScreenMode().equalsIgnoreCase("add")) {
                    try {
                        schoolStagesGuide = schoolStagesGuideService.addSchoolStagesGuide(schoolStagesGuide);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("DUBLICATED")));
                        return null;
                    }

                }
                if (getScreenMode().equalsIgnoreCase("edit")) {
                    schoolStagesGuide.setId(stageSelected);
                    try {
                        schoolStagesGuide = schoolStagesGuideService.addSchoolStagesGuide(schoolStagesGuide);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), getUserData().getUserDDs().get("SAVED")));
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getUserData().getUserDDs().get("DUBLICATED")));
                        return null;
                    }

                }
                exit("../role/roleList.xhtml");
                return "";
            }
            return "";
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "save");
            return null;
        }
    }


    public String cancel() {
        try {
            exit("../schoolstages/schoolstagesList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "RoleFormBean", "cancel");
            return null;
        }
    }

    @Override
    public String getScreenName() {
        return "stages";

    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public IgmaCompany getCompany() {
        return company;
    }

    public void setCompany(IgmaCompany company) {
        this.company = company;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the schoolStagesGuide
     */
    public SchoolStagesGuide getSchoolStagesGuide() {
        if(schoolStagesGuide == null){
            schoolStagesGuide = new SchoolStagesGuide();
        }
        return schoolStagesGuide;
    }

    /**
     * @param schoolStagesGuide the schoolStagesGuide to set
     */
    public void setSchoolStagesGuide(SchoolStagesGuide schoolStagesGuide) {
        this.schoolStagesGuide = schoolStagesGuide;
    }

    /**
     * @return the stageSelected
     */
    public Integer getStageSelected() {
        return stageSelected;
    }

    /**
     * @param stageSelected the stageSelected to set
     */
    public void setStageSelected(Integer stageSelected) {
        this.stageSelected = stageSelected;
    }
}
