/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;

public class InventorySetupDTO extends BaseEntityDTO {

    private Boolean negativeSell;
    private Boolean sellBuy;
    private Boolean invSystem;
    private Boolean sellAllow;
    private Boolean transfer;
    private Boolean dateSystem;
    private Integer reservationPeriod;    
    private Integer itemCoding;
    private Boolean defaultSalesTax;
    private Integer deliveryPeriod;
    private BigDecimal taxValue;

    public InventorySetupDTO() {
    }

    public InventorySetupDTO(Integer id) {
        this.id = id;
    }

    public InventorySetupDTO(Integer id, Date creationDate) {
        this.id = id;
    }

    public Boolean getNegativeSell() {
        return negativeSell;
    }

    public void setNegativeSell(Boolean negativeSell) {
        this.negativeSell = negativeSell;
    }

    public Boolean getSellBuy() {
        return sellBuy;
    }

    public void setSellBuy(Boolean sellBuy) {
        this.sellBuy = sellBuy;
    }

    public Boolean getInvSystem() {
        return invSystem;
    }

    public void setInvSystem(Boolean invSystem) {
        this.invSystem = invSystem;
    }

    public Boolean getSellAllow() {
        return sellAllow;
    }

    public void setSellAllow(Boolean sellAllow) {
        this.sellAllow = sellAllow;
    }

    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
        this.transfer = transfer;
    }

    public Boolean getDateSystem() {
        return dateSystem;
    }

    public void setDateSystem(Boolean dateSystem) {
        this.dateSystem = dateSystem;
    }

    public Integer getReservationPeriod() {
        return reservationPeriod;
    }

    public void setReservationPeriod(Integer reservationPeriod) {
        this.reservationPeriod = reservationPeriod;
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
        if (!(object instanceof InventorySetupDTO)) {
            return false;
        }
        InventorySetupDTO other = (InventorySetupDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.InventorySetup[ id=" + id + " ]";
    }

    public Integer getItemCoding() {
        return itemCoding;
    }

    public void setItemCoding(Integer itemCoding) {
        this.itemCoding = itemCoding;
    }

    public Boolean getDefaultSalesTax() {
        return defaultSalesTax;
    }

    public void setDefaultSalesTax(Boolean defaultSalesTax) {
        this.defaultSalesTax = defaultSalesTax;
    }

    /**
     * @return the deliveryPeriod
     */
    public Integer getDeliveryPeriod() {
        return deliveryPeriod;
    }

    /**
     * @param deliveryPeriod the deliveryPeriod to set
     */
    public void setDeliveryPeriod(Integer deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

    /**
     * @return the taxValue
     */
    public BigDecimal getTaxValue() {
        return taxValue;
    }

    /**
     * @param taxValue the taxValue to set
     */
    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

}
