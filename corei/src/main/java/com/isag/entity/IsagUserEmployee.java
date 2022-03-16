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
 * @author amr
 */
@Entity
@Table(name = "isag_user_employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsagUserInventory.findAll", query = "SELECT i FROM IsagUserInventory i")
    , @NamedQuery(name = "IsagUserInventory.findById", query = "SELECT i FROM IsagUserInventory i WHERE i.id = :id")
    , @NamedQuery(name = "IsagUserInventory.findByCreationDate", query = "SELECT i FROM IsagUserInventory i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "IsagUserInventory.findByModificationDate", query = "SELECT i FROM IsagUserInventory i WHERE i.modificationDate = :modificationDate")})
public class IsagUserEmployee extends BaseEntity {
    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employee;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private IsagUser userId;

    public IsagUserEmployee() {
    }

    public IsagUserEmployee(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IsagUser getUserId() {
        return userId;
    }

    public void setUserId(IsagUser userId) {
        this.userId = userId;
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
        if (!(object instanceof IsagUserEmployee)) {
            return false;
        }
        IsagUserEmployee other = (IsagUserEmployee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.IsagUserInventory[ id=" + id + " ]";
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
}
