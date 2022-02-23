/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GeneraljournalDetailsService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAnalyticsAccountsServcie;
import com.isag.businessservice.reports.analyticalRatiosService;
import com.isag.businessservice.reports.searchBean.CommonSearchBean;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAnalyticsAccounts;
import com.isag.isag.BaseGlAccountReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.IncomeMenuBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hq002
 */
public class RatioOfNetProfitToOwnerEquityMB extends BaseGlAccountReportBean {

    private List<GlAccount> glAccountRootList;
    private List<IncomeMenuBean> incomeMenuBeanList;
    private List<IncomeMenuBean> incomeMenuBeanViewList;
    private List<IncomeMenuBean> expensesAndIncomeList;
    private List<IncomeMenuBean> incomeMenuBeanViewPartOfIncomeList;
    private List<IncomeMenuBean> incomeMenuBeanViewPartOfExpensesList;
    private List<IncomeMenuBean> holdIncomeMenuBeanViewListTemp;
    private List<GeneralJournalDetails> generalJournalDetailsForDebitList;
    private List<GeneralJournalDetails> generalJournalDetailsForCreditList;
    private Date dateTo;
    private Integer level;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private BigDecimal valueOfIncome = BigDecimal.ZERO;
    private BigDecimal valueOfExpenses = BigDecimal.ZERO;
    private ExternalContext context;
    private boolean flow = false;
    private boolean zeroAmount = false;
    private Integer oneListConfirmation = 0;
    private Integer indexOfIncome;
    private Integer indexOfExpenses;
    private IncomeMenuBean Lastbean;
    private Date dateFrom;

    private Map<Integer, IncomeMenuBean> IncomeMenuBeanMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalBalanceMap = new HashMap<>();
    private TreeMap<Integer, IncomeMenuBean> accountMap = new TreeMap<Integer, IncomeMenuBean>();

    private BigDecimal valueOfOwnerEquity = BigDecimal.ZERO;
    private GlAnalyticsAccounts glAnalyticsAccount;
    private BigDecimal totalRatio = BigDecimal.ZERO;
    private boolean accountIdExist;
    private CommonSearchBean commonSearchBean;

    @EJB
    GlAccountService glAccountService;

    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;

    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    private analyticalRatiosService ratiosService;

    @PostConstruct
    public void init() {
        setContext(FacesContext.getCurrentInstance().getExternalContext());
        setUserData((UserData) getContext().getSessionMap().get("userLogInData"));
        reset();
        search();
    }

    @Override
    public void resetDateFrom() {
        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
    }

