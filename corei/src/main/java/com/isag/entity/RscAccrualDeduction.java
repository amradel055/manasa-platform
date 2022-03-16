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
 * @author ahmed
 */
@Entity
@Table(name = "rsc_accrual_deduction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscAccrualDeduction.findAll", query = "SELECT r FROM RscAccrualDeduction r"),
    @NamedQuery(name = "RscAccrualDeduction.findById", query = "SELECT r FROM RscAccrualDeduction r WHERE r.id = :id"),
    @NamedQuery(name = "RscAccrualDeduction.findByCreationDate", query = "SELECT r FROM RscAccrualDeduction r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "RscAccrualDeduction.findByModificationDate", query = "SELECT r FROM RscAccrualDeduction r WHERE r.modificationDate = :modificationDate"),
    @NamedQuery(name = "RscAccrualDeduction.findBySerial", query = "SELECT r FROM RscAccrualDeduction r WHERE r.serial = :serial")})
public class RscAccrualDeduction extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "serial")
    private Integer serial;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Column(name = "type")
    private Integer type;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public RscAccrualDeduction() {
    }

    public RscAccrualDeduction(Integer id) {
        this.id = id;
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
        if (!(object instanceof RscAccrualDeduction)) {
            return false;
        }
        RscAccrualDeduction other = (RscAccrualDeduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscAccrualDeduction[ id=" + id + " ]";
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
