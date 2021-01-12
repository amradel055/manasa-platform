/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.report.entity.MonitoringBalancesBean;
import com.toby.businessservice.reports.searchBean.CommonSearchBean;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.report.entity.MainTransactionStatementBean;
import com.toby.report.entity.SubAccountSummaryReport;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmed
 */
@Named(value = "mainTransactionStatementMB")
@ViewScoped
public class MainTransactionStatementMB extends SubAccountSummaryReportMB {

    private CommonSearchBean commonSearchBean;
    private GlaccountConverter accountConverter;
    private List<GlAccount> glAccountMainList;
    private GlAccount glAccountSelected;
    private String glAccountNameSelected;
    private Integer glAccountNumberSelected;
    private MainTransactionStatementBean transactionStatementBean;
    private List<MainTransactionStatementBean> mainTransactionStatementBeanList;
    private List<MainTransactionStatementBean> mainTransactionStatementBeanForChartList;
    private List<MainTransactionStatementBean> holdMainTransactionStatementBeanListTemp;
    private Map<Integer, MainTransactionStatementBean> MainTransactionStatementBeanMap = new HashMap<>();
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;
    private BigDecimal totalCredit = BigDecimal.ZERO;
    private Integer multipleOrDirectLevel = 0;
    private List<GlAdminUnit> glAdminUnitLocalList = new ArrayList<>();
    private GlAdminUnit glAdminUnitToSelected;
    private GlAdminUnit glAdminUnitFromSelected;

    private List<CostCenter> costCenterLocalList = new ArrayList<>();
    private CostCenter costCenterToSelected;
    private CostCenter costCenterFromSelected;

    private CostCenterConverter costCenterConverter;
    private GlAdminUnitConverter glAdminUnitConverter;
    private boolean zeroes;
    private String uri;

    private List<MonitoringBalancesBean> monitoringBalancesBeanList;
    private List<SubAccountSummaryReport> subAccountSummaryReportList;
    private MainTransactionStatementBean mainTransactionStatementBeanSelected;
    private List<Object> pieChartsRows;

    //private List<SubAccountSummary> subAccountSummaryList;
    //private SubAccountSummarySearchBean subAccountSummarySearchBean;

    /*  @EJB
     GlAccountService glAccountService;
     @EJB
     GlYearService glYearService;*/
    @PostConstruct
    @Override
    public void init() {
        if (getGlYearSelection() != null) {
            reset();
            super.init();
            //load();
            loadData();
            resetDateFrom();
            resetDateTo();
        } else {
            redirectFinancailYearPage();
        }
        resetAllData();
        zeroes = Boolean.FALSE;
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

    }

    public void loadData() {
        glAccountMainList = new ArrayList<>();

        costCenterLocalList = getCostCenterList();
        glAdminUnitLocalList = getGlAdminUnitList();

        costCenterConverter = new CostCenterConverter(costCenterLocalList);
        glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnitLocalList);

