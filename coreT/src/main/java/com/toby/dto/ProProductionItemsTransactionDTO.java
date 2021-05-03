/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author H
 */
public class ProProductionItemsTransactionDTO extends BaseEntityDTO {

    private Date creationDate;
    

    private Date modificationDate;

    private Integer serial;
    private Integer invoiceSerial;
    private Integer serialNumber;

    private BigDecimal number;
    private BigDecimal clothNumber;

    private BigDecimal productionStageCost;

    private Date date;
    private Date invoiceDate;
    private Date OrderDate;

    private Integer status;
    private String stageName;

    private Integer isDeleted;

    private InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailId;

    private ProProductionStagesDTO proProductionStagesId;

    private ProProductionTransactionDTO proProductionTransactionId;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getProductionStageCost() {
        return productionStageCost;
    }

    public void setProductionStageCost(BigDecimal productionStageCost) {
        this.productionStageCost = productionStageCost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public InvPurchaseInvoiceDetailDTO getInvPurchaseInvoiceDetailId() {
        return invPurchaseInvoiceDetailId;
    }

    public void setInvPurchaseInvoiceDetailId(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailId) {
        this.invPurchaseInvoiceDetailId = invPurchaseInvoiceDetailId;
    }

    public ProProductionStagesDTO getProProductionStagesId() {
        return proProductionStagesId;
    }

    public void setProProductionStagesId(ProProductionStagesDTO proProductionStagesId) {
        this.proProductionStagesId = proProductionStagesId;
    }

    public ProProductionTransactionDTO getProProductionTransactionId() {
        return proProductionTransactionId;
    }

    public void setProProductionTransactionId(ProProductionTransactionDTO proProductionTransactionId) {
        this.proProductionTransactionId = proProductionTransactionId;
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
        if (!(object instanceof ProProductionTransactionDTO)) {
            return false;
        }
        ProProductionTransactionDTO other = (ProProductionTransactionDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ProProductionItemsTransaction[ id=" + id + " ]";
    }

    /**
     * @return the stageName
     */
    public String getStageName() {
        if (getProProductionStagesId() != null) {
            stageName = getProProductionStagesId().getName();
        }

        return stageName;
    }

    /**
     * @param stageName the stageName to set
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * @return the serialNumber
     */
    public Integer getSerialNumber() {

        if (getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId() != null) {
            serialNumber = getInvPurchaseInvoiceDetailId().getInvPurchaseInvoiceId().getSerial();
        }
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
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
     * @return the clothNumber
     */
    public BigDecimal getClothNumber() {
        return clothNumber;
    }

    /**
     * @param clothNumber the clothNumber to set
     */
    public void setClothNumber(BigDecimal clothNumber) {
        this.clothNumber = clothNumber;
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
     * @return the OrderDate
     */
    public Date getOrderDate() {
        return OrderDate;
    }

    /**
     * @param OrderDate the OrderDate to set
     */
    public void setOrderDate(Date OrderDate) {
        this.OrderDate = OrderDate;
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
