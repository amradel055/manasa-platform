/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

import com.toby.define.InventoryPricesGroupEnum;

/**
 *
 * @author amr
 */
public class InvInventoryDTO extends BaseEntityDTO {

    private String msg;
    private String code;
    private String name;
    private String typeItem;
    private InventoryPricesGroupEnum sellPriceType;
    private GlAccountDTO accountId;
    private CostCenterDTO costCenterId;
    private GlAdminUnitDTO glAdminUnitId;
    private String daribaValue;
    private String segilValue;
    
     private Integer invType;
    
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

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
        return name + " -- " + code;
    }

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
     * @return the glAdminUnitId
     */
    public GlAdminUnitDTO getGlAdminUnitId() {
        return glAdminUnitId;
    }

    /**
     * @param glAdminUnitId the glAdminUnitId to set
     */
    public void setGlAdminUnitId(GlAdminUnitDTO glAdminUnitId) {
        this.glAdminUnitId = glAdminUnitId;
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
    
}
