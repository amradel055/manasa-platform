package com.toby.views;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "monthly_account_transaction")
public class MonthlyAccountTransactionView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "month_date")
    private String monthDate;
    @Column(name = "glaccount_id")
    private Integer glaccountId;
    @Column(name = "glAccountName")
    private String glAccountName;
    @Column(name = "acc_class")
    private String accClass;
    @Column(name = "glAccountCode")
    private Integer glAccountCode;
    @Column(name = "glBranchId")
    private Integer glBranchId;
    @Column(name = "admin_unit_id")
    private Integer adminUnitId;
    @Column(name = "cost_center_id")
    private Integer costCenterId;
    @Column(name = "general_Journa_lId")
    private int generalJournalId;
    @Column(name = "debit_amount")
    private BigDecimal debitAmount;
    @Column(name = "credit_amount")
    private BigDecimal creditAmount;
    @Transient
    private BigDecimal balance;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(String monthDate) {
        this.monthDate = monthDate;
    }

    public Integer getGlaccountId() {
        return glaccountId;
    }

    public void setGlaccountId(Integer glaccountId) {
        this.glaccountId = glaccountId;
    }

    public String getGlAccountName() {
        return glAccountName;
    }

    public void setGlAccountName(String glAccountName) {
        this.glAccountName = glAccountName;
    }

    public Integer getGlAccountCode() {
        return glAccountCode;
    }

    public void setGlAccountCode(Integer glAccountCode) {
        this.glAccountCode = glAccountCode;
    }

    public Integer getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(Integer adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public Integer getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    public int getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(int generalJournalId) {
        this.generalJournalId = generalJournalId;
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

    /**
     * @return the glBranchId
     */
    public Integer getGlBranchId() {
        return glBranchId;
    }

    /**
     * @param glBranchId the glBranchId to set
     */
    public void setGlBranchId(Integer glBranchId) {
        this.glBranchId = glBranchId;
    }

    /**
     * @return the accClass
     */
    public String getAccClass() {
        return accClass;
    }

    /**
     * @param accClass the accClass to set
     */
    public void setAccClass(String accClass) {
        this.accClass = accClass;
    }

}
