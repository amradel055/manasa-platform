package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author H
 */
public class ProProductionStagesDTO extends BaseEntityDTO{
    
    
    private String name;
    private String nameEn;
    private String nameIn;
    private BigDecimal price;
    private String hostName;
     private Integer serial;
     private Integer typeStage;
    
    public ProProductionStagesDTO(){
    
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameIn() {
        return nameIn;
    }

    public void setNameIn(String nameIn) {
        this.nameIn = nameIn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
      public Integer getSerial() {
        return serial;
    }

    
    public void setSerial(Integer serial) {
        this.serial = serial;
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
        if (!(object instanceof ProProductionStagesDTO)) {
            return false;
        }
        ProProductionStagesDTO other = (ProProductionStagesDTO) object;
        if ((this.id == null && other.getId() != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

  
     @Override
    public String toString() {
        return name + " " + id;
    }

    
    public Integer getTypeStage() {
        return typeStage;
    }

    
    public void setTypeStage(Integer typeStage) {
        this.typeStage = typeStage;
    }
    
  
    
    

}
