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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "get_operation_rate_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GetOperationRateView.findAll", query = "SELECT g FROM GetOperationRateView g")
    , @NamedQuery(name = "GetOperationRateView.findByRowNum", query = "SELECT g FROM GetOperationRateView g WHERE g.rowNum = :rowNum")
    , @NamedQuery(name = "GetOperationRateView.findById", query = "SELECT g FROM GetOperationRateView g WHERE g.id = :id")
    , @NamedQuery(name = "GetOperationRateView.findByOperationDate", query = "SELECT g FROM GetOperationRateView g WHERE g.operationDate = :operationDate")
    , @NamedQuery(name = "GetOperationRateView.findByNotes", query = "SELECT g FROM GetOperationRateView g WHERE g.notes = :notes")
    , @NamedQuery(name = "GetOperationRateView.findByCurrencyId", query = "SELECT g FROM GetOperationRateView g WHERE g.currencyId = :currencyId")
    , @NamedQuery(name = "GetOperationRateView.findByRate", query = "SELECT g FROM GetOperationRateView g WHERE g.rate = :rate")
    , @NamedQuery(name = "GetOperationRateView.findByCreatedBy", query = "SELECT g FROM GetOperationRateView g WHERE g.createdBy = :createdBy")
    , @NamedQuery(name = "GetOperationRateView.findByCreationDate", query = "SELECT g FROM GetOperationRateView g WHERE g.creationDate = :creationDate")
    , @NamedQuery(name = "GetOperationRateView.findByModifiedBy", query = "SELECT g FROM GetOperationRateView g WHERE g.modifiedBy = :modifiedBy")
    , @NamedQuery(name = "GetOperationRateView.findByModificationDate", query = "SELECT g FROM GetOperationRateView g WHERE g.modificationDate = :modificationDate")
    , @NamedQuery(name = "GetOperationRateView.findByCompanyId", query = "SELECT g FROM GetOperationRateView g WHERE g.companyId = :companyId")})
public class GetOperationRateView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Column(name = "operation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
    @Size(max = 250)
    @Column(name = "notes")
    private String notes;
    @Column(name = "currency_id")
    private Integer currencyId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate")
    private BigDecimal rate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_by")
    private int createdBy;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "modified_by")
    private Integer modifiedBy;
    @Column(name = "modification_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "company_id")
    private int companyId;

    public GetOperationRateView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
    
}
