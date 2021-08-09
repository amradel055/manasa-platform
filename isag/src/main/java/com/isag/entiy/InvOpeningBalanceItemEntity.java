/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.Branch;
import com.isag.entity.InvInventory;
import com.isag.entity.InvOpenningBalanceItemDetail;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hhhh
 */
public class InvOpeningBalanceItemEntity extends BaseEntity{
    private String documentStrip;
    private Date date;
    private String remark;
    private Branch branchId;
    private InvInventory invInventoryId;
    private List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailList;

    public String getDocumentStrip() {
        return documentStrip;
    }

    public void setDocumentStrip(String documentStrip) {
        this.documentStrip = documentStrip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public InvInventory getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(InvInventory invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public List<InvOpenningBalanceItemDetail> getInvOpenningBalanceItemDetailList() {
        return invOpenningBalanceItemDetailList;
    }

    public void setInvOpenningBalanceItemDetailList(List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailList) {
        this.invOpenningBalanceItemDetailList = invOpenningBalanceItemDetailList;
    }
}
