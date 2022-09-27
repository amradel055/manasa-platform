/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class TeacherAnswersDTO extends BaseEntityDTO {

    private String text;
    private String image;
    private Integer studentQuestionId;
    private List<TeacherAnswerDetailDTO> teacherAnswerDetailDTOList ; 

    public TeacherAnswersDTO() {
    }

    public TeacherAnswersDTO(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TeacherAnswersDTO)) {
            return false;
        }
        TeacherAnswersDTO other = (TeacherAnswersDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Student[ id=" + id + " ]";
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(String image) {
        this.image = image;
    }


    /**
     * @return the studentQuestionId
     */
    public Integer getStudentQuestionId() {
        return studentQuestionId;
    }

    /**
     * @param studentQuestionId the studentQuestionId to set
     */
    public void setStudentQuestionId(Integer studentQuestionId) {
        this.studentQuestionId = studentQuestionId;
    }

    /**
     * @return the teacherAnswerDetailDTOList
     */
    public List<TeacherAnswerDetailDTO> getTeacherAnswerDetailDTOList() {
        if(teacherAnswerDetailDTOList == null){
            teacherAnswerDetailDTOList = new ArrayList<>() ;
        }
        return teacherAnswerDetailDTOList;
    }

    /**
     * @param teacherAnswerDetailDTOList the teacherAnswerDetailDTOList to set
     */
    public void setTeacherAnswerDetailDTOList(List<TeacherAnswerDetailDTO> teacherAnswerDetailDTOList) {
        this.teacherAnswerDetailDTOList = teacherAnswerDetailDTOList;
    }

}
