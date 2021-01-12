package com.toby.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "pro_item_production_stages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProItemProductionStages.findAll", query = "SELECT p FROM ProItemProductionStages p"),
    @NamedQuery(name = "ProItemProductionStages.findById", query = "SELECT p FROM ProItemProductionStages p WHERE p.id = :id"),
    @NamedQuery(name = "ProItemProductionStages.findByCreationDate", query = "SELECT p FROM ProItemProductionStages p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProItemProductionStages.findByModificationDate", query = "SELECT p FROM ProItemProductionStages p WHERE p.modificationDate = :modificationDate"),
    @NamedQuery(name = "ProItemProductionStages.findBySerial", query = "SELECT p FROM ProItemProductionStages p WHERE p.serial = :serial")})
public class ProItemProductionStages extends BaseEntity {
   
    
    @Column(name = "serial")
    private Integer serial;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "inv_item_id", referencedColumnName = "id")
    @ManyToOne
    private InvItem invItemId;
    @JoinColumn(name = "pro_production_stages_id", referencedColumnName = "id")
    @ManyToOne
    private ProProductionStages proProductionStagesId;

    public ProItemProductionStages() {
    }

    public ProItemProductionStages(Integer id) {
        this.id = id;
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

    
    public InvItem getInvItemId() {
        return invItemId;
    }

    public void setInvItemId(InvItem invItemId) {
        this.invItemId = invItemId;
    }

   
    public ProProductionStages getProProductionStagesId() {
        return proProductionStagesId;
    }

    public void setProProductionStagesId(ProProductionStages proProductionStagesId) {
        this.proProductionStagesId = proProductionStagesId;
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
        if (!(object instanceof ProItemProductionStages)) {
            return false;
        }
        ProItemProductionStages other = (ProItemProductionStages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "newpackage.ProItemProductionStages[ id=" + id + " ]";
    }
    
}
