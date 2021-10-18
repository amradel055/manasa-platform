/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.teachers;

import com.isag.dto.TeachersDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.TeachersService;
import java.util.ArrayList;

/**
 *
 * @author hq004
 */
@Named(value = "teachersListMB")
@ViewScoped
public class TeachersListMB extends BaseListBean {

    private TeachersDTO teachersDTO;
    private List<TeachersDTO> teachersList;

    @EJB
    TeachersService teachersService;

    @Override
    @PostConstruct
    public void init() {
        setTeachersList(teachersService.findTeachers(getUserData().getUserBranch().getId()));
    }

    @Override
    public void delete() {
        teachersService.deleteTeachers(getTeachersDTO());
        getTeachersList().remove(getTeachersDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddteachersDTO");
            exit("../teachers/teachersForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getTeachersDTO() != null && getTeachersDTO().getId() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("teachersDTOSelected", getTeachersDTO().getId());
                context.getSessionMap().put("ScreenMode", "EditteachersDTO");
                exit("../teachers/teachersForm.xhtml");
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
     * @return the teachersDTO
     */
    public TeachersDTO getTeachersDTO() {
        if (teachersDTO == null) {
            teachersDTO = new TeachersDTO();
        }
        return teachersDTO;
    }

    /**
     * @param teachersDTO the teachersDTO to set
     */
    public void setTeachersDTO(TeachersDTO teachersDTO) {
        this.teachersDTO = teachersDTO;
    }

    /**
     * @return the teachersList
     */
    public List<TeachersDTO> getTeachersList() {
        if (teachersList == null) {
            teachersList = new ArrayList<>();
        }
        return teachersList;
    }

    /**
     * @param teachersList the teachersList to set
     */
    public void setTeachersList(List<TeachersDTO> teachersList) {
        this.teachersList = teachersList;
    }

}
