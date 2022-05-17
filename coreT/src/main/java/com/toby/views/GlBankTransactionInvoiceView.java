/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H
 */
@Entity
@Table(name = "gl_bank_transaction_invoice_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlBankTransactionInvoiceView.findAll", query = "SELECT g FROM GlBankTransactionInvoiceView g"),
    @NamedQuery(name = "GlBankTransactionInvoiceView.findByRowNum", query = "SELECT g FROM GlBankTransactionInvoiceView g WHERE g.rowNum = :rowNum"),
    @NamedQuery(name = "GlBankTransactionInvoiceView.findBySerial", query = "SELECT g FROM GlBankTransactionInvoiceView g WHERE g.serial = :serial"),
    @NamedQuery(name = "GlBankTransactionInvoiceView.findByValue", query = "SELECT g FROM GlBankTransactionInvoiceView g WHERE g.value = :value"),
    @NamedQuery(name = "GlBankTransactionInvoiceView.findByInvoiceId", query = "SELECT g FROM GlBankTransactionInvoiceView g WHERE g.invoiceId = :invoiceId"),
    @NamedQuery(name = "GlBankTransactionInvoiceView.findByOrganizationSiteId", query = "SELECT g FROM GlBankTransactionInvoiceView g WHERE g.organizationSiteId = :organizationSiteId"),
    @NamedQuery(name = "GlBankTransactionInvoiceView.findByBranchId", query = "SELECT g FROM GlBankTransactionInvoiceView g WHERE g.branchId = :branchId")})
public class GlBankTransactionInvoiceView implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "serial")
    private Integer serial;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "invoice_id")
    private Integer invoiceId;
    @Column(name = "organization_site_id")
    private Integer organizationSiteId;
    @Column(name = "branchId")
    private Integer branchId;

    public GlBankTransactionInvoiceView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    @Override
    public String toString() {
        return serial + " " ; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rowNum != null ? rowNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GlBankTransactionInvoiceView)) {
            return false;
        }
        GlBankTransactionInvoiceView other = (GlBankTransactionInvoiceView) object;
        if ((this.rowNum == null && other.rowNum != null) || (this.rowNum != null && !this.rowNum.equals(other.rowNum))) {
            return false;
        }
        return true;
    }
    
}
