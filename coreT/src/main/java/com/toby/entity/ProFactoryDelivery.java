/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author User
 */
@Entity
@Table(name = "pro_factory_delivery")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProFactoryDelivery.findAll", query = "SELECT p FROM ProFactoryDelivery p")
    , @NamedQuery(name = "ProFactoryDelivery.findById", query = "SELECT p FROM ProFactoryDelivery p WHERE p.id = :id")
    , @NamedQuery(name = "ProFactoryDelivery.findByDate", query = "SELECT p FROM ProFactoryDelivery p WHERE p.date = :date")
    , @NamedQuery(name = "ProFactoryDelivery.findByTime", query = "SELECT p FROM ProFactoryDelivery p WHERE p.time = :time")
    , @NamedQuery(name = "ProFactoryDelivery.findByModificationDate", query = "SELECT p FROM ProFactoryDelivery p WHERE p.modificationDate = :modificationDate")
    , @NamedQuery(name = "ProFactoryDelivery.findByCreationDate", query = "SELECT p FROM ProFactoryDelivery p WHERE p.creationDate = :creationDate")
    , @NamedQuery(name = "ProFactoryDelivery.findBySerial", query = "SELECT p FROM ProFactoryDelivery p WHERE p.serial = :serial")})
public class ProFactoryDelivery extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHegry;
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @Basic(optional = false)
    @Column(name = "serial")
    private Integer serial;    
    @Basic(optional = false)
    @Column(name = "type")
    private Integer type;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;
    @JoinColumn(name = "factory_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvInventory factoryId;    
    @Size(max = 100)
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "gallary_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvInventory gallaryId;
    @JoinColumn(name = "inv_purchase_invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvPurchaseInvoice invPurchaseInvoiceId;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProFactoryDelivery parent;

    public ProFactoryDelivery() {
    }

    public ProFactoryDelivery(Integer id) {
        this.id = id;
    }

    public ProFactoryDelivery(Integer id, Date date, Date creationDate, int serial) {
        this.id = id;
        this.date = date;
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(InvInventory factoryId) {
        this.factoryId = factoryId;
    }

    public InvInventory getGallaryId() {
        return gallaryId;
    }

    public void setGallaryId(InvInventory gallaryId) {
        this.gallaryId = gallaryId;
    }

    public InvPurchaseInvoice getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    public void setInvPurchaseInvoiceId(InvPurchaseInvoice invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
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
        if (!(object instanceof ProFactoryDelivery)) {
            return false;
        }
        ProFactoryDelivery other = (ProFactoryDelivery) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ccc.ProFactoryDelivery[ id=" + id + " ]";
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
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

    /**
     * @return the parent
     */
    public ProFactoryDelivery getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(ProFactoryDelivery parent) {
        this.parent = parent;
    }

    /**
     * @return the dateHegry
     */
    public Date getDateHegry() {
        return dateHegry;
    }

    /**
     * @param dateHegry the dateHegry to set
     */
    public void setDateHegry(Date dateHegry) {
        this.dateHegry = dateHegry;
    }
    
}
