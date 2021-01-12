/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inv_update")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvUpdate.findAll", query = "SELECT i FROM InvUpdate i")
    , @NamedQuery(name = "InvUpdate.findById", query = "SELECT i FROM InvUpdate i WHERE i.id = :id")
    , @NamedQuery(name = "InvUpdate.findByCreationDate", query = "SELECT i FROM InvUpdate i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "InvUpdate.findByModificationDate", query = "SELECT i FROM InvUpdate i WHERE i.modificationDate = :modificationDate")
    , @NamedQuery(name = "InvUpdate.findBySerial", query = "SELECT i FROM InvUpdate i WHERE i.serial = :serial")
    , @NamedQuery(name = "InvUpdate.findByRemarks", query = "SELECT i FROM InvUpdate i WHERE i.remarks = :remarks")
    , @NamedQuery(name = "InvUpdate.findByDocument", query = "SELECT i FROM InvUpdate i WHERE i.document = :document")
    , @NamedQuery(name = "InvUpdate.findByDate", query = "SELECT i FROM InvUpdate i WHERE i.date = :date")
    , @NamedQuery(name = "InvUpdate.findByPostFlag", query = "SELECT i FROM InvUpdate i WHERE i.postFlag = :postFlag")})
public class InvUpdate extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "serial")
    private int serial;
    @Size(max = 200)
    @Column(name = "remarks")
    private String remarks;
    @Size(max = 200)
    @Column(name = "document")
    private String document;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "post_flag")
    private Integer postFlag;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory inventoryId;
    @OneToMany(mappedBy = "invUpdateId")
    private Collection<InvUpdateDetail> invUpdateDetailCollection;

    public InvUpdate() {
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(InvInventory inventoryId) {
        this.inventoryId = inventoryId;
    }

    @XmlTransient
    public Collection<InvUpdateDetail> getInvUpdateDetailCollection() {
        return invUpdateDetailCollection;
    }

    public void setInvUpdateDetailCollection(Collection<InvUpdateDetail> invUpdateDetailCollection) {
        this.invUpdateDetailCollection = invUpdateDetailCollection;
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
        if (!(object instanceof InvUpdate)) {
            return false;
        }
        InvUpdate other = (InvUpdate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.en.entity.InvUpdate[ id=" + id + " ]";
    }
    
}
