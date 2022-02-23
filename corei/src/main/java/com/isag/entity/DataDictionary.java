/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "data_dictionary")
public class DataDictionary extends BaseEntity {

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "property_key")
    private String propertyKey;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "property_value")
    private String propertyValue;

    @JoinColumn(name = "lang", referencedColumnName = "id")
    @ManyToOne()
    private Symbol lang;

    /**
     * @return the propertyKey
     */
    public String getPropertyKey() {
        return propertyKey;
    }

    /**
     * @param propertyKey the propertyKey to set
     */
    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    /**
     * @return the propertyValue
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * @param propertyValue the propertyValue to set
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * @return the lang
     */
    public Symbol getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(Symbol lang) {
        this.lang = lang;
    }

}
