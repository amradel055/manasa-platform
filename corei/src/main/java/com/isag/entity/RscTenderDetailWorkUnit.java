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
@Table(name = "rsc_tender_detail_work_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscTenderDetailWorkUnit.findAll", query = "SELECT r FROM RscTenderDetailWorkUnit r"),
    @NamedQuery(name = "RscTenderDetailWorkUnit.findById", query = "SELECT r FROM RscTenderDetailWorkUnit r WHERE r.id = :id"),
    @NamedQuery(name = "RscTenderDetailWorkUnit.findByQuantity", query = "SELECT r FROM RscTenderDetailWorkUnit r WHERE r.quantity = :quantity"),
    @NamedQuery(name = "RscTenderDetailWorkUnit.findByStatus", query = "SELECT r FROM RscTenderDetailWorkUnit r WHERE r.status = :status"),
    @NamedQuery(name = "RscTenderDetailWorkUnit.findByRemark", query = "SELECT r FROM RscTenderDetailWorkUnit r WHERE r.remark = :remark"),
    @NamedQuery(name = "RscTenderDetailWorkUnit.findByCreationDate", query = "SELECT r FROM RscTenderDetailWorkUnit r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "RscTenderDetailWorkUnit.findByModificationDate", query = "SELECT r FROM RscTenderDetailWorkUnit r WHERE r.modificationDate = :modificationDate"),
    @NamedQuery(name = "RscTenderDetailWorkUnit.findBySerial", query = "SELECT r FROM RscTenderDetailWorkUnit r WHERE r.serial = :serial")})
public class RscTenderDetailWorkUnit extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "status")
    private Integer status;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private RscTenderDetailWorkUnit parent;
    @JoinColumn(name = "tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender tenderId;
    @JoinColumn(name = "work_unit_id", referencedColumnName = "id")
    @ManyToOne
    private RscWorkUnitsGuide workUnitId;
    @Size(max = 45)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "price_to")
    private BigDecimal priceTo;

    public RscTenderDetailWorkUnit() {
    }

    public RscTenderDetailWorkUnit(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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

    public RscTenderDetailWorkUnit getParent() {
        return parent;
    }

    public void setParent(RscTenderDetailWorkUnit parent) {
        this.parent = parent;
    }

    public RscTender getTenderId() {
        return tenderId;
    }

    public void setTenderId(RscTender tenderId) {
        this.tenderId = tenderId;
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
        if (!(object instanceof RscTenderDetailWorkUnit)) {
            return false;
        }
        RscTenderDetailWorkUnit other = (RscTenderDetailWorkUnit) object;
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
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    /**
     * @return the priceTo
     */
    public BigDecimal getPriceTo() {
        return priceTo;
    }

    /**
     * @param priceTo the priceTo to set
     */
    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

}
