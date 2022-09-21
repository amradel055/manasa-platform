package com.toby.reports;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.toby.businessservice.report.ComplexRevisionReportViewService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.businessservice.reports.searchBean.ComplexRevisionBalanceSearchBean;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.define.AccountClassEnum;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.report.entity.ComplexRevisionBalanceBean;
import com.toby.report.entity.IncomeMenuBean;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.views.ComplexRevisionBalance;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.JasperPrint;

@Named("complexRevisionBalanceMB")
@ViewScoped
public class ComplexRevisionBalanceMB extends BaseGlAccountReportBean {

    private List<ComplexRevisionBalance> complexRevisionBalance;
    private List<ComplexRevisionBalanceBean> allComplexRevisionBalanceBean;
    private List<ComplexRevisionBalance> complexRevisionBalanceList;
    private List<ComplexRevisionBalance> complexRevisionFirstPeriodList;
    private List<GlAccount> glAccountRootList;
    private List<GlAccount> glAccountFromMenuItemList;
    
     private List<CostCenter> costCenterListNew ;
             
             
    private List<IncomeMenuBean> incomeMenuBeanList;
    private GlaccountConverter accountConverter;
    private CostCenterConverter costCenterConverter;
    private GlAdminUnitConverter glAdminUnitConverter;
    private Integer level;
    private List<IncomeMenuBean> incomeMenuBeanViewList;
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal debitChild = BigDecimal.ZERO;
    private BigDecimal creditChild = BigDecimal.ZERO;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
    private BigDecimal totalBalanceDebit;
    private BigDecimal totalBalanceCredit;
    private BigDecimal totalFirstBalance;
    Map<Integer, IncomeMenuBean> IncomeMenuBeanMap = new HashMap<>();
    private Map<Integer, ComplexRevisionBalanceBean> complexRevisionBalanceMap = new HashMap<>();
    private Map<Integer, ComplexRevisionBalanceBean> complexRevisionBalanceFirstPeriodMap = new HashMap<>();
    private TreeMap<Integer, IncomeMenuBean> accountMap = new TreeMap<Integer, IncomeMenuBean>();
    private List<IncomeMenuBean> holdIncomeMenuBeanViewListTemp;
    private List<IncomeMenuBean> incomeMenuBeanForMenuItemsReportList;
    private CommonSearchBean commonSearchBean;

    private JasperPrint jasperPrint;

    private ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBeanA;

    private List<Integer> levelList;
    private List<Integer> accountList;

    private boolean zeroAmount = false;

    private StringBuilder stringBuilderParent;
    private String uri;

//    private CostCenter costCenterToSelected;
//    private CostCenter costCenterFromSelected;
    private List<CostCenter> costCenterListSelected;
    private GlAdminUnit glAdminUnitToSelected;
    private GlAdminUnit glAdminUnitFromSelected;
    private GlAccount glAccountSelectedTo;
    private GlAccount glAccountSelectedFrom;
    private HashMap hashMap;
    private Integer parameterNumber;
    @EJB
    ComplexRevisionReportViewService complexRevisionReportViewService;

    @EJB
    GlAccountService glAccountService;

    public ComplexRevisionBalanceMB() {
    }

    @PostConstruct
    public void init() {
        if (getGlYearSelection() != null) {
            reset();
            load();

            resetDateFrom();
            resetDateTo();
        } else {
            redirectFinancailYearPage();
        }
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        costCenterConverter = new CostCenterConverter(getCostCenterList());
        glAdminUnitConverter = new GlAdminUnitConverter(getGlAdminUnitList());
        accountConverter = new GlaccountConverter(getGlAccountList());
    }

    @Override
    public void reset() {
        glAccountRootList = new ArrayList<>();
        incomeMenuBeanList = new ArrayList<>();
        IncomeMenuBeanMap = new HashMap<>();
        accountMap = new TreeMap<>();
        complexRevisionBalanceSearchBeanA = new ComplexRevisionBalanceSearchBean();
        complexRevisionBalanceMap = new HashMap<>();
        complexRevisionBalanceFirstPeriodMap = new HashMap<>();
        incomeMenuBeanViewList = new ArrayList<>();
        complexRevisionFirstPeriodList = new ArrayList<>();
        holdIncomeMenuBeanViewListTemp = new ArrayList<>();
        incomeMenuBeanForMenuItemsReportList = new ArrayList<>();
        glAccountFromMenuItemList = new ArrayList<>();
        commonSearchBean = new CommonSearchBean();
        totalDebit = BigDecimal.ZERO;
        totalCredit = BigDecimal.ZERO;
        totalBalanceDebit = BigDecimal.ZERO;
        totalBalanceCredit = BigDecimal.ZERO;
        totalFirstBalance = BigDecimal.ZERO;
        resetDateFrom();
        resetDateTo();
        prepareOpeneingBlancesValues(commonSearchBean);
//        costCenterToSelected = null;
//        costCenterFromSelected = null;
        setCostCenterListSelected(null);
        glAdminUnitFromSelected = null;
        glAdminUnitToSelected = null;
        glAccountSelectedTo = null;
        glAccountSelectedFrom = null;

    }

    public void intailize() {
        glAccountRootList = new ArrayList<>();
        incomeMenuBeanList = new ArrayList<>();
        IncomeMenuBeanMap = new HashMap<>();
        accountMap = new TreeMap<>();
        complexRevisionBalanceMap = new HashMap<>();
        complexRevisionBalanceFirstPeriodMap = new HashMap<>();
        incomeMenuBeanViewList = new ArrayList<>();
        complexRevisionFirstPeriodList = new ArrayList<>();
        incomeMenuBeanForMenuItemsReportList = new ArrayList<>();
        totalDebit = BigDecimal.ZERO;
        totalCredit = BigDecimal.ZERO;
        totalBalanceDebit = BigDecimal.ZERO;
        totalBalanceCredit = BigDecimal.ZERO;
        totalFirstBalance = BigDecimal.ZERO;
    }

