/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "gl_bank_transaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlBankTransaction.findAll", query = "SELECT g FROM GlBankTransaction g")
    , @NamedQuery(name = "GlBankTransaction.findById", query = "SELECT g FROM GlBankTransaction g WHERE g.id = :id")
    , @NamedQuery(name = "GlBankTransaction.findByDate", query = "SELECT g FROM GlBankTransaction g WHERE g.date = :date")
    , @NamedQuery(name = "GlBankTransaction.findByValue", query = "SELECT g FROM GlBankTransaction g WHERE g.value = :value")
    , @NamedQuery(name = "GlBankTransaction.findByPostFlag", query = "SELECT g FROM GlBankTransaction g WHERE g.postFlag = :postFlag")
    , @NamedQuery(name = "GlBankTransaction.findByPaymentType", query = "SELECT g FROM GlBankTransaction g WHERE g.paymentType = :paymentType")
    , @NamedQuery(name = "GlBankTransaction.findByOrganizationType", query = "SELECT g FROM GlBankTransaction g WHERE g.organizationType = :organizationType")
    , @NamedQuery(name = "GlBankTransaction.findByValueLocal", query = "SELECT g FROM GlBankTransaction g WHERE g.valueLocal = :valueLocal")
    , @NamedQuery(name = "GlBankTransaction.findByRemark", query = "SELECT g FROM GlBankTransaction g WHERE g.remark = :remark")
    , @NamedQuery(name = "GlBankTransaction.findByRemark2", query = "SELECT g FROM GlBankTransaction g WHERE g.remark2 = :remark2")
    , @NamedQuery(name = "GlBankTransaction.findByTransactionType", query = "SELECT g FROM GlBankTransaction g WHERE g.transactionType = :transactionType")
    , @NamedQuery(name = "GlBankTransaction.findByCreationDate", query = "SELECT g FROM GlBankTransaction g WHERE g.creationDate = :creationDate")
    , @NamedQuery(name = "GlBankTransaction.findByModificationDate", query = "SELECT g FROM GlBankTransaction g WHERE g.modificationDate = :modificationDate")
    , @NamedQuery(name = "GlBankTransaction.findByRate", query = "SELECT g FROM GlBankTransaction g WHERE g.rate = :rate")
    , @NamedQuery(name = "GlBankTransaction.findByGlYear", query = "SELECT g FROM GlBankTransaction g WHERE g.glYear = :glYear")
    , @NamedQuery(name = "GlBankTransaction.findByChequeNumber", query = "SELECT g FROM GlBankTransaction g WHERE g.chequeNumber = :chequeNumber")
    , @NamedQuery(name = "GlBankTransaction.findByChequeDate", query = "SELECT g FROM GlBankTransaction g WHERE g.chequeDate = :chequeDate")
    , @NamedQuery(name = "GlBankTransaction.findByChequeDueDate", query = "SELECT g FROM GlBankTransaction g WHERE g.chequeDueDate = :chequeDueDate")
    , @NamedQuery(name = "GlBankTransaction.findByChequeStatus", query = "SELECT g FROM GlBankTransaction g WHERE g.chequeStatus = :chequeStatus")})
public class GlBankTransaction extends BaseEntity {

