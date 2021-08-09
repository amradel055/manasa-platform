/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import com.isag.define.CompanyActivityClassEnum;
import com.isag.dto.GlBankDTO;
import com.isag.entity.AccountsSystemSettings;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author IT Shop
 */
public class CashAccountSettlementBean implements Serializable{

    private List<CashAccountSettlmentView> cashAccountSettlementList;
    private List<CashAccountSettlmentView> cashAccountSettlementOpeningBalanceList;
    private List<SubAccountSummaryReport> cashAccountSettlementReports;

    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
    private BigDecimal openingBalance;

    private Map<Integer, GlBankDTO> glBankMap;
    private Map<Integer, GlBankDTO> glBankCopyMap;
    private Map<Integer, BigDecimal> totalForDebitMap;
    private Map<Integer, BigDecimal> totalForCreditMap;
    private Map<Integer, BigDecimal> totalForBalanceMap;
    private Map<Integer, BigDecimal> openingBalanceMap;
    private TreeMap<Integer, SubAccountSummaryReport> cashTreeMap;

    private Integer distinctBankId;
    private StringBuilder builder;
    private Integer glBankTransactionIdSeclected;
    private AccountsSystemSettings accountsSystemSettings;
    private CompanyActivityClassEnum companyActivity;
    private SubAccountSummaryReport glBankTransactionTypeSeclected;
    
    

