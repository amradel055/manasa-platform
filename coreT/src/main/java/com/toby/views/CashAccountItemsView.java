/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "cash_account_items_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CashAccountItemsView.findAll", query = "SELECT c FROM CashAccountItemsView c")
    , @NamedQuery(name = "CashAccountItemsView.findByRowNum", query = "SELECT c FROM CashAccountItemsView c WHERE c.rowNum = :rowNum")
    , @NamedQuery(name = "CashAccountItemsView.findByBanktransactionId", query = "SELECT c FROM CashAccountItemsView c WHERE c.banktransactionId = :banktransactionId")
    , @NamedQuery(name = "CashAccountItemsView.findByCompanyId", query = "SELECT c FROM CashAccountItemsView c WHERE c.companyId = :companyId")
    , @NamedQuery(name = "CashAccountItemsView.findByBranchId", query = "SELECT c FROM CashAccountItemsView c WHERE c.branchId = :branchId")
    , @NamedQuery(name = "CashAccountItemsView.findByBankId", query = "SELECT c FROM CashAccountItemsView c WHERE c.bankId = :bankId")
    , @NamedQuery(name = "CashAccountItemsView.findByType", query = "SELECT c FROM CashAccountItemsView c WHERE c.type = :type")
    , @NamedQuery(name = "CashAccountItemsView.findByPaymentType", query = "SELECT c FROM CashAccountItemsView c WHERE c.paymentType = :paymentType")
    , @NamedQuery(name = "CashAccountItemsView.findByDate", query = "SELECT c FROM CashAccountItemsView c WHERE c.date = :date")
    , @NamedQuery(name = "CashAccountItemsView.findBySerial", query = "SELECT c FROM CashAccountItemsView c WHERE c.serial = :serial")
    , @NamedQuery(name = "CashAccountItemsView.findByValue", query = "SELECT c FROM CashAccountItemsView c WHERE c.value = :value")
    , @NamedQuery(name = "CashAccountItemsView.findByValueLocal", query = "SELECT c FROM CashAccountItemsView c WHERE c.valueLocal = :valueLocal")
    , @NamedQuery(name = "CashAccountItemsView.findByRemark", query = "SELECT c FROM CashAccountItemsView c WHERE c.remark = :remark")
    , @NamedQuery(name = "CashAccountItemsView.findByRemark2", query = "SELECT c FROM CashAccountItemsView c WHERE c.remark2 = :remark2")
    , @NamedQuery(name = "CashAccountItemsView.findByName", query = "SELECT c FROM CashAccountItemsView c WHERE c.name = :name")
    , @NamedQuery(name = "CashAccountItemsView.findByCode", query = "SELECT c FROM CashAccountItemsView c WHERE c.code = :code")
    , @NamedQuery(name = "CashAccountItemsView.findByCurrencyName", query = "SELECT c FROM CashAccountItemsView c WHERE c.currencyName = :currencyName")
    , @NamedQuery(name = "CashAccountItemsView.findByCurrencyCode", query = "SELECT c FROM CashAccountItemsView c WHERE c.currencyCode = :currencyCode")
    , @NamedQuery(name = "CashAccountItemsView.findBySymbolId", query = "SELECT c FROM CashAccountItemsView c WHERE c.symbolId = :symbolId")
    , @NamedQuery(name = "CashAccountItemsView.findByItemName", query = "SELECT c FROM CashAccountItemsView c WHERE c.itemName = :itemName")
    , @NamedQuery(name = "CashAccountItemsView.findByItemSerial", query = "SELECT c FROM CashAccountItemsView c WHERE c.itemSerial = :itemSerial")
    , @NamedQuery(name = "CashAccountItemsView.findByStatus", query = "SELECT c FROM CashAccountItemsView c WHERE c.status = :status")
    , @NamedQuery(name = "CashAccountItemsView.findByTypeId", query = "SELECT c FROM CashAccountItemsView c WHERE c.typeId = :typeId")
    , @NamedQuery(name = "CashAccountItemsView.findByTypeName", query = "SELECT c FROM CashAccountItemsView c WHERE c.typeName = :typeName")
    , @NamedQuery(name = "CashAccountItemsView.findByOrdered", query = "SELECT c FROM CashAccountItemsView c WHERE c.ordered = :ordered")})
public class CashAccountItemsView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bank_transaction_Id")
    private int banktransactionId;
    @Column(name = "companyId")
    private Integer companyId;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "bankId")
    private Integer bankId;
    @Column(name = "type")
    private Integer type;
    @Column(name = "paymentType")
    private Integer paymentType;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "serial")
    private String serial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Size(max = 450)
    @Column(name = "remark2")
    private String remark2;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 45)
    @Column(name = "currencyName")
    private String currencyName;
    @Size(max = 5)
    @Column(name = "currencyCode")
    private String currencyCode;
    @Column(name = "symbol_id")
    private Integer symbolId;
    @Size(max = 255)
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_serial")
    private Integer itemSerial;
    @Column(name = "status")
    private Integer status;
    @Column(name = "type_id")
    private Integer typeId;
    @Size(max = 7)
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "ordered")
    private Integer ordered;

    public CashAccountItemsView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getBanktransactionId() {
        return banktransactionId;
    }

    public void setBanktransactionId(int banktransactionId) {
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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(Integer symbolId) {
        this.symbolId = symbolId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemSerial() {
        return itemSerial;
    }

    public void setItemSerial(Integer itemSerial) {
        this.itemSerial = itemSerial;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getOrdered() {
        return ordered;
    }

    public void setOrdered(Integer ordered) {
        this.ordered = ordered;
    }
    
}
