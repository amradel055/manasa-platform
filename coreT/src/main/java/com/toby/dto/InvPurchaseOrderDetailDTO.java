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

public class InvPurchaseOrderDetailDTO extends BaseEntityDTO {

   
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal discountRate;
    private BigDecimal total;
    private BigDecimal finalQuantity;
    private InvPurchaseOrderDTO invPurchaseOrderId;
    private InvItemDTO itemId;
    private SymbolDTO unitId;
    private Integer status;
    private String itemBarcode;
    private BigDecimal net;
    private Integer serial;
    private BigDecimal screwing;
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getFinalQuantity() {
        return finalQuantity;
    }

    public void setFinalQuantity(BigDecimal finalQuantity) {
        this.finalQuantity = finalQuantity;
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
        if (!(object instanceof InvPurchaseOrderDetailDTO)) {
            return false;
        }
        InvPurchaseOrderDetailDTO other = (InvPurchaseOrderDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getItemId().getName() +" "+getItemId().getCode() ;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @return the invPurchaseOrderId
     */
    public InvPurchaseOrderDTO getInvPurchaseOrderId() {
        return invPurchaseOrderId;
    }

    /**
     * @param invPurchaseOrderId the invPurchaseOrderId to set
     */
    public void setInvPurchaseOrderId(InvPurchaseOrderDTO invPurchaseOrderId) {
        this.invPurchaseOrderId = invPurchaseOrderId;
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

    /**
     * @return the unitId
     */
    public SymbolDTO getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(SymbolDTO unitId) {
        this.unitId = unitId;
    }
}
