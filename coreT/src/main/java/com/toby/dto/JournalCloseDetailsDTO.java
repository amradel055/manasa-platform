/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;


import com.toby.entity.JournalClose;


public class JournalCloseDetailsDTO extends BaseEntityDTO{
    

    private String imageName;
    private JournalClose journalId;
    
    

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JournalCloseDetailsDTO)) {
            return false;
        }
        JournalCloseDetailsDTO other = (JournalCloseDetailsDTO) object;
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
        if(journalId == null){
        journalId = new JournalClose();
        }
        return journalId;
    }

    /**
     * @param jounalId the jounalId to set
     */
    public void setJounalId(JournalClose jounalId) {
        this.journalId = jounalId;
    }

  
}
