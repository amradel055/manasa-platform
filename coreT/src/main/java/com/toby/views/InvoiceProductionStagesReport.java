package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class InvoiceProductionStagesReport implements Serializable {

    private Integer invoice;
    private Integer invInvoice;
    private Integer clothNumber;
    private Date date;
    private String client;
    private String clientName;
    private BigDecimal quantity;
    private String stage;
    private String empName;
    private BigDecimal number;

    public InvoiceProductionStagesReport() {

    }

    public Integer getInvoice() {
        return invoice;
    }

    public void setInvoice(Integer invoice) {
        this.invoice = invoice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * @return the invInvoice
     */
    public Integer getInvInvoice() {
        return invInvoice;
    }

    /**
     * @param invInvoice the invInvoice to set
     */
    public void setInvInvoice(Integer invInvoice) {
        this.invInvoice = invInvoice;
    }

    /**
     * @return the clothNumber
     */
    public Integer getClothNumber() {
        return clothNumber;
    }

    /**
     * @param clothNumber the clothNumber to set
     */
    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
    }

    /**
     * @return the number
     */
    public BigDecimal getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

}
