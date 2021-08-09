/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "isag_user_bank")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsagUserBank.findAll", query = "SELECT i FROM IsagUserBank i")
    , @NamedQuery(name = "IsagUserBank.findById", query = "SELECT i FROM IsagUserBank i WHERE i.id = :id")
    , @NamedQuery(name = "IsagUserBank.findByCreationDate", query = "SELECT i FROM IsagUserBank i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "IsagUserBank.findByModificationDate", query = "SELECT i FROM IsagUserBank i WHERE i.modificationDate = :modificationDate")})
public class IsagUserBank extends BaseEntity {
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank bankId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private IsagUser userId;

    public IsagUserBank() {
    }

    public IsagUserBank(Integer id) {
        this.id = id;
    }

    

    public GlBank getBankId() {
        return bankId;
    }

    public void setBankId(GlBank bankId) {
        this.bankId = bankId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    

    public IsagUser getUserId() {
        return userId;
    }

    public void setUserId(IsagUser userId) {
        this.userId = userId;
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
        if (!(object instanceof IsagUserBank)) {
            return false;
        }
        IsagUserBank other = (IsagUserBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "isag.com.entity.IsagUserBank[ id=" + id + " ]";
    }
    
}
