/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.TobyUserYearService;
import com.toby.businessservice.SymbolService;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.converter.GlYearConverter;
import com.toby.entity.CostCenter;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlYear;
import com.toby.entity.Symbol;
//import com.toby.report.DeletedAndUpdatedGeneralJournalMB;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.ListUtils;

/**
 *
 * @author hq002
 */
public abstract class BaseGlAccountReportBean extends BaseReportBean {

    private List<GlAccount> glAccountList;

    private List<GlAdminUnit> glAdminUnitList;

    private List<CostCenter> costCenterList;

    private List<GlYear> glYearList;

    private GlYear glYearSelection;

    private List<Symbol> symbolList;

    private GlYearConverter glYearConverter;

    private Map<Integer, BigDecimal> totalBalanceMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalDebitMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalCreditMap = new HashMap<>();

    private List<GeneralJournalDetails> generalJournalDetailsList;
    private List<GeneralJournalDetails> generalJournalDetailsFirstList;
    private List<GeneralJournalDetails> generalJournalDetailsSecondList;

    private List<GeneralJournalDetails> generalJournalDetailsAList;
    private List<GeneralJournalDetails> generalJournalDetailsBList;
    private List<GeneralJournalDetails> generalJournalDetailsCList;
    private BigDecimal netProfit;
    private CommonSearchBean commonSearchBean;
    private Symbol openingBalanceId;
    @EJB
    GlAccountService accountService;
    @EJB
    CostCenterService costCenterService;
    @EJB
    GlAdminUnitService adminUnitService;
    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;
    @EJB
    SymbolService symbolService;
    @EJB
    private TobyUserYearService tobyUserYearService;

    public abstract void checkDate(Boolean dateType);

    public abstract void resetDateFrom();

    public abstract void resetDateTo();

    public void resetFinancailYearDate() {
        resetDateFrom();
        resetDateTo();
    }

    public BaseGlAccountReportBean() {

    }

    @Override
    public void load() {
        
        
    }

    protected Integer getSelectedBranch() {
        return getUserData().getSelectedBranch();
    }

    public void pushGlYearSelection() {
        resetFinancailYearDate();
        getUserData().setGlYear(glYearSelection);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("userLogInData", getUserData());
    }

