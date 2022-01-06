/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hq002
 */
public class JournalDocumentAndSettelment {

    private List<JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList;
    private List<GlBankTransactionDetailFromEntity> glBankTransactionDetailEntityList;

    /**
     * @return the journalDocumentArrangedReportBeanList
     */
    public List<JournalDocumentArrangedReportBean> getJournalDocumentArrangedReportBeanList() {
        if(journalDocumentArrangedReportBeanList == null){
            journalDocumentArrangedReportBeanList = new ArrayList<>();
        }
        return journalDocumentArrangedReportBeanList;
    }

    /**
     * @param journalDocumentArrangedReportBeanList the
     * journalDocumentArrangedReportBeanList to set
     */
    public void setJournalDocumentArrangedReportBeanList(List<JournalDocumentArrangedReportBean> journalDocumentArrangedReportBeanList) {
        this.journalDocumentArrangedReportBeanList = journalDocumentArrangedReportBeanList;
    }

    /**
     * @return the glBankTransactionDetailEntityList
     */
    public List<GlBankTransactionDetailFromEntity> getGlBankTransactionDetailEntityList() {
        if(glBankTransactionDetailEntityList == null){
            glBankTransactionDetailEntityList = new ArrayList<>();
        }
        return glBankTransactionDetailEntityList;
    }

    /**
     * @param glBankTransactionDetailEntityList the
     * glBankTransactionDetailEntityList to set
     */
    public void setGlBankTransactionDetailEntityList(List<GlBankTransactionDetailFromEntity> glBankTransactionDetailEntityList) {
        this.glBankTransactionDetailEntityList = glBankTransactionDetailEntityList;
    }

}
