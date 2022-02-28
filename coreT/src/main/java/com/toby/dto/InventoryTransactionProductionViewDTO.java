package com.toby.dto;


public class InventoryTransactionProductionViewDTO extends BaseEntityDTO{
    
    private Integer rowNum;
    private Integer serial;
    private String name;
    private Integer status;
    private Integer proProductionId;

    public InventoryTransactionProductionViewDTO() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProProductionId() {
        return proProductionId;
    }

    public void setProProductionId(Integer proProductionId) {
        this.proProductionId = proProductionId;
    }
    
    
}
