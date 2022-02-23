/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;


/**
 *
 * @author AhmedEssam
 */
public class GlAdminUnitDTO extends BaseEntityDTO {

    private Integer code;
    private String name;
    private Integer shortCode;
    private Integer level;
    private boolean active;
    private Integer branchId;
    private GlAdminUnitDTO parent;
    

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShortCode() {
        return shortCode;
    }

    public void setShortCode(Integer shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public GlAdminUnitDTO getParent() {
        return parent;
    }

    public void setParent(GlAdminUnitDTO parent) {
        this.parent = parent;
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
        if (!(object instanceof GlAdminUnitDTO)) {
            return false;
        }
        GlAdminUnitDTO other = (GlAdminUnitDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    public String getAdminUnit() {
        return code + " " + name;
    }
    
    @Override
    public String toString() {
       return code + " " + name;
    }

   
}
