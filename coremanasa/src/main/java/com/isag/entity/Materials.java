/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
    @OneToMany(mappedBy = "materialId")
    private Collection<TeacherMaterials> teacherMaterialsCollection;

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

    @XmlTransient
    public Collection<TeacherMaterials> getTeacherMaterialsCollection() {
        return teacherMaterialsCollection;
    }

    public void setTeacherMaterialsCollection(Collection<TeacherMaterials> teacherMaterialsCollection) {
        this.teacherMaterialsCollection = teacherMaterialsCollection;
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

}
