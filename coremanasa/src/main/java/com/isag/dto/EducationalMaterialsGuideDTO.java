/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.entity.*;

/**
 *
 * @author ahmed
 */
public class EducationalMaterialsGuideDTO extends BaseEntityDTO{

    private String code;
    private String name;
    private Integer level;
    private Integer type;
    private SchoolStagesGuide parent;
    private SchoolStagesGuide schoolStagesId;

    public EducationalMaterialsGuideDTO() {
    }

    public EducationalMaterialsGuideDTO(Integer id) {
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

    public SchoolStagesGuide getParent() {
        return parent;
    }

    public void setParent(SchoolStagesGuide parent) {
        this.parent = parent;
    }

    public SchoolStagesGuide getSchoolStagesId() {
        return schoolStagesId;
    }

    public void setSchoolStagesId(SchoolStagesGuide schoolStagesId) {
        this.schoolStagesId = schoolStagesId;
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
        if (!(object instanceof EducationalMaterialsGuideDTO)) {
            return false;
        }
        EducationalMaterialsGuideDTO other = (EducationalMaterialsGuideDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.EducationalMaterialsGuide[ id=" + id + " ]";
    }

}
