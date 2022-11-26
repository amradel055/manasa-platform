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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "materials")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materials.findAll", query = "SELECT m FROM Materials m")
    , @NamedQuery(name = "Materials.findById", query = "SELECT m FROM Materials m WHERE m.id = :id")
    , @NamedQuery(name = "Materials.findByName", query = "SELECT m FROM Materials m WHERE m.name = :name")
    , @NamedQuery(name = "Materials.findByCreationDate", query = "SELECT m FROM Materials m WHERE m.creationDate = :creationDate")
    , @NamedQuery(name = "Materials.findByModificationDate", query = "SELECT m FROM Materials m WHERE m.modificationDate = :modificationDate")})
public class Materials extends BaseEntity {

    @Column(name = "name")
    private String name;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser teacherId;
    @JoinColumn(name = "stage_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide stageId;

    public Materials() {
    }

    public Materials(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof Materials)) {
            return false;
        }
        Materials other = (Materials) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Materials[ id=" + id + " ]";
    }

    /**
     * @return the teacherId
     */
    public IgmaUser getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(IgmaUser teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return the stageId
     */
    public SchoolStagesGuide getStageId() {
        return stageId;
    }

    /**
     * @param stageId the stageId to set
     */
    public void setStageId(SchoolStagesGuide stageId) {
        this.stageId = stageId;
    }

}
