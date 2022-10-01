/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author ahmed
 */
public class QuestionCommonDTO extends BaseEntityDTO {

    private String text;
    private String image;
    private Integer teacherId;
    private Integer materialId;
    
 

    private QuestionAnswerDTO questionAnswerDTO;

    public QuestionCommonDTO() {
    }

    public QuestionCommonDTO(Integer id) {
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
        if (!(object instanceof QuestionCommonDTO)) {
            return false;
        }
        QuestionCommonDTO other = (QuestionCommonDTO) object;
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
     * @return the teacherId
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return the questionAnswerDTO
     */
    public QuestionAnswerDTO getQuestionAnswerDTO() {
        if (questionAnswerDTO == null) {
            setQuestionAnswerDTO(new QuestionAnswerDTO());
        }
        return questionAnswerDTO;
    }

    /**
     * @param questionAnswerDTO the questionAnswerDTO to set
     */
    public void setQuestionAnswerDTO(QuestionAnswerDTO questionAnswerDTO) {
        this.questionAnswerDTO = questionAnswerDTO;
    }

    /**
     * @return the materialId
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

}
