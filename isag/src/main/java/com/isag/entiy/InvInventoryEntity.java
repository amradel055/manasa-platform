/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.define.InventoryPricesGroupEnum;
import com.isag.entity.CostCenter;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;

/**
 *
 * @author hq002
 */
public class InvInventoryEntity {

    private String code;
    private String name;
    private InventoryPricesGroupEnum sellPriceType;
    private String type;
    private CostCenter costCenterId;
    private GlAdminUnit adminUnitId;
    private GlAccount accountId;
    
    private String daribaValue;
    private String segilValue;

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

    public InventoryPricesGroupEnum getSellPriceType() {
        return sellPriceType;
    }

    public void setSellPriceType(InventoryPricesGroupEnum sellPriceType) {
        this.sellPriceType = sellPriceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public GlAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
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
