/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "rsc_contract_register")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RscContractRegister.findAll", query = "SELECT r FROM RscContractRegister r"),
    @NamedQuery(name = "RscContractRegister.findById", query = "SELECT r FROM RscContractRegister r WHERE r.id = :id"),
    @NamedQuery(name = "RscContractRegister.findByDate", query = "SELECT r FROM RscContractRegister r WHERE r.date = :date"),
    @NamedQuery(name = "RscContractRegister.findBySerial", query = "SELECT r FROM RscContractRegister r WHERE r.serial = :serial"),
    @NamedQuery(name = "RscContractRegister.findByDocumentNumber", query = "SELECT r FROM RscContractRegister r WHERE r.documentNumber = :documentNumber"),
    @NamedQuery(name = "RscContractRegister.findByStatus", query = "SELECT r FROM RscContractRegister r WHERE r.status = :status"),
    @NamedQuery(name = "RscContractRegister.findByInitialReceivedDate", query = "SELECT r FROM RscContractRegister r WHERE r.initialReceivedDate = :initialReceivedDate"),
    @NamedQuery(name = "RscContractRegister.findByFinallReceivedDate", query = "SELECT r FROM RscContractRegister r WHERE r.finallReceivedDate = :finallReceivedDate"),
    @NamedQuery(name = "RscContractRegister.findByContractTotal", query = "SELECT r FROM RscContractRegister r WHERE r.contractTotal = :contractTotal"),
    @NamedQuery(name = "RscContractRegister.findByInitialValue", query = "SELECT r FROM RscContractRegister r WHERE r.initialValue = :initialValue"),
    @NamedQuery(name = "RscContractRegister.findByInitialPercentage", query = "SELECT r FROM RscContractRegister r WHERE r.initialPercentage = :initialPercentage"),
    @NamedQuery(name = "RscContractRegister.findByCreationDate", query = "SELECT r FROM RscContractRegister r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "RscContractRegister.findByModificationDate", query = "SELECT r FROM RscContractRegister r WHERE r.modificationDate = :modificationDate")})
public class RscContractRegister extends BaseEntity {

    @Column(name = "status")
    private Integer status;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "serial")
    private Integer serial;
    @Size(max = 45)
    @Column(name = "document_number")
    private String documentNumber;
    @Column(name = "initial_received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date initialReceivedDate;
    @Column(name = "finall_received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finallReceivedDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "contract_total")
    private BigDecimal contractTotal;
    @Column(name = "initial_value")
    private BigDecimal initialValue;
    @Column(name = "initial_percentage")
    private BigDecimal initialPercentage;
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    @ManyToOne
    private InvOrganizationSite siteId;
    @JoinColumn(name = "rsc_qotation_price_tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscQotationPriceTender rscQotationPriceTenderId;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "tender_id", referencedColumnName = "id")
    @ManyToOne
    private RscTender tenderId;
    @Column(name = "remarks")
    private String remarks;

    public RscContractRegister() {
    }

    public RscContractRegister(Integer id) {
        this.id = id;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the documentNumber
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * @param documentNumber the documentNumber to set
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @return the initialReceivedDate
     */
    public Date getInitialReceivedDate() {
        return initialReceivedDate;
    }

    /**
     * @param initialReceivedDate the initialReceivedDate to set
     */
    public void setInitialReceivedDate(Date initialReceivedDate) {
        this.initialReceivedDate = initialReceivedDate;
    }

    /**
     * @return the finallReceivedDate
     */
    public Date getFinallReceivedDate() {
        return finallReceivedDate;
    }

    /**
     * @param finallReceivedDate the finallReceivedDate to set
     */
    public void setFinallReceivedDate(Date finallReceivedDate) {
        this.finallReceivedDate = finallReceivedDate;
    }

    /**
     * @return the contractTotal
     */
    public BigDecimal getContractTotal() {
        return contractTotal;
    }

    /**
     * @param contractTotal the contractTotal to set
     */
    public void setContractTotal(BigDecimal contractTotal) {
        this.contractTotal = contractTotal;
    }

    /**
     * @return the initialValue
     */
    public BigDecimal getInitialValue() {
        return initialValue;
    }

    /**
     * @param initialValue the initialValue to set
     */
    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * @return the initialPercentage
     */
    public BigDecimal getInitialPercentage() {
        return initialPercentage;
    }

    /**
     * @param initialPercentage the initialPercentage to set
     */
    public void setInitialPercentage(BigDecimal initialPercentage) {
        this.initialPercentage = initialPercentage;
    }

    /**
     * @return the siteId
     */
    public InvOrganizationSite getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(InvOrganizationSite siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the branchId
     */
    public Branch getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the rscQotationPriceTenderId
     */
    public RscQotationPriceTender getRscQotationPriceTenderId() {
        return rscQotationPriceTenderId;
    }

    /**
     * @param rscQotationPriceTenderId the rscQotationPriceTenderId to set
     */
    public void setRscQotationPriceTenderId(RscQotationPriceTender rscQotationPriceTenderId) {
        this.rscQotationPriceTenderId = rscQotationPriceTenderId;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the tenderId
     */
    public RscTender getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public void setTenderId(RscTender tenderId) {
        this.tenderId = tenderId;
    }

}
