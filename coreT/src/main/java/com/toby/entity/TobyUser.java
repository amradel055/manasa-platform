/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author hq004
 */
@Entity
@Table(name = "toby_user")
@NamedQueries({
    @NamedQuery(name = "TobyUser.authenticate", query = "SELECT i FROM TobyUser i WHERE i.userCode = :userCode")})
public class TobyUser extends BaseEntity {

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
    
    @Size(min = 1, max = 45)
    @Column(name = "residency_number")
    private String residencyNumber;
    
    

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @OneToMany(mappedBy = "createdBy")
    private Collection<TobyUserYear> tobyUserYearCollection;
    @OneToMany(mappedBy = "userId")
    private Collection<TobyUserYear> tobyUserYearCollection1;
    @OneToMany(mappedBy = "modifiedBy")
    private Collection<TobyUserYear> tobyUserYearCollection2;

    @Transient
    private BigDecimal sallary;

    public TobyUser() {
    }

    public TobyUser(Integer id) {
        this.id = id;
    }

    public TobyUser(Integer id, String userName, String password) {
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
        if (!(object instanceof TobyUser)) {
            return false;
        }
        TobyUser other = (TobyUser) object;
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
    public Collection<TobyUserYear> getTobyUserYearCollection() {
        return tobyUserYearCollection;
    }

    public void setTobyUserYearCollection(Collection<TobyUserYear> tobyUserYearCollection) {
        this.tobyUserYearCollection = tobyUserYearCollection;
    }

    @XmlTransient
    public Collection<TobyUserYear> getTobyUserYearCollection1() {
        return tobyUserYearCollection1;
    }

    public void setTobyUserYearCollection1(Collection<TobyUserYear> tobyUserYearCollection1) {
        this.tobyUserYearCollection1 = tobyUserYearCollection1;
    }

    @XmlTransient
    public Collection<TobyUserYear> getTobyUserYearCollection2() {
        return tobyUserYearCollection2;
    }

    public void setTobyUserYearCollection2(Collection<TobyUserYear> tobyUserYearCollection2) {
        this.tobyUserYearCollection2 = tobyUserYearCollection2;
    }

    public Integer getMaster() {
        return master;
    }

    public void setMaster(Integer master) {
        this.master = master;
    }

    /**
     * @return the sallary
     */
    public BigDecimal getSallary() {
        return sallary;
    }

    /**
     * @param sallary the sallary to set
     */
    public void setSallary(BigDecimal sallary) {
        this.sallary = sallary;
    }

    /**
     * @return the residencyNumber
     */
    public String getResidencyNumber() {
        return residencyNumber;
    }

    /**
     * @param residencyNumber the residencyNumber to set
     */
    public void setResidencyNumber(String residencyNumber) {
        this.residencyNumber = residencyNumber;
    }

   

}
