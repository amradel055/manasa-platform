/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; 
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "isag_notification_setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsagNotificationSetting.findAll", query = "SELECT i FROM IsagNotificationSetting i")
    , @NamedQuery(name = "IsagNotificationSetting.findById", query = "SELECT i FROM IsagNotificationSetting i WHERE i.id = :id")
    , @NamedQuery(name = "IsagNotificationSetting.findByPosition", query = "SELECT i FROM IsagNotificationSetting i WHERE i.position = :position")
    , @NamedQuery(name = "IsagNotificationSetting.findByTimeout", query = "SELECT i FROM IsagNotificationSetting i WHERE i.timeout = :timeout")
    , @NamedQuery(name = "IsagNotificationSetting.findByTheme", query = "SELECT i FROM IsagNotificationSetting i WHERE i.theme = :theme")
    , @NamedQuery(name = "IsagNotificationSetting.findBySound", query = "SELECT i FROM IsagNotificationSetting i WHERE i.sound = :sound")
    , @NamedQuery(name = "IsagNotificationSetting.findByCreationDate", query = "SELECT i FROM IsagNotificationSetting i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "IsagNotificationSetting.findByModificationDate", query = "SELECT i FROM IsagNotificationSetting i WHERE i.modificationDate = :modificationDate")})
public class IsagNotificationSetting extends BaseEntity {
 
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "position")
    private String position;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timeout")
    private BigDecimal timeout;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "theme")
    private String theme;
    @Column(name = "sound")
    private Boolean sound;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    

    public IsagNotificationSetting() {
    }

    public IsagNotificationSetting(Integer id) {
        this.id = id;
    }

    public IsagNotificationSetting(Integer id, String position, BigDecimal timeout, String theme) {
        this.id = id;
        this.position = position;
        this.timeout = timeout;
        this.theme = theme;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getTimeout() {
        return timeout;
    }

    public void setTimeout(BigDecimal timeout) {
        this.timeout = timeout;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Boolean getSound() {
        return sound;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
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
        if (!(object instanceof IsagNotificationSetting)) {
            return false;
        }
        IsagNotificationSetting other = (IsagNotificationSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.notification.IsagNotificationSetting[ id=" + id + " ]";
    }
    
}