    @Override
    public void search() {
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

        intailize();
        complexRevisionBalanceSearchBeanA.setAccountFrom(glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
        complexRevisionBalanceSearchBeanA.setAccountTo(glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
//        complexRevisionBalanceSearchBeanA.setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
//        complexRevisionBalanceSearchBeanA.setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
        complexRevisionBalanceSearchBeanA.setCostCenterList(getCostCenterListSelected());
        complexRevisionBalanceSearchBeanA.setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
        complexRevisionBalanceSearchBeanA.setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);
        complexRevisionBalanceSearchBeanA.setBranchId(getUserData().getUserBranch().getId());
        complexRevisionBalanceSearchBeanA.setPostFlag(uri.contains("financialCenterItemsMenuReport"));
        glAccountRootList = glAccountService.getBranchAccountRoots(getUserData().getUserBranch().getId());
        Integer levelSearch = complexRevisionBalanceSearchBeanA.getLevelFrom() != null ? complexRevisionBalanceSearchBeanA.getLevelFrom() : 5;

        if (uri.contains("costCenter")) {
            zeroAmount = true;
            complexRevisionBalanceList = complexRevisionReportViewService.getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnCostCenter(complexRevisionBalanceSearchBeanA, getGlYearSelection(), stringBuilderParent);
            findComplexRevisionBalanceValues();
            calculateTotals(levelSearch);
        } else if (uri.contains("adminUnit")) {
            zeroAmount = true;
            complexRevisionBalanceList = complexRevisionReportViewService.getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnAdminUnit(complexRevisionBalanceSearchBeanA, getGlYearSelection(), stringBuilderParent);
            findComplexRevisionBalanceValues();
            calculateTotals(levelSearch);
        } else {
            if (uri.contains("complexRevisionOpenningReport")) {
                complexRevisionBalanceSearchBeanA.setIsOpenningBalance(Boolean.TRUE);
            }
            complexRevisionBalanceList = complexRevisionReportViewService.getALLRevisionBalanceReportWithSumOfDebitAndCredit(complexRevisionBalanceSearchBeanA, getGlYearSelection());
            complexRevisionFirstPeriodList = complexRevisionReportViewService.getFirstPeriodByBranchId(complexRevisionBalanceSearchBeanA, getGlYearSelection());
            ComplexRevisionBalanceBean complexRevisionBalanceBean;

            for (ComplexRevisionBalance complexRevisionBalances : complexRevisionFirstPeriodList) {
                complexRevisionBalanceBean = new ComplexRevisionBalanceBean();
                complexRevisionBalanceBean.setAccountId(complexRevisionBalances.getGlAccountId());
                complexRevisionBalanceBean.setDebit(complexRevisionBalances.getDebit() != null ? complexRevisionBalances.getDebit() : BigDecimal.ZERO);
                complexRevisionBalanceFirstPeriodMap.put(complexRevisionBalances.getGlAccountNumber(), complexRevisionBalanceBean);
            }
            findComplexRevisionBalanceValues();
            for (IncomeMenuBean incomeMenuForTotal : incomeMenuBeanViewList) {
                BigDecimal x = BigDecimal.ZERO;
                if (incomeMenuForTotal.getCreditBalance().signum() == -1) {
                    x = incomeMenuForTotal.getCreditBalance();
                    x = x.multiply(BigDecimal.valueOf(-1));
                    incomeMenuForTotal.setCreditBalance(BigDecimal.ZERO);
                    incomeMenuForTotal.setDebitBalance(x);
                } else if (incomeMenuForTotal.getDebitBalance().signum() == -1) {
                    x = incomeMenuForTotal.getDebitBalance();
                    x = x.multiply(BigDecimal.valueOf(-1));
                    incomeMenuForTotal.setDebitBalance(BigDecimal.ZERO);
                    incomeMenuForTotal.setCreditBalance(x);
                }

                if (incomeMenuForTotal.getLevel().compareTo(levelSearch) == 0) {
                    totalDebit = totalDebit.add(incomeMenuForTotal.getDebit() != null ? incomeMenuForTotal.getDebit() : BigDecimal.ZERO);
                    totalCredit = totalCredit.add(incomeMenuForTotal.getCredit() != null ? incomeMenuForTotal.getCredit() : BigDecimal.ZERO);
                    totalBalanceDebit = totalBalanceDebit.add(incomeMenuForTotal.getDebitBalance() != null ? incomeMenuForTotal.getDebitBalance() : BigDecimal.ZERO);
                    totalBalanceCredit = totalBalanceCredit.add(incomeMenuForTotal.getCreditBalance() != null ? incomeMenuForTotal.getCreditBalance() : BigDecimal.ZERO);
                    totalFirstBalance = totalFirstBalance.add(incomeMenuForTotal.getFirstDurationBalance() != null ? incomeMenuForTotal.getFirstDurationBalance() : BigDecimal.ZERO);
                }
            }
        }

        holdIncomeMenuBeanViewListTemp = new ArrayList<>();
        deleteZeroRecords();
        if (incomeMenuBeanViewList != null && !incomeMenuBeanViewList.isEmpty()) {
            setStickyHeader(true);
        } else {
            setStickyHeader(false);
        }
    }

    private void calculateTotals(Integer levelSearch) {
        for (IncomeMenuBean incomeMenuForTotal : incomeMenuBeanViewList) {
            if (incomeMenuForTotal.getLevel().compareTo(levelSearch) == 0) {
                totalDebit = totalDebit.add(incomeMenuForTotal.getDebit() != null ? incomeMenuForTotal.getDebit() : BigDecimal.ZERO);
                totalCredit = totalCredit.add(incomeMenuForTotal.getCredit() != null ? incomeMenuForTotal.getCredit() : BigDecimal.ZERO);
                totalFirstBalance = totalFirstBalance.add(incomeMenuForTotal.getBalance() != null ? incomeMenuForTotal.getBalance() : BigDecimal.ZERO);
            }
        }
    }

    private void findComplexRevisionBalanceValues() {
        ComplexRevisionBalanceBean complexRevisionBalanceBean;

        for (ComplexRevisionBalance complexRevisionBalances : complexRevisionBalanceList) {
            complexRevisionBalanceBean = new ComplexRevisionBalanceBean();
            complexRevisionBalanceBean.setAccountNumber(complexRevisionBalances.getGlAccountNumber());
            complexRevisionBalanceBean.setCredit(complexRevisionBalances.getCredit() != null ? complexRevisionBalances.getCredit() : null);
            complexRevisionBalanceBean.setDebit(complexRevisionBalances.getDebit() != null ? complexRevisionBalances.getDebit() : null);
            complexRevisionBalanceMap.put(complexRevisionBalances.getGlAccountNumber(), complexRevisionBalanceBean);
        }
        prepareDataForReport(glAccountRootList);

        incomeMenuBeanList = new ArrayList<IncomeMenuBean>(IncomeMenuBeanMap.values());

        if (complexRevisionBalanceSearchBeanA.getLevelFrom() != null || complexRevisionBalanceSearchBeanA.getLevelTo() != null) {
            levelList = new ArrayList<>();
            for (int i = (complexRevisionBalanceSearchBeanA.getLevelFrom() == null ? 0 : complexRevisionBalanceSearchBeanA.getLevelFrom()); i <= (complexRevisionBalanceSearchBeanA.getLevelTo() == null ? 20 : complexRevisionBalanceSearchBeanA.getLevelTo()); i++) {
                levelList.add(i);
            }
        }

        if (complexRevisionBalanceSearchBeanA.getAccountFrom() != null || complexRevisionBalanceSearchBeanA.getAccountTo() != null) {
            accountList = new ArrayList<>();
            accountList = glAccountService.getAccountList(complexRevisionBalanceSearchBeanA);
        }

        if (complexRevisionBalanceSearchBeanA.getLevelFrom() != null || complexRevisionBalanceSearchBeanA.getLevelTo() != null || complexRevisionBalanceSearchBeanA.getAccountFrom() != null || complexRevisionBalanceSearchBeanA.getAccountTo() != null) {
            for (IncomeMenuBean bean : incomeMenuBeanList) {
                if ((complexRevisionBalanceSearchBeanA.getLevelFrom() != null || complexRevisionBalanceSearchBeanA.getLevelTo() != null) && (complexRevisionBalanceSearchBeanA.getAccountFrom() == null && complexRevisionBalanceSearchBeanA.getAccountTo() == null)) {
                    if (levelList.contains(bean.getLevel())) {
                        getIncomeMenuBeanViewList().add(bean);
                        getAccountMap().put(bean.getAccountNumber(), bean);
                    }
                } else if ((complexRevisionBalanceSearchBeanA.getAccountFrom() != null || complexRevisionBalanceSearchBeanA.getAccountTo() != null) && (complexRevisionBalanceSearchBeanA.getLevelFrom() == null && complexRevisionBalanceSearchBeanA.getLevelTo() == null)) {
                    if (accountList.contains(bean.getAccountNumber())) {
                        getIncomeMenuBeanViewList().add(bean);
                        getAccountMap().put(bean.getAccountNumber(), bean);
                    }
                } else {
                    if (accountList.contains(bean.getAccountNumber()) && levelList.contains(bean.getLevel())) {
                        getIncomeMenuBeanViewList().add(bean);
                        getAccountMap().put(bean.getAccountNumber(), bean);
                    }
                }
            }
            incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
        } else {
            for (IncomeMenuBean incomeMenuArranged : incomeMenuBeanList) {
                accountMap.put(incomeMenuArranged.getAccountNumber(), incomeMenuArranged);
            }
            incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
        }
    }

    private void prepareDataForReport(List<GlAccount> glAccountRootList) {
        for (GlAccount glAccount : glAccountRootList) {
            IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
            incomeMenuBean.setAccountName(glAccount.getName());
            incomeMenuBean.setAccountNumber(glAccount.getAccNumber());
            incomeMenuBean.setLevel(glAccount.getLevelAcc());
            incomeMenuBean.setId(glAccount.getId());
            incomeMenuBean.setParent(glAccount.getParentAcc() == null ? null : glAccount.getParentAcc().getId());
            incomeMenuBean.setGeneralBudgetGuideDebitId(glAccount.getGeneralBudgetIdDebit() != null ? glAccount.getGeneralBudgetIdDebit().getId() : null);
            incomeMenuBean.setGeneralBudgetGuideCreditId(glAccount.getGeneralBudgetIdCredit() != null ? glAccount.getGeneralBudgetIdCredit().getId() : null);

            IncomeMenuBeanMap.put(glAccount.getId(), incomeMenuBean);
//            System.out.println("------------------>    " + glAccount.getAccNumber());
//            if(glAccount.getAccNumber() == 20000000){
//                System.out.println("------------------>    " + glAccount.getAccNumber() +"   <--------------------");
//            }
            
            getChildTreeNodesForAccount(glAccount);
        }
    }

    private void prepareDataForMenuItemReport() {
        for (GlAccount glAccount : glAccountFromMenuItemList) {
            IncomeMenuBean incomeMenuBean = new IncomeMenuBean();
            incomeMenuBean.setAccountName(glAccount.getName());
            incomeMenuBean.setAccountNumber(glAccount.getAccNumber());
            incomeMenuBean.setLevel(glAccount.getLevelAcc());
            incomeMenuBean.setId(glAccount.getId());
            incomeMenuBean.setParent(glAccount.getParentAcc() == null ? null : glAccount.getParentAcc().getId());
            incomeMenuBean.setGeneralBudgetGuideDebitId(glAccount.getGeneralBudgetIdDebit() != null ? glAccount.getGeneralBudgetIdDebit().getId() : null);
            incomeMenuBean.setGeneralBudgetGuideCreditId(glAccount.getGeneralBudgetIdCredit() != null ? glAccount.getGeneralBudgetIdCredit().getId() : null);

            IncomeMenuBeanMap.put(glAccount.getId(), incomeMenuBean);
            getChildTreeNodesForAccount(glAccount);
            IncomeMenuBean menuBean = new IncomeMenuBean();
            menuBean = IncomeMenuBeanMap.get(glAccount.getId());
            incomeMenuBeanForMenuItemsReportList.add(menuBean);

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
                incomeMenuBean.setGeneralBudgetGuideDebitId(childAcc.getGeneralBudgetIdDebit() != null ? childAcc.getGeneralBudgetIdDebit().getId() : null);
                incomeMenuBean.setGeneralBudgetGuideCreditId(childAcc.getGeneralBudgetIdCredit() != null ? childAcc.getGeneralBudgetIdCredit().getId() : null);

                if (childAcc.getType() != null && childAcc.getType() == 1) {
//                    if(childAcc.getAccNumber() == 21010001){
//                        System.out.println("com.toby.reports.ComplexRevisionBalanceMB.getChildTreeNodesForAccount()");
//                    }
                    ComplexRevisionBalanceBean balanceBean = complexRevisionBalanceMap.get(childAcc.getAccNumber());

                    incomeMenuBean.setDebit((balanceBean != null && balanceBean.getDebit() != null) ? balanceBean.getDebit() : BigDecimal.ZERO);
                    incomeMenuBean.setCredit((balanceBean != null && balanceBean.getCredit() != null) ? balanceBean.getCredit() : BigDecimal.ZERO);

                    if (!complexRevisionBalanceSearchBeanA.isIsOpenningBalance() && getGlYearSelection().getDateFrom().before(complexRevisionBalanceSearchBeanA.getPeriodFrom() != null ? complexRevisionBalanceSearchBeanA.getPeriodFrom() : getGlYearSelection().getDateFrom())) {
                        setFirstDuration(childAcc, incomeMenuBean);
                    } else {
                        if (getGlYearSelection().getDateFrom().equals(complexRevisionBalanceSearchBeanA.getPeriodFrom() != null ? complexRevisionBalanceSearchBeanA.getPeriodFrom() : getGlYearSelection().getDateFrom())) {
                            setFirstDuration(childAcc, incomeMenuBean);
                        } else if (getGlYearSelection().getDateFrom().before(complexRevisionBalanceSearchBeanA.getPeriodFrom() != null ? complexRevisionBalanceSearchBeanA.getPeriodFrom() : getGlYearSelection().getDateFrom())) {
                            setFirstDuration(childAcc, incomeMenuBean);
                        }
                    }
                    // incomeMenuBean.setFirstDurationBalance(incomeMenuBean.getFirstDurationBalance().add(getTotalBalanceMap().get(childAcc.getId()) != null ? getTotalBalanceMap().get(childAcc.getId()) : BigDecimal.ZERO));

                    if (childAcc.getAccClass().compareTo(AccountClassEnum.DEBIT) == 0) {
                        incomeMenuBean.setDebitBalance(incomeMenuBean.getFirstDurationBalance().add(incomeMenuBean.getDebit().subtract(incomeMenuBean.getCredit())));
                        incomeMenuBean.setBalance(incomeMenuBean.getDebit().subtract(incomeMenuBean.getCredit()));
                    } else if (childAcc.getAccClass().compareTo(AccountClassEnum.CREDIT) == 0) {
                        incomeMenuBean.setCreditBalance(incomeMenuBean.getFirstDurationBalance().add(incomeMenuBean.getCredit().subtract(incomeMenuBean.getDebit())));
                        incomeMenuBean.setBalance(incomeMenuBean.getCredit().subtract(incomeMenuBean.getDebit()));
                    }

                    if (childAcc.getParentAcc() != null) {
                        putValueOfParent(childAcc.getParentAcc(), incomeMenuBean.getCredit(), incomeMenuBean.getDebit(), incomeMenuBean.getFirstDurationBalance());
                    }
                }
                //  incomeMenuBean.setValue(total);
                IncomeMenuBeanMap.put(childAcc.getId(), incomeMenuBean);
//                System.out.println(childAcc.getAccNumber());
//                if(childAcc.getAccNumber() == 20000000){
//                    System.out.println(childAcc.getAccNumber());
//                }
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    private void setFirstDuration(GlAccount childAcc, IncomeMenuBean incomeMenuBean) {
        ComplexRevisionBalanceBean balanceBeanForPeriod = complexRevisionBalanceFirstPeriodMap.get(childAcc.getId());
        if (balanceBeanForPeriod != null) {
            incomeMenuBean.setFirstDurationBalance(balanceBeanForPeriod.getDebit());
        } else {
            incomeMenuBean.setFirstDurationBalance(BigDecimal.ZERO);
        }
    }

    public void putValueOfParent(GlAccount parentAcc, BigDecimal creditParam, BigDecimal debitParam, BigDecimal balanceBeanForPeriod) {
        IncomeMenuBean menuBean = IncomeMenuBeanMap.get(parentAcc.getId());
        if (IncomeMenuBeanMap.containsKey(parentAcc.getId())) {
            menuBean.setCredit(menuBean.getCredit() == null ? BigDecimal.ZERO : menuBean.getCredit());
            menuBean.setCredit(menuBean.getCredit().add(creditParam == null ? BigDecimal.ZERO : creditParam));
            menuBean.setDebit(menuBean.getDebit() == null ? BigDecimal.ZERO : menuBean.getDebit());
            menuBean.setDebit(menuBean.getDebit().add(debitParam == null ? BigDecimal.ZERO : debitParam));

            if (getGlYearSelection().getDateFrom().before(complexRevisionBalanceSearchBeanA.getPeriodFrom() != null ? complexRevisionBalanceSearchBeanA.getPeriodFrom() : getGlYearSelection().getDateFrom())) {
                menuBean.setFirstDurationBalance(menuBean.getFirstDurationBalance() == null ? BigDecimal.ZERO : menuBean.getFirstDurationBalance());
                menuBean.setFirstDurationBalance(menuBean.getFirstDurationBalance().add(balanceBeanForPeriod));
            } else if (balanceBeanForPeriod.compareTo(BigDecimal.ZERO) != 0) {
                menuBean.setFirstDurationBalance(menuBean.getFirstDurationBalance() == null ? BigDecimal.ZERO : menuBean.getFirstDurationBalance());
                menuBean.setFirstDurationBalance(menuBean.getFirstDurationBalance().add(balanceBeanForPeriod));
            }

            if (parentAcc.getAccClass().compareTo(AccountClassEnum.DEBIT) == 0) {
                menuBean.setDebitBalance(menuBean.getFirstDurationBalance().add(menuBean.getDebit().subtract(menuBean.getCredit())));
                menuBean.setBalance(menuBean.getDebit().subtract(menuBean.getCredit()));
            } else if (parentAcc.getAccClass().compareTo(AccountClassEnum.CREDIT) == 0) {
                menuBean.setCreditBalance(menuBean.getFirstDurationBalance().add(menuBean.getCredit().subtract(menuBean.getDebit())));
                menuBean.setBalance(menuBean.getCredit().subtract(menuBean.getDebit()));
            }
            IncomeMenuBeanMap.put(parentAcc.getId(), menuBean);
            if (parentAcc.getParentAcc() != null) {
                putValueOfParent(parentAcc.getParentAcc(), creditParam, debitParam, balanceBeanForPeriod);
            }
        }
    }

    public void deleteZeroRecords() {

        /*if (holdIncomeMenuBeanViewListTemp != null && !holdIncomeMenuBeanViewListTemp.isEmpty()) {
         incomeMenuBeanViewList = new ArrayList<>(holdIncomeMenuBeanViewListTemp);
         }*/
        if (zeroAmount && incomeMenuBeanViewList != null && !incomeMenuBeanViewList.isEmpty()) {
            holdIncomeMenuBeanViewListTemp = new ArrayList<IncomeMenuBean>(incomeMenuBeanViewList);
            Iterator it = incomeMenuBeanViewList.iterator();
            List<IncomeMenuBean> incomeMenuBeanIterationList = new ArrayList<>();
            while (it.hasNext()) {
                IncomeMenuBean imb = new IncomeMenuBean();
                imb = (IncomeMenuBean) it.next();

                if (imb.getCreditBalance().compareTo(BigDecimal.ZERO) != 0 || imb.getDebitBalance().compareTo(BigDecimal.ZERO) != 0
                        || imb.getCredit().compareTo(BigDecimal.ZERO) != 0 || imb.getDebit().compareTo(BigDecimal.ZERO) != 0
                        || imb.getFirstDurationBalance().compareTo(BigDecimal.ZERO) != 0) {
                    incomeMenuBeanIterationList.add(imb);
                }
            }
            incomeMenuBeanViewList = new ArrayList<>(incomeMenuBeanIterationList);
        } else if (holdIncomeMenuBeanViewListTemp != null && !holdIncomeMenuBeanViewListTemp.isEmpty()) {
            incomeMenuBeanViewList = new ArrayList<>(holdIncomeMenuBeanViewListTemp);
        }
    }

    /* public void searchBasedOnCostCenterOrAdminUnit(StringBuilder stringBuilder) {
     intailize();
     zeroAmount = true;
     complexRevisionBalanceSearchBeanA.setBranchId(getUserData().getUserBranch().getId());
     glAccountRootList = glAccountService.getBranchAccountRoots(getUserData().getUserBranch().getId());
     String uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
     if (uri.contains("costCenter")) {
     complexRevisionBalanceList = complexRevisionReportViewService.getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnCostCenter(complexRevisionBalanceSearchBeanA, getGlYearSelection(), stringBuilder);
     } else {
     complexRevisionBalanceList = complexRevisionReportViewService.getALLRevisionBalanceReportWithSumOfDebitAndCreditBasedOnAdminUnit(complexRevisionBalanceSearchBeanA, getGlYearSelection(), stringBuilder);
     }
     ComplexRevisionBalanceBean complexRevisionBalanceBean;
     allComplexRevisionBalanceBean = new ArrayList<>(0);

     for (ComplexRevisionBalance complexRevisionBalances : complexRevisionBalanceList) {
     complexRevisionBalanceBean = new ComplexRevisionBalanceBean();
     complexRevisionBalanceBean.setAccountNumber(complexRevisionBalances.getGlAccountNumber());
     complexRevisionBalanceBean.setCredit(complexRevisionBalances.getCredit() != null ? complexRevisionBalances.getCredit() : null);
     complexRevisionBalanceBean.setDebit(complexRevisionBalances.getDebit() != null ? complexRevisionBalances.getDebit() : null);
     complexRevisionBalanceMap.put(complexRevisionBalances.getGlAccountNumber(), complexRevisionBalanceBean);
     }

     prepareDataForReport(glAccountRootList);
     incomeMenuBeanList = new ArrayList<IncomeMenuBean>(IncomeMenuBeanMap.values());

     if (complexRevisionBalanceSearchBeanA.getLevelFrom() != null || complexRevisionBalanceSearchBeanA.getLevelTo() != null) {
     levelList = new ArrayList<>();
     for (int i = (complexRevisionBalanceSearchBeanA.getLevelFrom() == null ? 0 : complexRevisionBalanceSearchBeanA.getLevelFrom()); i <= (complexRevisionBalanceSearchBeanA.getLevelTo() == null ? 20 : complexRevisionBalanceSearchBeanA.getLevelTo()); i++) {
     levelList.add(i);
     }
     }
     if (complexRevisionBalanceSearchBeanA.getAccountFrom() != null || complexRevisionBalanceSearchBeanA.getAccountTo() != null) {
     accountList = new ArrayList<>();
     accountList = glAccountService.getAccountList(complexRevisionBalanceSearchBeanA);
     }
     totalDebit = BigDecimal.ZERO;
     totalCredit = BigDecimal.ZERO;
     totalFirstBalance = BigDecimal.ZERO;

     if (complexRevisionBalanceSearchBeanA.getLevelFrom() != null || complexRevisionBalanceSearchBeanA.getLevelTo() != null || complexRevisionBalanceSearchBeanA.getAccountFrom() != null || complexRevisionBalanceSearchBeanA.getAccountTo() != null) {
     for (IncomeMenuBean bean : incomeMenuBeanList) {
     if ((complexRevisionBalanceSearchBeanA.getLevelFrom() != null || complexRevisionBalanceSearchBeanA.getLevelTo() != null) && (complexRevisionBalanceSearchBeanA.getAccountFrom() == null && complexRevisionBalanceSearchBeanA.getAccountTo() == null)) {
     if (levelList.contains(bean.getLevel())) {
     getIncomeMenuBeanViewList().add(bean);
     getAccountMap().put(bean.getAccountNumber(), bean);
     }
     } else if ((complexRevisionBalanceSearchBeanA.getAccountFrom() != null || complexRevisionBalanceSearchBeanA.getAccountTo() != null) && (complexRevisionBalanceSearchBeanA.getLevelFrom() == null && complexRevisionBalanceSearchBeanA.getLevelTo() == null)) {
     if (accountList.contains(bean.getAccountNumber())) {
     getIncomeMenuBeanViewList().add(bean);
     getAccountMap().put(bean.getAccountNumber(), bean);
     }
     } else {
     if (accountList.contains(bean.getAccountNumber()) && levelList.contains(bean.getLevel())) {
     getIncomeMenuBeanViewList().add(bean);
     getAccountMap().put(bean.getAccountNumber(), bean);
     }
     }
     }
     incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
     } else {
     for (IncomeMenuBean incomeMenuArranged : incomeMenuBeanList) {
     accountMap.put(incomeMenuArranged.getAccountNumber(), incomeMenuArranged);
     }
     incomeMenuBeanViewList = new ArrayList<IncomeMenuBean>(accountMap.values());
     }

     deleteZeroRecords();
     }*/
    public List<ComplexRevisionBalanceBean> getAllComplexRevisionBalanceBean() {
        return allComplexRevisionBalanceBean;
    }

    public void setAllComplexRevisionBalanceBean(List<ComplexRevisionBalanceBean> allComplexRevisionBalanceBean) {
        this.allComplexRevisionBalanceBean = allComplexRevisionBalanceBean;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public List<ComplexRevisionBalance> getComplexRevisionBalance() {
        return complexRevisionBalance;
    }

    public void setComplexRevisionBalance(List<ComplexRevisionBalance> complexRevisionBalance) {
        this.complexRevisionBalance = complexRevisionBalance;
    }

    public ComplexRevisionReportViewService getComplexRevisionReportViewService() {
        return complexRevisionReportViewService;
    }

    public void setComplexRevisionReportViewService(ComplexRevisionReportViewService complexRevisionReportViewService) {
        this.complexRevisionReportViewService = complexRevisionReportViewService;
    }

    public ComplexRevisionBalanceSearchBean getComplexRevisionBalanceSearchBeanA() {
        return complexRevisionBalanceSearchBeanA;
    }

    public void setComplexRevisionBalanceSearchBeanA(
            ComplexRevisionBalanceSearchBean complexRevisionBalanceSearchBeanA) {
        this.complexRevisionBalanceSearchBeanA = complexRevisionBalanceSearchBeanA;
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();
        hashMap = new HashMap();
        parameterNumber = 5;
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("firstDurationBalanceText", ("اول مدة"));
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("creditBalanceText", ("رصيد دائن"));
        hashMap.put("debitBalanceText", ("رصيد مدين"));

        hashMap.put("totalText", "الاجمالي");
//        hashMap.put("dateToText", dateToText);
//        hashMap.put("dateFromText", dateFromText);
//        hashMap.put("periodTo", complexRevisionBalanceSearchBeanA.getPeriodTo());
//        hashMap.put("periodFrom", complexRevisionBalanceSearchBeanA.getPeriodFrom());

        hashMap.put("complexRevisionReportText", " ميزان المراجعة");
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("totalTransactionForPeriodText", ("اجمالي الحركة للفترة"));
        hashMap.put("BalanceForPeriodText", ("الرصيد للفترة"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        //  hashMap.put("companyImage", companyImage);
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("totalDebit", totalDebit);
        hashMap.put("totalCredit", totalCredit);
        hashMap.put("totalBalanceDebit", totalBalanceDebit);
        hashMap.put("totalBalanceCredit", totalBalanceCredit);
        hashMap.put("totalFirstBalance", totalFirstBalance);
        hashMap.put("CompanyName", getUserData().getCompany().getName());
//        hashMap.put("levelFromValue", complexRevisionBalanceSearchBeanA.getLevelFrom());
//        hashMap.put("levelToValue", complexRevisionBalanceSearchBeanA.getLevelTo());
//        hashMap.put("levelFromText", userDDs.get("LEVEL_FROM"));
//        hashMap.put("levelToText", userDDs.get("LEVEL_TO"));
//        hashMap.put("accountFromText", userDDs.get("ACCOUNT_FROM"));
//        hashMap.put("accountToText", userDDs.get("ACCOUNT_TO"));
//        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
//        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_NUMBER"));
//        hashMap.put("fromCostCenterText", userDDs.get("COST_CENTER_FROM"));
//        hashMap.put("toCostCenterText", userDDs.get("COST_CENTER_TO"));
//        hashMap.put("fromAdminUnitText", userDDs.get("ADMIN_UNIT_FROM"));
//        hashMap.put("toAdminUnitText", userDDs.get("ADMIN_UNIT_TO"));
//        hashMap.put("accountFromName", glAccountSelectedFrom != null ? glAccountSelectedFrom.getName() : null);
//        hashMap.put("accountFromCode", glAccountSelectedFrom != null ? glAccountSelectedFrom.getAccNumber() : null);
//        hashMap.put("accountToName", glAccountSelectedTo != null ? glAccountSelectedTo.getName() : null);
//        hashMap.put("accountToCode", glAccountSelectedTo != null ? glAccountSelectedTo.getAccNumber() : null);
//        hashMap.put("fromCostCenterName", costCenterFromSelected != null ? costCenterFromSelected.getName() : null);
//        hashMap.put("toCostCenterName", costCenterToSelected != null ? costCenterToSelected.getName() : null);
//        hashMap.put("fromAdminUnitName", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getName() : null);
//        hashMap.put("toAdminUnitName", glAdminUnitToSelected != null ? glAdminUnitToSelected.getName() : null);

        if (glAccountSelectedFrom != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("ACCOUNT_FROM"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), glAccountSelectedFrom.getName());
            parameterNumber++;
        }
        if (glAccountSelectedTo != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("ACCOUNT_TO"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), glAccountSelectedTo.getName());
            parameterNumber++;
        }

//        if (costCenterFromSelected != null) {
//            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("COST_CENTER_FROM"));
//            parameterNumber++;
//            hashMap.put("parameter_" + parameterNumber.toString(), costCenterFromSelected.getName());
//            parameterNumber++;
//        }
//        if (costCenterToSelected != null) {
//            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("COST_CENTER_TO"));
//            parameterNumber++;
//            hashMap.put("parameter_" + parameterNumber.toString(), costCenterToSelected.getName());
//            parameterNumber++;
//        }

        if (glAdminUnitFromSelected != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("ADMIN_UNIT_FROM"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), glAdminUnitFromSelected.getName());
            parameterNumber++;
        }
        if (glAdminUnitToSelected != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("ADMIN_UNIT_TO"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), glAdminUnitToSelected.getName());
            parameterNumber++;
        }

        if (complexRevisionBalanceSearchBeanA.getLevelFrom() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("LEVEL_FROM"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), complexRevisionBalanceSearchBeanA.getLevelFrom().toString());
            parameterNumber++;
        }
        if (complexRevisionBalanceSearchBeanA.getLevelTo() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("LEVEL_TO"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), complexRevisionBalanceSearchBeanA.getLevelTo().toString());
            parameterNumber++;
        }

        if (complexRevisionBalanceSearchBeanA.getPeriodFrom() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("YEAR_FROM"));
            parameterNumber++;
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBeanA.getPeriodFrom());
            hashMap.put("parameter_" + parameterNumber.toString(), formatDateFrom);
            parameterNumber++;
        }
        if (complexRevisionBalanceSearchBeanA.getPeriodTo() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("YEAR_TO"));
            parameterNumber++;
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(complexRevisionBalanceSearchBeanA.getPeriodTo());
            hashMap.put("parameter_" + parameterNumber.toString(), formatDateTo);
            parameterNumber++;
        }

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
            fillReport(prepareReport(), getUserData().getReportPath() + "complexRevisionBalanceNew.jasper", incomeMenuBeanViewList, "pdf");
        } else {
            search();
            fillReport(prepareReport(), getUserData().getReportPath() + "complexRevisionBalanceNew.jasper", incomeMenuBeanViewList, "pdf");
        }
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // To
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); // To
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); // To
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    @Override
    public void resetDateFrom() {
        complexRevisionBalanceSearchBeanA.setPeriodFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);

    }

    @Override
    public void resetDateTo() {
        complexRevisionBalanceSearchBeanA.setPeriodTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(complexRevisionBalanceSearchBeanA.getPeriodFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(complexRevisionBalanceSearchBeanA.getPeriodTo())) {
                resetDateTo();
            }
        }
    }
    
    public void findCostCenter() {
        setCostCenterConverter(new CostCenterConverter(getCostCenterList()));
    }

    public List<CostCenter> completeCostCenter(String query) {
        List<CostCenter> costCenterList = getCostCenterList();
        if (query == null || query.trim().equals("")) {
            costCenterConverter = new CostCenterConverter(costCenterList);
            return costCenterList;
        }
        List<CostCenter> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        CostCenter costCenter;
        for (int i = 0; i < getCostCenterList().size(); i++) {
            costCenter = costCenterList.get(i);
            nameAr = costCenter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }

            code = costCenter.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredCostCenters.contains(costCenter)) {
                    filteredCostCenters.add(costCenter);
                }
            }
        }

        costCenterConverter = new CostCenterConverter(filteredCostCenters);
        return filteredCostCenters;
    }

    public List<GlAdminUnit> completeAdminUnit(String query) {
        List<GlAdminUnit> glAdminUnits = getGlAdminUnitList();
        if (query == null || query.trim().equals("")) {
            glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnits);
            return glAdminUnits;
        }
        List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAdminUnit glAdminUnit;
        for (int i = 0; i < getGlAdminUnitList().size(); i++) {
            glAdminUnit = glAdminUnits.get(i);
            nameAr = glAdminUnit.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }

            code = glAdminUnit.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }
        }
        glAdminUnitConverter = new GlAdminUnitConverter(filteredGlAdminUnitList);
        return filteredGlAdminUnitList;
    }

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = getGlAccountList();//45
        if (query == null || query.trim().equals("")) {
            accountConverter = new GlaccountConverter(glaccounts);
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<>();
        String nameAr;
        Integer code;
        GlAccount glaccount;
        for (int i = 0; i < getGlAccountList().size(); i++) {
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

        accountConverter = new GlaccountConverter(filteredGlaccounts);
        return filteredGlaccounts;
    }

    /**
     * @return the complexRevisionBalanceList
     */
    public List<ComplexRevisionBalance> getComplexRevisionBalanceList() {
        return complexRevisionBalanceList;
    }

    /**
     * @param complexRevisionBalanceList the complexRevisionBalanceList to set
     */
    public void setComplexRevisionBalanceList(List<ComplexRevisionBalance> complexRevisionBalanceList) {
        this.complexRevisionBalanceList = complexRevisionBalanceList;
    }

    /**
     * @return the complexRevisionBalanceMap
     */
    public Map<Integer, ComplexRevisionBalanceBean> getComplexRevisionBalanceMap() {
        return complexRevisionBalanceMap;
    }

    /**
     * @param complexRevisionBalanceMap the complexRevisionBalanceMap to set
     */
    public void setComplexRevisionBalanceMap(Map<Integer, ComplexRevisionBalanceBean> complexRevisionBalanceMap) {
        this.complexRevisionBalanceMap = complexRevisionBalanceMap;
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
     * @return the debitChild
     */
    public BigDecimal getDebitChild() {
        return debitChild;
    }

    /**
     * @param debitChild the debitChild to set
     */
    public void setDebitChild(BigDecimal debitChild) {
        this.debitChild = debitChild;
    }

    /**
     * @return the creditChild
     */
    public BigDecimal getCreditChild() {
        return creditChild;
    }

    /**
     * @param creditChild the creditChild to set
     */
    public void setCreditChild(BigDecimal creditChild) {
        this.creditChild = creditChild;
    }

    /**
     * @return the complexRevisionFirstPeriodList
     */
    public List<ComplexRevisionBalance> getComplexRevisionFirstPeriodList() {
        return complexRevisionFirstPeriodList;
    }

    /**
     * @param complexRevisionFirstPeriodList the complexRevisionFirstPeriodList
     * to set
     */
    public void setComplexRevisionFirstPeriodList(List<ComplexRevisionBalance> complexRevisionFirstPeriodList) {
        this.complexRevisionFirstPeriodList = complexRevisionFirstPeriodList;
    }

    /**
     * @return the complexRevisionBalanceFirstPeriodMap
     */
    public Map<Integer, ComplexRevisionBalanceBean> getComplexRevisionBalanceFirstPeriodMap() {
        return complexRevisionBalanceFirstPeriodMap;
    }

    /**
     * @param complexRevisionBalanceFirstPeriodMap the
     * complexRevisionBalanceFirstPeriodMap to set
     */
    public void setComplexRevisionBalanceFirstPeriodMap(Map<Integer, ComplexRevisionBalanceBean> complexRevisionBalanceFirstPeriodMap) {
        this.complexRevisionBalanceFirstPeriodMap = complexRevisionBalanceFirstPeriodMap;
    }

    /**
     * @return the totalDebit
     */
    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    /**
     * @param totalDebit the totalDebit to set
     */
    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    /**
     * @return the totalCredit
     */
    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    /**
     * @param totalCredit the totalCredit to set
     */
    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
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
     * @return the totalBalanceDebit
     */
    public BigDecimal getTotalBalanceDebit() {
        return totalBalanceDebit;
    }

    /**
     * @param totalBalanceDebit the totalBalanceDebit to set
     */
    public void setTotalBalanceDebit(BigDecimal totalBalanceDebit) {
        this.totalBalanceDebit = totalBalanceDebit;
    }

    /**
     * @return the totalBalanceCredit
     */
    public BigDecimal getTotalBalanceCredit() {
        return totalBalanceCredit;
    }

    /**
     * @param totalBalanceCredit the totalBalanceCredit to set
     */
    public void setTotalBalanceCredit(BigDecimal totalBalanceCredit) {
        this.totalBalanceCredit = totalBalanceCredit;
    }

    /**
     * @return the totalFirstBalance
     */
    public BigDecimal getTotalFirstBalance() {
        return totalFirstBalance;
    }

    /**
     * @param totalFirstBalance the totalFirstBalance to set
     */
    public void setTotalFirstBalance(BigDecimal totalFirstBalance) {
        this.totalFirstBalance = totalFirstBalance;
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
     * @return the stringBuilderParent
     */
    public StringBuilder getStringBuilderParent() {
        return stringBuilderParent;
    }

    /**
     * @param stringBuilderParent the stringBuilderParent to set
     */
    public void setStringBuilderParent(StringBuilder stringBuilderParent) {
        this.stringBuilderParent = stringBuilderParent;
    }

    /**
     * @return the incomeMenuBeanForMenuItemsReportList
     */
    public List<IncomeMenuBean> getIncomeMenuBeanForMenuItemsReportList() {
        return incomeMenuBeanForMenuItemsReportList;
    }

    /**
     * @param incomeMenuBeanForMenuItemsReportList the
     * incomeMenuBeanForMenuItemsReportList to set
     */
    public void setIncomeMenuBeanForMenuItemsReportList(List<IncomeMenuBean> incomeMenuBeanForMenuItemsReportList) {
        this.incomeMenuBeanForMenuItemsReportList = incomeMenuBeanForMenuItemsReportList;
    }

    /**
     * @return the glAccountFromMenuItemList
     */
    public List<GlAccount> getGlAccountFromMenuItemList() {
        return glAccountFromMenuItemList;
    }

    /**
     * @param glAccountFromMenuItemList the glAccountFromMenuItemList to set
     */
    public void setGlAccountFromMenuItemList(List<GlAccount> glAccountFromMenuItemList) {
        this.glAccountFromMenuItemList = glAccountFromMenuItemList;
    }

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

//    /**
//     * @return the costCenterToSelected
//     */
//    public CostCenter getCostCenterToSelected() {
//        return costCenterToSelected;
//    }
//
//    /**
//     * @param costCenterToSelected the costCenterToSelected to set
//     */
//    public void setCostCenterToSelected(CostCenter costCenterToSelected) {
//        this.costCenterToSelected = costCenterToSelected;
//    }
//
//    /**
//     * @return the costCenterFromSelected
//     */
//    public CostCenter getCostCenterFromSelected() {
//        return costCenterFromSelected;
//    }
//
//    /**
//     * @param costCenterFromSelected the costCenterFromSelected to set
//     */
//    public void setCostCenterFromSelected(CostCenter costCenterFromSelected) {
//        this.costCenterFromSelected = costCenterFromSelected;
//    }

    /**
     * @return the glAdminUnitToSelected
     */
    public GlAdminUnit getGlAdminUnitToSelected() {
        return glAdminUnitToSelected;
    }

    /**
     * @param glAdminUnitToSelected the glAdminUnitToSelected to set
     */
    public void setGlAdminUnitToSelected(GlAdminUnit glAdminUnitToSelected) {
        this.glAdminUnitToSelected = glAdminUnitToSelected;
    }

    /**
     * @return the glAdminUnitFromSelected
     */
    public GlAdminUnit getGlAdminUnitFromSelected() {
        return glAdminUnitFromSelected;
    }

    /**
     * @param glAdminUnitFromSelected the glAdminUnitFromSelected to set
     */
    public void setGlAdminUnitFromSelected(GlAdminUnit glAdminUnitFromSelected) {
        this.glAdminUnitFromSelected = glAdminUnitFromSelected;
    }

    /**
     * @return the glAccountSelectedTo
     */
    public GlAccount getGlAccountSelectedTo() {
        return glAccountSelectedTo;
    }

    /**
     * @param glAccountSelectedTo the glAccountSelectedTo to set
     */
    public void setGlAccountSelectedTo(GlAccount glAccountSelectedTo) {
        this.glAccountSelectedTo = glAccountSelectedTo;
    }

    /**
     * @return the glAccountSelectedFrom
     */
    public GlAccount getGlAccountSelectedFrom() {
        return glAccountSelectedFrom;
    }

    /**
     * @param glAccountSelectedFrom the glAccountSelectedFrom to set
     */
    public void setGlAccountSelectedFrom(GlAccount glAccountSelectedFrom) {
        this.glAccountSelectedFrom = glAccountSelectedFrom;
    }

    /**
     * @return the accountConverter
     */
    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    /**
     * @param accountConverter the accountConverter to set
     */
    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    /**
     * @return the costCenterConverter
     */
    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    /**
     * @param costCenterConverter the costCenterConverter to set
     */
    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    /**
     * @return the glAdminUnitConverter
     */
    public GlAdminUnitConverter getGlAdminUnitConverter() {
        return glAdminUnitConverter;
    }

    /**
     * @param glAdminUnitConverter the glAdminUnitConverter to set
     */
    public void setGlAdminUnitConverter(GlAdminUnitConverter glAdminUnitConverter) {
        this.glAdminUnitConverter = glAdminUnitConverter;
    }

    /**
     * @return the hashMap
     */
    public HashMap getHashMap() {
        return hashMap;
    }

    /**
     * @param hashMap the hashMap to set
     */
    public void setHashMap(HashMap hashMap) {
        this.hashMap = hashMap;
    }

    /**
     * @return the parameterNumber
     */
    public Integer getParameterNumber() {
        return parameterNumber;
    }

    /**
     * @param parameterNumber the parameterNumber to set
     */
    public void setParameterNumber(Integer parameterNumber) {
        this.parameterNumber = parameterNumber;
    }

    /**
     * @return the costCenterListSelected
     */
    public List<CostCenter> getCostCenterListSelected() {
        if(costCenterListSelected == null){
            costCenterListSelected = new ArrayList<>();
        }
        return costCenterListSelected;
    }

    /**
     * @param costCenterListSelected the costCenterListSelected to set
     */
    public void setCostCenterListSelected(List<CostCenter> costCenterListSelected) {
        this.costCenterListSelected = costCenterListSelected;
    }

    /**
     * @return the costCenterListNew
     */
    public List<CostCenter> getCostCenterListNew() {
        if(costCenterListNew == null ){
            costCenterListNew = getCostCenterList();
            setCostCenterConverter(new CostCenterConverter(costCenterListNew));
        }
        return costCenterListNew;
    }

    /**
     * @param costCenterListNew the costCenterListNew to set
     */
    public void setCostCenterListNew(List<CostCenter> costCenterListNew) {
        this.costCenterListNew = costCenterListNew;
    }

}
