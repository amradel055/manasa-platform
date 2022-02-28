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
 * @author ahmed
 */
@Entity
@Table(name = "states")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "States.findAll", query = "SELECT s FROM States s")
    , @NamedQuery(name = "States.findById", query = "SELECT s FROM States s WHERE s.id = :id")
    , @NamedQuery(name = "States.findByName", query = "SELECT s FROM States s WHERE s.name = :name")
    , @NamedQuery(name = "States.findByCreationDate", query = "SELECT s FROM States s WHERE s.creationDate = :creationDate")
    , @NamedQuery(name = "States.findByModificationDate", query = "SELECT s FROM States s WHERE s.modificationDate = :modificationDate")})
public class States extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    @JoinColumn(name = "governorate_id", referencedColumnName = "id")
    @ManyToOne
    private Governorates governorateId;

    public States() {
    }

    public States(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

   

    public Governorates getGovernorateId() {
        return governorateId;
    }

    public void setGovernorateId(Governorates governorateId) {
        this.governorateId = governorateId;
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
        if (!(object instanceof States)) {
            return false;
        }
        States other = (States) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com_igma_entity.States[ id=" + id + " ]";
    }

}
