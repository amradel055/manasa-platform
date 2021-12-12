/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author Ahmed Shaaban
 */
public class SymbolDTO extends BaseEntityDTO {

    private String name;
    private Integer serial;
    private Integer arrange;
    private Integer generalSymbolId;
    private Integer accountId;
    private BigDecimal cost;
    private Boolean symbolType;
    
    
    public SymbolDTO() {
    }

    public SymbolDTO(Integer id) {
        this.id = id;
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
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the arrange
     */
    public Integer getArrange() {
        return arrange;
    }

    /**
     * @param arrange the arrange to set
     */
    public void setArrange(Integer arrange) {
        this.arrange = arrange;
    }

    /**
     * @return the generalSymbolId
     */
    public Integer getGeneralSymbolId() {
        return generalSymbolId;
    }

    /**
     * @param generalSymbolId the generalSymbolId to set
     */
    public void setGeneralSymbolId(Integer generalSymbolId) {
        this.generalSymbolId = generalSymbolId;
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
        if (!(object instanceof SymbolDTO)) {
            return false;
        }
        SymbolDTO other = (SymbolDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + serial;
    }

    /**
     * @return the accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the symbolType
     */
    public Boolean getSymbolType() {
        if(symbolType == null){
            symbolType = Boolean.FALSE;
        }
        return symbolType;
    }

    /**
     * @param symbolType the symbolType to set
     */
    public void setSymbolType(Boolean symbolType) {
        this.symbolType = symbolType;
    }

}
