/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IT Shop
 */
public class RemEmployeeShiftDTO extends BaseEntityDTO{

    private String code;
    private String name;
    private List<RemEmployeeShiftDetailDTO> remEmployeeShiftDetailList;
    private List<RemEmployeeShiftDetailDTO> remEmployeeShiftDetailListDeleted;
    private RemEmployeeShiftDetailDTO remEmployeeShiftDetailSelected;
    private RemBusesDTO remBusesId;
    private RemCompanyDTO remCompanyId;
    private RemTrafficDTO remTrafficId;

    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof RemEmployeeShiftDTO)) {
            return false;
        }
        RemEmployeeShiftDTO other = (RemEmployeeShiftDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the remEmployeeShiftDetailList
     */
    public List<RemEmployeeShiftDetailDTO> getRemEmployeeShiftDetailList() {
        if(remEmployeeShiftDetailList == null){
            remEmployeeShiftDetailList = new ArrayList<>();
        }
        return remEmployeeShiftDetailList;
    }

    /**
     * @param remEmployeeShiftDetailList the remEmployeeShiftDetailList to set
     */
    public void setRemEmployeeShiftDetailList(List<RemEmployeeShiftDetailDTO> remEmployeeShiftDetailList) {
        this.remEmployeeShiftDetailList = remEmployeeShiftDetailList;
    }

    /**
     * @return the remBusesId
     */
    public RemBusesDTO getRemBusesId() {
        return remBusesId;
    }

    /**
     * @param remBusesId the remBusesId to set
     */
    public void setRemBusesId(RemBusesDTO remBusesId) {
        this.remBusesId = remBusesId;
    }

    /**
     * @return the remCompanyId
     */
    public RemCompanyDTO getRemCompanyId() {
        return remCompanyId;
    }

    /**
     * @param remCompanyId the remCompanyId to set
     */
    public void setRemCompanyId(RemCompanyDTO remCompanyId) {
        this.remCompanyId = remCompanyId;
    }

    /**
     * @return the remTrafficId
     */
    public RemTrafficDTO getRemTrafficId() {
        return remTrafficId;
    }

    /**
     * @param remTrafficId the remTrafficId to set
     */
    public void setRemTrafficId(RemTrafficDTO remTrafficId) {
        this.remTrafficId = remTrafficId;
    }

    /**
     * @return the remEmployeeShiftDetailListDeleted
     */
    public List<RemEmployeeShiftDetailDTO> getRemEmployeeShiftDetailListDeleted() {
        return remEmployeeShiftDetailListDeleted;
    }

    /**
     * @param remEmployeeShiftDetailListDeleted the remEmployeeShiftDetailListDeleted to set
     */
    public void setRemEmployeeShiftDetailListDeleted(List<RemEmployeeShiftDetailDTO> remEmployeeShiftDetailListDeleted) {
        this.remEmployeeShiftDetailListDeleted = remEmployeeShiftDetailListDeleted;
    }

    /**
     * @return the remEmployeeShiftDetailSelected
     */
    public RemEmployeeShiftDetailDTO getRemEmployeeShiftDetailSelected() {
        return remEmployeeShiftDetailSelected;
    }

    /**
     * @param remEmployeeShiftDetailSelected the remEmployeeShiftDetailSelected to set
     */
    public void setRemEmployeeShiftDetailSelected(RemEmployeeShiftDetailDTO remEmployeeShiftDetailSelected) {
        this.remEmployeeShiftDetailSelected = remEmployeeShiftDetailSelected;
    }
    
}
