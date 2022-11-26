/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

public class PeriodsDTO extends BaseEntityDTO {

    private String name;
    private Integer teacherId;
    private SchoolStagesGuideDTO schoolStagesGuideDTO;
    private Integer materialId;
    private String materialName;
    private Integer parent;
    private String parentName;

    public PeriodsDTO() {
    }

    public PeriodsDTO(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof PeriodsDTO)) {
            return false;
        }
        PeriodsDTO other = (PeriodsDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "com.mycompany.mavenproject1.Periods[ id=" + id + " ]";
//    }
    
     @Override
    public String toString() {
        return teacherId + " " + materialId + " " + parent + " " ;
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
     * @return the parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Integer parent) {
        this.parent = parent;
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
     * @return the parentName
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * @param parentName the parentName to set
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * @return the schoolStagesGuideDTO
     */
    public SchoolStagesGuideDTO getSchoolStagesGuideDTO() {
        return schoolStagesGuideDTO;
    }

    /**
     * @param schoolStagesGuideDTO the schoolStagesGuideDTO to set
     */
    public void setSchoolStagesGuideDTO(SchoolStagesGuideDTO schoolStagesGuideDTO) {
        this.schoolStagesGuideDTO = schoolStagesGuideDTO;
    }

  

}
