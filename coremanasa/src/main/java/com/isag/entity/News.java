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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "news")
@XmlRootElement
public class News extends BaseEntity {

    @Column(name = "active")
    private Integer active;
    @Column(name = "type")
    private Integer type;
    @Column(name = "adrress")
    private String adrress;
    @Column(name = "post")
    private String post;
    @Column(name = "img")
    private String img;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "school_stage_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide schoolStageId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser teacherId;

    public News() {
    }

    public News(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public SchoolStagesGuide getSchoolStageId() {
        return schoolStageId;
    }

    public void setSchoolStageId(SchoolStagesGuide schoolStageId) {
        this.schoolStageId = schoolStageId;
    }

    public IgmaUser getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(IgmaUser teacherId) {
        this.teacherId = teacherId;
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
        if (!(object instanceof News)) {
            return false;
        }
        News other = (News) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Appointments[ id=" + id + " ]";
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
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the post
     */
    public String getPost() {
        return post;
    }

    /**
     * @param post the post to set
     */
    public void setPost(String post) {
        this.post = post;
    }

    /**
     * @return the adrress
     */
    public String getAdrress() {
        return adrress;
    }

    /**
     * @param adrress the adrress to set
     */
    public void setAdrress(String adrress) {
        this.adrress = adrress;
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
