
package com.toby.entity;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "pro_production_movement_client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionMovementClient.findAll", query = "SELECT p FROM ProProductionMovementClient p"),
    @NamedQuery(name = "ProProductionMovementClient.findById", query = "SELECT p FROM ProProductionMovementClient p WHERE p.id = :id"),
    @NamedQuery(name = "ProProductionMovementClient.findByDate", query = "SELECT p FROM ProProductionMovementClient p WHERE p.date = :date"),
    @NamedQuery(name = "ProProductionMovementClient.findByTime", query = "SELECT p FROM ProProductionMovementClient p WHERE p.time = :time"),
    @NamedQuery(name = "ProProductionMovementClient.findByCreationDate", query = "SELECT p FROM ProProductionMovementClient p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductionMovementClient.findByModificationDate", query = "SELECT p FROM ProProductionMovementClient p WHERE p.modificationDate = :modificationDate")})
public class ProProductionMovementClient extends BaseEntity {
    
  
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Size(max = 45)
    @Column(name = "time")
    private String time;
    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    
    @JoinColumn(name = "inv_organization_site_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private InvOrganizationSite invOrganizationSiteId;


    public ProProductionMovementClient() {
    }

    public ProProductionMovementClient(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
        if (!(object instanceof ProProductionMovementClient)) {
            return false;
        }
        ProProductionMovementClient other = (ProProductionMovementClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProProductionMovementClient[ id=" + id + " ]";
    }

   
    public InvOrganizationSite getInvOrganizationSiteId() {
        return invOrganizationSiteId;
    }

    
    public void setInvOrganizationSiteId(InvOrganizationSite invOrganizationSiteId) {
        this.invOrganizationSiteId = invOrganizationSiteId;
    }
    
}
