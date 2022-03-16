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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "analytical_ratios_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnalyticalRatiosView.findAll", query = "SELECT a FROM AnalyticalRatiosView a")
    , @NamedQuery(name = "AnalyticalRatiosView.findByRowNum", query = "SELECT a FROM AnalyticalRatiosView a WHERE a.rowNum = :rowNum")
    , @NamedQuery(name = "AnalyticalRatiosView.findByAccountId", query = "SELECT a FROM AnalyticalRatiosView a WHERE a.accountId = :accountId")
    , @NamedQuery(name = "AnalyticalRatiosView.findByBalance", query = "SELECT a FROM AnalyticalRatiosView a WHERE a.balance = :balance")
    , @NamedQuery(name = "AnalyticalRatiosView.findByBalanceLocal", query = "SELECT a FROM AnalyticalRatiosView a WHERE a.balanceLocal = :balanceLocal")
    , @NamedQuery(name = "AnalyticalRatiosView.findByBranchId", query = "SELECT a FROM AnalyticalRatiosView a WHERE a.branchId = :branchId")
    , @NamedQuery(name = "AnalyticalRatiosView.findByDate", query = "SELECT a FROM AnalyticalRatiosView a WHERE a.date = :date")})
public class AnalyticalRatiosView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "account_id")
    private int accountId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "balance_local")
    private BigDecimal balanceLocal;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public AnalyticalRatiosView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalanceLocal() {
        return balanceLocal;
    }

    public void setBalanceLocal(BigDecimal balanceLocal) {
        this.balanceLocal = balanceLocal;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
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
    
}
