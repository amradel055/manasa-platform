/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H
 */
@Entity
@Table(name = "pro_production_check_final")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionCheckFinal.findAll", query = "SELECT p FROM ProProductionCheckFinal p"),
    @NamedQuery(name = "ProProductionCheckFinal.findByRowNum", query = "SELECT p FROM ProProductionCheckFinal p WHERE p.rowNum = :rowNum"),
    @NamedQuery(name = "ProProductionCheckFinal.findByInvoiceId", query = "SELECT p FROM ProProductionCheckFinal p WHERE p.invoiceId = :invoiceId"),
    @NamedQuery(name = "ProProductionCheckFinal.findByItemId", query = "SELECT p FROM ProProductionCheckFinal p WHERE p.itemId = :itemId"),
    @NamedQuery(name = "ProProductionCheckFinal.findByProductionStagesId", query = "SELECT p FROM ProProductionCheckFinal p WHERE p.productionStagesId = :productionStagesId")})
public class ProProductionCheckFinal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "invoiceId")
    private Integer invoiceId;
    @Column(name = "itemId")
    private Integer itemId;
    @Column(name = "productionStagesId")
    private Integer productionStagesId;
     @Column(name = "serial")
    private Integer serial;

    public ProProductionCheckFinal() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getProductionStagesId() {
        return productionStagesId;
    }

    public void setProductionStagesId(Integer productionStagesId) {
        this.productionStagesId = productionStagesId;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    
}