    public BigDecimal getTotalCredit() {
        if (totalCredit == null) {
            totalCredit = BigDecimal.ZERO;
        }
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public BigDecimal getTotalDebit() {
        if (totalDebit == null) {
            totalDebit = BigDecimal.ZERO;
        }
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public List<CashAccountSettlmentView> getCashAccountSettlementList() {
        return cashAccountSettlementList;
    }

    public void setCashAccountSettlementList(List<CashAccountSettlmentView> cashAccountSettlementList) {
        this.cashAccountSettlementList = cashAccountSettlementList;
    }

    public List<SubAccountSummaryReport> getCashAccountSettlementReports() {
        return cashAccountSettlementReports;
    }

    public void setCashAccountSettlementReports(List<SubAccountSummaryReport> cashAccountSettlementReports) {
        this.cashAccountSettlementReports = cashAccountSettlementReports;
    }

    /**
     * @return the glBankMap
     */
    public Map<Integer, GlBankDTO> getGlBankMap() {
        if (glBankMap == null) {
            glBankMap = new HashMap<>();
        }
        return glBankMap;
    }

    /**
     * @param glBankMap the glBankMap to set
     */
    public void setGlBankMap(Map<Integer, GlBankDTO> glBankMap) {
        this.glBankMap = glBankMap;
    }

    /**
     * @return the totalForDebitMap
     */
    public Map<Integer, BigDecimal> getTotalForDebitMap() {
        if (totalForDebitMap == null) {
            totalForDebitMap = new HashMap<>();
        }
        return totalForDebitMap;
    }

    /**
     * @param totalForDebitMap the totalForDebitMap to set
     */
    public void setTotalForDebitMap(Map<Integer, BigDecimal> totalForDebitMap) {
        this.totalForDebitMap = totalForDebitMap;
    }

    /**
     * @return the totalForCreditMap
     */
    public Map<Integer, BigDecimal> getTotalForCreditMap() {
        if (totalForCreditMap == null) {
            totalForCreditMap = new HashMap<>();
        }
        return totalForCreditMap;
    }

    /**
     * @param totalForCreditMap the totalForCreditMap to set
     */
    public void setTotalForCreditMap(Map<Integer, BigDecimal> totalForCreditMap) {
        this.totalForCreditMap = totalForCreditMap;
    }

    /**
     * @return the totalForBalanceMap
     */
    public Map<Integer, BigDecimal> getTotalForBalanceMap() {
        if (totalForBalanceMap == null) {
            totalForBalanceMap = new HashMap<>();
        }
        return totalForBalanceMap;
    }

    /**
     * @param totalForBalanceMap the totalForBalanceMap to set
     */
    public void setTotalForBalanceMap(Map<Integer, BigDecimal> totalForBalanceMap) {
        this.totalForBalanceMap = totalForBalanceMap;
    }

    /**
     * @return the cashTreeMap
     */
    public TreeMap<Integer, SubAccountSummaryReport> getCashTreeMap() {
        if (cashTreeMap == null) {
            cashTreeMap = new TreeMap<>();
        }
        return cashTreeMap;
    }

    /**
     * @param cashTreeMap the cashTreeMap to set
     */
    public void setCashTreeMap(TreeMap<Integer, SubAccountSummaryReport> cashTreeMap) {
        this.cashTreeMap = cashTreeMap;
    }

    /**
     * @return the openingBalanceMap
     */
    public Map<Integer, BigDecimal> getOpeningBalanceMap() {
        if (openingBalanceMap == null) {
            openingBalanceMap = new HashMap<>();
        }
        return openingBalanceMap;
    }

    /**
     * @param openingBalanceMap the openingBalanceMap to set
     */
    public void setOpeningBalanceMap(Map<Integer, BigDecimal> openingBalanceMap) {
        this.openingBalanceMap = openingBalanceMap;
    }

    /**
     * @return the distinctBankId
     */
    public Integer getDistinctBankId() {
        return distinctBankId;
    }

    /**
     * @param distinctBankId the distinctBankId to set
     */
    public void setDistinctBankId(Integer distinctBankId) {
        this.distinctBankId = distinctBankId;
    }

    /**
     * @return the openingBalance
     */
    public BigDecimal getOpeningBalance() {
        if (openingBalance == null) {
            openingBalance = BigDecimal.ZERO;
        }
        return openingBalance;
    }

    /**
     * @param openingBalance the openingBalance to set
     */
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    /**
     * @return the glBankCopyMap
     */
    public Map<Integer, GlBankDTO> getGlBankCopyMap() {
        if (glBankCopyMap == null) {
            glBankCopyMap = new HashMap<>();
        }
        return glBankCopyMap;
    }

    /**
     * @param glBankCopyMap the glBankCopyMap to set
     */
    public void setGlBankCopyMap(Map<Integer, GlBankDTO> glBankCopyMap) {
        this.glBankCopyMap = glBankCopyMap;
    }

    /**
     * @return the cashAccountSettlementOpeningBalanceList
     */
    public List<CashAccountSettlmentView> getCashAccountSettlementOpeningBalanceList() {
        return cashAccountSettlementOpeningBalanceList;
    }

    /**
     * @param cashAccountSettlementOpeningBalanceList the
     * cashAccountSettlementOpeningBalanceList to set
     */
    public void setCashAccountSettlementOpeningBalanceList(List<CashAccountSettlmentView> cashAccountSettlementOpeningBalanceList) {
        this.cashAccountSettlementOpeningBalanceList = cashAccountSettlementOpeningBalanceList;
    }

    /**
     * @return the builder
     */
    public StringBuilder getBuilder() {
        return builder;
    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public Integer getGlBankTransactionIdSeclected() {
        return glBankTransactionIdSeclected;
    }

    public void setGlBankTransactionIdSeclected(Integer glBankTransactionIdSeclected) {
        this.glBankTransactionIdSeclected = glBankTransactionIdSeclected;
    }

    /**
     * @return the glBankTransactionTypeSeclected
     */
    public SubAccountSummaryReport getGlBankTransactionTypeSeclected() {
        return glBankTransactionTypeSeclected;
    }

    /**
     * @param glBankTransactionTypeSeclected the glBankTransactionTypeSeclected
     * to set
     */
    public void setGlBankTransactionTypeSeclected(SubAccountSummaryReport glBankTransactionTypeSeclected) {
        this.glBankTransactionTypeSeclected = glBankTransactionTypeSeclected;
    }

    /**
     * @return the accountsSystemSettings
     */
    public AccountsSystemSettings getAccountsSystemSettings() {
        return accountsSystemSettings;
    }

    /**
     * @param accountsSystemSettings the accountsSystemSettings to set
     */
    public void setAccountsSystemSettings(AccountsSystemSettings accountsSystemSettings) {
        this.accountsSystemSettings = accountsSystemSettings;
    }

    /**
     * @return the companyActivity
     */
    public CompanyActivityClassEnum getCompanyActivity() {
        return companyActivity;
    }

    /**
     * @param companyActivity the companyActivity to set
     */
    public void setCompanyActivity(CompanyActivityClassEnum companyActivity) {
        this.companyActivity = companyActivity;
    }
}
