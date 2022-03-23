/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "crm_setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CrmSetting.findAll", query = "SELECT c FROM CrmSetting c")
    , @NamedQuery(name = "CrmSetting.findById", query = "SELECT c FROM CrmSetting c WHERE c.id = :id")
    , @NamedQuery(name = "CrmSetting.findByShowEvent", query = "SELECT c FROM CrmSetting c WHERE c.showEvent = :showEvent")
    , @NamedQuery(name = "CrmSetting.findByCreationDate", query = "SELECT c FROM CrmSetting c WHERE c.creationDate = :creationDate")
    , @NamedQuery(name = "CrmSetting.findByModificationDate", query = "SELECT c FROM CrmSetting c WHERE c.modificationDate = :modificationDate")})
public class CrmSetting extends BaseEntity  {
    
    @Column(name = "showEvent")
    private Boolean showEvent;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public CrmSetting() {
    }

    public CrmSetting(Integer id) {
        this.id = id;
    }

  
    public Boolean getShowEvent() {
        return showEvent;
    }

    public void setShowEvent(Boolean showEvent) {
        this.showEvent = showEvent;
    }

  
    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof CrmSetting)) {
            return false;
        }
        CrmSetting other = (CrmSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.CrmSetting[ id=" + id + " ]";
    }
    
}
