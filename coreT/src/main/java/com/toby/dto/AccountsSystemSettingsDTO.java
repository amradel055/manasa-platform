/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

/**
 *
 * @author AhmedEssam
 */
public class AccountsSystemSettingsDTO extends BaseEntityDTO{
    
    
    private String journalsSerial;
    private String journalsDailyBalanced;
    private String journalsEdit;
    private String accountStatementAppearance;
    private String AllowSubsequentCacheAdministration;
    private String workWithAccounts;
    private String noteSreceivablesType;
    private String accountPrinting;

    public String getJournalsSerial() {
        return journalsSerial;
    }

    public void setJournalsSerial(String journalsSerial) {
        this.journalsSerial = journalsSerial;
    }
    
    public String getJournalsEdit() {
        return journalsEdit;
    }

    public void setJournalsEdit(String journalsEdit) {
        this.journalsEdit = journalsEdit;
    }

    public String getAccountStatementAppearance() {
        return accountStatementAppearance;
    }

    public void setAccountStatementAppearance(String accountStatementAppearance) {
        this.accountStatementAppearance = accountStatementAppearance;
    }

    public String getJournalsDailyBalanced() {
        return journalsDailyBalanced;
    }

    public void setJournalsDailyBalanced(String journalsDailyBalanced) {
        this.journalsDailyBalanced = journalsDailyBalanced;
    }    
    
    /**
     * @return the AllowSubsequentCacheAdministration
     */
    public String getAllowSubsequentCacheAdministration() {
        return AllowSubsequentCacheAdministration;
    }

    /**
     * @param AllowSubsequentCacheAdministration the AllowSubsequentCacheAdministration to set
     */
    public void setAllowSubsequentCacheAdministration(String AllowSubsequentCacheAdministration) {
        this.AllowSubsequentCacheAdministration = AllowSubsequentCacheAdministration;
    }

    /**
     * @return the workWithAccounts
     */
    public String getWorkWithAccounts() {
        return workWithAccounts;
    }

    /**
     * @param workWithAccounts the workWithAccounts to set
     */
    public void setWorkWithAccounts(String workWithAccounts) {
        this.workWithAccounts = workWithAccounts;
    }

    /**
     * @return the noteSreceivablesType
     */
    public String getNoteSreceivablesType() {
        return noteSreceivablesType;
    }

    /**
     * @param noteSreceivablesType the noteSreceivablesType to set
     */
    public void setNoteSreceivablesType(String noteSreceivablesType) {
        this.noteSreceivablesType = noteSreceivablesType;
    }

    /**
     * @return the accountPrinting
     */
    public String getAccountPrinting() {
        return accountPrinting;
    }

    /**
     * @param accountPrinting the accountPrinting to set
     */
    public void setAccountPrinting(String accountPrinting) {
        this.accountPrinting = accountPrinting;
    }
}
