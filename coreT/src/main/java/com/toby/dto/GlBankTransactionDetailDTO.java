/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import java.math.BigDecimal;

/**
 *
 * @author amr
 */
public class GlBankTransactionDetailDTO extends BaseEntityDTO {

    private String remarks;
    private BigDecimal value;
    private BigDecimal valueLocal;
    private Integer accountCreditId;
    private Integer accountDebitId;
    private Integer adminUnitId;
    private Integer costCenterId;
    private Integer glBankTransactionId;
    private GlBankDTO bankIdFrom;
    private GlBankDTO bankIdTo;
    private InvOrganizationSiteDTO organizationSiteId;
    private Integer serial;
    private BigDecimal rateBankFrom;
    private BigDecimal rateBankTo;
    private SymbolDTO symbolId;

    public GlBankTransactionDetailDTO() {
    }

    public GlBankTransactionDetailDTO(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GlBankTransactionDetailDTO)) {
            return false;
        }
        GlBankTransactionDetailDTO other = (GlBankTransactionDetailDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.GlBankTransactionDetail[ id=" + id + " ]";
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

    public BigDecimal getRateBankFrom() {
        return rateBankFrom;
    }

    public void setRateBankFrom(BigDecimal rateBankFrom) {
        this.rateBankFrom = rateBankFrom;
    }

    public BigDecimal getRateBankTo() {
        return rateBankTo;
    }

    public void setRateBankTo(BigDecimal rateBankTo) {
        this.rateBankTo = rateBankTo;
    }

    /**
     * @return the accountCreditId
     */
    public Integer getAccountCreditId() {
        return accountCreditId;
    }

    /**
     * @param accountCreditId the accountCreditId to set
     */
    public void setAccountCreditId(Integer accountCreditId) {
        this.accountCreditId = accountCreditId;
    }

    /**
     * @return the accountDebitId
     */
    public Integer getAccountDebitId() {
        return accountDebitId;
    }

    /**
     * @param accountDebitId the accountDebitId to set
     */
    public void setAccountDebitId(Integer accountDebitId) {
        this.accountDebitId = accountDebitId;
    }

    /**
     * @return the adminUnitId
     */
    public Integer getAdminUnitId() {
        return adminUnitId;
    }

    /**
     * @param adminUnitId the adminUnitId to set
     */
    public void setAdminUnitId(Integer adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    /**
     * @return the costCenterId
     */
    public Integer getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(Integer costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the glBankTransactionId
     */
    public Integer getGlBankTransactionId() {
        return glBankTransactionId;
    }

    /**
     * @param glBankTransactionId the glBankTransactionId to set
     */
    public void setGlBankTransactionId(Integer glBankTransactionId) {
        this.glBankTransactionId = glBankTransactionId;
    }

    /**
     * @return the bankIdFrom
     */
    public GlBankDTO getBankIdFrom() {
        return bankIdFrom;
    }

    /**
     * @param bankIdFrom the bankIdFrom to set
     */
    public void setBankIdFrom(GlBankDTO bankIdFrom) {
        this.bankIdFrom = bankIdFrom;
    }

    /**
     * @return the bankIdTo
     */
    public GlBankDTO getBankIdTo() {
        return bankIdTo;
    }

    /**
     * @param bankIdTo the bankIdTo to set
     */
    public void setBankIdTo(GlBankDTO bankIdTo) {
        this.bankIdTo = bankIdTo;
    }

    /**
     * @return the organizationSiteId
     */
    public InvOrganizationSiteDTO getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(InvOrganizationSiteDTO organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the symbolId
     */
    public SymbolDTO getSymbolId() {
        return symbolId;
    }

    /**
     * @param symbolId the symbolId to set
     */
    public void setSymbolId(SymbolDTO symbolId) {
        this.symbolId = symbolId;
    }
    
}
