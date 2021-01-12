/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hq004
 */
@Entity
@Table(name = "toby_screen")
public class TobyScreen extends BaseEntity{
    
    @Column(name = "serial")
    private Integer serial;
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(min = 1, max = 200)
    @Column(name = "namear")
    private String namear;
    @Size(max = 200)
    @Column(name = "tab_ar")
    private String tabAr;
    @Size(max = 200)
    @Column(name = "tab_en")
    private String tabEn;
    @Size(max = 200)
    @Column(name = "module_en")
    private String moduleEn;
    @Size(max = 200)
    @Column(name = "module_ar")
    private String moduleAr;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the namear
     */
    public String getNamear() {
        return namear;
    }

    /**
     * @param namear the namear to set
     */
    public void setNamear(String namear) {
        this.namear = namear;
    }
    
    public String getTabAr() {
        return tabAr;
    }

    public void setTabAr(String tabAr) {
        this.tabAr = tabAr;
    }

    public String getTabEn() {
        return tabEn;
    }

    public void setTabEn(String tabEn) {
        this.tabEn = tabEn;
    }

    public String getModuleEn() {
        return moduleEn;
    }

    public void setModuleEn(String moduleEn) {
        this.moduleEn = moduleEn;
    }

    public String getModuleAr() {
        return moduleAr;
    }

    public void setModuleAr(String moduleAr) {
        this.moduleAr = moduleAr;
    }
    
    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    
}
