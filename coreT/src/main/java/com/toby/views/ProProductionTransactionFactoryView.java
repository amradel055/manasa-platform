/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserDTO;
import java.util.Date;

/**
 *
 * @author m lapan
 */
public class ProProductionTransactionFactoryView {

    private Date date;
    private InvInventoryDTO gallerySelected;
    private ProProductionStagesDTO status;
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
     * @return the gallerySelected
     */
    public InvInventoryDTO getGallerySelected() {
        return gallerySelected;
    }

    /**
     * @param gallerySelected the gallerySelected to set
     */
    public void setGallerySelected(InvInventoryDTO gallerySelected) {
        this.gallerySelected = gallerySelected;
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

    /**
     * @return the status
     */
    public ProProductionStagesDTO getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ProProductionStagesDTO status) {
        this.status = status;
    }

}
