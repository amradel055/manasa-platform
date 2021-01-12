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
@Table(name = "quantity_item_not_finish")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuantityItemNotFinish.findAll", query = "SELECT q FROM QuantityItemNotFinish q"),
    @NamedQuery(name = "QuantityItemNotFinish.findByRowNum", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.rowNum = :rowNum"),
    @NamedQuery(name = "QuantityItemNotFinish.findByQuantity", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.quantity = :quantity"),
    @NamedQuery(name = "QuantityItemNotFinish.findByInvoiceId", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.invoiceId = :invoiceId"),
    @NamedQuery(name = "QuantityItemNotFinish.findByItemId", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.itemId = :itemId"),
    @NamedQuery(name = "QuantityItemNotFinish.findByInvinventoryid", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.invinventoryid = :invinventoryid"),
    @NamedQuery(name = "QuantityItemNotFinish.findByBranchid", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.branchid = :branchid"),
    @NamedQuery(name = "QuantityItemNotFinish.findByItemName", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.itemName = :itemName"),
    @NamedQuery(name = "QuantityItemNotFinish.findByItemCode", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.itemCode = :itemCode"),
    @NamedQuery(name = "QuantityItemNotFinish.findByItemUnit", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.itemUnit = :itemUnit"),
    @NamedQuery(name = "QuantityItemNotFinish.findByUnitName", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.unitName = :unitName"),
    @NamedQuery(name = "QuantityItemNotFinish.findByUnitCode", query = "SELECT q FROM QuantityItemNotFinish q WHERE q.unitCode = :unitCode")})
public class QuantityItemNotFinish implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "invoiceId")
        private Integer invoiceId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "itemId")
    private int itemId;
    @Column(name = "invinventoryid")
    private Integer invinventoryid;
    @Column(name = "branchid")
    private Integer branchid;
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

    public QuantityItemNotFinish() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Integer getInvinventoryid() {
        return invinventoryid;
    }

    public void setInvinventoryid(Integer invinventoryid) {
        this.invinventoryid = invinventoryid;
    }

    public Integer getBranchid() {
        return branchid;
    }

    public void setBranchid(Integer branchid) {
        this.branchid = branchid;
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
