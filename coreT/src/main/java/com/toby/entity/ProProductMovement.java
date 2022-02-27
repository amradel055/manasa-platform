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
 * @author H
 */
@Entity
@Table(name = "pro_product_movement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductMovement.findAll", query = "SELECT p FROM ProProductMovement p"),
    @NamedQuery(name = "ProProductMovement.findById", query = "SELECT p FROM ProProductMovement p WHERE p.id = :id"),
    @NamedQuery(name = "ProProductMovement.findByDate", query = "SELECT p FROM ProProductMovement p WHERE p.date = :date"),
    @NamedQuery(name = "ProProductMovement.findByRemark", query = "SELECT p FROM ProProductMovement p WHERE p.remark = :remark"),
    @NamedQuery(name = "ProProductMovement.findByType", query = "SELECT p FROM ProProductMovement p WHERE p.type = :type"),
    @NamedQuery(name = "ProProductMovement.findBySerial", query = "SELECT p FROM ProProductMovement p WHERE p.serial = :serial"),
    @NamedQuery(name = "ProProductMovement.findByCreationDate", query = "SELECT p FROM ProProductMovement p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductMovement.findByModificationDate", query = "SELECT p FROM ProProductMovement p WHERE p.modificationDate = :modificationDate"),
    @NamedQuery(name = "ProProductMovement.findByTime", query = "SELECT p FROM ProProductMovement p WHERE p.time = :time")})
public class ProProductMovement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;
    @Column(name = "type")
    private Integer type;
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
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Branch branchId;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TobyCompany companyId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser createdBy;
    @JoinColumn(name = "inv_galary_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invGalaryId;
    @JoinColumn(name = "inv_inventory_id", referencedColumnName = "id")
    @ManyToOne
    private InvInventory invInventoryId;
    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private TobyUser modifiedBy;
    @OneToMany(mappedBy = "parent")
    private Collection<ProProductMovement> proProductMovementCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private ProProductMovement parent;
    @JoinColumn(name = "inv_organization_site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite invOrganizationSiteId;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite client;

    public ProProductMovement() {
    }

    public ProProductMovement(Integer id) {
        this.id = id;
    }

    public ProProductMovement(Integer id, Date creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public InvInventory getInvGalaryId() {
        return invGalaryId;
    }

    public void setInvGalaryId(InvInventory invGalaryId) {
        this.invGalaryId = invGalaryId;
    }

    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public TobyUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(TobyUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @XmlTransient
    public Collection<ProProductMovement> getProProductMovementCollection() {
        return proProductMovementCollection;
    }

    public void setProProductMovementCollection(Collection<ProProductMovement> proProductMovementCollection) {
        this.proProductMovementCollection = proProductMovementCollection;
    }

    public ProProductMovement getParent() {
        return parent;
    }

    public void setParent(ProProductMovement parent) {
        this.parent = parent;
    }

    public InvOrganizationSite getInvOrganizationSiteId() {
        return invOrganizationSiteId;
    }

    public void setInvOrganizationSiteId(InvOrganizationSite invOrganizationSiteId) {
        this.invOrganizationSiteId = invOrganizationSiteId;
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
        if (!(object instanceof ProProductMovement)) {
            return false;
        }
        ProProductMovement other = (ProProductMovement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ProProductMovement[ id=" + id + " ]";
    }

    /**
     * @return the client
     */
    public InvOrganizationSite getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(InvOrganizationSite client) {
        this.client = client;
    }
    
}
