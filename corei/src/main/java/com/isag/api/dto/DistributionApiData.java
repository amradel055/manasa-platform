/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.api.dto;

import java.math.BigDecimal;

/**
 *
 * @author ahmedEssam
 */
public class DistributionApiData {

    private Integer invoiceSalesNumbers;
    private Integer invoicePurchaseNumbers;
    private BigDecimal invoiceSalesValues;
    private BigDecimal invoicePurchaseValues;

    /**
     * @return the invoiceSalesNumbers
     */
    public Integer getInvoiceSalesNumbers() {
        return invoiceSalesNumbers;
    }

    /**
     * @param invoiceSalesNumbers the invoiceSalesNumbers to set
     */
    public void setInvoiceSalesNumbers(Integer invoiceSalesNumbers) {
        this.invoiceSalesNumbers = invoiceSalesNumbers;
    }

    /**
     * @return the invoicePurchaseNumbers
     */
    public Integer getInvoicePurchaseNumbers() {
        return invoicePurchaseNumbers;
    }

    /**
     * @param invoicePurchaseNumbers the invoicePurchaseNumbers to set
     */
    public void setInvoicePurchaseNumbers(Integer invoicePurchaseNumbers) {
        this.invoicePurchaseNumbers = invoicePurchaseNumbers;
    }

    /**
     * @return the invoiceSalesValues
     */
    public BigDecimal getInvoiceSalesValues() {
        return invoiceSalesValues;
    }

    /**
     * @param invoiceSalesValues the invoiceSalesValues to set
     */
    public void setInvoiceSalesValues(BigDecimal invoiceSalesValues) {
        this.invoiceSalesValues = invoiceSalesValues;
    }

    /**
     * @return the invoicePurchaseValues
     */
    public BigDecimal getInvoicePurchaseValues() {
        return invoicePurchaseValues;
    }

    /**
     * @param invoicePurchaseValues the invoicePurchaseValues to set
     */
    public void setInvoicePurchaseValues(BigDecimal invoicePurchaseValues) {
        this.invoicePurchaseValues = invoicePurchaseValues;
    }
    
    
}
