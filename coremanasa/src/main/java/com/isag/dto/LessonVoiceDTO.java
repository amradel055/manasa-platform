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
public class LessonVoiceDTO extends BaseEntityDTO {

    private String voice;
    private LessonDTO lessonId;

    public LessonVoiceDTO() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LessonVoiceDTO)) {
            return false;
        }
        LessonVoiceDTO other = (LessonVoiceDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
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
    public LessonDTO getLessonId() {
        return lessonId;
    }

    /**
     * @param lessonId the lessonId to set
     */
    public void setLessonId(LessonDTO lessonId) {
        this.lessonId = lessonId;
    }

    /**
     * @return the voice
     */
    public String getVoice() {
        return voice;
    }

    /**
     * @param voice the voice to set
     */
    public void setVoice(String voice) {
        this.voice = voice;
    }

}
