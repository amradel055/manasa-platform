/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author IT Shop
 */
public class RemRegisterEmployeeDTO extends BaseEntityDTO{

    
    private Date dateArrival;
    private Date date;
    private BigDecimal lag;
    private BigDecimal lat;
    private RemBusesDTO busId;
    private RemPointsDTO pointId;
    private RemWorkOrderDetailDTO remWorkOrderDetailId;
    private RemEmployeeShiftDTO shiftId;
    private RemTrafficDTO trafficId;

    public RemRegisterEmployeeDTO() {
    }

    public Date getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(Date dateArrival) {
        this.dateArrival = dateArrival;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getLag() {
        return lag;
    }

    public void setLag(BigDecimal lag) {
        this.lag = lag;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
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
        if (!(object instanceof RemRegisterEmployeeDTO)) {
            return false;
        }
        RemRegisterEmployeeDTO other = (RemRegisterEmployeeDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemRegisterEmployee[ id=" + id + " ]";
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
