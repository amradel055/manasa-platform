/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author AhmedEssam
 */
@Entity
@Table(name = "inst_purchase_invoice_contract_difrrance_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findAll", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i")
    , @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findByRowNum", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.rowNum = :rowNum")
    , @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findById", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.id = :id")
    , @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findByName", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.name = :name")
    , @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findByCode", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.code = :code")
    , @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findByQuantity", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.quantity = :quantity")
    , @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findByBranchId", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.branchId = :branchId")
    , @NamedQuery(name = "InstPurchaseInvoiceContractDifrranceView.findByTableType", query = "SELECT i FROM InstPurchaseInvoiceContractDifrranceView i WHERE i.tableType = :tableType")})
public class InstPurchaseInvoiceContractDifrranceView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "rowNum")
    @Id
    private Integer rowNum;
    @Column(name = "id")
    private Integer id;
    @Size(max = 450)
    @Column(name = "name")
    private String name;
    @Size(max = 450)
    @Column(name = "code")
    private String code;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "table_type")
    private Integer tableType;
    @Column(name = "price")
    private BigDecimal price;

    public InstPurchaseInvoiceContractDifrranceView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getTableType() {
        return tableType;
    }

    public void setTableType(Integer tableType) {
        this.tableType = tableType;
    }

    @Override
    public String toString() {
        return " " + code + " " + name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
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
}
