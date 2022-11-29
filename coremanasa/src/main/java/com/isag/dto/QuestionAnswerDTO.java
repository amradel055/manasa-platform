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
public class QuestionAnswerDTO extends BaseEntityDTO {

    private String text;
    private String image;
    private Integer questionCommonId;
    private List<QuestionAnswerDetailDTO> answerDetailDTOList ; 

    public QuestionAnswerDTO() {
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuestionAnswerDTO)) {
            return false;
        }
        QuestionAnswerDTO other = (QuestionAnswerDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
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
     * @return the questionCommonId
     */
    public Integer getQuestionCommonId() {
        return questionCommonId;
    }

    /**
     * @param questionCommonId the questionCommonId to set
     */
    public void setQuestionCommonId(Integer questionCommonId) {
        this.questionCommonId = questionCommonId;
    }

    /**
     * @return the answerDetailDTOList
     */
    public List<QuestionAnswerDetailDTO> getAnswerDetailDTOList() {
        if(answerDetailDTOList == null){
            answerDetailDTOList = new ArrayList<>() ;
        }
        return answerDetailDTOList;
    }

    /**
     * @param answerDetailDTOList the answerDetailDTOList to set
     */
    public void setAnswerDetailDTOList(List<QuestionAnswerDetailDTO> answerDetailDTOList) {
        this.answerDetailDTOList = answerDetailDTOList;
    }

}
