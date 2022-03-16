/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
@Table(name = "toby_user_year")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TobyUserYear.findAll", query = "SELECT i FROM TobyUserYear i")
    , @NamedQuery(name = "TobyUserYear.findById", query = "SELECT i FROM TobyUserYear i WHERE i.id = :id")
    , @NamedQuery(name = "TobyUserYear.findByCreationDate", query = "SELECT i FROM TobyUserYear i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "TobyUserYear.findByModificationDate", query = "SELECT i FROM TobyUserYear i WHERE i.modificationDate = :modificationDate")})
public class TobyUserYear extends BaseEntity {
    
    @JoinColumn(name = "year_id", referencedColumnName = "id")
    @ManyToOne
    private GlYear yearId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private TobyUser userId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public TobyUserYear() {
    }

    public TobyUserYear(Integer id) {
        this.id = id;
    }

    public GlYear getYearId() {
        return yearId;
    }

    public void setYearId(GlYear yearId) {
        this.yearId = yearId;
    }

    public TobyUser getUserId() {
        return userId;
    }

    public void setUserId(TobyUser userId) {
        this.userId = userId;
    }
    
    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof TobyUserYear)) {
            return false;
        }
        TobyUserYear other = (TobyUserYear) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.TobyUserYear[ id=" + id + " ]";
    }
    
}
