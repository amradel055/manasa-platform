/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.util.Date;


public class JournalCloseDTO extends BaseEntityDTO{
    

    private Date date ;
    private InvInventoryDTO galleryId;
 
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JournalCloseDTO)) {
            return false;
        }
        JournalCloseDTO other = (JournalCloseDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.entity.GlYear[ id=" + id + " ]";
    }

    /**
     * @return the date
     */
    public Date getDate() {
        if(date == null){
         date = new Date();
        }
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the galleryId
     */
    public InvInventoryDTO getGalleryId() {
        if(galleryId == null){
        galleryId = new InvInventoryDTO();
        }
        return galleryId;
    }

    /**
     * @param galleryId the galleryId to set
     */
    public void setGalleryId(InvInventoryDTO galleryId) {
        this.galleryId = galleryId;
    }
}
