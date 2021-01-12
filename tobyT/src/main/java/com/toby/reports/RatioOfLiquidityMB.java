/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.analyticalRatiosService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAnalyticsAccountsServcie;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAnalyticsAccounts;
import com.toby.toby.BaseGlAccountReportBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hq002
 */
@Named("ratioOfLiquidityMB")
@ViewScoped
public class RatioOfLiquidityMB extends BaseGlAccountReportBean {

    private List<GlAccount> glAccountRootList;
    private List<GlAccount> glAccountFilteredRoot;
    private GlAccount testedGlAccount;
    private List<GlAnalyticsAccounts> GlAnalyticsAccountList;
    private GlAnalyticsAccounts glAnalyticsAccount;
    private List<Integer> allIds;
    private BigDecimal assetCurrentLastPeriodRatio = BigDecimal.ZERO;
    private BigDecimal totalOfCurrentDeduction = BigDecimal.ZERO;
    private BigDecimal totalRatio = BigDecimal.ZERO;
    private BigDecimal storageOfLastPeriod = BigDecimal.ZERO;
    private StringBuilder stringBuilder;
    private Integer flow = 0;
    private Date dateFrom;
    private Date dateTo;
    private boolean accountIdExist;
    @EJB
    GlAccountService glAccountService;
    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    private analyticalRatiosService ratiosService;

    @PostConstruct
    public void init() {
        reset();
        load();
    }

    @Override
    public void reset() {
        glAccountRootList = new ArrayList<>();
        glAccountFilteredRoot = new ArrayList<>();
        GlAnalyticsAccountList = new ArrayList<>();
        stringBuilder = new StringBuilder();
        allIds = new ArrayList<>();
        flow = 0;
    }

