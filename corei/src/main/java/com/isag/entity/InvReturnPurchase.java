/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "inv_return_purchase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvReturnPurchase.findAll", query = "SELECT i FROM InvReturnPurchase i"),
    @NamedQuery(name = "InvReturnPurchase.findById", query = "SELECT i FROM InvReturnPurchase i WHERE i.id = :id"),
    @NamedQuery(name = "InvReturnPurchase.findByPaymentType", query = "SELECT i FROM InvReturnPurchase i WHERE i.paymentType = :paymentType"),
    @NamedQuery(name = "InvReturnPurchase.findByDate", query = "SELECT i FROM InvReturnPurchase i WHERE i.date = :date"),
    @NamedQuery(name = "InvReturnPurchase.findByInvoiceDate", query = "SELECT i FROM InvReturnPurchase i WHERE i.invoiceDate = :invoiceDate"),
    @NamedQuery(name = "InvReturnPurchase.findByRemark", query = "SELECT i FROM InvReturnPurchase i WHERE i.remark = :remark"),
    @NamedQuery(name = "InvReturnPurchase.findByRate", query = "SELECT i FROM InvReturnPurchase i WHERE i.rate = :rate"),
    @NamedQuery(name = "InvReturnPurchase.findByCreationDate", query = "SELECT i FROM InvReturnPurchase i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvReturnPurchase.findByModificationDate", query = "SELECT i FROM InvReturnPurchase i WHERE i.modificationDate = :modificationDate")})
public class InvReturnPurchase extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_type")
    private Integer paymentType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "invoice_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDate;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "cost_center", referencedColumnName = "id")
    @ManyToOne
    private CostCenter costCenter;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate")
    private BigDecimal rate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invReturnPurchaseId")
    private Collection<InvReturnPurchaseDetail> invReturnPurchaseDetailCollection;
    @JoinColumn(name = "cuurency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;
    @JoinColumn(name = "purchase_invoice_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoice purchaseInvoiceId;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite supplierId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
    @JoinColumn(name = "gl_bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank glBankId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "type")
    private Boolean type;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Column(name = "net")
    private BigDecimal net;    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "discount_value")
    private BigDecimal discountValue;    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tax_value")
    private BigDecimal taxValue;
    @JoinColumn(name = "inv_delegator_id", referencedColumnName = "id")
    @ManyToOne
    private InventoryDelegator salesPerson;
    @JoinColumn(name = "general_journal_id", referencedColumnName = "id")
    @ManyToOne
    private GeneralJournal generalJournalId;
 
    public InvReturnPurchase() {
    }

    public InvReturnPurchase(Integer id) {
        this.id = id;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @XmlTransient
    public Collection<InvReturnPurchaseDetail> getInvReturnPurchaseDetailCollection() {
        return invReturnPurchaseDetailCollection;
    }

    public void setInvReturnPurchaseDetailCollection(Collection<InvReturnPurchaseDetail> invReturnPurchaseDetailCollection) {
        this.invReturnPurchaseDetailCollection = invReturnPurchaseDetailCollection;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public InvPurchaseInvoice getPurchaseInvoiceId() {
        return purchaseInvoiceId;
    }

    public void setPurchaseInvoiceId(InvPurchaseInvoice purchaseInvoiceId) {
        this.purchaseInvoiceId = purchaseInvoiceId;
    }

    public InvOrganizationSite getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(InvOrganizationSite supplierId) {
        this.supplierId = supplierId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public CostCenter getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
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
        if (!(object instanceof InvReturnPurchase)) {
            return false;
        }
        InvReturnPurchase other = (InvReturnPurchase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial != null ? serial.toString() : "";
    }

    public GlBank getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(GlBank glBankId) {
        this.glBankId = glBankId;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public InventoryDelegator getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(InventoryDelegator salesPerson) {
        this.salesPerson = salesPerson;
    }

    /**
     * @return the generalJournalId
     */
    public GeneralJournal getGeneralJournalId() {
        return generalJournalId;
    }

    /**
     * @param generalJournalId the generalJournalId to set
     */
    public void setGeneralJournalId(GeneralJournal generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    /**
     * @return the taxValue
     */
    public BigDecimal getTaxValue() {
        return taxValue;
    }

    /**
     * @param taxValue the taxValue to set
     */
    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }

}
