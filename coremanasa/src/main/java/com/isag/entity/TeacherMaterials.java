/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
@Table(name = "teacher_materials")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TeacherMaterials.findAll", query = "SELECT t FROM TeacherMaterials t")
    , @NamedQuery(name = "TeacherMaterials.findById", query = "SELECT t FROM TeacherMaterials t WHERE t.id = :id")
    , @NamedQuery(name = "TeacherMaterials.findByCreationDate", query = "SELECT t FROM TeacherMaterials t WHERE t.creationDate = :creationDate")
    , @NamedQuery(name = "TeacherMaterials.findByModificationDate", query = "SELECT t FROM TeacherMaterials t WHERE t.modificationDate = :modificationDate")})
public class TeacherMaterials extends BaseEntity {

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @ManyToOne
    private Materials materialId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser teacherId;

    public TeacherMaterials() {
    }

    public TeacherMaterials(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Materials getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Materials materialId) {
        this.materialId = materialId;
    }

    public IgmaUser getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(IgmaUser teacherId) {
        this.teacherId = teacherId;
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
        if (!(object instanceof TeacherMaterials)) {
            return false;
        }
        TeacherMaterials other = (TeacherMaterials) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.TeacherMaterials[ id=" + id + " ]";
    }

}
