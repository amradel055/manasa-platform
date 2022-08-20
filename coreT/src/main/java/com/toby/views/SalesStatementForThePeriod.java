/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author m lapan
 */
public class SalesStatementForThePeriod {

    private int serial;
    private Date invoiceDate;
    private Date dateFrom;
    private Date dateTo;
    private String code;
    private String name;
    private BigDecimal totalNet;
    private BigDecimal net;
    private InvInventoryDTO gallerySelected;
    private List<InvInventoryDTO> gallaryListSelected;
    private String invoiceStatus;
    private int invoiceId;
    private BigDecimal remain;
    private BigDecimal paid;
    private BigDecimal cash;
    private BigDecimal noticeDebit;
    private BigDecimal noticeCredit;
    private BigDecimal totalBeforeTax;
    private BigDecimal totalAfterTax;
    private BigDecimal tax;
    private BigDecimal invNoticeValue;
    private BigDecimal returnPurchaseValue;
    private String galleryName;
    private Integer organizationId;

    /**
     * @return the serial
     */
    public int getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(int serial) {
        this.serial = serial;
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

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

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
     * @return the totalNet
     */
    public BigDecimal getTotalNet() {
        if (totalNet == null) {
            totalNet = BigDecimal.ZERO;
        }
        return totalNet;
    }

    /**
     * @param totalNet the totalNet to set
     */
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        if (net == null) {
            net = BigDecimal.ZERO;
        }
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
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
     * @return the gallaryListSelected
     */
    public List<InvInventoryDTO> getGallaryListSelected() {
        if (gallaryListSelected == null) {
            gallaryListSelected = new ArrayList<>();
        }
        return gallaryListSelected;
    }

    /**
     * @param gallaryListSelected the gallaryListSelected to set
     */
    public void setGallaryListSelected(List<InvInventoryDTO> gallaryListSelected) {
        this.gallaryListSelected = gallaryListSelected;
    }

    /**
     * @return the invoiceStatus
     */
    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    /**
     * @param invoiceStatus the invoiceStatus to set
     */
    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    /**
     * @return the invoiceId
     */
    public int getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        if (remain == null) {
            remain = getNet().subtract(getReturnPurchaseValue()).add(getNoticeDebit()).subtract(getNoticeCredit()).subtract(getCash());
        }
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the paid
     */
    public BigDecimal getPaid() {
        if (paid == null) {
            paid = BigDecimal.ZERO;
        }
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(BigDecimal paid) {
        this.paid = paid;
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
     * @return the totalBeforeTax
     */
    public BigDecimal getTotalBeforeTax() {
        if (totalBeforeTax == null) {
            totalBeforeTax = getTotalNet();
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
     * @return the totalAfterTax
     */
    public BigDecimal getTotalAfterTax() {
        if (totalAfterTax == null) {
            totalAfterTax = getNet();
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
     * @return the tax
     */
    public BigDecimal getTax() {
        if (tax == null) {
            tax = getTotalAfterTax().subtract(getTotalBeforeTax());
        }
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    /**
     * @return the invNoticeValue
     */
    public BigDecimal getInvNoticeValue() {
        return invNoticeValue;
    }

    /**
     * @param invNoticeValue the invNoticeValue to set
     */
    public void setInvNoticeValue(BigDecimal invNoticeValue) {
        this.invNoticeValue = invNoticeValue;
    }

    /**
     * @return the returnPurchaseValue
     */
    public BigDecimal getReturnPurchaseValue() {
        if (returnPurchaseValue == null) {
            returnPurchaseValue = BigDecimal.ZERO;
        }
        return returnPurchaseValue;
    }

    /**
     * @param returnPurchaseValue the returnPurchaseValue to set
     */
    public void setReturnPurchaseValue(BigDecimal returnPurchaseValue) {
        this.returnPurchaseValue = returnPurchaseValue;
    }

    /**
     * @return the noticeDebit
     */
    public BigDecimal getNoticeDebit() {
        if (noticeDebit == null) {
            noticeDebit = BigDecimal.ZERO;
        }
        return noticeDebit;
    }

    /**
     * @param noticeDebit the noticeDebit to set
     */
    public void setNoticeDebit(BigDecimal noticeDebit) {
        this.noticeDebit = noticeDebit;
    }

    /**
     * @return the noticeCredit
     */
    public BigDecimal getNoticeCredit() {
        if (noticeCredit == null) {
            noticeCredit = BigDecimal.ZERO;
        }
        return noticeCredit;
    }

    /**
     * @param noticeCredit the noticeCredit to set
     */
    public void setNoticeCredit(BigDecimal noticeCredit) {
        this.noticeCredit = noticeCredit;
    }

    /**
     * @return the cash
     */
    public BigDecimal getCash() {
        if (cash == null) {
            cash = BigDecimal.ZERO;
        }
        return cash;
    }

    /**
     * @param cash the cash to set
     */
    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    /**
     * @return the organizationId
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

}
