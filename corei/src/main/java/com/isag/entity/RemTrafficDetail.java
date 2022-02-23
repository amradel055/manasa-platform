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
@Table(name = "rem_traffic_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemTrafficDetail.findAll", query = "SELECT r FROM RemTrafficDetail r")
    , @NamedQuery(name = "RemTrafficDetail.findById", query = "SELECT r FROM RemTrafficDetail r WHERE r.id = :id")
    , @NamedQuery(name = "RemTrafficDetail.findByCreationDate", query = "SELECT r FROM RemTrafficDetail r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemTrafficDetail.findByModificationDate", query = "SELECT r FROM RemTrafficDetail r WHERE r.modificationDate = :modificationDate")})
public class RemTrafficDetail extends BaseEntity{

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "point_id", referencedColumnName = "id")
    @ManyToOne
    private RemPoints pointId;
    @JoinColumn(name = "rem_traffic_id", referencedColumnName = "id")
    @ManyToOne
    private RemTraffic remTrafficId;

    public RemTrafficDetail() {
    }

    public RemTrafficDetail(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }
    
    public RemPoints getPointId() {
        return pointId;
    }

    public void setPointId(RemPoints pointId) {
        this.pointId = pointId;
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
        if (!(object instanceof RemTrafficDetail)) {
            return false;
        }
        RemTrafficDetail other = (RemTrafficDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemTrafficDetail[ id=" + id + " ]";
    }

    /**
     * @return the remTrafficId
     */
    public RemTraffic getRemTrafficId() {
        return remTrafficId;
    }

    /**
     * @param remTrafficId the remTrafficId to set
     */
    public void setRemTrafficId(RemTraffic remTrafficId) {
        this.remTrafficId = remTrafficId;
    }
    
}
