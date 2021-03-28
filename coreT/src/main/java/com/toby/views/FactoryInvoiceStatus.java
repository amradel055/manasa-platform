/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author m lapan
 */
public class FactoryInvoiceStatus {

    /**
     * @return the Number
     */
    public BigDecimal getNumber() {
        return Number;
    }

    private Integer invoiceNumber;
    private String Gallery;
    private Date invoiceDate;
    private String clientName;
    private BigDecimal Number;
    private String Stage;
    private Date stageDate;
    private String Employee;

    /**
     * @return the invoiceNumber
     */
    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * @return the Gallery
     */
    public String getGallery() {
        return Gallery;
    }

    /**
     * @param Gallery the Gallery to set
     */
    public void setGallery(String Gallery) {
        this.Gallery = Gallery;
    }

    /**
     * @return the invoiceDate
     */
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * @param invoiceDate the invoiceDate to set
     */
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

   

    /**
     * @return the Stage
     */
    public String getStage() {
        return Stage;
    }

    /**
     * @param Stage the Stage to set
     */
    public void setStage(String Stage) {
        this.Stage = Stage;
    }

    /**
     * @return the stageDate
     */
    public Date getStageDate() {
        return stageDate;
    }

    /**
     * @param stageDate the stageDate to set
     */
    public void setStageDate(Date stageDate) {
        this.stageDate = stageDate;
    }


    /**
     * @return the Employee
     */
    public String getEmployee() {
        return Employee;
    }

    /**
     * @param Employee the Employee to set
     */
    public void setEmployee(String Employee) {
        this.Employee = Employee;
    }

    /**
     * @param Number the Number to set
     */
    public void setNumber(BigDecimal Number) {
        this.Number = Number;
    }

}
