/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.accountssystemsettings;

import com.toby.businessservice.AccountsSystemSettingsService;
import com.toby.entity.AccountsSystemSettings;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hq002
 */
public class AccountsSystemSettingsBean extends BaseFormBean {

    private UserData userData;
    private AccountsSystemSettings accountsSystemSettings;
    int x;

    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;

    @Override
    public String save() {
        try {
            Map<String, String> userDDs = userData.getUserDDs();
            if (accountsSystemSettings != null) {
                accountsSystemSettings.setCreatedBy(userData.getUser());
                accountsSystemSettings.setCreationDate(new Date());
                accountsSystemSettings.setCompanyId(userData.getCompany());
                try {
                    accountsSystemSettingsService.addInventorySetup(accountsSystemSettings);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("ERROR"), userDDs.get("UNIQE_KEY_ERROR")));
                }

            }
            return "";
        } catch (Exception ex) {
            saveError(ex, "AccountsSystemSettingsBean", "save");
            return null;
        }
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            load();
        } catch (Exception ex) {
            saveError(ex, "AccountsSystemSettingsBean", "init");
        }
    }

    @Override
    public void load() {
        try {
            accountsSystemSettings = new AccountsSystemSettings();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            if (userData.getIsAdmin()) {
                accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(62);
            } else {
                accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(userData.getCompany().getId());
            }

            if (accountsSystemSettings == null) {
                accountsSystemSettings = new AccountsSystemSettings();
                accountsSystemSettings.setAccountStatementAppearance("APPEARANCE_BEFOR_REVISION");
                accountsSystemSettings.setJournalsDailyBalanced("NO_WEIGHT_JOURNAL");
                accountsSystemSettings.setJournalsEdit("ALL_USERS");
                accountsSystemSettings.setJournalsSerial("SEQUENCE_GENERAL");
                accountsSystemSettings.setWorkWithAccounts("ALLOW_ACCOUNT");
                accountsSystemSettings.setAccountPrinting("HORIZONTAL");
            }
        } catch (Exception ex) {
            saveError(ex, "AccountsSystemSettingsBean", "load");
        }
    }

    public void cancel() {
        try {
            accountsSystemSettings = new AccountsSystemSettings();
        } catch (Exception ex) {
            saveError(ex, "AccountsSystemSettingsBean", "cancel");
        }
    }

    @Override
    public String getScreenName() {
        return "accountssystemsettings";
    }

    public AccountsSystemSettings getAccountsSystemSettings() {
        return accountsSystemSettings;
    }

    public void setAccountsSystemSettings(AccountsSystemSettings accountsSystemSettings) {
        this.accountsSystemSettings = accountsSystemSettings;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}
