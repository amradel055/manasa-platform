package com.toby.dto;

import java.util.Date;
import java.util.List;


public class ProProductMovementDTO extends BaseEntityDTO {
     
   private Date date;
   private Date time;
   private String remark;
   private Integer serial;
   private Integer type;
   private InvOrganizationSiteDTO invOrganizationSiteId;
   private ProProductMovementDTO parent;
   private InvInventoryDTO inventory;
   private InvInventoryDTO invGalary;
   private List<ProProductMovementDetailDTO> proProductMovementDetailDTOList;

    public ProProductMovementDTO() {
    }

    public ProProductMovementDTO(Integer id) {
        this.id = id;
    }

   

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    
   
      public List<ProProductMovementDetailDTO> getProProductMovementDetailDTOList() {
         
        return proProductMovementDetailDTOList;
    }

    public void setProProductMovementDetailDTOList(List<ProProductMovementDetailDTO> proProductMovementDetailDTOList) {
        this.proProductMovementDetailDTOList = proProductMovementDetailDTOList;
    }


    @Override
    public String toString() {

        return serial != null ? serial.toString() : "";
    }

    
    public ProProductMovementDTO getParent() {
        return parent;
    }

    
    public void setParent(ProProductMovementDTO parent) {
        this.parent = parent;
    }

    
    public InvInventoryDTO getInventory() {
        return inventory;
    }

    
    public void setInventory(InvInventoryDTO inventory) {
        this.inventory = inventory;
    }

    
    public InvOrganizationSiteDTO getInvOrganizationSiteId() {
        return invOrganizationSiteId;
    }

    
    public void setInvOrganizationSiteId(InvOrganizationSiteDTO invOrganizationSiteId) {
        this.invOrganizationSiteId = invOrganizationSiteId;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the invGalary
     */
    public InvInventoryDTO getInvGalary() {
        return invGalary;
    }

    /**
     * @param invGalary the invGalary to set
     */
    public void setInvGalary(InvInventoryDTO invGalary) {
        this.invGalary = invGalary;
    }

    
    
    
   

   

  
}
