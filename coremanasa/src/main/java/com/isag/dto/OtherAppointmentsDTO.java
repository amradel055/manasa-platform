/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author m lapan
 */
public class OtherAppointmentsDTO extends BaseEntityDTO {

    private Integer appointmentsType;
    private String img;
    private SchoolStagesGuideDTO schoolStage;
    private Integer schoolStageId;
    private IgmaUserDTO teacher;
    private MaterialsDTO material;
    private Integer teacherId;
    private Integer materialId;
    private String materialName;
    private Integer otherAppointmentsType;

    public OtherAppointmentsDTO() {
    }

    public OtherAppointmentsDTO(Integer id) {
        this.id = id;
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
        if (!(object instanceof OtherAppointmentsDTO)) {
            return false;
        }
        OtherAppointmentsDTO other = (OtherAppointmentsDTO) object;
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

    /**
     * @return the teacher
     */
    public IgmaUserDTO getTeacher() {
        return teacher;
    }

    /**
     * @param teacher the teacher to set
     */
    public void setTeacher(IgmaUserDTO teacher) {
        this.teacher = teacher;
    }

    /**
     * @return the material
     */
    public MaterialsDTO getMaterial() {
        return material;
    }

    /**
     * @param material the material to set
     */
    public void setMaterial(MaterialsDTO material) {
        this.material = material;
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
     * @return the schoolStage
     */
    public SchoolStagesGuideDTO getSchoolStage() {
        return schoolStage;
    }

    /**
     * @param schoolStage the schoolStage to set
     */
    public void setSchoolStage(SchoolStagesGuideDTO schoolStage) {
        this.schoolStage = schoolStage;
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

}
