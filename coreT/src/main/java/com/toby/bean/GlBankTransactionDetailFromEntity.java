/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bean;

import com.toby.dto.BaseEntityDTO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.Symbol;
import com.toby.views.GlBankTransactionInvoiceView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author WIN7
 */
public class GlBankTransactionDetailFromEntity extends  BaseEntityDTO{

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
    private InvOrganizationSiteDTO invOrganizationSite;
    private String accountName;
    private String organizationsiteName;
    private Integer accountNumber;
    private GlAccountDTO glAccount;
    private GlAccountDTO glAccountBak;
    private CostCenterDTO costCenter;
    private CostCenterDTO costCenterBak;
    private GlAdminUnit glAdminUnit;
    private GlAdminUnit glAdminUnitBak;
    private Symbol glItems;
    private Integer type;
    private Integer serial;
    private BigDecimal total;
    private BigDecimal rate;
    private BigDecimal valueLocalReverse;
    private List<GlBankTransactionDetailFromEntity> glBankTransactionDetailEntityList;
    private List<GlBankTransactionInvoiceView> glBankTransactionInvoiceViewList;
    private Integer invoiceId;
    private BigDecimal bankCommition;
    
    private GlAccountDTO glAccountCredit;
    private GlAccountDTO glAccountBakCredit;
    private CostCenterDTO costCenterCredit;
    private CostCenterDTO costCenterBakCredit;

    public GlBankTransactionDetailFromEntity() {
    }

    public GlBankTransactionDetailFromEntity(List<GlBankTransactionDetailFromEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

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
    public GlAccountDTO getGlAccount() {
        return glAccount;
    }

    /**
     * @param glAccount the glAccount to set
     */
    public void setGlAccount(GlAccountDTO glAccount) {
        this.glAccount = glAccount;
    }

    /**
     * @return the costCenter
     */
    public CostCenterDTO getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(CostCenterDTO costCenter) {
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
    public GlAccountDTO getGlAccountBak() {
        return glAccountBak;
    }

    /**
     * @param glAccountBak the glAccountBak to set
     */
    public void setGlAccountBak(GlAccountDTO glAccountBak) {
        this.glAccountBak = glAccountBak;
    }

    /**
     * @return the costCenterBak
     */
    public CostCenterDTO getCostCenterBak() {
        return costCenterBak;
    }

    /**
     * @param costCenterBak the costCenterBak to set
     */
    public void setCostCenterBak(CostCenterDTO costCenterBak) {
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

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        if (rate == null) {
            rate = BigDecimal.ZERO;
        }
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @return the valueLocalReverse
     */
    public BigDecimal getValueLocalReverse() {
        return valueLocalReverse;
    }

    /**
     * @param valueLocalReverse the valueLocalReverse to set
     */
    public void setValueLocalReverse(BigDecimal valueLocalReverse) {
        this.valueLocalReverse = valueLocalReverse;
    }

    /**
     * @return the glBankTransactionDetailEntityList
     */
    public List<GlBankTransactionDetailFromEntity> getGlBankTransactionDetailEntityList() {
        return glBankTransactionDetailEntityList;
    }

    /**
     * @param glBankTransactionDetailEntityList the
     * glBankTransactionDetailEntityList to set
     */
    public void setGlBankTransactionDetailEntityList(List<GlBankTransactionDetailFromEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

    /**
     * @return the invOrganizationSite
     */
    public InvOrganizationSiteDTO getInvOrganizationSite() {
        return invOrganizationSite;
    }

    /**
     * @param invOrganizationSite the invOrganizationSite to set
     */
    public void setInvOrganizationSite(InvOrganizationSiteDTO invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
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
     * @return the glBankTransactionInvoiceViewList
     */
    public List<GlBankTransactionInvoiceView> getGlBankTransactionInvoiceViewList() {
        if(glBankTransactionInvoiceViewList == null){
            glBankTransactionInvoiceViewList = new ArrayList<>();
        }
        return glBankTransactionInvoiceViewList;
    }

    /**
     * @param glBankTransactionInvoiceViewList the glBankTransactionInvoiceViewList to set
     */
    public void setGlBankTransactionInvoiceViewList(List<GlBankTransactionInvoiceView> glBankTransactionInvoiceViewList) {
        this.glBankTransactionInvoiceViewList = glBankTransactionInvoiceViewList;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the bankCommition
     */
    public BigDecimal getBankCommition() {
        if(bankCommition == null){
            bankCommition = BigDecimal.ZERO;
        }
        return bankCommition;
    }

    /**
     * @param bankCommition the bankCommition to set
     */
    public void setBankCommition(BigDecimal bankCommition) {
        this.bankCommition = bankCommition;
    }
    
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (index != null ? index.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GlBankTransactionDetailFromEntity)) {
            return false;
        }
        GlBankTransactionDetailFromEntity other = (GlBankTransactionDetailFromEntity) object;
        if ((this.index == null && other.index != null) || (this.index != null && !this.index.equals(other.index))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.GlBankTransactionDetail[ id=" + id + " ]";
    }

    /**
     * @return the glAccountCredit
     */
    public GlAccountDTO getGlAccountCredit() {
        return glAccountCredit;
    }

    /**
     * @param glAccountCredit the glAccountCredit to set
     */
    public void setGlAccountCredit(GlAccountDTO glAccountCredit) {
        this.glAccountCredit = glAccountCredit;
    }

    /**
     * @return the glAccountBakCredit
     */
    public GlAccountDTO getGlAccountBakCredit() {
        return glAccountBakCredit;
    }

    /**
     * @param glAccountBakCredit the glAccountBakCredit to set
     */
    public void setGlAccountBakCredit(GlAccountDTO glAccountBakCredit) {
        this.glAccountBakCredit = glAccountBakCredit;
    }

    /**
     * @return the costCenterCredit
     */
    public CostCenterDTO getCostCenterCredit() {
        return costCenterCredit;
    }

    /**
     * @param costCenterCredit the costCenterCredit to set
     */
    public void setCostCenterCredit(CostCenterDTO costCenterCredit) {
        this.costCenterCredit = costCenterCredit;
    }

    /**
     * @return the costCenterBakCredit
     */
    public CostCenterDTO getCostCenterBakCredit() {
        return costCenterBakCredit;
    }

    /**
     * @param costCenterBakCredit the costCenterBakCredit to set
     */
    public void setCostCenterBakCredit(CostCenterDTO costCenterBakCredit) {
        this.costCenterBakCredit = costCenterBakCredit;
    }


}
