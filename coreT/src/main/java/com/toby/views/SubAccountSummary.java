package com.toby.views;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "subaccountsummary")
public class SubAccountSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "glaccount_id")
    private Integer glaccountId;
    @Column(name = "glAccountName")
    private String glAccountName;
    @Column(name = "admin_unit_id")
    private Integer adminUnitId;
    @Column(name = "cost_center_id")
    private Integer costCenterId;
    @Column(name = "glbranchId")
    private Integer glbranchId;
    @Column(name = "general_Journa_lId")
    private int generalJournalId;
    @Column(name = "general_decument")
    private Integer generalDecument;
    @Column(name = "symbol_id")
    private Integer symbolId;
    @Column(name = "serial")
    private String serial;
    @Size(max = 255)
    @Column(name = "symbol_name")
    private String symbolName;
    @Size(max = 45)
    @Column(name = "general_statement")
    private String generalStatement;
    @Size(max = 45)
    @Column(name = "discribtion")
    private String discribtion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debit_amount")
    private BigDecimal debitAmount;
    @Column(name = "credit_amount")
    private BigDecimal creditAmount;
    @Column(name = "debit_amount_foriegn")
    private BigDecimal debitAmountForiegn;
    @Column(name = "credit_amount_foriegn")
    private BigDecimal creditAmountForiegn;

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

    public Integer getGeneralDecument() {
        return generalDecument;
    }

    public void setGeneralDecument(Integer generalDecument) {
        this.generalDecument = generalDecument;
    }

    public Integer getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(Integer symbolId) {
        this.symbolId = symbolId;
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
     public Integer getGlbranchId() {
        return glbranchId;
    }

    public void setGlbranchId(Integer glbranchId) {
        this.glbranchId = glbranchId;
    }

    /**
     * @return the debitAmountForiegn
     */
    public BigDecimal getDebitAmountForiegn() {
        return debitAmountForiegn;
    }

    /**
     * @param debitAmountForiegn the debitAmountForiegn to set
     */
    public void setDebitAmountForiegn(BigDecimal debitAmountForiegn) {
        this.debitAmountForiegn = debitAmountForiegn;
    }

    /**
     * @return the creditAmountForiegn
     */
    public BigDecimal getCreditAmountForiegn() {
        return creditAmountForiegn;
    }

    /**
     * @param creditAmountForiegn the creditAmountForiegn to set
     */
    public void setCreditAmountForiegn(BigDecimal creditAmountForiegn) {
        this.creditAmountForiegn = creditAmountForiegn;
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
     * @return the discribtion
     */
    public String getDiscribtion() {
        return discribtion;
    }

    /**
     * @param discribtion the discribtion to set
     */
    public void setDiscribtion(String discribtion) {
        this.discribtion = discribtion;
    }
}
