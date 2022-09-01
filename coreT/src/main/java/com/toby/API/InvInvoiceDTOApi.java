/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.dto.*;
import java.util.Date;
import java.util.List;

public class InvInvoiceDTOApi extends BaseEntityDTO {

    private String customerName;
    private String customerMobile;
    private String gallaryName;
    private Date invoiceDate;
    private Date takeDate;
    private String status;
    private String invoiceNumber;
    private String remarks;
    private Integer customerId;
    private Integer gallaryId;
    private Integer discountType;
    private Integer invDelegatorId;
    private Integer invInventoryId;
    private List<InvDetailsDTOAPI> invoiceDetailApiList;

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the gallaryName
     */
    public String getGallaryName() {
        return gallaryName;
    }

    /**
     * @param gallaryName the gallaryName to set
     */
    public void setGallaryName(String gallaryName) {
        this.gallaryName = gallaryName;
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
     * @return the takeDate
     */
    public Date getTakeDate() {
        return takeDate;
    }

    /**
     * @param takeDate the takeDate to set
     */
    public void setTakeDate(Date takeDate) {
        this.takeDate = takeDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the invoiceNumber
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the customerMobile
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * @param customerMobile the customerMobile to set
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the invoiceDetailApiList
     */
    public List<InvDetailsDTOAPI> getInvoiceDetailApiList() {
        return invoiceDetailApiList;
    }

    /**
     * @param invoiceDetailApiList the invoiceDetailApiList to set
     */
    public void setInvoiceDetailApiList(List<InvDetailsDTOAPI> invoiceDetailApiList) {
        this.invoiceDetailApiList = invoiceDetailApiList;
    }

    /**
     * @return the gallaryId
     */
    public Integer getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(Integer gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the discountType
     */
    public Integer getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType the discountType to set
     */
    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    /**
     * @return the invDelegatorId
     */
    public Integer getInvDelegatorId() {
        return invDelegatorId;
    }

    /**
     * @param invDelegatorId the invDelegatorId to set
     */
    public void setInvDelegatorId(Integer invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
    }

    /**
     * @return the invInventoryId
     */
    public Integer getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(Integer invInventoryId) {
        this.invInventoryId = invInventoryId;
    }
}
