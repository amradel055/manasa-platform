/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
 * @author User
 */
@Entity
@Table(name = "crm_comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CrmComments.findAll", query = "SELECT c FROM CrmComments c"),
    @NamedQuery(name = "CrmComments.findById", query = "SELECT c FROM CrmComments c WHERE c.id = :id"),
    @NamedQuery(name = "CrmComments.findByContent", query = "SELECT c FROM CrmComments c WHERE c.content = :content"),
    @NamedQuery(name = "CrmComments.findByApproved", query = "SELECT c FROM CrmComments c WHERE c.approved = :approved"),
    @NamedQuery(name = "CrmComments.findByDeletedDate", query = "SELECT c FROM CrmComments c WHERE c.deletedDate = :deletedDate"),
    @NamedQuery(name = "CrmComments.findByCreationDate", query = "SELECT c FROM CrmComments c WHERE c.creationDate = :creationDate"),
    @NamedQuery(name = "CrmComments.findByModificationDate", query = "SELECT c FROM CrmComments c WHERE c.modificationDate = :modificationDate")})
public class CrmComments extends BaseEntity  {
    @Size(max = 100)
    @Column(name = "content")
    private String content;
    @Column(name = "approved")
    private Integer approved;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser deletedBy;
    @JoinColumn(name = "interested_product_id", referencedColumnName = "id")
    @ManyToOne
    private CrmCustomerInterestedProducts interestedProductId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private CrmComments parent;

    public CrmComments() {
    }

    public CrmComments(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IsagUser getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(IsagUser deletedBy) {
        this.deletedBy = deletedBy;
    }

    public CrmCustomerInterestedProducts getInterestedProductId() {
        return interestedProductId;
    }

    public void setInterestedProductId(CrmCustomerInterestedProducts interestedProductId) {
        this.interestedProductId = interestedProductId;
    }

    public InvOrganizationSite getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(InvOrganizationSite organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public CrmComments getParent() {
        return parent;
    }

    public void setParent(CrmComments parent) {
        this.parent = parent;
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
        if (!(object instanceof CrmComments)) {
            return false;
        }
        CrmComments other = (CrmComments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdfsdfs.CrmComments[ id=" + id + " ]";
    }
    
}
