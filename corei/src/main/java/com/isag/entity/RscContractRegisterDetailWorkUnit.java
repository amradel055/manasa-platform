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
 * @author amr
 */
@Entity
@Table(name = "rsc_contract_register_detail_work_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findAll", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findById", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.id = :id")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findByRemark", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.remark = :remark")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findByCreationDate", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findByModificationDate", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findBySerial", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.serial = :serial")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findByQuantity", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.quantity = :quantity")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findByPrice", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.price = :price")
    , @NamedQuery(name = "RscContractRegisterDetailWorkUnit.findByTotal", query = "SELECT r FROM RscContractRegisterDetailWorkUnit r WHERE r.total = :total")})
public class RscContractRegisterDetailWorkUnit extends BaseEntity {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @JoinColumn(name = "rsc_contract_register_id", referencedColumnName = "id")
    @ManyToOne
    private RscContractRegister rscContractRegisterId;
    @JoinColumn(name = "work_unit_id", referencedColumnName = "id")
    @ManyToOne
    private RscWorkUnitsGuide workUnitId;
    
     @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public RscContractRegisterDetailWorkUnit() {
    }

    public RscContractRegisterDetailWorkUnit(Integer id) {
        this.id = id;
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
        if (!(object instanceof RscContractRegisterDetailWorkUnit)) {
            return false;
        }
        RscContractRegisterDetailWorkUnit other = (RscContractRegisterDetailWorkUnit) object;
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
     * @return the rscContractRegisterId
     */
    public RscContractRegister getRscContractRegisterId() {
        return rscContractRegisterId;
    }

    /**
     * @param rscContractRegisterId the rscContractRegisterId to set
     */
    public void setRscContractRegisterId(RscContractRegister rscContractRegisterId) {
        this.rscContractRegisterId = rscContractRegisterId;
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
