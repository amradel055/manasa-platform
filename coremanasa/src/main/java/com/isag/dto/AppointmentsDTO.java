/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author m lapan
 */
public class AppointmentsDTO extends BaseEntityDTO {

    private String day;
    private Date time;
    private Integer schoolStageId;
    private Integer teacherId;
    private Integer studentId;
    private MaterialsDTO materials;
    private Integer materialsId;

    public AppointmentsDTO() {
    }

    public AppointmentsDTO(Integer id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
        if (!(object instanceof AppointmentsDTO)) {
            return false;
        }
        AppointmentsDTO other = (AppointmentsDTO) object;
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
     * @return the schoolStageId
     */
    public Integer getSchoolStageId() {
        return schoolStageId;
    }

    /**
     * @param schoolStageId the schoolStageId to set
     */
    public void setSchoolStageId(Integer schoolStageId) {
        this.schoolStageId = schoolStageId;
    }

    /**
     * @return the teacherId
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return the materials
     */
    public MaterialsDTO getMaterials() {
        return materials;
    }

    /**
     * @param materials the materials to set
     */
    public void setMaterials(MaterialsDTO materials) {
        this.materials = materials;
    }

    /**
     * @return the materialsId
     */
    public Integer getMaterialsId() {
        return materialsId;
    }

    /**
     * @param materialsId the materialsId to set
     */
    public void setMaterialsId(Integer materialsId) {
        this.materialsId = materialsId;
    }

    /**
     * @return the studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

}
