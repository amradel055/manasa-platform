/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.math.BigDecimal;

/**
 *
 * @author IT Shop
 */
public class TestDetail {
    
    private BigDecimal number;
    private BigDecimal price;
    private BigDecimal totalCost;

    /**
     * @return the number
     */
    public BigDecimal getNumber() {
        if(number == null){
            number = BigDecimal.ZERO;
        }
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        if(price == null){
            price = BigDecimal.ZERO;
        }
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        if(totalCost == null){
            totalCost = BigDecimal.ZERO;
        }
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    
}
