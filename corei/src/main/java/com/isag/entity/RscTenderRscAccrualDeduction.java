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
 * @author AhmedEssam
 */
@Entity
@Table(name = "rsc_tender_rsc_accrual_deduction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscTenderRscAccrualDeduction.findAll", query = "SELECT r FROM RscTenderRscAccrualDeduction r")
    , @NamedQuery(name = "RscTenderRscAccrualDeduction.findById", query = "SELECT r FROM RscTenderRscAccrualDeduction r WHERE r.id = :id")
    , @NamedQuery(name = "RscTenderRscAccrualDeduction.findByCreationDate", query = "SELECT r FROM RscTenderRscAccrualDeduction r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscTenderRscAccrualDeduction.findByModificationDate", query = "SELECT r FROM RscTenderRscAccrualDeduction r WHERE r.modificationDate = :modificationDate")})
public class RscTenderRscAccrualDeduction extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @JoinColumn(name = "rsc_accrual_deduction_id", referencedColumnName = "id")
    @ManyToOne
    private RscAccrualDeduction rscAccrualDeductionId;
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
    @JoinColumn(name = "rsc_tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender rscTenderId;
    @Column(name = "percentage")
    private BigDecimal percentage;

    public RscTenderRscAccrualDeduction() {
    }

    public RscTenderRscAccrualDeduction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public RscAccrualDeduction getRscAccrualDeductionId() {
        return rscAccrualDeductionId;
    }

    public void setRscAccrualDeductionId(RscAccrualDeduction rscAccrualDeductionId) {
        this.rscAccrualDeductionId = rscAccrualDeductionId;
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

    public RscTender getRscTenderId() {
        return rscTenderId;
    }

    public void setRscTenderId(RscTender rscTenderId) {
        this.rscTenderId = rscTenderId;
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
        if (!(object instanceof RscTenderRscAccrualDeduction)) {
            return false;
        }
        RscTenderRscAccrualDeduction other = (RscTenderRscAccrualDeduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscTenderRscAccrualDeduction[ id=" + id + " ]";
    }

    /**
     * @return the percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
    
}
