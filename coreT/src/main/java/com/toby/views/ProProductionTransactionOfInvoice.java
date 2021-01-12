/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.toby.views;


import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author H
 */
@Entity
@Table(name = "pro_production_transaction_of_invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findAll", query = "SELECT p FROM ProProductionTransactionOfInvoice p"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByRowNum", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.rowNum = :rowNum"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByTotalNumber", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.totalNumber = :totalNumber"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByNumberExcute", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.numberExcute = :numberExcute"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByNumberRemain", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.numberRemain = :numberRemain"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByProductionStagesId", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.productionStagesId = :productionStagesId"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByProductionStageCost", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.productionStageCost = :productionStageCost"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByDate", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.date = :date"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByItemId", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.itemId = :itemId"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByItemName", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.itemName = :itemName"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByProductionStagesName", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.productionStagesName = :productionStagesName"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByItemCode", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.itemCode = :itemCode"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByInvPurchaseInvoiceId", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.invPurchaseInvoiceId = :invPurchaseInvoiceId"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByInvPurchaseInvoiceserial", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.invPurchaseInvoiceserial = :invPurchaseInvoiceserial"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByProductionLine", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.productionLine = :productionLine"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByInvoiceDetailId", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.invoiceDetailId = :invoiceDetailId"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByClothNumber", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.clothNumber = :clothNumber"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByProductionLineId", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.productionLineId = :productionLineId"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByBranchId", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.branchId = :branchId"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByUserId", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.userId = :userId"),
    @NamedQuery(name = "ProProductionTransactionOfInvoice.findByUserName", query = "SELECT p FROM ProProductionTransactionOfInvoice p WHERE p.userName = :userName")})
public class ProProductionTransactionOfInvoice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalNumber")
    private BigDecimal totalNumber;
    @Column(name = "numberExcute")
    private BigDecimal numberExcute;
    @Column(name = "numberRemain")
    private BigDecimal numberRemain;
    @Column(name = "productionStagesId")
    private Integer productionStagesId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "productionStageCost")
    private BigDecimal productionStageCost;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "itemId")
    private Integer itemId;
    @Size(max = 450)
    @Column(name = "itemName")
    private String itemName;
    @Size(max = 450)
    @Column(name = "productionStagesName")
    private String productionStagesName;
    @Size(max = 45)
    @Column(name = "itemCode")
    private String itemCode;
    @Column(name = "invPurchaseInvoiceId")
    private Integer invPurchaseInvoiceId;
    @Column(name = "invPurchaseInvoiceserial")
    private Integer invPurchaseInvoiceserial;
    @Size(max = 450)
    @Column(name = "ProductionLine")
    private String productionLine;
    @Column(name = "invoiceDetailId")
    private Integer invoiceDetailId;
    @Column(name = "clothNumber")
    private Integer clothNumber;
    @Column(name = "ProductionLineId")
    private Integer productionLineId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "branchId")
    private int branchId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userId")
    private int userId;
    @Size(max = 45)
    @Column(name = "userName")
    private String userName;

    public ProProductionTransactionOfInvoice() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public BigDecimal getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(BigDecimal totalNumber) {
        this.totalNumber = totalNumber;
    }

    public BigDecimal getNumberExcute() {
        return numberExcute;
    }

    public void setNumberExcute(BigDecimal numberExcute) {
        this.numberExcute = numberExcute;
    }

    public BigDecimal getNumberRemain() {
        return numberRemain;
    }

    public void setNumberRemain(BigDecimal numberRemain) {
        this.numberRemain = numberRemain;
    }

    public Integer getProductionStagesId() {
        return productionStagesId;
    }

    public void setProductionStagesId(Integer productionStagesId) {
        this.productionStagesId = productionStagesId;
    }

    public BigDecimal getProductionStageCost() {
        return productionStageCost;
    }

    public void setProductionStageCost(BigDecimal productionStageCost) {
        this.productionStageCost = productionStageCost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public Integer getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    public void setInvPurchaseInvoiceId(Integer invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    public Integer getInvPurchaseInvoiceserial() {
        return invPurchaseInvoiceserial;
    }

    public void setInvPurchaseInvoiceserial(Integer invPurchaseInvoiceserial) {
        this.invPurchaseInvoiceserial = invPurchaseInvoiceserial;
    }

    public String getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(String productionLine) {
        this.productionLine = productionLine;
    }

    public Integer getInvoiceDetailId() {
        return invoiceDetailId;
    }

    public void setInvoiceDetailId(Integer invoiceDetailId) {
        this.invoiceDetailId = invoiceDetailId;
    }

    public Integer getClothNumber() {
        return clothNumber;
    }

    public void setClothNumber(Integer clothNumber) {
        this.clothNumber = clothNumber;
    }

    public Integer getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(Integer productionLineId) {
        this.productionLineId = productionLineId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the productionStagesName
     */
    public String getProductionStagesName() {
        return productionStagesName;
    }

    /**
     * @param productionStagesName the productionStagesName to set
     */
    public void setProductionStagesName(String productionStagesName) {
        this.productionStagesName = productionStagesName;
    }
    
}
