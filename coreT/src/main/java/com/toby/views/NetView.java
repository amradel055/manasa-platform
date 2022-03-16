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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elsakr6
 */
@Entity
@Table(name = "net_view")
@XmlRootElement
public class NetView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @Column(name = "head_id")
    private int headId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "invoice_id")
    private Integer invoiceId;
    @Column(name = "serial_tax")
    private Integer serialTax;    
    @Column(name = "tax_flag")
    private Boolean taxFlag;
    @Column(name = "tax_flag_final")
    private Boolean taxFlagFinal;
    @Column(name = "tax_flag_final_text")
    private String taxFlagFinalText;
    @Column(name = "type")
    private Integer type;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Column(name = "inventory_name")
    private String inventoryName;
    @Column(name = "gl_bank_id")
    private Integer glBankId;
    @Column(name = "gl_bank_name")
    private String glBankName;
    @Column(name = "inv_delegator")
    private Integer invDelegator;
    @Column(name = "inv_delegator_name")
    private String invDelegatorName;
    @Column(name = "organization_site_id")
    private Integer organizationSiteId;
    @Column(name = "organization_site_name")
    private String organizationSiteName;
    @Column(name = "organization_site_code")
    private String organizationSiteCode;
    @Column(name = "organization_site_type")
    private Integer organizationSiteType;
    @Basic(optional = false)
    @Column(name = "payment_type")
    private int paymentType;
    @Column(name = "payment_type_text")
    private String paymentTypeText;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "head_discount")
    private BigDecimal headDiscount;
    @Column(name = "sum_after_discount")
    private String sumAfterDiscount;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "net")
    private BigDecimal net;
    @Column(name = "gl_year")
    private Integer glYear;
    @Basic(optional = false)
    @Column(name = "post_flag")
    private int postFlag;
    @Column(name = "general_journal_id")
    private Integer generalJournalId;
    @Column(name = "net_local")
    private BigDecimal netLocal;
    @Basic(optional = false)
    @Column(name = "company_id")
    private int companyId;
    @Column(name = "user_name")
    private String userName;    
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "sum_total_Cost")
    private BigDecimal sumTotalCost;
    
    public NetView() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public Integer getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(Integer glBankId) {
        this.glBankId = glBankId;
    }

    public String getGlBankName() {
        return glBankName;
    }

    public void setGlBankName(String glBankName) {
        this.glBankName = glBankName;
    }

    public Integer getInvDelegator() {
        return invDelegator;
    }

    public void setInvDelegator(Integer invDelegator) {
        this.invDelegator = invDelegator;
    }

    public String getInvDelegatorName() {
        return invDelegatorName;
    }

    public void setInvDelegatorName(String invDelegatorName) {
        this.invDelegatorName = invDelegatorName;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public String getOrganizationSiteName() {
        return organizationSiteName;
    }

    public void setOrganizationSiteName(String organizationSiteName) {
        this.organizationSiteName = organizationSiteName;
    }

    public Integer getOrganizationSiteType() {
        return organizationSiteType;
    }

    public void setOrganizationSiteType(Integer organizationSiteType) {
        this.organizationSiteType = organizationSiteType;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getHeadDiscount() {
        return headDiscount;
    }

    public void setHeadDiscount(BigDecimal headDiscount) {
        this.headDiscount = headDiscount;
    }

    public String getSumAfterDiscount() {
        return sumAfterDiscount;
    }

    public void setSumAfterDiscount(String sumAfterDiscount) {
        this.sumAfterDiscount = sumAfterDiscount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Integer getSerialTax() {
        return serialTax;
    }

    public void setSerialTax(Integer serialTax) {
        this.serialTax = serialTax;
    }

    /**
     * @return the taxFlagFinal
     */
    public Boolean getTaxFlagFinal() {
        return taxFlagFinal;
    }

    /**
     * @param taxFlagFinal the taxFlagFinal to set
     */
    public void setTaxFlagFinal(Boolean taxFlagFinal) {
        this.taxFlagFinal = taxFlagFinal;
    }

    /**
     * @return the taxFlagFinalText
     */
    public String getTaxFlagFinalText() {
        return taxFlagFinalText;
    }

    /**
     * @param taxFlagFinalText the taxFlagFinalText to set
     */
    public void setTaxFlagFinalText(String taxFlagFinalText) {
        this.taxFlagFinalText = taxFlagFinalText;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the paymentTypeText
     */
    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    /**
     * @param paymentTypeText the paymentTypeText to set
     */
    public void setPaymentTypeText(String paymentTypeText) {
        this.paymentTypeText = paymentTypeText;
    }
    
    public Integer getGlYear() {
        return glYear;
    }

    public void setGlYear(Integer glYear) {
        this.glYear = glYear;
    }

    public int getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(int postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    public BigDecimal getNetLocal() {
        return netLocal;
    }

    public void setNetLocal(BigDecimal netLocal) {
        this.netLocal = netLocal;
    }
    
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the taxFlag
     */
    public Boolean getTaxFlag() {
        return taxFlag;
    }

    /**
     * @param taxFlag the taxFlag to set
     */
    public void setTaxFlag(Boolean taxFlag) {
        this.taxFlag = taxFlag;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the sumTotalCost
     */
    public BigDecimal getSumTotalCost() {
        return sumTotalCost;
    }

    /**
     * @param sumTotalCost the sumTotalCost to set
     */
    public void setSumTotalCost(BigDecimal sumTotalCost) {
        this.sumTotalCost = sumTotalCost;
    }

    /**
     * @return the organizationSiteCode
     */
    public String getOrganizationSiteCode() {
        return organizationSiteCode;
    }

    /**
     * @param organizationSiteCode the organizationSiteCode to set
     */
    public void setOrganizationSiteCode(String organizationSiteCode) {
        this.organizationSiteCode = organizationSiteCode;
    }

}
