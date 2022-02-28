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
public class AnswersDTO extends BaseEntityDTO {

    private String answer;
    private Integer correct;
    private String reason;
    private QuestionsDTO questionsDTO;
    private Integer questionId;
    private Integer parent;
    private StudentDTO studentDTO;
    private Integer studentId;
    private ExamsDTO examsDTO;
    private Integer examId;
    private Integer selection;
    private String img;

    public AnswersDTO() {
    }

    public AnswersDTO(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        if (!(object instanceof AnswersDTO)) {
            return false;
        }
        AnswersDTO other = (AnswersDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Answers[ id=" + id + " ]";
    }

    /**
     * @return the questionsDTO
     */
    public QuestionsDTO getQuestionsDTO() {
        return questionsDTO;
    }

    /**
     * @param questionsDTO the questionsDTO to set
     */
    public void setQuestionsDTO(QuestionsDTO questionsDTO) {
        this.questionsDTO = questionsDTO;
    }

    /**
     * @return the questionId
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * @param questionId the questionId to set
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
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
     * @return the examId
     */
    public Integer getExamId() {
        return examId;
    }

    /**
     * @param examId the examId to set
     */
    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    /**
     * @return the selection
     */
    public Integer getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    public void setSelection(Integer selection) {
        this.selection = selection;
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
