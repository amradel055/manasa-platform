/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author AhmedEssam
 */
public class CurrencyOperationDTO extends BaseEntityDTO{
    
     
    private Date operationDate;
    
    private String notes;
     
    private BigDecimal rate;
   
    private CurrencyDTO currencyId;
     

    public Date getOperationDate() {
        if (operationDate == null) {
            operationDate=new Date();
        }
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public CurrencyDTO getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(CurrencyDTO currencyId) {
        this.currencyId = currencyId;
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
        if (!(object instanceof CurrencyOperationDTO)) {
            return false;
        }
        CurrencyOperationDTO other = (CurrencyOperationDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.CurrencyOperation[ id=" + id + " ]";
    }
 
}
