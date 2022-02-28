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

public class StudyGroupDTO extends BaseEntityDTO {

    
    private String name;
    private String code;
    private Integer serial;
    private SchoolStagesGuide schoolStagesId;
    private Collection<ExamDegreeRegister> examDegreeRegisterCollection;
    private Collection<StudentStudyGroup> studentStudyGroupCollection;

    public StudyGroupDTO() {
    }

    public StudyGroupDTO(Integer id) {
        this.id = id;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

   

    

    

    public SchoolStagesGuide getSchoolStagesId() {
        return schoolStagesId;
    }

    public void setSchoolStagesId(SchoolStagesGuide schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
    }

    @XmlTransient
    public Collection<ExamDegreeRegister> getExamDegreeRegisterCollection() {
        return examDegreeRegisterCollection;
    }

    public void setExamDegreeRegisterCollection(Collection<ExamDegreeRegister> examDegreeRegisterCollection) {
        this.examDegreeRegisterCollection = examDegreeRegisterCollection;
    }

    @XmlTransient
    public Collection<StudentStudyGroup> getStudentStudyGroupCollection() {
        return studentStudyGroupCollection;
    }

    public void setStudentStudyGroupCollection(Collection<StudentStudyGroup> studentStudyGroupCollection) {
        this.studentStudyGroupCollection = studentStudyGroupCollection;
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
        if (!(object instanceof StudyGroupDTO)) {
            return false;
        }
        StudyGroupDTO other = (StudyGroupDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.StudyGroup[ id=" + id + " ]";
    }
    
}
