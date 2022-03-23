/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.define.ScreenNameClassEnum;
import java.util.Date;

/**
 *
 * @author amr
 */

public class InvDailogueData extends BaseEntityDTO {

    private Integer serial;    
    private String organizationSiteName;
    private Date date;
    private String supplierInvoiceNumber;
    private Date supplierInvoiceDate;
    private String status;
    private String remarks;
    private ScreenNameClassEnum screenName;
    
    

    public InvDailogueData() {
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvDailogueData)) {
            return false;
        }
        InvDailogueData other = (InvDailogueData) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial + "";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the organizationSiteName
     */
    public String getOrganizationSiteName() {
        return organizationSiteName;
    }

    /**
     * @param organizationSiteName the organizationSiteName to set
     */
    public void setOrganizationSiteName(String organizationSiteName) {
        this.organizationSiteName = organizationSiteName;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the supplierInvoiceNumber
     */
    public String getSupplierInvoiceNumber() {
        return supplierInvoiceNumber;
    }

    /**
     * @param supplierInvoiceNumber the supplierInvoiceNumber to set
     */
    public void setSupplierInvoiceNumber(String supplierInvoiceNumber) {
        this.supplierInvoiceNumber = supplierInvoiceNumber;
    }

    /**
     * @return the supplierInvoiceDate
     */
    public Date getSupplierInvoiceDate() {
        return supplierInvoiceDate;
    }

    /**
     * @param supplierInvoiceDate the supplierInvoiceDate to set
     */
    public void setSupplierInvoiceDate(Date supplierInvoiceDate) {
        this.supplierInvoiceDate = supplierInvoiceDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the screenName
     */
    public ScreenNameClassEnum getScreenName() {
        return screenName;
    }

    /**
     * @param screenName the screenName to set
     */
    public void setScreenName(ScreenNameClassEnum screenName) {
        this.screenName = screenName;
    }

}
