/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
 * @author amr
 */
@Entity
@Table(name = "inst_contract_detail") 
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstContractDetail.findAll", query = "SELECT i FROM InstContractDetail i")
    , @NamedQuery(name = "InstContractDetail.findById", query = "SELECT i FROM InstContractDetail i WHERE i.id = :id")
    , @NamedQuery(name = "InstContractDetail.findByDueDate", query = "SELECT i FROM InstContractDetail i WHERE i.dueDate = :dueDate")
    , @NamedQuery(name = "InstContractDetail.findByValue", query = "SELECT i FROM InstContractDetail i WHERE i.value = :value")
    , @NamedQuery(name = "InstContractDetail.findBySerial", query = "SELECT i FROM InstContractDetail i WHERE i.serial = :serial")
    , @NamedQuery(name = "InstContractDetail.findByCreationDate", query = "SELECT i FROM InstContractDetail i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "InstContractDetail.findByModificationDate", query = "SELECT i FROM InstContractDetail i WHERE i.modificationDate = :modificationDate")})
public class InstContractDetail extends BaseEntity {

    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "status")
    private Integer status;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inst_contract_id", referencedColumnName = "id")
    @ManyToOne
    private InstContract instContractId;

    public InstContractDetail() {
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InstContract getInstContractId() {
        return instContractId;
    }

    public void setInstContractId(InstContract instContractId) {
        this.instContractId = instContractId;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof InstContractDetail)) {
            return false;
        }
        InstContractDetail other = (InstContractDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(dueDate);
        return date + " " + value;
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
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
