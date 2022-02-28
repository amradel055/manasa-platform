/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H
 */
@Entity
@Table(name = "find_purchase_to production")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FindPurchaseToProduction.findAll", query = "SELECT f FROM FindPurchaseToProduction f"),
    @NamedQuery(name = "FindPurchaseToProduction.findById", query = "SELECT f FROM FindPurchaseToProduction f WHERE f.id = :id"),
    @NamedQuery(name = "FindPurchaseToProduction.findBySerial", query = "SELECT f FROM FindPurchaseToProduction f WHERE f.serial = :serial")})
public class FindPurchaseToProduction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Column(name = "serial")
    private Integer serial;

    public FindPurchaseToProduction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    
}