    public void load() {

        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "CURRENT_ASSETS");
        isAccountSelected("CURRENT_ASSETS");
        if (accountIdExist) {
            GlAccount firstAccountForCurrentAssets = glAccountService.getGlAccountByBranchAndGlAccountId(getUserData().getUserBranch().getId(), glAnalyticsAccount.getAccountId().getId());
            glAccountRootList.add(firstAccountForCurrentAssets);
        }

        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "CURRENT_DEDUCTIONS");
        isAccountSelected("CURRENT_DEDUCTIONS");
        if (accountIdExist) {
            GlAccount secondAccountForCurrentAssets = glAccountService.getGlAccountByBranchAndGlAccountId(getUserData().getUserBranch().getId(), glAnalyticsAccount.getAccountId().getId());
            glAccountRootList.add(secondAccountForCurrentAssets);
        }

        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "STORAGE_OF_LAST_PERIOD");
        isAccountSelected("STORAGE_OF_LAST_PERIOD");
        if (accountIdExist) {
            checkDateNull();
            storageOfLastPeriod = ratiosService.getSpecificRatio(dateFrom, dateTo, glAnalyticsAccount.getAccountId().getId());
        }
        for (GlAccount account : glAccountRootList) {
            prepareDataForReport(account);
            calculateLocalValueForGlAccount();
            stringBuilder = new StringBuilder();
            allIds = new ArrayList<>();
            flow++;
        }
        checkZeroes();
        totalRatio = assetCurrentLastPeriodRatio.divide(totalOfCurrentDeduction, 4, 4).setScale(2, BigDecimal.ROUND_UP);
    }

    private void prepareDataForReport(GlAccount ipo) {
        getChildTreeNodesForAccount(ipo);
    }

    private void getChildTreeNodesForAccount(GlAccount parentAcc) {

        if (parentAcc.getChildAccounts() != null && !parentAcc.getChildAccounts().isEmpty()) {
            for (GlAccount childAcc : parentAcc.getChildAccounts()) {
                if (childAcc.getType() != null && childAcc.getType() == 1) {
                    glAccountFilteredRoot.add(childAcc);
                    allIds.add(childAcc.getId());
                }
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    private void calculateLocalValueForGlAccount() {
        if (allIds.isEmpty()) {
            allIds.add(1);
        }
        for (Integer i : allIds) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append(i.toString());
            } else {
                stringBuilder.append("," + i.toString());
            }
        }
        if (flow == 0) {
            assetCurrentLastPeriodRatio = ratiosService.getCurrentRatio(dateFrom, dateTo, stringBuilder);
            if (assetCurrentLastPeriodRatio != null) {
                if (assetCurrentLastPeriodRatio.signum() == -1) {
                    assetCurrentLastPeriodRatio = assetCurrentLastPeriodRatio.multiply(BigDecimal.valueOf(-1));
                }
            } else {
                assetCurrentLastPeriodRatio = BigDecimal.ONE;
            }
        }
        if (flow == 1) {
            totalOfCurrentDeduction = ratiosService.getCurrentRatio(dateFrom, dateTo, stringBuilder);
            if (totalOfCurrentDeduction == null || totalOfCurrentDeduction.compareTo(BigDecimal.ZERO) == 0) {
                totalOfCurrentDeduction = BigDecimal.ONE;
            } else if (totalOfCurrentDeduction.signum() == -1) {
                totalOfCurrentDeduction = totalOfCurrentDeduction.multiply(BigDecimal.valueOf(-1));
            }
        }

        if (flow == 0 && assetCurrentLastPeriodRatio != null && assetCurrentLastPeriodRatio.signum() > 0 && storageOfLastPeriod != null && storageOfLastPeriod.signum() > 0) {
            assetCurrentLastPeriodRatio = assetCurrentLastPeriodRatio.subtract(storageOfLastPeriod);
        }
    }

    public void checkDateNull() {
        if (dateFrom == null) {
            dateFrom = getGlYearSelection().getDateFrom();
        }
        if (dateTo == null) {
            dateTo = getGlYearSelection().getDateTo();
        }
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(dateFrom)) {
                resetDateFrom();
            } else {
                init();
            }
        } else {
            if (checkFinancailYear(dateTo)) {
                resetDateTo();
            } else {
                init();
            }
        }
    }

    public void isAccountSelected(String accountName) {
        if (accountName.contains("CURRENT_ASSETS")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص حساب للاصول المتداولة"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }

        } else if (accountName.contains("CURRENT_DEDUCTIONS")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص حساب للخصوم المتداولة"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }

        } else if (accountName.contains("STORAGE_OF_LAST_PERIOD")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص حساب لمخزون اخر مدة"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }
        }
    }

    public void checkZeroes() {
        if (assetCurrentLastPeriodRatio.compareTo(BigDecimal.ZERO) == 0) {
            assetCurrentLastPeriodRatio = BigDecimal.ONE;
        }
        if (totalOfCurrentDeduction.compareTo(BigDecimal.ZERO) == 0) {
            totalOfCurrentDeduction = BigDecimal.ONE;
        }
    }

    /**
     * @return the flow
     */
    public Integer getFlow() {
        return flow;
    }

    /**
     * @param flow the flow to set
     */
    public void setFlow(Integer flow) {
        this.flow = flow;
    }

    @Override
    public void search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
    }

    @Override
    public void resetDateTo() {
        setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public HashMap prepareReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the glAccountRootList
     */
    public List<GlAccount> getGlAccountRootList() {
        return glAccountRootList;
    }

    /**
     * @param glAccountRootList the glAccountRootList to set
     */
    public void setGlAccountRootList(List<GlAccount> glAccountRootList) {
        this.glAccountRootList = glAccountRootList;
    }

    /**
     * @return the testedGlAccount
     */
    public GlAccount getTestedGlAccount() {
        return testedGlAccount;
    }

    /**
     * @param testedGlAccount the testedGlAccount to set
     */
    public void setTestedGlAccount(GlAccount testedGlAccount) {
        this.testedGlAccount = testedGlAccount;
    }

    /**
     * @return the glAccountFilteredRoot
     */
    public List<GlAccount> getGlAccountFilteredRoot() {
        return glAccountFilteredRoot;
    }

    /**
     * @param glAccountFilteredRoot the glAccountFilteredRoot to set
     */
    public void setGlAccountFilteredRoot(List<GlAccount> glAccountFilteredRoot) {
        this.glAccountFilteredRoot = glAccountFilteredRoot;
    }

    /**
     * @return the GlAnalyticsAccountList
     */
    public List<GlAnalyticsAccounts> getGlAnalyticsAccountList() {
        return GlAnalyticsAccountList;
    }

    /**
     * @param GlAnalyticsAccountList the GlAnalyticsAccountList to set
     */
    public void setGlAnalyticsAccountList(List<GlAnalyticsAccounts> GlAnalyticsAccountList) {
        this.GlAnalyticsAccountList = GlAnalyticsAccountList;
    }

    /**
     * @return the glAnalyticsAccount
     */
    public GlAnalyticsAccounts getGlAnalyticsAccount() {
        return glAnalyticsAccount;
    }

    /**
     * @param glAnalyticsAccount the glAnalyticsAccount to set
     */
    public void setGlAnalyticsAccount(GlAnalyticsAccounts glAnalyticsAccount) {
        this.glAnalyticsAccount = glAnalyticsAccount;
    }

    /**
     * @return the totalOfCurrentDeduction
     */
    public BigDecimal getTotalOfCurrentDeduction() {
        return totalOfCurrentDeduction;
    }

    /**
     * @param totalOfCurrentDeduction the totalOfCurrentDeduction to set
     */
    public void setTotalOfCurrentDeduction(BigDecimal totalOfCurrentDeduction) {
        this.totalOfCurrentDeduction = totalOfCurrentDeduction;
    }

    /**
     * @return the allIds
     */
    public List<Integer> getAllIds() {
        return allIds;
    }

    /**
     * @param allIds the allIds to set
     */
    public void setAllIds(List<Integer> allIds) {
        this.allIds = allIds;
    }

    /**
     * @return the stringBuilder
     */
    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    /**
     * @param stringBuilder the stringBuilder to set
     */
    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    /**
     * @return the totalRatio
     */
    public BigDecimal getTotalRatio() {
        return totalRatio;
    }

    /**
     * @param totalRatio the totalRatio to set
     */
    public void setTotalRatio(BigDecimal totalRatio) {
        this.totalRatio = totalRatio;
    }

    /**
     * @return the storageOfLastPeriod
     */
    public BigDecimal getStorageOfLastPeriod() {
        return storageOfLastPeriod;
    }

    /**
     * @param storageOfLastPeriod the storageOfLastPeriod to set
     */
    public void setStorageOfLastPeriod(BigDecimal storageOfLastPeriod) {
        this.storageOfLastPeriod = storageOfLastPeriod;
    }

    /**
     * @return the assetCurrentLastPeriodRatio
     */
    public BigDecimal getAssetCurrentLastPeriodRatio() {
        return assetCurrentLastPeriodRatio;
    }

    /**
     * @param assetCurrentLastPeriodRatio the assetCurrentLastPeriodRatio to set
     */
    public void setAssetCurrentLastPeriodRatio(BigDecimal assetCurrentLastPeriodRatio) {
        this.assetCurrentLastPeriodRatio = assetCurrentLastPeriodRatio;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the accountIdExist
     */
    public boolean isAccountIdExist() {
        return accountIdExist;
    }

    /**
     * @param accountIdExist the accountIdExist to set
     */
    public void setAccountIdExist(boolean accountIdExist) {
        this.accountIdExist = accountIdExist;
    }

}
