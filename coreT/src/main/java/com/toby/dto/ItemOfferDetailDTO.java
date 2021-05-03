/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;

/**
 *
 * @author IT Shop
 */
public class ItemOfferDetailDTO  extends BaseEntityDTO {

    private BigDecimal cost;
    private BigDecimal costTrans;
    private BigDecimal costBefor;
    private BigDecimal costAfter;
    private BigDecimal costBeforTrans;
    private BigDecimal costAfterTrans;
    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;

    public ItemOfferDetailDTO() {
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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
        if (!(object instanceof ItemOfferDetailDTO)) {
            return false;
        }
        ItemOfferDetailDTO other = (ItemOfferDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ItemOffer[ id=" + id + " ]";
    }

    /**
     * @return the itemsBarcodesDetail
     */
    public ItemsBarcodesDetailsView getItemsBarcodesDetail() {
        return itemsBarcodesDetail;
    }

    /**
     * @param itemsBarcodesDetail the itemsBarcodesDetail to set
     */
    public void setItemsBarcodesDetail(ItemsBarcodesDetailsView itemsBarcodesDetail) {
        this.itemsBarcodesDetail = itemsBarcodesDetail;
    }

    /**
     * @return the itemsBarcodesDetailTrans
     */
    public ItemsBarcodesDetailsView getItemsBarcodesDetailTrans() {
        return itemsBarcodesDetailTrans;
    }

    /**
     * @param itemsBarcodesDetailTrans the itemsBarcodesDetailTrans to set
     */
    public void setItemsBarcodesDetailTrans(ItemsBarcodesDetailsView itemsBarcodesDetailTrans) {
        this.itemsBarcodesDetailTrans = itemsBarcodesDetailTrans;
    }

    /**
     * @return the costTrans
     */
    public BigDecimal getCostTrans() {
        return costTrans;
    }

    /**
     * @param costTrans the costTrans to set
     */
    public void setCostTrans(BigDecimal costTrans) {
        this.costTrans = costTrans;
    }

    /**
     * @return the costBefor
     */
    public BigDecimal getCostBefor() {
        return costBefor;
    }

    /**
     * @param costBefor the costBefor to set
     */
    public void setCostBefor(BigDecimal costBefor) {
        this.costBefor = costBefor;
    }

    /**
     * @return the costAfter
     */
    public BigDecimal getCostAfter() {
        return costAfter;
    }

    /**
     * @param costAfter the costAfter to set
     */
    public void setCostAfter(BigDecimal costAfter) {
        this.costAfter = costAfter;
    }

    /**
     * @return the costBeforTrans
     */
    public BigDecimal getCostBeforTrans() {
        return costBeforTrans;
    }

    /**
     * @param costBeforTrans the costBeforTrans to set
     */
    public void setCostBeforTrans(BigDecimal costBeforTrans) {
        this.costBeforTrans = costBeforTrans;
    }

    /**
     * @return the costAfterTrans
     */
    public BigDecimal getCostAfterTrans() {
        return costAfterTrans;
    }

    /**
     * @param costAfterTrans the costAfterTrans to set
     */
    public void setCostAfterTrans(BigDecimal costAfterTrans) {
        this.costAfterTrans = costAfterTrans;
    }
    
}
