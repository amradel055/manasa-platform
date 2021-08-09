/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.dto;

import com.isag.define.InventoryPricesGroupEnum;

/**
 *
 * @author amr
 */
public class InvInventoryDTO extends BaseEntityDTO{

    private String msg;
    private String code;
    private String name;
    private String typeItem;
    private InventoryPricesGroupEnum sellPriceType;
    private GlAccountDTO accountId;
    private CostCenterDTO costCenterId;
    private GlAdminUnitDTO adminUnitId;
    private Integer invName;
    
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
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
     * @return the typeItem
     */
    public String getTypeItem() {
        return typeItem;
    }

    /**
     * @param typeItem the typeItem to set
     */
    public void setTypeItem(String typeItem) {
        this.typeItem = typeItem;
    }

    /**
     * @return the sellPriceType
     */
    public InventoryPricesGroupEnum getSellPriceType() {
        return sellPriceType;
    }

    /**
     * @param sellPriceType the sellPriceType to set
     */
    public void setSellPriceType(InventoryPricesGroupEnum sellPriceType) {
        this.sellPriceType = sellPriceType;
    }

    /**
     * @return the accountId
     */
    public GlAccountDTO getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(GlAccountDTO accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the costCenterId
     */
    public CostCenterDTO getCostCenterId() {
        return costCenterId;
    }

    /**
     * @param costCenterId the costCenterId to set
     */
    public void setCostCenterId(CostCenterDTO costCenterId) {
        this.costCenterId = costCenterId;
    }

    /**
     * @return the adminUnitId
     */
    public GlAdminUnitDTO getAdminUnitId() {
        return adminUnitId;
    }

    /**
     * @param adminUnitId the adminUnitId to set
     */
    public void setAdminUnitId(GlAdminUnitDTO adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvInventoryDTO)) {
            return false;
        }
        InvInventoryDTO other = (InvInventoryDTO) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
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
