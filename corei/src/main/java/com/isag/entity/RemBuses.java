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
 * @author IT Shop
 */
@Entity
@Table(name = "rem_buses")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Buses.findAll", query = "SELECT b FROM RemBuses b")
    , @NamedQuery(name = "Buses.findByName", query = "SELECT b FROM RemBuses b WHERE b.name = :name")
    , @NamedQuery(name = "Buses.findByChairNumber", query = "SELECT b FROM RemBuses b WHERE b.chairNumber = :chairNumber")
    , @NamedQuery(name = "Buses.findByCreationDate", query = "SELECT b FROM RemBuses b WHERE b.creationDate = :creationDate")
    , @NamedQuery(name = "Buses.findByModificationDate", query = "SELECT b FROM RemBuses b WHERE b.modificationDate = :modificationDate")})
public class RemBuses extends BaseEntity{

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private Integer code;
    @Column(name = "chair_number")
    private Integer chairNumber;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public RemBuses() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(Integer chairNumber) {
        this.chairNumber = chairNumber;
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
        if (!(object instanceof RemBuses)) {
            return false;
        }
        RemBuses other = (RemBuses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Buses[ busesPK=" + id + " ]";
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

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

}
