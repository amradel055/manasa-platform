/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
@Table(name = "toby_user_bank")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TobyUserBank.findAll", query = "SELECT i FROM TobyUserBank i")
    , @NamedQuery(name = "TobyUserBank.findById", query = "SELECT i FROM TobyUserBank i WHERE i.id = :id")
    , @NamedQuery(name = "TobyUserBank.findByCreationDate", query = "SELECT i FROM TobyUserBank i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "TobyUserBank.findByModificationDate", query = "SELECT i FROM TobyUserBank i WHERE i.modificationDate = :modificationDate")})
public class TobyUserBank extends BaseEntity {
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    @ManyToOne
    private GlBank bankId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private TobyUser userId;

    public TobyUserBank() {
    }

    public TobyUserBank(Integer id) {
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

    

    public TobyUser getUserId() {
        return userId;
    }

    public void setUserId(TobyUser userId) {
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
        if (!(object instanceof TobyUserBank)) {
            return false;
        }
        TobyUserBank other = (TobyUserBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "toby.com.entity.TobyUserBank[ id=" + id + " ]";
    }
    
}
