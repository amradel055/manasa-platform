/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.List;

/**
 *
 * @author ahmed
 */
public class APIDataDTO {

    private String name;
    private Integer branchSelected;
    private List<APIWorkOrderDTO> workOrderList;
    private Integer pointId;
    private Integer employeeId;
    private Integer shiftId;
    private Integer trafficId;
    private Integer userId;
   
    

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
     * @return the branchSelected
     */
    public Integer getBranchSelected() {
        return branchSelected;
    }

    /**
     * @param branchSelected the branchSelected to set
     */
    public void setBranchSelected(Integer branchSelected) {
        this.branchSelected = branchSelected;
    }

    /**
     * @return the workOrderList
     */
    public List<APIWorkOrderDTO> getWorkOrderList() {
        return workOrderList;
    }

    /**
     * @param workOrderList the workOrderList to set
     */
    public void setWorkOrderList(List<APIWorkOrderDTO> workOrderList) {
        this.workOrderList = workOrderList;
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
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    
}
