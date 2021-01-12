
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


@Entity
@Table(name = "invoice_details_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvoiceDetailsReport.findAll", query = "SELECT i FROM InvoiceDetailsReport i"),
    @NamedQuery(name = "InvoiceDetailsReport.findByRowNum", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.rowNum = :rowNum"),
    @NamedQuery(name = "InvoiceDetailsReport.findByInvoice", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.invoice = :invoice"),
    @NamedQuery(name = "InvoiceDetailsReport.findByBranch", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.branch = :branch"),
    @NamedQuery(name = "InvoiceDetailsReport.findByBranchId", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.branchId = :branchId"),
    @NamedQuery(name = "InvoiceDetailsReport.findByDate", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.date = :date"),
    @NamedQuery(name = "InvoiceDetailsReport.findByClient", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.client = :client"),
    @NamedQuery(name = "InvoiceDetailsReport.findByClientName", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.clientName = :clientName"),
    @NamedQuery(name = "InvoiceDetailsReport.findByQuantity", query = "SELECT i FROM InvoiceDetailsReport i WHERE i.quantity = :quantity")})
public class InvoiceDetailsReport implements Serializable {
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invoice")
    private int invoice;
    @Size(max = 100)
    @Column(name = "branch")
    private String branch;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 45)
    @Column(name = "client")
    private String client;
    @Size(max = 45)
    @Column(name = "client_name")
    private String clientName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantity")
    private BigDecimal quantity;

    public InvoiceDetailsReport() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
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
    
}
