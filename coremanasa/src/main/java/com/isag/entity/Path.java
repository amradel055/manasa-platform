/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "path")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Path.findAll", query = "SELECT p FROM Path p"),
    @NamedQuery(name = "Path.findById", query = "SELECT p FROM Path p WHERE p.id = :id"),
    @NamedQuery(name = "Path.findByTypeId", query = "SELECT p FROM Path p WHERE p.typeId = :typeId"),
    @NamedQuery(name = "Path.findByTypeName", query = "SELECT p FROM Path p WHERE p.typeName = :typeName"),
    @NamedQuery(name = "Path.findByPath", query = "SELECT p FROM Path p WHERE p.path = :path"),
    @NamedQuery(name = "Path.findByOperatingSystem", query = "SELECT p FROM Path p WHERE p.operatingSystem = :operatingSystem")})
public class Path implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "typeId")
    private Integer typeId;
    @Size(max = 45)
    @Column(name = "typeName")
    private String typeName;
    @Size(max = 450)
    @Column(name = "path")
    private String path;
    @Column(name = "operatingSystem")
    private Integer operatingSystem;

    public Path() {
    }

    public Path(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(Integer operatingSystem) {
        this.operatingSystem = operatingSystem;
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
        if (!(object instanceof Path)) {
            return false;
        }
        Path other = (Path) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.Path[ id=" + id + " ]";
    }
    
}
