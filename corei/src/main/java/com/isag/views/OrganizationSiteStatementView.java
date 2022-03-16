/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.views;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hhhh
 */
@Entity
@Table(name = "organization_site_statement_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganizationSiteStatementView.findAll", query = "SELECT o FROM OrganizationSiteStatementView o")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByRowNum", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.rowNum = :rowNum")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findBySerial", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.serial = :serial")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByDate", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.date = :date")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByBranchId", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.branchId = :branchId")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByScreenName", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.screenName = :screenName")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationSiteId", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationSiteId = :organizationSiteId")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationCode", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationCode = :organizationCode")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationName", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationName = :organizationName")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationType", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationType = :organizationType")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByOrganizationCurrencyId", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.organizationCurrencyId = :organizationCurrencyId")
    ,

    @NamedQuery(name = "OrganizationSiteStatementView.findByOpenningBalance", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.openningBalance = :openningBalance")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByExitt", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.exitt = :exitt")
    ,
    @NamedQuery(name = "OrganizationSiteStatementView.findByAdding", query = "SELECT o FROM OrganizationSiteStatementView o WHERE o.adding = :adding")})
public class OrganizationSiteStatementView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "idNumber")
    private Integer idNumber;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @Column(name = "ScreenName")
    private String screenName;
    @Basic(optional = false)
    @Column(name = "ScreenName2")
    private String screenName2;
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
    @Column(name = "inst_contract_serial")
    private Integer instContractSerial;
    @Column(name = "gl_bank_name")
    private String glBankName;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "supplier_invoice_number")
    private String supplierInvoiceNumber;
    @Column(name = "total_before_fracture")
    private BigDecimal totalBeforeFracture;
    @Column(name = "fracture_number")
    private BigDecimal fractureNumber;
    @Column(name = "value_fracture")
    private BigDecimal valueFracture;
    @Column(name = "look")
    private Integer look;
    @Column(name = "is_done")
    private Integer isDone;

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
        if (exitt == null) {
            exitt = BigDecimal.ZERO;
        }
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
        if (adding == null) {
            adding = BigDecimal.ZERO;
        }
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
     * @return the instContractSerial
     */
    public Integer getInstContractSerial() {
        return instContractSerial;
    }

    /**
     * @param instContractSerial the instContractSerial to set
     */
    public void setInstContractSerial(Integer instContractSerial) {
        this.instContractSerial = instContractSerial;
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
     * @return the screenName2
     */
    public String getScreenName2() {
        return screenName2;
    }

    /**
     * @param screenName2 the screenName2 to set
     */
    public void setScreenName2(String screenName2) {
        this.screenName2 = screenName2;
    }

    /**
     * @return the supplierInvoiceNumber
     */
    public String getSupplierInvoiceNumber() {
        return supplierInvoiceNumber;
    }

    /**
     * @param supplierInvoiceNumber the supplierInvoiceNumber to set
     */
    public void setSupplierInvoiceNumber(String supplierInvoiceNumber) {
        this.supplierInvoiceNumber = supplierInvoiceNumber;
    }

    /**
     * @return the totalBeforeFracture
     */
    public BigDecimal getTotalBeforeFracture() {
        return totalBeforeFracture;
    }

    /**
     * @param totalBeforeFracture the totalBeforeFracture to set
     */
    public void setTotalBeforeFracture(BigDecimal totalBeforeFracture) {
        this.totalBeforeFracture = totalBeforeFracture;
    }

    /**
     * @return the fractureNumber
     */
    public BigDecimal getFractureNumber() {
        return fractureNumber;
    }

    /**
     * @param fractureNumber the fractureNumber to set
     */
    public void setFractureNumber(BigDecimal fractureNumber) {
        this.fractureNumber = fractureNumber;
    }

    /**
     * @return the valueFracture
     */
    public BigDecimal getValueFracture() {
        return valueFracture;
    }

    /**
     * @param valueFracture the valueFracture to set
     */
    public void setValueFracture(BigDecimal valueFracture) {
        this.valueFracture = valueFracture;
    }

    /**
     * @return the idNumber
     */
    public Integer getIdNumber() {
        return idNumber;
    }

    /**
     * @param idNumber the idNumber to set
     */
    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * @return the look
     */
    public Integer getLook() {
        if(look == null){
            look = 0 ;
        }
        return look;
    }

    /**
     * @param look the look to set
     */
    public void setLook(Integer look) {
        this.look = look;
    }

    /**
     * @return the isDone
     */
    public Integer getIsDone() {
        if(isDone == null){
            isDone = 0 ;
        }
        return isDone;
    }

    /**
     * @param isDone the isDone to set
     */
    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

}
