/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.managedBean.lesson;

import com.isag.businessservice.LessonService;
import com.isag.dto.LessonDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq004
 */
@Named(value = "lessonListMB")
@ViewScoped
public class LessonListMB extends BaseListBean {

    private LessonDTO lessonDTO;
    private List<LessonDTO> LessonDTOList;
    private Integer lessonDTOSelected;

    private String host;

    @EJB
    LessonService lessonService;

    @Override
    @PostConstruct
    public void init() {
    }

    @Override
    public void delete() {
        lessonService.deleteLesson(getLessonDTO().getId());
        getLessonDTOList().remove(getLessonDTO());
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddLesson");
            exit("../lesson/lessonForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "PeriodListBean", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getLessonDTOSelected() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("lessonSelected", getLessonDTOSelected());
                context.getSessionMap().put("ScreenMode", "EditLesson");
                exit("../lesson/lessonForm.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "PeriodListBean", "goToEdit");
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
     * @return the lessonDTO
     */
    public LessonDTO getLessonDTO() {
        if (lessonDTO == null) {
            setLessonDTO(new LessonDTO());
        }
        return lessonDTO;
    }

    /**
     * @param lessonDTO the lessonDTO to set
     */
    public void setLessonDTO(LessonDTO lessonDTO) {
        this.lessonDTO = lessonDTO;
    }

    /**
     * @return the LessonDTOList
     */
    public List<LessonDTO> getLessonDTOList() {
        if (LessonDTOList == null) {
            setLessonDTOList(lessonService.findLessonByTeacherId(getUserData().getUser().getId()));
        }
        return LessonDTOList;
    }

    /**
     * @param LessonDTOList the LessonDTOList to set
     */
    public void setLessonDTOList(List<LessonDTO> LessonDTOList) {
        this.LessonDTOList = LessonDTOList;
    }

    /**
     * @return the lessonDTOSelected
     */
    public Integer getLessonDTOSelected() {
        return lessonDTOSelected;
    }

    /**
     * @param lessonDTOSelected the lessonDTOSelected to set
     */
    public void setLessonDTOSelected(Integer lessonDTOSelected) {
        this.lessonDTOSelected = lessonDTOSelected;
    }

    /**
     * @return the host
     */
    public String getHost() {
        HttpServletRequest request = (HttpServletRequest) getContext().getRequest();
        setHost(request.getRequestURL().substring(0, request.getRequestURL().indexOf("faces/")).concat("images/"));
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

}
