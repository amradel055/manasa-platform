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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author User
 */
@Entity
@Table(name = "crm_customer_interested_products")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CrmCustomerInterestedProducts.findAll", query = "SELECT c FROM CrmCustomerInterestedProducts c"),
    @NamedQuery(name = "CrmCustomerInterestedProducts.findById", query = "SELECT c FROM CrmCustomerInterestedProducts c WHERE c.id = :id"),
    @NamedQuery(name = "CrmCustomerInterestedProducts.findByInterestedDegree", query = "SELECT c FROM CrmCustomerInterestedProducts c WHERE c.interestedDegree = :interestedDegree"),
    @NamedQuery(name = "CrmCustomerInterestedProducts.findByAskDate", query = "SELECT c FROM CrmCustomerInterestedProducts c WHERE c.askDate = :askDate"),
    @NamedQuery(name = "CrmCustomerInterestedProducts.findByDeletedDate", query = "SELECT c FROM CrmCustomerInterestedProducts c WHERE c.deletedDate = :deletedDate"),
    @NamedQuery(name = "CrmCustomerInterestedProducts.findByCreationDate", query = "SELECT c FROM CrmCustomerInterestedProducts c WHERE c.creationDate = :creationDate"),
    @NamedQuery(name = "CrmCustomerInterestedProducts.findByModificationDate", query = "SELECT c FROM CrmCustomerInterestedProducts c WHERE c.modificationDate = :modificationDate")})
public class CrmCustomerInterestedProducts extends BaseEntity {
    
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "interested_degree")
    private Integer interestedDegree;
    @Column(name = "ask_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date askDate;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "deleted_by", referencedColumnName = "id")
    @ManyToOne
    private IsagUser deletedBy;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne
    private InstProjectGuide projectId;

    public CrmCustomerInterestedProducts() {
    }

    public CrmCustomerInterestedProducts(Integer id) {
        this.id = id;
    }

    public Integer getInterestedDegree() {
        return interestedDegree;
    }

    public void setInterestedDegree(Integer interestedDegree) {
        this.interestedDegree = interestedDegree;
    }

    public Date getAskDate() {
        return askDate;
    }

    public void setAskDate(Date askDate) {
        this.askDate = askDate;
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

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrmCustomerInterestedProducts)) {
            return false;
        }
        CrmCustomerInterestedProducts other = (CrmCustomerInterestedProducts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sdfsdfs.CrmCustomerInterestedProducts[ id=" + id + " ]";
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
    
}
