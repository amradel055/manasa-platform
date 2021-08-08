/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.entity.*;
import java.util.Date;

/**
 *
 * @author m lapan
 */
public class OfferTextDTO extends BaseEntityDTO {

    private String text;
    private Date dateFrom;
    private Date dateTo;
    private InvInventoryDTO galaryDTO;
    private Integer galaryId;

    public OfferTextDTO() {
    }

    public OfferTextDTO(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getGalaryId() {
        return galaryId;
    }

    public void setGalaryId(Integer galaryId) {
        this.galaryId = galaryId;
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
        if (!(object instanceof OfferTextDTO)) {
            return false;
        }
        OfferTextDTO other = (OfferTextDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.OfferText[ id=" + id + " ]";
    }

    /**
     * @return the galaryDTO
     */
    public InvInventoryDTO getGalaryDTO() {
        return galaryDTO;
    }

    /**
     * @param galaryDTO the galaryDTO to set
     */
    public void setGalaryDTO(InvInventoryDTO galaryDTO) {
        this.galaryDTO = galaryDTO;
    }

}
