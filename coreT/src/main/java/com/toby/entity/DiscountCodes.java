/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m lapan
 */
@Entity
@Table(name = "discount_codes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiscountCodes.findAll", query = "SELECT d FROM DiscountCodes d")
    , @NamedQuery(name = "DiscountCodes.findById", query = "SELECT d FROM DiscountCodes d WHERE d.id = :id")
    , @NamedQuery(name = "DiscountCodes.findByCode", query = "SELECT d FROM DiscountCodes d WHERE d.code = :code")
    , @NamedQuery(name = "DiscountCodes.findByBankName", query = "SELECT d FROM DiscountCodes d WHERE d.bankName = :bankName")
    , @NamedQuery(name = "DiscountCodes.findByDiscountValue", query = "SELECT d FROM DiscountCodes d WHERE d.discountValue = :discountValue")
    , @NamedQuery(name = "DiscountCodes.findByModificationDate", query = "SELECT d FROM DiscountCodes d WHERE d.modificationDate = :modificationDate")
    , @NamedQuery(name = "DiscountCodes.findByCreationDate", query = "SELECT d FROM DiscountCodes d WHERE d.creationDate = :creationDate")})
public class DiscountCodes extends BaseEntity{

    @Column(name = "code")
    private String code;
    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    

    public DiscountCodes() {
    }

    public DiscountCodes(Integer id) {
        this.id = id;
    }

   

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    public Branch getBranchId() {
        return branchId;
    }

    public void setBranchId(Branch branchId) {
        this.branchId = branchId;
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
        if (!(object instanceof DiscountCodes)) {
            return false;
        }
        DiscountCodes other = (DiscountCodes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.DiscountCodes[ id=" + id + " ]";
    }

    /**
     * @return the discountValue
     */
    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    /**
     * @param discountValue the discountValue to set
     */
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
    
}
