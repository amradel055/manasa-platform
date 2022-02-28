/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author hhhh
 */

public class MissingReport {

    private BigDecimal quantity;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal different;

  

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {

        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the high
     */
    public BigDecimal getHigh() {
        return high;
    }

    /**
     * @param high the high to set
     */
    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    /**
     * @return the low
     */
    public BigDecimal getLow() {
        return low;
    }

    /**
     * @param low the low to set
     */
    public void setLow(BigDecimal low) {
        this.low = low;
    }

    /**
     * @return the different
     */
    public BigDecimal getDifferent() {
        return different;
    }

    /**
     * @param different the different to set
     */
    public void setDifferent(BigDecimal different) {
        this.different = different;
    }

}
