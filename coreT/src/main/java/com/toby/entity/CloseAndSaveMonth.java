/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
 * @author hq003
 */
@Entity
@Table(name = "close_and_save_month")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CloseAndSaveMonth.findAll", query = "SELECT c FROM CloseAndSaveMonth c"),
    @NamedQuery(name = "CloseAndSaveMonth.findById", query = "SELECT c FROM CloseAndSaveMonth c WHERE c.id = :id"),
    @NamedQuery(name = "CloseAndSaveMonth.findByMonthNumber", query = "SELECT c FROM CloseAndSaveMonth c WHERE c.monthNumber = :monthNumber"),
    @NamedQuery(name = "CloseAndSaveMonth.findByMonthName", query = "SELECT c FROM CloseAndSaveMonth c WHERE c.monthName = :monthName"),
    @NamedQuery(name = "CloseAndSaveMonth.findByStatus", query = "SELECT c FROM CloseAndSaveMonth c WHERE c.status = :status"),
    @NamedQuery(name = "CloseAndSaveMonth.findByCreationDate", query = "SELECT c FROM CloseAndSaveMonth c WHERE c.creationDate = :creationDate"),
    @NamedQuery(name = "CloseAndSaveMonth.findByModificationDate", query = "SELECT c FROM CloseAndSaveMonth c WHERE c.modificationDate = :modificationDate")})
public class CloseAndSaveMonth extends BaseEntity {
    @Column(name = "month_number")
    private Integer monthNumber;
    @Size(max = 45)
    @Column(name = "month_name")
    private String monthName;
    @Column(name = "status")
    private Boolean status;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;
    @JoinColumn(name = "year", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GlYear year;

    public CloseAndSaveMonth() {
    }

    public CloseAndSaveMonth(Integer id) {
        this.id = id;
    }

    public Integer getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }
    
    public GlYear getYear() {
        return year;
    }

    public void setYear(GlYear year) {
        this.year = year;
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
        if (!(object instanceof CloseAndSaveMonth)) {
            return false;
        }
        CloseAndSaveMonth other = (CloseAndSaveMonth) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.CloseAndSaveMonth[ id=" + id + " ]";
    }
    
}
