/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author hq004
 */
@Entity
@Table(name = "igma_user")
@NamedQueries({
    @NamedQuery(name = "IgmaUser.authenticate", query = "SELECT i FROM IgmaUser i WHERE i.userCode = :userCode")})
public class IgmaUser extends BaseEntity {

    
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private IgmaRole roleId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "user_code")
    private String userCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;

    @JoinColumn(name = "lang", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Symbol lang;
    
    @Column(name = "master")
    private Integer master;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;

    

    public IgmaUser() {
    }

    public IgmaUser(Integer id) {
        this.id = id;
    }

    public IgmaUser(Integer id, String userName, String password) {
        this.id = id;
        this.userCode = userName;
        this.password = password;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (!(object instanceof IgmaUser)) {
            return false;
        }
        IgmaUser other = (IgmaUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + userCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the lang
     */
    public Symbol getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(Symbol lang) {
        this.lang = lang;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Integer getMaster() {
        return master;
    }

    public void setMaster(Integer master) {
        this.master = master;
    }

    /**
     * @return the employeeId
     */
    public Employee getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public IgmaRole getRoleId() {
        return roleId;
    }

    public void setRoleId(IgmaRole roleId) {
        this.roleId = roleId;
    }
}
