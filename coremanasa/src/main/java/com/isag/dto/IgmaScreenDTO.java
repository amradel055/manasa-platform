/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;


/**
 *
 * @author hq004
 */

public class IgmaScreenDTO extends BaseEntityDTO{
    
   
    private Integer serial;
    
    private String name;
   
    private String namear;
   
    private String tabAr;
   
    private String tabEn;
   
    private String moduleEn;
   
    private String moduleAr;

   
    private Integer moduleType;
    
    
     
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the namear
     */
    public String getNamear() {
        return namear;
    }

    /**
     * @param namear the namear to set
     */
    public void setNamear(String namear) {
        this.namear = namear;
    }
    
    public String getTabAr() {
        return tabAr;
    }

    public void setTabAr(String tabAr) {
        this.tabAr = tabAr;
    }

    public String getTabEn() {
        return tabEn;
    }

    public void setTabEn(String tabEn) {
        this.tabEn = tabEn;
    }

    public String getModuleEn() {
        return moduleEn;
    }

    public void setModuleEn(String moduleEn) {
        this.moduleEn = moduleEn;
    }

    public String getModuleAr() {
        return moduleAr;
    }

    public void setModuleAr(String moduleAr) {
        this.moduleAr = moduleAr;
    }
    
    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the moduleType
     */
    public Integer getModuleType() {
        return moduleType;
    }

    /**
     * @param moduleType the moduleType to set
     */
    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
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
        if (!(object instanceof IgmaScreenDTO)) {
            return false;
        }
        IgmaScreenDTO other = (IgmaScreenDTO) object;
        if ((this.id == null && other.getId() != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdfsdfs.CrmComments[ id=" + id + " ]";
    }
    
}
