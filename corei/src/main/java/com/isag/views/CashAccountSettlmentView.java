/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "cash_account_settlment_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CashAccountSettlmentView.findAll", query = "SELECT c FROM CashAccountSettlmentView c"),
    @NamedQuery(name = "CashAccountSettlmentView.findByRowNum", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.rowNum = :rowNum"),
    @NamedQuery(name = "CashAccountSettlmentView.findByBanktransactionId", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.banktransactionId = :banktransactionId"),

    @NamedQuery(name = "CashAccountSettlmentView.findByCompanyId", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.companyId = :companyId"),
    @NamedQuery(name = "CashAccountSettlmentView.findByBranchId", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.branchId = :branchId"),
    @NamedQuery(name = "CashAccountSettlmentView.findByType", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.type = :type"),
    @NamedQuery(name = "CashAccountSettlmentView.findByDate", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.date = :date"),
    @NamedQuery(name = "CashAccountSettlmentView.findByValue", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.value = :value"),
    @NamedQuery(name = "CashAccountSettlmentView.findByValueLocal", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.valueLocal = :valueLocal"),
    @NamedQuery(name = "CashAccountSettlmentView.findByName", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.name = :name"),
    @NamedQuery(name = "CashAccountSettlmentView.findByCode", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.code = :code"),
    @NamedQuery(name = "CashAccountSettlmentView.findBySerial", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.serial = :serial"),
    @NamedQuery(name = "CashAccountSettlmentView.findByCurrencyName", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.currencyName = :currencyName"),
    @NamedQuery(name = "CashAccountSettlmentView.findBycurrencyCode", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.currencyCode = :currencyCode"),
    @NamedQuery(name = "CashAccountSettlmentView.findByRemark", query = "SELECT c FROM CashAccountSettlmentView c WHERE c.remark = :remark")})
public class CashAccountSettlmentView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "bank_transaction_Id")
    private Integer banktransactionId;

    @Column(name = "companyId")
    private Integer companyId;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "bankId")
    private Integer bankId;
    @Column(name = "type")
    private Integer type;
    @Column(name = "serial")
    private BigInteger serial;
    @Column(name = "paymentType")
    private Integer paymentType;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Size(max = 450)
    @Column(name = "name")
    private String name;
    @Size(max = 450)
    @Column(name = "code")
    private String code;
    @Size(max = 450)
    @Column(name = "currencyName")
    private String currencyName;
    @Size(max = 450)
    @Column(name = "currencyCode")
    private String currencyCode;
    @Column(name = "status")
    private Integer status;
    @Column(name = "type_id")
    private Integer typeId;
    @Size(max = 450)
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "ordered")
    private Integer ordered;
    @Size(max = 450)
    @Column(name = "remark2")
    private String remark2;
    @Column(name = "createdBy")
    private String createdBy;
    @Column(name = "contractSerial")
    private String contractSerial;
    @Column(name = "documentSerial")
    private Integer document_Serial;
    @Transient
    private Integer documentSerial;
    

    public CashAccountSettlmentView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getBanktransactionId() {
        return banktransactionId;
    }

    public void setBanktransactionId(Integer banktransactionId) {
        this.banktransactionId = banktransactionId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the paymentType
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
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
     * @return the serial
     */
    public BigInteger getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(BigInteger serial) {
        this.serial = serial;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return the ordered
     */
    public Integer getOrdered() {
        return ordered;
    }

    /**
     * @param ordered the ordered to set
     */
    public void setOrdered(Integer ordered) {
        this.ordered = ordered;
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
     * @return the typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
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
     * @return the document_Serial
     */
    public Integer getDocument_Serial() {
        return document_Serial;
    }

    /**
     * @param document_Serial the document_Serial to set
     */
    public void setDocument_Serial(Integer document_Serial) {
        this.document_Serial = document_Serial;
    }

}
