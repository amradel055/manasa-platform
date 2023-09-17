/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.schoolstages;

import com.isag.businessservice.SchoolStagesGuideService;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "schoolStagesListMB")
@ViewScoped
public class SchoolStagesListMB extends BaseListBean {

    private SchoolStagesGuideDTO schoolStagesGuideDTO;
    private List<SchoolStagesGuideDTO> schoolStagesGuideDTOs;

    @EJB
    SchoolStagesGuideService schoolStagesGuideService;

    @Override
    @PostConstruct
    public void init() {
    }

    @Override
    public void delete() {
        schoolStagesGuideService.deleteStage(getSchoolStagesGuideDTO().getId());
        getSchoolStagesGuideDTOs().remove(getSchoolStagesGuideDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddStage");
            exit("../schoolstages/schoolstagesForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "StageListBean", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getSchoolStagesGuideDTO() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("stageSelected", getSchoolStagesGuideDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditStage");
                exit("../schoolstages/schoolstagesForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "StageListBean", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onCompanyListChange() {
        try {
            getSchoolStagesGuideDTOs();
        } catch (Exception e) {
            saveError(e, "UserRoleFormBean", "onCompanyListChange");
        }

    }

    /**
     * @return the schoolStagesGuideDTO
     */
    public SchoolStagesGuideDTO getSchoolStagesGuideDTO() {
        return schoolStagesGuideDTO;
    }

    /**
     * @param schoolStagesGuideDTO the schoolStagesGuideDTO to set
     */
    public void setSchoolStagesGuideDTO(SchoolStagesGuideDTO schoolStagesGuideDTO) {
        this.schoolStagesGuideDTO = schoolStagesGuideDTO;
    }

    /**
     * @return the schoolStagesGuideDTOs
     */
    public List<SchoolStagesGuideDTO> getSchoolStagesGuideDTOs() {
        if (schoolStagesGuideDTOs == null) {
            Integer companySelected = getSelectedCompanyId() != null ? getSelectedCompanyId() : getUserData().getUser().getCompanyId().getId();
            schoolStagesGuideDTOs = schoolStagesGuideService.findschoolStagesGuideByCompanyId(companySelected);
        }
        return schoolStagesGuideDTOs;
    }

    /**
     * @param schoolStagesGuideDTOs the schoolStagesGuideDTOs to set
     */
    public void setSchoolStagesGuideDTOs(List<SchoolStagesGuideDTO> schoolStagesGuideDTOs) {
        this.schoolStagesGuideDTOs = schoolStagesGuideDTOs;
    }

}
