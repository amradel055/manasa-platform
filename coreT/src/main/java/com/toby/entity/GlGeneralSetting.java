/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "gl_general_setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlGeneralSetting.findAll", query = "SELECT g FROM GlGeneralSetting g"),
    @NamedQuery(name = "GlGeneralSetting.findById", query = "SELECT g FROM GlGeneralSetting g WHERE g.id = :id"),
    @NamedQuery(name = "GlGeneralSetting.findByVoucherBalance", query = "SELECT g FROM GlGeneralSetting g WHERE g.voucherBalance = :voucherBalance"),
    @NamedQuery(name = "GlGeneralSetting.findByVoucherUpdate", query = "SELECT g FROM GlGeneralSetting g WHERE g.voucherUpdate = :voucherUpdate"),
    @NamedQuery(name = "GlGeneralSetting.findBySummaryAppear", query = "SELECT g FROM GlGeneralSetting g WHERE g.summaryAppear = :summaryAppear"),
    @NamedQuery(name = "GlGeneralSetting.findByCreationDate", query = "SELECT g FROM GlGeneralSetting g WHERE g.creationDate = :creationDate"),
    @NamedQuery(name = "GlGeneralSetting.findByModificationDate", query = "SELECT g FROM GlGeneralSetting g WHERE g.modificationDate = :modificationDate")})
public class GlGeneralSetting extends BaseEntity{

    @Column(name = "voucher_balance")
    private Boolean voucherBalance;
    @Column(name = "voucher_update")
    private Boolean voucherUpdate;
    @Column(name = "summary_appear")
    private Boolean summaryAppear;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;

    public GlGeneralSetting() {
    }

    public GlGeneralSetting(Integer id) {
        this.id = id;
    }

    public Boolean getVoucherBalance() {
        return voucherBalance;
    }

    public void setVoucherBalance(Boolean voucherBalance) {
        this.voucherBalance = voucherBalance;
    }

    public Boolean getVoucherUpdate() {
        return voucherUpdate;
    }

    public void setVoucherUpdate(Boolean voucherUpdate) {
        this.voucherUpdate = voucherUpdate;
    }

    public Boolean getSummaryAppear() {
        return summaryAppear;
    }

    public void setSummaryAppear(Boolean summaryAppear) {
        this.summaryAppear = summaryAppear;
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
        if (!(object instanceof GlGeneralSetting)) {
            return false;
        }
        GlGeneralSetting other = (GlGeneralSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.GlGeneralSetting[ id=" + id + " ]";
    }
    
}
