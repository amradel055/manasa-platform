/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.monitoring;

import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAnalyticsAccountsServcie;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAnalyticsAccounts;
import com.toby.report.entity.MonitoringBalancesBean;
import com.toby.reports.MainTransactionStatementMB;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ahmed
 */
@Named(value = "monitoringFormMB")
@ViewScoped
public class MonitoringFormMB extends MainTransactionStatementMB {

    private List<GlAnalyticsAccounts> glAnalyticsList;
    private List<GlAccount> glAccountsSelectedFromSettings;
    private List<GlAccount> glAccountRootsList;

    private Map<Integer, GlAccount> GlAccountMap = new HashMap<>();
    private UserData userData;
    private MonitoringBalancesBean monitoringBalancesBeanSelected;
    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    GlAccountService accountService;

    @PostConstruct
    public void initial() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            super.init();
            resetLists();
            loadingData();
        } catch (Exception e) {
            saveError(e, "MonitoringFormMB", "initial");
        }

    }

    public void resetLists() {
        try {
            glAnalyticsList = new ArrayList<>();
            glAccountsSelectedFromSettings = new ArrayList<>();
            glAccountRootsList = new ArrayList<>();
            setZeroes(true);
        } catch (Exception e) {
            saveError(e, "MonitoringFormMB", "resetLists");
        }
    }

    public void loadingData() {
        try {
            glAnalyticsList = glAnalyticsAccountsServcie.getALLGlAnalyticsAccountsForMonitoringByBranch(getUserData().getUserBranch().getId(), 1);
            glAccountRootsList = accountService.findAllMainGlAccountsByBranch(getUserData().getUserBranch().getId());
            if (glAccountRootsList != null && !glAccountRootsList.isEmpty()) {
                for (GlAccount acc : glAccountRootsList) {
                    GlAccountMap.put(acc.getId(), acc);
                }

            }
            if (glAccountRootsList != null && !glAccountRootsList.isEmpty()) {
                for (GlAnalyticsAccounts glana : glAnalyticsList) {
                    if (getGlAccountMap() != null && glana.getAccountId() != null && getGlAccountMap().containsKey(glana.getAccountId().getId())) {
                        glAccountsSelectedFromSettings.add(getGlAccountMap().get(glana.getAccountId().getId()));
                    }
                }
            }
            if (glAccountsSelectedFromSettings != null && !glAccountsSelectedFromSettings.isEmpty()) {
                for (GlAccount glana : glAccountsSelectedFromSettings) {
                    super.setGlAccountSelected(glana);
                    search();
                }
            }
            super.getMonitoringBalancesBeanList();
        } catch (Exception e) {
            saveError(e, "MonitoringFormMB", "loadingData");
        }
    }

    /**
     * @return the glAnalyticsList
     */
    public List<GlAnalyticsAccounts> getGlAnalyticsList() {
        return glAnalyticsList;
    }

    /**
     * @param glAnalyticsList the glAnalyticsList to set
     */
    public void setGlAnalyticsList(List<GlAnalyticsAccounts> glAnalyticsList) {
        this.glAnalyticsList = glAnalyticsList;
    }

    /**
     * @return the glAccountsSelectedFromSettings
     */
    public List<GlAccount> getGlAccountsSelectedFromSettings() {
        return glAccountsSelectedFromSettings;
    }

    /**
     * @param glAccountsSelectedFromSettings the glAccountsSelectedFromSettings
     * to set
     */
    public void setGlAccountsSelectedFromSettings(List<GlAccount> glAccountsSelectedFromSettings) {
        this.glAccountsSelectedFromSettings = glAccountsSelectedFromSettings;
    }

    /**
     * @return the glAccountRootsList
     */
    public List<GlAccount> getGlAccountRootsList() {
        return glAccountRootsList;
    }

    /**
     * @param glAccountRootsList the glAccountRootsList to set
     */
    public void setGlAccountRootsList(List<GlAccount> glAccountRootsList) {
        this.glAccountRootsList = glAccountRootsList;
    }

    /**
     * @return the GlAccountMap
     */
    public Map<Integer, GlAccount> getGlAccountMap() {
        return GlAccountMap;
    }

    /**
     * @param GlAccountMap the GlAccountMap to set
     */
    public void setGlAccountMap(Map<Integer, GlAccount> GlAccountMap) {
        this.GlAccountMap = GlAccountMap;
    }

    /**
     * @return the monitoringBalancesBeanSelected
     */
    public MonitoringBalancesBean getMonitoringBalancesBeanSelected() {
        return monitoringBalancesBeanSelected;
    }

    /**
     * @param monitoringBalancesBeanSelected the monitoringBalancesBeanSelected
     * to set
     */
    public void setMonitoringBalancesBeanSelected(MonitoringBalancesBean monitoringBalancesBeanSelected) {
        this.monitoringBalancesBeanSelected = monitoringBalancesBeanSelected;
    }
}
