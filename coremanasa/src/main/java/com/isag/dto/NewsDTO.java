/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author m lapan
 */

public class NewsDTO extends BaseEntityDTO {

    private Integer active;
    private Integer type;
    private Integer adrress;
    private String post;
    private Integer img;
    private SchoolStagesGuideDTO schoolStage;
    private IgmaUserDTO teacher;
    private Integer schoolStageId;
    private Integer teacherId;

    public NewsDTO() {
    }

    public NewsDTO(Integer id) {
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
        if (!(object instanceof NewsDTO)) {
            return false;
        }
        NewsDTO other = (NewsDTO) object;
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
     * @return the adrress
     */
    public Integer getAdrress() {
        return adrress;
    }

    /**
     * @param adrress the adrress to set
     */
    public void setAdrress(Integer adrress) {
        this.adrress = adrress;
    }

    /**
     * @return the img
     */
    public Integer getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(Integer img) {
        this.img = img;
    }

    /**
     * @return the schoolStage
     */
    public SchoolStagesGuideDTO getSchoolStage() {
        return schoolStage;
    }

    /**
     * @param schoolStage the schoolStage to set
     */
    public void setSchoolStage(SchoolStagesGuideDTO schoolStage) {
        this.schoolStage = schoolStage;
    }

    /**
     * @return the teacher
     */
    public IgmaUserDTO getTeacher() {
        return teacher;
    }

    /**
     * @param teacher the teacher to set
     */
    public void setTeacher(IgmaUserDTO teacher) {
        this.teacher = teacher;
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
     * @return the schoolStageId
     */
    public Integer getSchoolStageId() {
        return schoolStageId;
    }

    /**
     * @param schoolStageId the schoolStageId to set
     */
    public void setSchoolStageId(Integer schoolStageId) {
        this.schoolStageId = schoolStageId;
    }

    /**
     * @return the teacherId
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

}
