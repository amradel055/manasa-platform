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
@Table(name = "rsc_qotation_price_tender_detail_work_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscQotationPriceTenderDetailWorkUnit.findAll", query = "SELECT r FROM RscQotationPriceTenderDetailWorkUnit r")
    , @NamedQuery(name = "RscQotationPriceTenderDetailWorkUnit.findById", query = "SELECT r FROM RscQotationPriceTenderDetailWorkUnit r WHERE r.id = :id")
    , @NamedQuery(name = "RscQotationPriceTenderDetailWorkUnit.findByStatus", query = "SELECT r FROM RscQotationPriceTenderDetailWorkUnit r WHERE r.status = :status")
    , @NamedQuery(name = "RscQotationPriceTenderDetailWorkUnit.findByRemark", query = "SELECT r FROM RscQotationPriceTenderDetailWorkUnit r WHERE r.remark = :remark")
    , @NamedQuery(name = "RscQotationPriceTenderDetailWorkUnit.findByCreationDate", query = "SELECT r FROM RscQotationPriceTenderDetailWorkUnit r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscQotationPriceTenderDetailWorkUnit.findByModificationDate", query = "SELECT r FROM RscQotationPriceTenderDetailWorkUnit r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscQotationPriceTenderDetailWorkUnit.findBySerial", query = "SELECT r FROM RscQotationPriceTenderDetailWorkUnit r WHERE r.serial = :serial")})
public class RscQotationPriceTenderDetailWorkUnit extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "total")
    private BigDecimal total;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "qotation_price_tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscQotationPriceTender qotationPriceTenderId;
    @JoinColumn(name = "work_unit_id", referencedColumnName = "id")
    @ManyToOne
    private RscWorkUnitsGuide workUnitId;

    public RscQotationPriceTenderDetailWorkUnit() {
    }

    public RscQotationPriceTenderDetailWorkUnit(Integer id) {
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
        if (!(object instanceof RscQotationPriceTenderDetailWorkUnit)) {
            return false;
        }
        RscQotationPriceTenderDetailWorkUnit other = (RscQotationPriceTenderDetailWorkUnit) object;
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
     * @return the qotationPriceTenderId
     */
    public RscQotationPriceTender getQotationPriceTenderId() {
        return qotationPriceTenderId;
    }

    /**
     * @param qotationPriceTenderId the qotationPriceTenderId to set
     */
    public void setQotationPriceTenderId(RscQotationPriceTender qotationPriceTenderId) {
        this.qotationPriceTenderId = qotationPriceTenderId;
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
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    
}
