/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amr
 */
public class GlBankTransactionDTO extends BaseEntityDTO {

    private Integer serial;
    private Date date;
    private Date dateHegry;
    private BigDecimal value;
    private Integer postFlag;
    private Integer paymentType;
    private Integer organizationType;
    private Integer glYear;
    private BigDecimal valueLocal;
    private String remark;
    private String remark2;
    private Integer transactionType;
    private List<GlBankTransactionDetailDTO> glBankTransactionDetailList;
    private GlBankDTO glBankId;
    private SymbolDTO documentTypeId;
    private Integer generalJournalId;
    private Integer installmentId;
    private Integer invoiceId;
    private BigDecimal rate;
    private Integer chequeNumber;
    private Date chequeDate;
    private Date chequeDueDate;
    private Integer chequeStatus;
    private GlBankTransactionDTO parent;

    private String paymentTypeName;
    private String createdByName;
    private Integer documentSerial;
    private String transactionTypeName;
    private BigDecimal credit;
    private BigDecimal debit;
    private BigDecimal balance;
    private BigDecimal openingBalance;
    private Integer listOrder;

    public GlBankTransactionDTO() {
    }

    public GlBankTransactionDTO(Integer id) {
        this.id = id;
    }

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

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GlBankTransactionDTO)) {
            return false;
        }
        GlBankTransactionDTO other = (GlBankTransactionDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.GlBankTransaction[ id=" + id + " ]";
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
     * @return the parent
     */
    public GlBankTransactionDTO getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(GlBankTransactionDTO parent) {
        this.parent = parent;
    }

    /**
     * @return the glBankId
     */
    public GlBankDTO getGlBankId() {
        return glBankId;
    }

    /**
     * @param glBankId the glBankId to set
     */
    public void setGlBankId(GlBankDTO glBankId) {
        this.glBankId = glBankId;
    }

    /**
     * @return the documentTypeId
     */
    public SymbolDTO getDocumentTypeId() {
        return documentTypeId;
    }

    /**
     * @param documentTypeId the documentTypeId to set
     */
    public void setDocumentTypeId(SymbolDTO documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    /**
     * @return the generalJournalId
     */
    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    /**
     * @param generalJournalId the generalJournalId to set
     */
    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    /**
     * @return the installmentId
     */
    public Integer getInstallmentId() {
        return installmentId;
    }

    /**
     * @param installmentId the installmentId to set
     */
    public void setInstallmentId(Integer installmentId) {
        this.installmentId = installmentId;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the glBankTransactionDetailList
     */
    public List<GlBankTransactionDetailDTO> getGlBankTransactionDetailList() {
        return glBankTransactionDetailList;
    }

    /**
     * @param glBankTransactionDetailList the glBankTransactionDetailList to set
     */
    public void setGlBankTransactionDetailList(List<GlBankTransactionDetailDTO> glBankTransactionDetailList) {
        this.glBankTransactionDetailList = glBankTransactionDetailList;
    }

    /**
     * @return the paymentTypeName
     */
    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    /**
     * @param paymentTypeName the paymentTypeName to set
     */
    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
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
     * @return the transactionTypeName
     */
    public String getTransactionTypeName() {
        return transactionTypeName;
    }

    /**
     * @param transactionTypeName the transactionTypeName to set
     */
    public void setTransactionTypeName(String transactionTypeName) {
        this.transactionTypeName = transactionTypeName;
    }

    /**
     * @return the credit
     */
    public BigDecimal getCredit() {
        return credit;
    }

    /**
     * @param credit the credit to set
     */
    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    /**
     * @return the debit
     */
    public BigDecimal getDebit() {
        return debit;
    }

    /**
     * @param debit the debit to set
     */
    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the openingBalance
     */
    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    /**
     * @param openingBalance the openingBalance to set
     */
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    /**
     * @return the listOrder
     */
    public Integer getListOrder() {
        return listOrder;
    }

    /**
     * @param listOrder the listOrder to set
     */
    public void setListOrder(Integer listOrder) {
        this.listOrder = listOrder;
    }

    /**
     * @return the dateHegry
     */
    public Date getDateHegry() {
        return dateHegry;
    }

    /**
     * @param dateHegry the dateHegry to set
     */
    public void setDateHegry(Date dateHegry) {
        this.dateHegry = dateHegry;
    }

}
