/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.searchBean;

import com.isag.dto.IsagUserDTO;
import java.util.Date;

/**
 *
 * @author khale
 */
public class InvPurchaseInvoiceSearchBean {
    
   
    private Date dateFrom;
    private Date dateTo;
    private IsagUserDTO cashierFrom;
    private IsagUserDTO cashierTo;
    
 
   
    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the cashierFrom
     */
    public IsagUserDTO getCashierFrom() {
        return cashierFrom;
    }

    /**
     * @param cashierFrom the cashierFrom to set
     */
    public void setCashierFrom(IsagUserDTO cashierFrom) {
        this.cashierFrom = cashierFrom;
    }

    /**
     * @return the cashierTo
     */
    public IsagUserDTO getCashierTo() {
        return cashierTo;
    }

    /**
     * @param cashierTo the cashierTo to set
     */
    public void setCashierTo(IsagUserDTO cashierTo) {
        this.cashierTo = cashierTo;
    }

   
}
