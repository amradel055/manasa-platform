/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hhhh
 */
@Entity
@Table(name = "inv_openning_balance_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvOpenningBalanceItem.findAll", query = "SELECT i FROM InvOpenningBalanceItem i"),
    @NamedQuery(name = "InvOpenningBalanceItem.findById", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.id = :id"),
    @NamedQuery(name = "InvOpenningBalanceItem.findBySerial", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.serial = :serial"),
    @NamedQuery(name = "InvOpenningBalanceItem.findByDocumentStrip", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.documentStrip = :documentStrip"),
    @NamedQuery(name = "InvOpenningBalanceItem.findByDate", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.date = :date"),
    @NamedQuery(name = "InvOpenningBalanceItem.findByRemark", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.remark = :remark"),
    @NamedQuery(name = "InvOpenningBalanceItem.findByCompanyId", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.companyId = :companyId"),
    @NamedQuery(name = "InvOpenningBalanceItem.findByModificationDate", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvOpenningBalanceItem.findByCreationDate", query = "SELECT i FROM InvOpenningBalanceItem i WHERE i.creationDate = :creationDate")})
public class InvOpenningBalanceItem extends BaseEntity {

    @Column(name = "serial")
    private Integer serial;
    @Column(name = "document_strip")
    private String documentStrip;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;

    public InvOpenningBalanceItem() {
    }

    public InvOpenningBalanceItem(Integer id) {
        this.id = id;
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
        if (!(object instanceof InvOpenningBalanceItem)) {
            return false;
        }
        InvOpenningBalanceItem other = (InvOpenningBalanceItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serial != null ? serial.toString() : "";
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getDocumentStrip() {
        return documentStrip;
    }

    public void setDocumentStrip(String documentStrip) {
        this.documentStrip = documentStrip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

}
