/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "inst_project_guide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InstProjectGuide.findAll", query = "SELECT i FROM InstProjectGuide i")
    , @NamedQuery(name = "InstProjectGuide.findById", query = "SELECT i FROM InstProjectGuide i WHERE i.id = :id")
    , @NamedQuery(name = "InstProjectGuide.findByCode", query = "SELECT i FROM InstProjectGuide i WHERE i.code = :code")
    , @NamedQuery(name = "InstProjectGuide.findByName", query = "SELECT i FROM InstProjectGuide i WHERE i.name = :name")
    , @NamedQuery(name = "InstProjectGuide.findByLevel", query = "SELECT i FROM InstProjectGuide i WHERE i.level = :level")
    , @NamedQuery(name = "InstProjectGuide.findByActive", query = "SELECT i FROM InstProjectGuide i WHERE i.active = :active")
    , @NamedQuery(name = "InstProjectGuide.findByCreationDate", query = "SELECT i FROM InstProjectGuide i WHERE i.creationDate = :creationDate")
    , @NamedQuery(name = "InstProjectGuide.findByModificationDate", query = "SELECT i FROM InstProjectGuide i WHERE i.modificationDate = :modificationDate")})
public class InstProjectGuide extends BaseEntity {

    @Size(max = 450)
    @Column(name = "code")
    private String code;
    @Size(max = 450)
    @Column(name = "name")
    private String name;
    @Column(name = "level")
    private Integer level;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "price")
    private BigDecimal price;
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Collection<InstProjectGuide> instProjectGuideCollection;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne
    private InstProjectGuide parent;
    @JoinColumn(name = "branch_id", referencedColumnName = "id")
    @ManyToOne
    private Branch branchId;
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<InstProjectGuide> childNodes;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @JoinColumn(name = "account_sale_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountSaleId;
    @JoinColumn(name = "symbol_size", referencedColumnName = "id")
    @ManyToOne
    private Symbol symbolSize;
    @Transient
    private Boolean sell;
    @Column(name = "reserved")
    private Boolean reserved;
      
    public InstProjectGuide() {
    }

    public InstProjectGuide(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @XmlTransient
    public Collection<InstProjectGuide> getInstProjectGuideCollection() {
        return instProjectGuideCollection;
    }

    public void setInstProjectGuideCollection(Collection<InstProjectGuide> instProjectGuideCollection) {
        this.instProjectGuideCollection = instProjectGuideCollection;
    }

    public InstProjectGuide getParent() {
        return parent;
    }

    public void setParent(InstProjectGuide parent) {
        this.parent = parent;
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
        if (!(object instanceof InstProjectGuide)) {
            return false;
        }
        InstProjectGuide other = (InstProjectGuide) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + code; //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the childNodes
     */
    public List<InstProjectGuide> getChildNodes() {
        return childNodes;
    }

    /**
     * @param childNodes the childNodes to set
     */
    public void setChildNodes(List<InstProjectGuide> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the accountId
     */
    public GlAccount getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the accountSaleId
     */
    public GlAccount getAccountSaleId() {
        return accountSaleId;
    }

    /**
     * @param accountSaleId the accountSaleId to set
     */
    public void setAccountSaleId(GlAccount accountSaleId) {
        this.accountSaleId = accountSaleId;
    }

    /**
     * @return the sell
     */
    public Boolean getSell() {
        if(sell == null){
        sell = Boolean.FALSE;
        }
        return sell;
    }

    /**
     * @param sell the sell to set
     */
    public void setSell(Boolean sell) {
        this.sell = sell;
    }

    /**
     * @return the reserved
     */
    public Boolean getReserved() {
        return reserved;
    }

    /**
     * @param reserved the reserved to set
     */
    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    /**
     * @return the symbolSize
     */
    public Symbol getSymbolSize() {
        return symbolSize;
    }

    /**
     * @param symbolSize the symbolSize to set
     */
    public void setSymbolSize(Symbol symbolSize) {
        this.symbolSize = symbolSize;
    }

}
