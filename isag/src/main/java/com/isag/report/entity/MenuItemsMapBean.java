/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report.entity;

import com.isag.entity.GlAccount;

/**
 *
 * @author ahmed
 */
public class MenuItemsMapBean {

    private Integer id;
    private GlAccount glAccount;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the glAccount
     */
    public GlAccount getGlAccount() {
        return glAccount;
    }

    /**
     * @param glAccount the glAccount to set
     */
    public void setGlAccount(GlAccount glAccount) {
        this.glAccount = glAccount;
    }

}
