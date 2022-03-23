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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "rsc_restriction_quantities_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscRestrictionQuantitiesDetail.findAll", query = "SELECT r FROM RscRestrictionQuantitiesDetail r")
    , @NamedQuery(name = "RscRestrictionQuantitiesDetail.findById", query = "SELECT r FROM RscRestrictionQuantitiesDetail r WHERE r.id = :id")})
public class RscRestrictionQuantitiesDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;
   
   
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
   
    @JoinColumn(name = "work_unit_id", referencedColumnName = "id")
    @ManyToOne
    private RscWorkUnitsGuide workUnitId;
    
    @Column(name = "serial")
    private Integer serial;
    
    @Column(name = "contract_qty")
    private BigDecimal contractQty;
    
    @Column(name = "contract_price")
    private BigDecimal contractPrice;
    
    @JoinColumn(name = "rsc_restriction_quantities", referencedColumnName = "id")
    @ManyToOne
    private RscRestrictionQuantities rscRestrictionQuantities;
   

    public RscRestrictionQuantitiesDetail() {
    }

    public RscRestrictionQuantitiesDetail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   
    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

   

    public RscWorkUnitsGuide getWorkUnitId() {
        return workUnitId;
    }

    public void setWorkUnitId(RscWorkUnitsGuide workUnitId) {
        this.workUnitId = workUnitId;
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
        if (!(object instanceof RscRestrictionQuantitiesDetail)) {
            return false;
        }
        RscRestrictionQuantitiesDetail other = (RscRestrictionQuantitiesDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscRestrictionQuantitiesDetail[ id=" + id + " ]";
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
     * @return the rscRestrictionQuantities
     */
    public RscRestrictionQuantities getRscRestrictionQuantities() {
        return rscRestrictionQuantities;
    }

    /**
     * @param rscRestrictionQuantities the rscRestrictionQuantities to set
     */
    public void setRscRestrictionQuantities(RscRestrictionQuantities rscRestrictionQuantities) {
        this.rscRestrictionQuantities = rscRestrictionQuantities;
    }

    /**
     * @return the contractQty
     */
    public BigDecimal getContractQty() {
        return contractQty;
    }

    /**
     * @param contractQty the contractQty to set
     */
    public void setContractQty(BigDecimal contractQty) {
        this.contractQty = contractQty;
    }

    /**
     * @return the contractPrice
     */
    public BigDecimal getContractPrice() {
        return contractPrice;
    }

    /**
     * @param contractPrice the contractPrice to set
     */
    public void setContractPrice(BigDecimal contractPrice) {
        this.contractPrice = contractPrice;
    }

   
    
}
