/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

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
@Table(name = "gl_bank")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GlBank.findAll", query = "SELECT g FROM GlBank g"),
    @NamedQuery(name = "GlBank.findById", query = "SELECT g FROM GlBank g WHERE g.id = :id"),
    @NamedQuery(name = "GlBank.findByName", query = "SELECT g FROM GlBank g WHERE g.name = :name"),
    @NamedQuery(name = "GlBank.findByCode", query = "SELECT g FROM GlBank g WHERE g.code = :code"),
    @NamedQuery(name = "GlBank.findByCreationDate", query = "SELECT g FROM GlBank g WHERE g.creationDate = :creationDate"),
    @NamedQuery(name = "GlBank.findByModificationDate", query = "SELECT g FROM GlBank g WHERE g.modificationDate = :modificationDate")})
public class GlBank extends BaseEntity {

    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "code")
    private Integer code;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "openinng_balance")
    private BigDecimal openinngBalance;
    @Column(name = "date_openinng_balance")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpeninngBalance;
    @Column(name = "date_hegry")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHegry;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @Column(name = "type")
    private Integer type;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currencyId;
//    @Column(name = "raghi_point")
//    private Integer raghiPoint;
    

    public GlBank() {
    }

    public GlBank(Integer id) {
        this.id = id;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public GlAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
        if (!(object instanceof GlBank)) {
            return false;
        }
        GlBank other = (GlBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code;
    }

    public BigDecimal getOpeninngBalance() {
        return openinngBalance;
    }

    public void setOpeninngBalance(BigDecimal openinngBalance) {
        this.openinngBalance = openinngBalance;
    }

    public Date getDateOpeninngBalance() {
        return dateOpeninngBalance;
    }

    public void setDateOpeninngBalance(Date dateOpeninngBalance) {
        this.dateOpeninngBalance = dateOpeninngBalance;
    }

    /**
     * @return the dateHegry
     */
    public Date getDateHegry() {
        return dateHegry;
    }

    /**
     * @param dateHegry the dateHegry to set
     */
    public void setDateHegry(Date dateHegry) {
        this.dateHegry = dateHegry;
    }

//    /**
//     * @return the raghiPoint
//     */
//    public Integer getRaghiPoint() {
//        return raghiPoint;
//    }
//
//    /**
//     * @param raghiPoint the raghiPoint to set
//     */
//    public void setRaghiPoint(Integer raghiPoint) {
//        this.raghiPoint = raghiPoint;
//    }

}
