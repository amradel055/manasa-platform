/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Entity
@Table(name = "hr_salaries_structure_effect")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrSalariesStructureEffect.findAll", query = "SELECT h FROM HrSalariesStructureEffect h"),
    @NamedQuery(name = "HrSalariesStructureEffect.findById", query = "SELECT h FROM HrSalariesStructureEffect h WHERE h.id = :id"),
    @NamedQuery(name = "HrSalariesStructureEffect.findByEffectDate", query = "SELECT h FROM HrSalariesStructureEffect h WHERE h.effectDate = :effectDate"),
    @NamedQuery(name = "HrSalariesStructureEffect.findByUnitEffectCount", query = "SELECT h FROM HrSalariesStructureEffect h WHERE h.unitEffectCount = :unitEffectCount"),
    @NamedQuery(name = "HrSalariesStructureEffect.findByStatus", query = "SELECT h FROM HrSalariesStructureEffect h WHERE h.status = :status"),
    @NamedQuery(name = "HrSalariesStructureEffect.findByModificationDate", query = "SELECT h FROM HrSalariesStructureEffect h WHERE h.modificationDate = :modificationDate"),
    @NamedQuery(name = "HrSalariesStructureEffect.findByCreationDate", query = "SELECT h FROM HrSalariesStructureEffect h WHERE h.creationDate = :creationDate")})
public class HrSalariesStructureEffect extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "effect_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "unit_effect_count")
    private BigDecimal unitEffectCount;
    @Column(name = "status")
    private Integer status;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
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
    @Column(name = "salary_structure_id")
    private Integer salaryStructureId;

    @JoinColumn(name = "effect_type", referencedColumnName = "id")
    @ManyToOne
    private HrSalaryEffect effectType;
    @JoinColumn(name = "unit_type", referencedColumnName = "id")
    @ManyToOne
    private Symbol unitType;

    public HrSalariesStructureEffect() {
    }

    public HrSalariesStructureEffect(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public BigDecimal getUnitEffectCount() {
        return unitEffectCount;
    }

    public void setUnitEffectCount(BigDecimal unitEffectCount) {
        this.unitEffectCount = unitEffectCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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

    public Integer getSalaryStructureId() {
        return salaryStructureId;
    }

    public void setSalaryStructureId(Integer salaryStructureId) {
        this.salaryStructureId = salaryStructureId;
    }

    public HrSalaryEffect getEffectType() {
        return effectType;
    }

    public void setEffectType(HrSalaryEffect effectType) {
        this.effectType = effectType;
    }

    public Symbol getUnitType() {
        return unitType;
    }

    public void setUnitType(Symbol unitType) {
        this.unitType = unitType;
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
        if (!(object instanceof HrSalaryStructure)) {
            return false;
        }
        HrSalaryStructure other = (HrSalaryStructure) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.HrSalariesEffect[ id=" + id + " ]";
    }

}