    public void redirectFinancailYearPage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect("../base/financailyear.xhtml");
        } catch (IOException ex) {
//            Logger.getLogger(DeletedAndUpdatedGeneralJournalMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean checkFinancailYear(Date date) {
        if ((date.before(getGlYearSelection().getDateFrom()) || date.after(getGlYearSelection().getDateTo()))) {
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار تاريخ يقع بين الفترة الماليه"));
            return true;
        }
        return false;
    }

    public void prepareOpeneingBlancesValues(CommonSearchBean commonSearchBean) {

        generalJournalDetailsFirstList = new ArrayList<>();
        generalJournalDetailsSecondList = new ArrayList<>();
        totalBalanceMap = new HashMap<>();
        generalJournalDetailsList = new ArrayList<>();
        symbolList = new ArrayList<>();

        symbolList = symbolService.getTheOpeningBalanceId(getUserData().getCompany().getId());
        openingBalanceId = new Symbol();
        if (symbolList != null && !symbolList.isEmpty()) {
            openingBalanceId = symbolList.get(0);
        }
        if (openingBalanceId != null) {
            generalJournalDetailsFirstList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountToGetOpeningBalnce(getUserData().getUserBranch().getId(), 1, openingBalanceId.getId(), commonSearchBean, getGlYearSelection());
            generalJournalDetailsSecondList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountToGetOpeningBalnce(getUserData().getUserBranch().getId(), 2, openingBalanceId.getId(), commonSearchBean, getGlYearSelection());
            generalJournalDetailsList = ListUtils.union(generalJournalDetailsFirstList, generalJournalDetailsSecondList);
        }
        for (GeneralJournalDetails journalDetails : generalJournalDetailsList) {
            totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
        }
    }

    public void getSumOfDetails(CommonSearchBean commonSearchBean) {
        generalJournalDetailsFirstList = new ArrayList<>();
        generalJournalDetailsSecondList = new ArrayList<>();
        totalBalanceMap = new HashMap<>();
        generalJournalDetailsList = new ArrayList<>();
        generalJournalDetailsFirstList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(getUserData().getUserBranch().getId(), 1, commonSearchBean, getGlYearSelection());
        generalJournalDetailsSecondList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(getUserData().getUserBranch().getId(), 2, commonSearchBean, getGlYearSelection());
        generalJournalDetailsList = ListUtils.union(generalJournalDetailsFirstList, generalJournalDetailsSecondList);
        if (generalJournalDetailsList != null && !generalJournalDetailsList.isEmpty()) {
            for (GeneralJournalDetails journalDetails : generalJournalDetailsList) {
                totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
            }
        }
    }

    public void getSumOfDetailsForCreditAndDebitAndBalance(CommonSearchBean commonSearchBean) {
        generalJournalDetailsFirstList = new ArrayList<>();
        generalJournalDetailsSecondList = new ArrayList<>();
        totalBalanceMap = new HashMap<>();
        totalDebitMap = new HashMap<>();
        totalCreditMap = new HashMap<>();

        generalJournalDetailsList = new ArrayList<>();
        generalJournalDetailsFirstList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditAndSumOfDebitAndSumOfCreditForAllGlAccount(getUserData().getUserBranch().getId(), 1, commonSearchBean);
        generalJournalDetailsSecondList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditAndSumOfDebitAndSumOfCreditForAllGlAccount(getUserData().getUserBranch().getId(), 2, commonSearchBean);
        generalJournalDetailsList = ListUtils.union(generalJournalDetailsFirstList, generalJournalDetailsSecondList);
        if (generalJournalDetailsList != null && !generalJournalDetailsList.isEmpty()) {
            for (GeneralJournalDetails journalDetails : generalJournalDetailsList) {
                totalDebitMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
                totalCreditMap.put(journalDetails.getSerial(), journalDetails.getCreditamount());
                totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmountLocal());
            }
        }
    }

    public void findTheNetProfit() {
        generalJournalDetailsAList = new ArrayList<>();
        generalJournalDetailsBList = new ArrayList<>();
        generalJournalDetailsCList = new ArrayList<>();
        commonSearchBean = new CommonSearchBean();
        generalJournalDetailsAList = generaljournalDetailsService.getTheNetProfitForIncomeAndExpensesGlAccounts(getUserData().getUserBranch().getId(), 1, commonSearchBean, getGlYearSelection());
        generalJournalDetailsBList = generaljournalDetailsService.getTheNetProfitForIncomeAndExpensesGlAccounts(getUserData().getUserBranch().getId(), 2, commonSearchBean, getGlYearSelection());
        generalJournalDetailsCList = ListUtils.union(generalJournalDetailsAList, generalJournalDetailsBList);
        netProfit = BigDecimal.ZERO;
        for (GeneralJournalDetails journalDetails : generalJournalDetailsCList) {
            if (getTotalBalanceMap().containsKey(journalDetails.getSerial())) {
                journalDetails.getDebitAmount().add(getTotalBalanceMap().get(journalDetails.getSerial()) != null ? getTotalBalanceMap().get(journalDetails.getSerial()) : null);
            }
            if (journalDetails.getDiscribtion().contains("INCOME")) {
                netProfit = netProfit.add(journalDetails.getDebitAmount());
            } else {
                netProfit = netProfit.subtract(journalDetails.getDebitAmount());
            }
        }
    }

    /**
     * @return the glAccountList
     */
    public List<GlAccount> getGlAccountList() {
        if (glAccountList == null || glAccountList.isEmpty()) {
            glAccountList = accountService.getBranchGLAccountsActive(getSelectedBranch());
        }
        return glAccountList;
    }

    /**
     * @param glAccountList the glAccountList to set
     */
    public void setGlAccountList(List<GlAccount> glAccountList) {
        this.glAccountList = glAccountList;
    }

    /**
     * @return the glAdminUnitList
     */
    public List<GlAdminUnit> getGlAdminUnitList() {
        if (glAdminUnitList == null || glAdminUnitList.isEmpty()) {
            glAdminUnitList = adminUnitService.getAllSubAdminUnitByBranchIdActive(getUserData().getUserBranch().getId());
        }
        return glAdminUnitList;
    }

    /**
     * @param glAdminUnitList the glAdminUnitList to set
     */
    public void setGlAdminUnitList(List<GlAdminUnit> glAdminUnitList) {
        this.glAdminUnitList = glAdminUnitList;
    }

    /**
     * @return the costCenterList
     */
    public List<CostCenter> getCostCenterList() {
        if (costCenterList == null || costCenterList.isEmpty()) {
            costCenterList = costCenterService.getAllSubCostCenterByBranchIdActive(getUserData().getUserBranch().getId());
        }
        return costCenterList;
    }

    /**
     * @param costCenterList the costCenterList to set
     */
    public void setCostCenterList(List<CostCenter> costCenterList) {
        this.costCenterList = costCenterList;
    }

    /**
     * @return the glYearList
     */
    public List<GlYear> getGlYearList() {
        if (glYearList == null || glYearList.isEmpty()) {
            glYearList = tobyUserYearService.findYearByUserId(getUserData().getUser().getId(), getUserData().getUserBranch());
            glYearConverter = new GlYearConverter(glYearList);
        }
        return glYearList;
    }

    /**
     * @param glYearList the glYearList to set
     */
    public void setGlYearList(List<GlYear> glYearList) {
        this.glYearList = glYearList;
    }

    /**
     * @return the glYearSelection
     */
    public GlYear getGlYearSelection() {
        if (glYearSelection == null) {
            if (getUserData().getGlYear() != null) {
                glYearSelection = getUserData().getGlYear();
            } else {
                if (getGlYearList() != null && !getGlYearList().isEmpty()) {
                    glYearSelection = getGlYearList().get(0);
                }
            }
        }
        return glYearSelection;
    }

    /**
     * @param glYearSelection the glYearSelection to set
     */
    public void setGlYearSelection(GlYear glYearSelection) {
        this.glYearSelection = glYearSelection;
    }

    /**
     * @return the glYearConverter
     */
    public GlYearConverter getGlYearConverter() {
        return glYearConverter;
    }

    /**
     * @param glYearConverter the glYearConverter to set
     */
    public void setGlYearConverter(GlYearConverter glYearConverter) {
        this.glYearConverter = glYearConverter;
    }

    /**
     * @return the totalBalanceMap
     */
    public Map<Integer, BigDecimal> getTotalBalanceMap() {
        return totalBalanceMap;
    }

    /**
     * @param totalBalanceMap the totalBalanceMap to set
     */
    public void setTotalBalanceMap(Map<Integer, BigDecimal> totalBalanceMap) {
        this.totalBalanceMap = totalBalanceMap;
    }

    /**
     * @return the generalJournalDetailsList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsList() {
        return generalJournalDetailsList;
    }

    /**
     * @param generalJournalDetailsList the generalJournalDetailsList to set
     */
    public void setGeneralJournalDetailsList(List<GeneralJournalDetails> generalJournalDetailsList) {
        this.generalJournalDetailsList = generalJournalDetailsList;
    }

    /**
     * @return the generalJournalDetailsFirstList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsFirstList() {
        return generalJournalDetailsFirstList;
    }

    /**
     * @param generalJournalDetailsFirstList the generalJournalDetailsFirstList
     * to set
     */
    public void setGeneralJournalDetailsFirstList(List<GeneralJournalDetails> generalJournalDetailsFirstList) {
        this.generalJournalDetailsFirstList = generalJournalDetailsFirstList;
    }

    /**
     * @return the generalJournalDetailsSecondList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsSecondList() {
        return generalJournalDetailsSecondList;
    }

    /**
     * @param generalJournalDetailsSecondList the
     * generalJournalDetailsSecondList to set
     */
    public void setGeneralJournalDetailsSecondList(List<GeneralJournalDetails> generalJournalDetailsSecondList) {
        this.generalJournalDetailsSecondList = generalJournalDetailsSecondList;
    }

    /**
     * @return the totalDebitMap
     */
    public Map<Integer, BigDecimal> getTotalDebitMap() {
        return totalDebitMap;
    }

    /**
     * @param totalDebitMap the totalDebitMap to set
     */
    public void setTotalDebitMap(Map<Integer, BigDecimal> totalDebitMap) {
        this.totalDebitMap = totalDebitMap;
    }

    /**
     * @return the totalCreditMap
     */
    public Map<Integer, BigDecimal> getTotalCreditMap() {
        return totalCreditMap;
    }

    /**
     * @param totalCreditMap the totalCreditMap to set
     */
    public void setTotalCreditMap(Map<Integer, BigDecimal> totalCreditMap) {
        this.totalCreditMap = totalCreditMap;
    }

    /**
     * @return the generalJournalDetailsAList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsAList() {
        return generalJournalDetailsAList;
    }

    /**
     * @param generalJournalDetailsAList the generalJournalDetailsAList to set
     */
    public void setGeneralJournalDetailsAList(List<GeneralJournalDetails> generalJournalDetailsAList) {
        this.generalJournalDetailsAList = generalJournalDetailsAList;
    }

    /**
     * @return the generalJournalDetailsCList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsCList() {
        return generalJournalDetailsCList;
    }

    /**
     * @param generalJournalDetailsCList the generalJournalDetailsCList to set
     */
    public void setGeneralJournalDetailsCList(List<GeneralJournalDetails> generalJournalDetailsCList) {
        this.generalJournalDetailsCList = generalJournalDetailsCList;
    }

    /**
     * @return the generalJournalDetailsBList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsBList() {
        return generalJournalDetailsBList;
    }

    /**
     * @param generalJournalDetailsBList the generalJournalDetailsBList to set
     */
    public void setGeneralJournalDetailsBList(List<GeneralJournalDetails> generalJournalDetailsBList) {
        this.generalJournalDetailsBList = generalJournalDetailsBList;
    }

    /**
     * @return the netProfit
     */
    public BigDecimal getNetProfit() {
        return netProfit;
    }

    /**
     * @param netProfit the netProfit to set
     */
    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    /**
     * @return the commonSearchBean
     */
    public CommonSearchBean getCommonSearchBean() {
        return commonSearchBean;
    }

    /**
     * @param commonSearchBean the commonSearchBean to set
     */
    public void setCommonSearchBean(CommonSearchBean commonSearchBean) {
        this.commonSearchBean = commonSearchBean;
    }

    /**
     * @return the openingBalanceId
     */
    public Symbol getOpeningBalanceId() {
        return openingBalanceId;
    }

    /**
     * @param openingBalanceId the openingBalanceId to set
     */
    public void setOpeningBalanceId(Symbol openingBalanceId) {
        this.openingBalanceId = openingBalanceId;
    }

}
