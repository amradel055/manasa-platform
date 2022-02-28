/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "igma_user_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IgmaUserRole.findAll", query = "SELECT i FROM IgmaUserRole i")
    ,
    @NamedQuery(name = "IgmaUserRole.findById", query = "SELECT i FROM IgmaUserRole i WHERE i.id = :id")
    ,
    @NamedQuery(name = "IgmaUserRole.findByCreationDate", query = "SELECT i FROM IgmaUserRole i WHERE i.creationDate = :creationDate")
    ,
    @NamedQuery(name = "IgmaUserRole.findByModificationDate", query = "SELECT i FROM IgmaUserRole i WHERE i.modificationDate = :modificationDate")})
public class IgmaUserRole extends BaseEntity {

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaRole roleId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaUser userId;  
    
    
    @Column(name = "cost_avarage_update")
    private Boolean costAvarageUpdate;    
    @Column(name = "sell_price_update")
    private Boolean sellPriceUpdate;

    @Transient
    private Boolean markEdit;

    
    @Transient
    private List<Employee> employeeList;

    public IgmaUserRole() {
    }

    public IgmaUserRole(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IgmaRole getRoleId() {
        return roleId;
    }

    public void setRoleId(IgmaRole roleId) {
        this.roleId = roleId;
    }

    public IgmaUser getUserId() {
        return userId;
    }

    public void setUserId(IgmaUser userId) {
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
        if (!(object instanceof IgmaUserRole)) {
            return false;
        }
        IgmaUserRole other = (IgmaUserRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.IgmaUserRole[ id=" + id + " ]";
    }

    /**
     * @return the employeeList
     */
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * @param employeeList the employeeList to set
     */
    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    /**
     * @return the costAvarageUpdate
     */
    public Boolean getCostAvarageUpdate() {
        return costAvarageUpdate;
    }

    /**
     * @param costAvarageUpdate the costAvarageUpdate to set
     */
    public void setCostAvarageUpdate(Boolean costAvarageUpdate) {
        this.costAvarageUpdate = costAvarageUpdate;
    }

    /**
     * @return the sellPriceUpdate
     */
    public Boolean getSellPriceUpdate() {
        return sellPriceUpdate;
    }

    /**
     * @param sellPriceUpdate the sellPriceUpdate to set
     */
    public void setSellPriceUpdate(Boolean sellPriceUpdate) {
        this.sellPriceUpdate = sellPriceUpdate;
    }

   
   
}
