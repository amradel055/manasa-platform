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
 * @author AhmedEssam
 */
@Entity
@Table(name = "hr_salary_effect_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrSalaryEffectType.findAll", query = "SELECT h FROM HrSalaryEffectType h")
    , @NamedQuery(name = "HrSalaryEffectType.findById", query = "SELECT h FROM HrSalaryEffectType h WHERE h.id = :id")
    , @NamedQuery(name = "HrSalaryEffectType.findByName", query = "SELECT h FROM HrSalaryEffectType h WHERE h.name = :name")
    , @NamedQuery(name = "HrSalaryEffectType.findByType", query = "SELECT h FROM HrSalaryEffectType h WHERE h.type = :type")
    , @NamedQuery(name = "HrSalaryEffectType.findByEffectType", query = "SELECT h FROM HrSalaryEffectType h WHERE h.effectType = :effectType")
    , @NamedQuery(name = "HrSalaryEffectType.findByCreationDate", query = "SELECT h FROM HrSalaryEffectType h WHERE h.creationDate = :creationDate")
    , @NamedQuery(name = "HrSalaryEffectType.findByModificationDate", query = "SELECT h FROM HrSalaryEffectType h WHERE h.modificationDate = :modificationDate")
    , @NamedQuery(name = "HrSalaryEffectType.findBySerial", query = "SELECT h FROM HrSalaryEffectType h WHERE h.serial = :serial")})
public class HrSalaryEffectType extends BaseEntity{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private Integer type;
    @Column(name = "effect_type")
    private Integer effectType;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 45)
    @Column(name = "modification_date")
    private String modificationDate;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne
    private IsagCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser createdBy;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser modifiedBy;

    public HrSalaryEffectType() {
    }

    public HrSalaryEffectType(Integer id) {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public IsagCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IsagCompany companyId) {
        this.companyId = companyId;
    }

    public IsagUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
    }

    public IsagUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IsagUser modifiedBy) {
        this.modifiedBy = modifiedBy;
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
        if (!(object instanceof HrSalaryEffectType)) {
            return false;
        }
        HrSalaryEffectType other = (HrSalaryEffectType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.HrSalaryEffectType[ id=" + id + " ]";
    }
    
}
