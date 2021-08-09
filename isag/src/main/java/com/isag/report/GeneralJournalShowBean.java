/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

/**
 *
 * @author hq002
 */
public class GeneralJournalShowBean {

    private Integer generalNumber;
    private String generalDate;
    private String generalStatement;
    private String generalType;
    private Integer generalDocument;
    private Integer serial;
    private boolean post_flag;
    private boolean markEdit;

    /**
     * @return the generalNumber
     */
    public Integer getGeneralNumber() {
        return generalNumber;
    }

    /**
     * @param generalNumber the generalNumber to set
     */
    public void setGeneralNumber(Integer generalNumber) {
        this.generalNumber = generalNumber;
    }

    /**
     * @return the generalDate
     */
    public String getGeneralDate() {
        return generalDate;
    }

    /**
     * @param generalDate the generalDate to set
     */
    public void setGeneralDate(String generalDate) {
        this.generalDate = generalDate;
    }

    /**
     * @return the generalStatement
     */
    public String getGeneralStatement() {
        return generalStatement;
    }

    /**
     * @param generalStatement the generalStatement to set
     */
    public void setGeneralStatement(String generalStatement) {
        this.generalStatement = generalStatement;
    }

    /**
     * @return the generalType
     */
    public String getGeneralType() {
        return generalType;
    }

    /**
     * @param generalType the generalType to set
     */
    public void setGeneralType(String generalType) {
        this.generalType = generalType;
    }

    /**
     * @return the generalDocument
     */
    public Integer getGeneralDocument() {
        return generalDocument;
    }

    /**
     * @param generalDocument the generalDocument to set
     */
    public void setGeneralDocument(Integer generalDocument) {
        this.generalDocument = generalDocument;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the post_flag
     */
    public boolean isPost_flag() {
        return post_flag;
    }

    /**
     * @param post_flag the post_flag to set
     */
    public void setPost_flag(boolean post_flag) {
        this.post_flag = post_flag;
    }

    /**
     * @return the markEdit
     */
    public boolean isMarkEdit() {
        return markEdit;
    }

    /**
     * @param markEdit the markEdit to set
     */
    public void setMarkEdit(boolean markEdit) {
        this.markEdit = markEdit;
    }
    
}
