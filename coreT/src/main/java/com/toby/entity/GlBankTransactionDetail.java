/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "gl_bank_transaction_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlBankTransactionDetail.findAll", query = "SELECT g FROM GlBankTransactionDetail g")
    , @NamedQuery(name = "GlBankTransactionDetail.findById", query = "SELECT g FROM GlBankTransactionDetail g WHERE g.id = :id")
    , @NamedQuery(name = "GlBankTransactionDetail.findByRemarks", query = "SELECT g FROM GlBankTransactionDetail g WHERE g.remarks = :remarks")
    , @NamedQuery(name = "GlBankTransactionDetail.findByValue", query = "SELECT g FROM GlBankTransactionDetail g WHERE g.value = :value")
    , @NamedQuery(name = "GlBankTransactionDetail.findByValueLocal", query = "SELECT g FROM GlBankTransactionDetail g WHERE g.valueLocal = :valueLocal")
    , @NamedQuery(name = "GlBankTransactionDetail.findBySerial", query = "SELECT g FROM GlBankTransactionDetail g WHERE g.serial = :serial")})
public class GlBankTransactionDetail extends BaseEntity {

    @Size(max = 450)
    @Column(name = "remarks")
    private String remarks;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @JoinColumn(name = "account_credit_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountCreditId;
    @JoinColumn(name = "account_debit_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountDebitId;
    @JoinColumn(name = "admin_unit_id", referencedColumnName = "id")
    @ManyToOne
    private GlAdminUnit adminUnitId;
    @JoinColumn(name = "cost_center_id", referencedColumnName = "id")
    @ManyToOne
    private CostCenter costCenterId;
    @JoinColumn(name = "gl_bank_transaction_id", referencedColumnName = "id")
    @ManyToOne
    private GlBankTransaction glBankTransactionId;
    
    @JoinColumn(name = "bank_id_from", referencedColumnName = "id")
    @ManyToOne
    private GlBank bankIdFrom;
    @JoinColumn(name = "bank_id_to", referencedColumnName = "id")
    @ManyToOne
    private GlBank bankIdTo;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @Column(name = "serial")
    private Integer serial;
    
    @Column(name = "rate_bank_from")
    private BigDecimal rateBankFrom;
    @Column(name = "rate_bank_to")
    private BigDecimal rateBankTo;
    
    
    @JoinColumn(name = "symbol_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol symbolId;

    public GlBankTransactionDetail() {
    }

    public GlBankTransactionDetail(Integer id) {
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

    public GlAccount getAccountCreditId() {
        return accountCreditId;
    }

    public void setAccountCreditId(GlAccount accountCreditId) {
        this.accountCreditId = accountCreditId;
    }

    public GlAccount getAccountDebitId() {
        return accountDebitId;
    }

    public void setAccountDebitId(GlAccount accountDebitId) {
        this.accountDebitId = accountDebitId;
    }

    public GlAdminUnit getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(GlAdminUnit adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public CostCenter getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(CostCenter costCenterId) {
        this.costCenterId = costCenterId;
    }

    public GlBankTransaction getGlBankTransactionId() {
        return glBankTransactionId;
    }

    public void setGlBankTransactionId(GlBankTransaction glBankTransactionId) {
        this.glBankTransactionId = glBankTransactionId;
    }
    
    public InvOrganizationSite getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSite organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public GlBank getBankIdFrom() {
        return bankIdFrom;
    }

    public void setBankIdFrom(GlBank bankIdFrom) {
        this.bankIdFrom = bankIdFrom;
    }

    public GlBank getBankIdTo() {
        return bankIdTo;
    }

    public void setBankIdTo(GlBank bankIdTo) {
        this.bankIdTo = bankIdTo;
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
        if (!(object instanceof GlBankTransactionDetail)) {
            return false;
        }
        GlBankTransactionDetail other = (GlBankTransactionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.GlBankTransactionDetail[ id=" + id + " ]";
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

    public Symbol getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(Symbol symbolId) {
        this.symbolId = symbolId;
    }
    
}
