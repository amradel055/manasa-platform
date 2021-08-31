/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
@Table(name = "answers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Answers.findAll", query = "SELECT a FROM Answers a")
    , @NamedQuery(name = "Answers.findById", query = "SELECT a FROM Answers a WHERE a.id = :id")
    , @NamedQuery(name = "Answers.findByAnswer", query = "SELECT a FROM Answers a WHERE a.answer = :answer")
    , @NamedQuery(name = "Answers.findByCorrect", query = "SELECT a FROM Answers a WHERE a.correct = :correct")
    , @NamedQuery(name = "Answers.findByReason", query = "SELECT a FROM Answers a WHERE a.reason = :reason")
    , @NamedQuery(name = "Answers.findByCreationDate", query = "SELECT a FROM Answers a WHERE a.creationDate = :creationDate")
    , @NamedQuery(name = "Answers.findByModificationDate", query = "SELECT a FROM Answers a WHERE a.modificationDate = :modificationDate")})
public class Answers extends BaseEntity {

    @Size(max = 450)
    @Column(name = "answer")
    private String answer;
    @Column(name = "correct")
    private Integer correct;
    @Size(max = 450)
    @Column(name = "reason")
    private String reason;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne
    private Questions questionId;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private Answers parent;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private Student studentId;
    @JoinColumn(name = "exam_id", referencedColumnName = "id")
    @ManyToOne
    private Exams examId;
    @Column(name = "selection")
    private Integer selection;
    @Column(name = "img")
    private String img;

    public Answers() {
    }

    public Answers(Integer id) {
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Questions getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Questions questionId) {
        this.questionId = questionId;
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
        if (!(object instanceof Answers)) {
            return false;
        }
        Answers other = (Answers) object;
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
     * @return the parent
     */
    public Answers getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Answers parent) {
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
     * @return the examId
     */
    public Exams getExamId() {
        return examId;
    }

    /**
     * @param examId the examId to set
     */
    public void setExamId(Exams examId) {
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
