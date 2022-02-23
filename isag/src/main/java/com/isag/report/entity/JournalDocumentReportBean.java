package com.isag.report.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class JournalDocumentReportBean {

    private int journalNum;
    private int documentNum;
    private Date journalDate;
    private String revision;
    private String posting;
    private BigDecimal amount;
    private String user;
    private String remarks;
    private String postFlag;
    private String generalTypeName;
    

    private List<JournalDocumentDailyReportBean> journalDocumentDailyReportBeanList;

    public JournalDocumentReportBean() {
    }

    public int getJournalNum() {
        return journalNum;
    }

    public void setJournalNum(int journalNum) {
        this.journalNum = journalNum;
    }

    public int getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(int documentNum) {
        this.documentNum = documentNum;
    }

    public Date getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(Date journalDate) {
        this.journalDate = journalDate;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(String postFlag) {
        this.postFlag = postFlag;
    }

    public List<JournalDocumentDailyReportBean> getJournalDocumentDailyReportBeanList() {
        return journalDocumentDailyReportBeanList;
    }

    public void setJournalDocumentDailyReportBeanList(List<JournalDocumentDailyReportBean> journalDocumentDailyReportBeanList) {
        this.journalDocumentDailyReportBeanList = journalDocumentDailyReportBeanList;
    }

    /**
     * @return the generalTypeName
     */
    public String getGeneralTypeName() {
        return generalTypeName;
    }

    /**
     * @param generalTypeName the generalTypeName to set
     */
    public void setGeneralTypeName(String generalTypeName) {
        this.generalTypeName = generalTypeName;
    }
}
