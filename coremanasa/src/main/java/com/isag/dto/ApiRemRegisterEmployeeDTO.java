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
public class ApiRemRegisterEmployeeDTO {

    
    private Date dateArrival;
    private Date date;
    private Integer busId;
    private Integer pointId;
    private Integer remWorkOrderDetailId;
    private Integer shiftId;
    private Integer trafficId;
    private Integer employeeDTO;
    private Integer type;
    private Integer registrationType;

    public ApiRemRegisterEmployeeDTO() {
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

    /**
     * @return the busId
     */
    public Integer getBusId() {
        return busId;
    }

    /**
     * @param busId the busId to set
     */
    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    /**
     * @return the pointId
     */
    public Integer getPointId() {
        return pointId;
    }

    /**
     * @param pointId the pointId to set
     */
    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }

    /**
     * @return the remWorkOrderDetailId
     */
    public Integer getRemWorkOrderDetailId() {
        return remWorkOrderDetailId;
    }

    /**
     * @param remWorkOrderDetailId the remWorkOrderDetailId to set
     */
    public void setRemWorkOrderDetailId(Integer remWorkOrderDetailId) {
        this.remWorkOrderDetailId = remWorkOrderDetailId;
    }

    /**
     * @return the shiftId
     */
    public Integer getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the trafficId
     */
    public Integer getTrafficId() {
        return trafficId;
    }

    /**
     * @param trafficId the trafficId to set
     */
    public void setTrafficId(Integer trafficId) {
        this.trafficId = trafficId;
    }

    /**
     * @return the employeeDTO
     */
    public Integer getEmployeeDTO() {
        return employeeDTO;
    }

    /**
     * @param employeeDTO the employeeDTO to set
     */
    public void setEmployeeDTO(Integer employeeDTO) {
        this.employeeDTO = employeeDTO;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the registrationType
     */
    public Integer getRegistrationType() {
        return registrationType;
    }

    /**
     * @param registrationType the registrationType to set
     */
    public void setRegistrationType(Integer registrationType) {
        this.registrationType = registrationType;
    }
    
}
