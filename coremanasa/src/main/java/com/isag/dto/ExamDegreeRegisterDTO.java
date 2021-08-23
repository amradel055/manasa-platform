/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.*;
import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
public class ExamDegreeRegisterDTO extends BaseEntityDTO {

    private Long degree;
    private Integer serial;
    private String name;
    private String code;
    private Date date;
    private ScheduleGuide scheduleId;
    private StudyGroup studyGroupId;
    private Collection<ExamDegreeRegisterDetail> examDegreeRegisterDetailCollection;

    public ExamDegreeRegisterDTO() {
    }

    public ExamDegreeRegisterDTO(Integer id) {
        this.id = id;
    }

    public Long getDegree() {
        return degree;
    }

    public void setDegree(Long degree) {
        this.degree = degree;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    

    public ScheduleGuide getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(ScheduleGuide scheduleId) {
        this.scheduleId = scheduleId;
    }

    public StudyGroup getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(StudyGroup studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    @XmlTransient
    public Collection<ExamDegreeRegisterDetail> getExamDegreeRegisterDetailCollection() {
        return examDegreeRegisterDetailCollection;
    }

    public void setExamDegreeRegisterDetailCollection(Collection<ExamDegreeRegisterDetail> examDegreeRegisterDetailCollection) {
        this.examDegreeRegisterDetailCollection = examDegreeRegisterDetailCollection;
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
        if (!(object instanceof ExamDegreeRegisterDTO)) {
            return false;
        }
        ExamDegreeRegisterDTO other = (ExamDegreeRegisterDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.ExamDegreeRegister[ id=" + id + " ]";
    }

}
