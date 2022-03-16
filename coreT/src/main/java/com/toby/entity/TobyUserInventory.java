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
@Table(name = "toby_user_inventory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TobyUserInventory.findAll", query = "SELECT i FROM TobyUserInventory i")
    , @NamedQuery(name = "TobyUserInventory.findById", query = "SELECT i FROM TobyUserInventory i WHERE i.id = :id")
    , @NamedQuery(name = "TobyUserInventory.findByCreationDate", query = "SELECT i FROM TobyUserInventory i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "TobyUserInventory.findByModificationDate", query = "SELECT i FROM TobyUserInventory i WHERE i.modificationDate = :modificationDate")})
public class TobyUserInventory extends BaseEntity {
    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private TobyUser userId;

    public TobyUserInventory() {
    }

    public TobyUserInventory(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
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
        if (!(object instanceof TobyUserInventory)) {
            return false;
        }
        TobyUserInventory other = (TobyUserInventory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.TobyUserInventory[ id=" + id + " ]";
    }
    
}
