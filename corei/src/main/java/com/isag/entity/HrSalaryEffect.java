/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ahmed Shaaban
 */
@Entity
@Table(name = "hr_salary_effect")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrSalaryEffect.findAll", query = "SELECT s FROM HrSalaryEffect s"),
    @NamedQuery(name = "HrSalaryEffect.findById", query = "SELECT s FROM HrSalaryEffect s WHERE s.id = :id"),
    @NamedQuery(name = "HrSalaryEffect.findByName", query = "SELECT s FROM HrSalaryEffect s WHERE s.name = :name"),
    @NamedQuery(name = "HrSalaryEffect.findByCode", query = "SELECT s FROM HrSalaryEffect s WHERE s.code = :code"),
    @NamedQuery(name = "HrSalaryEffect.findByType", query = "SELECT s FROM HrSalaryEffect s WHERE s.type = :type"),
    @NamedQuery(name = "HrSalaryEffect.findByCreationDate", query = "SELECT s FROM HrSalaryEffect s WHERE s.creationDate = :creationDate"),
    @NamedQuery(name = "HrSalaryEffect.findByModificationDate", query = "SELECT s FROM HrSalaryEffect s WHERE s.modificationDate = :modificationDate"),
    @NamedQuery(name = "HrSalaryEffect.findByCofficientPerDay", query = "SELECT s FROM HrSalaryEffect s WHERE s.cofficientPerDay = :cofficientPerDay"),
    @NamedQuery(name = "HrSalaryEffect.findByCofficientPerHour", query = "SELECT s FROM HrSalaryEffect s WHERE s.cofficientPerHour = :cofficientPerHour")})
public class HrSalaryEffect extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private Integer code;
    @Column(name = "impact_indicator")
    private Integer impactIndicator;
    @Column(name = "cofficient_per_day")
    private Integer cofficientPerDay;
    @Column(name = "cofficient_per_hour")
    private Integer cofficientPerHour;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "gl_account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount glAccountId;
    @JoinColumn(name = "type", referencedColumnName = "id")
    @ManyToOne
    private Symbol type;

    public HrSalaryEffect() {
    }

    public HrSalaryEffect(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Symbol getType() {
        return type;
    }

    public void setType(Symbol type) {
        this.type = type;
    }

    public Integer getCofficientPerDay() {
        return cofficientPerDay;
    }

    public void setCofficientPerDay(Integer cofficientPerDay) {
        this.cofficientPerDay = cofficientPerDay;
    }

    public Integer getCofficientPerHour() {
        return cofficientPerHour;
    }

    public void setCofficientPerHour(Integer cofficientPerHour) {
        this.cofficientPerHour = cofficientPerHour;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public GlAccount getGlAccountId() {
        return glAccountId;
    }

    public void setGlAccountId(GlAccount glAccountId) {
        this.glAccountId = glAccountId;
    }

    public Integer getImpactIndicator() {
        return impactIndicator;
    }

    public void setImpactIndicator(Integer impactIndicator) {
        this.impactIndicator = impactIndicator;
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
        if (!(object instanceof HrSalaryEffect)) {
            return false;
        }
        HrSalaryEffect other = (HrSalaryEffect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.SalaryEffectType[ id=" + id + " ]";
    }

}
