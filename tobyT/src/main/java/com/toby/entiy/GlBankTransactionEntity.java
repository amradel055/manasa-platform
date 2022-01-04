/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.dto.GlAccountDTO;
import com.toby.entity.GlBank;
import com.toby.views.InstTransactionView;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author WIN7
 */
public class GlBankTransactionEntity extends BaseEntity {

    private Date date;
    private BigDecimal value;
    private Integer paymentType;
    private String paymentTypeName;
    private Integer organizationType;
    private BigDecimal valueLocal;
    private String remark;
    private String remark2;
    private Integer transactionType;
    private Collection<GlBankTransactionDetailEntity> glBankTransactionDetailCollection;
    private GlBank glBank;
    private Integer invoiceId;
    private Integer invoiceSerial;
    private BigDecimal accountStatement;
    private BigDecimal remainInvoice;
    private Integer glYear;
    private BigDecimal rate;
    private BigDecimal rateBankFrom;
    private BigDecimal rateBankTo;
    private BigDecimal bankCommition;

    private Integer chequeNumber;
    private Date chequeDate;
    private Date chequeDueDate;
    private Integer chequeStatus;

    private Integer serial;
    private Integer postFlag;
    private Integer hiddenId;
    private InstTransactionView instTransactionView;
    private Integer serailParent;
    private String createdNameParent;
    private String createdByName;
    private GlAccountDTO costAccount;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        if(value == null){
            value = BigDecimal.ZERO;
        }
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
    }

    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Collection<GlBankTransactionDetailEntity> getGlBankTransactionDetailCollection() {
        return glBankTransactionDetailCollection;
    }

    public void setGlBankTransactionDetailCollection(Collection<GlBankTransactionDetailEntity> glBankTransactionDetailCollection) {
        this.glBankTransactionDetailCollection = glBankTransactionDetailCollection;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getRate() {
        if(rate == null){
            rate = BigDecimal.ONE;
        }
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the glBank
     */
    public GlBank getGlBank() {
        return glBank;
    }

    /**
     * @param glBank the glBank to set
     */
    public void setGlBank(GlBank glBank) {
        this.glBank = glBank;
    }

    public Integer getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(Integer chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public Date getChequeDueDate() {
        return chequeDueDate;
    }

    public void setChequeDueDate(Date chequeDueDate) {
        this.chequeDueDate = chequeDueDate;
    }

    public Integer getChequeStatus() {
        return chequeStatus;
    }

    public void setChequeStatus(Integer chequeStatus) {
        this.chequeStatus = chequeStatus;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    /**
     * @return the glYear
     */
    public Integer getGlYear() {
        return glYear;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYear(Integer glYear) {
        this.glYear = glYear;
    }

    /**
     * @return the remark2
     */
    public String getRemark2() {
        return remark2;
    }

    /**
     * @param remark2 the remark2 to set
     */
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    /**
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the hiddenId
     */
    public Integer getHiddenId() {
        return hiddenId;
    }

    /**
     * @param hiddenId the hiddenId to set
     */
    public void setHiddenId(Integer hiddenId) {
        this.hiddenId = hiddenId;
    }
    
    /**
     * @return the instTransactionView
     */
    public InstTransactionView getInstTransactionView() {
        return instTransactionView;
    }

    /**
     * @param instTransactionView the instTransactionView to set
     */
    public void setInstTransactionView(InstTransactionView instTransactionView) {
        this.instTransactionView = instTransactionView;
    }
    
    
    /**
     * @return the rateBankFrom
     */
    public BigDecimal getRateBankFrom() {
        return rateBankFrom;
    }

    /**
     * @param rateBankFrom the rateBankFrom to set
     */
    public void setRateBankFrom(BigDecimal rateBankFrom) {
        this.rateBankFrom = rateBankFrom;
    }

    /**
     * @return the rateBankTo
     */
    public BigDecimal getRateBankTo() {
        return rateBankTo;
    }

    /**
     * @param rateBankTo the rateBankTo to set
     */
    public void setRateBankTo(BigDecimal rateBankTo) {
        this.rateBankTo = rateBankTo;
    }

    /**
     * @return the serailParent
     */
    public Integer getSerailParent() {
        return serailParent;
    }

    /**
     * @param serailParent the serailParent to set
     */
    public void setSerailParent(Integer serailParent) {
        this.serailParent = serailParent;
    }

    /**
     * @return the createdNameParent
     */
    public String getCreatedNameParent() {
        return createdNameParent;
    }

    /**
     * @param createdNameParent the createdNameParent to set
     */
    public void setCreatedNameParent(String createdNameParent) {
        this.createdNameParent = createdNameParent;
    }

    /**
     * @return the createdByName
     */
    public String getCreatedByName() {
        return createdByName;
    }

    /**
     * @param createdByName the createdByName to set
     */
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    /**
     * @return the accountStatement
     */
    public BigDecimal getAccountStatement() {
        return accountStatement;
    }

    /**
     * @param accountStatement the accountStatement to set
     */
    public void setAccountStatement(BigDecimal accountStatement) {
        this.accountStatement = accountStatement;
    }

    /**
     * @return the remainInvoice
     */
    public BigDecimal getRemainInvoice() {
        if(remainInvoice == null){
            remainInvoice = BigDecimal.ZERO;
        }
        return remainInvoice;
    }

    /**
     * @param remainInvoice the remainInvoice to set
     */
    public void setRemainInvoice(BigDecimal remainInvoice) {
        this.remainInvoice = remainInvoice;
    }

    /**
     * @return the bankCommition
     */
    public BigDecimal getBankCommition() {
        return bankCommition;
    }

    /**
     * @param bankCommition the bankCommition to set
     */
    public void setBankCommition(BigDecimal bankCommition) {
        this.bankCommition = bankCommition;
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
     * @return the costAccount
     */
    public GlAccountDTO getCostAccount() {
        return costAccount;
    }

    /**
     * @param costAccount the costAccount to set
     */
    public void setCostAccount(GlAccountDTO costAccount) {
        this.costAccount = costAccount;
    }

}
