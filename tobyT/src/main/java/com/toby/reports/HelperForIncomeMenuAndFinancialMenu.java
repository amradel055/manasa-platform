/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.GeneralBudgetGuideService;
import com.toby.businessservice.GeneraljournalDetailsService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.entity.GeneralBudgetGuide;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entiy.GlAccountEntity;
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
@Named("helperForIncomeMenuAndFinancialMenu")
@ViewScoped
public class HelperForIncomeMenuAndFinancialMenu extends BaseGlAccountReportBean {

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
    private Integer whichComeFirst = 0;
    private IncomeMenuBean Lastbean;

    private Map<Integer, IncomeMenuBean> IncomeMenuBeanMap = new HashMap<>();
    private TreeMap<Integer, IncomeMenuBean> accountMap = new TreeMap<Integer, IncomeMenuBean>();
    private Map<Integer, BigDecimal> totalBalanceMap = new HashMap<>();

    private List<GeneralBudgetGuide> generalBudgetGuideList;
    private List<GlAccount> glAccountPrepareList;
    private List<GlAccountEntity> glAccountEntityArrangedList;
    private List<IncomeMenuBean> incomeMenuBeanArrangedList;
    private List<GlAccountEntity> glAccountEntityMirrorList;
    private Integer count;
    private BigDecimal totalOfEveryIndividualGroup;
    private BigDecimal totalOfWholeGroup;
    private BigDecimal finalTotalOfIncome;
    private BigDecimal finalTotalOfExpeneses;
    private BigDecimal finalTotalOfAssets;
    private BigDecimal finalTotalOfDeduction;
    private Integer firstBeanReference;
    private CommonSearchBean commonSearchBean;
    Integer totalOfAsset;
    Integer totalOfDeduction;
    @EJB
    private GeneralBudgetGuideService budgetGuideService;

    private List<GlAccount> glAccountList;
    private List<GlAccount> glAccountMirrorList;
    private List<GlAccountEntity> glAccountEntityList;
    private BigDecimal tatalOfEveryGroup;
    private String groupNameComparable;
    private Integer groupComparable;

    private Integer totalOfIncome;
    private Integer totalOfExpenses;
    private BigDecimal netProfitOrLoss;
    private Integer controlSearchFlow;
    private String lastItemname;
    IncomeMenuBean theLastBean;
    @EJB
    GlAccountService glAccountService;

    @EJB
    GeneraljournalDetailsService generaljournalDetailsService;

