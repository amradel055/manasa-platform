/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ahmed Shaaban
 */
@Entity
@Table(name = "hr_emergency_contact")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrEmergencyContact.findAll", query = "SELECT h FROM HrEmergencyContact h"),
    @NamedQuery(name = "HrEmergencyContact.findById", query = "SELECT h FROM HrEmergencyContact h WHERE h.id = :id"),
    @NamedQuery(name = "HrEmergencyContact.findByName", query = "SELECT h FROM HrEmergencyContact h WHERE h.name = :name"),
    @NamedQuery(name = "HrEmergencyContact.findByMobile", query = "SELECT h FROM HrEmergencyContact h WHERE h.mobile = :mobile"),
    @NamedQuery(name = "HrEmergencyContact.findByRelation", query = "SELECT h FROM HrEmergencyContact h WHERE h.relation = :relation"),
    @NamedQuery(name = "HrEmergencyContact.findByCurrentAddress", query = "SELECT h FROM HrEmergencyContact h WHERE h.currentAddress = :currentAddress"),
    @NamedQuery(name = "HrEmergencyContact.findByPermenantAddress", query = "SELECT h FROM HrEmergencyContact h WHERE h.permenantAddress = :permenantAddress"),
    @NamedQuery(name = "HrEmergencyContact.findByCreationDate", query = "SELECT h FROM HrEmergencyContact h WHERE h.creationDate = :creationDate"),
    @NamedQuery(name = "HrEmergencyContact.findByModificationDate", query = "SELECT h FROM HrEmergencyContact h WHERE h.modificationDate = :modificationDate")})
public class HrEmergencyContact extends BaseEntity{

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "mobile")
    private Integer mobile;
    @Size(max = 45)
    @Column(name = "relation")
    private String relation;
    @Size(max = 45)
    @Column(name = "current_address")
    private String currentAddress;
    @Size(max = 45)
    @Column(name = "permenant_address")
    private String permenantAddress;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;

    public HrEmergencyContact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermenantAddress() {
        return permenantAddress;
    }

    public void setPermenantAddress(String permenantAddress) {
        this.permenantAddress = permenantAddress;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }
    public Employee getEmployeeId() {
        if (employeeId == null){
        employeeId = new Employee();
        }
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
        if (!(object instanceof HrEmergencyContact)) {
            return false;
        }
        HrEmergencyContact other = (HrEmergencyContact) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.HrEmergencyContact[ id=" + id + " ]";
    }
    
}
