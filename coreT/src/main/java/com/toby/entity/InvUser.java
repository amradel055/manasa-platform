package com.toby.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author khaled
 */
@Entity
@Table(name = "inv_user")
public class InvUser extends BaseEntity {

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private TobyUser userId;

    public InvUser() {
    }

    public InvUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getInvId() {
        return invId;
    }

    public void setInvId(InvInventory invId) {
        this.invId = invId;
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
        if (!(object instanceof InvUser)) {
            return false;
        }
        InvUser other = (InvUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.InvUser[ id=" + id + " ]";
    }

}
