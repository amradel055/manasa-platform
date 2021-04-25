/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author m lapan
 */
public class SalesReportByValueAdedDetails {

    private Integer invoiceNumber;
    private Integer invoiceType;
    private Date invoiceDate;
    private String galleryName;
    private String employeeName;
    private BigDecimal Discount;
    private BigDecimal totalAfterTax;
    private BigDecimal totalBeforeTax;
    private BigDecimal Tax;
    private InvInventoryDTO gallerySelected;
    private Date dateFrom;
    private Date dateTo;

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
     * @return the invoiceType
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
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
     * @return the galleryName
     */
    public String getGalleryName() {
        return galleryName;
    }

    /**
     * @param galleryName the galleryName to set
     */
    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the Discount
     */
    public BigDecimal getDiscount() {
        if (Discount == null) {
            return BigDecimal.ZERO;
        }
        return Discount;
    }

    /**
     * @param Discount the Discount to set
     */
    public void setDiscount(BigDecimal Discount) {
        this.Discount = Discount;
    }

    /**
     * @return the totalAfterTax
     */
    public BigDecimal getTotalAfterTax() {
        if (totalAfterTax == null) {
            return BigDecimal.ZERO;
        }
        return totalAfterTax;
    }

    /**
     * @param totalAfterTax the totalAfterTax to set
     */
    public void setTotalAfterTax(BigDecimal totalAfterTax) {
        this.totalAfterTax = totalAfterTax;
    }

    /**
     * @return the totalBeforeTax
     */
    public BigDecimal getTotalBeforeTax() {
        if (totalBeforeTax == null) {
            return BigDecimal.ZERO;
        }
        return totalBeforeTax;
    }

    /**
     * @param totalBeforeTax the totalBeforeTax to set
     */
    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }

    /**
     * @return the Tax
     */
    public BigDecimal getTax() {
        if (Tax == null) {
            Tax = getTotalBeforeTax().subtract(getTotalAfterTax());
        }
        return Tax;
    }

    /**
     * @param Tax the Tax to set
     */
    public void setTax(BigDecimal Tax) {
        this.Tax = Tax;
    }

    /**
     * @return the gallerySelected
     */
    public InvInventoryDTO getGallerySelected() {
        return gallerySelected;
    }

    /**
     * @param gallerySelected the gallerySelected to set
     */
    public void setGallerySelected(InvInventoryDTO gallerySelected) {
        this.gallerySelected = gallerySelected;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

}
