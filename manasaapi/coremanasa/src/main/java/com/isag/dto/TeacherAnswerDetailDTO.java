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
public class TeacherAnswerDetailDTO extends BaseEntityDTO {

    private String description;
    private String image;
    private Integer teacherAnswerId;

    public TeacherAnswerDetailDTO() {
    }

    public TeacherAnswerDetailDTO(Integer id) {
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
        if (!(object instanceof TeacherAnswerDetailDTO)) {
            return false;
        }
        TeacherAnswerDetailDTO other = (TeacherAnswerDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Student[ id=" + id + " ]";
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
    public Integer getTeacherAnswerId() {
        return teacherAnswerId;
    }

    /**
     * @param teacherAnswerId the teacherAnswerId to set
     */
    public void setTeacherAnswerId(Integer teacherAnswerId) {
        this.teacherAnswerId = teacherAnswerId;
    }

}
