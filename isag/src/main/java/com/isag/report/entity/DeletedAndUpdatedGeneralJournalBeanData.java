package com.isag.report.entity;

import java.math.BigDecimal;
import java.util.Date;

public class DeletedAndUpdatedGeneralJournalBeanData {

    private BigDecimal totalAmmountBefore;
    private BigDecimal totalAmmountAfter;
    private Integer accountNumber;
    private Integer accountNumberNew;
    private Integer voucherId;
    private Integer serial;
    private String accountName;
    private String accountNameNew;
    private String userName;
    private String macId;
    private String macIdNew;
    private String operationStatus;
    private Date operationDate;
    private Date operationDateNew;
    private Date modificationDateNew;

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public BigDecimal getTotalAmmountBefore() {
        return totalAmmountBefore;
    }

    public void setTotalAmmountBefore(BigDecimal totalAmmountBefore) {
        this.totalAmmountBefore = totalAmmountBefore;
    }

    public BigDecimal getTotalAmmountAfter() {
        return totalAmmountAfter;
    }

    public void setTotalAmmountAfter(BigDecimal totalAmmountAfter) {
        this.totalAmmountAfter = totalAmmountAfter;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getAccountNumberNew() {
        return accountNumberNew;
    }

    public void setAccountNumberNew(Integer accountNumberNew) {
        this.accountNumberNew = accountNumberNew;
    }

    public String getMacIdNew() {
        return macIdNew;
    }

    public void setMacIdNew(String macIdNew) {
        this.macIdNew = macIdNew;
    }

    public String getAccountNameNew() {
        return accountNameNew;
    }

    public void setAccountNameNew(String accountNameNew) {
        this.accountNameNew = accountNameNew;
    }

    public Date getOperationDateNew() {
        return operationDateNew;
    }

    public void setOperationDateNew(Date operationDateNew) {
        this.operationDateNew = operationDateNew;
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
