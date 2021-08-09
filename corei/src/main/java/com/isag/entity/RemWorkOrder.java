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
@Table(name = "rem_work_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemWorkOrder.findAll", query = "SELECT r FROM RemWorkOrder r")
    , @NamedQuery(name = "RemWorkOrder.findById", query = "SELECT r FROM RemWorkOrder r WHERE r.id = :id")
    , @NamedQuery(name = "RemWorkOrder.findByCreationDate", query = "SELECT r FROM RemWorkOrder r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemWorkOrder.findByModificationDate", query = "SELECT r FROM RemWorkOrder r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RemWorkOrder.findBySerial", query = "SELECT r FROM RemWorkOrder r WHERE r.serial = :serial")
    , @NamedQuery(name = "RemWorkOrder.findByDate", query = "SELECT r FROM RemWorkOrder r WHERE r.date = :date")})
public class RemWorkOrder extends BaseEntity{

 
  
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "buses_id", referencedColumnName = "id")
    @ManyToOne
    private RemBuses busesId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    @ManyToOne
    private Employee driverId;
  

    public RemWorkOrder() {
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

   
    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RemBuses getBusesId() {
        return busesId;
    }

    public void setBusesId(RemBuses busesId) {
        this.busesId = busesId;
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
        if (!(object instanceof RemWorkOrder)) {
            return false;
        }
        RemWorkOrder other = (RemWorkOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemWorkOrder[ id=" + id + " ]";
    }

    /**
     * @return the driverId
     */
    public Employee getDriverId() {
        return driverId;
    }

    /**
     * @param driverId the driverId to set
     */
    public void setDriverId(Employee driverId) {
        this.driverId = driverId;
    }
    
}
