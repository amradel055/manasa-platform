package com.toby.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H
 */
@Entity
@Table(name = "pro_production_transaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionTransaction.findAll", query = "SELECT p FROM ProProductionTransaction p"),
    @NamedQuery(name = "ProProductionTransaction.findById", query = "SELECT p FROM ProProductionTransaction p WHERE p.id = :id"),
    @NamedQuery(name = "ProProductionTransaction.findByStatus", query = "SELECT p FROM ProProductionTransaction p WHERE p.status = :status"),
    @NamedQuery(name = "ProProductionTransaction.findByCreationDate", query = "SELECT p FROM ProProductionTransaction p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductionTransaction.findByModificationDate", query = "SELECT p FROM ProProductionTransaction p WHERE p.modificationDate = :modificationDate"),
   /* @NamedQuery(name = "ProProductionTransaction.findByDeletedDate", query = "SELECT p FROM ProProductionTransaction p WHERE p.deletedDate = :deletedDate")*/})
public class ProProductionTransaction extends BaseEntity {
   
    @Size(max = 45)
    @Column(name = "in_organization_site_name")
    private String inOrganizationSiteName;
    
    @JoinColumn(name = "inv_purchase_invoice_id", referencedColumnName = "id")
    @ManyToOne
    private InvPurchaseInvoice invPurchaseInvoiceId;
    
    @JoinColumn(name = "pro_factory_delivery_id", referencedColumnName = "id")
    @ManyToOne
    private ProFactoryDelivery proFactoryDelivery;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "cloth_number")
    private Integer clothNumber;
 
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @JoinColumn(name = "pro_production_id", referencedColumnName = "id")
    @ManyToOne
    private ProProductionStages proProductionId;

    public ProProductionTransaction() {
    }

    public ProProductionTransaction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public InvPurchaseInvoice getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    public void setInvPurchaseInvoiceId(InvPurchaseInvoice invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof ProProductionTransaction)) {
            return false;
        }
        ProProductionTransaction other = (ProProductionTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jokjhui.ProProductionTransaction[ id=" + id + " ]";
    }

    /**
     * @return the proProductionId
     */
    public ProProductionStages getProProductionId() {
        return proProductionId;
    }

    /**
     * @param proProductionId the proProductionId to set
     */
    public void setProProductionId(ProProductionStages proProductionId) {
        this.proProductionId = proProductionId;
    }

    /**
     * @return the inOrganizationSiteName
     */
    public String getInOrganizationSiteName() {
        return inOrganizationSiteName;
    }

    /**
     * @param inOrganizationSiteName the inOrganizationSiteName to set
     */
    public void setInOrganizationSiteName(String inOrganizationSiteName) {
        this.inOrganizationSiteName = inOrganizationSiteName;
    }

    /**
     * @return the proFactoryDelivery
     */
    public ProFactoryDelivery getProFactoryDelivery() {
        return proFactoryDelivery;
    }

    /**
     * @param proFactoryDelivery the proFactoryDelivery to set
     */
    public void setProFactoryDelivery(ProFactoryDelivery proFactoryDelivery) {
        this.proFactoryDelivery = proFactoryDelivery;
    }

    /**
     * @return the clothNumber
     */
    public Integer getClothNumber() {
        return clothNumber;
    }

    /**
     * @param clothNumber the clothNumber to set
     */
    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
    }

   

  
}
