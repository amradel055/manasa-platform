/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import com.toby.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author H
 */
@Entity
@Table(name = "pro_production_items_transaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionItemsTransaction.findAll", query = "SELECT p FROM ProProductionItemsTransaction p"),
    @NamedQuery(name = "ProProductionItemsTransaction.findById", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.id = :id"),
    @NamedQuery(name = "ProProductionItemsTransaction.findByCreationDate", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductionItemsTransaction.findByModificationDate", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.modificationDate = :modificationDate"),
    @NamedQuery(name = "ProProductionItemsTransaction.findBySerial", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.serial = :serial"),
    @NamedQuery(name = "ProProductionItemsTransaction.findByNumber", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.number = :number"),
    @NamedQuery(name = "ProProductionItemsTransaction.findByProductionStageCost", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.productionStageCost = :productionStageCost"),
    @NamedQuery(name = "ProProductionItemsTransaction.findByDate", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.date = :date"),
    @NamedQuery(name = "ProProductionItemsTransaction.findByStatus", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.status = :status"),
    @NamedQuery(name = "ProProductionItemsTransaction.findByIsDeleted", query = "SELECT p FROM ProProductionItemsTransaction p WHERE p.isDeleted = :isDeleted")})
public class ProProductionItemsTransaction extends BaseEntity{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Column(name = "serial")
    private Integer serial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "number")
    private BigDecimal number;
    @Basic(optional = false)
    @NotNull
    @Column(name = "production_stage_cost")
    private BigDecimal productionStageCost;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "status")
    private Integer status;
    @Column(name = "is_deleted")
    private Integer isDeleted;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TobyCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TobyUser createdBy;
    @JoinColumn(name = "inv_purchase_invoice_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvPurchaseInvoiceDetail invPurchaseInvoiceDetailId;
    @JoinColumn(name = "pro_factory_delivery_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProFactoryDeliveryDetail proFactoryDeliveryDetailId;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser modifiedBy;
    @JoinColumn(name = "pro_production_stages_id", referencedColumnName = "id")
    @ManyToOne
    private ProProductionStages proProductionStagesId;
    @JoinColumn(name = "pro_production_transaction_id", referencedColumnName = "id")
    @ManyToOne
    private ProProductionTransaction proProductionTransactionId;

    public ProProductionItemsTransaction() {
    }

    public ProProductionItemsTransaction(Integer id) {
        this.id = id;
    }

    public ProProductionItemsTransaction(Integer id, Date creationDate, BigDecimal productionStageCost) {
        this.id = id;
        this.creationDate = creationDate;
        this.productionStageCost = productionStageCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getProductionStageCost() {
        return productionStageCost;
    }

    public void setProductionStageCost(BigDecimal productionStageCost) {
        this.productionStageCost = productionStageCost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public InvPurchaseInvoiceDetail getInvPurchaseInvoiceDetailId() {
        return invPurchaseInvoiceDetailId;
    }

    public void setInvPurchaseInvoiceDetailId(InvPurchaseInvoiceDetail invPurchaseInvoiceDetailId) {
        this.invPurchaseInvoiceDetailId = invPurchaseInvoiceDetailId;
    }

    public TobyUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(TobyUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ProProductionStages getProProductionStagesId() {
        return proProductionStagesId;
    }

    public void setProProductionStagesId(ProProductionStages proProductionStagesId) {
        this.proProductionStagesId = proProductionStagesId;
    }

    public ProProductionTransaction getProProductionTransactionId() {
        return proProductionTransactionId;
    }

    public void setProProductionTransactionId(ProProductionTransaction proProductionTransactionId) {
        this.proProductionTransactionId = proProductionTransactionId;
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
        if (!(object instanceof ProProductionItemsTransaction)) {
            return false;
        }
        ProProductionItemsTransaction other = (ProProductionItemsTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ProProductionItemsTransaction[ id=" + id + " ]";
    }

    /**
     * @return the proFactoryDeliveryDetailId
     */
    public ProFactoryDeliveryDetail getProFactoryDeliveryDetailId() {
        return proFactoryDeliveryDetailId;
    }

    /**
     * @param proFactoryDeliveryDetailId the proFactoryDeliveryDetailId to set
     */
    public void setProFactoryDeliveryDetailId(ProFactoryDeliveryDetail proFactoryDeliveryDetailId) {
        this.proFactoryDeliveryDetailId = proFactoryDeliveryDetailId;
    }
    
}
