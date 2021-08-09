/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.remEmployeeShift;

import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.RemEmployeeShiftService;
import com.isag.dto.RemEmployeeShiftDTO;

/**
 *
 * @author hq004
 */
@Named(value = "employeeShiftListMB")
@ViewScoped
public class EmployeeShiftListMB extends BaseListBean {

    private RemEmployeeShiftDTO remEmployeeShiftDTO;
    private List<RemEmployeeShiftDTO> remEmployeeShiftDTOList;

    @EJB
    RemEmployeeShiftService remEmployeeShiftService;
    

    @Override
    @PostConstruct
    public void init() {
        setRemEmployeeShiftDTOList(remEmployeeShiftService.findEmployeeShifts(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        setRemEmployeeShiftDTO(remEmployeeShiftService.findEmployeeShift(getRemEmployeeShiftDTO().getId()));
        remEmployeeShiftService.deleteEmployeeShift(getRemEmployeeShiftDTO());
        remEmployeeShiftDTOList.remove(getRemEmployeeShiftDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddemployeeShiftDTO");
            exit("../rememployeeShift/employeeShiftForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getRemEmployeeShiftDTO() != null && getRemEmployeeShiftDTO().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("employeeShiftDTOlected", getRemEmployeeShiftDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditemployeeShiftDTO");
                exit("../rememployeeShift/employeeShiftForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "BranchListBean", "goToEdit");
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

    /**
     * @return the remEmployeeShiftDTO
     */
    public RemEmployeeShiftDTO getRemEmployeeShiftDTO() {
        return remEmployeeShiftDTO;
    }

    /**
     * @param remEmployeeShiftDTO the remEmployeeShiftDTO to set
     */
    public void setRemEmployeeShiftDTO(RemEmployeeShiftDTO remEmployeeShiftDTO) {
        this.remEmployeeShiftDTO = remEmployeeShiftDTO;
    }

    /**
     * @return the remEmployeeShiftDTOList
     */
    public List<RemEmployeeShiftDTO> getRemEmployeeShiftDTOList() {
        return remEmployeeShiftDTOList;
    }

    /**
     * @param remEmployeeShiftDTOList the remEmployeeShiftDTOList to set
     */
    public void setRemEmployeeShiftDTOList(List<RemEmployeeShiftDTO> remEmployeeShiftDTOList) {
        this.remEmployeeShiftDTOList = remEmployeeShiftDTOList;
    }

}
