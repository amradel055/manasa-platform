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
@Table(name = "rem_employee_shift_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemEmployeeShiftDetail.findAll", query = "SELECT r FROM RemEmployeeShiftDetail r")
    , @NamedQuery(name = "RemEmployeeShiftDetail.findById", query = "SELECT r FROM RemEmployeeShiftDetail r WHERE r.id = :id")
    , @NamedQuery(name = "RemEmployeeShiftDetail.findByCreationDate", query = "SELECT r FROM RemEmployeeShiftDetail r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemEmployeeShiftDetail.findByModificationDate", query = "SELECT r FROM RemEmployeeShiftDetail r WHERE r.modificationDate = :modificationDate")})
public class RemEmployeeShiftDetail extends BaseEntity{

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;
    @JoinColumn(name = "rem_employee_shift_id", referencedColumnName = "id")
    @ManyToOne
    private RemEmployeeShift remEmployeeShiftId;

    public RemEmployeeShiftDetail() {
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public RemEmployeeShift getRemEmployeeShiftId() {
        return remEmployeeShiftId;
    }

    public void setRemEmployeeShiftId(RemEmployeeShift remEmployeeShiftId) {
        this.remEmployeeShiftId = remEmployeeShiftId;
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
        if (!(object instanceof RemEmployeeShiftDetail)) {
            return false;
        }
        RemEmployeeShiftDetail other = (RemEmployeeShiftDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemEmployeeShiftDetail[ id=" + id + " ]";
    }
    
}
