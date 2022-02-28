package com.toby.entity;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Toby
 */
@Entity
@Table(name = "pro_product_movement_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductMovementDetail.findAll", query = "SELECT p FROM ProProductMovementDetail p"),
    @NamedQuery(name = "ProProductMovementDetail.findById", query = "SELECT p FROM ProProductMovementDetail p WHERE p.id = :id"),
    @NamedQuery(name = "ProProductMovementDetail.findBySerial", query = "SELECT p FROM ProProductMovementDetail p WHERE p.serial = :serial"),
    @NamedQuery(name = "ProProductMovementDetail.findByCreationDate", query = "SELECT p FROM ProProductMovementDetail p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductMovementDetail.findByModificationDate", query = "SELECT p FROM ProProductMovementDetail p WHERE p.modificationDate = :modificationDate")})
public class ProProductMovementDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "serial")
    private Integer serial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TobyCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser createdBy;
    @JoinColumn(name = "inv_purches_invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvPurchaseInvoice invPurchesInvoiceId;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser modifiedBy;
    @JoinColumn(name = "pro_production_delivery_id", referencedColumnName = "id")
    @ManyToOne
    private ProProductMovement proProductionDeliveryId;

    public ProProductMovementDetail() {
    }

    public ProProductMovementDetail(Integer id) {
        this.id = id;
    }

    public ProProductMovementDetail(Integer id, Date creationDate) {
        this.id = id;
        this.creationDate = creationDate;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public TobyCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(TobyCompany companyId) {
        this.companyId = companyId;
    }

    public TobyUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(TobyUser createdBy) {
        this.createdBy = createdBy;
    }

    public InvPurchaseInvoice getInvPurchesInvoiceId() {
        return invPurchesInvoiceId;
    }

    public void setInvPurchesInvoiceId(InvPurchaseInvoice invPurchesInvoiceId) {
        this.invPurchesInvoiceId = invPurchesInvoiceId;
    }

    public TobyUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(TobyUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ProProductMovement getProProductionDeliveryId() {
        return proProductionDeliveryId;
    }

    public void setProProductionDeliveryId(ProProductMovement proProductionDeliveryId) {
        this.proProductionDeliveryId = proProductionDeliveryId;
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
        if (!(object instanceof ProProductMovementDetail)) {
            return false;
        }
        ProProductMovementDetail other = (ProProductMovementDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ProProductMovementDetail[ id=" + id + " ]";
    }
    
}
