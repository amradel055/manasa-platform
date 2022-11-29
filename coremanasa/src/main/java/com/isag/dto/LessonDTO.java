/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ahmed
 */
public class LessonDTO extends BaseEntityDTO {

    private String lessonName;
    private Integer lessonType;
    private Integer periodId;
    private String periodName;

    private Integer monthId;
    private String lessonDescription;
    private String videoLink;
    private SchoolStagesGuideDTO schoolStagesGuideId;
    private Integer schoolId;
    private String image;
    private String youtubeLink;
    private String vimeoLink;
    private String thumb;
    private String schoolStagesName;
    private List<String> documents;
    private Integer materialId;
    private String materialName;

    private Integer teacherId;
    private Integer studentId;
    private String voiceLink;
    private List<String> images;
    private List<String> lessonVoiceList;
    private List<LessonImgDTO> lessonImgDTOList;
    private List<LessonVoiceDTO> lessonVoiceDTOList;
    private List<DocumentsDTO> documentsDTOList;

    public LessonDTO() {
    }

    public LessonDTO(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LessonDTO)) {
            return false;
        }
        LessonDTO other = (LessonDTO) object;
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

    public Integer getSchoolId() {
        return schoolId;
    }

    /**
     * @param schoolId the schoolId to set
     */
    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    /**
     * @return the schoolStagesGuideId
     */
    public SchoolStagesGuideDTO getSchoolStagesGuideId() {
        return schoolStagesGuideId;
    }

    /**
     * @param schoolStagesGuideId the schoolStagesGuideId to set
     */
    public void setSchoolStagesGuideId(SchoolStagesGuideDTO schoolStagesGuideId) {
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
     * @return the schoolStagesName
     */
    public String getSchoolStagesName() {
        return schoolStagesName;
    }

    /**
     * @param schoolStagesName the schoolStagesName to set
     */
    public void setSchoolStagesName(String schoolStagesName) {
        this.schoolStagesName = schoolStagesName;
    }

    /**
     * @return the documents
     */
    public List<String> getDocuments() {
        return documents;
    }

    /**
     * @param documents the documents to set
     */
    public void setDocuments(List<String> documents) {
        this.documents = documents;
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
    public Integer getPeriodId() {
        return periodId;
    }

    /**
     * @param periodId the periodId to set
     */
    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    /**
     * @return the materialId
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
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

    /**
     * @return the voiceLink
     */
    public String getVoiceLink() {
        return voiceLink;
    }

    /**
     * @param voiceLink the voiceLink to set
     */
    public void setVoiceLink(String voiceLink) {
        this.voiceLink = voiceLink;
    }

    /**
     * @return the images
     */
    public List<String> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * @return the studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the lessonImgDTOList
     */
    public List<LessonImgDTO> getLessonImgDTOList() {
        if (lessonImgDTOList == null) {
            lessonImgDTOList = new ArrayList<>();
        }
        return lessonImgDTOList;
    }

    /**
     * @param lessonImgDTOList the lessonImgDTOList to set
     */
    public void setLessonImgDTOList(List<LessonImgDTO> lessonImgDTOList) {
        this.lessonImgDTOList = lessonImgDTOList;
    }

    /**
     * @return the lessonVoiceDTOList
     */
    public List<LessonVoiceDTO> getLessonVoiceDTOList() {
        if (lessonVoiceDTOList == null) {
            lessonVoiceDTOList = new ArrayList<>();
        }
        return lessonVoiceDTOList;
    }

    /**
     * @param lessonVoiceDTOList the lessonVoiceDTOList to set
     */
    public void setLessonVoiceDTOList(List<LessonVoiceDTO> lessonVoiceDTOList) {
        this.lessonVoiceDTOList = lessonVoiceDTOList;
    }

    /**
     * @return the documentsDTOList
     */
    public List<DocumentsDTO> getDocumentsDTOList() {
        if (documentsDTOList == null) {
            documentsDTOList = new ArrayList<>();
        }
        return documentsDTOList;
    }

    /**
     * @param documentsDTOList the documentsDTOList to set
     */
    public void setDocumentsDTOList(List<DocumentsDTO> documentsDTOList) {
        this.documentsDTOList = documentsDTOList;
    }

    /**
     * @return the periodName
     */
    public String getPeriodName() {
        return periodName;
    }

    /**
     * @param periodName the periodName to set
     */
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    /**
     * @return the materialName
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * @param materialName the materialName to set
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * @return the lessonVoiceList
     */
    public List<String> getLessonVoiceList() {
        return lessonVoiceList;
    }

    /**
     * @param lessonVoiceList the lessonVoiceList to set
     */
    public void setLessonVoiceList(List<String> lessonVoiceList) {
        this.lessonVoiceList = lessonVoiceList;
    }

}
