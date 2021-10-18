/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.List;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
public class APIWorkOrderDTO {

    private Integer busId;
    private String busName;
    private Integer busCode;
    private String trafficName;
    private Integer trafficId;
    private Integer workOrderDetailId;
    private List<ApiPoint> points;
    private Integer type;

    /**
     * @return the busName
     */
    public String getBusName() {
        return busName;
    }

    /**
     * @param busName the busName to set
     */
    public void setBusName(String busName) {
        this.busName = busName;
    }

    /**
     * @return the busCode
     */
    public Integer getBusCode() {
        return busCode;
    }

    /**
     * @param busCode the busCode to set
     */
    public void setBusCode(Integer busCode) {
        this.busCode = busCode;
    }

    /**
     * @return the points
     */
    public List<ApiPoint> getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(List<ApiPoint> points) {
        this.points = points;
    }

    /**
     * @return the trafficName
     */
    public String getTrafficName() {
        return trafficName;
    }

    /**
     * @param trafficName the trafficName to set
     */
    public void setTrafficName(String trafficName) {
        this.trafficName = trafficName;
    }

    /**
     * @return the workOrderDetailId
     */
    public Integer getWorkOrderDetailId() {
        return workOrderDetailId;
    }

    /**
     * @param workOrderDetailId the workOrderDetailId to set
     */
    public void setWorkOrderDetailId(Integer workOrderDetailId) {
        this.workOrderDetailId = workOrderDetailId;
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

}
