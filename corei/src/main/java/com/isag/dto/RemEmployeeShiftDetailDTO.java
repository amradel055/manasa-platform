/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author IT Shop
 */
public class RemEmployeeShiftDetailDTO extends BaseEntityDTO{

    private EmployeeDTO employeeId;
    private EmployeeDTO employeeIdBak;
    private Integer remEmployeeShiftId;

    public RemEmployeeShiftDetailDTO() {
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
        if (!(object instanceof RemEmployeeShiftDetailDTO)) {
            return false;
        }
        RemEmployeeShiftDetailDTO other = (RemEmployeeShiftDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemEmployeeShiftDetail[ id=" + id + " ]";
    }

    /**
     * @return the employeeId
     */
    public EmployeeDTO getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(EmployeeDTO employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the remEmployeeShiftId
     */
    public Integer getRemEmployeeShiftId() {
        return remEmployeeShiftId;
    }

    /**
     * @param remEmployeeShiftId the remEmployeeShiftId to set
     */
    public void setRemEmployeeShiftId(Integer remEmployeeShiftId) {
        this.remEmployeeShiftId = remEmployeeShiftId;
    }

    /**
     * @return the employeeIdBak
     */
    public EmployeeDTO getEmployeeIdBak() {
        return employeeIdBak;
    }

    /**
     * @param employeeIdBak the employeeIdBak to set
     */
    public void setEmployeeIdBak(EmployeeDTO employeeIdBak) {
        this.employeeIdBak = employeeIdBak;
    }
    
}
