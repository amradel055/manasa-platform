/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author IT Shop
 */

public class RemWorkOrderDetailDTO extends BaseEntityDTO {

   
    private RemTrafficDTO trafficId;
    private RemTrafficDTO trafficIdBak;
    private Integer remWorkOrder;

    public RemWorkOrderDetailDTO() {
    }

    public RemWorkOrderDetailDTO(Integer id) {
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
        if (!(object instanceof RemWorkOrderDetailDTO)) {
            return false;
        }
        RemWorkOrderDetailDTO other = (RemWorkOrderDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemWorkOrderDetail[ id=" + id + " ]";
    }

    /**
     * @return the trafficId
     */
    public RemTrafficDTO getTrafficId() {
        return trafficId;
    }

    /**
     * @param trafficId the trafficId to set
     */
    public void setTrafficId(RemTrafficDTO trafficId) {
        this.trafficId = trafficId;
    }

    /**
     * @return the remWorkOrder
     */
    public Integer getRemWorkOrder() {
        return remWorkOrder;
    }

    /**
     * @param remWorkOrder the remWorkOrder to set
     */
    public void setRemWorkOrder(Integer remWorkOrder) {
        this.remWorkOrder = remWorkOrder;
    }

    /**
     * @return the trafficIdBak
     */
    public RemTrafficDTO getTrafficIdBak() {
        return trafficIdBak;
    }

    /**
     * @param trafficIdBak the trafficIdBak to set
     */
    public void setTrafficIdBak(RemTrafficDTO trafficIdBak) {
        this.trafficIdBak = trafficIdBak;
    }

}
