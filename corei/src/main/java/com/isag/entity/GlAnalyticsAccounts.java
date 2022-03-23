/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
@Table(name = "gl_analytics_accounts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlAnalyticsAccounts.findAll", query = "SELECT g FROM GlAnalyticsAccounts g")
    , @NamedQuery(name = "GlAnalyticsAccounts.findById", query = "SELECT g FROM GlAnalyticsAccounts g WHERE g.id = :id")
    , @NamedQuery(name = "GlAnalyticsAccounts.findByCode", query = "SELECT g FROM GlAnalyticsAccounts g WHERE g.code = :code")
    , @NamedQuery(name = "GlAnalyticsAccounts.findByModificationDate", query = "SELECT g FROM GlAnalyticsAccounts g WHERE g.modificationDate = :modificationDate")
    , @NamedQuery(name = "GlAnalyticsAccounts.findByType", query = "SELECT g FROM GlAnalyticsAccounts g WHERE g.type = :type")
    , @NamedQuery(name = "GlAnalyticsAccounts.findByCreationDate", query = "SELECT g FROM GlAnalyticsAccounts g WHERE g.creationDate = :creationDate")})
public class GlAnalyticsAccounts extends BaseEntity {


    @Size(max = 450)
    @Column(name = "code")
    private String code;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @Column(name = "type")
    private Integer type;
    
    public GlAnalyticsAccounts() {
    }

   

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    

    public GlAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof GlAnalyticsAccounts)) {
            return false;
        }
        GlAnalyticsAccounts other = (GlAnalyticsAccounts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.GlAnalyticsAccounts[ id=" + id + " ]";
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }
    
}
