/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IT Shop
 */
@Entity
@Table(name = "rem_buses_follow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemBusesFollow.findAll", query = "SELECT r FROM RemBusesFollow r")
    , @NamedQuery(name = "RemBusesFollow.findById", query = "SELECT r FROM RemBusesFollow r WHERE r.id = :id")
    , @NamedQuery(name = "RemBusesFollow.findByDateArrival", query = "SELECT r FROM RemBusesFollow r WHERE r.dateArrival = :dateArrival")
    , @NamedQuery(name = "RemBusesFollow.findByDateMove", query = "SELECT r FROM RemBusesFollow r WHERE r.dateMove = :dateMove")
    , @NamedQuery(name = "RemBusesFollow.findByCreationDate", query = "SELECT r FROM RemBusesFollow r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemBusesFollow.findByModificationDate", query = "SELECT r FROM RemBusesFollow r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RemBusesFollow.findByWaitingTime", query = "SELECT r FROM RemBusesFollow r WHERE r.waitingTime = :waitingTime")})
public class RemBusesFollow extends BaseEntity {

    @Column(name = "date_arrival")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateArrival;
    @Column(name = "date_move")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateMove;
    @Column(name = "waiting_time")
    private Long waitingTime;
    @JoinColumn(name = "bus_id", referencedColumnName = "id")
    @ManyToOne
    private RemBuses busId;
    @JoinColumn(name = "point_id", referencedColumnName = "id")
    @ManyToOne
    private RemPoints pointId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "rem_work_order_detail_id", referencedColumnName = "id")
    @ManyToOne
    private RemWorkOrderDetail remWorkOrderDetailId;
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    @ManyToOne
    private RemEmployeeShift shiftId;
    @JoinColumn(name = "traffic_id", referencedColumnName = "id")
    @ManyToOne
    private RemTraffic trafficId;

    public RemBusesFollow() {
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

    public RemBuses getBusId() {
        return busId;
    }

    public void setBusId(RemBuses busId) {
        this.busId = busId;
    }

    public RemPoints getPointId() {
        return pointId;
    }

    public void setPointId(RemPoints pointId) {
        this.pointId = pointId;
    }

    public RemWorkOrderDetail getRemWorkOrderDetailId() {
        return remWorkOrderDetailId;
    }

    public void setRemWorkOrderDetailId(RemWorkOrderDetail remWorkOrderDetailId) {
        this.remWorkOrderDetailId = remWorkOrderDetailId;
    }

    public RemEmployeeShift getShiftId() {
        return shiftId;
    }

    public void setShiftId(RemEmployeeShift shiftId) {
        this.shiftId = shiftId;
    }

    public RemTraffic getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(RemTraffic trafficId) {
        this.trafficId = trafficId;
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
        if (!(object instanceof RemBusesFollow)) {
            return false;
        }
        RemBusesFollow other = (RemBusesFollow) object;
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
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }
    
}
