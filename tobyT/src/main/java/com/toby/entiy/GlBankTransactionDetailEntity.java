/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entiy;

import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.Symbol;
import java.math.BigDecimal;

/**
 *
 * @author WIN7
 */
public class GlBankTransactionDetailEntity extends BaseEntity {

    private Integer index;

    private String remarks;
    private BigDecimal value;
    private BigDecimal valueLocal;
    private Integer accountCreditId;
    private Integer accountDebitId;
    private Integer adminUnitId;
    private Integer costCenterId;
    private Integer glBankTransactionId;
    private GlBank bankFrom;
    private GlBank bankTo;
    private InvOrganizationSite invOrganizationSite;
    private String accountName;
    private Integer accountNumber;
    private GlAccount glAccount;
    private GlAccount glAccountBak;
    private CostCenter costCenter;
    private CostCenter costCenterBak;
    private GlAdminUnit glAdminUnit;
    private GlAdminUnit glAdminUnitBak;
    private Symbol glItems;
    private Integer type;
    private BigDecimal total;
    private String organizationsiteName;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
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

    public Integer getGlBankTransactionId() {
        return glBankTransactionId;
    }

    public void setGlBankTransactionId(Integer glBankTransactionId) {
        this.glBankTransactionId = glBankTransactionId;
    }

    /**
     * @return the bankFrom
     */
    public GlBank getBankFrom() {
        return bankFrom;
    }

    /**
     * @param bankFrom the bankFrom to set
     */
    public void setBankFrom(GlBank bankFrom) {
        this.bankFrom = bankFrom;
    }

    /**
     * @return the bankTo
     */
    public GlBank getBankTo() {
        return bankTo;
    }

    /**
     * @param bankTo the bankTo to set
     */
    public void setBankTo(GlBank bankTo) {
        this.bankTo = bankTo;
    }

    /**
     * @return the invOrganizationSite
     */
    public InvOrganizationSite getInvOrganizationSite() {
        return invOrganizationSite;
    }

    /**
     * @param invOrganizationSite the invOrganizationSite to set
     */
    public void setInvOrganizationSite(InvOrganizationSite invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
    }

    public void setInvOrganizationSite(InvOrganizationSiteDTO invOrganizationSite) {
        InvOrganizationSite invOrganizationSiteNew = new InvOrganizationSite();
        invOrganizationSiteNew.setId(invOrganizationSite.getId());
        invOrganizationSiteNew.setName(invOrganizationSite.getName());
        invOrganizationSiteNew.setCode(invOrganizationSite.getCode());
        this.invOrganizationSite = invOrganizationSiteNew;
    }

    public Integer getAccountCreditId() {
        return accountCreditId;
    }

    public void setAccountCreditId(Integer accountCreditId) {
        this.accountCreditId = accountCreditId;
    }

    public Integer getAccountDebitId() {
        return accountDebitId;
    }

    public void setAccountDebitId(Integer accountDebitId) {
        this.accountDebitId = accountDebitId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the accountNumber
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the glAccount
     */
    public GlAccount getGlAccount() {
        return glAccount;
    }

    /**
     * @param glAccount the glAccount to set
     */
    public void setGlAccount(GlAccount glAccount) {
        this.glAccount = glAccount;
    }

    /**
     * @return the costCenter
     */
    public CostCenter getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the glAdminUnit
     */
    public GlAdminUnit getGlAdminUnit() {
        return glAdminUnit;
    }

    /**
     * @param glAdminUnit the glAdminUnit to set
     */
    public void setGlAdminUnit(GlAdminUnit glAdminUnit) {
        this.glAdminUnit = glAdminUnit;
    }

    /**
     * @return the glAccountBak
     */
    public GlAccount getGlAccountBak() {
        return glAccountBak;
    }

    /**
     * @param glAccountBak the glAccountBak to set
     */
    public void setGlAccountBak(GlAccount glAccountBak) {
        this.glAccountBak = glAccountBak;
    }

    /**
     * @return the costCenterBak
     */
    public CostCenter getCostCenterBak() {
        return costCenterBak;
    }

    /**
     * @param costCenterBak the costCenterBak to set
     */
    public void setCostCenterBak(CostCenter costCenterBak) {
        this.costCenterBak = costCenterBak;
    }

    /**
     * @return the glAdminUnitBak
     */
    public GlAdminUnit getGlAdminUnitBak() {
        return glAdminUnitBak;
    }

    /**
     * @param glAdminUnitBak the glAdminUnitBak to set
     */
    public void setGlAdminUnitBak(GlAdminUnit glAdminUnitBak) {
        this.glAdminUnitBak = glAdminUnitBak;
    }

    /**
     * @return the glItems
     */
    public Symbol getGlItems() {
        return glItems;
    }

    /**
     * @param glItems the glItems to set
     */
    public void setGlItems(Symbol glItems) {
        this.glItems = glItems;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the organizationsiteName
     */
    public String getOrganizationsiteName() {
        return organizationsiteName;
    }

    /**
     * @param organizationsiteName the organizationsiteName to set
     */
    public void setOrganizationsiteName(String organizationsiteName) {
        this.organizationsiteName = organizationsiteName;
    }

}
