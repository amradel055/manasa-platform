/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
 * @author IT Shop
 */
@Entity
@Table(name = "rem_traffic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemTraffic.findAll", query = "SELECT r FROM RemTraffic r")
    , @NamedQuery(name = "RemTraffic.findById", query = "SELECT r FROM RemTraffic r WHERE r.id = :id")
    , @NamedQuery(name = "RemTraffic.findByName", query = "SELECT r FROM RemTraffic r WHERE r.name = :name")
    , @NamedQuery(name = "RemTraffic.findByCreationDate", query = "SELECT r FROM RemTraffic r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemTraffic.findByModificationDate", query = "SELECT r FROM RemTraffic r WHERE r.modificationDate = :modificationDate")
    , @NamedQuery(name = "RemTraffic.findByCode", query = "SELECT r FROM RemTraffic r WHERE r.code = :code")})
public class RemTraffic extends BaseEntity{

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private Integer code;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "rem_company_id", referencedColumnName = "id")
    @ManyToOne
    private RemCompany remCompanyId;

    public RemTraffic() {
    }

    public RemTraffic(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
        if (!(object instanceof RemTraffic)) {
            return false;
        }
        RemTraffic other = (RemTraffic) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemTraffic[ id=" + id + " ]";
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the remCompanyId
     */
    public RemCompany getRemCompanyId() {
        return remCompanyId;
    }

    /**
     * @param remCompanyId the remCompanyId to set
     */
    public void setRemCompanyId(RemCompany remCompanyId) {
        this.remCompanyId = remCompanyId;
    }
    
}
