/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
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
@Table(name = "rem_register_employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemRegisterEmployee.findAll", query = "SELECT r FROM RemRegisterEmployee r")
    , @NamedQuery(name = "RemRegisterEmployee.findById", query = "SELECT r FROM RemRegisterEmployee r WHERE r.id = :id")
    , @NamedQuery(name = "RemRegisterEmployee.findByDateArrival", query = "SELECT r FROM RemRegisterEmployee r WHERE r.dateArrival = :dateArrival")
    , @NamedQuery(name = "RemRegisterEmployee.findByDate", query = "SELECT r FROM RemRegisterEmployee r WHERE r.date = :date")
    , @NamedQuery(name = "RemRegisterEmployee.findByCreationDate", query = "SELECT r FROM RemRegisterEmployee r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemRegisterEmployee.findByModificationDate", query = "SELECT r FROM RemRegisterEmployee r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RemRegisterEmployee.findByLag", query = "SELECT r FROM RemRegisterEmployee r WHERE r.lag = :lag")
    , @NamedQuery(name = "RemRegisterEmployee.findByLat", query = "SELECT r FROM RemRegisterEmployee r WHERE r.lat = :lat")})
public class RemRegisterEmployee extends BaseEntity{

    
    @Column(name = "date_arrival")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateArrival;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "lag")
    private BigDecimal lag;
    @Column(name = "lat")
    private BigDecimal lat;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "bus_id", referencedColumnName = "id")
    @ManyToOne
    private RemBuses busId;
    @JoinColumn(name = "point_id", referencedColumnName = "id")
    @ManyToOne
    private RemPoints pointId;
    @JoinColumn(name = "rem_work_order_detail_id", referencedColumnName = "id")
    @ManyToOne
    private RemWorkOrderDetail remWorkOrderDetailId;
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    @ManyToOne
    private RemEmployeeShift shiftId;
    @JoinColumn(name = "traffic_id", referencedColumnName = "id")
    @ManyToOne
    private RemTraffic trafficId;

    public RemRegisterEmployee() {
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof RemRegisterEmployee)) {
            return false;
        }
        RemRegisterEmployee other = (RemRegisterEmployee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemRegisterEmployee[ id=" + id + " ]";
    }
    
}