    @Column(name = "serial")
    private Integer serial;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "date_hegry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHegry;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Column(name = "payment_type")
    private Integer paymentType;
    @Column(name = "organization_type")
    private Integer organizationType;
    @Column(name = "gl_year")
    private Integer glYear;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "remark2")
    private String remark2;
    @Column(name = "transaction_type")
    private Integer transactionType;
    @OneToMany(mappedBy = "glBankTransactionId")
    private Collection<GlBankTransactionDetail> glBankTransactionDetailCollection;
    @JoinColumn(name = "gl_bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank glBankId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "document_type_id", referencedColumnName = "id")
    @ManyToOne
    private Symbol documentTypeId;
    @JoinColumn(name = "general_journal_id", referencedColumnName = "id")
    @ManyToOne
    private GeneralJournal generalJournalId;
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoice invoiceId;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "cheque_number")
    private Integer chequeNumber;
    @Column(name = "cheque_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chequeDate;
    @Column(name = "cheque_due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chequeDueDate;
    @Column(name = "cheque_status")
    private Integer chequeStatus;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private GlBankTransaction parent;
    @JoinColumn(name = "gallary_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory gallaryId;
    @Column(name = "document_serial")
    private Integer documentSerial;
    @Column(name = "movement_type")
    private Integer movementType;
    @Transient
    private Boolean dublara = Boolean.FALSE;
    @JoinColumn(name = "glAccount_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount glAccountId;
    @Column(name = "bank_commetion")
    private BigDecimal bankCommetion;
    @JoinColumn(name = "cost_center_id", referencedColumnName = "id")
    @ManyToOne
    private CostCenter costCenter;
    @Transient
    private Boolean isDebit = Boolean.FALSE;

    public GlBankTransaction() {

    }

    public GlBankTransaction(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
    }

    public BigDecimal getValueLocal() {
        if (valueLocal == null) {
            valueLocal = BigDecimal.ZERO;
        }
        return valueLocal;
    }

    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    @XmlTransient
    public Collection<GlBankTransactionDetail> getGlBankTransactionDetailCollection() {
        return glBankTransactionDetailCollection;
    }

    public void setGlBankTransactionDetailCollection(Collection<GlBankTransactionDetail> glBankTransactionDetailCollection) {
        this.glBankTransactionDetailCollection = glBankTransactionDetailCollection;
    }

    public GlBank getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(GlBank glBankId) {
        this.glBankId = glBankId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvPurchaseInvoice getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(InvPurchaseInvoice invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(Integer chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public Date getChequeDueDate() {
        return chequeDueDate;
    }

    public void setChequeDueDate(Date chequeDueDate) {
        this.chequeDueDate = chequeDueDate;
    }

    public Integer getChequeStatus() {
        return chequeStatus;
    }

    public void setChequeStatus(Integer chequeStatus) {
        this.chequeStatus = chequeStatus;
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

    public Symbol getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Symbol documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public GeneralJournal getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(GeneralJournal generalJournalId) {
        this.generalJournalId = generalJournalId;
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
        if (!(object instanceof GlBankTransaction)) {
            return false;
        }
        GlBankTransaction other = (GlBankTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.GlBankTransaction[ id=" + id + " ]";
    }

    /**
     * @return the glYear
     */
    public Integer getGlYear() {
        return glYear;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYear(Integer glYear) {
        this.glYear = glYear;
    }

    /**
     * @return the remark2
     */
    public String getRemark2() {
        return remark2;
    }

    /**
     * @param remark2 the remark2 to set
     */
    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    /**
     * @return the parent
     */
    public GlBankTransaction getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(GlBankTransaction parent) {
        this.parent = parent;
    }

    /**
     * @return the dublara
     */
    public Boolean getDublara() {
        return dublara;
    }

    /**
     * @param dublara the dublara to set
     */
    public void setDublara(Boolean dublara) {
        this.dublara = dublara;
    }

    /**
     * @return the documentSerial
     */
    public Integer getDocumentSerial() {
        return documentSerial;
    }

    /**
     * @param documentSerial the documentSerial to set
     */
    public void setDocumentSerial(Integer documentSerial) {
        this.documentSerial = documentSerial;
    }

    /**
     * @return the dateHegry
     */
    public Date getDateHegry() {
        return dateHegry;
    }

    /**
     * @param dateHegry the dateHegry to set
     */
    public void setDateHegry(Date dateHegry) {
        this.dateHegry = dateHegry;
    }

    /**
     * @return the gallaryId
     */
    public InvInventory getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(InvInventory gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the glAccountId
     */
    public GlAccount getGlAccountId() {
        return glAccountId;
    }

    /**
     * @param glAccountId the glAccountId to set
     */
    public void setGlAccountId(GlAccount glAccountId) {
        this.glAccountId = glAccountId;
    }

    /**
     * @return the bankCommetion
     */
    public BigDecimal getBankCommetion() {
        if (bankCommetion == null) {
            bankCommetion = BigDecimal.ZERO;
        }
        return bankCommetion;
    }

    /**
     * @param bankCommetion the bankCommetion to set
     */
    public void setBankCommetion(BigDecimal bankCommetion) {
        this.bankCommetion = bankCommetion;
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
     * @return the isDebit
     */
    public Boolean getIsDebit() {
        return isDebit;
    }

    /**
     * @param isDebit the isDebit to set
     */
    public void setIsDebit(Boolean isDebit) {
        this.isDebit = isDebit;
    }

    /**
     * @return the movementType
     */
    public Integer getMovementType() {
        return movementType;
    }

    /**
     * @param movementType the movementType to set
     */
    public void setMovementType(Integer movementType) {
        this.movementType = movementType;
    }

}
