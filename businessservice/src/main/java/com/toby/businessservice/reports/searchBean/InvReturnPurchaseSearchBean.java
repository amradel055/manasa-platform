/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.reports.searchBean;

import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvReturnPurchase;
import java.util.Date;

/**
 *
 * @author hhhh
 */
public class InvReturnPurchaseSearchBean {

    private Integer branchId;
    private InvOrganizationSite suplierFrom;
    private InvOrganizationSite suplierTo;
    private Date dateFrom;
    private Date dateTo;
    private InvInventory inventoryFrom;
    private InvInventory inventoryTo;
    private InvReturnPurchase invReturnPurchaseFrom;
    private InvReturnPurchase invReturnPurchaseTo;
    private Integer paymentTypeFrom;
    private Integer paymentTypeTo;
    private Boolean type;
    private Boolean showReport;

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public InvOrganizationSite getSuplierFrom() {
        return suplierFrom;
    }

    public void setSuplierFrom(InvOrganizationSite suplierFrom) {
        this.suplierFrom = suplierFrom;
    }

    public InvOrganizationSite getSuplierTo() {
        return suplierTo;
    }

    public void setSuplierTo(InvOrganizationSite suplierTo) {
        this.suplierTo = suplierTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public InvInventory getInventoryFrom() {
        return inventoryFrom;
    }

    public void setInventoryFrom(InvInventory inventoryFrom) {
        this.inventoryFrom = inventoryFrom;
    }

    public InvInventory getInventoryTo() {
        return inventoryTo;
    }

    public void setInventoryTo(InvInventory inventoryTo) {
        this.inventoryTo = inventoryTo;
    }

    public InvReturnPurchase getInvReturnPurchaseFrom() {
        return invReturnPurchaseFrom;
    }

    public void setInvReturnPurchaseFrom(InvReturnPurchase invReturnPurchaseFrom) {
        this.invReturnPurchaseFrom = invReturnPurchaseFrom;
    }

    public InvReturnPurchase getInvReturnPurchaseTo() {
        return invReturnPurchaseTo;
    }

    public void setInvReturnPurchaseTo(InvReturnPurchase invReturnPurchaseTo) {
        this.invReturnPurchaseTo = invReturnPurchaseTo;
    }

    public Integer getPaymentTypeFrom() {
        return paymentTypeFrom;
    }

    public void setPaymentTypeFrom(Integer paymentTypeFrom) {
        this.paymentTypeFrom = paymentTypeFrom;
    }

    public Integer getPaymentTypeTo() {
        return paymentTypeTo;
    }

    public void setPaymentTypeTo(Integer paymentTypeTo) {
        this.paymentTypeTo = paymentTypeTo;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Boolean getShowReport() {
        return showReport;
    }

    public void setShowReport(Boolean showReport) {
        this.showReport = showReport;
    }
}
