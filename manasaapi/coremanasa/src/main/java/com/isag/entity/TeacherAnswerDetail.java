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
@Table(name = "teacher_answer_detail")
public class TeacherAnswerDetail extends BaseEntity {

    @Column(name = "description")
    private String description;
    @Column(name = "image")
    private String image;
    
    @JoinColumn(name = "teacher_answer_id", referencedColumnName = "id")
    @ManyToOne
    private TeacherAnswers teacherAnswerId;
  
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
 

    public TeacherAnswerDetail() {
    }

    public TeacherAnswerDetail(Integer id) {
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
        if (!(object instanceof TeacherAnswerDetail)) {
            return false;
        }
        TeacherAnswerDetail other = (TeacherAnswerDetail) object;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the teacherAnswerId
     */
    public TeacherAnswers getTeacherAnswerId() {
        return teacherAnswerId;
    }

    /**
     * @param teacherAnswerId the teacherAnswerId to set
     */
    public void setTeacherAnswerId(TeacherAnswers teacherAnswerId) {
        this.teacherAnswerId = teacherAnswerId;
    }


}
