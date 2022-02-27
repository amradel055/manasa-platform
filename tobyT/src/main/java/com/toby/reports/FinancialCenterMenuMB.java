/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.AccountsSystemSettingsService;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GeneraljournalService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.entity.AccountsSystemSettings;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.report.entity.IncomeMenuBean;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.toby.UserData;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.collections.ListUtils;

/**
 *
 * @author hq002
 */
@Named("financialCenterMenuMB")
@ViewScoped
public class FinancialCenterMenuMB extends BaseGlAccountReportBean {

    private List<GlAccount> glAccountRootList;
    private List<GlAccount> glAccountRootForCalculateBalanceList;
    private List<IncomeMenuBean> incomeMenuBeanList;
    private List<IncomeMenuBean> financialMenuBeanList;
    private List<IncomeMenuBean> incomeMenuBeanViewList;
    private List<IncomeMenuBean> expensesAndIncomeList;
    private List<GeneralJournalDetails> generalJournalDetailsList;
    private List<GeneralJournalDetails> generalJournalDetailsFirstList;
    private List<GeneralJournalDetails> generalJournalDetailsSecondList;
    private List<IncomeMenuBean> holdIncomeMenuBeanViewListTemp;
    private boolean zeroAmount = false;
    private Date dateTo;
    private Integer level;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private ExternalContext context;
    private boolean flow = false;
    private Integer oneListConfirmation = 0;
    private IncomeMenuBean Lastbean;
    private CommonSearchBean commonSearchBean;
    private AccountsSystemSettings accountsSystemSettings;

    Map<Integer, IncomeMenuBean> IncomeMenuBeanMap = new HashMap<>();
    private TreeMap<Integer, IncomeMenuBean> accountMap = new TreeMap<Integer, IncomeMenuBean>();
    private Map<Integer, BigDecimal> totalBalanceMap = new HashMap<>();

    @EJB
    GlAccountService glAccountService;

    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;

    @EJB
    GeneraljournalService generaljournalService;

