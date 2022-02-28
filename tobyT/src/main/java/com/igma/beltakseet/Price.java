/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.beltakseet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author User
 */
@Named
@ViewScoped
public class Price implements Serializable{
    
    private BigDecimal total;
    private BigDecimal month6;
    private BigDecimal month12;
    private BigDecimal month18;

    
    public void calc(){
        setMonth6((((total.divide(new BigDecimal(100))).multiply(new BigDecimal(10))).add(total)).divide(new BigDecimal(6),0,RoundingMode.UP));
        setMonth12((((total.divide(new BigDecimal(100))).multiply(new BigDecimal(20))).add(total)).divide(new BigDecimal(12),0,RoundingMode.UP));
        setMonth18((((total.divide(new BigDecimal(100))).multiply(new BigDecimal(30))).add(total)).divide(new BigDecimal(18),0,RoundingMode.UP));
    }
    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the month6
     */
    public BigDecimal getMonth6() {
        return month6;
    }

    /**
     * @param month6 the month6 to set
     */
    public void setMonth6(BigDecimal month6) {
        this.month6 = month6;
    }

    /**
     * @return the month12
     */
    public BigDecimal getMonth12() {
        return month12;
    }

    /**
     * @param month12 the month12 to set
     */
    public void setMonth12(BigDecimal month12) {
        this.month12 = month12;
    }

    /**
     * @return the month18
     */
    public BigDecimal getMonth18() {
        return month18;
    }

    /**
     * @param month18 the month18 to set
     */
    public void setMonth18(BigDecimal month18) {
        this.month18 = month18;
    }
}