    @Override
    public void resetDateTo() {
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public void reset() {
        glAccountRootList = new ArrayList<>();
        incomeMenuBeanList = new ArrayList<>();
        expensesAndIncomeList = new ArrayList<>();
        IncomeMenuBeanMap = new HashMap<>();
        totalBalanceMap = new HashMap<>();
        accountMap = new TreeMap<>();
        incomeMenuBeanViewPartOfIncomeList = new ArrayList<>();
        incomeMenuBeanViewPartOfExpensesList = new ArrayList<>();
        generalJournalDetailsForDebitList = new ArrayList<>();
        generalJournalDetailsForCreditList = new ArrayList<>();
        holdIncomeMenuBeanViewListTemp = new ArrayList<>();
        Lastbean = new IncomeMenuBean();

        commonSearchBean = new CommonSearchBean();
        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public void search() {
        checkDateNull();
        //to make sure that in the second lab doesn't erease the previous list created for assets and deductions
        setIncomeMenuBeanViewList(new ArrayList<IncomeMenuBean>());
        reset();
        glAccountRootList = glAccountService.getBranchAccountRootsForExpensesAndIncome(getUserData().getSelectedBranch());
        Integer debitOrCredit;
        debitOrCredit = 1;
        generalJournalDetailsForDebitList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccount(getUserData().getUserBranch().getId(), debitOrCredit, commonSearchBean, getGlYearSelection());
        //debitOrCredit = 2;
        // generalJournalDetailsForCreditList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccount(getUserData().getUserBranch().getId(), getGlYearSelection().getDateFrom(), dateTo, debitOrCredit);
        //generalJournalDetailsList = ListUtils.union(generalJournalDetailsForDebitList, generalJournalDetailsForCreditList);
        for (GeneralJournalDetails journalDetails : generalJournalDetailsForDebitList) {
            totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
        }
        prepareDataForReport(glAccountRootList);
        incomeMenuBeanList = new ArrayList<IncomeMenuBean>(IncomeMenuBeanMap.values());
        if (level != null) {
            for (IncomeMenuBean bean : incomeMenuBeanList) {
                if (level.compareTo(bean.getLevel()) == 0 || bean.getParent() == null) {
                    getIncomeMenuBeanViewList().add(bean);
                    accountMap.put(bean.getAccountNumber(), bean);
                }
            }
            incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
        } else {
            setIncomeMenuBeanViewList(incomeMenuBeanList);
            for (IncomeMenuBean incomeMenuArranged : getIncomeMenuBeanViewList()) {
                accountMap.put(incomeMenuArranged.getAccountNumber(), incomeMenuArranged);
            }
            incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
        }

        for (IncomeMenuBean bean : incomeMenuBeanList) {
            if (bean.getLevel() == 1) {
                getExpensesAndIncomeList().add(bean);
            }
        }

        totalBalance = expensesAndIncomeList.get(1).getValue().add(expensesAndIncomeList.get(0).getValue());

        IncomeMenuBean bean = new IncomeMenuBean();
        Lastbean.setValue(totalBalance);
        if (Lastbean.getValue().signum() > 0) {
       
               Lastbean.setAccountName(getUserData().getUserDDs().get("NET_INCOME"));
    } else {
       Lastbean.setAccountName(getUserData().getUserDDs().get("NET_LOSS"));
        }
        Lastbean.setColorReference(0);
        Lastbean.setParent(null);
        Lastbean.setRatio(null);
        Lastbean.setAppearenceOfTotal(true);
        incomeMenuBeanViewList.add(Lastbean);
        getTheValueOfOwnerEquity();
    }

    private void prepareDataForReport(List<GlAccount> glAccountRootList) {
        for (GlAccount glAccount : glAccountRootList) {
            IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
            incomeMenuBean.setAccountName(glAccount.getName());
            incomeMenuBean.setAccountNumber(glAccount.getAccNumber());
            incomeMenuBean.setLevel(glAccount.getLevelAcc());
            incomeMenuBean.setId(glAccount.getId());
            incomeMenuBean.setParent(glAccount.getParentAcc() == null ? null : glAccount.getParentAcc().getId());

            incomeMenuBeanList.add(incomeMenuBean);
            IncomeMenuBeanMap.put(glAccount.getId(), incomeMenuBean);
            getChildTreeNodesForAccount(glAccount);
        }
    }

    private void getChildTreeNodesForAccount(GlAccount parentAcc) {

        if (parentAcc.getChildAccounts() != null && !parentAcc.getChildAccounts().isEmpty()) {
            for (GlAccount childAcc : parentAcc.getChildAccounts()) {
                IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
                incomeMenuBean.setAccountName(childAcc.getName());
                incomeMenuBean.setAccountNumber(childAcc.getAccNumber());
                incomeMenuBean.setId(childAcc.getId());
                incomeMenuBean.setLevel(childAcc.getLevelAcc());
                incomeMenuBean.setParent(childAcc.getParentAcc() == null ? null : childAcc.getParentAcc().getId());
                if (childAcc.getType() != null && childAcc.getType() == 1) {
                    //total = generaljournalDetailsService.getBalanceForGlAccount(childAcc.getId(), getGlYearSelection().getDateFrom(), dateTo);
                    total = totalBalanceMap.get(childAcc.getId());
                    if (total != null) {
                        getTotal().add(total);
                        incomeMenuBean.setValue(total);
                        if (childAcc.getParentAcc() != null) {
                            putValueOfParent(childAcc.getParentAcc(), total);
                        }
                    }
                }
                incomeMenuBeanList.add(incomeMenuBean);
                IncomeMenuBeanMap.put(childAcc.getId(), incomeMenuBean);
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    public void putValueOfParent(GlAccount parentAcc, BigDecimal value) {
        IncomeMenuBean menuBean = IncomeMenuBeanMap.get(parentAcc.getId());
        menuBean.setValue(menuBean.getValue() == null ? BigDecimal.ZERO : menuBean.getValue());
        menuBean.setValue(menuBean.getValue().add(value == null ? BigDecimal.ZERO : value));
        //menuBean.setValue((value == null ? BigDecimal.ZERO : value).subtract(menuBean.getValue()));
        IncomeMenuBeanMap.put(parentAcc.getId(), menuBean);
        if (parentAcc.getParentAcc() != null) {
            putValueOfParent(parentAcc.getParentAcc(), value);
        }
    }

    public void getTheValueOfOwnerEquity() {
        glAnalyticsAccount = glAnalyticsAccountsServcie.getSpecificGlAnalyticAccount(getUserData().getUserBranch().getId(), "OWNER_EQUITY");
        isAccountSelected("OWNER_EQUITY");
        if (accountIdExist) {
            valueOfOwnerEquity = ratiosService.getSpecificRatio(dateFrom, dateTo, glAnalyticsAccount.getId());
        }
        validateValues();
        totalRatio = totalBalance.divide(valueOfOwnerEquity, 4, 4).setScale(2, BigDecimal.ROUND_UP);
    }

    private void validateValues() {
        if (totalBalance != null) {
            if (totalBalance.signum() == -1) {
                totalBalance = totalBalance.multiply(BigDecimal.valueOf(-1));
            } else if (totalBalance.compareTo(BigDecimal.ZERO) == 0) {
                totalBalance = totalBalance.add(BigDecimal.ONE);
            }
        } else {
            totalBalance = totalBalance.add(BigDecimal.ONE);
        }
        if (valueOfOwnerEquity != null) {
            if (valueOfOwnerEquity.compareTo(BigDecimal.ZERO) == 0) {
                valueOfOwnerEquity = valueOfOwnerEquity.add(BigDecimal.ONE);
            } else if (valueOfOwnerEquity.signum() == -1) {
                valueOfOwnerEquity = valueOfOwnerEquity.multiply(BigDecimal.valueOf(-1));
            }
        } else if ((totalBalance.compareTo(BigDecimal.ONE) == 0 || totalBalance.compareTo(BigDecimal.ZERO) > 0) && valueOfOwnerEquity == null) {
            valueOfOwnerEquity = BigDecimal.ONE;
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
        if (accountName.contains("OWNER_EQUITY")) {
            if (glAnalyticsAccount.getAccountId() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب ان يتم تخصيص حساب لحقوق الملكية"));
                accountIdExist = false;
            } else if (glAnalyticsAccount.getAccountId().getId() == null) {
                accountIdExist = false;
            } else {
                accountIdExist = true;
            }
        }
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
     * @return the incomeMenuBeanList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanList() {
        return incomeMenuBeanList;
    }

    /**
     * @param incomeMenuBeanList the incomeMenuBeanList to set
     */
    public void setIncomeMenuBeanList(List<IncomeMenuBean> incomeMenuBeanList) {
        this.incomeMenuBeanList = incomeMenuBeanList;
    }

    /**
     * @return the incomeMenuBeanViewList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanViewList() {
        return incomeMenuBeanViewList;
    }

    /**
     * @param incomeMenuBeanViewList the incomeMenuBeanViewList to set
     */
    public void setIncomeMenuBeanViewList(List<IncomeMenuBean> incomeMenuBeanViewList) {
        this.incomeMenuBeanViewList = incomeMenuBeanViewList;
    }

    /**
     * @return the expensesAndIncomeList
     */
    public List<IncomeMenuBean> getExpensesAndIncomeList() {
        return expensesAndIncomeList;
    }

    /**
     * @param expensesAndIncomeList the expensesAndIncomeList to set
     */
    public void setExpensesAndIncomeList(List<IncomeMenuBean> expensesAndIncomeList) {
        this.expensesAndIncomeList = expensesAndIncomeList;
    }

    /**
     * @return the incomeMenuBeanViewPartOfIncomeList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanViewPartOfIncomeList() {
        return incomeMenuBeanViewPartOfIncomeList;
    }

    /**
     * @param incomeMenuBeanViewPartOfIncomeList the
     * incomeMenuBeanViewPartOfIncomeList to set
     */
    public void setIncomeMenuBeanViewPartOfIncomeList(List<IncomeMenuBean> incomeMenuBeanViewPartOfIncomeList) {
        this.incomeMenuBeanViewPartOfIncomeList = incomeMenuBeanViewPartOfIncomeList;
    }

    /**
     * @return the incomeMenuBeanViewPartOfExpensesList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanViewPartOfExpensesList() {
        return incomeMenuBeanViewPartOfExpensesList;
    }

    /**
     * @param incomeMenuBeanViewPartOfExpensesList the
     * incomeMenuBeanViewPartOfExpensesList to set
     */
    public void setIncomeMenuBeanViewPartOfExpensesList(List<IncomeMenuBean> incomeMenuBeanViewPartOfExpensesList) {
        this.incomeMenuBeanViewPartOfExpensesList = incomeMenuBeanViewPartOfExpensesList;
    }

    /**
     * @return the holdIncomeMenuBeanViewListTemp
     */
    public List<IncomeMenuBean> getHoldIncomeMenuBeanViewListTemp() {
        return holdIncomeMenuBeanViewListTemp;
    }

    /**
     * @param holdIncomeMenuBeanViewListTemp the holdIncomeMenuBeanViewListTemp
     * to set
     */
    public void setHoldIncomeMenuBeanViewListTemp(List<IncomeMenuBean> holdIncomeMenuBeanViewListTemp) {
        this.holdIncomeMenuBeanViewListTemp = holdIncomeMenuBeanViewListTemp;
    }

    /**
     * @return the generalJournalDetailsForDebitList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsForDebitList() {
        return generalJournalDetailsForDebitList;
    }

    /**
     * @param generalJournalDetailsForDebitList the
     * generalJournalDetailsForDebitList to set
     */
    public void setGeneralJournalDetailsForDebitList(List<GeneralJournalDetails> generalJournalDetailsForDebitList) {
        this.generalJournalDetailsForDebitList = generalJournalDetailsForDebitList;
    }

    /**
     * @return the generalJournalDetailsForCreditList
     */
    public List<GeneralJournalDetails> getGeneralJournalDetailsForCreditList() {
        return generalJournalDetailsForCreditList;
    }

    /**
     * @param generalJournalDetailsForCreditList the
     * generalJournalDetailsForCreditList to set
     */
    public void setGeneralJournalDetailsForCreditList(List<GeneralJournalDetails> generalJournalDetailsForCreditList) {
        this.generalJournalDetailsForCreditList = generalJournalDetailsForCreditList;
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
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the totalBalance
     */
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    /**
     * @param totalBalance the totalBalance to set
     */
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * @return the valueOfIncome
     */
    public BigDecimal getValueOfIncome() {
        return valueOfIncome;
    }

    /**
     * @param valueOfIncome the valueOfIncome to set
     */
    public void setValueOfIncome(BigDecimal valueOfIncome) {
        this.valueOfIncome = valueOfIncome;
    }

    /**
     * @return the valueOfExpenses
     */
    public BigDecimal getValueOfExpenses() {
        return valueOfExpenses;
    }

    /**
     * @param valueOfExpenses the valueOfExpenses to set
     */
    public void setValueOfExpenses(BigDecimal valueOfExpenses) {
        this.valueOfExpenses = valueOfExpenses;
    }

    /**
     * @return the context
     */
    public ExternalContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(ExternalContext context) {
        this.context = context;
    }

    /**
     * @return the flow
     */
    public boolean isFlow() {
        return flow;
    }

    /**
     * @param flow the flow to set
     */
    public void setFlow(boolean flow) {
        this.flow = flow;
    }

    /**
     * @return the zeroAmount
     */
    public boolean isZeroAmount() {
        return zeroAmount;
    }

    /**
     * @param zeroAmount the zeroAmount to set
     */
    public void setZeroAmount(boolean zeroAmount) {
        this.zeroAmount = zeroAmount;
    }

    /**
     * @return the oneListConfirmation
     */
    public Integer getOneListConfirmation() {
        return oneListConfirmation;
    }

    /**
     * @param oneListConfirmation the oneListConfirmation to set
     */
    public void setOneListConfirmation(Integer oneListConfirmation) {
        this.oneListConfirmation = oneListConfirmation;
    }

    /**
     * @return the indexOfIncome
     */
    public Integer getIndexOfIncome() {
        return indexOfIncome;
    }

    /**
     * @param indexOfIncome the indexOfIncome to set
     */
    public void setIndexOfIncome(Integer indexOfIncome) {
        this.indexOfIncome = indexOfIncome;
    }

    /**
     * @return the indexOfExpenses
     */
    public Integer getIndexOfExpenses() {
        return indexOfExpenses;
    }

    /**
     * @param indexOfExpenses the indexOfExpenses to set
     */
    public void setIndexOfExpenses(Integer indexOfExpenses) {
        this.indexOfExpenses = indexOfExpenses;
    }

    /**
     * @return the Lastbean
     */
    public IncomeMenuBean getLastbean() {
        return Lastbean;
    }

    /**
     * @param Lastbean the Lastbean to set
     */
    public void setLastbean(IncomeMenuBean Lastbean) {
        this.Lastbean = Lastbean;
    }

    /**
     * @return the IncomeMenuBeanMap
     */
    public Map<Integer, IncomeMenuBean> getIncomeMenuBeanMap() {
        return IncomeMenuBeanMap;
    }

    /**
     * @param IncomeMenuBeanMap the IncomeMenuBeanMap to set
     */
    public void setIncomeMenuBeanMap(Map<Integer, IncomeMenuBean> IncomeMenuBeanMap) {
        this.IncomeMenuBeanMap = IncomeMenuBeanMap;
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
     * @return the accountMap
     */
    public TreeMap<Integer, IncomeMenuBean> getAccountMap() {
        return accountMap;
    }

    /**
     * @param accountMap the accountMap to set
     */
    public void setAccountMap(TreeMap<Integer, IncomeMenuBean> accountMap) {
        this.accountMap = accountMap;
    }

    /**
     * @return the valueOfOwnerEquity
     */
    public BigDecimal getValueOfOwnerEquity() {
        return valueOfOwnerEquity;
    }

    /**
     * @param valueOfOwnerEquity the valueOfOwnerEquity to set
     */
    public void setValueOfOwnerEquity(BigDecimal valueOfOwnerEquity) {
        this.valueOfOwnerEquity = valueOfOwnerEquity;
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

}
