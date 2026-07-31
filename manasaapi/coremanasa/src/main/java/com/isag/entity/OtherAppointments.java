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
@Table(name = "other_appointments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appointments.findAll", query = "SELECT a FROM Appointments a")
    , @NamedQuery(name = "Appointments.findById", query = "SELECT a FROM Appointments a WHERE a.id = :id")
    , @NamedQuery(name = "Appointments.findByDay", query = "SELECT a FROM Appointments a WHERE a.day = :day")
    , @NamedQuery(name = "Appointments.findByCreationDate", query = "SELECT a FROM Appointments a WHERE a.creationDate = :creationDate")
    , @NamedQuery(name = "Appointments.findByModificationDate", query = "SELECT a FROM Appointments a WHERE a.modificationDate = :modificationDate")})
public class OtherAppointments extends BaseEntity {

    @Column(name = "other_appointments_type")
    private Integer appointmentsType;
    @Column(name = "img")
    private String img;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "school_stage_id", referencedColumnName = "id")
    @ManyToOne
    private SchoolStagesGuide schoolStageId;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser teacherId;
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @ManyToOne
    private Materials materialId;

    public OtherAppointments() {
    }

    public OtherAppointments(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public SchoolStagesGuide getSchoolStageId() {
        return schoolStageId;
    }

    public void setSchoolStageId(SchoolStagesGuide schoolStageId) {
        this.schoolStageId = schoolStageId;
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
        if (!(object instanceof OtherAppointments)) {
            return false;
        }
        OtherAppointments other = (OtherAppointments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Appointments[ id=" + id + " ]";
    }

    /**
     * @return the materialId
     */
    public Materials getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(Materials materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the appointmentsType
     */
    public Integer getAppointmentsType() {
        return appointmentsType;
    }

    /**
     * @param appointmentsType the appointmentsType to set
     */
    public void setAppointmentsType(Integer appointmentsType) {
        this.appointmentsType = appointmentsType;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
    }

}
