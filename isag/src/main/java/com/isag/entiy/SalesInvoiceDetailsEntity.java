/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.InvItem;
import java.math.BigDecimal;

/**
 *
 * @author WIN7
 */
public class SalesInvoiceDetailsEntity extends BaseEntity {

    private Integer salesInvoiceId;
    private BigDecimal quantity;
    private BigDecimal weight;
    private BigDecimal cost;
    private BigDecimal discount;
    private BigDecimal net;
    private BigDecimal total;
    private InvItem invItem;
    private Integer unitId;
    private BigDecimal totalLocal;

    public Integer getSalesInvoiceId() {
        return salesInvoiceId;
    }

    public void setSalesInvoiceId(Integer salesInvoiceId) {
        this.salesInvoiceId = salesInvoiceId;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public InvItem getInvItem() {
        return invItem;
    }

    public void setInvItem(InvItem invItem) {
        this.invItem = invItem;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getTotalLocal() {
        return totalLocal;
    }

    public void setTotalLocal(BigDecimal totalLocal) {
        this.totalLocal = totalLocal;
    }

    /**
     * @return the weight
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
