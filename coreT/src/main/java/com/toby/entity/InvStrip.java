/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author kmelsayed
 */
@Entity
@Table(name = "inv_strip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvStrip.findAll", query = "SELECT i FROM InvStrip i"),
    @NamedQuery(name = "InvStrip.findById", query = "SELECT i FROM InvStrip i WHERE i.id = :id"),
    @NamedQuery(name = "InvStrip.findByCreationDate", query = "SELECT i FROM InvStrip i WHERE i.creationDate = :creationDate"),
    @NamedQuery(name = "InvStrip.findByModificationDate", query = "SELECT i FROM InvStrip i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvStrip.findByRemarks", query = "SELECT i FROM InvStrip i WHERE i.remarks = :remarks"),
    @NamedQuery(name = "InvStrip.findByStripDocument", query = "SELECT i FROM InvStrip i WHERE i.stripDocument = :stripDocument"),
    @NamedQuery(name = "InvStrip.findByStripDate", query = "SELECT i FROM InvStrip i WHERE i.stripDate = :stripDate")})
public class InvStrip extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "strip_document")
    private String stripDocument;
    @Column(name = "strip_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stripDate;
    @OneToMany(mappedBy = "invStripId")
    private Collection<InvStripDetail> invStripDetailCollection;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory inventoryId;
    @Column(name = "type")
    private Integer type;

    public InvStrip() {
    }

    public InvStrip(Integer id) {
        this.id = id;
    }

    public InvStrip(Integer id, int serialNo) {
        this.id = id;
        this.serial = serialNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStripDocument() {
        return stripDocument;
    }

    public void setStripDocument(String stripDocument) {
        this.stripDocument = stripDocument;
    }

    public Date getStripDate() {
        return stripDate;
    }

    public void setStripDate(Date stripDate) {
        this.stripDate = stripDate;
    }

    @XmlTransient
    public Collection<InvStripDetail> getInvStripDetailCollection() {
        return invStripDetailCollection;
    }

    public void setInvStripDetailCollection(Collection<InvStripDetail> invStripDetailCollection) {
        this.invStripDetailCollection = invStripDetailCollection;
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
        if (!(object instanceof InvStrip)) {
            return false;
        }
        InvStrip other = (InvStrip) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.toby.entity.InvStrip[ id=" + id + " ]";
    }

    public InvInventory getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(InvInventory inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
