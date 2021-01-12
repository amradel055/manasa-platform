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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hhhh
 */
@Entity
@Table(name = "return_invoice_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReturnInvoiceView.findAll", query = "SELECT r FROM ReturnInvoiceView r"),
    @NamedQuery(name = "ReturnInvoiceView.findById", query = "SELECT r FROM ReturnInvoiceView r WHERE r.id = :id"),
    @NamedQuery(name = "ReturnInvoiceView.findBySerial", query = "SELECT r FROM ReturnInvoiceView r WHERE r.serial = :serial"),
    @NamedQuery(name = "ReturnInvoiceView.findByType", query = "SELECT r FROM ReturnInvoiceView r WHERE r.type = :type"),
    @NamedQuery(name = "ReturnInvoiceView.findByPaymentType", query = "SELECT r FROM ReturnInvoiceView r WHERE r.paymentType = :paymentType"),
    @NamedQuery(name = "ReturnInvoiceView.findByInvoiceSerial", query = "SELECT r FROM ReturnInvoiceView r WHERE r.invoiceSerial = :invoiceSerial"),
    @NamedQuery(name = "ReturnInvoiceView.findByBranchId", query = "SELECT r FROM ReturnInvoiceView r WHERE r.branchId = :branchId"),
    @NamedQuery(name = "ReturnInvoiceView.findByDate", query = "SELECT r FROM ReturnInvoiceView r WHERE r.date = :date"),
    @NamedQuery(name = "ReturnInvoiceView.findByInventoryCode", query = "SELECT r FROM ReturnInvoiceView r WHERE r.inventoryCode = :inventoryCode"),
    @NamedQuery(name = "ReturnInvoiceView.findByInventoryName", query = "SELECT r FROM ReturnInvoiceView r WHERE r.inventoryName = :inventoryName"),
    @NamedQuery(name = "ReturnInvoiceView.findBySupplierCode", query = "SELECT r FROM ReturnInvoiceView r WHERE r.supplierCode = :supplierCode"),
    @NamedQuery(name = "ReturnInvoiceView.findBySupplierName", query = "SELECT r FROM ReturnInvoiceView r WHERE r.supplierName = :supplierName"),
    @NamedQuery(name = "ReturnInvoiceView.findByNet", query = "SELECT r FROM ReturnInvoiceView r WHERE r.net = :net")})
public class ReturnInvoiceView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "type")
    private Boolean type;
    @Basic(optional = false)
    @Column(name = "paymentType")
    private int paymentType;
    @Column(name = "invoiceSerial")
    private Integer invoiceSerial;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "company_id")
    private Integer companyId;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "inventory_code")
    private String inventoryCode;
    @Column(name = "inventory_name")
    private String inventoryName;
    @Column(name = "supplier_code")
    private String supplierCode;
    @Column(name = "supplier_name")
    private String supplierName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "net")
    private BigDecimal net;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "general_journal_id")
    private Integer generalJournalId;
    @Column(name = "post_flag")
    private Integer postFlag;

    public ReturnInvoiceView() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the generalJournalId
     */
    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    /**
     * @param generalJournalId the generalJournalId to set
     */
    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    /**
     * @return the postFlag
     */
    public Integer getPostFlag() {
        return postFlag;
    }

    /**
     * @param postFlag the postFlag to set
     */
    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

}
