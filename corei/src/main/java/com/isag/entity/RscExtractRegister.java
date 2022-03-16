/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "rsc_extract_register")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscExtractRegister.findAll", query = "SELECT r FROM RscExtractRegister r")

    , @NamedQuery(name = "RscExtractRegister.findBySerial", query = "SELECT r FROM RscExtractRegister r WHERE r.serial = :serial")
    , @NamedQuery(name = "RscExtractRegister.findByDate", query = "SELECT r FROM RscExtractRegister r WHERE r.date = :date")
    , @NamedQuery(name = "RscExtractRegister.findByReceivedDate", query = "SELECT r FROM RscExtractRegister r WHERE r.receivedDate = :receivedDate")
    , @NamedQuery(name = "RscExtractRegister.findByClientAcceptedDate", query = "SELECT r FROM RscExtractRegister r WHERE r.clientAcceptedDate = :clientAcceptedDate")
    , @NamedQuery(name = "RscExtractRegister.findByNetExtractPrice", query = "SELECT r FROM RscExtractRegister r WHERE r.netExtractPrice = :netExtractPrice")
    , @NamedQuery(name = "RscExtractRegister.findByValid", query = "SELECT r FROM RscExtractRegister r WHERE r.valid = :valid")
    , @NamedQuery(name = "RscExtractRegister.findByClosed", query = "SELECT r FROM RscExtractRegister r WHERE r.closed = :closed")
    , @NamedQuery(name = "RscExtractRegister.findByDocNumber", query = "SELECT r FROM RscExtractRegister r WHERE r.docNumber = :docNumber")
    , @NamedQuery(name = "RscExtractRegister.findByRemarks", query = "SELECT r FROM RscExtractRegister r WHERE r.remarks = :remarks")
    , @NamedQuery(name = "RscExtractRegister.findByProfissionaiAcceptedDate", query = "SELECT r FROM RscExtractRegister r WHERE r.profissionaiAcceptedDate = :profissionaiAcceptedDate")
    , @NamedQuery(name = "RscExtractRegister.findByRscExtractReport", query = "SELECT r FROM RscExtractRegister r WHERE r.branchId.id = :branchId  AND  (:contractorFrom IS NULL OR r.siteId.code >= :contractorFrom) AND (:contractorTo  IS NULL OR r.siteId.code <= :contractorTo) AND (:dateFrom IS NULL OR r.date >= :dateFrom) AND (:dateTo IS NULL OR r.date <= :dateTo) AND (:extractFrom IS NULL OR r.serial >= CAST(:extractFrom AS SIGNED)) AND (:extractTo IS NULL OR r.serial <= CAST(:extractTo AS SIGNED))")
})
public class RscExtractRegister extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "serial")
    private Integer serial;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;
    @Column(name = "client_accepted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clientAcceptedDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "net_extract_price")
    private BigDecimal netExtractPrice;
    @Column(name = "valid")
    private Boolean valid;
    @Column(name = "closed")
    private Boolean closed;
    @Size(max = 45)
    @Column(name = "doc_number")
    private String docNumber;
    @Size(max = 100)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "remain")
    private BigDecimal remain;
    @Column(name = "profissionai_accepted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date profissionaiAcceptedDate;

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "restriction_id", referencedColumnName = "id")
    @ManyToOne
    private RscRestrictionQuantities restrictionId;
    @JoinColumn(name = "old_extract", referencedColumnName = "id")
    @ManyToOne
    private RscExtractRegister oldExtract;
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite siteId;
    @JoinColumn(name = "tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender tenderId;
    @JoinColumn(name = "inst_projectGuide", referencedColumnName = "id")
    @ManyToOne
    private InstProjectGuide instProjectGuide;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite customerId;
    @Column(name = "percentage")
    private BigDecimal percentage;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Column(name = "total_value")
    private BigDecimal totalValue;
    @JoinColumn(name = "general_journal_id", referencedColumnName = "id")
    @ManyToOne
    private GeneralJournal generalJournalId;
    @Column(name = "total_percentage")
    private BigDecimal totalPercentage;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;

    public RscExtractRegister() {
    }

    public RscExtractRegister(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getClientAcceptedDate() {
        return clientAcceptedDate;
    }

    public void setClientAcceptedDate(Date clientAcceptedDate) {
        this.clientAcceptedDate = clientAcceptedDate;
    }

    public BigDecimal getNetExtractPrice() {
        if(netExtractPrice == null){
            netExtractPrice = getTotalValue();
        }
        return netExtractPrice;
    }

    public void setNetExtractPrice(BigDecimal netExtractPrice) {
        this.netExtractPrice = netExtractPrice;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getProfissionaiAcceptedDate() {
        return profissionaiAcceptedDate;
    }

    public void setProfissionaiAcceptedDate(Date profissionaiAcceptedDate) {
        this.profissionaiAcceptedDate = profissionaiAcceptedDate;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public RscExtractRegister getOldExtract() {
        return oldExtract;
    }

    public void setOldExtract(RscExtractRegister oldExtract) {
        this.oldExtract = oldExtract;
    }

    public InvOrganizationSite getSiteId() {
        return siteId;
    }

    public void setSiteId(InvOrganizationSite siteId) {
        this.siteId = siteId;
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
        if (!(object instanceof RscExtractRegister)) {
            return false;
        }
        RscExtractRegister other = (RscExtractRegister) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RscExtractRegister[ id=" + id + " ]";
    }

    /**
     * @return the remain
     */
    public BigDecimal getRemain() {
        return remain;
    }

    /**
     * @param remain the remain to set
     */
    public void setRemain(BigDecimal remain) {
        this.remain = remain;
    }

    /**
     * @return the tenderId
     */
    public RscTender getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(RscTender tenderId) {
        this.tenderId = tenderId;
    }

    /**
     * @return the restrictionId
     */
    public RscRestrictionQuantities getRestrictionId() {
        return restrictionId;
    }

    /**
     * @param restrictionId the restrictionId to set
     */
    public void setRestrictionId(RscRestrictionQuantities restrictionId) {
        this.restrictionId = restrictionId;
    }

    /**
     * @return the instProjectGuide
     */
    public InstProjectGuide getInstProjectGuide() {
        return instProjectGuide;
    }

    /**
     * @param instProjectGuide the instProjectGuide to set
     */
    public void setInstProjectGuide(InstProjectGuide instProjectGuide) {
        this.instProjectGuide = instProjectGuide;
    }

    /**
     * @return the customerId
     */
    public InvOrganizationSite getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(InvOrganizationSite customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the percentage
     */
    public BigDecimal getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
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
     * @return the totalValue
     */
    public BigDecimal getTotalValue() {
        if(totalValue == null){
            totalValue = BigDecimal.ZERO;
        }
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
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

    
    public BigDecimal getTotalPercentage() {
        return totalPercentage;
    }

    
    public void setTotalPercentage(BigDecimal totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    /**
     * @return the invInventoryId
     */
    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    /**
     * @param invInventoryId the invInventoryId to set
     */
    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

}
