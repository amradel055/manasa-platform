/**
 *
 */
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
import javax.validation.constraints.Size;

/**
 * @author anady
 *
 */
@Entity
@Table(name = "deleted_and_updated_general_journal")
public class DeletedAndUpdatedGeneralJournal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "rowNum")
    private Integer rowNum;

    @Column(name = "voucherId")
    private Integer voucherId;
    
    @Column(name = "serial")
    private Integer serial;

    @Column(name = "generalDocument")
    private Integer generalDecument;

    @Column(name = "generalDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalDate;

    @Column(name = "generalDateNew")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generalDateNew;
    
    @Column(name = "modificationDateNew")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDateNew;

    @Column(name = "TotalAmount", columnDefinition = "Decimal(10,2)")
    private BigDecimal totalAmount;

    @Column(name = "TotalAmountNew", columnDefinition = "Decimal(10,2)")
    private BigDecimal totalAmountNew;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "userName")
    private String userName;

    @Column(name = "userIdNew")
    private Integer userIdNew;

    @Column(name = "userNameNew")
    private String userNameNew;

    @Column(name = "generalStatement")
    private String generalStatement;

    @Column(name = "generalStatementNew")
    private String generalStatementNew;

    @Column(name = "accountId")
    private Integer accountId;

    @Column(name = "accountName")
    private String accountName;

    @Column(name = "accountIdNew")
    private Integer accountIdNew;

    @Column(name = "accountNameNew")
    private String accountNameNew;

    @Column(name = "accountNumber")
    private Integer accountNumber;

    @Column(name = "accountNumberNew")
    private Integer accountNumberNew;

    @Column(name = "operationStatue")
    private Integer operationStatue;

    @Column(name = "branchId")
    private Integer branchId;

    @Column(name = "companyId")
    private Integer companyId;

    @Size(max = 450)
    @Column(name = "macId")
    private String macId;
    
    @Size(max = 450)
    @Column(name = "macIdNew")
    private String macIdNew;

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

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getGeneralDecument() {
        return generalDecument;
    }

    public void setGeneralDecument(Integer generalDecument) {
        this.generalDecument = generalDecument;
    }

    public Date getGeneralDate() {
        return generalDate;
    }

    public void setGeneralDate(Date generalDate) {
        this.generalDate = generalDate;
    }

    public Date getGeneralDateNew() {
        return generalDateNew;
    }

    public void setGeneralDateNew(Date generalDateNew) {
        this.generalDateNew = generalDateNew;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmountNew() {
        return totalAmountNew;
    }

    public void setTotalAmountNew(BigDecimal totalAmountNew) {
        this.totalAmountNew = totalAmountNew;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserIdNew() {
        return userIdNew;
    }

    public void setUserIdNew(Integer userIdNew) {
        this.userIdNew = userIdNew;
    }

    public String getUserNameNew() {
        return userNameNew;
    }

    public void setUserNameNew(String userNameNew) {
        this.userNameNew = userNameNew;
    }

    public String getGeneralStatement() {
        return generalStatement;
    }

    public void setGeneralStatement(String generalStatement) {
        this.generalStatement = generalStatement;
    }

    public String getGeneralStatementNew() {
        return generalStatementNew;
    }

    public void setGeneralStatementNew(String generalStatementNew) {
        this.generalStatementNew = generalStatementNew;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAccountIdNew() {
        return accountIdNew;
    }

    public void setAccountIdNew(Integer accountIdNew) {
        this.accountIdNew = accountIdNew;
    }

    public String getAccountNameNew() {
        return accountNameNew;
    }

    public void setAccountNameNew(String accountNameNew) {
        this.accountNameNew = accountNameNew;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getAccountNumberNew() {
        return accountNumberNew;
    }

    public void setAccountNumberNew(Integer accountNumberNew) {
        this.accountNumberNew = accountNumberNew;
    }

    public Integer getOperationStatue() {
        return operationStatue;
    }

    public void setOperationStatue(Integer operationStatue) {
        this.operationStatue = operationStatue;
    }
    
    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }
    
    public String getMacIdNew() {
        return macIdNew;
    }

    public void setMacIdNew(String macIdNew) {
        this.macIdNew = macIdNew;
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

    /**
     * @return the modificationDateNew
     */
    public Date getModificationDateNew() {
        return modificationDateNew;
    }

    /**
     * @param modificationDateNew the modificationDateNew to set
     */
    public void setModificationDateNew(Date modificationDateNew) {
        this.modificationDateNew = modificationDateNew;
    }
}
