/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author IT Shop
 */
@Entity
@Table(name = "rem_work_order_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemWorkOrderDetail.findAll", query = "SELECT r FROM RemWorkOrderDetail r")
    , @NamedQuery(name = "RemWorkOrderDetail.findById", query = "SELECT r FROM RemWorkOrderDetail r WHERE r.id = :id")
    , @NamedQuery(name = "RemWorkOrderDetail.findByCreationDate", query = "SELECT r FROM RemWorkOrderDetail r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemWorkOrderDetail.findByModificationDate", query = "SELECT r FROM RemWorkOrderDetail r WHERE r.modificationDate = :modificationDate")})
public class RemWorkOrderDetail extends BaseEntity {

    @JoinColumn(name = "traffic_id", referencedColumnName = "id")
    @ManyToOne
    private RemTraffic trafficId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "rem_work_order", referencedColumnName = "id")
    @ManyToOne
    private RemWorkOrder remWorkOrder;

    public RemWorkOrderDetail() {
    }

    public RemWorkOrderDetail(Integer id) {
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
        if (!(object instanceof RemWorkOrderDetail)) {
            return false;
        }
        RemWorkOrderDetail other = (RemWorkOrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemWorkOrderDetail[ id=" + id + " ]";
    }

    /**
     * @return the trafficId
     */
    public RemTraffic getTrafficId() {
        return trafficId;
    }

    /**
     * @param trafficId the trafficId to set
     */
    public void setTrafficId(RemTraffic trafficId) {
        this.trafficId = trafficId;
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

    /**
     * @return the remWorkOrder
     */
    public RemWorkOrder getRemWorkOrder() {
        return remWorkOrder;
    }

    /**
     * @param remWorkOrder the remWorkOrder to set
     */
    public void setRemWorkOrder(RemWorkOrder remWorkOrder) {
        this.remWorkOrder = remWorkOrder;
    }

}
