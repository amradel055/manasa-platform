/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import java.math.BigDecimal;

/**
 *
 * @author ahmed
 */
public class IsagNotificationSettingDTO extends BaseEntityDTO {
    
    
    private String position;
    private BigDecimal timeout;
    private String theme;
    private Boolean sound;

    public IsagNotificationSettingDTO() {
    }

    public IsagNotificationSettingDTO(Integer id) {
        this.id = id;
    }

    public IsagNotificationSettingDTO(Integer id, String position, BigDecimal timeout, String theme) {
        this.id = id;
        this.position = position;
        this.timeout = timeout;
        this.theme = theme;
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
    
   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IsagNotificationSettingDTO)) {
            return false;
        }
        IsagNotificationSettingDTO other = (IsagNotificationSettingDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  id + " ";
    }
    
}
