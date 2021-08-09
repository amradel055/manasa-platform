/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import com.isag.entity.InvInventory;
import com.isag.entity.InvPurchaseInvoiceDetail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hq002
 */
public class GLBankEntity {

   
    
    private Date paymentDate;
    
    private BigDecimal payee;
    

    

    /**
     * @return the paymentDate
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the payee
     */
    public BigDecimal getPayee() {
        return payee;
    }

    /**
     * @param payee the payee to set
     */
    public void setPayee(BigDecimal payee) {
        this.payee = payee;
    }

}
