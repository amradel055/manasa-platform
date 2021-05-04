/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Toby
 */
public class InvoiceMovementReport implements Serializable {

    private Integer invoiceSerial;
    private String inventoryName;
    private String clientName;
    private Date deliveryDate;
    private Date clientDate;
    private Date galaryDate;
    private Date factoryDate;
    private Date date;
    private Integer type;
    private Integer id;
    private Integer idFactory;
    private Integer parent;
    private String parentCheck;
    private BigDecimal numberTobInvoice;
    private BigDecimal numberToFactory;
    private BigDecimal numberTobExitFactory;
    private BigDecimal numberTobgallary;
    private BigDecimal numberTobcustomer;

    public InvoiceMovementReport() {

    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getClientDate() {
        return clientDate;
    }

    public void setClientDate(Date clientDate) {
        this.clientDate = clientDate;
    }

    public Date getGalaryDate() {
        return galaryDate;
    }

    public void setGalaryDate(Date galaryDate) {
        this.galaryDate = galaryDate;
    }

    public Date getFactoryDate() {
        return factoryDate;
    }

    public void setFactoryDate(Date factoryDate) {
        this.factoryDate = factoryDate;
    }

    /**
     * @return the invoiceSerial
     */
    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    /**
     * @param invoiceSerial the invoiceSerial to set
     */
    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

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
     * @return the idFactory
     */
    public Integer getIdFactory() {
        return idFactory;
    }

    /**
     * @param idFactory the idFactory to set
     */
    public void setIdFactory(Integer idFactory) {
        this.idFactory = idFactory;
    }

    /**
     * @return the numberTobInvoice
     */
    public BigDecimal getNumberTobInvoice() {
        return numberTobInvoice;
    }

    /**
     * @param numberTobInvoice the numberTobInvoice to set
     */
    public void setNumberTobInvoice(BigDecimal numberTobInvoice) {
        this.numberTobInvoice = numberTobInvoice;
    }

    /**
     * @return the numberToFactory
     */
    public BigDecimal getNumberToFactory() {
        return numberToFactory;
    }

    /**
     * @param numberToFactory the numberToFactory to set
     */
    public void setNumberToFactory(BigDecimal numberToFactory) {
        this.numberToFactory = numberToFactory;
    }

    /**
     * @return the numberTobExitFactory
     */
    public BigDecimal getNumberTobExitFactory() {
        return numberTobExitFactory;
    }

    /**
     * @param numberTobExitFactory the numberTobExitFactory to set
     */
    public void setNumberTobExitFactory(BigDecimal numberTobExitFactory) {
        this.numberTobExitFactory = numberTobExitFactory;
    }

    /**
     * @return the numberTobgallary
     */
    public BigDecimal getNumberTobgallary() {
        return numberTobgallary;
    }

    /**
     * @param numberTobgallary the numberTobgallary to set
     */
    public void setNumberTobgallary(BigDecimal numberTobgallary) {
        this.numberTobgallary = numberTobgallary;
    }

    /**
     * @return the numberTobcustomer
     */
    public BigDecimal getNumberTobcustomer() {
        return numberTobcustomer;
    }

    /**
     * @param numberTobcustomer the numberTobcustomer to set
     */
    public void setNumberTobcustomer(BigDecimal numberTobcustomer) {
        this.numberTobcustomer = numberTobcustomer;
    }

    /**
     * @return the parent
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * @return the parentCheck
     */
    public String getParentCheck() {
        return parentCheck;
    }

    /**
     * @param parentCheck the parentCheck to set
     */
    public void setParentCheck(String parentCheck) {
        this.parentCheck = parentCheck;
    }

}
