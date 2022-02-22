/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author amr
 */
public class IgmaUserEmployeeDTO extends BaseEntityDTO {
    
    private IgmaUserDTO userId;

    public IgmaUserEmployeeDTO() {
    }

    public IgmaUserEmployeeDTO(Integer id) {
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
        if (!(object instanceof IgmaUserEmployeeDTO)) {
            return false;
        }
        IgmaUserEmployeeDTO other = (IgmaUserEmployeeDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.IsagUserInventory[ id=" + id + " ]";
    }

    /**
     * @return the userId
     */
    public IgmaUserDTO getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(IgmaUserDTO userId) {
        this.userId = userId;
    }
    
}
