/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author IT Shop
 */

public class RemWorkOrderDTO extends BaseEntityDTO{

 
  
  
  
    private Integer serial;
    
    private Date date;
    
    private RemBusesDTO busesId;
    
    private EmployeeDTO driverId;
    
    private List<RemWorkOrderDetailDTO> remWorkOrderDetailDTOList;
    private List<RemWorkOrderDetailDTO> remWorkOrderDetailDTOListDeleted;
    private RemWorkOrderDetailDTO remWorkOrderDetailDTOSelected;
    
    
  

    public RemWorkOrderDTO() {
    }

    /**
     * @return the branchId
     */
   

   
    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        if (!(object instanceof RemWorkOrderDTO)) {
            return false;
        }
        RemWorkOrderDTO other = (RemWorkOrderDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemWorkOrder[ id=" + id + " ]";
    }

    /**
     * @return the busesId
     */
    public RemBusesDTO getBusesId() {
        return busesId;
    }

    /**
     * @param busesId the busesId to set
     */
    public void setBusesId(RemBusesDTO busesId) {
        this.busesId = busesId;
    }

    /**
     * @return the remWorkOrderDetailDTOList
     */
    public List<RemWorkOrderDetailDTO> getRemWorkOrderDetailDTOList() {
        if(remWorkOrderDetailDTOList == null){
            remWorkOrderDetailDTOList = new  ArrayList<>();
        }
        return remWorkOrderDetailDTOList;
    }

    /**
     * @param remWorkOrderDetailDTOList the remWorkOrderDetailDTOList to set
     */
    public void setRemWorkOrderDetailDTOList(List<RemWorkOrderDetailDTO> remWorkOrderDetailDTOList) {
        this.remWorkOrderDetailDTOList = remWorkOrderDetailDTOList;
    }

    /**
     * @return the remWorkOrderDetailDTOListDeleted
     */
    public List<RemWorkOrderDetailDTO> getRemWorkOrderDetailDTOListDeleted() {
        return remWorkOrderDetailDTOListDeleted;
    }

    /**
     * @param remWorkOrderDetailDTOListDeleted the remWorkOrderDetailDTOListDeleted to set
     */
    public void setRemWorkOrderDetailDTOListDeleted(List<RemWorkOrderDetailDTO> remWorkOrderDetailDTOListDeleted) {
        this.remWorkOrderDetailDTOListDeleted = remWorkOrderDetailDTOListDeleted;
    }

    /**
     * @return the remWorkOrderDetailDTOSelected
     */
    public RemWorkOrderDetailDTO getRemWorkOrderDetailDTOSelected() {
        return remWorkOrderDetailDTOSelected;
    }

    /**
     * @param remWorkOrderDetailDTOSelected the remWorkOrderDetailDTOSelected to set
     */
    public void setRemWorkOrderDetailDTOSelected(RemWorkOrderDetailDTO remWorkOrderDetailDTOSelected) {
        this.remWorkOrderDetailDTOSelected = remWorkOrderDetailDTOSelected;
    }

    /**
     * @return the driverId
     */
    public EmployeeDTO getDriverId() {
        return driverId;
    }

    /**
     * @param driverId the driverId to set
     */
    public void setDriverId(EmployeeDTO driverId) {
        this.driverId = driverId;
    }
    
}
