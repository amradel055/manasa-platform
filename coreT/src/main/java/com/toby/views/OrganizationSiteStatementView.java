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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hhhh
 */
@Entity
@Table(name = "organization_site_statement_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganizationSiteStatementView.findAll", query = "SELECT o FROM OrganizationSiteStatementView o"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByRowNum", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.rowNum = :rowNum"),
    @NamedQuery(name = "OrganizationSiteStatementView.findBySerial", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.serial = :serial"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByDate", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.date = :date"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByBranchId", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.branchId = :branchId"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByScreenName", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.screenName = :screenName"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationSiteId", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationSiteId = :organizationSiteId"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationCode", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationCode = :organizationCode"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationName", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationName = :organizationName"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationType", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationType = :organizationType"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationCurrencyId", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationCurrencyId = :organizationCurrencyId"),

    @NamedQuery(name = "OrganizationSiteStatementView.findByOpenningBalance", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.openningBalance = :openningBalance"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByExitt", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.exitt = :exitt"),
    @NamedQuery(name = "OrganizationSiteStatementView.findByAdding", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.adding = :adding")})
public class OrganizationSiteStatementView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @Column(name = "ScreenName")
    private String screenName;
    @Column(name = "organization_site_id")
    private Integer organizationSiteId;
    @Column(name = "organization_code")
    private String organizationCode;
    @Column(name = "organization_name")
    private String organizationName;
    @Column(name = "organization_currency_id")
    private Integer organizationCurrencyId;
    @Column(name = "organization_type")
    private Integer organizationType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "openning_balance")
    private BigDecimal openningBalance;
    @Column(name = "exitt")
    private BigDecimal exitt;
    @Column(name = "adding")
    private BigDecimal adding;
    @Column(name = "gl_bank_name")
    private String glBankName;
    @Column(name = "remarks")
    private String remarks;
    @Transient
    private Integer invoiceSerial;

    public OrganizationSiteStatementView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
    }

    public BigDecimal getOpenningBalance() {
        return openningBalance;
    }

    public void setOpenningBalance(BigDecimal openningBalance) {
        this.openningBalance = openningBalance;
    }

    /**
     * @return the exitt
     */
    public BigDecimal getExitt() {
        return exitt;
    }

    /**
     * @param exitt the exitt to set
     */
    public void setExitt(BigDecimal exitt) {
        this.exitt = exitt;
    }

    /**
     * @return the adding
     */
    public BigDecimal getAdding() {
        return adding;
    }

    /**
     * @param adding the adding to set
     */
    public void setAdding(BigDecimal adding) {
        this.adding = adding;
    }

    /**
     * @return the organizationCurrencyId
     */
    public Integer getOrganizationCurrencyId() {
        return organizationCurrencyId;
    }

    /**
     * @param organizationCurrencyId the organizationCurrencyId to set
     */
    public void setOrganizationCurrencyId(Integer organizationCurrencyId) {
        this.organizationCurrencyId = organizationCurrencyId;
    }

    /**
     * @return the glBankName
     */
    public String getGlBankName() {
        return glBankName;
    }

    /**
     * @param glBankName the glBankName to set
     */
    public void setGlBankName(String glBankName) {
        this.glBankName = glBankName;
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
     * @return the invoiceSerial
     */
    public Integer getInvoiceSerial() {
        return invoiceSerial;
    }

    /**
     * @param invoiceSerial the invoiceSerial to set
     */
    public void setInvoiceSerial(Integer invoiceSerial) {
        this.invoiceSerial = invoiceSerial;
    }

}
