/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.SalaryEffectType;

import com.isag.businessservice.HrSalaryEffectTypeService;
import com.isag.dto.HrSalaryEffectTypeDTO;
import com.isag.isag.HRBasicDataForm;
import com.isag.isag.UserData;
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
 * @author AhmedEssam
 */
@Named(value = "hrSalaryEffectTypeFormMB")
@ViewScoped
public class SalaryEffectTypeFormMB extends HRBasicDataForm {

    private HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO;
    @EJB
    HrSalaryEffectTypeService hrSalaryEffectTypeService;

    @Override
    @PostConstruct
    public void init() {
        try {
            load();
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());
            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Add")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("Edit")) {
                Integer hrSalaryEffectTypeId = (Integer) context.getSessionMap().get("hrSalaryEffectTypeDTOSelected");
                setHrSalaryEffectTypeDTO(hrSalaryEffectTypeService.findSalaryEffectTypeDto(hrSalaryEffectTypeId));
            }
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeFormMB", "load");
        }
    }

    @Override
    public String save() {
        try {
            if (validateCalculation()) {
                setHrSalaryEffectTypeDTO(hrSalaryEffectTypeService.addSalaryEffectType(getHrSalaryEffectTypeDTO(), getUserData().getUser()));
                if (getHrSalaryEffectTypeDTO().getMsg() != null && !getHrSalaryEffectTypeDTO().getMsg().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getHrSalaryEffectTypeDTO().getMsg()));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
                    exit("../hrsalaryeffecttype/hrSalaryEffectTypeList.xhtml");
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "حدث خطأ!"));
            e.printStackTrace();
        }
        return "";
    }

    public boolean validateCalculation() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        List<HrSalaryEffectTypeDTO> HrSalaryEffectTypeDTOList = hrSalaryEffectTypeService.findExistSalaryEffectType(getUserData().getUser().getBranchId().getId(), getHrSalaryEffectTypeDTO());
        if (getHrSalaryEffectTypeDTO().getName().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "لا يمكن ترك اسم المؤثر فارغ"));
            return false;
        } else if (HrSalaryEffectTypeDTOList != null && !HrSalaryEffectTypeDTOList.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "هذه البيانات موجوده من قبل"));
            return false;
        } else {
            return true;
        }

    }

    public void reset() {
        try {
            hrSalaryEffectTypeDTO = new HrSalaryEffectTypeDTO();
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeFormMB", "reset");
        }
    }

    public String exit() {
        try {
            exit("../base/home.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "SalaryEffectTypeFormMB", "exit");
            return null;
        }
    }

    /**
     * @return the hrSalaryEffectTypeDTO
     */
    public HrSalaryEffectTypeDTO getHrSalaryEffectTypeDTO() {
        if (hrSalaryEffectTypeDTO == null) {
            hrSalaryEffectTypeDTO = new HrSalaryEffectTypeDTO();
        }
        return hrSalaryEffectTypeDTO;
    }

    /**
     * @param hrSalaryEffectTypeDTO the hrSalaryEffectTypeDTO to set
     */
    public void setHrSalaryEffectTypeDTO(HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO) {
        this.hrSalaryEffectTypeDTO = hrSalaryEffectTypeDTO;
    }

}
