/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "schedule_guide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScheduleGuide.findAll", query = "SELECT s FROM ScheduleGuide s")
    , @NamedQuery(name = "ScheduleGuide.findById", query = "SELECT s FROM ScheduleGuide s WHERE s.id = :id")
    , @NamedQuery(name = "ScheduleGuide.findByName", query = "SELECT s FROM ScheduleGuide s WHERE s.name = :name")
    , @NamedQuery(name = "ScheduleGuide.findByCode", query = "SELECT s FROM ScheduleGuide s WHERE s.code = :code")
    , @NamedQuery(name = "ScheduleGuide.findByActive", query = "SELECT s FROM ScheduleGuide s WHERE s.active = :active")
    , @NamedQuery(name = "ScheduleGuide.findByCreationDate", query = "SELECT s FROM ScheduleGuide s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "ScheduleGuide.findByModificationDate", query = "SELECT s FROM ScheduleGuide s WHERE s.modificationDate = :modificationDate")
    , @NamedQuery(name = "ScheduleGuide.findByLevel", query = "SELECT s FROM ScheduleGuide s WHERE s.level = :level")})
public class ScheduleGuide extends BaseEntity {

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Column(name = "active")
    private Boolean active;

    @Column(name = "level")
    private Integer level;
    @OneToMany(mappedBy = "scheduleId")
    private Collection<ExamDegreeRegister> examDegreeRegisterCollection;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @OneToMany(mappedBy = "parent")
    private Collection<ScheduleGuide> scheduleGuideCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private ScheduleGuide parent;

    public ScheduleGuide() {
    }

    public ScheduleGuide(Integer id) {
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    @XmlTransient
    public Collection<ScheduleGuide> getScheduleGuideCollection() {
        return scheduleGuideCollection;
    }

    public void setScheduleGuideCollection(Collection<ScheduleGuide> scheduleGuideCollection) {
        this.scheduleGuideCollection = scheduleGuideCollection;
    }

    public ScheduleGuide getParent() {
        return parent;
    }

    public void setParent(ScheduleGuide parent) {
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
        if (!(object instanceof ScheduleGuide)) {
            return false;
        }
        ScheduleGuide other = (ScheduleGuide) object;
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
