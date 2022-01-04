/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "gl_cost_center")
@XmlRootElement
public class CostCenter extends BaseEntity {

    @Column(name = "code")
    private Integer code;
    @Column(name = "name")
    private String name;
    @Column(name = "short_code")
    private Integer shortCode;
    @Column(name = "level")
    private Integer level;
    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<CostCenter> childNodes;

    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private CostCenter parent;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "currentaccount_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount currentaccountId;

    public CostCenter() {
    }

    public CostCenter(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShortCode() {
        return shortCode;
    }

    public void setShortCode(Integer shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CostCenter getParent() {
        return parent;
    }

    public void setParent(CostCenter parent) {
        this.parent = parent;
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
        if (!(object instanceof CostCenter)) {
            return false;
        }
        CostCenter other = (CostCenter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the costCenter
     */
    public String getCostCenter() {
        return code + " " + name;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(String costCenter) {
    }

    /**
     * @return the childNodes
     */
    public List<CostCenter> getChildNodes() {
        return childNodes;
    }

    /**
     * @param childNodes the childNodes to set
     */
    public void setChildNodes(List<CostCenter> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * @return the currentaccountId
     */
    public GlAccount getCurrentaccountId() {
        return currentaccountId;
    }

    /**
     * @param currentaccountId the currentaccountId to set
     */
    public void setCurrentaccountId(GlAccount currentaccountId) {
        this.currentaccountId = currentaccountId;
    }

}
