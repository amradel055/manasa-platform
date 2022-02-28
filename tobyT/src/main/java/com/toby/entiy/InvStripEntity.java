package com.toby.entiy;

import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvStripEntity extends BaseEntity {

    private Date stripDate;
    private String stripDocument;
    private String remark;
    private Branch branchId;
    private InvInventory invInventory;
    private List<InvStripDetailEntity> invStripDetailEntityList = new ArrayList<>();

    public InvStripEntity() {
        setCounter(getCounter() + 1);
    }

    public Date getStripDate() {
        return stripDate;
    }

    public void setStripDate(Date stripDate) {
        this.stripDate = stripDate;
    }

    public String getStripDocument() {
        return stripDocument;
    }

    public void setStripDocument(String stripDocument) {
        this.stripDocument = stripDocument;
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

    public List<InvStripDetailEntity> getInvStripDetailEntityList() {
        return invStripDetailEntityList;
    }

    public void setInvStripDetailEntityList(List<InvStripDetailEntity> invStripDetailEntityList) {
        this.invStripDetailEntityList = invStripDetailEntityList;
    }

    public InvInventory getInvInventory() {
        return invInventory;
    }

    public void setInvInventory(InvInventory invInventory) {
        this.invInventory = invInventory;
    }
}
