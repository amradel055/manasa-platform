/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author Mostafa
 */
public class InstProjectGuideDTO extends BaseEntityDTO{
    
    private String code;
    private String name;
    private String customerName;
    private Integer level;
    private BigDecimal price;
    private InstProjectGuideDTO parent;
    private Integer parentId;
    private Boolean sell;
    private Boolean reserved;
    private SymbolDTO symbolSize;
    private Boolean active;

    private Boolean isEdit;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
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
        if (!(object instanceof InstProjectGuideDTO)) {
            return false;
        }
        InstProjectGuideDTO other = (InstProjectGuideDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  code +" "+ name + (parent == null ? "" : parent.getName()) + (symbolSize == null ? "" : symbolSize.getName());
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the parent
     */
    public InstProjectGuideDTO getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(InstProjectGuideDTO parent) {
        this.parent = parent;
    }

    /**
     * @return the parentId
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the sell
     */
    public Boolean getSell() {
        return sell;
    }

    /**
     * @param sell the sell to set
     */
    public void setSell(Boolean sell) {
        this.sell = sell;
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
     * @return the symbolSize
     */
    public SymbolDTO getSymbolSize() {
        return symbolSize;
    }

    /**
     * @param symbolSize the symbolSize to set
     */
    public void setSymbolSize(SymbolDTO symbolSize) {
        this.symbolSize = symbolSize;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return the isEdit
     */
    public Boolean getIsEdit() {
        if(isEdit == null){
            isEdit = Boolean.FALSE;
        }
        return isEdit;
    }

    /**
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(Boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
