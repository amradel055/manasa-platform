/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.*;

public class TobyUserInventoryDTO extends BaseEntityDTO {
    
    private InvInventoryDTO invInventoryId;
    private TobyUser userId;

    public TobyUserInventoryDTO() {
    }

    public TobyUserInventoryDTO(Integer id) {
        this.id = id;
    }

   

    public InvInventoryDTO getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventoryDTO invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public TobyUser getUserId() {
        return userId;
    }

    public void setUserId(TobyUser userId) {
        this.userId = userId;
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
        if (!(object instanceof TobyUserInventoryDTO)) {
            return false;
        }
        TobyUserInventoryDTO other = (TobyUserInventoryDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.TobyUserInventory[ id=" + id + " ]";
    }
    
}
