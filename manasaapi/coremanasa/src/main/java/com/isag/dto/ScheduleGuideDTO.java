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

public class ScheduleGuideDTO extends BaseEntityDTO {

    private String name;
    private String code;
    private Boolean active;
    private Integer level;
    private Collection<ExamDegreeRegister> examDegreeRegisterCollection;
    private Collection<ScheduleGuideDTO> scheduleGuideCollection;
    private ScheduleGuideDTO parent;

    public ScheduleGuideDTO() {
    }

    public ScheduleGuideDTO(Integer id) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @XmlTransient
    public Collection<ExamDegreeRegister> getExamDegreeRegisterCollection() {
        return examDegreeRegisterCollection;
    }

    public void setExamDegreeRegisterCollection(Collection<ExamDegreeRegister> examDegreeRegisterCollection) {
        this.examDegreeRegisterCollection = examDegreeRegisterCollection;
    }

    

    @XmlTransient
    public Collection<ScheduleGuideDTO> getScheduleGuideCollection() {
        return scheduleGuideCollection;
    }

    public void setScheduleGuideCollection(Collection<ScheduleGuideDTO> scheduleGuideCollection) {
        this.scheduleGuideCollection = scheduleGuideCollection;
    }

    public ScheduleGuideDTO getParent() {
        return parent;
    }

    public void setParent(ScheduleGuideDTO parent) {
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
        if (!(object instanceof ScheduleGuideDTO)) {
            return false;
        }
        ScheduleGuideDTO other = (ScheduleGuideDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.ScheduleGuide[ id=" + id + " ]";
    }

}
