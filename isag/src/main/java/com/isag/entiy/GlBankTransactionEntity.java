/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.GlBank;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.views.InstTransactionTemp;
import com.isag.views.InstTransactionView;
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
    private Integer glYear;
    private BigDecimal rate;
    private BigDecimal rateBankFrom;
    private BigDecimal rateBankTo;

    private Integer chequeNumber;
    private Date chequeDate;
    private Date chequeDueDate;
    private Integer chequeStatus;

    private Integer serial;
    private Integer postFlag;
    private Integer hiddenId;
    private InstContract instContract;
    private InstContractDetail instContractDetail;
    private InstTransactionView instTransactionView;
    private InstTransactionTemp instTransactionTemp;
    private Integer serailParent;
    private String createdNameParent;
    private String createdByName;
    private String modifiedByName;
    private Integer documentSerial;
    private Boolean isDebit;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getValue() {
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
     * @return the instContract
     */
    public InstContract getInstContract() {
        return instContract;
    }

    /**
     * @param instContract the instContract to set
     */
    public void setInstContract(InstContract instContract) {
        this.instContract = instContract;
    }

    /**
     * @return the instContractDetail
     */
    public InstContractDetail getInstContractDetail() {
        return instContractDetail;
    }

    /**
     * @param instContractDetail the instContractDetail to set
     */
    public void setInstContractDetail(InstContractDetail instContractDetail) {
        this.instContractDetail = instContractDetail;
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
     * @return the instTransactionTemp
     */
    public InstTransactionTemp getInstTransactionTemp() {
        return instTransactionTemp;
    }

    /**
     * @param instTransactionTemp the instTransactionTemp to set
     */
    public void setInstTransactionTemp(InstTransactionTemp instTransactionTemp) {
        this.instTransactionTemp = instTransactionTemp;
    }

    /**
     * @return the modifiedByName
     */
    public String getModifiedByName() {
        return modifiedByName;
    }

    /**
     * @param modifiedByName the modifiedByName to set
     */
    public void setModifiedByName(String modifiedByName) {
        this.modifiedByName = modifiedByName;
    }

    /**
     * @return the documentSerial
     */
    public Integer getDocumentSerial() {
        return documentSerial;
    }

    /**
     * @param documentSerial the documentSerial to set
     */
    public void setDocumentSerial(Integer documentSerial) {
        this.documentSerial = documentSerial;
    }

    /**
     * @return the isDebit
     */
    public Boolean getIsDebit() {
        return isDebit;
    }

    /**
     * @param isDebit the isDebit to set
     */
    public void setIsDebit(Boolean isDebit) {
        this.isDebit = isDebit;
    }

}
