/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "isag_api_token")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsagApiToken.findAll", query = "SELECT i FROM IsagApiToken i")
    , @NamedQuery(name = "IsagApiToken.findById", query = "SELECT i FROM IsagApiToken i WHERE i.id = :id")
    , @NamedQuery(name = "IsagApiToken.findByToken", query = "SELECT i FROM IsagApiToken i WHERE i.token = :token")
    , @NamedQuery(name = "IsagApiToken.findByTokenId", query = "SELECT i FROM IsagApiToken i WHERE i.tokenId = :tokenId")
    , @NamedQuery(name = "IsagApiToken.findByCreationDate", query = "SELECT i FROM IsagApiToken i WHERE i.creationDate = :creationDate")})
public class IsagApiToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    protected Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 700)
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "token_id")
    private String tokenId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne()
    private IsagCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne()
    private IsagUser createdBy;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public IsagApiToken() {
    }

    public IsagApiToken(Integer id) {
        this.id = id;
    }

    public IsagApiToken(Integer id, String token, String tokenId) {
        this.id = id;
        this.token = token;
        this.tokenId = tokenId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IsagCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(IsagCompany companyId) {
        this.companyId = companyId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public IsagUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IsagUser createdBy) {
        this.createdBy = createdBy;
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
        if (!(object instanceof IsagApiToken)) {
            return false;
        }
        IsagApiToken other = (IsagApiToken) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.isag.notification.IsagApiToken[ id=" + id + " ]";
    }

}
