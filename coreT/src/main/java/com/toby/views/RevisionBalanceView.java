package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "revisionbalance")
public class RevisionBalanceView implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "rowNum")
	private Integer rowNum;

	@Column(name = "date")
	private String date;

	@Column(name = "level")
	private int levelAcc;

	@Column(name = "glaccountId")
	private Integer glaccountId;
	
	@Column(name = "glAccountNumber")
	private Integer glAccountNumber;

	@Column(name = "glAccountName")
	private String glAccountName;

	@Column(name = "glAdminUnitId")
	private Integer adminUnitId;
	
	@Column(name = "glAdminUnitName")
	private String glAdminUnitName;
	
	@Column(name = "glAdminUnitCode")
	private int glAdminUnitCode;
	
	@Column(name = "dibit")
	private BigDecimal debitAmount;
	
	@Column(name = "credit")
	private BigDecimal creditAmount;
	
	@Transient
	private BigDecimal balance;

        
	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public int getLevelAcc() {
		return levelAcc;
	}

	public void setLevelAcc(int levelAcc) {
		this.levelAcc = levelAcc;
	}

	public Integer getGlaccountId() {
		return glaccountId;
	}

	public void setGlaccountId(Integer glaccountId) {
		this.glaccountId = glaccountId;
	}

	public Integer getGlAccountNumber() {
		return glAccountNumber;
	}

	public void setGlAccountNumber(Integer glAccountNumber) {
		this.glAccountNumber = glAccountNumber;
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

	public String getGlAdminUnitName() {
		return glAdminUnitName;
	}

	public void setGlAdminUnitName(String glAdminUnitName) {
		this.glAdminUnitName = glAdminUnitName;
	}

	public int getGlAdminUnitCode() {
		return glAdminUnitCode;
	}

	public void setGlAdminUnitCode(int glAdminUnitCode) {
		this.glAdminUnitCode = glAdminUnitCode;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
