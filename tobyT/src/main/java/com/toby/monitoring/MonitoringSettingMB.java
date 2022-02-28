/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.monitoring;

import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAnalyticsAccountsServcie;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAnalyticsAccounts;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ahmed
 */
@Named(value = "monitoringSettingMB")
@ViewScoped
public class MonitoringSettingMB extends BaseListBean {

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    private UserData userData;
    private GlAnalyticsAccounts glAnalyticsAccounts;
    private List<GlAccount> glAccountList;
    private GlaccountConverter glaccountConverter;
    private GlAccount glAccountSlected;
    private List<GlAnalyticsAccounts> glAnalyticsList;
    private List<GlAnalyticsAccounts> glAnalyticsToSaveList;
    private String uri;

    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    GlAccountService accountService;

    @Override
    @PostConstruct
    public void init() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
            setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());

            reset();
            load();
        } catch (Exception e) {
            saveError(e, "MonitoringSettingMB", "init");
        }
    }

    public void reset() {
        try {
            glAccountList = new ArrayList<>();
            glAnalyticsList = new ArrayList<>();
            glAnalyticsToSaveList = new ArrayList<>();
        } catch (Exception e) {
            saveError(e, "MonitoringSettingMB", "reset");
        }
    }

    @Override
    public void load() {
        try {
            glAnalyticsList = new ArrayList<>();
            if (uri.contains("monitoringSetting")) {
                glAnalyticsList = glAnalyticsAccountsServcie.getALLGlAnalyticsAccountsForMonitoringByBranch(userData.getUserBranch().getId(), 1);
                glAccountList = accountService.getBranchGLAccountsActive(userData.getUserBranch().getId());
            } else {
                glAnalyticsList = glAnalyticsAccountsServcie.getALLGlAnalyticsAccountsForMonitoringByBranch(userData.getUserBranch().getId(), 2);
                glAccountList = accountService.getBranchGLAccountsActiveWithException(userData.getUserBranch().getId());
            }
//            glAccountList = accountService.getBranchGLAccountsActiveByBranch(userData.getUserBranch().getId());
//            glAccountList = accountService.findAllMainGlAccountsByBranch(userData.getUserBranch().getId());

            setGlaccountConverter(new GlaccountConverter(glAccountList));
        } catch (Exception e) {
            saveError(e, "MonitoringSettingMB", "load");
        }
    }

    public void selectAccountNumber(SelectEvent event) {
        try {
            glAccountSlected = (GlAccount) event.getObject();
            GlAnalyticsAccounts analyticsAccounts = (GlAnalyticsAccounts) event.getComponent().getAttributes().get("varValue");
            analyticsAccounts.setAccountId(glAccountSlected);
            glAnalyticsToSaveList.add(analyticsAccounts);
        } catch (Exception e) {
            saveError(e, "MonitoringSettingMB", "selectAccountNumber");
            e.printStackTrace();
        }
    }

    public void saveAll() {
        try {
            Map<String, String> userDDs = getUserData().getUserDDs();

            if (glAnalyticsToSaveList != null && !glAnalyticsToSaveList.isEmpty()) {
                try {
                    glAnalyticsAccountsServcie.saveGlAnalyticsAccounts(glAnalyticsToSaveList);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, userDDs.get("INFO"), userDDs.get("SAVED")));
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), userDDs.get("SAVED_ERROR")));
                }
            }
        } catch (Exception e) {
            saveError(e, "MonitoringSettingMB", "load");
        }
    }

    public List<GlAccount> completeGlAccount(String query) {
        try {
            List<GlAccount> glaccounts = glAccountList;
            if (query == null || query.trim().equals("")) {
                glaccountConverter = new GlaccountConverter(glaccounts);
                return glaccounts;
            }
            List<GlAccount> filteredGlaccounts = new ArrayList<>();

            String nameAr;
            Integer code;
            GlAccount glaccount;
            for (int i = 0; i < glAccountList.size(); i++) {
                glaccount = glaccounts.get(i);
                nameAr = glaccount.getName();
                if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }

                code = glaccount.getAccNumber();
                if (code != null && String.valueOf(code).contains(query)) {
                    if (!filteredGlaccounts.contains(glaccount)) {
                        filteredGlaccounts.add(glaccount);
                    }
                }
            }

            glaccountConverter = new GlaccountConverter(filteredGlaccounts);
            return filteredGlaccounts;
        } catch (Exception e) {
            saveError(e, "MonitoringSettingMB", "completeGlAccount");
            return null;
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the glAnalyticsAccounts
     */
    public GlAnalyticsAccounts getGlAnalyticsAccounts() {
        return glAnalyticsAccounts;
    }

    /**
     * @param glAnalyticsAccounts the glAnalyticsAccounts to set
     */
    public void setGlAnalyticsAccounts(GlAnalyticsAccounts glAnalyticsAccounts) {
        this.glAnalyticsAccounts = glAnalyticsAccounts;
    }

    /**
     * @return the glAccountList
     */
    public List<GlAccount> getGlAccountList() {
        return glAccountList;
    }

    /**
     * @param glAccountList the glAccountList to set
     */
    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    /**
     * @return the glaccountConverter
     */
    public GlaccountConverter getGlaccountConverter() {
        return glaccountConverter;
    }

    /**
     * @param glaccountConverter the glaccountConverter to set
     */
    public void setGlaccountConverter(GlaccountConverter glaccountConverter) {
        this.glaccountConverter = glaccountConverter;
    }

    /**
     * @return the glAccountSlected
     */
    public GlAccount getGlAccountSlected() {
        return glAccountSlected;
    }

    /**
     * @param glAccountSlected the glAccountSlected to set
     */
    public void setGlAccountSlected(GlAccount glAccountSlected) {
        this.glAccountSlected = glAccountSlected;
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
     * @return the glAnalyticsToSaveList
     */
    public List<GlAnalyticsAccounts> getGlAnalyticsToSaveList() {
        return glAnalyticsToSaveList;
    }

    /**
     * @param glAnalyticsToSaveList the glAnalyticsToSaveList to set
     */
    public void setGlAnalyticsToSaveList(List<GlAnalyticsAccounts> glAnalyticsToSaveList) {
        this.glAnalyticsToSaveList = glAnalyticsToSaveList;
    }

}
