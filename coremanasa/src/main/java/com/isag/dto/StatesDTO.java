/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author ahmed
 */

public class StatesDTO extends BaseEntityDTO{

    
   
    private String name;
    private GovernoratesDTO governorateId;
   

    public StatesDTO() {
    }

    public StatesDTO(Integer id) {
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
        if (!(object instanceof StatesDTO)) {
            return false;
        }
        StatesDTO other = (StatesDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.States[ id=" + id + " ]";
    }

    /**
     * @return the governorateId
     */
    public GovernoratesDTO getGovernorateId() {
        return governorateId;
    }

    /**
     * @param governorateId the governorateId to set
     */
    public void setGovernorateId(GovernoratesDTO governorateId) {
        this.governorateId = governorateId;
    }
    
}
