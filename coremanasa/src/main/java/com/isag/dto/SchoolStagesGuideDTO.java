/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.*;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
public class SchoolStagesGuideDTO extends BaseEntityDTO {

    private String code;
    private String name;
    private Integer level;
    private Integer type;
    private Collection<Student> studentCollection;
    private Collection<EducationalMaterialsGuide> educationalMaterialsGuideCollection;
    private Collection<EducationalMaterialsGuide> educationalMaterialsGuideCollection1;
    private Collection<StudyGroup> studyGroupCollection;
    private Collection<SchoolStagesGuideDTO> schoolStagesGuideCollection;
    private SchoolStagesGuideDTO parent;
    private Integer teacherId;

    public SchoolStagesGuideDTO() {
    }

    public SchoolStagesGuideDTO(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @XmlTransient
    public Collection<Student> getStudentCollection() {
        return studentCollection;
    }

    public void setStudentCollection(Collection<Student> studentCollection) {
        this.studentCollection = studentCollection;
    }

    @XmlTransient
    public Collection<EducationalMaterialsGuide> getEducationalMaterialsGuideCollection() {
        return educationalMaterialsGuideCollection;
    }

    public void setEducationalMaterialsGuideCollection(Collection<EducationalMaterialsGuide> educationalMaterialsGuideCollection) {
        this.educationalMaterialsGuideCollection = educationalMaterialsGuideCollection;
    }

    @XmlTransient
    public Collection<EducationalMaterialsGuide> getEducationalMaterialsGuideCollection1() {
        return educationalMaterialsGuideCollection1;
    }

    public void setEducationalMaterialsGuideCollection1(Collection<EducationalMaterialsGuide> educationalMaterialsGuideCollection1) {
        this.educationalMaterialsGuideCollection1 = educationalMaterialsGuideCollection1;
    }

    @XmlTransient
    public Collection<StudyGroup> getStudyGroupCollection() {
        return studyGroupCollection;
    }

    public void setStudyGroupCollection(Collection<StudyGroup> studyGroupCollection) {
        this.studyGroupCollection = studyGroupCollection;
    }

    @XmlTransient
    public Collection<SchoolStagesGuideDTO> getSchoolStagesGuideCollection() {
        return schoolStagesGuideCollection;
    }

    public void setSchoolStagesGuideCollection(Collection<SchoolStagesGuideDTO> schoolStagesGuideCollection) {
        this.schoolStagesGuideCollection = schoolStagesGuideCollection;
    }

    public SchoolStagesGuideDTO getParent() {
        return parent;
    }

    public void setParent(SchoolStagesGuideDTO parent) {
        this.parent = parent;
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
        if (!(object instanceof SchoolStagesGuideDTO)) {
            return false;
        }
        SchoolStagesGuideDTO other = (SchoolStagesGuideDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.SchoolStagesGuide[ id=" + id + " ]";
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
