/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAnalyticsAccountsServcie;
import com.isag.businessservice.reports.analyticalRatiosService;
import com.isag.entity.GlAnalyticsAccounts;
import com.isag.isag.BaseGlAccountReportBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hq002
 */
public class RateOfCirculationOfStorageMB extends BaseGlAccountReportBean {

    private GlAnalyticsAccounts glAnalyticsAccount;
    private BigDecimal valueOfCostSoldGoods = BigDecimal.ZERO;
    private BigDecimal valueOfStorageLastPeriod = BigDecimal.ZERO;
    private BigDecimal totalRatio = BigDecimal.ZERO;
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

    }

    public void load() {

        checkDateNull();
        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "COST_OF_SOLD_GOODS");
        isAccountSelected("COST_OF_SOLD_GOODS");
        if (accountIdExist) {
            valueOfCostSoldGoods = ratiosService.getSpecificRatio(dateFrom, dateTo, glAnalyticsAccount.getId());
        }

        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "STORAGE_OF_LAST_PERIOD");
        isAccountSelected("STORAGE_OF_LAST_PERIOD");
        if (accountIdExist) {
            valueOfStorageLastPeriod = ratiosService.getSpecificRatio(dateFrom, dateTo, glAnalyticsAccount.getId());
        }
        validateValues();
        totalRatio = valueOfCostSoldGoods.divide(valueOfStorageLastPeriod, 4, 4).setScale(2, BigDecimal.ROUND_UP);
    }

    private void validateValues() {
        if (valueOfCostSoldGoods != null) {
            if (valueOfCostSoldGoods.signum() == -1) {
                valueOfCostSoldGoods = valueOfCostSoldGoods.multiply(BigDecimal.valueOf(-1));
            } else if (valueOfCostSoldGoods.compareTo(BigDecimal.ZERO) == 0) {
                valueOfCostSoldGoods = BigDecimal.ONE;
            }
        } else {
            valueOfCostSoldGoods = BigDecimal.ONE;
        }

        if (valueOfStorageLastPeriod != null) {
            if (valueOfStorageLastPeriod.compareTo(BigDecimal.ZERO) == 0) {
                valueOfStorageLastPeriod = BigDecimal.ONE;
            } else if (valueOfStorageLastPeriod.signum() == -1) {
                valueOfStorageLastPeriod = valueOfStorageLastPeriod.multiply(BigDecimal.valueOf(-1));
            }
        } else if (valueOfCostSoldGoods.compareTo(BigDecimal.ONE) == 0 && valueOfStorageLastPeriod == null) {
            valueOfStorageLastPeriod = BigDecimal.ONE;
        }
    }

    @Override
    public void search() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void checkDateNull() {
        if (getDateFrom() == null) {
            setDateFrom(getGlYearSelection().getDateFrom());
        }
        if (getDateTo() == null) {
            setDateTo(getGlYearSelection().getDateTo());
        }
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(getDateFrom())) {
                resetDateFrom();
            } else {
                init();
            }
        } else {
            if (checkFinancailYear(getDateTo())) {
                resetDateTo();
            } else {
                init();
            }
        }
    }

    public void isAccountSelected(String accountName) {
        if (accountName.contains("COST_OF_SOLD_GOODS")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص حساب الى تكلفة البضاعة المباعة"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }
        } else if (accountName.contains("STORAGE_OF_LAST_PERIOD")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص خساب الى مخزون اخر مدة"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }
        }
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
     * @return the valueOfCostSoldGoods
     */
    public BigDecimal getValueOfCostSoldGoods() {
        return valueOfCostSoldGoods;
    }

    /**
     * @param valueOfCostSoldGoods the valueOfCostSoldGoods to set
     */
    public void setValueOfCostSoldGoods(BigDecimal valueOfCostSoldGoods) {
        this.valueOfCostSoldGoods = valueOfCostSoldGoods;
    }

    /**
     * @return the valueOfStorageLastPeriod
     */
    public BigDecimal getValueOfStorageLastPeriod() {
        return valueOfStorageLastPeriod;
    }

    /**
     * @param valueOfStorageLastPeriod the valueOfStorageLastPeriod to set
     */
    public void setValueOfStorageLastPeriod(BigDecimal valueOfStorageLastPeriod) {
        this.valueOfStorageLastPeriod = valueOfStorageLastPeriod;
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
