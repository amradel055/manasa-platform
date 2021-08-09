/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "rsc_price_tender_detail_work_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findAll", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findById", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.id = :id")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByQuantityUnits", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.quantityUnits = :quantityUnits")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByStatus", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.status = :status")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByRemark", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.remark = :remark")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByCreationDate", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByModificationDate", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findBySerial", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.serial = :serial")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByPriceUnit", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.priceUnit = :priceUnit")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByTotalUnits", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.totalUnits = :totalUnits")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByProfitValue", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.profitValue = :profitValue")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByProfitPercentage", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.profitPercentage = :profitPercentage")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByPriceUnitSell", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.priceUnitSell = :priceUnitSell")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByTotalProfit", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.totalProfit = :totalProfit")
    , @NamedQuery(name = "RscPriceTenderDetailWorkUnit.findByTotalSell", query = "SELECT r FROM RscPriceTenderDetailWorkUnit r WHERE r.totalSell = :totalSell")})
public class RscPriceTenderDetailWorkUnit extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "quantity_units")
    private BigDecimal quantityUnits;
    @Column(name = "price_unit")
    private BigDecimal priceUnit;
    @Column(name = "total_units")
    private BigDecimal totalUnits;
    @Column(name = "profit_value")
    private BigDecimal profitValue;
    @Column(name = "profit_percentage")
    private BigDecimal profitPercentage;
    @Column(name = "price_unit_sell")
    private BigDecimal priceUnitSell;
    @Column(name = "total_profit")
    private BigDecimal totalProfit;
    @Column(name = "total_sell")
    private BigDecimal totalSell;
    @Column(name = "total_quantity_unit")
    private BigDecimal totalQuantityUnit;
    @Column(name = "total_cost")
    private BigDecimal totalCost;
    @Column(name = "quantity_in")
    private BigDecimal quantityIn;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @OneToMany(mappedBy = "parent")
    private Collection<RscPriceTenderDetailWorkUnit> rscPriceTenderDetailWorkUnitCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private RscPriceTenderDetailWorkUnit parent;
    @JoinColumn(name = "price_tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscPriceTender rscPriceTender;
    @JoinColumn(name = "work_unit_id", referencedColumnName = "id")
    @ManyToOne
    private RscWorkUnitsGuide workUnitId;

    public RscPriceTenderDetailWorkUnit() {
    }

    public RscPriceTenderDetailWorkUnit(Integer id) {
        this.id = id;
    }

   
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public RscPriceTenderDetailWorkUnit getParent() {
        return parent;
    }

    public void setParent(RscPriceTenderDetailWorkUnit parent) {
        this.parent = parent;
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
        if (!(object instanceof RscPriceTenderDetailWorkUnit)) {
            return false;
        }
        RscPriceTenderDetailWorkUnit other = (RscPriceTenderDetailWorkUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.RscTenderDetailWorkUnit[ id=" + id + " ]";
    }

    /**
     * @return the quantityUnits
     */
    public BigDecimal getQuantityUnits() {
        return quantityUnits;
    }

    /**
     * @param quantityUnits the quantityUnits to set
     */
    public void setQuantityUnits(BigDecimal quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    /**
     * @return the priceUnit
     */
    public BigDecimal getPriceUnit() {
        return priceUnit;
    }

    /**
     * @param priceUnit the priceUnit to set
     */
    public void setPriceUnit(BigDecimal priceUnit) {
        this.priceUnit = priceUnit;
    }

    /**
     * @return the totalUnits
     */
    public BigDecimal getTotalUnits() {
        return totalUnits;
    }

    /**
     * @param totalUnits the totalUnits to set
     */
    public void setTotalUnits(BigDecimal totalUnits) {
        this.totalUnits = totalUnits;
    }

    /**
     * @return the profitValue
     */
    public BigDecimal getProfitValue() {
        return profitValue;
    }

    /**
     * @param profitValue the profitValue to set
     */
    public void setProfitValue(BigDecimal profitValue) {
        this.profitValue = profitValue;
    }

    /**
     * @return the profitPercentage
     */
    public BigDecimal getProfitPercentage() {
        return profitPercentage;
    }

    /**
     * @param profitPercentage the profitPercentage to set
     */
    public void setProfitPercentage(BigDecimal profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    /**
     * @return the priceUnitSell
     */
    public BigDecimal getPriceUnitSell() {
        return priceUnitSell;
    }

    /**
     * @param priceUnitSell the priceUnitSell to set
     */
    public void setPriceUnitSell(BigDecimal priceUnitSell) {
        this.priceUnitSell = priceUnitSell;
    }

    /**
     * @return the totalProfit
     */
    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    /**
     * @param totalProfit the totalProfit to set
     */
    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    /**
     * @return the totalSell
     */
    public BigDecimal getTotalSell() {
        return totalSell;
    }

    /**
     * @param totalSell the totalSell to set
     */
    public void setTotalSell(BigDecimal totalSell) {
        this.totalSell = totalSell;
    }

    /**
     * @return the rscPriceTenderDetailWorkUnitCollection
     */
    public Collection<RscPriceTenderDetailWorkUnit> getRscPriceTenderDetailWorkUnitCollection() {
        return rscPriceTenderDetailWorkUnitCollection;
    }

    /**
     * @param rscPriceTenderDetailWorkUnitCollection the rscPriceTenderDetailWorkUnitCollection to set
     */
    public void setRscPriceTenderDetailWorkUnitCollection(Collection<RscPriceTenderDetailWorkUnit> rscPriceTenderDetailWorkUnitCollection) {
        this.rscPriceTenderDetailWorkUnitCollection = rscPriceTenderDetailWorkUnitCollection;
    }

    /**
     * @return the totalQuantityUnit
     */
    public BigDecimal getTotalQuantityUnit() {
        return totalQuantityUnit;
    }

    /**
     * @param totalQuantityUnit the totalQuantityUnit to set
     */
    public void setTotalQuantityUnit(BigDecimal totalQuantityUnit) {
        this.totalQuantityUnit = totalQuantityUnit;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the quantityIn
     */
    public BigDecimal getQuantityIn() {
        return quantityIn;
    }

    /**
     * @param quantityIn the quantityIn to set
     */
    public void setQuantityIn(BigDecimal quantityIn) {
        this.quantityIn = quantityIn;
    }

    /**
     * @return the rscPriceTender
     */
    public RscPriceTender getRscPriceTender() {
        return rscPriceTender;
    }

    /**
     * @param rscPriceTender the rscPriceTender to set
     */
    public void setRscPriceTender(RscPriceTender rscPriceTender) {
        this.rscPriceTender = rscPriceTender;
    }
    
}
