/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class QuestionsDTO extends BaseEntityDTO {

    private String questionName;
    private Integer questionType;
    private BigDecimal questionDegree;
    private BigDecimal totalDegree;
    private ExamsDTO examsDTO;
    private Integer examsId;
    private Integer parent;
    private StudentDTO studentDTO;
    private Integer studentId;
    private String img;
    private List<AnswersDTO> answersdtoList;

    public QuestionsDTO() {
    }

    public QuestionsDTO(Integer id) {
        this.id = id;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
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
        if (!(object instanceof QuestionsDTO)) {
            return false;
        }
        QuestionsDTO other = (QuestionsDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Questions[ id=" + id + " ]";
    }

    /**
     * @return the examsDTO
     */
    public ExamsDTO getExamsDTO() {
        return examsDTO;
    }

    /**
     * @param examsDTO the examsDTO to set
     */
    public void setExamsDTO(ExamsDTO examsDTO) {
        this.examsDTO = examsDTO;
    }

    /**
     * @return the examsId
     */
    public Integer getExamsId() {
        return examsId;
    }

    /**
     * @param examsId the examsId to set
     */
    public void setExamsId(Integer examsId) {
        this.examsId = examsId;
    }

    /**
     * @return the studentDTO
     */
    public StudentDTO getStudentDTO() {
        return studentDTO;
    }

    /**
     * @param studentDTO the studentDTO to set
     */
    public void setStudentDTO(StudentDTO studentDTO) {
        this.studentDTO = studentDTO;
    }

    /**
     * @return the studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * @return the answersdtoList
     */
    public List<AnswersDTO> getAnswersdtoList() {
        return answersdtoList;
    }

    /**
     * @param answersdtoList the answersdtoList to set
     */
    public void setAnswersdtoList(List<AnswersDTO> answersdtoList) {
        this.answersdtoList = answersdtoList;
    }

    /**
     * @return the questionDegree
     */
    public BigDecimal getQuestionDegree() {
        if (questionDegree == null) {
            questionDegree = BigDecimal.ZERO;
        }
        return questionDegree;
    }

    /**
     * @param questionDegree the questionDegree to set
     */
    public void setQuestionDegree(BigDecimal questionDegree) {
        this.questionDegree = questionDegree;
    }

    /**
     * @return the totalDegree
     */
    public BigDecimal getTotalDegree() {
        return totalDegree;
    }

    /**
     * @param totalDegree the totalDegree to set
     */
    public void setTotalDegree(BigDecimal totalDegree) {
        this.totalDegree = totalDegree;
    }

    /**
     * @return the questionType
     */
    public Integer getQuestionType() {
        return questionType;
    }

    /**
     * @param questionType the questionType to set
     */
    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

}
