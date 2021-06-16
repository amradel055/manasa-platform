/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import com.toby.define.InventoryPricesGroupEnum;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author WIN7
 */
@Entity
@Table(name = "inv_inventory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvInventory.findAll", query = "SELECT i FROM InvInventory i"),
    @NamedQuery(name = "InvInventory.findById", query = "SELECT i FROM InvInventory i WHERE i.id = :id"),
    @NamedQuery(name = "InvInventory.findByCode", query = "SELECT i FROM InvInventory i WHERE i.code = :code"),
    @NamedQuery(name = "InvInventory.findByName", query = "SELECT i FROM InvInventory i WHERE i.name = :name"),
    @NamedQuery(name = "InvInventory.findByTypeItem", query = "SELECT i FROM InvInventory i WHERE i.typeItem = :typeItem"),
    @NamedQuery(name = "InvInventory.findBySellPriceType", query = "SELECT i FROM InvInventory i WHERE i.sellPriceType = :sellPriceType"),
    @NamedQuery(name = "InvInventory.findByModificationDate", query = "SELECT i FROM InvInventory i WHERE i.modificationDate = :modificationDate"),
    @NamedQuery(name = "InvInventory.findByCreationDate", query = "SELECT i FROM InvInventory i WHERE i.creationDate = :creationDate")})
public class InvInventory extends BaseEntity {

    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "type_item")
    private String typeItem;
    @Column(name = "sell_price_type")
    @Enumerated(EnumType.STRING)
    private InventoryPricesGroupEnum sellPriceType;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "cost_center_id", referencedColumnName = "id")
    @ManyToOne
    private CostCenter costCenterId;
    @JoinColumn(name = "admin_unit_id", referencedColumnName = "id")
    @ManyToOne
    private GlAdminUnit adminUnitId;
     @Column(name = "inv_type")
     private Integer invType;
     @Column(name = "dariba_value")
    private String daribaValue;
    @Column(name = "segil_value")
    private String segilValue;
    @Column(name = "inv_name")
    private Integer invName;

    public InvInventory() {
    }

    public InvInventory(Integer id) {
        this.id = id;
    }

    public InvInventory(Integer id, String name, Date creationDate) {
        this.id = id;
        this.name = name;
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

    public String getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(String typeItem) {
        this.typeItem = typeItem;
    }

    public GlAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public CostCenter getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(CostCenter costCenterId) {
        this.costCenterId = costCenterId;
    }

    public GlAdminUnit getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(GlAdminUnit adminUnitId) {
        this.adminUnitId = adminUnitId;
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
        if (!(object instanceof InvInventory)) {
            return false;
        }
        InvInventory other = (InvInventory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public InventoryPricesGroupEnum getSellPriceType() {
        return sellPriceType;
    }

    public void setSellPriceType(InventoryPricesGroupEnum sellPriceType) {
        this.sellPriceType = sellPriceType;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    /**
     * @return the invType
     */
    public Integer getInvType() {
        return invType;
    }

    /**
     * @param invType the invType to set
     */
    public void setInvType(Integer invType) {
        this.invType = invType;
    }

    /**
     * @return the daribaValue
     */
    public String getDaribaValue() {
        return daribaValue;
    }

    /**
     * @param daribaValue the daribaValue to set
     */
    public void setDaribaValue(String daribaValue) {
        this.daribaValue = daribaValue;
    }

    /**
     * @return the segilValue
     */
    public String getSegilValue() {
        return segilValue;
    }

    /**
     * @param segilValue the segilValue to set
     */
    public void setSegilValue(String segilValue) {
        this.segilValue = segilValue;
    }

    /**
     * @return the invName
     */
    public Integer getInvName() {
        return invName;
    }

    /**
     * @param invName the invName to set
     */
    public void setInvName(Integer invName) {
        this.invName = invName;
    }
}
