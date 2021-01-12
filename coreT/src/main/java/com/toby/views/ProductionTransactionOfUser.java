/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Toby
 */
public class ProductionTransactionOfUser implements Serializable {

    private BigDecimal totalNumber;
    private BigDecimal numberExcute;
    private BigDecimal numberRemain;
    private BigDecimal avarage;
    private Integer productionStagesId;
    private BigDecimal productionStageCost;
    private Date date;
    private Integer itemId;
    private String itemName;
    private String stageName;
    private String itemCode;
    private Integer invPurchaseInvoiceId;
    private Integer invPurchaseInvoiceserial;
    private String productionLine;
    private Integer productionLineId;
    private Integer branchId;
    private Integer userId;
    private String userName;
    private BigDecimal price;
    private String  userCode;

    public ProductionTransactionOfUser() {
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

    public Integer getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(Integer productionLineId) {
        this.productionLineId = productionLineId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the userCode
     */
    public String  getUserCode() {
        return userCode;
    }

    /**
     * @param userCode the userCode to set
     */
    public void setUserCode(String  userCode) {
        this.userCode = userCode;
    }

    /**
     * @return the stageName
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * @param stageName the stageName to set
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * @return the avarage
     */
    public BigDecimal getAvarage() {
        return avarage;
    }

    /**
     * @param avarage the avarage to set
     */
    public void setAvarage(BigDecimal avarage) {
        this.avarage = avarage;
    }
    
}
