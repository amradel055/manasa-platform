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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ahmed
 */
@Entity
@Table(name = "post_restrictions_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PostRestrictionsView.findAll", query = "SELECT p FROM PostRestrictionsView p")
    , @NamedQuery(name = "PostRestrictionsView.findByRowNum", query = "SELECT p FROM PostRestrictionsView p WHERE p.rowNum = :rowNum")
    , @NamedQuery(name = "PostRestrictionsView.findByBranchId", query = "SELECT p FROM PostRestrictionsView p WHERE p.branchId = :branchId")
    , @NamedQuery(name = "PostRestrictionsView.findByCompanyId", query = "SELECT p FROM PostRestrictionsView p WHERE p.companyId = :companyId")
    , @NamedQuery(name = "PostRestrictionsView.findById", query = "SELECT p FROM PostRestrictionsView p WHERE p.id = :id")
    , @NamedQuery(name = "PostRestrictionsView.findByDocumentTypeId", query = "SELECT p FROM PostRestrictionsView p WHERE p.documentTypeId = :documentTypeId")
    , @NamedQuery(name = "PostRestrictionsView.findByDate", query = "SELECT p FROM PostRestrictionsView p WHERE p.date = :date")
    , @NamedQuery(name = "PostRestrictionsView.findByRemark", query = "SELECT p FROM PostRestrictionsView p WHERE p.remark = :remark")
    , @NamedQuery(name = "PostRestrictionsView.findByGlYear", query = "SELECT p FROM PostRestrictionsView p WHERE p.glYear = :glYear")
    , @NamedQuery(name = "PostRestrictionsView.findByPostFlag", query = "SELECT p FROM PostRestrictionsView p WHERE p.postFlag = :postFlag")
    , @NamedQuery(name = "PostRestrictionsView.findBySerial", query = "SELECT p FROM PostRestrictionsView p WHERE p.serial = :serial")
    , @NamedQuery(name = "PostRestrictionsView.findByValueLocal", query = "SELECT p FROM PostRestrictionsView p WHERE p.valueLocal = :valueLocal")
    , @NamedQuery(name = "PostRestrictionsView.findByValue", query = "SELECT p FROM PostRestrictionsView p WHERE p.value = :value")
    , @NamedQuery(name = "PostRestrictionsView.findByGeneralJournalId", query = "SELECT p FROM PostRestrictionsView p WHERE p.generalJournalId = :generalJournalId")

    , @NamedQuery(name = "PostRestrictionsView.findByScreenCode", query = "SELECT p FROM PostRestrictionsView p WHERE p.screenCode = :screenCode")})
public class PostRestrictionsView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "company_id")
    private Integer companyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "document_type_id")
    private String documentTypeId;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Size(max = 450)
    @Column(name = "remark")
    private String remark;
    @Column(name = "gl_year")
    private Integer glYear;
    @Column(name = "post_flag")
    private Integer postFlag;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Column(name = "value_local")
    private BigDecimal valueLocal;
    @Column(name = "installment_value")
    private BigDecimal installmentValue;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "general_journal_id")
    private Integer generalJournalId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ScreenCode")
    private long screenCode;
    @Transient
    private Integer accountId; 
    @Transient
    private String screenName;
    
    public PostRestrictionsView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGlYear() {
        return glYear;
    }

    public void setGlYear(Integer glYear) {
        this.glYear = glYear;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public long getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(long screenCode) {
        this.screenCode = screenCode;
    }

    /**
     * @return the valueLocal
     */
    public BigDecimal getValueLocal() {
        return valueLocal;
    }

    /**
     * @param valueLocal the valueLocal to set
     */
    public void setValueLocal(BigDecimal valueLocal) {
        this.valueLocal = valueLocal;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the generalJournalId
     */
    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    /**
     * @param generalJournalId the generalJournalId to set
     */
    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    /**
     * @return the accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the installmentValue
     */
    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    /**
     * @param installmentValue the installmentValue to set
     */
    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
    }

    /**
     * @return the screenName
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @param screenName the screenName to set
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }



}
