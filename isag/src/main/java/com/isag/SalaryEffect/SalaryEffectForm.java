/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.SalaryEffect;

import com.isag.dto.HrSalaryEffectDTO;
import com.isag.isag.HRBasicDataForm;
import com.isag.isag.UserData;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.HrSalaryEffectService;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Named(value = "salaryEffectFormMB")
@ViewScoped
public class SalaryEffectForm extends HRBasicDataForm {

    private HrSalaryEffectDTO salaryEffectTypeDTO;

    @EJB
    HrSalaryEffectService salaryEffectTypeService;

    @Override
    public String save() {
        try {
            setSalaryEffectTypeDTO(salaryEffectTypeService.addSalaryEffectType(getSalaryEffectTypeDTO(), getUserData().getUser()));
            if (getSalaryEffectTypeDTO().getMsg() != null && !salaryEffectTypeDTO.getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getSalaryEffectTypeDTO().getMsg()));
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
        }
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            // load the basic user data and then fill the lists and autocompletes once page loads.
            load();
            getSalaryEffectTypeList(salaryEffectTypeDTO != null && salaryEffectTypeDTO.getImpactIndicator() != null ? salaryEffectTypeDTO.getImpactIndicator() : 0);

        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeForm", "init");
        }
    }

    @Override
    public void load() {

        try {

            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddSalaryEffect")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EdithrSalaryEffectDTO")) {
                Integer id = (Integer) context.getSessionMap().get("hrSalaryEffectDTOSelected");
                setSalaryEffectTypeDTO(salaryEffectTypeService.findSalaryEffect(id));
            }
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeForm", "load");
        }
    }

    public void reset() {
        salaryEffectTypeDTO = new HrSalaryEffectDTO();

    }

    public String exit() {
        try {
            exit("../base/home.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeForm", "exit");
            return null;
        }
    }

    public void findSalaryEffectTypeList() {

        getSalaryEffectTypeList(salaryEffectTypeDTO.getImpactIndicator());

    }

    /**
     * @return the salaryEffectTypeDTO
     */
    public HrSalaryEffectDTO getSalaryEffectTypeDTO() {
        if (salaryEffectTypeDTO == null) {
            salaryEffectTypeDTO = new HrSalaryEffectDTO();
        }
        return salaryEffectTypeDTO;
    }

    /**
     * @param salaryEffectTypeDTO the salaryEffectTypeDTO to set
     */
    public void setSalaryEffectTypeDTO(HrSalaryEffectDTO salaryEffectTypeDTO) {
        this.salaryEffectTypeDTO = salaryEffectTypeDTO;
    }

}
