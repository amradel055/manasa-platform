package com.toby.entity;

import java.math.BigDecimal;
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
@Table(name = "pro_production_stages")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionStages.findAll", query = "SELECT p FROM ProProductionStages p"),
    @NamedQuery(name = "ProProductionStages.findById", query = "SELECT p FROM ProProductionStages p WHERE p.id = :id"),
    @NamedQuery(name = "ProProductionStages.findByName", query = "SELECT p FROM ProProductionStages p WHERE p.name = :name"),
    @NamedQuery(name = "ProProductionStages.findByNameEn", query = "SELECT p FROM ProProductionStages p WHERE p.nameEn = :nameEn"),
    @NamedQuery(name = "ProProductionStages.findByNameIn", query = "SELECT p FROM ProProductionStages p WHERE p.nameIn = :nameIn"),
    @NamedQuery(name = "ProProductionStages.findByPrice", query = "SELECT p FROM ProProductionStages p WHERE p.price = :price"),
    @NamedQuery(name = "ProProductionStages.findByCreationDate", query = "SELECT p FROM ProProductionStages p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductionStages.findByModificationDate", query = "SELECT p FROM ProProductionStages p WHERE p.modificationDate = :modificationDate"),
    @NamedQuery(name = "ProProductionStages.findByHostName", query = "SELECT p FROM ProProductionStages p WHERE p.hostName = :hostName")})
public class ProProductionStages extends BaseEntity {

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "nameEn")
    private String nameEn;
    @Size(max = 45)
    @Column(name = "nameIn")
    private String nameIn;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @Size(max = 45)
    @Column(name = "host_name")
    private String hostName;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "type_stage")
    private Integer typeStage;

    public ProProductionStages() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameIn() {
        return nameIn;
    }

    public void setNameIn(String nameIn) {
        this.nameIn = nameIn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Integer getSerial() {
        return serial;
    }

    
    public void setSerial(Integer serial) {
        this.serial = serial;
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
        if (!(object instanceof ProProductionStages)) {
            return false;
        }
        ProProductionStages other = (ProProductionStages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jokjhui.ProProductionStages[ id=" + id + " ]";
    }

   
    public Integer getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(Integer typeStage) {
        this.typeStage = typeStage;
    }

   
    
    
}
