/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Branch;
import com.isag.entity.InvItem;
import com.isag.entity.InvOpenningBalanceItem;
import com.isag.entity.Symbol;
import java.math.BigDecimal;

/**
 *
 * @author hhhh
 */
public class InvOpeningBalanceItemDetailEntity extends BaseEntity {

    private Symbol unitId;
    private BigDecimal screwing;
    private BigDecimal quantity;
    private BigDecimal cost;
    private BigDecimal net;
    private Branch branchId;
    private InvItem itemId;
    private InvItem itemIdTrans;
    private InvOpenningBalanceItem balanceItemId;

    public Symbol getUnitId() {
        return unitId;
    }

    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
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

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public InvOpenningBalanceItem getBalanceItemId() {
        return balanceItemId;
    }

    public void setBalanceItemId(InvOpenningBalanceItem balanceItemId) {
        this.balanceItemId = balanceItemId;
    }

    public InvItem getItemIdTrans() {
        return itemIdTrans;
    }

    public void setItemIdTrans(InvItem itemIdTrans) {
        this.itemIdTrans = itemIdTrans;
    }
}
