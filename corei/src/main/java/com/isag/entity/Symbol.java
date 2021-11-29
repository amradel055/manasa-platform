/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entity;

import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "symbol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Symbol.findAll", query = "SELECT s FROM Symbol s"),
    @NamedQuery(name = "Symbol.findById", query = "SELECT s FROM Symbol s WHERE s.id = :id"),
    @NamedQuery(name = "Symbol.findByName", query = "SELECT s FROM Symbol s WHERE s.name = :name"),
    @NamedQuery(name = "Symbol.findBySerial", query = "SELECT s FROM Symbol s WHERE s.serial = :serial"),
    @NamedQuery(name = "Symbol.findByCreationDate", query = "SELECT s FROM Symbol s WHERE s.creationDate = :creationDate"),
    @NamedQuery(name = "Symbol.findByModificationDate", query = "SELECT s FROM Symbol s WHERE s.modificationDate = :modificationDate")})
public class Symbol extends BaseEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "cost")
    private BigDecimal cost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "serial")
    private Integer serial;    
    @Column(name = "arrange")
    private Integer arrange;
    @JoinColumn(name = "general_symbol_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GeneralSymbol generalSymbolId;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private GlAccount accountId;
    @OneToMany(mappedBy = "unitId")
    private Collection<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "symbol")
    private Collection<CompanyLanguage> companyLanguageCollection;
    @OneToMany(mappedBy = "typeBarcode")
    private Collection<InvBarcode> invBarcodeCollection;
    @OneToMany(mappedBy = "unitId")
    private Collection<InvBarcode> invBarcodeCollection1;
    @OneToMany(mappedBy = "brandId")
    private Collection<InvItem> invItemCollection;
    @OneToMany(mappedBy = "enamelColor")
    private Collection<InvItem> invItemCollection1;
    @OneToMany(mappedBy = "originCountry")
    private Collection<InvItem> invItemCollection2;
    @OneToMany(mappedBy = "paintColor")
    private Collection<InvItem> invItemCollection3;
    @OneToMany(mappedBy = "unitId")
    private Collection<InvItem> invItemCollection4;
    @OneToMany(mappedBy = "stone")
    private Collection<InvItem> invItemCollection5;
    @OneToMany(mappedBy = "countryId")
    private Collection<InvOrganizationSite> invOrganizationSiteCollection;
    @OneToMany(mappedBy = "regionId")
    private Collection<InvOrganizationSite> invOrganizationSiteCollection1;
    @OneToMany(mappedBy = "supplierType")
    private Collection<InvOrganizationSite> invOrganizationSiteCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lang")
    private Collection<DataDictionary> dataDictionaryCollection;
    @OneToMany(mappedBy = "unitId")
    private Collection<InvPurchaseOrderDetail> invPurchaseOrderDetailCollection;
    @OneToMany(mappedBy = "lang")
    private Collection<IsagUser> isagUserCollection;

    public Symbol() {
    }

    public Symbol(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public GeneralSymbol getGeneralSymbolId() {
        return generalSymbolId;
    }

    public void setGeneralSymbolId(GeneralSymbol generalSymbolId) {
        this.generalSymbolId = generalSymbolId;
    }

    public GlAccount getAccountId() {
        return accountId;
    }

    public void setAccountId(GlAccount accountId) {
        this.accountId = accountId;
    }

    @XmlTransient
    public Collection<InvPurchaseInvoiceDetail> getInvPurchaseInvoiceDetailCollection() {
        return invPurchaseInvoiceDetailCollection;
    }

    public void setInvPurchaseInvoiceDetailCollection(Collection<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailCollection) {
        this.invPurchaseInvoiceDetailCollection = invPurchaseInvoiceDetailCollection;
    }

    @XmlTransient
    public Collection<CompanyLanguage> getCompanyLanguageCollection() {
        return companyLanguageCollection;
    }

    public void setCompanyLanguageCollection(Collection<CompanyLanguage> companyLanguageCollection) {
        this.companyLanguageCollection = companyLanguageCollection;
    }

    @XmlTransient
    public Collection<InvBarcode> getInvBarcodeCollection() {
        return invBarcodeCollection;
    }

    public void setInvBarcodeCollection(Collection<InvBarcode> invBarcodeCollection) {
        this.invBarcodeCollection = invBarcodeCollection;
    }

    @XmlTransient
    public Collection<InvBarcode> getInvBarcodeCollection1() {
        return invBarcodeCollection1;
    }

    public void setInvBarcodeCollection1(Collection<InvBarcode> invBarcodeCollection1) {
        this.invBarcodeCollection1 = invBarcodeCollection1;
    }

    @XmlTransient
    public Collection<InvItem> getInvItemCollection() {
        return invItemCollection;
    }

    public void setInvItemCollection(Collection<InvItem> invItemCollection) {
        this.invItemCollection = invItemCollection;
    }

    @XmlTransient
    public Collection<InvItem> getInvItemCollection1() {
        return invItemCollection1;
    }

    public void setInvItemCollection1(Collection<InvItem> invItemCollection1) {
        this.invItemCollection1 = invItemCollection1;
    }

    @XmlTransient
    public Collection<InvItem> getInvItemCollection2() {
        return invItemCollection2;
    }

    public void setInvItemCollection2(Collection<InvItem> invItemCollection2) {
        this.invItemCollection2 = invItemCollection2;
    }

    @XmlTransient
    public Collection<InvItem> getInvItemCollection3() {
        return invItemCollection3;
    }

    public void setInvItemCollection3(Collection<InvItem> invItemCollection3) {
        this.invItemCollection3 = invItemCollection3;
    }

    @XmlTransient
    public Collection<InvItem> getInvItemCollection4() {
        return invItemCollection4;
    }

    public void setInvItemCollection4(Collection<InvItem> invItemCollection4) {
        this.invItemCollection4 = invItemCollection4;
    }

    @XmlTransient
    public Collection<InvItem> getInvItemCollection5() {
        return invItemCollection5;
    }

    public void setInvItemCollection5(Collection<InvItem> invItemCollection5) {
        this.invItemCollection5 = invItemCollection5;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection() {
        return invOrganizationSiteCollection;
    }

    public void setInvOrganizationSiteCollection(Collection<InvOrganizationSite> invOrganizationSiteCollection) {
        this.invOrganizationSiteCollection = invOrganizationSiteCollection;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection1() {
        return invOrganizationSiteCollection1;
    }

    public void setInvOrganizationSiteCollection1(Collection<InvOrganizationSite> invOrganizationSiteCollection1) {
        this.invOrganizationSiteCollection1 = invOrganizationSiteCollection1;
    }

    @XmlTransient
    public Collection<InvOrganizationSite> getInvOrganizationSiteCollection2() {
        return invOrganizationSiteCollection2;
    }

    public void setInvOrganizationSiteCollection2(Collection<InvOrganizationSite> invOrganizationSiteCollection2) {
        this.invOrganizationSiteCollection2 = invOrganizationSiteCollection2;
    }

    @XmlTransient
    public Collection<DataDictionary> getDataDictionaryCollection() {
        return dataDictionaryCollection;
    }

    public void setDataDictionaryCollection(Collection<DataDictionary> dataDictionaryCollection) {
        this.dataDictionaryCollection = dataDictionaryCollection;
    }

    @XmlTransient
    public Collection<InvPurchaseOrderDetail> getInvPurchaseOrderDetailCollection() {
        return invPurchaseOrderDetailCollection;
    }

    public void setInvPurchaseOrderDetailCollection(Collection<InvPurchaseOrderDetail> invPurchaseOrderDetailCollection) {
        this.invPurchaseOrderDetailCollection = invPurchaseOrderDetailCollection;
    }

    @XmlTransient
    public Collection<IsagUser> getIsagUserCollection() {
        return isagUserCollection;
    }

    public void setIsagUserCollection(Collection<IsagUser> isagUserCollection) {
        this.isagUserCollection = isagUserCollection;
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
        if (!(object instanceof Symbol)) {
            return false;
        }
        Symbol other = (Symbol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + serial;
    }

    /**
     * @return the arrange
     */
    public Integer getArrange() {
        return arrange;
    }

    /**
     * @param arrange the arrange to set
     */
    public void setArrange(Integer arrange) {
        this.arrange = arrange;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

}
