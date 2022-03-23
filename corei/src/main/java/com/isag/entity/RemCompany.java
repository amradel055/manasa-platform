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
@Table(name = "rem_company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemCompany.findAll", query = "SELECT r FROM RemCompany r")
    , @NamedQuery(name = "RemCompany.findById", query = "SELECT r FROM RemCompany r WHERE r.id = :id")
    , @NamedQuery(name = "RemCompany.findByCode", query = "SELECT r FROM RemCompany r WHERE r.code = :code")
    , @NamedQuery(name = "RemCompany.findByName", query = "SELECT r FROM RemCompany r WHERE r.name = :name")
    , @NamedQuery(name = "RemCompany.findByAddress", query = "SELECT r FROM RemCompany r WHERE r.address = :address")
    , @NamedQuery(name = "RemCompany.findByPhone", query = "SELECT r FROM RemCompany r WHERE r.phone = :phone")
    , @NamedQuery(name = "RemCompany.findByCreationDate", query = "SELECT r FROM RemCompany r WHERE r.creationDate = :creationDate")
    , @NamedQuery(name = "RemCompany.findByModificationDate", query = "SELECT r FROM RemCompany r WHERE r.modificationDate = :modificationDate")})
public class RemCompany extends BaseEntity{

    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 45)
    @Column(name = "phone")
    private String phone;    
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public RemCompany() {
    }

    public RemCompany(Integer id) {
        this.id = id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        if (!(object instanceof RemCompany)) {
            return false;
        }
        RemCompany other = (RemCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.RemCompany[ id=" + id + " ]";
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
    
}
