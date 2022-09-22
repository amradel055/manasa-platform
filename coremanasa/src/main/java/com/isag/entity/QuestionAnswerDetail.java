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
import javax.persistence.Table;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "question_answer")
public class QuestionAnswerDetail extends BaseEntity {

    @Column(name = "text")
    private String text;
    @Column(name = "image")
    private String image;
    
    @JoinColumn(name = "question_answer_id", referencedColumnName = "id")
    @ManyToOne
    private QuestionAnswer questionAnswerId;
  
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
 

    public QuestionAnswerDetail() {
    }

    public QuestionAnswerDetail(Integer id) {
        this.id = id;
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
        if (!(object instanceof QuestionAnswerDetail)) {
            return false;
        }
        QuestionAnswerDetail other = (QuestionAnswerDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.StudentPeriods[ id=" + id + " ]";
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
     * @return the questionAnswerId
     */
    public QuestionAnswer getQuestionAnswerId() {
        return questionAnswerId;
    }

    /**
     * @param questionAnswerId the questionAnswerId to set
     */
    public void setQuestionAnswerId(QuestionAnswer questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }


}
