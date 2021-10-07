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
public class LessonImgDTO extends BaseEntityDTO {

    private String img;
    private LessonDTO lessonId;

    public LessonImgDTO() {
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
        if (!(object instanceof LessonImgDTO)) {
            return false;
        }
        LessonImgDTO other = (LessonImgDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.Lesson[ id=" + id + " ]";
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
     * @return the lessonId
     */
    public LessonDTO getLessonId() {
        return lessonId;
    }

    /**
     * @param lessonId the lessonId to set
     */
    public void setLessonId(LessonDTO lessonId) {
        this.lessonId = lessonId;
    }

}
