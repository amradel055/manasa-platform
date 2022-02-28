/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Toby
 */
@Entity
@Table(name = "production_transaction_stages_invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findAll", query = "SELECT p FROM ProductionTransactionStagesInvoice p"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByRowNum", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.rowNum = :rowNum"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByInvoiceId", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.invoiceId = :invoiceId"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByItemId", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.itemId = :itemId"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByUnitId", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.unitId = :unitId"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByClothNumber", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.clothNumber = :clothNumber"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByInvItemParentId", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.invItemParentId = :invItemParentId"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByNumber", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.number = :number"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByQuantity", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "ProductionTransactionStagesInvoice.findByProProductionStagesId", query = "SELECT p FROM ProductionTransactionStagesInvoice p WHERE p.proProductionStagesId = :proProductionStagesId")})
public class ProductionTransactionStagesInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invoiceId")
    private int invoiceId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_id")
    private int itemId;
    @Column(name = "unit_id")
    private Integer unitId;
    @Column(name = "cloth_number")
    private Integer clothNumber;
    @Column(name = "inv_item_parent_id")
    private Integer invItemParentId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "number")
    private BigDecimal number;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "pro_production_stages_id")
    private Integer proProductionStagesId;

    public ProductionTransactionStagesInvoice() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getClothNumber() {
        return clothNumber;
    }

    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
    }

    public Integer getInvItemParentId() {
        return invItemParentId;
    }

    public void setInvItemParentId(Integer invItemParentId) {
        this.invItemParentId = invItemParentId;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getProProductionStagesId() {
        return proProductionStagesId;
    }

    public void setProProductionStagesId(Integer proProductionStagesId) {
        this.proProductionStagesId = proProductionStagesId;
    }
    
}
