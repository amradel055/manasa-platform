/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 *
 * @author User
 */
public class FinalRemainDataView implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "totalNumber")
    private BigDecimal totalNumber;
    @Column(name = "numberExcute")
    private BigDecimal numberExcute;
    @Column(name = "numberRemain")
    private BigDecimal numberRemain;
    @Column(name = "productionStagesId")
    private Integer productionStagesId;
    @Column(name = "productionStageCost")
    private BigDecimal productionStageCost;
    @Column(name = "date")
    private Date date;
    @Column(name = "itemId")
    private Integer itemId;
    @Column(name = "itemName")
    private String itemName;
    @Column(name = "itemCode")
    private String itemCode;
    @Column(name = "test")
    private Integer test;
    @Column(name = "invPurchaseInvoiceId")
    private Integer invPurchaseInvoiceId;
    @Column(name = "invPurchaseInvoiceserial")
    private Integer invPurchaseInvoiceserial;
    @Column(name = "ProductionLine")
    private String ProductionLine;
    @Column(name = "ProductionLineId")
    private Integer ProductionLineId;
    @Column(name = "branchId")
    private Integer branchId;
    @Column(name = "userId")
    private Integer userId;
    @Column(name = "userName")
    private String userName;

    /**
     * @return the rowNum
     */
    public Integer getRowNum() {
        return rowNum;
    }

    /**
     * @param rowNum the rowNum to set
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * @return the totalNumber
     */
    public BigDecimal getTotalNumber() {
        return totalNumber;
    }

    /**
     * @param totalNumber the totalNumber to set
     */
    public void setTotalNumber(BigDecimal totalNumber) {
        this.totalNumber = totalNumber;
    }

    /**
     * @return the numberExcute
     */
    public BigDecimal getNumberExcute() {
        return numberExcute;
    }

    /**
     * @param numberExcute the numberExcute to set
     */
    public void setNumberExcute(BigDecimal numberExcute) {
        this.numberExcute = numberExcute;
    }

    /**
     * @return the numberRemain
     */
    public BigDecimal getNumberRemain() {
        return numberRemain;
    }

    /**
     * @param numberRemain the numberRemain to set
     */
    public void setNumberRemain(BigDecimal numberRemain) {
        this.numberRemain = numberRemain;
    }

    /**
     * @return the productionStagesId
     */
    public Integer getProductionStagesId() {
        return productionStagesId;
    }

    /**
     * @param productionStagesId the productionStagesId to set
     */
    public void setProductionStagesId(Integer productionStagesId) {
        this.productionStagesId = productionStagesId;
    }

    /**
     * @return the productionStageCost
     */
    public BigDecimal getProductionStageCost() {
        return productionStageCost;
    }

    /**
     * @param productionStageCost the productionStageCost to set
     */
    public void setProductionStageCost(BigDecimal productionStageCost) {
        this.productionStageCost = productionStageCost;
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
     * @return the itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the test
     */
    public Integer getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(Integer test) {
        this.test = test;
    }

    /**
     * @return the invPurchaseInvoiceId
     */
    public Integer getInvPurchaseInvoiceId() {
        return invPurchaseInvoiceId;
    }

    /**
     * @param invPurchaseInvoiceId the invPurchaseInvoiceId to set
     */
    public void setInvPurchaseInvoiceId(Integer invPurchaseInvoiceId) {
        this.invPurchaseInvoiceId = invPurchaseInvoiceId;
    }

    /**
     * @return the invPurchaseInvoiceserial
     */
    public Integer getInvPurchaseInvoiceserial() {
        return invPurchaseInvoiceserial;
    }

    /**
     * @param invPurchaseInvoiceserial the invPurchaseInvoiceserial to set
     */
    public void setInvPurchaseInvoiceserial(Integer invPurchaseInvoiceserial) {
        this.invPurchaseInvoiceserial = invPurchaseInvoiceserial;
    }

    /**
     * @return the ProductionLine
     */
    public String getProductionLine() {
        return ProductionLine;
    }

    /**
     * @param ProductionLine the ProductionLine to set
     */
    public void setProductionLine(String ProductionLine) {
        this.ProductionLine = ProductionLine;
    }

    /**
     * @return the ProductionLineId
     */
    public Integer getProductionLineId() {
        return ProductionLineId;
    }

    /**
     * @param ProductionLineId the ProductionLineId to set
     */
    public void setProductionLineId(Integer ProductionLineId) {
        this.ProductionLineId = ProductionLineId;
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

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
