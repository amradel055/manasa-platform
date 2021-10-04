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
public class ExamsDTO extends BaseEntityDTO {

    /**
     * @return the lessonId
     */
    private LessonDTO lessonDTO;
    private Integer lessonId;
    private String lessonName;
    private String schoolStagesName;
    private SchoolStagesGuideDTO schoolStagesGuideDTO;
    private Integer schoolStagesId;
    private String examName;
    private BigDecimal degree;
    private List<QuestionsDTO> questionsList;
    private Integer parent;
    private StudentDTO studentDTO;
    private Integer studentId;
    private Integer teacherId;
    private Integer periodId;
    private BigDecimal score;
    private Integer active;
    private Integer monthId;
    private Integer examType;
    private Integer timer;
    private Integer materialId;
    private List<Integer> materialList;
    private List<Integer> periodList;

    public ExamsDTO() {
    }

    public ExamsDTO(Integer id) {
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
        if (!(object instanceof ExamsDTO)) {
            return false;
        }
        ExamsDTO other = (ExamsDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Exams[ id=" + id + " ]";
    }

    public String getExamName() {
        return examName;
    }

    /**
     * @param examName the examName to set
     */
    public void setExamName(String examName) {
        this.examName = examName;
    }

    /**
     * @return the degree
     */
    public BigDecimal getDegree() {
        if(degree == null){
            degree = BigDecimal.ZERO;
        }
        return degree;
    }

    /**
     * @param degree the degree to set
     */
    public void setDegree(BigDecimal degree) {
        this.degree = degree;
    }

    /**
     * @return the lessonDTO
     */
    public LessonDTO getLessonDTO() {
        return lessonDTO;
    }

    /**
     * @param lessonDTO the lessonDTO to set
     */
    public void setLessonDTO(LessonDTO lessonDTO) {
        this.lessonDTO = lessonDTO;
    }

    /**
     * @return the schoolStagesGuideDTO
     */
    public SchoolStagesGuideDTO getSchoolStagesGuideDTO() {
        return schoolStagesGuideDTO;
    }

    /**
     * @param schoolStagesGuideDTO the schoolStagesGuideDTO to set
     */
    public void setSchoolStagesGuideDTO(SchoolStagesGuideDTO schoolStagesGuideDTO) {
        this.schoolStagesGuideDTO = schoolStagesGuideDTO;
    }

    /**
     * @param lessonId the lessonId to set
     */
    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    /**
     * @param schoolStagesId the schoolStagesId to set
     */
    public void setSchoolStagesId(Integer schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
    }

    /**
     * @return the schoolStagesId
     */
    public Integer getSchoolStagesId() {
        return schoolStagesId;
    }

    /**
     * @return the lessonId
     */
    public Integer getLessonId() {
        return lessonId;
    }

    /**
     * @return the questionsList
     */
    public List<QuestionsDTO> getQuestionsList() {
        return questionsList;
    }

    /**
     * @param questionsList the questionsList to set
     */
    public void setQuestionsList(List<QuestionsDTO> questionsList) {
        this.questionsList = questionsList;
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
     * @return the score
     */
    public BigDecimal getScore() {
        if(score == null){
            score = BigDecimal.ZERO;
        }
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * @return the active
     */
    public Integer getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Integer active) {
        this.active = active;
    }

    /**
     * @return the lessonName
     */
    public String getLessonName() {
        return lessonName;
    }

    /**
     * @param lessonName the lessonName to set
     */
    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    /**
     * @return the schoolStagesName
     */
    public String getSchoolStagesName() {
        return schoolStagesName;
    }

    /**
     * @param schoolStagesName the schoolStagesName to set
     */
    public void setSchoolStagesName(String schoolStagesName) {
        this.schoolStagesName = schoolStagesName;
    }

    /**
     * @return the monthId
     */
    public Integer getMonthId() {
        return monthId;
    }

    /**
     * @param monthId the monthId to set
     */
    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    /**
     * @return the periodId
     */
    public Integer getPeriodId() {
        return periodId;
    }

    /**
     * @param periodId the periodId to set
     */
    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

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
     * @return the examType
     */
    public Integer getExamType() {
        return examType;
    }

    /**
     * @param examType the examType to set
     */
    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    /**
     * @return the timer
     */
    public Integer getTimer() {
        return timer;
    }

    /**
     * @param timer the timer to set
     */
    public void setTimer(Integer timer) {
        this.timer = timer;
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

    /**
     * @return the materialList
     */
    public List<Integer> getMaterialList() {
        return materialList;
    }

    /**
     * @param materialList the materialList to set
     */
    public void setMaterialList(List<Integer> materialList) {
        this.materialList = materialList;
    }

    /**
     * @return the periodList
     */
    public List<Integer> getPeriodList() {
        return periodList;
    }

    /**
     * @param periodList the periodList to set
     */
    public void setPeriodList(List<Integer> periodList) {
        this.periodList = periodList;
    }

}
