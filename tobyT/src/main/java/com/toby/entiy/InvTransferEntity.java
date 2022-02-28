package com.toby.entiy;

import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvTransfer;
import com.toby.views.ItemsBarcodesDetailsView;

import java.util.Date;

public class InvTransferEntity extends BaseEntity {

    private Integer serialNo;
    private Date transferDate;
    private InvInventory invFrom;
    private InvInventory invTo;
    private String remark;
    private Branch branchId;
    private ItemsBarcodesDetailsView itemsBarcodesDetailsView;
    private InvTransfer invTransfer;
   
    private InvTransfer invTransferTrans;

    public InvTransferEntity() {
        setCounter(getCounter() + 1);
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public InvInventory getInvFrom() {
        return invFrom;
    }

    public void setInvFrom(InvInventory invFrom) {
        this.invFrom = invFrom;
    }

    public InvInventory getInvTo() {
        return invTo;
    }

    public void setInvTo(InvInventory invTo) {
        this.invTo = invTo;
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

    public ItemsBarcodesDetailsView getItemsBarcodesDetailsView() {
        return itemsBarcodesDetailsView;
    }

    public void setItemsBarcodesDetailsView(ItemsBarcodesDetailsView itemsBarcodesDetailsView) {
        this.itemsBarcodesDetailsView = itemsBarcodesDetailsView;
    }

    /**
     * @return the invTransfer
     */
    public InvTransfer getInvTransfer() {
        return invTransfer;
    }

    /**
     * @param invTransfer the invTransfer to set
     */
    public void setInvTransfer(InvTransfer invTransfer) {
        this.invTransfer = invTransfer;
    }

    /**
     * @return the invTransferTrans
     */
    public InvTransfer getInvTransferTrans() {
        return invTransferTrans;
    }

    /**
     * @param invTransferTrans the invTransferTrans to set
     */
    public void setInvTransferTrans(InvTransfer invTransferTrans) {
        this.invTransferTrans = invTransferTrans;
    }

   
}
