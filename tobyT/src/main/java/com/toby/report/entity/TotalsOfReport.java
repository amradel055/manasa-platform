/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class TotalsOfReport {

    private BigDecimal debitValue = BigDecimal.ZERO;
    private BigDecimal creditValue = BigDecimal.ZERO;
    private BigDecimal balanceValue = BigDecimal.ZERO;
    private BigDecimal firstValue = BigDecimal.ZERO;
    private BigDecimal secondValue = BigDecimal.ZERO;
    private BigDecimal debitTotalValue = BigDecimal.ZERO;
    private BigDecimal creditTotalValue = BigDecimal.ZERO;
    private BigDecimal balanceTotalValue = BigDecimal.ZERO;
    private BigDecimal firstTotalValue = BigDecimal.ZERO;
    private BigDecimal secondTotalValue = BigDecimal.ZERO;

    /**
     * @return the debitValue
     */
    public BigDecimal getDebitValue() {
        return debitValue;
    }

    /**
     * @param debitValue the debitValue to set
     */
    public void setDebitValue(BigDecimal debitValue) {
        this.debitValue = debitValue;
    }

    /**
     * @return the creditValue
     */
    public BigDecimal getCreditValue() {
        return creditValue;
    }

    /**
     * @param creditValue the creditValue to set
     */
    public void setCreditValue(BigDecimal creditValue) {
        this.creditValue = creditValue;
    }

    /**
     * @return the balanceValue
     */
    public BigDecimal getBalanceValue() {
        return balanceValue;
    }

    /**
     * @param balanceValue the balanceValue to set
     */
    public void setBalanceValue(BigDecimal balanceValue) {
        this.balanceValue = balanceValue;
    }

    /**
     * @return the firstValue
     */
    public BigDecimal getFirstValue() {
        return firstValue;
    }

    /**
     * @param firstValue the firstValue to set
     */
    public void setFirstValue(BigDecimal firstValue) {
        this.firstValue = firstValue;
    }

    /**
     * @return the secondValue
     */
    public BigDecimal getSecondValue() {
        return secondValue;
    }

    /**
     * @param secondValue the secondValue to set
     */
    public void setSecondValue(BigDecimal secondValue) {
        this.secondValue = secondValue;
    }

    /**
     * @return the debitTotalValue
     */
    public BigDecimal getDebitTotalValue() {
        return debitTotalValue;
    }

    /**
     * @param debitTotalValue the debitTotalValue to set
     */
    public void setDebitTotalValue(BigDecimal debitTotalValue) {
        this.debitTotalValue = debitTotalValue;
    }

    /**
     * @return the creditTotalValue
     */
    public BigDecimal getCreditTotalValue() {
        return creditTotalValue;
    }

    /**
     * @param creditTotalValue the creditTotalValue to set
     */
    public void setCreditTotalValue(BigDecimal creditTotalValue) {
        this.creditTotalValue = creditTotalValue;
    }

    /**
     * @return the balanceTotalValue
     */
    public BigDecimal getBalanceTotalValue() {
        return balanceTotalValue;
    }

    /**
     * @param balanceTotalValue the balanceTotalValue to set
     */
    public void setBalanceTotalValue(BigDecimal balanceTotalValue) {
        this.balanceTotalValue = balanceTotalValue;
    }

    /**
     * @return the firstTotalValue
     */
    public BigDecimal getFirstTotalValue() {
        return firstTotalValue;
    }

    /**
     * @param firstTotalValue the firstTotalValue to set
     */
    public void setFirstTotalValue(BigDecimal firstTotalValue) {
        this.firstTotalValue = firstTotalValue;
    }

    /**
     * @return the secondTotalValue
     */
    public BigDecimal getSecondTotalValue() {
        return secondTotalValue;
    }

    /**
     * @param secondTotalValue the secondTotalValue to set
     */
    public void setSecondTotalValue(BigDecimal secondTotalValue) {
        this.secondTotalValue = secondTotalValue;
    }

}
