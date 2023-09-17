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
public class LessonVideosDTO extends BaseEntityDTO {

    private String videoLink;
    private String youtubeLink;
    private String vimeoLink;
    private Integer lessonId;

    public LessonVideosDTO() {
    }
    
    public LessonVideosDTO(Integer id) {
        this.id = id ;
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
        if (!(object instanceof LessonVideosDTO)) {
            return false;
        }
        LessonVideosDTO other = (LessonVideosDTO) object;
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
     * @return the lessonId
     */
    public Integer getLessonId() {
        return lessonId;
    }

    /**
     * @param lessonId the lessonId to set
     */
    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    /**
     * @return the videoLink
     */
    public String getVideoLink() {
        return videoLink;
    }

    /**
     * @param videoLink the videoLink to set
     */
    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    /**
     * @return the youtubeLink
     */
    public String getYoutubeLink() {
        return youtubeLink;
    }

    /**
     * @param youtubeLink the youtubeLink to set
     */
    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    /**
     * @return the vimeoLink
     */
    public String getVimeoLink() {
        return vimeoLink;
    }

    /**
     * @param vimeoLink the vimeoLink to set
     */
    public void setVimeoLink(String vimeoLink) {
        this.vimeoLink = vimeoLink;
    }

}
