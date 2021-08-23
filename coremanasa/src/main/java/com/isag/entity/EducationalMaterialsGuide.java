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
@Table(name = "educational_materials_guide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EducationalMaterialsGuide.findAll", query = "SELECT e FROM EducationalMaterialsGuide e")
    , @NamedQuery(name = "EducationalMaterialsGuide.findById", query = "SELECT e FROM EducationalMaterialsGuide e WHERE e.id = :id")
    , @NamedQuery(name = "EducationalMaterialsGuide.findByCode", query = "SELECT e FROM EducationalMaterialsGuide e WHERE e.code = :code")
    , @NamedQuery(name = "EducationalMaterialsGuide.findByName", query = "SELECT e FROM EducationalMaterialsGuide e WHERE e.name = :name")
    , @NamedQuery(name = "EducationalMaterialsGuide.findByCreationDate", query = "SELECT e FROM EducationalMaterialsGuide e WHERE e.creationDate = :creationDate")
    , @NamedQuery(name = "EducationalMaterialsGuide.findByModificationDate", query = "SELECT e FROM EducationalMaterialsGuide e WHERE e.modificationDate = :modificationDate")
    , @NamedQuery(name = "EducationalMaterialsGuide.findByLevel", query = "SELECT e FROM EducationalMaterialsGuide e WHERE e.level = :level")
    , @NamedQuery(name = "EducationalMaterialsGuide.findByType", query = "SELECT e FROM EducationalMaterialsGuide e WHERE e.type = :type")})
public class EducationalMaterialsGuide extends BaseEntity {

   
    @Column(name = "code")
    private String code;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    
    @Column(name = "level")
    private Integer level;
    @Column(name = "type")
    private Integer type;
    
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide parent;
    @JoinColumn(name = "school_stages_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide schoolStagesId;
    

    public EducationalMaterialsGuide() {
    }

    public EducationalMaterialsGuide(Integer id) {
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
        if (!(object instanceof EducationalMaterialsGuide)) {
            return false;
        }
        EducationalMaterialsGuide other = (EducationalMaterialsGuide) object;
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
