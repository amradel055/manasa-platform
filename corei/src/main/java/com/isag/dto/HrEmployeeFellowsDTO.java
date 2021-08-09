/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.Date;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class HrEmployeeFellowsDTO extends BaseEntityDTO {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer idNumber;
    private Date idEndDate;
    private Integer employeeId;
    private String relation;

    public HrEmployeeFellowsDTO() {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIndex() != null ? getIndex().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HrEmployeeFellowsDTO)) {
            return false;
        }
        HrEmployeeFellowsDTO other = (HrEmployeeFellowsDTO) object;
        if ((this.getIndex() == null && other.getIndex() != null) || (this.getIndex() != null && !this.getIndex().equals(other.getIndex()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getIndex() + "" + getName();
    }

    public String getRelation() {
        return relation;
    }

    /**
     * @param relation the relation to set
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

}
