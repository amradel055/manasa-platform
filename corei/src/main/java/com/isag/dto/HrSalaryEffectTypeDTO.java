/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class HrSalaryEffectTypeDTO extends BaseEntityDTO{
     
    
    private String name; 
    private Integer type; 
    private Integer effectType;  
    private Integer serial;  
    private String modificationDate; 
    private Integer modifiedBy;
    //not in Database
    private String typeText; 
    private String effectTypeText;  

    
 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getEffectTypeText() {
        return effectTypeText;
    }

    public void setEffectTypeText(String effectTypeText) {
        this.effectTypeText = effectTypeText;
    }
 
    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    } 
    
    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
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
        if (!(object instanceof HrSalaryEffectTypeDTO)) {
            return false;
        }
        HrSalaryEffectTypeDTO other = (HrSalaryEffectTypeDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return   name + " " + serial;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the effectType
     */
    public Integer getEffectType() {
        return effectType;
    }

    /**
     * @param effectType the effectType to set
     */
    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }
    
}
