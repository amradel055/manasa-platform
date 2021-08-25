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
@Table(name = "school_stages_guide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SchoolStagesGuide.findAll", query = "SELECT s FROM SchoolStagesGuide s")
    , @NamedQuery(name = "SchoolStagesGuide.findById", query = "SELECT s FROM SchoolStagesGuide s WHERE s.id = :id")
    , @NamedQuery(name = "SchoolStagesGuide.findByCode", query = "SELECT s FROM SchoolStagesGuide s WHERE s.code = :code")
    , @NamedQuery(name = "SchoolStagesGuide.findByName", query = "SELECT s FROM SchoolStagesGuide s WHERE s.name = :name")
    , @NamedQuery(name = "SchoolStagesGuide.findByCreationDate", query = "SELECT s FROM SchoolStagesGuide s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "SchoolStagesGuide.findByModificationDate", query = "SELECT s FROM SchoolStagesGuide s WHERE s.modificationDate = :modificationDate")
    , @NamedQuery(name = "SchoolStagesGuide.findByLevel", query = "SELECT s FROM SchoolStagesGuide s WHERE s.level = :level")
    , @NamedQuery(name = "SchoolStagesGuide.findByType", query = "SELECT s FROM SchoolStagesGuide s WHERE s.type = :type")})
public class SchoolStagesGuide extends BaseEntity {

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
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private Teachers teacherId;

    public SchoolStagesGuide() {
    }

    public SchoolStagesGuide(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SchoolStagesGuide)) {
            return false;
        }
        SchoolStagesGuide other = (SchoolStagesGuide) object;
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
     * @return the teacherId
     */
    public Teachers getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(Teachers teacherId) {
        this.teacherId = teacherId;
    }

}
