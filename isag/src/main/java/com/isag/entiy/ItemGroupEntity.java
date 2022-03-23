/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.InvGroup;
import com.isag.entity.InvItem;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author WIN7
 */
public class ItemGroupEntity {

    private Integer id;
    private InvItem invItemSelected;
    private Date date;
    private String itemCode;
    private String itemName;

    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private String unitName;
    private InvGroup groupId;
    private Integer branchId;
    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public InvGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(InvGroup groupId) {
        this.groupId = groupId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public InvItem getInvItemSelected() {
        return invItemSelected;
    }

    public void setInvItemSelected(InvItem invItemSelected) {
        this.invItemSelected = invItemSelected;
    }

}
