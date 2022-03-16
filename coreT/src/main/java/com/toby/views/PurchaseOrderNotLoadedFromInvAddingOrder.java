/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "purchase_order_not_loaded_from_inv_adding_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderNotLoadedFromInvAddingOrder.findAll", query = "SELECT p FROM PurchaseOrderNotLoadedFromInvAddingOrder p")
    , @NamedQuery(name = "PurchaseOrderNotLoadedFromInvAddingOrder.findByRowNum", query = "SELECT p FROM PurchaseOrderNotLoadedFromInvAddingOrder p WHERE p.rowNum = :rowNum")
    , @NamedQuery(name = "PurchaseOrderNotLoadedFromInvAddingOrder.findByPurchaseId", query = "SELECT p FROM PurchaseOrderNotLoadedFromInvAddingOrder p WHERE p.purchaseId = :purchaseId")
    , @NamedQuery(name = "PurchaseOrderNotLoadedFromInvAddingOrder.findBySerial", query = "SELECT p FROM PurchaseOrderNotLoadedFromInvAddingOrder p WHERE p.serial = :serial")
    , @NamedQuery(name = "PurchaseOrderNotLoadedFromInvAddingOrder.findByBranchId", query = "SELECT p FROM PurchaseOrderNotLoadedFromInvAddingOrder p WHERE p.branchId = :branchId")})
public class PurchaseOrderNotLoadedFromInvAddingOrder implements Serializable {

    /**
     * @return the organizationId
     */
    public int getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the organizationCode
     */
    public String getOrganizationCode() {
        return organizationCode;
    }

    /**
     * @param organizationCode the organizationCode to set
     */
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

     private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "purchase_id")
    private int purchaseId;
    @Column(name = "serial")
    private Integer serial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "branch_id")
    private int branchId;
    @Column(name = "organization_id")
    private int organizationId;
    @Column(name = "organization_name")
    private String organizationName;
    @Column(name = "organization_code")
    private String organizationCode;
    @Column(name = "delegator_id")
    private int delegatorId;
    @Column(name = "delegator_name")
    private String delegatorName;
    @Column(name = "delegator_code")
    private String delegatorCode;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 450)
    @Column(name = "remarks")
    private String remarks;
    

    public PurchaseOrderNotLoadedFromInvAddingOrder() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    
     @Override
    public String toString() {
        return "" + serial;
    }

    /**
     * @return the delegatorId
     */
    public int getDelegatorId() {
        return delegatorId;
    }

    /**
     * @param delegatorId the delegatorId to set
     */
    public void setDelegatorId(int delegatorId) {
        this.delegatorId = delegatorId;
    }

    /**
     * @return the delegatorName
     */
    public String getDelegatorName() {
        return delegatorName;
    }

    /**
     * @param delegatorName the delegatorName to set
     */
    public void setDelegatorName(String delegatorName) {
        this.delegatorName = delegatorName;
    }

    /**
     * @return the delegatorCode
     */
    public String getDelegatorCode() {
        return delegatorCode;
    }

    /**
     * @param delegatorCode the delegatorCode to set
     */
    public void setDelegatorCode(String delegatorCode) {
        this.delegatorCode = delegatorCode;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
}