    @EJB
    private AccountsSystemSettingsService accountsSystemSettingsService;

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));

        reset();
        level = 1;
        accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(getUserData().getCompany().getId());

    }

    @Override
    public void reset() {
        glAccountRootList = new ArrayList<>();
        incomeMenuBeanList = new ArrayList<>();
        financialMenuBeanList = new ArrayList<>();
        expensesAndIncomeList = new ArrayList<>();
        IncomeMenuBeanMap = new HashMap<>();
        accountMap = new TreeMap<>();
        totalBalanceMap = new HashMap<>();
        generalJournalDetailsList = new ArrayList<>();
        holdIncomeMenuBeanViewListTemp = new ArrayList<>();
        generalJournalDetailsFirstList = new ArrayList<>();
        generalJournalDetailsSecondList = new ArrayList<>();
        commonSearchBean = new CommonSearchBean();
        Lastbean = new IncomeMenuBean();
    }

    @Override
    public void search() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        reset();
        commonSearchBean.setDateTo(dateTo);
        commonSearchBean.setBranchId(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        if (checkAppearenceBeforeRevision() == 1) {

            glAccountRootList = glAccountService.getBranchAccountRootsForAssetsAndDeduction(getUserData().getSelectedBranch());

            Integer debitOrCredit;
            debitOrCredit = 1;
            generalJournalDetailsFirstList =  generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(getUserData().getUserBranch().getId(), 1, commonSearchBean, getGlYearSelection());
            generalJournalDetailsSecondList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(getUserData().getUserBranch().getId(), 2, commonSearchBean, getGlYearSelection());
            generalJournalDetailsList = ListUtils.union(generalJournalDetailsFirstList, generalJournalDetailsSecondList);

            for (GeneralJournalDetails journalDetails : generalJournalDetailsList) {
                totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
            }
            prepareDataForReport(glAccountRootList);
            financialMenuBeanList = new ArrayList<IncomeMenuBean>(IncomeMenuBeanMap.values());
            setIncomeMenuBeanViewList(new ArrayList<IncomeMenuBean>());
            if (level != null) {
                for (IncomeMenuBean bean : financialMenuBeanList) {
                    if (level.compareTo(bean.getLevel()) == 0 || bean.getParent() == null) {
                        getIncomeMenuBeanViewList().add(bean);
                        accountMap.put(bean.getAccountNumber(), bean);
                    }
                }
                incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
            } else {
                setIncomeMenuBeanViewList(financialMenuBeanList);
            }
            for (IncomeMenuBean incomeMenuArranged : getIncomeMenuBeanViewList()) {
                accountMap.put(incomeMenuArranged.getAccountNumber(), incomeMenuArranged);
            }
            incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
            totalBalance();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userDDs.get("ERROR"), "يجب مراجعة القيود اولا"));
        }
    }

    public void totalBalance() {

        glAccountRootForCalculateBalanceList = glAccountService.getBranchAccountRootsForExpensesAndIncome(getUserData().getSelectedBranch());
        IncomeMenuBeanMap = new HashMap<>();
        prepareDataForReport(glAccountRootForCalculateBalanceList);
        financialMenuBeanList = new ArrayList<IncomeMenuBean>(IncomeMenuBeanMap.values());

        //iterate through incomeMenuBeanList and check the level for 1 to get the value for the income and expenses 
        for (IncomeMenuBean bean : financialMenuBeanList) {
            if (bean.getLevel() == 1) {
                expensesAndIncomeList.add(bean);
            }
        }
        //after getting a list of 2 elements subtract expenses from income to get the total balance in bigDecimal value
        //totalBalance = expensesAndIncomeList.get(0).getValue().subtract(expensesAndIncomeList.get(1).getValue());

        String x = "INCOME";
        if (x.equalsIgnoreCase(glAccountRootForCalculateBalanceList.get(0).getAccGroup().toString())) {
            //after getting a list of 2 elements subtract expenses from income to get the total balance in bigDecimal value
            totalBalance = expensesAndIncomeList.get(0).getValue().subtract(expensesAndIncomeList.get(1).getValue());
        } else {
            //after getting a list of 2 elements subtract expenses from income to get the total balance in bigDecimal value
            totalBalance = expensesAndIncomeList.get(1).getValue().subtract(expensesAndIncomeList.get(0).getValue());
        }

        Iterator it = incomeMenuBeanViewList.iterator();
        List<IncomeMenuBean> incomeMenuBeanIterationList = new ArrayList<>();
        int i = 0;
        while (it.hasNext()) {
            IncomeMenuBean imb = new IncomeMenuBean();
            imb = (IncomeMenuBean) it.next();
            if (imb.getLevel() == 1 && imb.getValue() != null) {
                incomeMenuBeanIterationList.add(imb);
            }

            if (incomeMenuBeanIterationList.size() > 1 && i == 0) {
                BigDecimal totalProfitAndLoss = incomeMenuBeanIterationList.get(1).getValue().add(totalBalance);
                imb.setValue(totalProfitAndLoss);
                i++;
            }
        }

        Lastbean.setValue(totalBalance);
        Lastbean.setParent(null);
        Lastbean.setColorReference(0);
        if (Lastbean.getValue().signum() > 0) {
            Lastbean.setAccountName("صافي الربح ");
            Lastbean.setTotalReference(10);
        } else {
            Lastbean.setAccountName("صافي الخسارة ");
            Lastbean.setTotalReference(20);
        }
        Lastbean.setAppearenceOfTotal(true);
        Lastbean.setLevel(-1);
        incomeMenuBeanViewList.add(Lastbean);
        deleteZeroRecords();
    }

    private void prepareDataForReport(List<GlAccount> glAccountRootList) {
        for (GlAccount glAccount : glAccountRootList) {
            IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
            incomeMenuBean.setAccountName(glAccount.getName());
            incomeMenuBean.setAccountNumber(glAccount.getAccNumber());
            incomeMenuBean.setLevel(glAccount.getLevelAcc());
            incomeMenuBean.setId(glAccount.getId());
            incomeMenuBean.setParent(glAccount.getParentAcc() == null ? null : glAccount.getParentAcc().getId());

            financialMenuBeanList.add(incomeMenuBean);
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

                financialMenuBeanList.add(incomeMenuBean);
                IncomeMenuBeanMap.put(childAcc.getId(), incomeMenuBean);
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    public void putValueOfParent(GlAccount parentAcc, BigDecimal value) {
        IncomeMenuBean menuBean = IncomeMenuBeanMap.get(parentAcc.getId());
        menuBean.setValue(menuBean.getValue() == null ? BigDecimal.ZERO : menuBean.getValue());
        menuBean.setValue(menuBean.getValue().add(value == null ? BigDecimal.ZERO : value));
        IncomeMenuBeanMap.put(parentAcc.getId(), menuBean);
        if (parentAcc.getParentAcc() != null) {
            putValueOfParent(parentAcc.getParentAcc(), value);
        }
    }

    public boolean chooseTheCurrentPage(ExternalContext context) {

        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (uri.contains("incomeMenu")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(dateTo)) {
                resetDateTo();
            }
        }
    }

    public Integer checkAppearenceBeforeRevision() {
        if (accountsSystemSettings != null && accountsSystemSettings.getAccountStatementAppearance().equalsIgnoreCase("APPEARANCE_AFTER_REVISION")) {
            List<GeneralJournal> generalJournalList = generaljournalService.getAllNotRevisionJournals(commonSearchBean, getGlYearSelection());
            if (generalJournalList != null && generalJournalList.size() > 0) {
                return 0;
            }
        }
        return 1;
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        String levelText = userDDs.get("LEVEL");
        String dateText = userDDs.get("DATE");
        String accountNameText = userDDs.get("ACCOUNT_NAME");
        String accountNumberText = userDDs.get("ACCOUNT_NUMBER");
        String amountText = userDDs.get("AMOUNT");
        String totalProfitOrLossText = Lastbean.getAccountName();

        String PrintedBy = getUserData().getUser().getName();
        String companyName = getUserData().getCompany().getName();
        String companyImage = getUserData().getImageReportPath();
        String branchName = getUserData().getUserBranch().getNameAr();

        HashMap hashMap = new HashMap();

        hashMap.put("levelFrom", level);
        hashMap.put("dateTo", dateTo);

        hashMap.put("levelText", levelText);
        hashMap.put("dateText", dateText);
        hashMap.put("accountNameText", accountNameText);
        hashMap.put("accountNumberText", accountNumberText);
        hashMap.put("amountText", amountText);
        hashMap.put("PrintedBy", PrintedBy);
        // hashMap.put("companyImage", companyImage);
        hashMap.put("branchName", branchName);
        String totalText = userDDs.get("TOTAL");
        hashMap.put("totalText", totalText);
        hashMap.put("totalBalance", totalBalance);
        hashMap.put("totalProfitOrLossText", totalProfitOrLossText);
        String menuNameText = userDDs.get("FINANCIAL_CENTER_MENU");
        hashMap.put("menuNameText", menuNameText);
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {

        if (incomeMenuBeanViewList != null && !incomeMenuBeanViewList.isEmpty()) {
            incomeMenuBeanViewList.remove(incomeMenuBeanViewList.size() - 1);
            fillReport(prepareReport(), getUserData().getReportPath() + "financialCenterMenu.jasper", incomeMenuBeanViewList, "pdf");
        } else {
            search();
            incomeMenuBeanViewList.remove(incomeMenuBeanViewList.size() - 1);
            fillReport(prepareReport(), getUserData().getReportPath() + "financialCenterMenu.jasper", incomeMenuBeanViewList, "pdf");
        }
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

    public void deleteZeroRecords() {

        if (zeroAmount && incomeMenuBeanViewList != null) {
            holdIncomeMenuBeanViewListTemp = new ArrayList<IncomeMenuBean>(incomeMenuBeanViewList);
            Iterator it = incomeMenuBeanViewList.iterator();
            List<IncomeMenuBean> incomeMenuBeanIterationList = new ArrayList<>();
            while (it.hasNext()) {
                IncomeMenuBean imb = new IncomeMenuBean();
                imb = (IncomeMenuBean) it.next();

                if (imb.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    incomeMenuBeanIterationList.add(imb);
                }
            }
            incomeMenuBeanViewList = new ArrayList<>(incomeMenuBeanIterationList);
        } else if (holdIncomeMenuBeanViewListTemp != null && !holdIncomeMenuBeanViewListTemp.isEmpty()) {
            incomeMenuBeanViewList = new ArrayList<>(holdIncomeMenuBeanViewListTemp);
        }
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
     * @return the financialMenuBeanList
     */
    public List<IncomeMenuBean> getFinancialMenuBeanList() {
        return financialMenuBeanList;
    }

    /**
     * @param financialMenuBeanList the financialMenuBeanList to set
     */
    public void setFinancialMenuBeanList(List<IncomeMenuBean> financialMenuBeanList) {
        this.financialMenuBeanList = financialMenuBeanList;
    }

    /**
     * @return the glAccountRootForCalculateBalanceList
     */
    public List<GlAccount> getGlAccountRootForCalculateBalanceList() {
        return glAccountRootForCalculateBalanceList;
    }

    /**
     * @param glAccountRootForCalculateBalanceList the
     * glAccountRootForCalculateBalanceList to set
     */
    public void setGlAccountRootForCalculateBalanceList(List<GlAccount> glAccountRootForCalculateBalanceList) {
        this.glAccountRootForCalculateBalanceList = glAccountRootForCalculateBalanceList;
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
     * @return the accountsSystemSettings
     */
    public AccountsSystemSettings getAccountsSystemSettings() {
        return accountsSystemSettings;
    }

    /**
     * @param accountsSystemSettings the accountsSystemSettings to set
     */
    public void setAccountsSystemSettings(AccountsSystemSettings accountsSystemSettings) {
        this.accountsSystemSettings = accountsSystemSettings;
    }

}
