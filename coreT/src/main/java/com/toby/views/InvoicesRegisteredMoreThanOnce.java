/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvPurchaseInvoiceDTO;
import java.math.BigDecimal;

/**
 *
 * @author m lapan
 */
public class InvoicesRegisteredMoreThanOnce {

    private Integer serial;
    private String stageName;
    private String itemName;
    private BigDecimal workNumber;
    private BigDecimal originNumber;
    private InvPurchaseInvoiceDTO serialSelected;
   

    /**
     * @return the stageName
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * @param stageName the stageName to set
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the workNumber
     */
    public BigDecimal getWorkNumber() {
        return workNumber;
    }

    /**
     * @param workNumber the workNumber to set
     */
    public void setWorkNumber(BigDecimal workNumber) {
        this.workNumber = workNumber;
    }

    /**
     * @return the originNumber
     */
    public BigDecimal getOriginNumber() {
        return originNumber;
    }

    /**
     * @param originNumber the originNumber to set
     */
    public void setOriginNumber(BigDecimal originNumber) {
        this.originNumber = originNumber;
    }

    /**
     * @return the serialSelected
     */
    public InvPurchaseInvoiceDTO getSerialSelected() {
        return serialSelected;
    }

    /**
     * @param serialSelected the serialSelected to set
     */
    public void setSerialSelected(InvPurchaseInvoiceDTO serialSelected) {
        this.serialSelected = serialSelected;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

   

}
