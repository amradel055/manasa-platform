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
@Table(name = "inst_purchase_invoice_contract_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstPurchaseInvoiceContractView.findAll", query = "SELECT i FROM InstPurchaseInvoiceContractView i")
    , @NamedQuery(name = "InstPurchaseInvoiceContractView.findByRowNum", query = "SELECT i FROM InstPurchaseInvoiceContractView i WHERE i.rowNum = :rowNum")
    , @NamedQuery(name = "InstPurchaseInvoiceContractView.findById", query = "SELECT i FROM InstPurchaseInvoiceContractView i WHERE i.id = :id")
    , @NamedQuery(name = "InstPurchaseInvoiceContractView.findByName", query = "SELECT i FROM InstPurchaseInvoiceContractView i WHERE i.name = :name")
    , @NamedQuery(name = "InstPurchaseInvoiceContractView.findByCode", query = "SELECT i FROM InstPurchaseInvoiceContractView i WHERE i.code = :code")
    , @NamedQuery(name = "InstPurchaseInvoiceContractView.findByQuantity", query = "SELECT i FROM InstPurchaseInvoiceContractView i WHERE i.quantity = :quantity")
    , @NamedQuery(name = "InstPurchaseInvoiceContractView.findByBranchId", query = "SELECT i FROM InstPurchaseInvoiceContractView i WHERE i.branchId = :branchId")
    , @NamedQuery(name = "InstPurchaseInvoiceContractView.findByTableType", query = "SELECT i FROM InstPurchaseInvoiceContractView i WHERE i.tableType = :tableType")})
public class InstPurchaseInvoiceContractView implements Serializable {

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
    @Column(name = "quantity")
    private BigInteger quantity;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "table_type")
    private BigInteger tableType;
    @Column(name = "price")
    private BigDecimal price;

    public InstPurchaseInvoiceContractView() {
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

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public BigInteger getTableType() {
        return tableType;
    }

    public void setTableType(BigInteger tableType) {
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
