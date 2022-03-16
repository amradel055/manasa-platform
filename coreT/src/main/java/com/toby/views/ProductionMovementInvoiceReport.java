package com.toby.views;

import java.io.Serializable;
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
@Table(name = "production_movement_invoice_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductionMovementInvoiceReport.findAll", query = "SELECT p FROM ProductionMovementInvoiceReport p"),
    @NamedQuery(name = "ProductionMovementInvoiceReport.findByRowNum", query = "SELECT p FROM ProductionMovementInvoiceReport p WHERE p.rowNum = :rowNum"),
    @NamedQuery(name = "ProductionMovementInvoiceReport.findByInvoiceId", query = "SELECT p FROM ProductionMovementInvoiceReport p WHERE p.invoiceId = :invoiceId"),
    @NamedQuery(name = "ProductionMovementInvoiceReport.findByInvoice", query = "SELECT p FROM ProductionMovementInvoiceReport p WHERE p.invoice = :invoice"),
    @NamedQuery(name = "ProductionMovementInvoiceReport.findByBranchName", query = "SELECT p FROM ProductionMovementInvoiceReport p WHERE p.branchName = :branchName"),
    @NamedQuery(name = "ProductionMovementInvoiceReport.findByClientName", query = "SELECT p FROM ProductionMovementInvoiceReport p WHERE p.clientName = :clientName"),
    @NamedQuery(name = "ProductionMovementInvoiceReport.findByReceivedDate", query = "SELECT p FROM ProductionMovementInvoiceReport p WHERE p.receivedDate = :receivedDate")})
public class ProductionMovementInvoiceReport implements Serializable {
    
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invoiceId")
    private int invoiceId;
    @Column(name = "invoice")
    private int invoice;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "branchName")
    private String branchName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "clientName")
    private String clientName;
    @Column(name = "receivedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;

    public ProductionMovementInvoiceReport() {
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

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * @return the invoice
     */
    public int getInvoice() {
        return invoice;
    }

    /**
     * @param invoice the invoice to set
     */
    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }
    
}
