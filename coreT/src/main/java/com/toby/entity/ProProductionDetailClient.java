
package com.toby.entity;


import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "pro_production_detail_client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionDetailClient.findAll", query = "SELECT p FROM ProProductionDetailClient p"),
    @NamedQuery(name = "ProProductionDetailClient.findById", query = "SELECT p FROM ProProductionDetailClient p WHERE p.id = :id"),
    @NamedQuery(name = "ProProductionDetailClient.findByCreationDate", query = "SELECT p FROM ProProductionDetailClient p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductionDetailClient.findByModificationDate", query = "SELECT p FROM ProProductionDetailClient p WHERE p.modificationDate = :modificationDate")})
public class ProProductionDetailClient extends BaseEntity{
   
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @JoinColumn(name = "pro_production_client_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProProductionMovementClient proProductionClientId;
    
    @JoinColumn(name = "inv_purches_invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvPurchaseInvoice invPurchesInvoiceId;
   

    public ProProductionDetailClient() {
    }

    public ProProductionDetailClient(Integer id) {
        this.id = id;
    }

   
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public ProProductionMovementClient getProProductionClientId() {
        return proProductionClientId;
    }

    public void setProProductionClientId(ProProductionMovementClient proProductionClientId) {
        this.proProductionClientId = proProductionClientId;
    }

    public InvPurchaseInvoice getInvPurchesInvoiceId() {
        return invPurchesInvoiceId;
    }

    public void setInvPurchesInvoiceId(InvPurchaseInvoice invPurchesInvoiceId) {
        this.invPurchesInvoiceId = invPurchesInvoiceId;
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
        if (!(object instanceof ProProductionDetailClient)) {
            return false;
        }
        ProProductionDetailClient other = (ProProductionDetailClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProProductionDetailClient[ id=" + id + " ]";
    }
    
}
