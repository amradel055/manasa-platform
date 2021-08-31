/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "exams")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exams.findAll", query = "SELECT e FROM Exams e")
    , @NamedQuery(name = "Exams.findById", query = "SELECT e FROM Exams e WHERE e.id = :id")
    , @NamedQuery(name = "Exams.findByCreationDate", query = "SELECT e FROM Exams e WHERE e.creationDate = :creationDate")
    , @NamedQuery(name = "Exams.findByModificationDate", query = "SELECT e FROM Exams e WHERE e.modificationDate = :modificationDate")})
public class Exams extends BaseEntity {

    @Size(max = 450)
    @Column(name = "exam_name")
    private String examName;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    @ManyToOne
    private Lesson lessonId;
    @JoinColumn(name = "school_stages_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide schoolStagesId;
    @Column(name = "degree")
    private BigDecimal degree;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private Exams parent;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "period_id", referencedColumnName = "id")
    @ManyToOne
    private Periods periodId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser teacherId;
    @Column(name = "score")
    private BigDecimal score;
    @Column(name = "active")
    private Integer active;
//    @Column(name = "timer")
//    private Integer timer;
    @Column(name = "monthId")
    private Integer monthId;
    @Column(name = "exam_type")
    private Integer examType;

    public Exams() {
    }

    public Exams(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Lesson getLessonId() {
        return lessonId;
    }

    public void setLessonId(Lesson lessonId) {
        this.lessonId = lessonId;
    }

    public SchoolStagesGuide getSchoolStagesId() {
        return schoolStagesId;
    }

    public void setSchoolStagesId(SchoolStagesGuide schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
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
        if (!(object instanceof Exams)) {
            return false;
        }
        Exams other = (Exams) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Exams[ id=" + id + " ]";
    }

    /**
     * @return the examName
     */
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
        return degree;
    }

    /**
     * @param degree the degree to set
     */
    public void setDegree(BigDecimal degree) {
        this.degree = degree;
    }

    /**
     * @return the parent
     */
    public Exams getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Exams parent) {
        this.parent = parent;
    }

    /**
     * @return the studentId
     */
    public Student getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the score
     */
    public BigDecimal getScore() {
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
    public Periods getPeriodId() {
        return periodId;
    }

    /**
     * @param periodId the periodId to set
     */
    public void setPeriodId(Periods periodId) {
        this.periodId = periodId;
    }

   
    public IgmaUser getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(IgmaUser teacherId) {
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
//    public Integer getTimer() {
//        return timer;
//    }
//
//    /**
//     * @param timer the timer to set
//     */
//    public void setTimer(Integer timer) {
//        this.timer = timer;
//    }
    /**
     * @return the timer
     */
}
