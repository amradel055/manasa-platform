/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author amr
 */
public class InvItemPriceDTO extends BaseEntityDTO {

    private BigDecimal minPriceMen;
    private BigDecimal maxPriceMen;
    private BigDecimal minPriceYoung;
    private BigDecimal maxPriceYoung;
    private InvInventoryDTO gallaryId;
    private InvItemDTO itemId;

    public InvItemPriceDTO() {
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
        if (!(object instanceof InvItemPriceDTO)) {
            return false;
        }
        InvItemPriceDTO other = (InvItemPriceDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.InvNotice[ id=" + id + " ]";
    }

    /**
     * @return the minPriceMen
     */
    public BigDecimal getMinPriceMen() {
        return minPriceMen;
    }

    /**
     * @param minPriceMen the minPriceMen to set
     */
    public void setMinPriceMen(BigDecimal minPriceMen) {
        this.minPriceMen = minPriceMen;
    }

    /**
     * @return the maxPriceMen
     */
    public BigDecimal getMaxPriceMen() {
        return maxPriceMen;
    }

    /**
     * @param maxPriceMen the maxPriceMen to set
     */
    public void setMaxPriceMen(BigDecimal maxPriceMen) {
        this.maxPriceMen = maxPriceMen;
    }

    /**
     * @return the minPriceYoung
     */
    public BigDecimal getMinPriceYoung() {
        return minPriceYoung;
    }

    /**
     * @param minPriceYoung the minPriceYoung to set
     */
    public void setMinPriceYoung(BigDecimal minPriceYoung) {
        this.minPriceYoung = minPriceYoung;
    }

    /**
     * @return the maxPriceYoung
     */
    public BigDecimal getMaxPriceYoung() {
        return maxPriceYoung;
    }

    /**
     * @param maxPriceYoung the maxPriceYoung to set
     */
    public void setMaxPriceYoung(BigDecimal maxPriceYoung) {
        this.maxPriceYoung = maxPriceYoung;
    }

    /**
     * @return the gallaryId
     */
    public InvInventoryDTO getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(InvInventoryDTO gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the itemId
     */
    public InvItemDTO getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(InvItemDTO itemId) {
        this.itemId = itemId;
    }

}
