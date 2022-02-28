/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.Date;

public class InvoiceProductionStadesDTO  {

    private Integer invoiceId;
    private Integer invoiceSerial;
    private Date invoiceDate;
    private Integer itemId;
    private Integer itemParentId;
    private String itemParentname;
    private Integer stageId;
    private String stageName;
    private Integer branchId;

    public InvoiceProductionStadesDTO() {
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the invoiceSerial
     */
    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    /**
     * @param invoiceSerial the invoiceSerial to set
     */
    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    /**
     * @return the invoiceDate
     */
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate the invoiceDate to set
     */
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * @return the itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the itemParentId
     */
    public Integer getItemParentId() {
        return itemParentId;
    }

    /**
     * @param itemParentId the itemParentId to set
     */
    public void setItemParentId(Integer itemParentId) {
        this.itemParentId = itemParentId;
    }

    /**
     * @return the itemParentname
     */
    public String getItemParentname() {
        return itemParentname;
    }

    /**
     * @param itemParentname the itemParentname to set
     */
    public void setItemParentname(String itemParentname) {
        this.itemParentname = itemParentname;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the stageId
     */
    public Integer getStageId() {
        return stageId;
    }

    /**
     * @param stageId the stageId to set
     */
    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

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

    
}
