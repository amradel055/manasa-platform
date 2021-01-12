/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.List;


public class CostCenterDTO extends BaseEntityDTO {

    private Integer code;
    private String name;
    private Integer shortCode;
    private Integer level;
    private Boolean active;
    private List<CostCenterDTO> childNodes;
    private CostCenterDTO parent;

    public CostCenterDTO() {
    }

    public CostCenterDTO(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShortCode() {
        return shortCode;
    }

    public void setShortCode(Integer shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CostCenterDTO getParent() {
        return parent;
    }

    public void setParent(CostCenterDTO parent) {
        this.parent = parent;
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
        if (!(object instanceof CostCenterDTO)) {
            return false;
        }
        CostCenterDTO other = (CostCenterDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the childNodes
     */
    public List<CostCenterDTO> getChildNodes() {
        return childNodes;
    }

    /**
     * @param childNodes the childNodes to set
     */
    public void setChildNodes(List<CostCenterDTO> childNodes) {
        this.childNodes = childNodes;
    }

}
