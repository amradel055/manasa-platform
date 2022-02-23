/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
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
@Table(name = "rsc_work_units_guide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscWorkUnitsGuide.findAll", query = "SELECT r FROM RscWorkUnitsGuide r")
    , @NamedQuery(name = "RscWorkUnitsGuide.findById", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.id = :id")
    , @NamedQuery(name = "RscWorkUnitsGuide.findByCode", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.code = :code")
    , @NamedQuery(name = "RscWorkUnitsGuide.findByName", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.name = :name")
    , @NamedQuery(name = "RscWorkUnitsGuide.findByLevel", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.level = :level")
    , @NamedQuery(name = "RscWorkUnitsGuide.findByActive", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.active = :active")
    , @NamedQuery(name = "RscWorkUnitsGuide.findByCreationDate", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscWorkUnitsGuide.findByModificationDate", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscWorkUnitsGuide.findByCodeReport", query = "SELECT r FROM RscWorkUnitsGuide r WHERE r.branchId.id = :branchId AND (:codeFrom IS NULL OR r.code >= :codeFrom) AND (:codeTo  IS NULL OR r.code <= :codeTo)" )
    })
public class RscWorkUnitsGuide extends BaseEntity {

    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "level")
    private Integer level;
    @Column(name = "active")
    private Boolean active;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private RscWorkUnitsGuide parent;
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitId;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "type")
    private Integer type;

    public RscWorkUnitsGuide() {
    }

    public RscWorkUnitsGuide(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public RscWorkUnitsGuide getParent() {
        return parent;
    }

    public void setParent(RscWorkUnitsGuide parent) {
        this.parent = parent;
    }

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
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
        if (!(object instanceof RscWorkUnitsGuide)) {
            return false;
        }
        RscWorkUnitsGuide other = (RscWorkUnitsGuide) object;
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
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
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
    
}
