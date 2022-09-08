/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import com.toby.views.SalesStatementForThePeriod;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class InvInvoiceDTOApi extends BaseEntityDTOAPI {


    private Integer customerId;
    private String customerName;
    private String customerMobile;

    private String gallaryName;
    private Date invoiceDate;
    private Date date;
    private Date takeDate;
    private String status;
    private String invoiceNumber;
    private String remarks;
    private Integer gallaryId;
    private Integer discountType;
    private Integer invDelegatorId;
    private Integer invInventoryId;

    private String invDelegatorName;
    private BigDecimal discount;
    private BigDecimal discHalala;
    
    private Date dueDate;
    private Integer dueperiod;
    private Integer pricetype;
    private Integer proof;
    private String offerCopoun;
    private String gallaryDelivery;
    private Integer gallaryDeliveryId;
    private String deliveryPlaceName;
    private Integer deliveryPlaceId;
    private String customerCode;
    private String customerEmail;
    private String customerOfferCompanyId;
    private BigDecimal length;
    private BigDecimal shoulder;
    private BigDecimal step;
    private BigDecimal customerBalance;
    private String invInventoryName;
    private Integer glBankId;
    private String glBankName;
    private Integer serialTax;
    private BigDecimal net;
    private String qrCode;

    private List<InvDetailsDTOAPI> invoiceDetailApiList;
    
    private List<GlPayDTOAPI> glPayDTOList ; 
    
    private SalesStatementForThePeriod salesStatementForThePeriod ;
    
    
    
    
    private BigDecimal totalNetAfterDiscount;
    private BigDecimal finalNet;
    private BigDecimal taxvalue;

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

    /**
     * @return the serialTax
     */
    public Integer getSerialTax() {
        return serialTax;
    }

    /**
     * @param serialTax the serialTax to set
     */
    public void setSerialTax(Integer serialTax) {
        this.serialTax = serialTax;
    }

    /**
     * @return the invDelegatorName
     */
    public String getInvDelegatorName() {
        return invDelegatorName;
    }

    /**
     * @param invDelegatorName the invDelegatorName to set
     */
    public void setInvDelegatorName(String invDelegatorName) {
        this.invDelegatorName = invDelegatorName;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the discHalala
     */
    public BigDecimal getDiscHalala() {
        return discHalala;
    }

    /**
     * @param discHalala the discHalala to set
     */
    public void setDiscHalala(BigDecimal discHalala) {
        this.discHalala = discHalala;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the dueperiod
     */
    public Integer getDueperiod() {
        return dueperiod;
    }

    /**
     * @param dueperiod the dueperiod to set
     */
    public void setDueperiod(Integer dueperiod) {
        this.dueperiod = dueperiod;
    }

    /**
     * @return the pricetype
     */
    public Integer getPricetype() {
        return pricetype;
    }

    /**
     * @param pricetype the pricetype to set
     */
    public void setPricetype(Integer pricetype) {
        this.pricetype = pricetype;
    }

    /**
     * @return the proof
     */
    public Integer getProof() {
        return proof;
    }

    /**
     * @param proof the proof to set
     */
    public void setProof(Integer proof) {
        this.proof = proof;
    }

    /**
     * @return the offerCopoun
     */
    public String getOfferCopoun() {
        return offerCopoun;
    }

    /**
     * @param offerCopoun the offerCopoun to set
     */
    public void setOfferCopoun(String offerCopoun) {
        this.offerCopoun = offerCopoun;
    }

    /**
     * @return the gallaryDelivery
     */
    public String getGallaryDelivery() {
        return gallaryDelivery;
    }

    /**
     * @param gallaryDelivery the gallaryDelivery to set
     */
    public void setGallaryDelivery(String gallaryDelivery) {
        this.gallaryDelivery = gallaryDelivery;
    }

    /**
     * @return the gallaryDeliveryId
     */
    public Integer getGallaryDeliveryId() {
        return gallaryDeliveryId;
    }

    /**
     * @param gallaryDeliveryId the gallaryDeliveryId to set
     */
    public void setGallaryDeliveryId(Integer gallaryDeliveryId) {
        this.gallaryDeliveryId = gallaryDeliveryId;
    }

    /**
     * @return the deliveryPlaceName
     */
    public String getDeliveryPlaceName() {
        return deliveryPlaceName;
    }

    /**
     * @param deliveryPlaceName the deliveryPlaceName to set
     */
    public void setDeliveryPlaceName(String deliveryPlaceName) {
        this.deliveryPlaceName = deliveryPlaceName;
    }

    /**
     * @return the deliveryPlaceId
     */
    public Integer getDeliveryPlaceId() {
        return deliveryPlaceId;
    }

    /**
     * @param deliveryPlaceId the deliveryPlaceId to set
     */
    public void setDeliveryPlaceId(Integer deliveryPlaceId) {
        this.deliveryPlaceId = deliveryPlaceId;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the customerEmail
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * @param customerEmail the customerEmail to set
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * @return the length
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /**
     * @return the shoulder
     */
    public BigDecimal getShoulder() {
        return shoulder;
    }

    /**
     * @param shoulder the shoulder to set
     */
    public void setShoulder(BigDecimal shoulder) {
        this.shoulder = shoulder;
    }

    /**
     * @return the step
     */
    public BigDecimal getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(BigDecimal step) {
        this.step = step;
    }

    /**
     * @return the customerBalance
     */
    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    /**
     * @param customerBalance the customerBalance to set
     */
    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }

    /**
     * @return the invInventoryName
     */
    public String getInvInventoryName() {
        return invInventoryName;
    }

    /**
     * @param invInventoryName the invInventoryName to set
     */
    public void setInvInventoryName(String invInventoryName) {
        this.invInventoryName = invInventoryName;
    }

    /**
     * @return the glBankId
     */
    public Integer getGlBankId() {
        return glBankId;
    }

    /**
     * @param glBankId the glBankId to set
     */
    public void setGlBankId(Integer glBankId) {
        this.glBankId = glBankId;
    }

    /**
     * @return the glBankName
     */
    public String getGlBankName() {
        return glBankName;
    }

    /**
     * @param glBankName the glBankName to set
     */
    public void setGlBankName(String glBankName) {
        this.glBankName = glBankName;
    }

    /**
     * @return the customerOfferCompanyId
     */
    public String getCustomerOfferCompanyId() {
        return customerOfferCompanyId;
    }

    /**
     * @param customerOfferCompanyId the customerOfferCompanyId to set
     */
    public void setCustomerOfferCompanyId(String customerOfferCompanyId) {
        this.customerOfferCompanyId = customerOfferCompanyId;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the salesStatementForThePeriod
     */
    public SalesStatementForThePeriod getSalesStatementForThePeriod() {
        return salesStatementForThePeriod;
    }

    /**
     * @param salesStatementForThePeriod the salesStatementForThePeriod to set
     */
    public void setSalesStatementForThePeriod(SalesStatementForThePeriod salesStatementForThePeriod) {
        this.salesStatementForThePeriod = salesStatementForThePeriod;
    }

    /**
     * @return the glPayDTOList
     */
    public List<GlPayDTOAPI> getGlPayDTOList() {
        return glPayDTOList;
    }

    /**
     * @param glPayDTOList the glPayDTOList to set
     */
    public void setGlPayDTOList(List<GlPayDTOAPI> glPayDTOList) {
        this.glPayDTOList = glPayDTOList;
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
     * @return the totalNetAfterDiscount
     */
    public BigDecimal getTotalNetAfterDiscount() {
        return totalNetAfterDiscount;
    }

    /**
     * @param totalNetAfterDiscount the totalNetAfterDiscount to set
     */
    public void setTotalNetAfterDiscount(BigDecimal totalNetAfterDiscount) {
        this.totalNetAfterDiscount = totalNetAfterDiscount;
    }

    /**
     * @return the finalNet
     */
    public BigDecimal getFinalNet() {
        return finalNet;
    }

    /**
     * @param finalNet the finalNet to set
     */
    public void setFinalNet(BigDecimal finalNet) {
        this.finalNet = finalNet;
    }

    /**
     * @return the taxvalue
     */
    public BigDecimal getTaxvalue() {
        return taxvalue;
    }

    /**
     * @param taxvalue the taxvalue to set
     */
    public void setTaxvalue(BigDecimal taxvalue) {
        this.taxvalue = taxvalue;
    }

    /**
     * @return the qrCode
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * @param qrCode the qrCode to set
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
