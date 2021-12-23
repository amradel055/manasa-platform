/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

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
@Table(name = "isag_user_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IsagUserRole.findAll", query = "SELECT i FROM IsagUserRole i")
    ,
    @NamedQuery(name = "IsagUserRole.findById", query = "SELECT i FROM IsagUserRole i WHERE i.id = :id")
    ,
    @NamedQuery(name = "IsagUserRole.findByCreationDate", query = "SELECT i FROM IsagUserRole i WHERE i.creationDate = :creationDate")
    ,
    @NamedQuery(name = "IsagUserRole.findByModificationDate", query = "SELECT i FROM IsagUserRole i WHERE i.modificationDate = :modificationDate")})
public class IsagUserRole extends BaseEntity {

    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private IsagRole roleId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private IsagUser userId;  
    
    @Column(name = "cost_avarage_update")
    private Boolean costAvarageUpdate;    
    @Column(name = "sell_price_update")
    private Boolean sellPriceUpdate;  
    @Column(name = "bank_update")
    private Boolean bankUpdate;
    @Column(name = "client_show")
    private Boolean clientShow;
    @Column(name = "price_update")
    private Boolean priceUpdate;
    @Transient
    private Boolean markEdit;

    @Transient
    private List<GlYear> glYearList;

    @Transient
    private List<GlBank> glBankList;

    @Transient
    private List<InvInventory> inventoryList;
    
    @Transient
    private List<Employee> employeeList;

    public IsagUserRole() {
    }

    public IsagUserRole(Integer id) {
        this.id = id;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public IsagRole getRoleId() {
        return roleId;
    }

    public void setRoleId(IsagRole roleId) {
        this.roleId = roleId;
    }

    public IsagUser getUserId() {
        return userId;
    }

    public void setUserId(IsagUser userId) {
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
        if (!(object instanceof IsagUserRole)) {
            return false;
        }
        IsagUserRole other = (IsagUserRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.IsagUserRole[ id=" + id + " ]";
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
     * @return the employeeList
     */
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * @param employeeList the employeeList to set
     */
    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
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

    /**
     * @return the bankUpdate
     */
    public Boolean getBankUpdate() {
        if(bankUpdate == null){
        bankUpdate = Boolean.FALSE;
        }
        return bankUpdate;
    }

    /**
     * @param bankUpdate the bankUpdate to set
     */
    public void setBankUpdate(Boolean bankUpdate) {
        this.bankUpdate = bankUpdate;
    }

    /**
     * @return the clientShow
     */
    public Boolean getClientShow() {
        if(clientShow == null){
        clientShow = Boolean.FALSE;
        }
        return clientShow;
    }

    /**
     * @param clientShow the clientShow to set
     */
    public void setClientShow(Boolean clientShow) {
        this.clientShow = clientShow;
    }

    /**
     * @return the priceUpdate
     */
    public Boolean getPriceUpdate() {
        if(priceUpdate == null){
        priceUpdate = Boolean.FALSE;
        }
        return priceUpdate;
    }

    /**
     * @param priceUpdate the priceUpdate to set
     */
    public void setPriceUpdate(Boolean priceUpdate) {
        this.priceUpdate = priceUpdate;
    }
}
