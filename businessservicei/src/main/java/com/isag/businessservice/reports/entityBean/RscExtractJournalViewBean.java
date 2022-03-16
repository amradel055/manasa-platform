/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.entityBean;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class RscExtractJournalViewBean {

  
    private BigDecimal currentQty;
    private Date extractDate;


  

    /**
     * @return the currentQty
     */
    public BigDecimal getCurrentQty() {
        return currentQty;
    }

    /**
     * @param currentQty the currentQty to set
     */
    public void setCurrentQty(BigDecimal currentQty) {
        this.currentQty = currentQty;
    }

    /**
     * @return the extractDate
     */
    public Date getExtractDate() {
        return extractDate;
    }

    /**
     * @param extractDate the extractDate to set
     */
    public void setExtractDate(Date extractDate) {
        this.extractDate = extractDate;
    }

   

}
