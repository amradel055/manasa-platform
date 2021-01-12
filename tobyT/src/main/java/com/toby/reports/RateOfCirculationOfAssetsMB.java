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
@Named("rateOfCirculationOfAssetsMB")
@ViewScoped
public class RateOfCirculationOfAssetsMB extends BaseGlAccountReportBean {

    private List<GlAccount> glAccountRootList;
    private List<GlAccount> glAccountFilteredRoot;
    private GlAccount testedGlAccount;
    private List<GlAnalyticsAccounts> GlAnalyticsAccountList;
    private GlAnalyticsAccounts glAnalyticsAccount;
    private List<Integer> allIds;
    private BigDecimal totalOfCurrentAsset = BigDecimal.ZERO;
    private BigDecimal totalOfCurrentDeduction = BigDecimal.ZERO;
    private BigDecimal totalRatio = BigDecimal.ZERO;
    private StringBuilder stringBuilder;
    private Integer flow = 0;
    private Date dateFrom;
    private Date dateTo;
    private boolean accountIdExist;
    private BigDecimal valueOfSales = BigDecimal.ZERO;
    private BigDecimal valueOfReturns = BigDecimal.ZERO;
    private BigDecimal valueOfNetSales = BigDecimal.ZERO;

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
    }

    public void load() {

        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "CURRENT_ASSETS");
        isAccountSelected("CURRENT_ASSETS");
        if (accountIdExist) {
            GlAccount firstAccountForCurrentAssets = glAccountService.getGlAccountByBranchAndGlAccountId(getUserData().getUserBranch().getId(), glAnalyticsAccount.getAccountId().getId());
            glAccountRootList.add(firstAccountForCurrentAssets);
        }

        if (glAccountRootList != null && !glAccountRootList.isEmpty()) {
            prepareDataForReport(glAccountRootList.get(0));
        }
        calculateLocalValueForGlAccount();
        stringBuilder = new StringBuilder();
        allIds = new ArrayList<>();

        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "SALES");
        isAccountSelected("SALES");
        if (accountIdExist) {
            valueOfSales = ratiosService.getSpecificRatio(dateFrom, dateTo, glAnalyticsAccount.getId());
        }

        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "RETURNS");
        isAccountSelected("RETURNS");
        if (accountIdExist) {
            valueOfReturns = ratiosService.getSpecificRatio(dateFrom, dateTo, glAnalyticsAccount.getId());
        }
        validateValues();
        calculateNetSales();

        totalRatio = valueOfNetSales.divide(totalOfCurrentAsset, 4, 4).setScale(2, BigDecimal.ROUND_UP);
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
        checkDateNull();
        totalOfCurrentAsset = ratiosService.getCurrentRatio(dateFrom, dateTo, stringBuilder);
        if (totalOfCurrentAsset != null) {
            if (totalOfCurrentAsset.signum() == -1) {
                totalOfCurrentAsset = totalOfCurrentAsset.multiply(BigDecimal.valueOf(-1));
            } else if (totalOfCurrentAsset.compareTo(BigDecimal.ZERO) == 0) {
                totalOfCurrentAsset = BigDecimal.ONE;
            }
        } else {
            totalOfCurrentAsset = BigDecimal.ONE;
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

        } else if (accountName.contains("SALES")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص حساب للمبيعات"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }
        } else if (accountName.contains("RETURNS")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص حساب للمردودات"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }
        }
    }

    private void validateValues() {

        if (valueOfSales != null) {
            if (valueOfSales.compareTo(BigDecimal.ZERO) == 0) {
                valueOfSales = valueOfSales.add(BigDecimal.ONE);
            } else if (valueOfSales.signum() == -1) {
                valueOfSales = valueOfSales.multiply(BigDecimal.valueOf(-1));
            }
        } else {
            valueOfSales = BigDecimal.ONE;
        }

        if (valueOfReturns != null) {
            if (valueOfReturns.compareTo(BigDecimal.ZERO) == 0) {
                valueOfReturns = valueOfReturns.add(BigDecimal.ONE);
            } else if (valueOfReturns.signum() == -1) {
                valueOfReturns = valueOfReturns.multiply(BigDecimal.valueOf(-1));
            }
        } else {
            valueOfReturns = BigDecimal.ONE;
        }
    }

    public void calculateNetSales() {
        valueOfNetSales = valueOfSales.subtract(valueOfReturns);
        if (valueOfNetSales != null) {
            if (valueOfNetSales.compareTo(BigDecimal.ZERO) == 0) {
                valueOfNetSales = valueOfNetSales.add(BigDecimal.ONE);
            } else if (valueOfNetSales.signum() == -1) {
                valueOfNetSales = valueOfNetSales.multiply(BigDecimal.valueOf(-1));
            }
        } else {
            valueOfNetSales = BigDecimal.ONE;
        }
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
     * @return the totalOfCurrentAsset
     */
    public BigDecimal getTotalOfCurrentAsset() {
        return totalOfCurrentAsset;
    }

    /**
     * @param totalOfCurrentAsset the totalOfCurrentAsset to set
     */
    public void setTotalOfCurrentAsset(BigDecimal totalOfCurrentAsset) {
        this.totalOfCurrentAsset = totalOfCurrentAsset;
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

    /**
     * @return the valueOfSales
     */
    public BigDecimal getValueOfSales() {
        return valueOfSales;
    }

    /**
     * @param valueOfSales the valueOfSales to set
     */
    public void setValueOfSales(BigDecimal valueOfSales) {
        this.valueOfSales = valueOfSales;
    }

    /**
     * @return the valueOfReturns
     */
    public BigDecimal getValueOfReturns() {
        return valueOfReturns;
    }

    /**
     * @param valueOfReturns the valueOfReturns to set
     */
    public void setValueOfReturns(BigDecimal valueOfReturns) {
        this.valueOfReturns = valueOfReturns;
    }

    /**
     * @return the valueOfNetSales
     */
    public BigDecimal getValueOfNetSales() {
        return valueOfNetSales;
    }

    /**
     * @param valueOfNetSales the valueOfNetSales to set
     */
    public void setValueOfNetSales(BigDecimal valueOfNetSales) {
        this.valueOfNetSales = valueOfNetSales;
    }

}
