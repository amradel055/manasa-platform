/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author H
 */
@Entity
@Table(name = "pro_production_movement_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProProductionMovementReport.findAll", query = "SELECT p FROM ProProductionMovementReport p"),
    @NamedQuery(name = "ProProductionMovementReport.findByRowNum", query = "SELECT p FROM ProProductionMovementReport p WHERE p.rowNum = :rowNum"),
    @NamedQuery(name = "ProProductionMovementReport.findByDriverName", query = "SELECT p FROM ProProductionMovementReport p WHERE p.driverName = :driverName"),
    @NamedQuery(name = "ProProductionMovementReport.findByDate", query = "SELECT p FROM ProProductionMovementReport p WHERE p.date = :date"),
    @NamedQuery(name = "ProProductionMovementReport.findByInvoiceSerial", query = "SELECT p FROM ProProductionMovementReport p WHERE p.invoiceSerial = :invoiceSerial"),
    @NamedQuery(name = "ProProductionMovementReport.findByCreationDate", query = "SELECT p FROM ProProductionMovementReport p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProProductionMovementReport.findByClientName", query = "SELECT p FROM ProProductionMovementReport p WHERE p.clientName = :clientName"),
    @NamedQuery(name = "ProProductionMovementReport.findByBranchId", query = "SELECT p FROM ProProductionMovementReport p WHERE p.branchId = :branchId")})
public class ProProductionMovementReport implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Size(max = 45)
    @Column(name = "driver_name")
    private String driverName;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invoice_serial")
    private Integer invoiceSerial;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Size(max = 45)
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "branch_id")
    private Integer branchId;

    public ProProductionMovementReport() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }
    
}
