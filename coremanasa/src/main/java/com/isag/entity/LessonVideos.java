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
import javax.validation.constraints.Size;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "lesson_videos")
public class LessonVideos extends BaseEntity {

    @Size(max = 455)
    @Column(name = "video_link")
    private String videoLink;
    @Size(max = 455)
    @Column(name = "youtube_link")
    private String youtubeLink;
    @Size(max = 455)
    @Column(name = "vimeo_link")
    private String vimeoLink;
    @JoinColumn(name = "lesson_id", referencedColumnName = "id")
    @ManyToOne
    private Lesson lessonId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public LessonVideos() {
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
        if (!(object instanceof LessonVideos)) {
            return false;
        }
        LessonVideos other = (LessonVideos) object;
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
    public Lesson getLessonId() {
        return lessonId;
    }

    /**
     * @param lessonId the lessonId to set
     */
    public void setLessonId(Lesson lessonId) {
        this.lessonId = lessonId;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
