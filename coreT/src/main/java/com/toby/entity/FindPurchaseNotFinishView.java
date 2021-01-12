/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H
 */
@Entity
@Table(name = "find_purchase_not_finish_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FindPurchaseNotFinishView.findAll", query = "SELECT f FROM FindPurchaseNotFinishView f"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByItemId", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.itemId = :itemId"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByQuantity", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.quantity = :quantity"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByInvinventoryId", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.invinventoryId = :invinventoryId"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByBranchId", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.branchId = :branchId"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByInvoiceId", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.invoiceId = :invoiceId"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByItemName", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.itemName = :itemName"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByItemCode", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.itemCode = :itemCode"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByItemUnit", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.itemUnit = :itemUnit"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByUnitName", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.unitName = :unitName"),
    @NamedQuery(name = "FindPurchaseNotFinishView.findByUnitCode", query = "SELECT f FROM FindPurchaseNotFinishView f WHERE f.unitCode = :unitCode")})
public class FindPurchaseNotFinishView implements Serializable {
   
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "itemId")
    private int itemId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "invinventoryId")
    private Integer invinventoryId;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "invoiceId")
    private Integer invoiceId;
    @Size(max = 450)
    @Column(name = "itemName")
    private String itemName;
    @Size(max = 45)
    @Column(name = "itemCode")
    private String itemCode;
    @Column(name = "itemUnit")
    private Integer itemUnit;
    @Size(max = 255)
    @Column(name = "unitName")
    private String unitName;
    @Column(name = "unitCode")
    private Integer unitCode;

    public FindPurchaseNotFinishView() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getInvinventoryId() {
        return invinventoryId;
    }

    public void setInvinventoryId(Integer invinventoryId) {
        this.invinventoryId = invinventoryId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(Integer itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(Integer unitCode) {
        this.unitCode = unitCode;
    }
    
}
