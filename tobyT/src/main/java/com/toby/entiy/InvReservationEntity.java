package com.toby.entiy;

import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventoryDelegator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvReservationEntity extends BaseEntity {

    private Date reservationDate;
    private Date endDate;
    private InventoryDelegator delegator;
    private InvInventory inventory;
    private InvOrganizationSite site;
    private InvOrganizationSite mainSite;
    private String address;
    private String remark;
    private Branch branchId;
    List<InvReservationDetailEntity> invReservationDetailsEntityList = new ArrayList<>();

    public InvReservationEntity() {
        setCounter(getCounter() + 1);
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public InventoryDelegator getDelegator() {
        return delegator;
    }

    public void setDelegator(InventoryDelegator delegator) {
        this.delegator = delegator;
    }

    public InvInventory getInventory() {
        return inventory;
    }

    public void setInventory(InvInventory inventory) {
        this.inventory = inventory;
    }

    public InvOrganizationSite getSite() {
        return site;
    }

    public void setSite(InvOrganizationSite site) {
        this.site = site;
    }

    public InvOrganizationSite getMainSite() {
        return mainSite;
    }

    public void setMainSite(InvOrganizationSite mainSite) {
        this.mainSite = mainSite;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<InvReservationDetailEntity> getInvReservationDetailsEntityList() {
        return invReservationDetailsEntityList;
    }

    public void setInvReservationDetailsEntityList(List<InvReservationDetailEntity> invReservationDetailsEntityList) {
        this.invReservationDetailsEntityList = invReservationDetailsEntityList;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }
}
