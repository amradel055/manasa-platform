/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ahmed Shaaban
 */
@Entity
@Table(name = "hr_sponsor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrSponsor.findAll", query = "SELECT h FROM HrSponsor h"),
    @NamedQuery(name = "HrSponsor.findById", query = "SELECT h FROM HrSponsor h WHERE h.id = :id"),
    @NamedQuery(name = "HrSponsor.findByName", query = "SELECT h FROM HrSponsor h WHERE h.name = :name"),
    @NamedQuery(name = "HrSponsor.findByCreationDate", query = "SELECT h FROM HrSponsor h WHERE h.creationDate = :creationDate"),
    @NamedQuery(name = "HrSponsor.findByModificationDate", query = "SELECT h FROM HrSponsor h WHERE h.modificationDate = :modificationDate")})
public class HrSponsor extends BaseEntity{

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private Integer code;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public HrSponsor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof HrSponsor)) {
            return false;
        }
        HrSponsor other = (HrSponsor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.HrSponsor[ id=" + id + " ]";
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }
    
}
