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
public class RemTrafficDetailDTO extends BaseEntityDTO{

    private RemPointsDTO pointId;
    private RemPointsDTO pointIdBakUp;
    private Integer remTrafficId;

    public RemTrafficDetailDTO() {
    }

    public RemTrafficDetailDTO(Integer id) {
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
        if (!(object instanceof RemTrafficDetailDTO)) {
            return false;
        }
        RemTrafficDetailDTO other = (RemTrafficDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemTrafficDetail[ id=" + id + " ]";
    }
    

    /**
     * @return the remTrafficId
     */
    public Integer getRemTrafficId() {
        return remTrafficId;
    }

    /**
     * @param remTrafficId the remTrafficId to set
     */
    public void setRemTrafficId(Integer remTrafficId) {
        this.remTrafficId = remTrafficId;
    }

    /**
     * @return the pointId
     */
    public RemPointsDTO getPointId() {
        return pointId;
    }

    /**
     * @param pointId the pointId to set
     */
    public void setPointId(RemPointsDTO pointId) {
        this.pointId = pointId;
    }

    /**
     * @return the pointIdBakUp
     */
    public RemPointsDTO getPointIdBakUp() {
        return pointIdBakUp;
    }

    /**
     * @param pointIdBakUp the pointIdBakUp to set
     */
    public void setPointIdBakUp(RemPointsDTO pointIdBakUp) {
        this.pointIdBakUp = pointIdBakUp;
    }
    
}
