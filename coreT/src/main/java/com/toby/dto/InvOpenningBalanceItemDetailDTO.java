/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author omar nezam
 */
public class InvOpenningBalanceItemDetailDTO extends BaseEntityDTO{
   
    private SymbolDTO unitId;
   
    private BigDecimal screwing;
  
    private BigDecimal quantity;
   
    private BigDecimal cost;
   
    private BigDecimal net;
   

   
    private InvItemDTO itemId;
    
    private Integer serial;
   
    private InvOpenningBalanceItemDTO balanceItemId;

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvOpenningBalanceItemDetailDTO)) {
            return false;
        }
        InvOpenningBalanceItemDetailDTO other = (InvOpenningBalanceItemDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial != null ? serial.toString() : "";
    }

    public SymbolDTO getUnitId() {
        return unitId;
    }

    public void setUnitId(SymbolDTO unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal scrawing) {
        this.screwing = scrawing;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    

    public InvItemDTO getItemId() {
        return itemId;
    }

    public void setItemId(InvItemDTO itemId) {
        this.itemId = itemId;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public InvOpenningBalanceItemDTO getBalanceItemId() {
        return balanceItemId;
    }

    public void setBalanceItemId(InvOpenningBalanceItemDTO balanceItemId) {
        this.balanceItemId = balanceItemId;
    }
}
