package com.toby.views;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "balance_account_view")
public class BalanceAccountView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "general_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalDate;
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "acc_number")
    private Integer accNumber;
    @Column(name = "balance")
    private BigDecimal balance;
    @Column(name = "cost_center")
    private Integer costCenter;
    @Column(name = "admin_unit")
    private Integer adminUnit;

    public BalanceAccountView() {
    }

    public BalanceAccountView(Integer rowNum, Date generalDate, Integer accountId, BigDecimal balance) {
        this.rowNum = rowNum;
        this.generalDate = generalDate;
        this.accountId = accountId;
        this.balance = balance;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Date getGeneralDate() {
        return generalDate;
    }

    public void setGeneralDate(Date generalDate) {
        this.generalDate = generalDate;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the accNumber
     */
    public Integer getAccNumber() {
        return accNumber;
    }

    /**
     * @param accNumber the accNumber to set
     */
    public void setAccNumber(Integer accNumber) {
        this.accNumber = accNumber;
    }

    /**
     * @return the costCenter
     */
    public Integer getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(Integer costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the adminUnit
     */
    public Integer getAdminUnit() {
        return adminUnit;
    }

    /**
     * @param adminUnit the adminUnit to set
     */
    public void setAdminUnit(Integer adminUnit) {
        this.adminUnit = adminUnit;
    }
}
