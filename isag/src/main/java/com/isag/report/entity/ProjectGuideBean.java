/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

/**
 *
 * @author hq002
 */
public class ProjectGuideBean {

    private String code;
    private String name;
   
    private Integer level; 
    private String active;
    private String parent;
    private Boolean sellType;
    private Boolean reserved;

  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
  
 public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the active
     */
   

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the sellType
     */
    public Boolean getSellType() {
        return sellType;
    }

    /**
     * @param sellType the sellType to set
     */
    public void setSellType(Boolean sellType) {
        this.sellType = sellType;
    }

    /**
     * @return the reserved
     */
    public Boolean getReserved() {
        return reserved;
    }

    /**
     * @param reserved the reserved to set
     */
    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    /**
     * @return the active
     */
    

}
