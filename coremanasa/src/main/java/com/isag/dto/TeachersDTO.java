/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.List;

/**
 *
 * @author m lapan
 */
public class TeachersDTO extends BaseEntityDTO {

    private String name;
    private Integer serial;
    private String description;
    private String image;
    private List<MaterialsDTO> materialList;

    public TeachersDTO() {
    }

    public TeachersDTO(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        if (!(object instanceof TeachersDTO)) {
            return false;
        }
        TeachersDTO other = (TeachersDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.Teachers[ id=" + id + " ]";
    }

    /**
     * @return the materialList
     */
    public List<MaterialsDTO> getMaterialList() {
        return materialList;
    }

    /**
     * @param materialList the materialList to set
     */
    public void setMaterialList(List<MaterialsDTO> materialList) {
        this.materialList = materialList;
    }

}
