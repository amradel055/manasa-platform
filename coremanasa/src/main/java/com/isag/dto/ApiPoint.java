/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author AhmedEssam <ahmedEssam.com>
 */
public class ApiPoint {

    private String name;
    private String longitude;
    private String latitude;
    private Boolean checkArrival;
    private Integer buseCode;
    private Integer buseId;
    private Integer workOrderDetailId;
    private Integer pointId;
    private Integer type;
    private Integer trafficId;
    private Integer moveType;
    private Integer branchId;

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
     * @return the checkArrival
     */
    public Boolean getCheckArrival() {
        return checkArrival;
    }

    /**
     * @param checkArrival the checkArrival to set
     */
    public void setCheckArrival(Boolean checkArrival) {
        this.checkArrival = checkArrival;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the buseCode
     */
    public Integer getBuseCode() {
        return buseCode;
    }

    /**
     * @param buseCode the buseCode to set
     */
    public void setBuseCode(Integer buseCode) {
        this.buseCode = buseCode;
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
     * @return the buseId
     */
    public Integer getBuseId() {
        return buseId;
    }

    /**
     * @param buseId the buseId to set
     */
    public void setBuseId(Integer buseId) {
        this.buseId = buseId;
    }

    /**
     * @return the moveType
     */
    public Integer getMoveType() {
        return moveType;
    }

    /**
     * @param moveType the moveType to set
     */
    public void setMoveType(Integer moveType) {
        this.moveType = moveType;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

}
