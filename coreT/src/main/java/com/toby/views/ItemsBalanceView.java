/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "items_balance_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemsBalanceView.findAll", query = "SELECT i FROM ItemsBalanceView i")
    , @NamedQuery(name = "ItemsBalanceView.findByRowNum", query = "SELECT i FROM ItemsBalanceView i WHERE i.rowNum = :rowNum")
    , @NamedQuery(name = "ItemsBalanceView.findByDate", query = "SELECT i FROM ItemsBalanceView i WHERE i.date = :date")
    , @NamedQuery(name = "ItemsBalanceView.findByRemark", query = "SELECT i FROM ItemsBalanceView i WHERE i.remark = :remark")
    , @NamedQuery(name = "ItemsBalanceView.findByGlYear", query = "SELECT i FROM ItemsBalanceView i WHERE i.glYear = :glYear")
    , @NamedQuery(name = "ItemsBalanceView.findBySerial", query = "SELECT i FROM ItemsBalanceView i WHERE i.serial = :serial")
    , @NamedQuery(name = "ItemsBalanceView.findByGlValue", query = "SELECT i FROM ItemsBalanceView i WHERE i.glValue = :glValue")
    , @NamedQuery(name = "ItemsBalanceView.findByValueLocal", query = "SELECT i FROM ItemsBalanceView i WHERE i.valueLocal = :valueLocal")
    , @NamedQuery(name = "ItemsBalanceView.findByTransactionType", query = "SELECT i FROM ItemsBalanceView i WHERE i.transactionType = :transactionType")
    , @NamedQuery(name = "ItemsBalanceView.findByBankIdTo", query = "SELECT i FROM ItemsBalanceView i WHERE i.bankIdTo = :bankIdTo")
    , @NamedQuery(name = "ItemsBalanceView.findByBankIdfrom", query = "SELECT i FROM ItemsBalanceView i WHERE i.bankIdfrom = :bankIdfrom")
    , @NamedQuery(name = "ItemsBalanceView.findByBankNameTo", query = "SELECT i FROM ItemsBalanceView i WHERE i.bankNameTo = :bankNameTo")
    , @NamedQuery(name = "ItemsBalanceView.findByBankNameFrom", query = "SELECT i FROM ItemsBalanceView i WHERE i.bankNameFrom = :bankNameFrom")
    , @NamedQuery(name = "ItemsBalanceView.findByAccountIdTo", query = "SELECT i FROM ItemsBalanceView i WHERE i.accountIdTo = :accountIdTo")
    , @NamedQuery(name = "ItemsBalanceView.findByAccountIdFrom", query = "SELECT i FROM ItemsBalanceView i WHERE i.accountIdFrom = :accountIdFrom")
    , @NamedQuery(name = "ItemsBalanceView.findByBranchId", query = "SELECT i FROM ItemsBalanceView i WHERE i.branchId = :branchId")
    , @NamedQuery(name = "ItemsBalanceView.findByItemId", query = "SELECT i FROM ItemsBalanceView i WHERE i.itemId = :itemId")
    , @NamedQuery(name = "ItemsBalanceView.findByItemSerial", query = "SELECT i FROM ItemsBalanceView i WHERE i.itemSerial = :itemSerial")
    , @NamedQuery(name = "ItemsBalanceView.findByItemName", query = "SELECT i FROM ItemsBalanceView i WHERE i.itemName = :itemName")})
public class ItemsBalanceView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "gl_year")
    private Integer glYear;
    @Column(name = "serial")
    private Integer serial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "glValue")
    private BigDecimal glValue;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Column(name = "transactionType")
    private Integer transactionType;
    @Column(name = "bankIdTo")
    private Integer bankIdTo;
    @Column(name = "bankIdfrom")
    private Integer bankIdfrom;
    @Size(max = 45)
    @Column(name = "bankNameTo")
    private String bankNameTo;
    @Size(max = 45)
    @Column(name = "bankNameFrom")
    private String bankNameFrom;
    @Column(name = "accountIdTo")
    private Integer accountIdTo;
    @Column(name = "accountIdFrom")
    private Integer accountIdFrom;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "item_serial")
    private Integer itemSerial;
    @Size(max = 255)
    @Column(name = "item_name")
    private String itemName;

    public ItemsBalanceView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGlYear() {
        return glYear;
    }

    public void setGlYear(Integer glYear) {
        this.glYear = glYear;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getGlValue() {
        return glValue;
    }

    public void setGlValue(BigDecimal glValue) {
        this.glValue = glValue;
    }

    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getBankIdTo() {
        return bankIdTo;
    }

    public void setBankIdTo(Integer bankIdTo) {
        this.bankIdTo = bankIdTo;
    }

    public Integer getBankIdfrom() {
        return bankIdfrom;
    }

    public void setBankIdfrom(Integer bankIdfrom) {
        this.bankIdfrom = bankIdfrom;
    }

    public String getBankNameTo() {
        return bankNameTo;
    }

    public void setBankNameTo(String bankNameTo) {
        this.bankNameTo = bankNameTo;
    }

    public String getBankNameFrom() {
        return bankNameFrom;
    }

    public void setBankNameFrom(String bankNameFrom) {
        this.bankNameFrom = bankNameFrom;
    }

    public Integer getAccountIdTo() {
        return accountIdTo;
    }

    public void setAccountIdTo(Integer accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    public Integer getAccountIdFrom() {
        return accountIdFrom;
    }

    public void setAccountIdFrom(Integer accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getItemSerial() {
        return itemSerial;
    }

    public void setItemSerial(Integer itemSerial) {
        this.itemSerial = itemSerial;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
}
