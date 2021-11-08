/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author User
 */
@Entity
@Table(name = "crm_customer_interested_products")
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
    private TobyUser deletedBy;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem itemId;
    @JoinColumn(name = "organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite organizationSiteId;

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

    public TobyUser getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(TobyUser deletedBy) {
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
