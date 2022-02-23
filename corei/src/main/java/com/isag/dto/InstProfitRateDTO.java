/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class InstProfitRateDTO extends BaseEntityDTO {

    private BigDecimal rate;
    private Integer monthNumber;
    private Boolean chBoolean = true;

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    
    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the monthNumber
     */
    public Integer getMonthNumber() {
        return monthNumber;
    }

    /**
     * @param monthNumber the monthNumber to set
     */
    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
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
        if (!(object instanceof InstProfitRateDTO)) {
            return false;
        }
        InstProfitRateDTO other = (InstProfitRateDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HR.InstProfitRate[ id=" + id + " ]";
    }

    /**
     * @return the chBoolean
     */
    public Boolean getChBoolean() {
        return chBoolean;
    }

    /**
     * @param chBoolean the chBoolean to set
     */
    public void setChBoolean(Boolean chBoolean) {
        this.chBoolean = chBoolean;
    }

}