    @PostConstruct
    public void init() {
        setContext(FacesContext.getCurrentInstance().getExternalContext());
        setUserData((UserData) getContext().getSessionMap().get("userLogInData"));

        reset();
        search();
        loadGlAccounts();
        loadData();
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
        Lastbean = new IncomeMenuBean();
        glAccountEntityList = new ArrayList<>();
        glAccountList = new ArrayList<>();
        glAccountMirrorList = new ArrayList<>();
        generalBudgetGuideList = new ArrayList<>();
        glAccountPrepareList = new ArrayList<>();
        glAccountEntityArrangedList = new ArrayList<>();
        incomeMenuBeanArrangedList = new ArrayList<>();
        totalOfEveryIndividualGroup = BigDecimal.ZERO;
        glAccountEntityMirrorList = new ArrayList<>();
        controlSearchFlow = 0;
        commonSearchBean = new CommonSearchBean();
        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);

    }

    @Override
    public void search() {
        //reset();
        //  glAccountRootList = glAccountService.getBranchAccountRootsForAssetsAndDeduction(getUserData().getSelectedBranch());

        tatalOfEveryGroup = BigDecimal.ZERO;
        Integer debitOrCredit;
        debitOrCredit = 1;

        if (chooseTheCurrentPage(context) == true || controlSearchFlow == 0) {
            generalJournalDetailsList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccount(getUserData().getUserBranch().getId(), debitOrCredit, commonSearchBean, getGlYearSelection());
        } else {
            generalJournalDetailsList = new ArrayList<>();
            generalJournalDetailsFirstList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(getUserData().getUserBranch().getId(), 1, commonSearchBean, getGlYearSelection());
            generalJournalDetailsSecondList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditForAllGlAccountForFinancialMenu(getUserData().getUserBranch().getId(), 2, commonSearchBean, getGlYearSelection());
            generalJournalDetailsList = ListUtils.union(generalJournalDetailsFirstList, generalJournalDetailsSecondList);
        }

        for (GeneralJournalDetails journalDetails : generalJournalDetailsList) {
            totalBalanceMap.put(journalDetails.getSerial(), journalDetails.getDebitAmount());
        }
        /* prepareDataForReport(glAccountRootList);
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
         totalBalance();*/
    }

    public void totalBalance() {

        glAccountRootForCalculateBalanceList = glAccountService.getBranchAccountRootsForExpensesAndIncome(getUserData().getSelectedBranch());
        IncomeMenuBeanMap = new HashMap<>();
        //prepareDataForReport(glAccountRootForCalculateBalanceList);
        financialMenuBeanList = new ArrayList<IncomeMenuBean>(IncomeMenuBeanMap.values());

        //iterate through incomeMenuBeanList and check the level for 1 to get the value for the income and expenses 
        for (IncomeMenuBean bean : financialMenuBeanList) {
            if (bean.getLevel() == 1) {
                expensesAndIncomeList.add(bean);
            }
        }
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
            Lastbean.setAccountName("صافي الربح :");
        } else {
            Lastbean.setAccountName("صافي الخسارة :");
        }
        incomeMenuBeanViewList.add(Lastbean);
        deleteZeroRecords();
    }

    /*  public void prepareDataForReport(List<GlAccount> glAccountRootList) {
     for (GlAccount glAccount : glAccountRootList) {
     IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
     incomeMenuBean.setAccountName(glAccount.getName());
     incomeMenuBean.setAccountNumber(glAccount.getAccNumber());
     incomeMenuBean.setLevel(glAccount.getLevelAcc());
     incomeMenuBean.setId(glAccount.getId());
     incomeMenuBean.setParent(glAccount.getParentAcc() == null ? null : glAccount.getParentAcc().getId());

     financialMenuBeanList.add(incomeMenuBean);
     IncomeMenuBeanMap.put(glAccount.getId(), incomeMenuBean);
     getChildTreeNodesForAccount(glAccount,  total1,  total2);
     }
     }*/
    public void prepareDataForReportPerOneAccount(GlAccount glAccountRoot, Integer compareGroupsToGetTotal, Integer total1, Integer total2) {
        Map<String, String> userDDs = getUserData().getUserDDs();

        IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
        incomeMenuBean.setAccountName(glAccountRoot.getName());
        incomeMenuBean.setAccountNumber(glAccountRoot.getAccNumber());
        incomeMenuBean.setLevel(glAccountRoot.getLevelAcc());
        incomeMenuBean.setId(glAccountRoot.getId());
        if (total1.compareTo(0) > 0) {
            incomeMenuBean.setTotalReference(10);
        } else if (total2.compareTo(0) < 0) {
            incomeMenuBean.setTotalReference(20);
        }

        incomeMenuBean.setParent(glAccountRoot.getParentAcc() == null ? null : glAccountRoot.getParentAcc().getId());
        incomeMenuBean.setAccountGroup(userDDs.get(glAccountRoot.getAccGroup().toString()));
        glAccountRoot.setParentAcc(null);
        financialMenuBeanList.add(incomeMenuBean);
        IncomeMenuBeanMap.put(glAccountRoot.getId(), incomeMenuBean);
        groupComparable = glAccountRoot.getId();

        getChildTreeNodesForAccount(glAccountRoot, total1, total2);
    }

    public void getChildTreeNodesForAccount(GlAccount parentAcc, Integer total1, Integer total2) {

        if (parentAcc.getChildAccounts() != null && !parentAcc.getChildAccounts().isEmpty()) {
            for (GlAccount childAcc : parentAcc.getChildAccounts()) {
                IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
                incomeMenuBean.setAccountName(childAcc.getName());
                incomeMenuBean.setAccountNumber(childAcc.getAccNumber());
                incomeMenuBean.setId(childAcc.getId());
                incomeMenuBean.setLevel(childAcc.getLevelAcc());
                if (total1.compareTo(0) > 0) {
                    incomeMenuBean.setTotalReference(10);
                } else if (total2.compareTo(0) < 0) {
                    incomeMenuBean.setTotalReference(20);
                }
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

                //     financialMenuBeanList.add(incomeMenuBean);
                IncomeMenuBeanMap.put(childAcc.getId(), incomeMenuBean);
                getChildTreeNodesForAccount(childAcc, total1, total2);
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
        } else {
            setTheTotalValueForEveryGroup(menuBean);
        }
    }

    public void setTheTotalValueForEveryGroup(IncomeMenuBean menuBean) {
        IncomeMenuBean menu = new IncomeMenuBean();
        tatalOfEveryGroup = tatalOfEveryGroup.add(menuBean.getValue());
        menu.setTotal(tatalOfEveryGroup);
        menu.setAccountName("asdasdasdasd");
        //   financialMenuBeanList.add(menu);
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(commonSearchBean.getDateTo())) {
                resetDateTo();
            }
        }
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
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
        } else if (!holdIncomeMenuBeanViewListTemp.isEmpty()) {
            incomeMenuBeanViewList = new ArrayList<>(holdIncomeMenuBeanViewListTemp);
        }
    }

    public void loadGlAccounts() {
        System.out.println("level: " + level);
        glAccountList = glAccountService.getBranchGLAccountsActiveByLeveAndBranchId(getUserData().getUserBranch().getId());
        glAccountMirrorList = new ArrayList<>(glAccountList);
        GlAccountEntity glAccountLoadedEntity;

        glAccountEntityList = new ArrayList<>();
        for (GlAccount glAccount : glAccountList) {

            glAccountLoadedEntity = new GlAccountEntity();
            glAccountLoadedEntity.setId(glAccount.getId());
            glAccountLoadedEntity.setGlAccount(glAccount);
            glAccountLoadedEntity.setGeneralBudgetId(glAccount.getGeneralBudgetId() != null ? glAccount.getGeneralBudgetId().getId() : null);

            glAccountEntityList.add(glAccountLoadedEntity);

        }

        if (glAccountList == null || glAccountList.size() == 0) {
            glAccountEntityList = new ArrayList<>();
        }
    }

    //method for financial items menu bean 
    public void loadAllData() {
        controlSearchFlow++;
        search();
        Map<String, String> userDDs = getUserData().getUserDDs();
        setGeneralBudgetGuideList(budgetGuideService.getAllGeneralBudgetGuideByBranchId(getUserData().getUserBranch().getId()));
        count = 0;
        firstBeanReference = 0;
        totalOfWholeGroup = BigDecimal.ZERO;
        int index = 0;

        List<Integer> assetIndecies = new ArrayList<>();
        List<Integer> deductionIndecies = new ArrayList<>();
        while (count < getGeneralBudgetGuideList().size()) {
            totalOfAsset = 0;
            totalOfDeduction = 0;
            for (GlAccount glAcc : getGlAccountMirrorList()) {
                if (glAcc.getGeneralBudgetId() != null) {

                    if (glAcc.getGeneralBudgetId().getId().equals(getGeneralBudgetGuideList().get(count).getId())) {
                        if (firstBeanReference == 0) {
                            IncomeMenuBean HeadBean = new IncomeMenuBean();
                            HeadBean.setAccountGroup(userDDs.get(getGeneralBudgetGuideList().get(count).getAccGroup().toString()));
                            HeadBean.setAccountName("البند");
                            HeadBean.setValue(null);
                            HeadBean.setTotalReference(0);
                            HeadBean.setId(getGeneralBudgetGuideList().get(count).getId());
                            getFinancialMenuBeanList().add(HeadBean);
                            firstBeanReference++;
                        }

                        if (glAcc.getAccGroup().toString().equals("ASSETS") || glAcc.getAccGroup().toString().equals("DEDUCTION")) {

                            if (glAcc.getAccGroup().toString().equals("ASSETS")) {
                                totalOfAsset++;
                            } else {
                                totalOfDeduction--;
                            }
                            getValuesOfGlAccount(glAcc, totalOfAsset, totalOfDeduction);
                            /*IncomeMenuBean bean = new IncomeMenuBean();
                             bean.setAccountName("اجمالي " + userDDs.get(generalBudgetGuideList.get(count).getAccGroup().toString()));
                             bean.setTotalReference(100);
                             getFinancialMenuBeanList().add(bean);*/
                        }
                    }
                }
            }
            if (getFinancialMenuBeanList() != null && !getFinancialMenuBeanList().isEmpty()) {
                if (getFinancialMenuBeanList().get(getFinancialMenuBeanList().size() - 1).getTotalReference() == 0) {
                    getFinancialMenuBeanList().remove(getFinancialMenuBeanList().size() - 1);
                } else {
                    if (firstBeanReference == 1) {
                        IncomeMenuBean bean = new IncomeMenuBean();
                        bean.setAccountName("اجمالي " + userDDs.get(getGeneralBudgetGuideList().get(count).getAccGroup().toString()));
                        bean.setTotalReference(200);
                        getFinancialMenuBeanList().add(bean);
                    }
                }
            }

            if (totalOfAsset.compareTo(0) > 0) {
                IncomeMenuBean bean = new IncomeMenuBean();
                bean.setAccountName("اجمالي " + userDDs.get("ASSETS"));
                bean.setTotalReference(100);
                getFinancialMenuBeanList().add(bean);
                assetIndecies.add(getFinancialMenuBeanList().size() - 1);

                if (assetIndecies.size() > 1) {
                    for (Integer i : assetIndecies) {
                        if (i != (int) assetIndecies.get(assetIndecies.size() - 1)) {
                            getFinancialMenuBeanList().remove((int) i);
                        } else {
                            assetIndecies = new ArrayList<>();
                            Integer previousNumOfasset = i - 1;
                            assetIndecies.add(previousNumOfasset);
                        }
                    }
                }

            } else if (totalOfDeduction.compareTo(0) < 0) {
                IncomeMenuBean bean = new IncomeMenuBean();
                bean.setAccountName("اجمالي " + userDDs.get("DEDUCTION"));
                bean.setTotalReference(100);
                getFinancialMenuBeanList().add(bean);
                deductionIndecies.add(getFinancialMenuBeanList().size() - 1);

                if (deductionIndecies.size() > 1) {
                    for (Integer i : deductionIndecies) {
                        if (i != (int) deductionIndecies.get(deductionIndecies.size() - 1)) {
                            getFinancialMenuBeanList().remove((int) i);
                        } else {
                            deductionIndecies = new ArrayList<>();
                            Integer previousNumOfDeduction = i - 1;
                            deductionIndecies.add(previousNumOfDeduction);
                        }
                    }
                }
            }

            count++;
            firstBeanReference = 0;
        }

        Iterator it = getFinancialMenuBeanList().iterator();
        while (it.hasNext()) {
            IncomeMenuBean imb = new IncomeMenuBean();
            imb = (IncomeMenuBean) it.next();
            /* if (imb.getAccountGroup() != null && imb.getTotalReference() != null) {
             if (imb.getTotalReference() != 0) {
             totalOfEveryIndividualGroup = totalOfEveryIndividualGroup.add(imb.getValue());
             totalOfWholeGroup = totalOfWholeGroup.add(totalOfEveryIndividualGroup);
             }
             }
             if (imb.getTotalReference() != null && imb.getTotalReference() == 100) {
             imb.setValue(totalOfEveryIndividualGroup);
             totalOfEveryIndividualGroup = BigDecimal.ZERO;
             }*/
            if (imb.getAccountGroup() != null && imb.getAccountGroup().equals(userDDs.get("ASSETS")) && imb.getTotalReference() == 10) {
                totalOfWholeGroup = totalOfWholeGroup.add(imb.getValue());
            }
            if (imb.getAccountGroup() != null && imb.getAccountGroup().equals(userDDs.get("DEDUCTION")) && imb.getTotalReference() == 20) {
                totalOfWholeGroup = totalOfWholeGroup.add(imb.getValue());
            }
            if (imb.getTotalReference() != null && imb.getTotalReference() == 200) {
                imb.setValue(totalOfWholeGroup);
                totalOfEveryIndividualGroup = totalOfEveryIndividualGroup.add(totalOfWholeGroup);
                totalOfWholeGroup = BigDecimal.ZERO;
            }
            if (imb.getTotalReference() != null && imb.getTotalReference() == 100) {
                imb.setValue(totalOfEveryIndividualGroup);
                if (imb.getAccountName().contains(userDDs.get("DEDUCTION"))) {
                    imb.setValue(imb.getValue().add(getNetProfitOrLoss()));
                    finalTotalOfDeduction = imb.getValue() != null ? imb.getValue() : BigDecimal.ZERO;
                }
                if (imb.getAccountName() != null && imb.getAccountName().contains(userDDs.get("ASSETS"))) {
                    finalTotalOfAssets = totalOfEveryIndividualGroup != null ? totalOfEveryIndividualGroup : BigDecimal.ZERO;
                }
                totalOfEveryIndividualGroup = BigDecimal.ZERO;
            }
        }
    }

    //method for income menu items bean
    public void loadData() {
        netProfitOrLoss = BigDecimal.ZERO;
        Map<String, String> userDDs = getUserData().getUserDDs();
        generalBudgetGuideList = budgetGuideService.getAllGeneralBudgetGuideByBranchId(getUserData().getUserBranch().getId());
        count = 0;
        firstBeanReference = 0;
        totalOfWholeGroup = BigDecimal.ZERO;
        int index = 0;

        List<Integer> assetIndecies = new ArrayList<>();
        List<Integer> deductionIndecies = new ArrayList<>();
        while (count < generalBudgetGuideList.size()) {
            setTotalOfIncome((Integer) 0);
            setTotalOfExpenses((Integer) 0);
            for (GlAccount glAcc : getGlAccountMirrorList()) {
                if (glAcc.getGeneralBudgetId() != null) {

                    if (glAcc.getGeneralBudgetId().getId().equals(generalBudgetGuideList.get(count).getId())) {
                        if (firstBeanReference == 0) {
                            IncomeMenuBean HeadBean = new IncomeMenuBean();
                            HeadBean.setAccountGroup(userDDs.get(generalBudgetGuideList.get(count).getAccGroup().toString()));
                            HeadBean.setAccountName("البند");
                            HeadBean.setValue(null);
                            HeadBean.setTotalReference(0);
                            HeadBean.setId(generalBudgetGuideList.get(count).getId());
                            getFinancialMenuBeanList().add(HeadBean);
                            firstBeanReference++;
                        }

                        if (glAcc.getAccGroup().toString().equals("INCOME") || glAcc.getAccGroup().toString().equals("EXPENSES")) {

                            if (glAcc.getAccGroup().toString().equals("INCOME")) {
                                totalOfIncome++;
                            } else {
                                totalOfExpenses--;
                            }
                            getValuesOfGlAccount(glAcc, totalOfIncome, totalOfExpenses);
                            /*IncomeMenuBean bean = new IncomeMenuBean();
                             bean.setAccountName("اجمالي " + userDDs.get(generalBudgetGuideList.get(count).getAccGroup().toString()));
                             bean.setTotalReference(100);
                             getFinancialMenuBeanList().add(bean);*/
                        }
                    }
                }
            }
            if (getFinancialMenuBeanList() != null && !getFinancialMenuBeanList().isEmpty()) {
                if (getFinancialMenuBeanList().get(getFinancialMenuBeanList().size() - 1).getTotalReference() == 0) {
                    getFinancialMenuBeanList().remove(getFinancialMenuBeanList().size() - 1);
                } else {
                    if (firstBeanReference == 1) {
                        IncomeMenuBean bean = new IncomeMenuBean();
                        bean.setAccountName("اجمالي " + userDDs.get(generalBudgetGuideList.get(count).getAccGroup().toString()));
                        bean.setTotalReference(200);
                        getFinancialMenuBeanList().add(bean);
                    }
                }
            }

            if (getTotalOfIncome().compareTo(0) > 0) {
                IncomeMenuBean bean = new IncomeMenuBean();
                bean.setAccountName("اجمالي " + userDDs.get("INCOME"));
                bean.setTotalReference(100);
                //to differentiate between income and expenses when calculate ratio in uncomeItemsMenuReportMB class
                bean.setParent(10);
                getFinancialMenuBeanList().add(bean);
                assetIndecies.add(getFinancialMenuBeanList().size() - 1);

                if (assetIndecies.size() > 1) {
                    for (Integer i : assetIndecies) {
                        if (i != (int) assetIndecies.get(assetIndecies.size() - 1)) {
                            getFinancialMenuBeanList().remove((int) i);
                        } else {
                            assetIndecies = new ArrayList<>();
                            Integer previousNumOfasset = i - 1;
                            assetIndecies.add(previousNumOfasset);
                        }
                    }
                }

            } else if (getTotalOfExpenses().compareTo(0) < 0) {
                IncomeMenuBean bean = new IncomeMenuBean();
                bean.setAccountName("اجمالي " + userDDs.get("EXPENSES"));
                bean.setTotalReference(100);
                //to differentiate between income and expenses when calculate ratio in uncomeItemsMenuReportMB class
                bean.setParent(20);
                getFinancialMenuBeanList().add(bean);
                deductionIndecies.add(getFinancialMenuBeanList().size() - 1);

                if (deductionIndecies.size() > 1) {
                    for (Integer i : deductionIndecies) {
                        if (i != (int) deductionIndecies.get(deductionIndecies.size() - 1)) {
                            getFinancialMenuBeanList().remove((int) i);
                        } else {
                            deductionIndecies = new ArrayList<>();
                            Integer previousNumOfDeduction = i - 1;
                            deductionIndecies.add(previousNumOfDeduction);
                        }
                    }
                }
            }

            count++;
            firstBeanReference = 0;
        }

        Iterator it = getFinancialMenuBeanList().iterator();
        whichComeFirst = 0;
        while (it.hasNext()) {
            IncomeMenuBean imb = new IncomeMenuBean();
            imb = (IncomeMenuBean) it.next();
            /* if (imb.getAccountGroup() != null && imb.getTotalReference() != null) {
             if (imb.getTotalReference() != 0) {
             totalOfEveryIndividualGroup = totalOfEveryIndividualGroup.add(imb.getValue());
             totalOfWholeGroup = totalOfWholeGroup.add(totalOfEveryIndividualGroup);
             }
             }
             if (imb.getTotalReference() != null && imb.getTotalReference() == 100) {
             imb.setValue(totalOfEveryIndividualGroup);
             totalOfEveryIndividualGroup = BigDecimal.ZERO;
             }*/
            if (imb.getAccountGroup() != null && imb.getAccountGroup().equals(userDDs.get("INCOME")) && imb.getTotalReference() == 10) {
                totalOfWholeGroup = totalOfWholeGroup.add(imb.getValue());
            }
            if (imb.getAccountGroup() != null && imb.getAccountGroup().equals(userDDs.get("EXPENSES")) && imb.getTotalReference() == 20) {
                totalOfWholeGroup = totalOfWholeGroup.add(imb.getValue());
            }
            if (imb.getTotalReference() != null && imb.getTotalReference() == 200) {
                imb.setValue(totalOfWholeGroup);
                totalOfEveryIndividualGroup = totalOfEveryIndividualGroup.add(totalOfWholeGroup);
                totalOfWholeGroup = BigDecimal.ZERO;
            }
            if (imb.getTotalReference() != null && imb.getTotalReference() == 100) {
                imb.setValue(totalOfEveryIndividualGroup);
                setNetProfitOrLoss(getNetProfitOrLoss().add(totalOfEveryIndividualGroup));
                if (imb.getAccountName() != null && imb.getAccountName().contains(userDDs.get("INCOME"))) {
                    finalTotalOfIncome = totalOfEveryIndividualGroup != null ? totalOfEveryIndividualGroup : BigDecimal.ZERO;
                } else if (imb.getAccountName() != null && imb.getAccountName().contains(userDDs.get("EXPENSES"))) {
                    finalTotalOfExpeneses = totalOfEveryIndividualGroup != null ? totalOfEveryIndividualGroup : BigDecimal.ZERO;
                }
                totalOfEveryIndividualGroup = BigDecimal.ZERO;
            }
        }
        if (getNetProfitOrLoss() != null) {
            theLastBean = new IncomeMenuBean();
            theLastBean.setValue(getNetProfitOrLoss());
            if (theLastBean.getValue().signum() > 0) {
                theLastBean.setAccountName("صافي الربح :");
            } else {
                theLastBean.setAccountName("صافي الخسارة :");
            }
            getFinancialMenuBeanList().add(theLastBean);
        }
    }

    public void getValuesOfGlAccount(GlAccount account, Integer total1, Integer total2) {
        prepareDataForReportPerOneAccount(account, generalBudgetGuideList.get(count).getId(), total1, total2);
        getFinancialMenuBeanList();
    }

    public boolean chooseTheCurrentPage(ExternalContext context) {

        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (uri.contains("income")) {
            return true;
        } else {
            return false;
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
     * @return the glAccountEntityList
     */
    public List<GlAccountEntity> getGlAccountEntityList() {
        return glAccountEntityList;
    }

    /**
     * @param glAccountEntityList the glAccountEntityList to set
     */
    public void setGlAccountEntityList(List<GlAccountEntity> glAccountEntityList) {
        this.glAccountEntityList = glAccountEntityList;
    }

    /**
     * @return the glAccountMirrorList
     */
    public List<GlAccount> getGlAccountMirrorList() {
        return glAccountMirrorList;
    }

    /**
     * @param glAccountMirrorList the glAccountMirrorList to set
     */
    public void setGlAccountMirrorList(List<GlAccount> glAccountMirrorList) {
        this.glAccountMirrorList = glAccountMirrorList;
    }

    /**
     * @return the tatalOfEveryGroup
     */
    public BigDecimal getTatalOfEveryGroup() {
        return tatalOfEveryGroup;
    }

    /**
     * @param tatalOfEveryGroup the tatalOfEveryGroup to set
     */
    public void setTatalOfEveryGroup(BigDecimal tatalOfEveryGroup) {
        this.tatalOfEveryGroup = tatalOfEveryGroup;
    }

    /**
     * @return the groupNameComparable
     */
    public String getGroupNameComparable() {
        return groupNameComparable;
    }

    /**
     * @param groupNameComparable the groupNameComparable to set
     */
    public void setGroupNameComparable(String groupNameComparable) {
        this.groupNameComparable = groupNameComparable;
    }

    /**
     * @return the groupComparable
     */
    public Integer getGroupComparable() {
        return groupComparable;
    }

    /**
     * @param groupComparable the groupComparable to set
     */
    public void setGroupComparable(Integer groupComparable) {
        this.groupComparable = groupComparable;
    }

    /**
     * @return the generalBudgetGuideList
     */
    public List<GeneralBudgetGuide> getGeneralBudgetGuideList() {
        return generalBudgetGuideList;
    }

    /**
     * @param generalBudgetGuideList the generalBudgetGuideList to set
     */
    public void setGeneralBudgetGuideList(List<GeneralBudgetGuide> generalBudgetGuideList) {
        this.generalBudgetGuideList = generalBudgetGuideList;
    }

    /**
     * @return the glAccountPrepareList
     */
    public List<GlAccount> getGlAccountPrepareList() {
        return glAccountPrepareList;
    }

    /**
     * @param glAccountPrepareList the glAccountPrepareList to set
     */
    public void setGlAccountPrepareList(List<GlAccount> glAccountPrepareList) {
        this.glAccountPrepareList = glAccountPrepareList;
    }

    /**
     * @return the glAccountEntityArrangedList
     */
    public List<GlAccountEntity> getGlAccountEntityArrangedList() {
        return glAccountEntityArrangedList;
    }

    /**
     * @param glAccountEntityArrangedList the glAccountEntityArrangedList to set
     */
    public void setGlAccountEntityArrangedList(List<GlAccountEntity> glAccountEntityArrangedList) {
        this.glAccountEntityArrangedList = glAccountEntityArrangedList;
    }

    /**
     * @return the incomeMenuBeanArrangedList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanArrangedList() {
        return incomeMenuBeanArrangedList;
    }

    /**
     * @param incomeMenuBeanArrangedList the incomeMenuBeanArrangedList to set
     */
    public void setIncomeMenuBeanArrangedList(List<IncomeMenuBean> incomeMenuBeanArrangedList) {
        this.incomeMenuBeanArrangedList = incomeMenuBeanArrangedList;
    }

    /**
     * @return the glAccountEntityMirrorList
     */
    public List<GlAccountEntity> getGlAccountEntityMirrorList() {
        return glAccountEntityMirrorList;
    }

    /**
     * @param glAccountEntityMirrorList the glAccountEntityMirrorList to set
     */
    public void setGlAccountEntityMirrorList(List<GlAccountEntity> glAccountEntityMirrorList) {
        this.glAccountEntityMirrorList = glAccountEntityMirrorList;
    }

    /**
     * @return the totalOfIncome
     */
    public Integer getTotalOfIncome() {
        return totalOfIncome;
    }

    /**
     * @param totalOfIncome the totalOfIncome to set
     */
    public void setTotalOfIncome(Integer totalOfIncome) {
        this.totalOfIncome = totalOfIncome;
    }

    /**
     * @return the totalOfExpenses
     */
    public Integer getTotalOfExpenses() {
        return totalOfExpenses;
    }

    /**
     * @param totalOfExpenses the totalOfExpenses to set
     */
    public void setTotalOfExpenses(Integer totalOfExpenses) {
        this.totalOfExpenses = totalOfExpenses;
    }

    /**
     * @return the netProfitOrLoss
     */
    public BigDecimal getNetProfitOrLoss() {
        return netProfitOrLoss;
    }

    /**
     * @param netProfitOrLoss the netProfitOrLoss to set
     */
    public void setNetProfitOrLoss(BigDecimal netProfitOrLoss) {
        this.netProfitOrLoss = netProfitOrLoss;
    }

    /**
     * @return the theLastBean
     */
    public IncomeMenuBean getTheLastBean() {
        return theLastBean;
    }

    /**
     * @param theLastBean the theLastBean to set
     */
    public void setTheLastBean(IncomeMenuBean theLastBean) {
        this.theLastBean = theLastBean;
    }

    /**
     * @return the controlSearchFlow
     */
    public Integer getControlSearchFlow() {
        return controlSearchFlow;
    }

    /**
     * @param controlSearchFlow the controlSearchFlow to set
     */
    public void setControlSearchFlow(Integer controlSearchFlow) {
        this.controlSearchFlow = controlSearchFlow;
    }

    /**
     * @return the lastItemname
     */
    public String getLastItemname() {
        return lastItemname;
    }

    /**
     * @param lastItemname the lastItemname to set
     */
    public void setLastItemname(String lastItemname) {
        this.lastItemname = lastItemname;
    }

    /**
     * @return the finalTotalOfIncome
     */
    public BigDecimal getFinalTotalOfIncome() {
        return finalTotalOfIncome;
    }

    /**
     * @param finalTotalOfIncome the finalTotalOfIncome to set
     */
    public void setFinalTotalOfIncome(BigDecimal finalTotalOfIncome) {
        this.finalTotalOfIncome = finalTotalOfIncome;
    }

    /**
     * @return the finalTotalOfExpeneses
     */
    public BigDecimal getFinalTotalOfExpeneses() {
        return finalTotalOfExpeneses;
    }

    /**
     * @param finalTotalOfExpeneses the finalTotalOfExpeneses to set
     */
    public void setFinalTotalOfExpeneses(BigDecimal finalTotalOfExpeneses) {
        this.finalTotalOfExpeneses = finalTotalOfExpeneses;
    }

    /**
     * @return the whichComeFirst
     */
    public Integer getWhichComeFirst() {
        return whichComeFirst;
    }

    /**
     * @param whichComeFirst the whichComeFirst to set
     */
    public void setWhichComeFirst(Integer whichComeFirst) {
        this.whichComeFirst = whichComeFirst;
    }

    /**
     * @return the finalTotalOfAssets
     */
    public BigDecimal getFinalTotalOfAssets() {
        return finalTotalOfAssets;
    }

    /**
     * @param finalTotalOfAssets the finalTotalOfAssets to set
     */
    public void setFinalTotalOfAssets(BigDecimal finalTotalOfAssets) {
        this.finalTotalOfAssets = finalTotalOfAssets;
    }

    /**
     * @return the finalTotalOfDeduction
     */
    public BigDecimal getFinalTotalOfDeduction() {
        return finalTotalOfDeduction;
    }

    /**
     * @param finalTotalOfDeduction the finalTotalOfDeduction to set
     */
    public void setFinalTotalOfDeduction(BigDecimal finalTotalOfDeduction) {
        this.finalTotalOfDeduction = finalTotalOfDeduction;
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
