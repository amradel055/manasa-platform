/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author hq004
 */
@Entity
@Table(name = "isag_user")
@NamedQueries({
    @NamedQuery(name = "IsagUser.authenticate", query = "SELECT i FROM IsagUser i WHERE i.userCode = :userCode")})
public class IsagUser extends BaseEntity {

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
    @Size(min = 1, max = 100)
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

    @OneToMany(mappedBy = "createdBy")
    private Collection<IsagUserYear> isagUserYearCollection;
    @OneToMany(mappedBy = "userId")
    private Collection<IsagUserYear> isagUserYearCollection1;
    @OneToMany(mappedBy = "modifiedBy")
    private Collection<IsagUserYear> isagUserYearCollection2;

    

    public IsagUser() {
    }

    public IsagUser(Integer id) {
        this.id = id;
    }

    public IsagUser(Integer id, String userName, String password) {
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
        if (!(object instanceof IsagUser)) {
            return false;
        }
        IsagUser other = (IsagUser) object;
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

    @XmlTransient
    public Collection<IsagUserYear> getIsagUserYearCollection() {
        return isagUserYearCollection;
    }

    public void setIsagUserYearCollection(Collection<IsagUserYear> isagUserYearCollection) {
        this.isagUserYearCollection = isagUserYearCollection;
    }

    @XmlTransient
    public Collection<IsagUserYear> getIsagUserYearCollection1() {
        return isagUserYearCollection1;
    }

    public void setIsagUserYearCollection1(Collection<IsagUserYear> isagUserYearCollection1) {
        this.isagUserYearCollection1 = isagUserYearCollection1;
    }

    @XmlTransient
    public Collection<IsagUserYear> getIsagUserYearCollection2() {
        return isagUserYearCollection2;
    }

    public void setIsagUserYearCollection2(Collection<IsagUserYear> isagUserYearCollection2) {
        this.isagUserYearCollection2 = isagUserYearCollection2;
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


}
