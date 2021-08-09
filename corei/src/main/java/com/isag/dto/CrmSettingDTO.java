/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

/**
 *
 * @author ahmed
 */
public class CrmSettingDTO extends BaseEntityDTO {

    private Boolean showEvent;

   
    /**
     * @return the showEvent
     */
    public Boolean getShowEvent() {
        if (showEvent == null) {
            showEvent = Boolean.TRUE;
        }

        return showEvent;
    }

    /**
     * @param showEvent the showEvent to set
     */
    public void setShowEvent(Boolean showEvent) {
        this.showEvent = showEvent;
    }

}
