package com.toby.views;

import com.toby.bean.BankBalanceReportBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SubAccountSummaryReport implements Serializable {

    private Integer id;
    private Integer bankId;
    private String bankName;
    private Date date;
    private Integer generalJournalId;
    private Integer generalDecument;
    private String symbolName;
    private String generalStatement;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private String remark;
    private String remark2;
    private Integer code;
    private String currencyName;
    private String currencyCode;
    private String transactionType;
    private String serial;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal openingBalance;
    private Integer transactionTypeNumber;
    private String createdBy;

    private String contractSerial;

    private String customerName;
    private String customerCode;
    private String customerMobile;
    private Integer invoiceNumber;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    public Integer getGeneralDecument() {
        return generalDecument;
    }

    public void setGeneralDecument(Integer generalDecument) {
        this.generalDecument = generalDecument;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getGeneralStatement() {
        return generalStatement;
    }

    public void setGeneralStatement(String generalStatement) {
        this.generalStatement = generalStatement;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the bankId
     */
    public Integer getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the totalDebit
     */
    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    /**
     * @param totalDebit the totalDebit to set
     */
    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    /**
     * @return the totalCredit
     */
    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    /**
     * @param totalCredit the totalCredit to set
     */
    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    /**
     * @return the code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return the currencyName
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * @param currencyName the currencyName to set
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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
     * @return the transactionType
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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
     * @return the transactionTypeNumber
     */
    public Integer getTransactionTypeNumber() {
        return transactionTypeNumber;
    }

    /**
     * @param transactionTypeNumber the transactionTypeNumber to set
     */
    public void setTransactionTypeNumber(Integer transactionTypeNumber) {
        this.transactionTypeNumber = transactionTypeNumber;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the contractSerial
     */
    public String getContractSerial() {
        return contractSerial;
    }

    /**
     * @param contractSerial the contractSerial to set
     */
    public void setContractSerial(String contractSerial) {
        this.contractSerial = contractSerial;
    }

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

 

}
