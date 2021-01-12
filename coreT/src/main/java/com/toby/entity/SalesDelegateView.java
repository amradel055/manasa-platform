/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author ahmed
 */
@Entity
@Table(name = "sales_delegate_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SalesDelegateView.findAll", query = "SELECT s FROM SalesDelegateView s")
    , @NamedQuery(name = "SalesDelegateView.findByInvDelegatorId", query = "SELECT s FROM SalesDelegateView s WHERE s.invDelegatorId = :invDelegatorId")
    , @NamedQuery(name = "SalesDelegateView.findByTypeView", query = "SELECT s FROM SalesDelegateView s WHERE s.typeView = :typeView")
    , @NamedQuery(name = "SalesDelegateView.findByDelegatorCode", query = "SELECT s FROM SalesDelegateView s WHERE s.delegatorCode = :delegatorCode")
    , @NamedQuery(name = "SalesDelegateView.findByDelegatorName", query = "SELECT s FROM SalesDelegateView s WHERE s.delegatorName = :delegatorName")
    , @NamedQuery(name = "SalesDelegateView.findByCash", query = "SELECT s FROM SalesDelegateView s WHERE s.cash = :cash")
    , @NamedQuery(name = "SalesDelegateView.findByBranchId", query = "SELECT s FROM SalesDelegateView s WHERE s.branchId = :branchId")
    , @NamedQuery(name = "SalesDelegateView.findByPostpone", query = "SELECT s FROM SalesDelegateView s WHERE s.postpone = :postpone")
    , @NamedQuery(name = "SalesDelegateView.findByDocumentarycredit", query = "SELECT s FROM SalesDelegateView s WHERE s.documentarycredit = :documentarycredit")
    , @NamedQuery(name = "SalesDelegateView.findByTotal", query = "SELECT s FROM SalesDelegateView s WHERE s.total = :total")})
public class SalesDelegateView extends BaseEntity {

    @Column(name = "inv_delegator_id")
    private Integer invDelegatorId;
    @Column(name = "serail")
    private Integer serail;
    @Column(name = "type_view")
    private Boolean typeView;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_type")
    private int paymentType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "organization_site_id")
    private Integer organizationSiteId;
    @Column(name = "inv_inventory_id")
    private Integer invInventoryId;
    @Column(name = "branchId")
    private Integer branchId;
    @Size(max = 450)
    @Column(name = "delegator_code")
    private String delegatorCode;
    @Size(max = 45)
    @Column(name = "delegator_name")
    private String delegatorName;
    @Column(name = "cash")
    private BigDecimal cash;
    @Column(name = "postpone")
    private BigDecimal postpone;
    @Column(name = "Documentary_credit")
    private BigDecimal documentarycredit;
    @Column(name = "total")
    private BigDecimal total;

    public SalesDelegateView() {
    }

    public Integer getInvDelegatorId() {
        return invDelegatorId;
    }

    public void setInvDelegatorId(Integer invDelegatorId) {
        this.invDelegatorId = invDelegatorId;
    }

    public Integer getSerail() {
        return serail;
    }

    public void setSerail(Integer serail) {
        this.serail = serail;
    }

    public Boolean getTypeView() {
        return typeView;
    }

    public void setTypeView(Boolean typeView) {
        this.typeView = typeView;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public Integer getInvInventoryId() {
        return invInventoryId;
    }

    public void setInvInventoryId(Integer invInventoryId) {
        this.invInventoryId = invInventoryId;
    }

    public String getDelegatorCode() {
        return delegatorCode;
    }

    public void setDelegatorCode(String delegatorCode) {
        this.delegatorCode = delegatorCode;
    }

    public String getDelegatorName() {
        return delegatorName;
    }

    public void setDelegatorName(String delegatorName) {
        this.delegatorName = delegatorName;
    }

    /**
     * @return the cash
     */
    public BigDecimal getCash() {
        return cash;
    }

    /**
     * @param cash the cash to set
     */
    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    /**
     * @return the postpone
     */
    public BigDecimal getPostpone() {
        return postpone;
    }

    /**
     * @param postpone the postpone to set
     */
    public void setPostpone(BigDecimal postpone) {
        this.postpone = postpone;
    }

    /**
     * @return the documentarycredit
     */
    public BigDecimal getDocumentarycredit() {
        return documentarycredit;
    }

    /**
     * @param documentarycredit the documentarycredit to set
     */
    public void setDocumentarycredit(BigDecimal documentarycredit) {
        this.documentarycredit = documentarycredit;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

}
