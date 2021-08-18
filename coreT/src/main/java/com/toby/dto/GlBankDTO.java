/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GlBankDTO extends BaseEntityDTO implements Comparable<GlBankDTO> , Serializable{

    private String name;
    private Integer code;
    private BigDecimal openinngBalance;
    private Date dateOpeninngBalance;
    private Date dateHegry;
    private Integer accountId;
    private Integer type;
    private Integer currencyId;
    private Integer raghiPoint;
    private BigDecimal commition;
    private BigDecimal rate;

    public GlBankDTO() {
    }

    public GlBankDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        if (!(object instanceof GlBankDTO)) {
            return false;
        }
        GlBankDTO other = (GlBankDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    public BigDecimal getOpeninngBalance() {
        return openinngBalance;
    }

    public void setOpeninngBalance(BigDecimal openinngBalance) {
        this.openinngBalance = openinngBalance;
    }

    public Date getDateOpeninngBalance() {
        return dateOpeninngBalance;
    }

    public void setDateOpeninngBalance(Date dateOpeninngBalance) {
        this.dateOpeninngBalance = dateOpeninngBalance;
    }

    /**
     * @return the dateHegry
     */
    public Date getDateHegry() {
        return dateHegry;
    }

    /**
     * @param dateHegry the dateHegry to set
     */
    public void setDateHegry(Date dateHegry) {
        this.dateHegry = dateHegry;
    }

    /**
     * @return the raghiPoint
     */
    public Integer getRaghiPoint() {
        return raghiPoint;
    }

    /**
     * @param raghiPoint the raghiPoint to set
     */
    public void setRaghiPoint(Integer raghiPoint) {
        this.raghiPoint = raghiPoint;
    }

    /**
     * @return the commition
     */
    public BigDecimal getCommition() {
        return commition;
    }

    /**
     * @param commition the commition to set
     */
    public void setCommition(BigDecimal commition) {
        this.commition = commition;
    }

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

    @Override
    public int compareTo(GlBankDTO o) {
        return this.getCode().compareTo(o.getCode());
    }

}
