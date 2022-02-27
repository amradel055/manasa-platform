/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author User
 */
public class ProFactoryDeliveryDetailDTO extends BaseEntityDTO {

    private BigDecimal number;
    private Integer clothNumber;
    private Integer proFactoryDeliveryId;
    private InvItemDTO itemId;
    private InvItemDTO invItemParentId;

    public ProFactoryDeliveryDetailDTO() {
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public Integer getClothNumber() {
        return clothNumber;
    }

    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
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
        if (!(object instanceof ProFactoryDeliveryDetailDTO)) {
            return false;
        }
        ProFactoryDeliveryDetailDTO other = (ProFactoryDeliveryDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ccc.ProFactoryDeliveryDetail[ id=" + id + " ]";
    }

    /**
     * @return the proFactoryDeliveryId
     */
    public Integer getProFactoryDeliveryId() {
        return proFactoryDeliveryId;
    }

    /**
     * @param proFactoryDeliveryId the proFactoryDeliveryId to set
     */
    public void setProFactoryDeliveryId(Integer proFactoryDeliveryId) {
        this.proFactoryDeliveryId = proFactoryDeliveryId;
    }

    /**
     * @return the itemId
     */
    public InvItemDTO getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(InvItemDTO itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the invItemParentId
     */
    public InvItemDTO getInvItemParentId() {
        return invItemParentId;
    }

    /**
     * @param invItemParentId the invItemParentId to set
     */
    public void setInvItemParentId(InvItemDTO invItemParentId) {
        this.invItemParentId = invItemParentId;
    }
    
}
