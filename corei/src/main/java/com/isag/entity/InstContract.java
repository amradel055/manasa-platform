/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inst_contract")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstContract.findAll", query = "SELECT i FROM InstContract i"),
    @NamedQuery(name = "InstContract.findById", query = "SELECT i FROM InstContract i WHERE i.id = :id"),
    @NamedQuery(name = "InstContract.findByDate", query = "SELECT i FROM InstContract i WHERE i.date = :date"),
    @NamedQuery(name = "InstContract.findByStartInstallment", query = "SELECT i FROM InstContract i WHERE i.startInstallment = :startInstallment"),
    @NamedQuery(name = "InstContract.findByAdvancePayment", query = "SELECT i FROM InstContract i WHERE i.advancePayment = :advancePayment"),
    @NamedQuery(name = "InstContract.findByDeliveryPayment", query = "SELECT i FROM InstContract i WHERE i.deliveryPayment = :deliveryPayment"),
    @NamedQuery(name = "InstContract.findByInstallmentValue", query = "SELECT i FROM InstContract i WHERE i.installmentValue = :installmentValue"),
    @NamedQuery(name = "InstContract.findByDaysNumber", query = "SELECT i FROM InstContract i WHERE i.daysNumber = :daysNumber"),
    @NamedQuery(name = "InstContract.findByInstallmentNumber", query = "SELECT i FROM InstContract i WHERE i.installmentNumber = :installmentNumber"),
    @NamedQuery(name = "InstContract.findByTotalValue", query = "SELECT i FROM InstContract i WHERE i.totalValue = :totalValue"),
    @NamedQuery(name = "InstContract.findByCreationDate", query = "SELECT i FROM InstContract i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InstContract.findByCancel", query = "SELECT i FROM InstContract i WHERE i.cancel = :cancel"),
    @NamedQuery(name = "InstContract.findByModificationDate", query = "SELECT i FROM InstContract i WHERE i.modificationDate = :modificationDate")})
public class InstContract extends BaseEntity {

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "start_installment")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startInstallment;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "advance_payment")
    private BigDecimal advancePayment;
    @Column(name = "delivery_payment")
    private BigDecimal deliveryPayment;
    @Column(name = "installment_value")
    private BigDecimal installmentValue;
    @Column(name = "days_number")
    private Integer daysNumber;
    @Column(name = "installment_number")
    private Integer installmentNumber;
    @Column(name = "total_value")
    private BigDecimal totalValue;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "murabaha_rate")
    private BigDecimal murabahaRate;
    @OneToMany(mappedBy = "instContractId")
    private Collection<InstContractDetail> instContractDetailCollection;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne
    private InstProjectGuide projectId;
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank bankId;
    @JoinColumn(name = "general_journal_id", referencedColumnName = "id")
    @ManyToOne
    private GeneralJournal generalJournalId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "status")
    private Integer status;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "inv_item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem invItemId;
    @Column(name = "cancel")
    private boolean cancel = Boolean.FALSE;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    
    public InstContract() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartInstallment() {
        return startInstallment;
    }

    public void setStartInstallment(Date startInstallment) {
        this.startInstallment = startInstallment;
    }

    public BigDecimal getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = advancePayment;
    }

    public BigDecimal getDeliveryPayment() {
        return deliveryPayment;
    }

    public void setDeliveryPayment(BigDecimal deliveryPayment) {
        this.deliveryPayment = deliveryPayment;
    }

    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
    }

    public Integer getDaysNumber() {
        return daysNumber;
    }

    public void setDaysNumber(Integer daysNumber) {
        this.daysNumber = daysNumber;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    @XmlTransient
    public Collection<InstContractDetail> getInstContractDetailCollection() {
        return instContractDetailCollection;
    }

    public void setInstContractDetailCollection(Collection<InstContractDetail> instContractDetailCollection) {
        this.instContractDetailCollection = instContractDetailCollection;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvOrganizationSite getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSite organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public InstProjectGuide getProjectId() {
        return projectId;
    }

    public void setProjectId(InstProjectGuide projectId) {
        this.projectId = projectId;
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
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof InstContract)) {
            return false;
        }
        InstContract other = (InstContract) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (projectId != null) {
            return projectId.getName() + " " + serial;
        }else if(invItemId != null){
            return invItemId.getName() + " " + serial;
        }else{
            return ""+serial;
        }
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
     * @return the bankId
     */
    public GlBank getBankId() {
        return bankId;
    }

    /**
     * @param bankId the bankId to set
     */
    public void setBankId(GlBank bankId) {
        this.bankId = bankId;
    }

    /**
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the murabahaRate
     */
    public BigDecimal getMurabahaRate() {
        return murabahaRate;
    }

    /**
     * @param murabahaRate the murabahaRate to set
     */
    public void setMurabahaRate(BigDecimal murabahaRate) {
        this.murabahaRate = murabahaRate;
    }

    /**
     * @return the invItemId
     */
    public InvItem getInvItemId() {
        return invItemId;
    }

    /**
     * @param invItemId the invItemId to set
     */
    public void setInvItemId(InvItem invItemId) {
        this.invItemId = invItemId;
    }

    /**
     * @return the cancel
     */
    public boolean isCancel() {
        return cancel;
    }

    /**
     * @param cancel the cancel to set
     */
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
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

}
