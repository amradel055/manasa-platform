/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.teacherQuestion;

import com.isag.businessservice.MaterialsService;
import com.isag.businessservice.QuestionCommonService;
import com.isag.dto.MaterialsDTO;
import com.isag.isag.BaseListBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import com.isag.dto.QuestionCommonDTO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author hq004
 */
@Named(value = "teacherQuestionListMB")
@ViewScoped
public class TeacherQuestionListMB extends BaseListBean {

    private QuestionCommonDTO questionCommonDTO;
    private List<QuestionCommonDTO> questionCommonDTOList;
    private Integer questionCommonDTOSelected;
    private Integer materialSelected;
    private String host;

    private List<MaterialsDTO> materialsDTOList;
//    private MaterialsDTO materialsDTO;
//    private MaterialDTOConverter materialDTOConverter;

    @EJB
    QuestionCommonService questionCommonService;
    @EJB
    MaterialsService materialsService;

    @Override
    @PostConstruct
    public void init() {
        setMaterialsDTOList(materialsService.findMaterialsByTeacher(getUserData().getUser().getId()));
//        setMaterialDTOConverter(new MaterialDTOConverter(materialsDTOList));
    }

    public void search() {
        if (getMaterialSelected() != null) {
            setQuestionCommonDTOList(questionCommonService.findQuestionCommonAndAnswer(getUserData().getUser().getId(), getMaterialSelected()));
        }

    }

    @Override
    public void delete() {
        questionCommonService.delete(getQuestionCommonDTO().getId(), getUserData().getUser());
        getQuestionCommonDTOList().remove(questionCommonDTO);
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("ScreenMode", "AddTeachersQuestions");
            exit("../teacherQuestions/teacherQuestionsForm.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "notesReceivablesListMB", "load");
            return null;
        }
    }

    @Override
    public String goToEdit() {
        try {
            if (getQuestionCommonDTOSelected() != null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("teachersDTOSelected", getQuestionCommonDTOSelected());
                context.getSessionMap().put("ScreenMode", "EditTeacherQuestions");
                exit("../teacherQuestions/teacherQuestionsForm.xhtml");
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
     * @return the questionCommonDTO
     */
    public QuestionCommonDTO getQuestionCommonDTO() {
        if (questionCommonDTO == null) {
            setQuestionCommonDTO(new QuestionCommonDTO());
        }
        return questionCommonDTO;
    }

    /**
     * @param questionCommonDTO the questionCommonDTO to set
     */
    public void setQuestionCommonDTO(QuestionCommonDTO questionCommonDTO) {
        this.questionCommonDTO = questionCommonDTO;
    }

    /**
     * @return the questionCommonDTOList
     */
    public List<QuestionCommonDTO> getQuestionCommonDTOList() {
        if (questionCommonDTOList == null) {
            setQuestionCommonDTOList(new ArrayList<>());
        }
        return questionCommonDTOList;
    }

    /**
     * @param questionCommonDTOList the questionCommonDTOList to set
     */
    public void setQuestionCommonDTOList(List<QuestionCommonDTO> questionCommonDTOList) {
        this.questionCommonDTOList = questionCommonDTOList;
    }

    /**
     * @return the questionCommonDTOSelected
     */
    public Integer getQuestionCommonDTOSelected() {
        return questionCommonDTOSelected;
    }

    /**
     * @param questionCommonDTOSelected the questionCommonDTOSelected to set
     */
    public void setQuestionCommonDTOSelected(Integer questionCommonDTOSelected) {
        this.questionCommonDTOSelected = questionCommonDTOSelected;
    }

    /**
     * @return the materialsDTOList
     */
    public List<MaterialsDTO> getMaterialsDTOList() {
        if (materialsDTOList == null) {
            setMaterialsDTOList(new ArrayList<>());
        }
        return materialsDTOList;
    }

    /**
     * @param materialsDTOList the materialsDTOList to set
     */
    public void setMaterialsDTOList(List<MaterialsDTO> materialsDTOList) {
        this.materialsDTOList = materialsDTOList;
    }

//    /**
//     * @return the materialsDTO
//     */
//    public MaterialsDTO getMaterialsDTO() {
//        if (materialsDTO == null) {
//            setMaterialsDTO(new MaterialsDTO());
//        }
//        return materialsDTO;
//    }
//
//    /**
//     * @param materialsDTO the materialsDTO to set
//     */
//    public void setMaterialsDTO(MaterialsDTO materialsDTO) {
//        this.materialsDTO = materialsDTO;
//    }
//
//    /**
//     * @return the materialDTOConverter
//     */
//    public MaterialDTOConverter getMaterialDTOConverter() {
//        return materialDTOConverter;
//    }
//
//    /**
//     * @param materialDTOConverter the materialDTOConverter to set
//     */
//    public void setMaterialDTOConverter(MaterialDTOConverter materialDTOConverter) {
//        this.materialDTOConverter = materialDTOConverter;
//    }
    /**
     * @return the materialSelected
     */
    public Integer getMaterialSelected() {
        return materialSelected;
    }

    /**
     * @param materialSelected the materialSelected to set
     */
    public void setMaterialSelected(Integer materialSelected) {
        this.materialSelected = materialSelected;
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
