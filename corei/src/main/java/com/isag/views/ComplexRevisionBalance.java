package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "complex_revision_balance")
public class ComplexRevisionBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "rowNum")
    private Integer rowNum;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "debit")
    private BigDecimal debit;

    @Column(name = "credit")
    private BigDecimal credit;
    
    @Transient
    private BigDecimal debitOpenningBalance;

    @Transient
    private BigDecimal creditOpenningBalance;

    @Column(name = "glAccountId")
    private Integer glAccountId;
    
    @Column(name = "glAccountName")
    private String glAccountName;

    @Column(name = "glBranchId")
    private Integer glBranchId;
    
    @Column(name = "accountClass")
    private String accountClass;

    @Column(name = "glAccountNumber")
    private Integer glAccountNumber;

    @Column(name = "glCostCenterCode")
    private Integer glCostCenterCode;

    @Column(name = "glCostCenterId")
    private Integer glCostCenterId;

    @Column(name = "glCostCenterName")
    private String glCostCenterName;

    @Column(name = "glAdminUnitId")
    private Integer glAdminUnitId;

    @Column(name = "glAdminUnitName")
    private String glAdminUnitName;

    @Column(name = "glAdminUnitCode")
    private int glAdminUnitCode;

    @Column(name = "level")
    private Integer level;

    @Column(name = "postFlag")
    private boolean postFlag = Boolean.FALSE;
    
    @Column(name = "symbolSerial")
    private Integer symbolSerial;
     
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

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }


    public Integer getGlCostCenterCode() {
        return glCostCenterCode;
    }

    public void setGlCostCenterCode(Integer glCostCenterCode) {
        this.glCostCenterCode = glCostCenterCode;
    }

    public Integer getGlCostCenterId() {
        return glCostCenterId;
    }

    public void setGlCostCenterId(Integer glCostCenterId) {
        this.glCostCenterId = glCostCenterId;
    }

    public String getGlCostCenterName() {
        return glCostCenterName;
    }

    public void setGlCostCenterName(String glCostCenterName) {
        this.glCostCenterName = glCostCenterName;
    }

    public Integer getLevel() {
        return level;
    }

    /**
     * @return the glAdminUnitName
     */
    public String getGlAdminUnitName() {
        return glAdminUnitName;
    }

    /**
     * @param glAdminUnitName the glAdminUnitName to set
     */
    public void setGlAdminUnitName(String glAdminUnitName) {
        this.glAdminUnitName = glAdminUnitName;
    }

    /**
     * @return the glAdminUnitCode
     */
    public int getGlAdminUnitCode() {
        return glAdminUnitCode;
    }

    /**
     * @param glAdminUnitCode the glAdminUnitCode to set
     */
    public void setGlAdminUnitCode(int glAdminUnitCode) {
        this.glAdminUnitCode = glAdminUnitCode;
    }

    /**
     * @return the glAccountId
     */
    public Integer getGlAccountId() {
        return glAccountId;
    }

    /**
     * @param glAccountId the glAccountId to set
     */
    public void setGlAccountId(Integer glAccountId) {
        this.glAccountId = glAccountId;
    }

    /**
     * @return the glAccountName
     */
    public String getGlAccountName() {
        return glAccountName;
    }

    /**
     * @param glAccountName the glAccountName to set
     */
    public void setGlAccountName(String glAccountName) {
        this.glAccountName = glAccountName;
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
    
    public String getAccountClass() {
        return accountClass;
    }

    public void setAccountClass(String accountClass) {
        this.accountClass = accountClass;
    }

    /**
     * @return the glAccountNumber
     */
    public Integer getGlAccountNumber() {
        return glAccountNumber;
    }

    /**
     * @param glAccountNumber the glAccountNumber to set
     */
    public void setGlAccountNumber(Integer glAccountNumber) {
        this.glAccountNumber = glAccountNumber;
    }

    /**
     * @return the glAdminUnitId
     */
    public Integer getGlAdminUnitId() {
        return glAdminUnitId;
    }

    /**
     * @param glAdminUnitId the glAdminUnitId to set
     */
    public void setGlAdminUnitId(Integer glAdminUnitId) {
        this.glAdminUnitId = glAdminUnitId;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the postFlag
     */
    public boolean isPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(boolean postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the symbolSerial
     */
    public Integer getSymbolSerial() {
        return symbolSerial;
    }

    /**
     * @param symbolSerial the symbolSerial to set
     */
    public void setSymbolSerial(Integer symbolSerial) {
        this.symbolSerial = symbolSerial;
    }

    /**
     * @return the debitOpenningBalance
     */
    public BigDecimal getDebitOpenningBalance() {
        return debitOpenningBalance;
    }

    /**
     * @param debitOpenningBalance the debitOpenningBalance to set
     */
    public void setDebitOpenningBalance(BigDecimal debitOpenningBalance) {
        this.debitOpenningBalance = debitOpenningBalance;
    }

    /**
     * @return the creditOpenningBalance
     */
    public BigDecimal getCreditOpenningBalance() {
        return creditOpenningBalance;
    }

    /**
     * @param creditOpenningBalance the creditOpenningBalance to set
     */
    public void setCreditOpenningBalance(BigDecimal creditOpenningBalance) {
        this.creditOpenningBalance = creditOpenningBalance;
    }


}
