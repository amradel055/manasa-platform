/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_notice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvNotice.findAll", query = "SELECT i FROM InvNotice i"),
    @NamedQuery(name = "InvNotice.findById", query = "SELECT i FROM InvNotice i WHERE i.id = :id"),
    @NamedQuery(name = "InvNotice.findByDate", query = "SELECT i FROM InvNotice i WHERE i.date = :date"),
    @NamedQuery(name = "InvNotice.findByValue", query = "SELECT i FROM InvNotice i WHERE i.value = :value"),
    @NamedQuery(name = "InvNotice.findByRemark", query = "SELECT i FROM InvNotice i WHERE i.remark = :remark"),
    @NamedQuery(name = "InvNotice.findByModificationDate", query = "SELECT i FROM InvNotice i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvNotice.findByCreationDate", query = "SELECT i FROM InvNotice i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvNotice.findByType", query = "SELECT i FROM InvNotice i WHERE i.type = :type")})
public class InvNotice extends BaseEntity {

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "type")
    private Integer type;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "contrast_account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount contrastAccountId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "post_flag")
    private Integer postFlag;
    @JoinColumn(name = "general_journal_id", referencedColumnName = "id")
    @ManyToOne
    private GeneralJournal generalJournalId;
    @JoinColumn(name = "inv_invoice", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoice invInvoice;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventory;
    @Transient
    private String typeName;

    public InvNotice() {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public GlAccount getContrastAccountId() {
        return contrastAccountId;
    }

    public void setContrastAccountId(GlAccount contrastAccountId) {
        this.contrastAccountId = contrastAccountId;
    }

    public InvOrganizationSite getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSite organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
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
        if (!(object instanceof InvNotice)) {
            return false;
        }
        InvNotice other = (InvNotice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.InvNotice[ id=" + id + " ]";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
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
     * @return the invInvoice
     */
    public InvPurchaseInvoice getInvInvoice() {
        return invInvoice;
    }

    /**
     * @param invInvoice the invInvoice to set
     */
    public void setInvInvoice(InvPurchaseInvoice invInvoice) {
        this.invInvoice = invInvoice;
    }

    /**
     * @return the invInventory
     */
    public InvInventory getInvInventory() {
        return invInventory;
    }

    /**
     * @param invInventory the invInventory to set
     */
    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
    }

    /**
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
