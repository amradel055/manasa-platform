/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "journal_close_details")
@XmlRootElement
public class JournalCloseDetails extends BaseEntity{

  
    @Column(name = "image_name")
    private String imageName;
    
    @JoinColumn(name = "journal_id", referencedColumnName = "id")
    @ManyToOne
    private JournalClose journalId;
 

    public JournalCloseDetails() {
    }

    public JournalCloseDetails(Integer id) {
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
        if (!(object instanceof JournalCloseDetails)) {
            return false;
        }
        JournalCloseDetails other = (JournalCloseDetails) object;
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
     * @return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName the imageName to set
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * @return the jounalId
     */
    public JournalClose getJounalId() {
        return journalId;
    }

    /**
     * @param jounalId the jounalId to set
     */
    public void setJounalId(JournalClose jounalId) {
        this.journalId = jounalId;
    }


}
