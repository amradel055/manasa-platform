/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "inv_month_target")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvMonthTarget.findAll", query = "SELECT i FROM InvMonthTarget i")
    , @NamedQuery(name = "InvMonthTarget.findById", query = "SELECT i FROM InvMonthTarget i WHERE i.id = :id")
    , @NamedQuery(name = "InvMonthTarget.findByTarget", query = "SELECT i FROM InvMonthTarget i WHERE i.target = :target")
    , @NamedQuery(name = "InvMonthTarget.findByDateFrom", query = "SELECT i FROM InvMonthTarget i WHERE i.dateFrom = :dateFrom")
    , @NamedQuery(name = "InvMonthTarget.findByCreationDate", query = "SELECT i FROM InvMonthTarget i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "InvMonthTarget.findByDateTo", query = "SELECT i FROM InvMonthTarget i WHERE i.dateTo = :dateTo")})
public class InvMonthTarget extends BaseEntity {

    @Column(name = "target")
    private Integer target;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @Column(name = "code")
    private Integer code;
    @Column(name = "type")
    private Integer type;
       
    public InvMonthTarget() {
    }

  
    public InvMonthTarget(Integer id, Date dateFrom) {
        this.id = id;
        this.dateFrom = dateFrom;
    }

    
    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

   
    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

     public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
        if (!(object instanceof InvMonthTarget)) {
            return false;
        }
        InvMonthTarget other = (InvMonthTarget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  code + " ";
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }
    
}
