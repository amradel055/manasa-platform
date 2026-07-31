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
@Table(name = "questions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Questions.findAll", query = "SELECT q FROM Questions q")
    , @NamedQuery(name = "Questions.findById", query = "SELECT q FROM Questions q WHERE q.id = :id")
    , @NamedQuery(name = "Questions.findByQuestionName", query = "SELECT q FROM Questions q WHERE q.questionName = :questionName")
    , @NamedQuery(name = "Questions.findByQuestionType", query = "SELECT q FROM Questions q WHERE q.questionType = :questionType")
    , @NamedQuery(name = "Questions.findByQuestionDegree", query = "SELECT q FROM Questions q WHERE q.questionDegree = :questionDegree")
    , @NamedQuery(name = "Questions.findByCreationDate", query = "SELECT q FROM Questions q WHERE q.creationDate = :creationDate")
    , @NamedQuery(name = "Questions.findByModificationDate", query = "SELECT q FROM Questions q WHERE q.modificationDate = :modificationDate")})
public class Questions extends BaseEntity {

    @Size(max = 450)
    @Column(name = "question_name")
    private String questionName;
    @Column(name = "question_type")
    private Integer questionType;
    @Column(name = "question_degree")
    private BigDecimal questionDegree;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "exams_id", referencedColumnName = "id")
    @ManyToOne
    private Exams examsId;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private Questions parent;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;
    @Size(max = 450)
    @Column(name = "img")
    private String img;
    @Column(name = "group_id")
    private Integer groupId;

    public Questions() {
    }

    public Questions(Integer id) {
        this.id = id;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof Questions)) {
            return false;
        }
        Questions other = (Questions) object;
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
     * @return the examsId
     */
    public Exams getExamsId() {
        return examsId;
    }

    /**
     * @param examsId the examsId to set
     */
    public void setExamsId(Exams examsId) {
        this.examsId = examsId;
    }

    /**
     * @return the parent
     */
    public Questions getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Questions parent) {
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
     * @return the questionDegree
     */
    public BigDecimal getQuestionDegree() {
        return questionDegree;
    }

    /**
     * @param questionDegree the questionDegree to set
     */
    public void setQuestionDegree(BigDecimal questionDegree) {
        this.questionDegree = questionDegree;
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

    /**
     * @return the groupId
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
