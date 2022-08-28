/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.HRDepartment;

import com.toby.businessservice.DepartmentService;
import com.toby.dto.DepartmentDTO;
import com.toby.toby.HRBasicDataForm;
import com.toby.toby.UserData;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ahmed Shaaban
 */
@Named(value = "departmentFormMB")
@ViewScoped
public class departmentFormMB extends HRBasicDataForm {

    private DepartmentDTO departmentDTO;
    @EJB
    DepartmentService departmentService;

    @Override
    public String save() {
        try {
            departmentDTO = departmentService.addDepartment(departmentDTO, getUserData().getUser());
            if (departmentDTO.getMsg() != null && !departmentDTO.getMsg().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), departmentDTO.getMsg()));
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
            load();
        } catch (Exception e) {
            saveError(e, "departmentFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            setScreenMode((String) context.getSessionMap().get("ScreenMode"));
            getUserData().getUser().setBranchId(getUserData().getUserBranch());

            if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AdddepartmentDTOSelected")) {
                reset();
            } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditdepartmentDTOSelected")) {
                Integer departmentId = (Integer) context.getSessionMap().get("rscTenderDTOSelected");
                departmentDTO = departmentService.findDepartment(departmentId);

            }
        } catch (Exception e) {
            saveError(e, "departmentFormMB", "load");
        }
    }

    public String cancel() {
        try {
            exit("../hrdeparment/DepartmentList.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "departmentFormMB", "cancel");
            return null;
        }
    }

    public void onDepartmentListChange() {
        try {
            RequestContext.getCurrentInstance().update("ScreenAddEditForm:com");
        } catch (Exception e) {
            saveError(e, "departmentFormMB", "onDepartmentListChange");
        }
    }

    public void reset() {

        departmentDTO = new DepartmentDTO();
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the departmentDTO
     */
    public DepartmentDTO getDepartmentDTO() {
        if (departmentDTO == null) {
            departmentDTO = new DepartmentDTO();
        }
        return departmentDTO;
    }

    /**
     * @param departmentDTO the departmentDTO to set
     */
    public void setDepartmentDTO(DepartmentDTO departmentDTO) {
        this.departmentDTO = departmentDTO;
    }

}
