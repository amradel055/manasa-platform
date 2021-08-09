/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author IT Shop
 */
@Entity
@Table(name = "rem_employee_shift")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemEmployeeShift.findAll", query = "SELECT r FROM RemEmployeeShift r")
    , @NamedQuery(name = "RemEmployeeShift.findById", query = "SELECT r FROM RemEmployeeShift r WHERE r.id = :id")
    , @NamedQuery(name = "RemEmployeeShift.findByCode", query = "SELECT r FROM RemEmployeeShift r WHERE r.code = :code")
    , @NamedQuery(name = "RemEmployeeShift.findByName", query = "SELECT r FROM RemEmployeeShift r WHERE r.name = :name")
    , @NamedQuery(name = "RemEmployeeShift.findByCreationDate", query = "SELECT r FROM RemEmployeeShift r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemEmployeeShift.findByModificationDate", query = "SELECT r FROM RemEmployeeShift r WHERE r.modificationDate = :modificationDate")})
public class RemEmployeeShift extends BaseEntity{

    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "remEmployeeShiftId")
    private Collection<RemEmployeeShiftDetail> remEmployeeShiftDetailCollection;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "rem_buses_id", referencedColumnName = "id")
    @ManyToOne
    private RemBuses remBusesId;
    @JoinColumn(name = "rem_company_id", referencedColumnName = "id")
    @ManyToOne
    private RemCompany remCompanyId;
    @JoinColumn(name = "rem_traffic_id", referencedColumnName = "id")
    @ManyToOne
    private RemTraffic remTrafficId;

    public RemEmployeeShift() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<RemEmployeeShiftDetail> getRemEmployeeShiftDetailCollection() {
        return remEmployeeShiftDetailCollection;
    }

    public void setRemEmployeeShiftDetailCollection(Collection<RemEmployeeShiftDetail> remEmployeeShiftDetailCollection) {
        this.remEmployeeShiftDetailCollection = remEmployeeShiftDetailCollection;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public RemBuses getRemBusesId() {
        return remBusesId;
    }

    public void setRemBusesId(RemBuses remBusesId) {
        this.remBusesId = remBusesId;
    }

    public RemCompany getRemCompanyId() {
        return remCompanyId;
    }

    public void setRemCompanyId(RemCompany remCompanyId) {
        this.remCompanyId = remCompanyId;
    }

    public RemTraffic getRemTrafficId() {
        return remTrafficId;
    }

    public void setRemTrafficId(RemTraffic remTrafficId) {
        this.remTrafficId = remTrafficId;
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
        if (!(object instanceof RemEmployeeShift)) {
            return false;
        }
        RemEmployeeShift other = (RemEmployeeShift) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }
    
}