        glAccountMainList = glAccountService.findAllMainGlAccountsByBranch(getUserData().getUserBranch().getId());
        if (glAccountMainList != null && !glAccountMainList.isEmpty()) {
            accountConverter = new GlaccountConverter(glAccountMainList);
        }

    }

    @Override
    public void reset() {
        commonSearchBean = new CommonSearchBean();
        transactionStatementBean = new MainTransactionStatementBean();
        MainTransactionStatementBeanMap = new HashMap<>();
        mainTransactionStatementBeanList = new ArrayList<>();
        holdMainTransactionStatementBeanListTemp = new ArrayList<>();
        mainTransactionStatementBeanForChartList = new ArrayList<>();
    }

    public void resetAllData() {
        glAccountSelected = null;
        costCenterToSelected = null;
        costCenterFromSelected = null;
        glAdminUnitFromSelected = null;
        glAdminUnitToSelected = null;
        zeroes = Boolean.FALSE;
        multipleOrDirectLevel = 0;
        resetDateFrom();
        resetDateTo();
        mainTransactionStatementBeanList = new ArrayList<>();
        monitoringBalancesBeanList = new ArrayList<>();
        subAccountSummaryReportList = new ArrayList<>();
        mainTransactionStatementBeanForChartList = new ArrayList<>();
    }

    @Override
    public void search() {
        setSubAccountSummarySearchBean(new SubAccountSummarySearchBean());
        getSubAccountSummarySearchBean().setBranchId(getUserData().getUserBranch().getId());
        getSubAccountSummarySearchBean().setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
        getSubAccountSummarySearchBean().setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);
        getSubAccountSummarySearchBean().setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
        getSubAccountSummarySearchBean().setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
        getSubAccountSummarySearchBean().setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
        getSubAccountSummarySearchBean().setCurrencyType(0);
        getSubAccountSummarySearchBean().setDateFrom(commonSearchBean.getDateFrom() != null ? commonSearchBean.getDateFrom() : null);
        getSubAccountSummarySearchBean().setDateTo(commonSearchBean.getDateTo() != null ? commonSearchBean.getDateTo() : null);

        Date dateFrom = commonSearchBean.getDateFrom() != null ? commonSearchBean.getDateFrom() : null;
        Date dateTo = commonSearchBean.getDateTo() != null ? commonSearchBean.getDateTo() : null;
        if (glAccountSelected != null) {
            commonSearchBean.setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
            commonSearchBean.setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
            commonSearchBean.setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
            commonSearchBean.setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);
            glAccountNameSelected = glAccountSelected.getName();
            glAccountNumberSelected = glAccountSelected.getAccNumber();
            getSumOfDetailsForCreditAndDebitAndBalance(commonSearchBean);

            reset();
            try {
                prepareDataForReportPerOneAccount(glAccountSelected);
                addLastRow();
                calculateRatio();
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(MainTransactionStatementMB.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (mainTransactionStatementBeanList != null && !mainTransactionStatementBeanList.isEmpty()) {
                holdMainTransactionStatementBeanListTemp = new ArrayList<>(mainTransactionStatementBeanList);
                loadChilds();
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار حساب اولا"));
        }
        if (mainTransactionStatementBeanList != null && !mainTransactionStatementBeanList.isEmpty()) {
            setStickyHeader(true);
        } else {
            setStickyHeader(false);
        }
        commonSearchBean.setDateFrom(dateFrom != null ? dateFrom : null);
        commonSearchBean.setDateTo(dateTo != null ? dateTo : null);
        if (uri.contains("monitoringForm")) {
            fillMonitoringBeanList();
        }
    }

    public void loadChilds() {

        if (holdMainTransactionStatementBeanListTemp != null && !holdMainTransactionStatementBeanListTemp.isEmpty()) {
            mainTransactionStatementBeanList = new ArrayList<>(holdMainTransactionStatementBeanListTemp);
        }

        if (multipleOrDirectLevel == 1 && !zeroes && holdMainTransactionStatementBeanListTemp != null) {
            Iterator it = mainTransactionStatementBeanList.iterator();
            List<MainTransactionStatementBean> mtsbList = new ArrayList<>();
            while (it.hasNext()) {
                MainTransactionStatementBean bean = new MainTransactionStatementBean();
                bean = (MainTransactionStatementBean) it.next();
                if (glAccountSelected.getId().compareTo(bean.getParentId()) == 0 || bean.getParentId() == -1) {
                    mtsbList.add(bean);
                }
            }
            mainTransactionStatementBeanList = new ArrayList<>(mtsbList);
        } else if (multipleOrDirectLevel == 0 && zeroes) {
            Iterator it = mainTransactionStatementBeanList.iterator();
            List<MainTransactionStatementBean> mtsbList = new ArrayList<>();
            while (it.hasNext()) {
                MainTransactionStatementBean bean = new MainTransactionStatementBean();
                bean = (MainTransactionStatementBean) it.next();
                if (bean.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    mtsbList.add(bean);
                }
            }
            mainTransactionStatementBeanList = new ArrayList<>(mtsbList);
        } else if (multipleOrDirectLevel == 1 && zeroes) {
            Iterator it = mainTransactionStatementBeanList.iterator();
            List<MainTransactionStatementBean> mtsbList = new ArrayList<>();
            while (it.hasNext()) {
                MainTransactionStatementBean bean = new MainTransactionStatementBean();
                bean = (MainTransactionStatementBean) it.next();
                if (glAccountSelected.getId().compareTo(bean.getParentId()) == 0 || bean.getParentId() == -1) {
                    if (bean.getValue().compareTo(BigDecimal.ZERO) != 0) {
                        mtsbList.add(bean);
                    }
                }
            }
            mainTransactionStatementBeanList = new ArrayList<>(mtsbList);
        }
    }

    public void calculateRatio() {

        BigDecimal totalValue = MainTransactionStatementBeanMap.get(glAccountSelected.getId()).getValue();
        if (totalValue.compareTo(BigDecimal.ZERO) == 0 || totalValue == null) {
            totalValue = BigDecimal.ONE;
        }
        BigDecimal percent = BigDecimal.valueOf(100);
        Iterator it = mainTransactionStatementBeanList.iterator();

        pieChartsRows = new ArrayList<>();

        List<Object> colmns = new ArrayList<>();
        colmns.add("'" + "x" + "'");
        colmns.add("'" + "y" + "'");
        pieChartsRows.add(colmns);

        while (it.hasNext()) {
            MainTransactionStatementBean bean = new MainTransactionStatementBean();
            bean = (MainTransactionStatementBean) it.next();
            bean.setRatio((bean.getValue().abs().divide(totalValue.abs(), 4, 4)).multiply(percent).setScale(2, BigDecimal.ROUND_UP));

            if (bean.getLevel() != null && bean.getLevel() == glAccountSelected.getLevelAcc() + 1 && bean.getValue() != null && bean.getValue().compareTo(BigDecimal.ZERO) != 0) {
                colmns = new ArrayList<>();
                colmns.add("'" + bean.getAccountName() + "'");
                if (bean.getValue().signum() == -1) {
                    colmns.add(BigDecimal.ZERO);
                } else {
                colmns.add(bean.getValue());
                }

                pieChartsRows.add(colmns);
                mainTransactionStatementBeanForChartList.add(bean);
            }

        }

    }

    public void prepareDataForReportPerOneAccount(GlAccount glAccountRoot) throws CloneNotSupportedException {
        //glAccountRoot.setParentAcc(null);
        MainTransactionStatementBean statementBean = new MainTransactionStatementBean();
        statementBean.setId(glAccountRoot.getId());
        statementBean.setLevel(glAccountRoot.getLevelAcc());
        MainTransactionStatementBeanMap.put(glAccountRoot.getId(), statementBean);
        getChildTreeNodesForAccount(glAccountRoot);
    }

    public void getChildTreeNodesForAccount(GlAccount parentAcc) {

        if (parentAcc.getChildAccounts() != null && !parentAcc.getChildAccounts().isEmpty()) {
            for (GlAccount childAcc : parentAcc.getChildAccounts()) {
                MainTransactionStatementBean statementBean = new MainTransactionStatementBean();
                statementBean.setAccountName(childAcc.getName());
                statementBean.setAccountNumber(childAcc.getAccNumber());
                statementBean.setId(childAcc.getId());
                statementBean.setLevel(childAcc.getLevelAcc());
                statementBean.setParentId(parentAcc.getId());

                if (childAcc.getType() != null && childAcc.getType() == 1) {
                    //total = generaljournalDetailsService.getBalanceForGlAccount(childAcc.getId(), getGlYearSelection().getDateFrom(), dateTo);
                    setTotal(getTotalBalanceMap().get(childAcc.getId()));
                    setTotalDebit(getTotalDebitMap().get(childAcc.getId()));
                    setTotalCredit(getTotalCreditMap().get(childAcc.getId()));
                    super.setGlAccountSelected(childAcc);
                    getSubAccountSummarySearchBean().setAccClass(childAcc.getAccClass().toString());
                    statementBean = findDetailsForEachSubAccount(statementBean);
                    /* if (getTotalDebitMap().get(childAcc.getId()).signum() == -1) {
                     setTotalCredit(getTotalDebitMap().get(childAcc.getId()).multiply(BigDecimal.valueOf(-1)));
                     } else {
                     setTotalDebit(getTotalDebitMap().get(childAcc.getId()));
                     }
                    
                     if (getTotalCreditMap().get(childAcc.getId()).signum() == -1) {
                     setTotalDebit(getTotalCreditMap().get(childAcc.getId()).multiply(BigDecimal.valueOf(-1)));
                     } else {
                     setTotalCredit(getTotalCreditMap().get(childAcc.getId()));
                     }
                    
                     if (getTotalBalanceMap().get(childAcc.getId()).signum() == -1) {
                     setTotal(getTotalBalanceMap().get(childAcc.getId()).multiply(BigDecimal.valueOf(-1)));
                     } else {
                     setTotal(getTotalBalanceMap().get(childAcc.getId()));
                     }*/
                    if (getTotal() != null) {
                        getTotal().add(getTotal());
                        statementBean.setValue(getTotal());

                        getTotalDebit().add(getTotalDebit() != null ? getTotalDebit() : BigDecimal.ZERO);
                        statementBean.setDebit(getTotalDebit());

                        getTotalCredit().add(getTotalCredit() != null ? getTotalCredit() : BigDecimal.ZERO);
                        statementBean.setCredit(getTotalCredit());
                        /*if ("DEBIT".equalsIgnoreCase(childAcc.getAccClass().toString())) {
                         statementBean.setDebit(statementBean.getValue());
                         } else if ("Credit".equalsIgnoreCase(childAcc.getAccClass().toString())) {
                         statementBean.setCredit(statementBean.getValue());
                         }*/
                        if (glAccountSelected.getParentAcc() == null) {
                            if (childAcc.getParentAcc() != null) {
                                putValueOfParent(childAcc.getParentAcc(), getTotal(), getTotalDebit(), getTotalCredit());
                            }
                        } else {
                            if (childAcc.getParentAcc().getId().compareTo(glAccountSelected.getParentAcc().getId()) != 0) {
                                putValueOfParent(childAcc.getParentAcc(), getTotal(), getTotalDebit(), getTotalCredit());
                            }
                        }
                    }
                }
                mainTransactionStatementBeanList.add(statementBean);
                MainTransactionStatementBeanMap.put(childAcc.getId(), statementBean);
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    public void putValueOfParent(GlAccount parentAcc, BigDecimal value, BigDecimal debit, BigDecimal credit) {
        MainTransactionStatementBean statementBean = MainTransactionStatementBeanMap.get(parentAcc.getId());
        statementBean.setValue(statementBean.getValue() == null ? BigDecimal.ZERO : statementBean.getValue());
        statementBean.setValue(statementBean.getValue().add(value == null ? BigDecimal.ZERO : value));

        statementBean.setDebit(statementBean.getDebit() == null ? BigDecimal.ZERO : statementBean.getDebit());
        statementBean.setDebit(statementBean.getDebit().add(debit == null ? BigDecimal.ZERO : debit));

        statementBean.setCredit(statementBean.getCredit() == null ? BigDecimal.ZERO : statementBean.getCredit());
        statementBean.setCredit(statementBean.getCredit().add(credit == null ? BigDecimal.ZERO : credit));
        /*if ("DEBIT".equalsIgnoreCase(parentAcc.getAccClass().toString())) {
         statementBean.setDebit(statementBean.getValue());
         } else if ("Credit".equalsIgnoreCase(parentAcc.getAccClass().toString())) {
         statementBean.setCredit(statementBean.getValue());
         }*/
        MainTransactionStatementBeanMap.put(parentAcc.getId(), statementBean);
        if (glAccountSelected.getParentAcc() == null) {
            if (parentAcc.getParentAcc() != null) {
                putValueOfParent(parentAcc.getParentAcc(), value, debit, credit);
            }
        } else {
            if (parentAcc.getParentAcc().getId().compareTo(glAccountSelected.getParentAcc().getId()) != 0) {
                putValueOfParent(parentAcc.getParentAcc(), value, debit, credit);
            }
        }

    }

    public void addLastRow() {
        MainTransactionStatementBean statementBean = new MainTransactionStatementBean();
        statementBean = MainTransactionStatementBeanMap.get(glAccountSelected.getId());
        statementBean.setAccountName("الاجمالي ");
        statementBean.setLevel(null);
        statementBean.setParentId(-1);

        mainTransactionStatementBeanList.add(statementBean);
    }

    public void fillMonitoringBeanList() {
        if (mainTransactionStatementBeanList != null && !mainTransactionStatementBeanList.isEmpty()) {
            MainTransactionStatementBean statementBean = new MainTransactionStatementBean();
            statementBean = MainTransactionStatementBeanMap.get(glAccountSelected.getId());

            MonitoringBalancesBean balancesBean = new MonitoringBalancesBean();
            balancesBean.setAccount(glAccountSelected);
            balancesBean.setBalance(statementBean.getValue());
            balancesBean.setMainTransactionStatementBeanMonitoringList(new ArrayList<>());
            for (MainTransactionStatementBean bean : mainTransactionStatementBeanList) {
                balancesBean.getMainTransactionStatementBeanMonitoringList().add(bean);
            }
            monitoringBalancesBeanList.add(balancesBean);
        } else if (mainTransactionStatementBeanList != null && mainTransactionStatementBeanList.isEmpty()) {
            MainTransactionStatementBean statementBean = new MainTransactionStatementBean();
            statementBean = MainTransactionStatementBeanMap.get(glAccountSelected.getId());
            MonitoringBalancesBean balancesBean = new MonitoringBalancesBean();
            balancesBean.setAccount(glAccountSelected);
            balancesBean.setBalance(statementBean.getValue() != null ? statementBean.getValue() : BigDecimal.ZERO);
            balancesBean.setMainTransactionStatementBeanMonitoringList(new ArrayList<>());
            monitoringBalancesBeanList.add(balancesBean);
        }
    }

    public MainTransactionStatementBean findDetailsForEachSubAccount(MainTransactionStatementBean statementBean) {

        getSubAccountSummarySearchBean().setAccountId(statementBean.getId());
        super.search();
        if (getSubAccountSummaryReports() != null && !getSubAccountSummaryReports().isEmpty()) {
            statementBean.setSubAccountSummaryReportList(new ArrayList<>());
            for (SubAccountSummaryReport sasr : getSubAccountSummaryReports()) {
                statementBean.getSubAccountSummaryReportList().add(sasr);
            }
        }
        return statementBean;
    }

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = glAccountMainList;
        if (query == null || query.trim().equals("")) {
            accountConverter = new GlaccountConverter(glaccounts);
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAccount glaccount;
        for (int i = 0; i < glAccountMainList.size(); i++) {
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

    public List<CostCenter> completeCostCenter(String query) {
        List<CostCenter> costCenterList = getCostCenterLocalList();
        if (query == null || query.trim().equals("")) {
            costCenterConverter = new CostCenterConverter(costCenterList);
            return costCenterList;
        }
        List<CostCenter> filteredCostCenters = new ArrayList<>();

        String nameAr;
        Integer code;
        CostCenter costCenter;
        for (int i = 0; i < getCostCenterLocalList().size(); i++) {
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
        List<GlAdminUnit> glAdminUnits = getGlAdminUnitLocalList();
        if (query == null || query.trim().equals("")) {
            glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnits);
            return glAdminUnits;
        }
        List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAdminUnit glAdminUnit;
        for (int i = 0; i < getGlAdminUnitLocalList().size(); i++) {
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

    @Override
    public void resetDateFrom() {
        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
    }

    @Override
    public void resetDateTo() {
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    @Override
    public void checkDate(Boolean dateType) {
        if (dateType) {
            if (checkFinancailYear(commonSearchBean.getDateFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(commonSearchBean.getDateTo())) {
                resetDateTo();
            }
        }
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("fromCostCenterText", userDDs.get("COST_CENTER_FROM"));
        hashMap.put("toCostCenterText", userDDs.get("COST_CENTER_TO"));
        hashMap.put("fromAdminUnitText", userDDs.get("ADMIN_UNIT_FROM"));
        hashMap.put("toAdminUnitText", userDDs.get("ADMIN_UNIT_TO"));
        hashMap.put("fromDateText", userDDs.get("YEAR_FROM"));
        hashMap.put("toDateText", userDDs.get("YEAR_TO"));
        hashMap.put("ratioText", userDDs.get("PERCENTAGE"));
        hashMap.put("mainAccountText", userDDs.get("MAIN_ACCOUNT"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("accountNumberText", userDDs.get("ACCOUNT_COD"));
        hashMap.put("mainTransactionReport", userDDs.get("MAIN_ACC_TRA_RE"));
        hashMap.put("accountNameSelected", glAccountSelected.getName());
        hashMap.put("accountNumberSelected", glAccountSelected.getAccNumber().toString());

        hashMap.put("fromCostCenter", commonSearchBean.getCostCenterFrom());
        hashMap.put("toCostCenter", commonSearchBean.getCostCenterTo());
        hashMap.put("fromAdminUnit", commonSearchBean.getAdminUnitFrom());
        hashMap.put("toAdminUnit", commonSearchBean.getAdminUnitTo());
        hashMap.put("fromDate", commonSearchBean.getDateFrom());
        hashMap.put("toDate", commonSearchBean.getDateTo());

        hashMap.put("accountFromName", glAccountSelected != null ? glAccountSelected.getName() : null);
        hashMap.put("fromCostCenterName", costCenterFromSelected != null ? costCenterFromSelected.getName() : null);
        hashMap.put("toCostCenterName", costCenterToSelected != null ? costCenterToSelected.getName() : null);
        hashMap.put("fromAdminUnitName", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getName() : null);
        hashMap.put("toAdminUnitName", glAdminUnitToSelected != null ? glAdminUnitToSelected.getName() : null);

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        //  hashMap.put("companyName", getUserData().getCompany().getName());
        //   hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        //subaccountSummaryReport map
        hashMap.put("accountSummaryReportText", userDDs.get("ACCOUNT_SUMMARY_REPORT"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("journalNumberText", userDDs.get("GENERAL_NUMBER"));
        hashMap.put("documentNumberText", userDDs.get("DOCUMENT_NUMBER"));
        hashMap.put("journalTypeText", userDDs.get("JOURNAL_TYPE"));
        hashMap.put("journalStatementText", userDDs.get("GERNAL_STATEMENT"));

        hashMap.put("totalText", userDDs.get("TOTAL"));

        hashMap.put("accountName", mainTransactionStatementBeanSelected != null ? mainTransactionStatementBeanSelected.getAccountName() : null);
        hashMap.put("accountCode", mainTransactionStatementBeanSelected != null ? mainTransactionStatementBeanSelected.getAccountNumber() : null);
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (mainTransactionStatementBeanList != null && !mainTransactionStatementBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "mainTransactionStatement.jasper", mainTransactionStatementBeanList, "pdf");
        } else {
            setStickyHeader(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "لايوجد نتائج بحث"));
        }
    }

    public void exportChartPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (mainTransactionStatementBeanForChartList != null && !mainTransactionStatementBeanForChartList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "mainTransactionStatementWithChart.jasper", mainTransactionStatementBeanForChartList, "pdf");
        } else {
            setStickyHeader(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "لايوجد نتائج بحث"));
        }
    }

    public void exportSummary(ActionEvent actionEvent) throws JRException, IOException {
        mainTransactionStatementBeanSelected = (MainTransactionStatementBean) actionEvent.getComponent().getAttributes().get("varValue");
        subAccountSummaryReportList = mainTransactionStatementBeanSelected.getSubAccountSummaryReportList();

        super.setSubAccountSummaryReports(new ArrayList<>());
        super.setSubAccountSummaryReports(subAccountSummaryReportList);
        //subAccountSummaryReportList = (List<SubAccountSummaryReport>) actionEvent.getComponent().getAttributes().get("varValue");
        super.exportPDF(actionEvent);
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
     * @return the glAccountMainList
     */
    public List<GlAccount> getGlAccountMainList() {
        return glAccountMainList;
    }

    /**
     * @param glAccountMainList the glAccountMainList to set
     */
    public void setGlAccountMainList(List<GlAccount> glAccountMainList) {
        this.glAccountMainList = glAccountMainList;
    }

    /**
     * @return the glAccountSelected
     */
    public GlAccount getGlAccountSelected() {
        return glAccountSelected;
    }

    /**
     * @param glAccountSelected the glAccountSelected to set
     */
    public void setGlAccountSelected(GlAccount glAccountSelected) {
        this.glAccountSelected = glAccountSelected;
    }

    /**
     * @return the transactionStatementBean
     */
    public MainTransactionStatementBean getTransactionStatementBean() {
        return transactionStatementBean;
    }

    /**
     * @param transactionStatementBean the transactionStatementBean to set
     */
    public void setTransactionStatementBean(MainTransactionStatementBean transactionStatementBean) {
        this.transactionStatementBean = transactionStatementBean;
    }

    /**
     * @return the MainTransactionStatementBeanMap
     */
    public Map<Integer, MainTransactionStatementBean> getMainTransactionStatementBeanMap() {
        return MainTransactionStatementBeanMap;
    }

    /**
     * @param MainTransactionStatementBeanMap the
     * MainTransactionStatementBeanMap to set
     */
    public void setMainTransactionStatementBeanMap(Map<Integer, MainTransactionStatementBean> MainTransactionStatementBeanMap) {
        this.MainTransactionStatementBeanMap = MainTransactionStatementBeanMap;
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
     * @return the mainTransactionStatementBeanList
     */
    public List<MainTransactionStatementBean> getMainTransactionStatementBeanList() {
        return mainTransactionStatementBeanList;
    }

    /**
     * @param mainTransactionStatementBeanList the
     * mainTransactionStatementBeanList to set
     */
    public void setMainTransactionStatementBeanList(List<MainTransactionStatementBean> mainTransactionStatementBeanList) {
        this.mainTransactionStatementBeanList = mainTransactionStatementBeanList;
    }

    /**
     * @return the multipleOrDirectLevel
     */
    public Integer getMultipleOrDirectLevel() {
        return multipleOrDirectLevel;
    }

    /**
     * @param multipleOrDirectLevel the multipleOrDirectLevel to set
     */
    public void setMultipleOrDirectLevel(Integer multipleOrDirectLevel) {
        this.multipleOrDirectLevel = multipleOrDirectLevel;
    }

    /**
     * @return / * @return the glAdminUnitLocalList
     */
    public List<GlAdminUnit> getGlAdminUnitLocalList() {
        return glAdminUnitLocalList;
    }

    /**
     * @param glAdminUnitLocalList the glAdminUnitLocalList to set
     */
    public void setGlAdminUnitLocalList(List<GlAdminUnit> glAdminUnitLocalList) {
        this.glAdminUnitLocalList = glAdminUnitLocalList;
    }

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
     * @return the costCenterLocalList
     */
    public List<CostCenter> getCostCenterLocalList() {
        return costCenterLocalList;
    }

    /**
     * @param costCenterLocalList the costCenterLocalList to set
     */
    public void setCostCenterLocalList(List<CostCenter> costCenterLocalList) {
        this.costCenterLocalList = costCenterLocalList;
    }

    /**
     * @return the costCenterToSelected
     */
    public CostCenter getCostCenterToSelected() {
        return costCenterToSelected;
    }

    /**
     * @param costCenterToSelected the costCenterToSelected to set
     */
    public void setCostCenterToSelected(CostCenter costCenterToSelected) {
        this.costCenterToSelected = costCenterToSelected;
    }

    /**
     * @return the costCenterFromSelected
     */
    public CostCenter getCostCenterFromSelected() {
        return costCenterFromSelected;
    }

    /**
     * @param costCenterFromSelected the costCenterFromSelected to set
     */
    public void setCostCenterFromSelected(CostCenter costCenterFromSelected) {
        this.costCenterFromSelected = costCenterFromSelected;
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

    public List<MainTransactionStatementBean> getHoldMainTransactionStatementBeanListTemp() {
        return holdMainTransactionStatementBeanListTemp;
    }

    /**
     * @param holdMainTransactionStatementBeanListTemp the
     * holdMainTransactionStatementBeanListTemp to set
     */
    public void setHoldMainTransactionStatementBeanListTemp(List<MainTransactionStatementBean> holdMainTransactionStatementBeanListTemp) {
        this.holdMainTransactionStatementBeanListTemp = holdMainTransactionStatementBeanListTemp;
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
     * @return the zeroes
     */
    public boolean isZeroes() {
        return zeroes;
    }

    /**
     * @param zeroes the zeroes to set
     */
    public void setZeroes(boolean zeroes) {
        this.zeroes = zeroes;
    }

    /**
     * @return the glAccountNameSelected
     */
    public String getGlAccountNameSelected() {
        return glAccountNameSelected;
    }

    /**
     * @param glAccountNameSelected the glAccountNameSelected to set
     */
    public void setGlAccountNameSelected(String glAccountNameSelected) {
        this.glAccountNameSelected = glAccountNameSelected;
    }

    /**
     * @return the glAccountNumberSelected
     */
    public Integer getGlAccountNumberSelected() {
        return glAccountNumberSelected;
    }

    /**
     * @param glAccountNumberSelected the glAccountNumberSelected to set
     */
    public void setGlAccountNumberSelected(Integer glAccountNumberSelected) {
        this.glAccountNumberSelected = glAccountNumberSelected;
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
     * @return the monitoringBalancesBeanList
     */
    public List<MonitoringBalancesBean> getMonitoringBalancesBeanList() {
        return monitoringBalancesBeanList;
    }

    /**
     * @param monitoringBalancesBeanList the monitoringBalancesBeanList to set
     */
    public void setMonitoringBalancesBeanList(List<MonitoringBalancesBean> monitoringBalancesBeanList) {
        this.monitoringBalancesBeanList = monitoringBalancesBeanList;
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

    /**
     * @return the subAccountSummaryReportList
     */
    public List<SubAccountSummaryReport> getSubAccountSummaryReportList() {
        return subAccountSummaryReportList;
    }

    /**
     * @param subAccountSummaryReportList the subAccountSummaryReportList to set
     */
    public void setSubAccountSummaryReportList(List<SubAccountSummaryReport> subAccountSummaryReportList) {
        this.subAccountSummaryReportList = subAccountSummaryReportList;
    }

    /**
     * @return the mainTransactionStatementBeanSelected
     */
    public MainTransactionStatementBean getMainTransactionStatementBeanSelected() {
        return mainTransactionStatementBeanSelected;
    }

    /**
     * @param mainTransactionStatementBeanSelected the
     * mainTransactionStatementBeanSelected to set
     */
    public void setMainTransactionStatementBeanSelected(MainTransactionStatementBean mainTransactionStatementBeanSelected) {
        this.mainTransactionStatementBeanSelected = mainTransactionStatementBeanSelected;
    }

    /**
     * @return the pieChartsRows
     */
    public List<Object> getPieChartsRows() {
        return pieChartsRows;
    }

    /**
     * @param pieChartsRows the pieChartsRows to set
     */
    public void setPieChartsRows(List<Object> pieChartsRows) {
        this.pieChartsRows = pieChartsRows;
    }

    /**
     * @return the mainTransactionStatementBeanForChartList
     */
    public List<MainTransactionStatementBean> getMainTransactionStatementBeanForChartList() {
        return mainTransactionStatementBeanForChartList;
    }

    /**
     * @param mainTransactionStatementBeanForChartList the
     * mainTransactionStatementBeanForChartList to set
     */
    public void setMainTransactionStatementBeanForChartList(List<MainTransactionStatementBean> mainTransactionStatementBeanForChartList) {
        this.mainTransactionStatementBeanForChartList = mainTransactionStatementBeanForChartList;
    }

}
