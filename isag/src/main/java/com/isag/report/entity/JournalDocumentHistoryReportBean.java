package com.isag.report.entity;

import java.math.BigDecimal;
import java.util.Date;

public class JournalDocumentHistoryReportBean extends JournalDocumentReportBean{

    private Date journalDateTo;
    private String postingTo;
    private BigDecimal amountTo;
    private String userTo;
    private String remarksTo;

    public JournalDocumentHistoryReportBean() {
    }

    /**
     * @return the journalDateTo
     */
    public Date getJournalDateTo() {
        return journalDateTo;
    }

    /**
     * @param journalDateTo the journalDateTo to set
     */
    public void setJournalDateTo(Date journalDateTo) {
        this.journalDateTo = journalDateTo;
    }

    /**
     * @return the postingTo
     */
    public String getPostingTo() {
        return postingTo;
    }

    /**
     * @param postingTo the postingTo to set
     */
    public void setPostingTo(String postingTo) {
        this.postingTo = postingTo;
    }

    /**
     * @return the amountTo
     */
    public BigDecimal getAmountTo() {
        return amountTo;
    }

    /**
     * @param amountTo the amountTo to set
     */
    public void setAmountTo(BigDecimal amountTo) {
        this.amountTo = amountTo;
    }

    /**
     * @return the userTo
     */
    public String getUserTo() {
        return userTo;
    }

    /**
     * @param userTo the userTo to set
     */
    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    /**
     * @return the remarksTo
     */
    public String getRemarksTo() {
        return remarksTo;
    }

    /**
     * @param remarksTo the remarksTo to set
     */
    public void setRemarksTo(String remarksTo) {
        this.remarksTo = remarksTo;
    }

    
}
