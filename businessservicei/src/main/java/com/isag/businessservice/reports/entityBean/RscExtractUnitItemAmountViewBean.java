/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports.entityBean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author khale
 */
public class RscExtractUnitItemAmountViewBean {

    private String projectName;
    private Integer projectId;
    private String supplierName;
    private Integer supplierId;
    private String customerName;
    private Integer customerId;
    private String itemName;
    private Integer itemId;
    private BigDecimal netCost;
    private BigDecimal amount;
    private BigDecimal price;
    
    private String projectCode;
    private String supplierCode;
    private String customerCode;
    private String itemCode;
    private Date date;
    
    private Boolean rowExpansion;
    private Boolean master;
    
    private List<RscExtractUnitItemAmountViewBean> extractUnitItemAmountViewBeanList ;

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the projectId
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return the supplierId
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
     * @return the netCost
     */
    public BigDecimal getNetCost() {
        return netCost;
    }

    /**
     * @param netCost the netCost to set
     */
    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    /**
     * @return the projectCode
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * @param projectCode the projectCode to set
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * @return the supplierCode
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * @param supplierCode the supplierCode to set
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
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
     * @return the extractUnitItemAmountViewBeanList
     */
    public List<RscExtractUnitItemAmountViewBean> getExtractUnitItemAmountViewBeanList() {
        return extractUnitItemAmountViewBeanList;
    }

    /**
     * @param extractUnitItemAmountViewBeanList the extractUnitItemAmountViewBeanList to set
     */
    public void setExtractUnitItemAmountViewBeanList(List<RscExtractUnitItemAmountViewBean> extractUnitItemAmountViewBeanList) {
        this.extractUnitItemAmountViewBeanList = extractUnitItemAmountViewBeanList;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the rowExpansion
     */
    public Boolean getRowExpansion() {
        if(rowExpansion == null){
            rowExpansion = Boolean.TRUE;
        }
        return rowExpansion;
    }

    /**
     * @param rowExpansion the rowExpansion to set
     */
    public void setRowExpansion(Boolean rowExpansion) {
        this.rowExpansion = rowExpansion;
    }

    /**
     * @return the master
     */
    public Boolean getMaster() {
        if(master == null){
            master = Boolean.TRUE;
        }
        return master;
    }

    /**
     * @param master the master to set
     */
    public void setMaster(Boolean master) {
        this.master = master;
    }

}
