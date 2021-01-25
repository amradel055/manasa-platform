/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "pro_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProGroup.findAll", query = "SELECT i FROM ProGroup i"),
    @NamedQuery(name = "ProGroup.findById", query = "SELECT i FROM ProGroup i WHERE i.id = :id"),
    @NamedQuery(name = "ProGroup.findByName", query = "SELECT i FROM ProGroup i WHERE i.name = :name"),
    @NamedQuery(name = "ProGroup.findByLevel", query = "SELECT i FROM ProGroup i WHERE i.level = :level"),
    @NamedQuery(name = "ProGroup.findByModificationDate", query = "SELECT i FROM ProGroup i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "ProGroup.findByCreationDate", query = "SELECT i FROM ProGroup i WHERE i.creationDate = :creationDate")})
public class ProGroup extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "level")
    private Integer level;
    @Column(name = "code")
    private Integer code;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @Column(name = "is_deleted")
    private Integer isDeleted;

    public ProGroup() {
    }

    public ProGroup(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
        if (!(object instanceof ProGroup)) {
            return false;
        }
        ProGroup other = (ProGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
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
     * @return the isDeleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

}
