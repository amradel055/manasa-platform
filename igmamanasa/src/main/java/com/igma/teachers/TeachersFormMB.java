/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.teachers;

import com.isag.dto.TeachersDTO;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.businessservice.TeachersService;
import com.isag.isag.BaseFormBean;

/**
 *
 * @author hq004
 */
@Named(value = "teachersFormMB")
@ViewScoped
public class TeachersFormMB extends BaseFormBean {

    private TeachersDTO teachersDTO;

    @EJB
    TeachersService teachersService;

    @Override
    public String save() {
        setTeachersDTO(teachersService.save(getTeachersDTO(), getUserData().getUser()));
        return "";
    }

    @Override
    @PostConstruct
    public void init() {
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddteachersDTO")) {

        } else if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("EditteachersDTO")) {
            try {
                Integer teacherId = (Integer) getContext().getSessionMap().get("teachersDTOSelected");
                setTeachersDTO(teachersService.findTeacher(teacherId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        setTeachersDTO(null);
    }

    public String exit() {
        exit("../teachers/teachersList.xhtml");
        return "";
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

}
