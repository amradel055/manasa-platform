/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author m lapan
 */
public class AppointmentsDTO extends BaseEntityDTO {

    private Integer appointmentsType;
    private Integer day;
    private String time;
    private Integer schoolStageId;
    private Integer teacherId;
    private Integer studentId;
    private MaterialsDTO materials;
    private Integer materialId;
    private String materialName;
    private String dayText;
    private String teacherName;
    private LessonLocationDTO lessonLocation;
    private String remarks;
    private Map<String, List<AppointmentsDTO>> map ; 

    public AppointmentsDTO() {
    }

    public AppointmentsDTO(Integer id) {
        this.id = id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
     * @return the materialId
     */
    public Integer getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
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
     * @return the materialName
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * @param materialName the materialName to set
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * @return the dayText
     */
    public String getDayText() {
        return dayText;
    }

    /**
     * @param dayText the dayText to set
     */
    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    /**
     * @return the teacherName
     */
    public String getTeacherName() {
        return teacherName;
    }

    /**
     * @param teacherName the teacherName to set
     */
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * @return the lessonLocation
     */
    public LessonLocationDTO getLessonLocation() {
        return lessonLocation;
    }

    /**
     * @param lessonLocation the lessonLocation to set
     */
    public void setLessonLocation(LessonLocationDTO lessonLocation) {
        this.lessonLocation = lessonLocation;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the map
     */
    public Map<String, List<AppointmentsDTO>> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(Map<String, List<AppointmentsDTO>> map) {
        this.map = map;
    }

}
