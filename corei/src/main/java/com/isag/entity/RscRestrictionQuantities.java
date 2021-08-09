/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author ahmed
 */
@Entity
@Table(name = "rsc_restriction_quantities")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscRestrictionQuantities.findAll", query = "SELECT r FROM RscRestrictionQuantities r"),
    @NamedQuery(name = "RscRestrictionQuantities.findById", query = "SELECT r FROM RscRestrictionQuantities r WHERE r.id = :id"),
    @NamedQuery(name = "RscRestrictionQuantities.findByDate", query = "SELECT r FROM RscRestrictionQuantities r WHERE r.date = :date")
  , @NamedQuery(name = "RscRestrictionQuantities.findByRscExtractReport", query = "SELECT r FROM RscRestrictionQuantities r WHERE r.branchId.id = :branchId  AND  (:contractorFrom IS NULL OR r.invOrganizationSite.code >= :contractorFrom) AND (:contractorTo  IS NULL OR r.invOrganizationSite.code <= :contractorTo) AND (:dateFrom IS NULL OR r.date >= :dateFrom) AND (:dateTo IS NULL OR r.date <= :dateTo) AND (:extractFrom IS NULL OR r.serial >= CAST(:extractFrom AS SIGNED)) AND (:extractTo IS NULL OR r.serial <= CAST(:extractTo AS SIGNED))")
})
public class RscRestrictionQuantities extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    @ManyToOne
    private RscContractRegister contractId;
    @JoinColumn(name = "tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender tenderId;
    @Column(name = "serial")
    private Integer serial; 
    @JoinColumn(name = "inst_project_guide_id", referencedColumnName = "id")
    @ManyToOne
    private InstProjectGuide instProjectGuide;
    @JoinColumn(name = "inv_organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite invOrganizationSite;
    @JoinColumn(name = "rsc_extract_register_id", referencedColumnName = "id")
    @ManyToOne
    private RscExtractRegister rscExtractRegisterId;
  
    public RscRestrictionQuantities() {
    }

    public RscRestrictionQuantities(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public RscContractRegister getContractId() {
        return contractId;
    }

    public void setContractId(RscContractRegister contractId) {
        this.contractId = contractId;
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
        if (!(object instanceof RscRestrictionQuantities)) {
            return false;
        }
        RscRestrictionQuantities other = (RscRestrictionQuantities) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscRestrictionQuantities[ id=" + id + " ]";
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the tenderId
     */
    public RscTender getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(RscTender tenderId) {
        this.tenderId = tenderId;
    }

    /**
     * @return the instProjectGuide
     */
    public InstProjectGuide getInstProjectGuide() {
        return instProjectGuide;
    }

    /**
     * @param instProjectGuide the instProjectGuide to set
     */
    public void setInstProjectGuide(InstProjectGuide instProjectGuide) {
        this.instProjectGuide = instProjectGuide;
    }

    /**
     * @return the invOrganizationSite
     */
    public InvOrganizationSite getInvOrganizationSite() {
        return invOrganizationSite;
    }

    /**
     * @param invOrganizationSite the invOrganizationSite to set
     */
    public void setInvOrganizationSite(InvOrganizationSite invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
    }

    /**
     * @return the rscExtractRegisterId
     */
    public RscExtractRegister getRscExtractRegisterId() {
        return rscExtractRegisterId;
    }

    /**
     * @param rscExtractRegisterId the rscExtractRegisterId to set
     */
    public void setRscExtractRegisterId(RscExtractRegister rscExtractRegisterId) {
        this.rscExtractRegisterId = rscExtractRegisterId;
    }

}
