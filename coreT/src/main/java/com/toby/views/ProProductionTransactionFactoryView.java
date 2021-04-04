/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.TobyUserDTO;
import java.util.Date;

/**
 *
 * @author m lapan
 */
public class ProProductionTransactionFactoryView {

    private Date date;
    private InvInventoryDTO galleryList;
    private ProGroupDTO status;
    private TobyUserDTO user;
    private String invoiceNumber;

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
     * @return the galleryList
     */
    public InvInventoryDTO getGalleryList() {
        return galleryList;
    }

    /**
     * @param galleryList the galleryList to set
     */
    public void setGalleryList(InvInventoryDTO galleryList) {
        this.galleryList = galleryList;
    }

    /**
     * @return the status
     */
    public ProGroupDTO getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ProGroupDTO status) {
        this.status = status;
    }

    /**
     * @return the user
     */
    public TobyUserDTO getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(TobyUserDTO user) {
        this.user = user;
    }

    /**
     * @return the invoiceNumber
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

}
