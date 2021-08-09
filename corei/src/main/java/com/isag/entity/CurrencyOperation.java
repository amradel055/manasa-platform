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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FreeComp
 */
@Entity
@Table(name = "currency_operation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CurrencyOperation.findAll", query = "SELECT c FROM CurrencyOperation c"),
    @NamedQuery(name = "CurrencyOperation.findById", query = "SELECT c FROM CurrencyOperation c WHERE c.id = :id"),
    @NamedQuery(name = "CurrencyOperation.findByOperationDate", query = "SELECT c FROM CurrencyOperation c WHERE c.operationDate = :operationDate"),
    @NamedQuery(name = "CurrencyOperation.findByNotes", query = "SELECT c FROM CurrencyOperation c WHERE c.notes = :notes"),
    @NamedQuery(name = "CurrencyOperation.findByRate", query = "SELECT c FROM CurrencyOperation c WHERE c.rate = :rate"),
    @NamedQuery(name = "CurrencyOperation.findByCreationDate", query = "SELECT c FROM CurrencyOperation c WHERE c.creationDate = :creationDate"),
    @NamedQuery(name = "CurrencyOperation.findByModificationDate", query = "SELECT c FROM CurrencyOperation c WHERE c.modificationDate = :modificationDate")})
public class CurrencyOperation extends BaseEntity {

    @Column(name = "operation_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;
    @Size(max = 250)
    @Column(name = "notes")
    private String notes;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate")
    private BigDecimal rate;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;

    @Transient
    private Boolean markEdit;

    public CurrencyOperation() {
    }

    public CurrencyOperation(Integer id) {
        this.id = id;
    }

    public Date getOperationDate() {
        if (operationDate == null) {
            operationDate=new Date();
        }
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CurrencyOperation)) {
            return false;
        }
        CurrencyOperation other = (CurrencyOperation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.CurrencyOperation[ id=" + id + " ]";
    }

    /**
     * @return the markEdit
     */
    public Boolean getMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(Boolean markEdit) {
        this.markEdit = markEdit;
    }

}
