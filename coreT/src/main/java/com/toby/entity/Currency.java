/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "currency")
@XmlRootElement
public class Currency extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Size(max = 5)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "serial")
    private Integer serial;
    @OneToMany(mappedBy = "currencyId")
    private List<CurrencyOperation> currencyOperationList;
    @OneToMany(mappedBy = "currencyId")
    private List<GlAccount> glAccountList;
    @OneToMany(mappedBy = "currencyId")
    private Collection<InvPurchaseOrder> invPurchaseOrderCollection;

    @Transient
    private Boolean markEdit;

    public Currency() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlTransient
    public List<CurrencyOperation> getCurrencyOperationList() {
        return currencyOperationList;
    }

    public void setCurrencyOperationList(List<CurrencyOperation> currencyOperationList) {
        this.currencyOperationList = currencyOperationList;
    }

    @XmlTransient
    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
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
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    @XmlTransient
    public Collection<InvPurchaseOrder> getInvPurchaseOrderCollection() {
        return invPurchaseOrderCollection;
    }

    public void setInvPurchaseOrderCollection(Collection<InvPurchaseOrder> invPurchaseOrderCollection) {
        this.invPurchaseOrderCollection = invPurchaseOrderCollection;
    }

}
