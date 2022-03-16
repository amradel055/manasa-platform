/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.views.ChangePriceView;
import java.math.BigDecimal;

/**
 *
 * @author elsakr6
 */
public class ChangePriceViewEntity extends ChangePriceView {

    private BigDecimal newCost;

    public BigDecimal getNewCost() {
        return newCost;
    }

    public void setNewCost(BigDecimal newCost) {
        this.newCost = newCost;
    }
}
