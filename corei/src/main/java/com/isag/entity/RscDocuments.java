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
 * @author amr
 */
@Entity
@Table(name = "rsc_documents")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscDocuments.findAll", query = "SELECT r FROM RscDocuments r")
    , @NamedQuery(name = "RscDocuments.findById", query = "SELECT r FROM RscDocuments r WHERE r.id = :id")
    , @NamedQuery(name = "RscDocuments.findByName", query = "SELECT r FROM RscDocuments r WHERE r.name = :name")
    , @NamedQuery(name = "RscDocuments.findByPath", query = "SELECT r FROM RscDocuments r WHERE r.path = :path")
    , @NamedQuery(name = "RscDocuments.findByCreationDate", query = "SELECT r FROM RscDocuments r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscDocuments.findByModificationDate", query = "SELECT r FROM RscDocuments r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscDocuments.findBySerial", query = "SELECT r FROM RscDocuments r WHERE r.serial = :serial")})
public class RscDocuments extends BaseEntity {

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "path")
    private String path;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public RscDocuments() {
    }

    public RscDocuments(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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
        if (!(object instanceof RscDocuments)) {
            return false;
        }
        RscDocuments other = (RscDocuments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscDocuments[ id=" + id + " ]";
    }
    
}
