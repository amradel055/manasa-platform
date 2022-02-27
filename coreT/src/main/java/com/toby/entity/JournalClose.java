/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "journal_close")
@XmlRootElement
public class JournalClose extends BaseEntity{

  
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @JoinColumn(name = "gallery_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory galleryId;
 

    public JournalClose() {
    }

    public JournalClose(Integer id) {
        this.id = id;
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
        if (!(object instanceof JournalClose)) {
            return false;
        }
        JournalClose other = (JournalClose) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.GlYear[ id=" + id + " ]";
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
     * @return the galleryId
     */
    public InvInventory getGalleryId() {
        return galleryId;
    }

    /**
     * @param galleryId the galleryId to set
     */
    public void setGalleryId(InvInventory galleryId) {
        this.galleryId = galleryId;
    }

}
