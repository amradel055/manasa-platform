/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.HRDepartment;

import com.isag.businessservice.DepartmentService;
import com.isag.dto.DepartmentDTO;
import com.isag.isag.BaseListBean;
import com.isag.isag.UserData;
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
@Named(value = "departmentListMB")
@ViewScoped
public class departmentListMB extends BaseListBean {

    @EJB
    DepartmentService departmentService;
    private List<DepartmentDTO> deapartmentDTOList;
    private DepartmentDTO departmentDTOSelected;
    private DepartmentDTO departmentDTO;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.load();
        } catch (Exception e) {
            saveError(e, "departmentListMB", "init");
        }
    }

    @Override
    public void delete() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();
            if (departmentDTOSelected != null) {
                try {
                    departmentService.deleteDepartment(departmentDTOSelected, getUserData().getUser());
                    deapartmentDTOList.remove(departmentDTOSelected);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("DELETED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "departmentListMB", "delete");
        }
    }

    @Override
    public void filter() {
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AdddepartmentDTOSelected");

            exit("../hrdeparment/DepartmentForm.xhtml");

            return "";
        } catch (Exception e) {
            saveError(e, "departmentListMB", "goToAdd");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (departmentDTOSelected != null && departmentDTOSelected.getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("rscTenderDTOSelected", departmentDTOSelected.getId());
                context.getSessionMap().put("ScreenMode", "EditdepartmentDTOSelected");
                exit("../hrdeparment/DepartmentForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "departmentListMB", "goToEdit");
            return null;
        }
    }

    @Override
    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            deapartmentDTOList = departmentService.getAllDepartmentsListByBranchId(getUserData().getUserBranch().getId());
        } catch (Exception e) {
            saveError(e, "departmentListMB", "load");
        }
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the deapartmentDTOList
     */
    public List<DepartmentDTO> getDeapartmentDTOList() {
        if (deapartmentDTOList == null || deapartmentDTOList.isEmpty()) {
            deapartmentDTOList = new ArrayList<>();

        }

        return deapartmentDTOList;
    }

    /**
     * @param deapartmentDTOList the deapartmentDTOList to set
     */
    public void setDeapartmentDTOList(List<DepartmentDTO> deapartmentDTOList) {
        this.deapartmentDTOList = deapartmentDTOList;
    }

    /**
     * @return the departmentDTOSelected
     */
    public DepartmentDTO getDepartmentDTOSelected() {
        if (departmentDTOSelected == null) {
            departmentDTOSelected = new DepartmentDTO();
        }
        return departmentDTOSelected;
    }

    /**
     * @param departmentDTOSelected the departmentDTOSelected to set
     */
    public void setDepartmentDTOSelected(DepartmentDTO departmentDTOSelected) {
        this.departmentDTOSelected = departmentDTOSelected;
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
