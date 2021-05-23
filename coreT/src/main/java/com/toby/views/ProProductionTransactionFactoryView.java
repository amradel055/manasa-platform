/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author m lapan
 */
public class ProProductionTransactionFactoryView {

    private Date date;
    private Date dateTo;
    private InvInventoryDTO gallerySelected;
    private List<InvInventoryDTO> gallerySelectedList;
    private ProProductionStagesDTO status;
    private TobyUserDTO user;
    private Integer invoiceNumber;

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
        if (gallerySelected == null) {
            gallerySelected = new InvInventoryDTO();
        }
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

    /**
     * @return the invoiceNumber
     */
    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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
     * @return the gallerySelectedList
     */
    public List<InvInventoryDTO> getGallerySelectedList() {
        if (gallerySelectedList == null) {
            gallerySelectedList = new ArrayList<>();
        }
        return gallerySelectedList;
    }

    /**
     * @param gallerySelectedList the gallerySelectedList to set
     */
    public void setGallerySelectedList(List<InvInventoryDTO> gallerySelectedList) {
        this.gallerySelectedList = gallerySelectedList;
    }

}
