/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author IT Shop
 */
@Entity
@Table(name = "gm_subscriptions")
public class GmSubscriptions extends BaseEntity {

    
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "taxvalue")
    private BigDecimal taxvalue;
    @Column(name = "finalNet")
    private BigDecimal finalNet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "discount_type")
    private Integer discountType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "post_flag")
    private int postFlag;
    @Column(name = "type")
    private Integer type;
    @Column(name = "account_id")
    private Integer accountId;
    @Size(max = 450)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "extra_cost")
    private BigDecimal extraCost;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "tax_flag")
    private Boolean taxFlag;
    @Column(name = "serial_tax")
    private Integer serialTax;
    @Column(name = "tax_flag_final")
    private Boolean taxFlagFinal;
    @Column(name = "general_journal_id")
    private Integer generalJournalId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "tax_disc_flag")
    private Boolean taxDiscFlag;
    @Column(name = "tax_disc_value")
    private BigDecimal taxDiscValue;
    @Column(name = "pay_cash")
    private BigDecimal payCash;
    @Column(name = "pay_net")
    private BigDecimal payNet;
    @Column(name = "revision")
    private Integer revision;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "gl_bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank glBankId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @JoinColumn(name = "gm_item_id", referencedColumnName = "id")
    @ManyToOne
    private GmItem gmItemId;
    public GmSubscriptions() {
    }

    public InvOrganizationSite getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSite organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public int getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(int postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(BigDecimal extraCost) {
        this.extraCost = extraCost;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Boolean getTaxFlag() {
        return taxFlag;
    }

    public void setTaxFlag(Boolean taxFlag) {
        this.taxFlag = taxFlag;
    }

    public Integer getSerialTax() {
        return serialTax;
    }

    public void setSerialTax(Integer serialTax) {
        this.serialTax = serialTax;
    }

    public Boolean getTaxFlagFinal() {
        return taxFlagFinal;
    }

    public void setTaxFlagFinal(Boolean taxFlagFinal) {
        this.taxFlagFinal = taxFlagFinal;
    }

    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getTaxDiscFlag() {
        return taxDiscFlag;
    }

    public void setTaxDiscFlag(Boolean taxDiscFlag) {
        this.taxDiscFlag = taxDiscFlag;
    }

    public BigDecimal getTaxDiscValue() {
        return taxDiscValue;
    }

    public void setTaxDiscValue(BigDecimal taxDiscValue) {
        this.taxDiscValue = taxDiscValue;
    }

    public BigDecimal getPayCash() {
        if(payCash == null){
        payCash = BigDecimal.ZERO ;
        }
        return payCash;
    }

    public void setPayCash(BigDecimal payCash) {
        this.payCash = payCash;
    }

    public BigDecimal getPayNet() {
         if(payNet == null){
        payNet = BigDecimal.ZERO ;
        }
        return payNet;
    }

    public void setPayNet(BigDecimal payNet) {
        this.payNet = payNet;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public GlBank getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(GlBank glBankId) {
        this.glBankId = glBankId;
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
        if (!(object instanceof GmSubscriptions)) {
            return false;
        }
        GmSubscriptions other = (GmSubscriptions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GmSubscriptions[ id=" + id + " ]";
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the taxvalue
     */
    public BigDecimal getTaxvalue() {
        return taxvalue;
    }

    /**
     * @param taxvalue the taxvalue to set
     */
    public void setTaxvalue(BigDecimal taxvalue) {
        this.taxvalue = taxvalue;
    }

    /**
     * @return the finalNet
     */
    public BigDecimal getFinalNet() {
        return finalNet;
    }

    /**
     * @param finalNet the finalNet to set
     */
    public void setFinalNet(BigDecimal finalNet) {
        this.finalNet = finalNet;
    }

    /**
     * @return the gmItemId
     */
    public GmItem getGmItemId() {
        return gmItemId;
    }

    /**
     * @param gmItemId the gmItemId to set
     */
    public void setGmItemId(GmItem gmItemId) {
        this.gmItemId = gmItemId;
    }

}
