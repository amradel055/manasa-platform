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
public class InvStripDetailDTO extends BaseEntityDTO {

   // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal actualAmount;

    private BigDecimal bookBalance;

    private BigDecimal difference;

    private InvItemDTO invItemId;

    private InvStripDTO invStripId;

    public BigDecimal getActualAmount() {
        if (actualAmount == null) {
            actualAmount = BigDecimal.ZERO;
        }
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getBookBalance() {
        if (bookBalance == null) {
            bookBalance = BigDecimal.ZERO;
        }
        return bookBalance;
    }

    public void setBookBalance(BigDecimal bookBalance) {
        this.bookBalance = bookBalance;
    }

    public BigDecimal getDifference() {
        difference = getActualAmount().subtract(getBookBalance());
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public InvItemDTO getInvItemId() {
        return invItemId;
    }

    public void setInvItemId(InvItemDTO invItemId) {
        this.invItemId = invItemId;
    }

    public InvStripDTO getInvStripId() {
        return invStripId;
    }

    public void setInvStripId(InvStripDTO invStripId) {
        this.invStripId = invStripId;
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
        if (!(object instanceof InvStripDetailDTO)) {
            return false;
        }
        InvStripDetailDTO other = (InvStripDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.InvStripDetail[ id=" + id + " ]";
    }

}
