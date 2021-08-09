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
 * @author `*"<:ahmedShaaban:>"*`
 */
@Entity
@Table(name = "inst_profit_rate")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstProfitRate.findAll", query = "SELECT i FROM InstProfitRate i"),
    @NamedQuery(name = "InstProfitRate.findById", query = "SELECT i FROM InstProfitRate i WHERE i.id = :id"),
    @NamedQuery(name = "InstProfitRate.findByMonthNumber", query = "SELECT i FROM InstProfitRate i WHERE i.monthNumber = :monthNumber"),
    @NamedQuery(name = "InstProfitRate.findByRate", query = "SELECT i FROM InstProfitRate i WHERE i.rate = :rate"),
    @NamedQuery(name = "InstProfitRate.findByCreationDate", query = "SELECT i FROM InstProfitRate i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InstProfitRate.findByModificationDate", query = "SELECT i FROM InstProfitRate i WHERE i.modificationDate = :modificationDate")})
public class InstProfitRate extends BaseEntity {

    @Column(name = "month_number")
    private Integer monthNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate")
    private BigDecimal rate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public InstProfitRate() {
    }

    public InstProfitRate(Integer id) {
        this.id = id;
    }

    public Integer getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof InstProfitRate)) {
            return false;
        }
        InstProfitRate other = (InstProfitRate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.InstProfitRate[ id=" + id + " ]";
    }

}
