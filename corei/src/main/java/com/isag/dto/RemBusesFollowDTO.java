/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author IT Shop
 */
public class RemBusesFollowDTO extends BaseEntityDTO {

    private Date dateArrival;
    private Date dateMove;
    private Long waitingTime;
    private RemBusesDTO busId;
    private RemPointsDTO pointId;
    private RemWorkOrderDetailDTO remWorkOrderDetailId;
    private RemEmployeeShiftDTO shiftId;
    private RemTrafficDTO trafficId;

    public RemBusesFollowDTO() {
    }

    public Date getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(Date dateArrival) {
        this.dateArrival = dateArrival;
    }

    public Date getDateMove() {
        return dateMove;
    }

    public void setDateMove(Date dateMove) {
        this.dateMove = dateMove;
    }

    public Long getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Long waitingTime) {
        this.waitingTime = waitingTime;
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
        if (!(object instanceof RemBusesFollowDTO)) {
            return false;
        }
        RemBusesFollowDTO other = (RemBusesFollowDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemBusesFollow[ id=" + id + " ]";
    }

    /**
     * @return the busId
     */
    public RemBusesDTO getBusId() {
        return busId;
    }

    /**
     * @param busId the busId to set
     */
    public void setBusId(RemBusesDTO busId) {
        this.busId = busId;
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
     * @return the remWorkOrderDetailId
     */
    public RemWorkOrderDetailDTO getRemWorkOrderDetailId() {
        return remWorkOrderDetailId;
    }

    /**
     * @param remWorkOrderDetailId the remWorkOrderDetailId to set
     */
    public void setRemWorkOrderDetailId(RemWorkOrderDetailDTO remWorkOrderDetailId) {
        this.remWorkOrderDetailId = remWorkOrderDetailId;
    }

    /**
     * @return the shiftId
     */
    public RemEmployeeShiftDTO getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(RemEmployeeShiftDTO shiftId) {
        this.shiftId = shiftId;
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
    
}
