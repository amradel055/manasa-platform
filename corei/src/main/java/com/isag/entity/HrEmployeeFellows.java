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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ahmed Shaaban
 */
@Entity
@Table(name = "hr_employee_fellows")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HrEmployeeFellows.findAll", query = "SELECT h FROM HrEmployeeFellows h"),
    @NamedQuery(name = "HrEmployeeFellows.findById", query = "SELECT h FROM HrEmployeeFellows h WHERE h.id = :id"),
    @NamedQuery(name = "HrEmployeeFellows.findByName", query = "SELECT h FROM HrEmployeeFellows h WHERE h.name = :name"),
    @NamedQuery(name = "HrEmployeeFellows.findByIdNumber", query = "SELECT h FROM HrEmployeeFellows h WHERE h.idNumber = :idNumber"),
    @NamedQuery(name = "HrEmployeeFellows.findByIdEndDate", query = "SELECT h FROM HrEmployeeFellows h WHERE h.idEndDate = :idEndDate"),
    @NamedQuery(name = "HrEmployeeFellows.findByCreationDate", query = "SELECT h FROM HrEmployeeFellows h WHERE h.creationDate = :creationDate"),
    @NamedQuery(name = "HrEmployeeFellows.findByModificationDate", query = "SELECT h FROM HrEmployeeFellows h WHERE h.modificationDate = :modificationDate")})
public class HrEmployeeFellows extends BaseEntity{


    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "relation")
    private String relation;
    @Column(name = "id_number")
    private Integer idNumber;
    @Column(name = "id_end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idEndDate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeId;

    public HrEmployeeFellows() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }

    public Date getIdEndDate() {
        return idEndDate;
    }

    public void setIdEndDate(Date idEndDate) {
        this.idEndDate = idEndDate;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
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
        if (!(object instanceof HrEmployeeFellows)) {
            return false;
        }
        HrEmployeeFellows other = (HrEmployeeFellows) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.HrEmployeeFellows[ id=" + id + " ]";
    }
    
}
