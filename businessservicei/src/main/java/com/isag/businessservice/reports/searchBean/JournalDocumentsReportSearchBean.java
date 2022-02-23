package com.isag.businessservice.reports.searchBean;

import java.math.BigDecimal;
import java.util.Date;

public class JournalDocumentsReportSearchBean {

    private Integer documentTypeFrom;
    private Integer documentTypeTo;
    private Integer documentNumForm;
    private Integer documentNumTo;
    private Integer journalNumForm;
    private Integer journalNumTo;
    private Date periodFrom;
    private Date periodTo;
    private Integer userFrom;
    private Integer userTo;
    private Integer branchId;
    private String postFlag;
    private String posting;

    private Integer glAccountFrom;
    private Integer glAccountTo;
    private Integer costCenterFrom;
    private Integer costCenterTo;
    private Integer adminUnitFrom;
    private Integer adminUnitTo;
    
    private BigDecimal value;

    public Integer getDocumentTypeFrom() {
        return documentTypeFrom;
    }

    public void setDocumentTypeFrom(Integer documentTypeFrom) {
        this.documentTypeFrom = documentTypeFrom;
    }

    public Integer getDocumentTypeTo() {
        return documentTypeTo;
    }

    public void setDocumentTypeTo(Integer documentTypeTo) {
        this.documentTypeTo = documentTypeTo;
    }

    public Integer getDocumentNumForm() {
        return documentNumForm;
    }

    public void setDocumentNumForm(Integer documentNumForm) {
        this.documentNumForm = documentNumForm;
    }

    public Integer getDocumentNumTo() {
        return documentNumTo;
    }

    public void setDocumentNumTo(Integer documentNumTo) {
        this.documentNumTo = documentNumTo;
    }

    public Integer getJournalNumForm() {
        return journalNumForm;
    }

    public void setJournalNumForm(Integer journalNumForm) {
        this.journalNumForm = journalNumForm;
    }

    public Integer getJournalNumTo() {
        return journalNumTo;
    }

    public void setJournalNumTo(Integer journalNumTo) {
        this.journalNumTo = journalNumTo;
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
    }

    public Integer getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(Integer userFrom) {
        this.userFrom = userFrom;
    }

    public Integer getUserTo() {
        return userTo;
    }

    public void setUserTo(Integer userTo) {
        this.userTo = userTo;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(String postFlag) {
        this.postFlag = postFlag;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public Integer getGlAccountFrom() {
        return glAccountFrom;
    }

    public void setGlAccountFrom(Integer glAccountFrom) {
        this.glAccountFrom = glAccountFrom;
    }

    public Integer getGlAccountTo() {
        return glAccountTo;
    }

    public void setGlAccountTo(Integer glAccountTo) {
        this.glAccountTo = glAccountTo;
    }

    public Integer getCostCenterFrom() {
        return costCenterFrom;
    }

    public void setCostCenterFrom(Integer costCenterFrom) {
        this.costCenterFrom = costCenterFrom;
    }

    public Integer getCostCenterTo() {
        return costCenterTo;
    }

    public void setCostCenterTo(Integer costCenterTo) {
        this.costCenterTo = costCenterTo;
    }

    public Integer getAdminUnitFrom() {
        return adminUnitFrom;
    }

    public void setAdminUnitFrom(Integer adminUnitFrom) {
        this.adminUnitFrom = adminUnitFrom;
    }

    public Integer getAdminUnitTo() {
        return adminUnitTo;
    }

    public void setAdminUnitTo(Integer adminUnitTo) {
        this.adminUnitTo = adminUnitTo;
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

}
