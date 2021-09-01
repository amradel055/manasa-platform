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
@Table(name = "lesson")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lesson.findAll", query = "SELECT l FROM Lesson l")
    , @NamedQuery(name = "Lesson.findById", query = "SELECT l FROM Lesson l WHERE l.id = :id")
    , @NamedQuery(name = "Lesson.findByLessonName", query = "SELECT l FROM Lesson l WHERE l.lessonName = :lessonName")
    , @NamedQuery(name = "Lesson.findByLessonType", query = "SELECT l FROM Lesson l WHERE l.lessonType = :lessonType")
    , @NamedQuery(name = "Lesson.findByLessonDescription", query = "SELECT l FROM Lesson l WHERE l.lessonDescription = :lessonDescription")
    , @NamedQuery(name = "Lesson.findByVideoLink", query = "SELECT l FROM Lesson l WHERE l.videoLink = :videoLink")
    , @NamedQuery(name = "Lesson.findByCreationDate", query = "SELECT l FROM Lesson l WHERE l.creationDate = :creationDate")
    , @NamedQuery(name = "Lesson.findByModificationDate", query = "SELECT l FROM Lesson l WHERE l.modificationDate = :modificationDate")})
public class Lesson extends BaseEntity {

    @Size(max = 450)
    @Column(name = "lesson_name")
    private String lessonName;
    @Column(name = "lesson_type")
    private Integer lessonType;
    @Column(name = "month_id")
    private Integer monthId;
    @Size(max = 450)
    @Column(name = "lesson_description")
    private String lessonDescription;
    @Size(max = 450)
    @Column(name = "video_link")
    private String videoLink;
    @Size(max = 450)
    @Column(name = "vimeo_link")
    private String vimeoLink;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @JoinColumn(name = "school_stages_guide_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide schoolStagesGuideId;
    @Size(max = 450)
    @Column(name = "image")
    private String image;
    @Size(max = 450)
    @Column(name = "youtube_link")
    private String youtubeLink;
    @Size(max = 450)
    @Column(name = "thumb")
    private String thumb;
    @JoinColumn(name = "period_id", referencedColumnName = "id")
    @ManyToOne
    private Periods periodId;

    public Lesson() {
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Integer getLessonType() {
        return lessonType;
    }

    public void setLessonType(Integer lessonType) {
        this.lessonType = lessonType;
    }

    public String getLessonDescription() {
        return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
        this.lessonDescription = lessonDescription;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
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
        if (!(object instanceof Lesson)) {
            return false;
        }
        Lesson other = (Lesson) object;
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
     * @return the monthId
     */
    public Integer getMonthId() {
        return monthId;
    }

    /**
     * @param monthId the monthId to set
     */
    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    /**
     * @return the schoolStagesGuideId
     */
    public SchoolStagesGuide getSchoolStagesGuideId() {
        return schoolStagesGuideId;
    }

    /**
     * @param schoolStagesGuideId the schoolStagesGuideId to set
     */
    public void setSchoolStagesGuideId(SchoolStagesGuide schoolStagesGuideId) {
        this.schoolStagesGuideId = schoolStagesGuideId;
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
     * @return the thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * @param thumb the thumb to set
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    /**
     * @return the periodId
     */
    public Periods getPeriodId() {
        return periodId;
    }

    /**
     * @param periodId the periodId to set
     */
    public void setPeriodId(Periods periodId) {
        this.periodId = periodId;
    }

}
