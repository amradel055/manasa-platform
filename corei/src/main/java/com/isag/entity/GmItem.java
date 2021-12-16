/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "gm_item")

public class GmItem extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "period")
    private Integer period;
    @Column(name = "cost")
    private BigDecimal cost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "serial")
    private Integer serial;    
    @Column(name = "arrange")
    private Integer arrange;
    @Column(name = "symbol_type")
    private Integer symbolType;
     @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    

    public GmItem() {
    }

    public GmItem(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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
        if (!(object instanceof GmItem)) {
            return false;
        }
        GmItem other = (GmItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " ;
    }

    /**
     * @return the arrange
     */
    public Integer getArrange() {
        return arrange;
    }

    /**
     * @param arrange the arrange to set
     */
    public void setArrange(Integer arrange) {
        this.arrange = arrange;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the symbolType
     */
    public Integer getSymbolType() {
        return symbolType;
    }

    /**
     * @param symbolType the symbolType to set
     */
    public void setSymbolType(Integer symbolType) {
        this.symbolType = symbolType;
    }

    /**
     * @return the period
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

  

}
