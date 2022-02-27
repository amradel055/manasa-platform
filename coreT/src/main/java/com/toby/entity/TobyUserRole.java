/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "toby_user_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TobyUserRole.findAll", query = "SELECT i FROM TobyUserRole i")
    ,
    @NamedQuery(name = "TobyUserRole.findById", query = "SELECT i FROM TobyUserRole i WHERE i.id = :id")
    ,
    @NamedQuery(name = "TobyUserRole.findByCreationDate", query = "SELECT i FROM TobyUserRole i WHERE i.creationDate = :creationDate")
    ,
    @NamedQuery(name = "TobyUserRole.findByModificationDate", query = "SELECT i FROM TobyUserRole i WHERE i.modificationDate = :modificationDate")})
public class TobyUserRole extends BaseEntity {

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private TobyRole roleId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private TobyUser userId;
    
    @Column(name = "cost_avarage_update")
    private Boolean costAvarageUpdate;    
    @Column(name = "sell_price_update")
    private Boolean sellPriceUpdate;

    @Transient
    private Boolean markEdit;

    @Transient
    private List<GlYear> glYearList;

    @Transient
    private List<GlBank> glBankList;

    @Transient
    private List<InvInventory> inventoryList;
    
    @Transient
    private List<InvInventory> gallaryList;
    
    @Transient
    private List<ProProductionStages> productionStagesList;

    public TobyUserRole() {
    }

    public TobyUserRole(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public TobyRole getRoleId() {
        return roleId;
    }

    public void setRoleId(TobyRole roleId) {
        this.roleId = roleId;
    }

    public TobyUser getUserId() {
        return userId;
    }

    public void setUserId(TobyUser userId) {
        this.userId = userId;
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
        if (!(object instanceof TobyUserRole)) {
            return false;
        }
        TobyUserRole other = (TobyUserRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.TobyUserRole[ id=" + id + " ]";
    }

    /**
     * @return the glYearList
     */
    public List<GlYear> getGlYearList() {
        return glYearList;
    }

    /**
     * @param glYearList the glYearList to set
     */
    public void setGlYearList(List<GlYear> glYearList) {
        this.glYearList = glYearList;
    }

    public List<GlBank> getGlBankList() {
        return glBankList;
    }

    public void setGlBankList(List<GlBank> glBankList) {
        this.glBankList = glBankList;
    }

    public List<InvInventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<InvInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    /**
     * @return the productionStagesList
     */
    public List<ProProductionStages> getProductionStagesList() {
        return productionStagesList;
    }

    /**
     * @param productionStagesList the productionStagesList to set
     */
    public void setProductionStagesList(List<ProProductionStages> productionStagesList) {
        this.productionStagesList = productionStagesList;
    }

    /**
     * @return the gallaryList
     */
    public List<InvInventory> getGallaryList() {
        return gallaryList;
    }

    /**
     * @param gallaryList the gallaryList to set
     */
    public void setGallaryList(List<InvInventory> gallaryList) {
        this.gallaryList = gallaryList;
    }

    /**
     * @return the costAvarageUpdate
     */
    public Boolean getCostAvarageUpdate() {
        return costAvarageUpdate;
    }

    /**
     * @param costAvarageUpdate the costAvarageUpdate to set
     */
    public void setCostAvarageUpdate(Boolean costAvarageUpdate) {
        this.costAvarageUpdate = costAvarageUpdate;
    }

    /**
     * @return the sellPriceUpdate
     */
    public Boolean getSellPriceUpdate() {
        return sellPriceUpdate;
    }

    /**
     * @param sellPriceUpdate the sellPriceUpdate to set
     */
    public void setSellPriceUpdate(Boolean sellPriceUpdate) {
        this.sellPriceUpdate = sellPriceUpdate;
    }
}
