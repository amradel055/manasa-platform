/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hq002
 */
@Entity
@Table(name = "inv_delegator")
@XmlRootElement
public class InventoryDelegator extends BaseEntity {

    @Size(max = 450)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "mobile")
    private String mobile;
    @Size(max = 45)
    @Column(name = "img")
    private String img;
    @Column(name = "type")
    private Integer type;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "allow_discount_limit")
    private BigDecimal allowDiscountLimit;
    @Column(name = "target_sales")
    private BigDecimal targetSales;
    @Column(name = "commission")
    private BigDecimal commission;
    @OneToMany(mappedBy = "delegatorId")
    private Collection<InvOrganizationSite> invOrganizationSiteCollection;
    @OneToMany(mappedBy = "delegatorId")
    private Collection<InvPurchaseOrder> invPurchaseOrderCollection;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public InventoryDelegator() {
    }

    public InventoryDelegator(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getAllowDiscountLimit() {
        return allowDiscountLimit;
    }

    public void setAllowDiscountLimit(BigDecimal allowDiscountLimit) {
        this.allowDiscountLimit = allowDiscountLimit;
    }

    public BigDecimal getTargetSales() {
        return targetSales;
    }

    public void setTargetSales(BigDecimal targetSales) {
        this.targetSales = targetSales;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection() {
        return invOrganizationSiteCollection;
    }

    public void setInvOrganizationSiteCollection(Collection<InvOrganizationSite> invOrganizationSiteCollection) {
        this.invOrganizationSiteCollection = invOrganizationSiteCollection;
    }

    @XmlTransient
    public Collection<InvPurchaseOrder> getInvPurchaseOrderCollection() {
        return invPurchaseOrderCollection;
    }

    public void setInvPurchaseOrderCollection(Collection<InvPurchaseOrder> invPurchaseOrderCollection) {
        this.invPurchaseOrderCollection = invPurchaseOrderCollection;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
        if (!(object instanceof InventoryDelegator)) {
            return false;
        }
        InventoryDelegator other = (InventoryDelegator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

}
