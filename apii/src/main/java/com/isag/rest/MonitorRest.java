/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.rest;

import com.isag.businessservice.GeneraljournalDetailsService;
import com.isag.businessservice.GlAccountService;
import com.isag.businessservice.GlAnalyticsAccountsServcie;
import com.isag.businessservice.GlYearService;
import com.isag.businessservice.IsagUserYearService;
import com.isag.businessservice.reports.SubAccountSummaryService;
import com.isag.businessservice.reports.searchBean.CommonSearchBean;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.entity.Branch;
import com.isag.entity.CostCenter;
import com.isag.entity.GeneralJournalDetails;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
import com.isag.entity.GlAnalyticsAccounts;
import com.isag.entity.GlYear;
import com.isag.config.Secured;
import com.isag.config.UserLoginData;
import com.isag.dto.APIMainTransactionStatementBean;
import com.isag.dto.APIMonitorDTO;
import com.isag.dto.APIMonitorDetailDTO;
import com.isag.dto.APIMonitoringBalancesBean;
import com.isag.dto.APISubAccountSummaryReport;
import com.isag.views.SubAccountSummary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.ListUtils;

/**
 *
 * @author AHMEDESSAM
 */
@Path("/monitoring")
public class MonitorRest {

    @EJB
    private GlAnalyticsAccountsServcie glAnalyticsAccountsServcie;
    @EJB
    private GlAccountService accountService;
    @EJB
    private GeneraljournalDetailsService generaljournalDetailsService;
    @EJB
    private GlYearService yearService;
    @EJB
    private IsagUserYearService isagUserYearService;
    @EJB
    private SubAccountSummaryService subAccountSummaryService;
    private Integer branchid;
    private Integer userId;
    private List<GlAnalyticsAccounts> glAnalyticsList;
    private List<GlAccount> glAccountsSelectedFromSettings;
    private List<GlAccount> glAccountRootsList;
    private Map<Integer, GlAccount> GlAccountMap = new HashMap<>();
    private GlAccount glAccountSelected;
    private List<GeneralJournalDetails> generalJournalDetailsList;
    private List<GeneralJournalDetails> generalJournalDetailsFirstList;
    private List<GeneralJournalDetails> generalJournalDetailsSecondList;
    private Map<Integer, BigDecimal> totalBalanceMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalDebitMap = new HashMap<>();
    private Map<Integer, BigDecimal> totalCreditMap = new HashMap<>();
    private CommonSearchBean commonSearchBean;
    private CostCenter costCenterToSelected;
    private CostCenter costCenterFromSelected;
    private GlAdminUnit glAdminUnitToSelected;
    private GlAdminUnit glAdminUnitFromSelected;
    private String glAccountNameSelected;
    private Integer glAccountNumberSelected;
    private Map<Integer, APIMainTransactionStatementBean> MainTransactionStatementBeanMap = new HashMap<>();
    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;
    private BigDecimal totalCredit = BigDecimal.ZERO;
    private SubAccountSummarySearchBean subAccountSummarySearchBean;
    private List<APIMainTransactionStatementBean> mainTransactionStatementBeanList;
    private List<APISubAccountSummaryReport> subAccountSummaryReports;
    private List<APIMonitoringBalancesBean> monitoringBalancesBeanList;
    private List<APIMainTransactionStatementBean> holdMainTransactionStatementBeanListTemp;
    private boolean stickyHeader = false;
    private Integer multipleOrDirectLevel = 0;
    private boolean zeroes;
    private GlYear glYearSelection;
    private GlYear currentYear;
    private GlYear glYear;
    private List<GlYear> userYears;
    private List<GlYear> glYearList;
    private String totalCreditString;
    private String totalDebitString;
    private String totalBalanceString;
    private List<SubAccountSummary> subAccountSummaryList;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private GlAccount glAccountSelected2;
    private UserLoginData userLoginData;

    @Transactional
    @Secured
    @Path("/monitor/{branshSerial}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response monitoring(@Context ContainerRequestContext crc) {
        try {
//            if(loginRoot == null && loginRoot.branchSelected != null && loginRoot.branchSelected > 0){
//            crc.setProperty("branchSelected", loginRoot.branchSelected);
            setUserLoginData((UserLoginData) crc.getProperty("userLogInData"));
            resetLists();
            List<APIMonitorDTO> monitorDTOList = preperMonitor(loadingData());
            GenericEntity<List<APIMonitorDTO>> list = new GenericEntity<List<APIMonitorDTO>>(monitorDTOList) {
            };
            // Return the token on the response
            return Response.ok(list).build();
//            }else{
//             return Response.status(Response.Status.FORBIDDEN).build();
//            }
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private List<APIMonitorDTO> preperMonitor(List<APIMonitoringBalancesBean> monitoringBalancesBeans) {
        List<APIMonitorDTO> monitorDTOList = new ArrayList<>();
        for (APIMonitoringBalancesBean monitoringBalancesBean : monitoringBalancesBeans) {
            APIMonitorDTO monitorDTO = new APIMonitorDTO();
            monitorDTO.setAccountName(monitoringBalancesBean.getAccount().getName());
            monitorDTO.setBalance(monitoringBalancesBean.getBalance());
            List<APIMonitorDetailDTO> detail = new ArrayList<>();
            for (APIMainTransactionStatementBean mainTransactionStatementBean : monitoringBalancesBean.getMainTransactionStatementBeanMonitoringList()) {
                APIMonitorDetailDTO monitorDetailDTO = new APIMonitorDetailDTO();
                monitorDetailDTO.setAccountName(mainTransactionStatementBean.getAccountName());
                monitorDetailDTO.setCredit(mainTransactionStatementBean.getCredit());
                monitorDetailDTO.setDebit(mainTransactionStatementBean.getDebit());
                monitorDetailDTO.setValue(mainTransactionStatementBean.getValue());
                detail.add(monitorDetailDTO);
            }
            monitorDTO.setDetail(detail);
            monitorDTOList.add(monitorDTO);
        }
        return monitorDTOList;
    }

    public void resetLists() {
        try {

            branchid = getUserLoginData().getBranchId();
            userId = getUserLoginData().getUserId();
            glAnalyticsList = new ArrayList<>();
            glAccountsSelectedFromSettings = new ArrayList<>();
            glAccountRootsList = new ArrayList<>();
            setZeroes(true);
            monitoringBalancesBeanList = new ArrayList<>();
            year();
            reset();
            resetDateFrom();
            resetDateTo();
        } catch (Exception e) {
        }
    }

    public void reset() {
        commonSearchBean = new CommonSearchBean();
        MainTransactionStatementBeanMap = new HashMap<>();
        mainTransactionStatementBeanList = new ArrayList<>();
        holdMainTransactionStatementBeanListTemp = new ArrayList<>();
    }

    private void year() {
        List<GlYear> branchYears = yearService.getALLGlyearByBranchId(branchid);
        Date today = new Date();
        for (GlYear year : branchYears) {
            if (today.after(year.getDateFrom()) && today.before(year.getDateTo())) {
                setCurrentYear(year);
            }
        }

        GlYear defaultYear = yearService.getDefaultYearsByBranchIdByUser(userId, branchid);

        if (defaultYear != null) {
            setGlYear(defaultYear);
        } else if (getUserYears() != null && !getUserYears().isEmpty()) {
            setGlYear(getUserYears().get(0));
        }
    }

    public void resetDateFrom() {
        commonSearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);
    }

    public void resetDateTo() {
        commonSearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);
    }

    public List<APIMonitoringBalancesBean> loadingData() {
        try {
            glAnalyticsList = glAnalyticsAccountsServcie.getALLGlAnalyticsAccountsForMonitoringByBranch(branchid, 1);
            glAccountRootsList = accountService.findAllMainGlAccountsByBranch(branchid);
            if (glAccountRootsList != null && !glAccountRootsList.isEmpty()) {
                for (GlAccount acc : glAccountRootsList) {
                    getGlAccountMap().put(acc.getId(), acc);
                }

            }
            if (glAccountRootsList != null && !glAccountRootsList.isEmpty()) {
                for (GlAnalyticsAccounts glana : glAnalyticsList) {
                    if (getGlAccountMap() != null && glana.getAccountId() != null && getGlAccountMap().containsKey(glana.getAccountId().getId())) {
                        glAccountsSelectedFromSettings.add(getGlAccountMap().get(glana.getAccountId().getId()));
                    }
                }
            }
            if (glAccountsSelectedFromSettings != null && !glAccountsSelectedFromSettings.isEmpty()) {
                for (GlAccount glana : glAccountsSelectedFromSettings) {
                    setGlAccountSelected(glana);
                    search();
                }
            }
            return getMonitoringBalancesBeanList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private void search() {
        setSubAccountSummarySearchBean(new SubAccountSummarySearchBean());
        getSubAccountSummarySearchBean().setBranchId(branchid);
        getSubAccountSummarySearchBean().setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
        getSubAccountSummarySearchBean().setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);
        getSubAccountSummarySearchBean().setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
        getSubAccountSummarySearchBean().setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
        getSubAccountSummarySearchBean().setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
        getSubAccountSummarySearchBean().setCurrencyType(0);
        getSubAccountSummarySearchBean().setDateFrom(commonSearchBean.getDateFrom() != null ? commonSearchBean.getDateFrom() : null);
        getSubAccountSummarySearchBean().setDateTo(commonSearchBean.getDateTo() != null ? commonSearchBean.getDateTo() : null);

        Date dateFrom = getCommonSearchBean().getDateFrom() != null ? getCommonSearchBean().getDateFrom() : null;
        Date dateTo = getCommonSearchBean().getDateTo() != null ? getCommonSearchBean().getDateTo() : null;
        if (glAccountSelected != null) {
            getCommonSearchBean().setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
            getCommonSearchBean().setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
            getCommonSearchBean().setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
            getCommonSearchBean().setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);
            glAccountNameSelected = glAccountSelected.getName();
            glAccountNumberSelected = glAccountSelected.getAccNumber();
            getSumOfDetailsForCreditAndDebitAndBalance(getCommonSearchBean());
            reset();
            try {
                prepareDataForReportPerOneAccount(glAccountSelected);
                addLastRow();

            } catch (CloneNotSupportedException ex) {
                System.out.println(ex);
            }
            if (getMainTransactionStatementBeanList() != null && !mainTransactionStatementBeanList.isEmpty()) {
                setHoldMainTransactionStatementBeanListTemp(new ArrayList<>(getMainTransactionStatementBeanList()));
                loadChilds();
            }

        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار حساب اولا"));
        }
        if (getMainTransactionStatementBeanList() != null && !mainTransactionStatementBeanList.isEmpty()) {
            setStickyHeader(true);
        } else {
            setStickyHeader(false);
        }
        getCommonSearchBean().setDateFrom(dateFrom != null ? dateFrom : null);
        getCommonSearchBean().setDateTo(dateTo != null ? dateTo : null);
        fillMonitoringBeanList();
    }

    public void getSumOfDetailsForCreditAndDebitAndBalance(CommonSearchBean commonSearchBean) {
        generalJournalDetailsFirstList = new ArrayList<>();
        generalJournalDetailsSecondList = new ArrayList<>();
        setTotalBalanceMap(new HashMap());
        setTotalDebitMap(new HashMap());
        setTotalCreditMap(new HashMap());

        generalJournalDetailsList = new ArrayList<>();
        generalJournalDetailsFirstList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditAndSumOfDebitAndSumOfCreditForAllGlAccount(branchid, 1, commonSearchBean);
        generalJournalDetailsSecondList = generaljournalDetailsService.getTheDifferenceBetweenDebitAndCreditAndSumOfDebitAndSumOfCreditForAllGlAccount(branchid, 2, commonSearchBean);
        generalJournalDetailsList = ListUtils.union(generalJournalDetailsFirstList, generalJournalDetailsSecondList);
        if (generalJournalDetailsList != null && !generalJournalDetailsList.isEmpty()) {
            for (GeneralJournalDetails journalDetails : generalJournalDetailsList) {
                getTotalDebitMap().put(journalDetails.getSerial(), journalDetails.getDebitAmount());
                getTotalCreditMap().put(journalDetails.getSerial(), journalDetails.getCreditamount());
                getTotalBalanceMap().put(journalDetails.getSerial(), journalDetails.getDebitAmountLocal());
            }
        }
    }

    public void prepareDataForReportPerOneAccount(GlAccount glAccountRoot) throws CloneNotSupportedException {
        //glAccountRoot.setParentAcc(null);
        APIMainTransactionStatementBean statementBean = new APIMainTransactionStatementBean();
        statementBean.setId(glAccountRoot.getId());
        statementBean.setLevel(glAccountRoot.getLevelAcc());
        MainTransactionStatementBeanMap.put(glAccountRoot.getId(), statementBean);
        getChildTreeNodesForAccount(glAccountRoot);
    }

    public void getChildTreeNodesForAccount(GlAccount parentAcc) {

        if (parentAcc.getChildAccounts() != null && !parentAcc.getChildAccounts().isEmpty()) {
            for (GlAccount childAcc : parentAcc.getChildAccounts()) {
                APIMainTransactionStatementBean statementBean = new APIMainTransactionStatementBean();
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
                    setGlAccountSelected2(childAcc);
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
                getMainTransactionStatementBeanList().add(statementBean);
                MainTransactionStatementBeanMap.put(childAcc.getId(), statementBean);
                getChildTreeNodesForAccount(childAcc);
            }
        }
    }

    public APIMainTransactionStatementBean findDetailsForEachSubAccount(APIMainTransactionStatementBean statementBean) {

        getSubAccountSummarySearchBean().setAccountId(statementBean.getId());
        search2();
        if (getSubAccountSummaryReports() != null && !getSubAccountSummaryReports().isEmpty()) {
            statementBean.setSubAccountSummaryReportList(new ArrayList());
            for (APISubAccountSummaryReport sasr : getSubAccountSummaryReports()) {
                statementBean.getSubAccountSummaryReportList().add(sasr);
            }
        }
        return statementBean;
    }

    public void putValueOfParent(GlAccount parentAcc, BigDecimal value, BigDecimal debit, BigDecimal credit) {
        APIMainTransactionStatementBean statementBean = MainTransactionStatementBeanMap.get(parentAcc.getId());
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

    public void fillMonitoringBeanList() {
        if (mainTransactionStatementBeanList != null && !mainTransactionStatementBeanList.isEmpty()) {
            APIMainTransactionStatementBean statementBean = new APIMainTransactionStatementBean();
            statementBean = MainTransactionStatementBeanMap.get(glAccountSelected.getId());

            APIMonitoringBalancesBean balancesBean = new APIMonitoringBalancesBean();
            balancesBean.setAccount(glAccountSelected);
            balancesBean.setBalance(statementBean.getValue());
            balancesBean.setMainTransactionStatementBeanMonitoringList(new ArrayList());
            for (APIMainTransactionStatementBean bean : mainTransactionStatementBeanList) {
                balancesBean.getMainTransactionStatementBeanMonitoringList().add(bean);
            }
            monitoringBalancesBeanList.add(balancesBean);
        } else if (mainTransactionStatementBeanList != null && mainTransactionStatementBeanList.isEmpty()) {
            APIMainTransactionStatementBean statementBean = new APIMainTransactionStatementBean();
            statementBean = MainTransactionStatementBeanMap.get(glAccountSelected.getId());
            APIMonitoringBalancesBean balancesBean = new APIMonitoringBalancesBean();
            balancesBean.setAccount(glAccountSelected);
            balancesBean.setBalance(statementBean.getValue() != null ? statementBean.getValue() : BigDecimal.ZERO);
            balancesBean.setMainTransactionStatementBeanMonitoringList(new ArrayList());
            monitoringBalancesBeanList.add(balancesBean);
        }
    }

    public void addLastRow() {
        APIMainTransactionStatementBean statementBean = new APIMainTransactionStatementBean();
        statementBean = MainTransactionStatementBeanMap.get(glAccountSelected.getId());
        statementBean.setAccountName("الاجمالي ");
        statementBean.setLevel(null);
        statementBean.setParentId(-1);

        mainTransactionStatementBeanList.add(statementBean);
    }

    public void loadChilds() {

        if (holdMainTransactionStatementBeanListTemp != null && !holdMainTransactionStatementBeanListTemp.isEmpty()) {
            mainTransactionStatementBeanList = new ArrayList<>(holdMainTransactionStatementBeanListTemp);
        }

        if (getMultipleOrDirectLevel() == 1 && !isZeroes() && holdMainTransactionStatementBeanListTemp != null) {
            Iterator it = mainTransactionStatementBeanList.iterator();
            List<APIMainTransactionStatementBean> mtsbList = new ArrayList<>();
            while (it.hasNext()) {
                APIMainTransactionStatementBean bean = new APIMainTransactionStatementBean();
                bean = (APIMainTransactionStatementBean) it.next();
                if (glAccountSelected.getId().compareTo(bean.getParentId()) == 0 || bean.getParentId() == -1) {
                    mtsbList.add(bean);
                }
            }
            mainTransactionStatementBeanList = new ArrayList<>(mtsbList);
        } else if (getMultipleOrDirectLevel() == 0 && isZeroes()) {
            Iterator it = mainTransactionStatementBeanList.iterator();
            List<APIMainTransactionStatementBean> mtsbList = new ArrayList<>();
            while (it.hasNext()) {
                APIMainTransactionStatementBean bean = new APIMainTransactionStatementBean();
                bean = (APIMainTransactionStatementBean) it.next();
                if (bean.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    mtsbList.add(bean);
                }
            }
            mainTransactionStatementBeanList = new ArrayList<>(mtsbList);
        } else if (getMultipleOrDirectLevel() == 1 && isZeroes()) {
            Iterator it = mainTransactionStatementBeanList.iterator();
            List<APIMainTransactionStatementBean> mtsbList = new ArrayList<>();
            while (it.hasNext()) {
                APIMainTransactionStatementBean bean = new APIMainTransactionStatementBean();
                bean = (APIMainTransactionStatementBean) it.next();
                if (glAccountSelected.getId().compareTo(bean.getParentId()) == 0 || bean.getParentId() == -1) {
                    if (bean.getValue().compareTo(BigDecimal.ZERO) != 0) {
                        mtsbList.add(bean);
                    }
                }
            }
            mainTransactionStatementBeanList = new ArrayList<>(mtsbList);
        }
    }

    public void search2() {
        setTotalDebitString(new String());
        setTotalCreditString(new String());
        if (glAccountSelected2 != null) {
            subAccountSummarySearchBean.setBranchId(branchid);
            subAccountSummarySearchBean.setAccountId(glAccountSelected2 != null ? glAccountSelected2.getId() : null);
            subAccountSummarySearchBean.setAccClass(glAccountSelected2 != null ? glAccountSelected2.getAccClass().toString() : null);
            subAccountSummarySearchBean.setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
            subAccountSummarySearchBean.setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
            subAccountSummarySearchBean.setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
            subAccountSummarySearchBean.setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);

            GlYear glYear = getGlYearSelection();

            BigDecimal beforeBalance = BigDecimal.ZERO;
            if (subAccountSummarySearchBean.getDateFrom() != null && subAccountSummarySearchBean.getDateFrom().after(glYear.getDateFrom())) {
                beforeBalance = subAccountSummaryService.getBeforeBalance(subAccountSummarySearchBean, glYear, false);
            }
            subAccountSummaryReports = new ArrayList<>();
            APISubAccountSummaryReport subAccountSummaryReport = new APISubAccountSummaryReport();
            subAccountSummaryReport.setBalance(beforeBalance);
            subAccountSummaryReport.setSymbolName("رصيد البداية");
            subAccountSummaryReport.setDebitAmount(BigDecimal.ZERO);
            subAccountSummaryReport.setCreditAmount(BigDecimal.ZERO);
            subAccountSummaryReports.add(subAccountSummaryReport);

            setSubAccountSummaryList(subAccountSummaryService.getSubAccountSummary(subAccountSummarySearchBean, glYear, false));

            totalDebit = BigDecimal.ZERO;
            totalCredit = BigDecimal.ZERO;
            setTotalBalance(BigDecimal.ZERO);
            for (SubAccountSummary subAccountSummary1 : getSubAccountSummaryList()) {
                APISubAccountSummaryReport subAccountSummaryReport1 = new APISubAccountSummaryReport();
                subAccountSummaryReport1.setDate(subAccountSummary1.getDate());
                subAccountSummaryReport1.setGeneralJournalId(subAccountSummary1.getGeneralJournalId());
                subAccountSummaryReport1.setSerial(subAccountSummary1.getSerial());
                subAccountSummaryReport1.setGeneralDecument(subAccountSummary1.getGeneralDecument());
                subAccountSummaryReport1.setSymbolName(subAccountSummary1.getSymbolName());
                subAccountSummaryReport1.setGeneralStatement(subAccountSummary1.getDiscribtion());

                if (subAccountSummarySearchBean.getCurrencyType() == 0) {
                    subAccountSummaryReport1.setDebitAmount(subAccountSummary1.getDebitAmount() != null ? subAccountSummary1.getDebitAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                    subAccountSummaryReport1.setCreditAmount(subAccountSummary1.getCreditAmount() != null ? subAccountSummary1.getCreditAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                } else {
                    subAccountSummaryReport1.setDebitAmount(subAccountSummary1.getDebitAmountForiegn() != null ? subAccountSummary1.getDebitAmountForiegn().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                    subAccountSummaryReport1.setCreditAmount(subAccountSummary1.getCreditAmountForiegn() != null ? subAccountSummary1.getCreditAmountForiegn().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);

                }

                if ("DEBIT".equalsIgnoreCase(subAccountSummarySearchBean.getAccClass())) {
                    beforeBalance = beforeBalance.add((subAccountSummary1.getDebitAmount() == null ? BigDecimal.ZERO : subAccountSummary1.getDebitAmount()).subtract(subAccountSummary1.getCreditAmount() == null ? BigDecimal.ZERO : subAccountSummary1.getCreditAmount()));
                } else if ("Credit".equalsIgnoreCase(subAccountSummarySearchBean.getAccClass())) {
                    beforeBalance = beforeBalance.add((subAccountSummary1.getCreditAmount() == null ? BigDecimal.ZERO : subAccountSummary1.getCreditAmount()).subtract(subAccountSummary1.getDebitAmount() == null ? BigDecimal.ZERO : subAccountSummary1.getDebitAmount()));
                }
                subAccountSummaryReport1.setBalance(beforeBalance == null ? BigDecimal.ZERO : (new BigDecimal(beforeBalance.doubleValue())).setScale(2, RoundingMode.UP));

                totalDebit = totalDebit.add(subAccountSummaryReport1.getDebitAmount() != null ? subAccountSummaryReport1.getDebitAmount() : BigDecimal.ZERO);
                totalCredit = totalCredit.add(subAccountSummaryReport1.getCreditAmount() != null ? subAccountSummaryReport1.getCreditAmount() : BigDecimal.ZERO);
                setTotalBalance(getTotalBalance().add(subAccountSummaryReport1.getBalance() != null ? subAccountSummaryReport1.getBalance() : BigDecimal.ZERO));
                subAccountSummaryReports.add(subAccountSummaryReport1);
            }
            DecimalFormat df = new DecimalFormat("#,##0.00");
            setTotalDebitString(df.format(totalDebit));
            setTotalCreditString(df.format(totalCredit));
            if (subAccountSummaryReports != null && subAccountSummaryReports.size() > 0) {
                setStickyHeader(true);
            }
        }
    }

    /**
     * @return the GlAccountMap
     */
    public Map<Integer, GlAccount> getGlAccountMap() {
        return GlAccountMap;
    }

    /**
     * @param GlAccountMap the GlAccountMap to set
     */
    public void setGlAccountMap(Map<Integer, GlAccount> GlAccountMap) {
        this.GlAccountMap = GlAccountMap;
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
     * @return the subAccountSummarySearchBean
     */
    public SubAccountSummarySearchBean getSubAccountSummarySearchBean() {
        return subAccountSummarySearchBean;
    }

    /**
     * @param subAccountSummarySearchBean the subAccountSummarySearchBean to set
     */
    public void setSubAccountSummarySearchBean(SubAccountSummarySearchBean subAccountSummarySearchBean) {
        this.subAccountSummarySearchBean = subAccountSummarySearchBean;
    }

    /**
     * @return the mainTransactionStatementBeanList
     */
    public List<APIMainTransactionStatementBean> getMainTransactionStatementBeanList() {
        return mainTransactionStatementBeanList;
    }

    /**
     * @param mainTransactionStatementBeanList the
     * mainTransactionStatementBeanList to set
     */
    public void setMainTransactionStatementBeanList(List<APIMainTransactionStatementBean> mainTransactionStatementBeanList) {
        this.mainTransactionStatementBeanList = mainTransactionStatementBeanList;
    }

    /**
     * @return the subAccountSummaryReports
     */
    public List<APISubAccountSummaryReport> getSubAccountSummaryReports() {
        return subAccountSummaryReports;
    }

    /**
     * @param subAccountSummaryReports the subAccountSummaryReports to set
     */
    public void setSubAccountSummaryReports(List<APISubAccountSummaryReport> subAccountSummaryReports) {
        this.subAccountSummaryReports = subAccountSummaryReports;
    }

    /**
     * @return the monitoringBalancesBeanList
     */
    public List<APIMonitoringBalancesBean> getMonitoringBalancesBeanList() {
        return monitoringBalancesBeanList;
    }

    /**
     * @param monitoringBalancesBeanList the monitoringBalancesBeanList to set
     */
    public void setMonitoringBalancesBeanList(List<APIMonitoringBalancesBean> monitoringBalancesBeanList) {
        this.monitoringBalancesBeanList = monitoringBalancesBeanList;
    }

    /**
     * @return the holdMainTransactionStatementBeanListTemp
     */
    public List<APIMainTransactionStatementBean> getHoldMainTransactionStatementBeanListTemp() {
        return holdMainTransactionStatementBeanListTemp;
    }

    /**
     * @param holdMainTransactionStatementBeanListTemp the
     * holdMainTransactionStatementBeanListTemp to set
     */
    public void setHoldMainTransactionStatementBeanListTemp(List<APIMainTransactionStatementBean> holdMainTransactionStatementBeanListTemp) {
        this.holdMainTransactionStatementBeanListTemp = holdMainTransactionStatementBeanListTemp;
    }

    /**
     * @return the stickyHeader
     */
    public boolean isStickyHeader() {
        return stickyHeader;
    }

    /**
     * @param stickyHeader the stickyHeader to set
     */
    public void setStickyHeader(boolean stickyHeader) {
        this.stickyHeader = stickyHeader;
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
     * @return the glYearSelection
     */
    public GlYear getGlYearSelection() {
        if (glYearSelection == null) {
            if (getGlYear() != null) {
                glYearSelection = getGlYear();
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
     * @return the currentYear
     */
    public GlYear getCurrentYear() {
        return currentYear;
    }

    /**
     * @param currentYear the currentYear to set
     */
    public void setCurrentYear(GlYear currentYear) {
        this.currentYear = currentYear;
    }

    /**
     * @return the glYear
     */
    public GlYear getGlYear() {
        return glYear;
    }

    /**
     * @param glYear the glYear to set
     */
    public void setGlYear(GlYear glYear) {
        this.glYear = glYear;
    }

    /**
     * @return the userYears
     */
    public List<GlYear> getUserYears() {
        if (userYears == null || userYears.isEmpty()) {
            if (branchid != null) {
                Branch branch = new Branch();
                branch.setId(branchid);
                userYears = isagUserYearService.findYearByUserId(userId, branch);
            } else {
                userYears = yearService.getAllYear();

            }
        }

        return userYears;
    }

    /**
     * @param userYears the userYears to set
     */
    public void setUserYears(List<GlYear> userYears) {
        this.userYears = userYears;
    }

    /**
     * @return the glYearList
     */
    public List<GlYear> getGlYearList() {
        if (glYearList == null || glYearList.isEmpty()) {
            Branch branch = new Branch();
            branch.setId(branchid);
            glYearList = isagUserYearService.findYearByUserId(userId, branch);
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
     * @return the totalCreditString
     */
    public String getTotalCreditString() {
        return totalCreditString;
    }

    /**
     * @param totalCreditString the totalCreditString to set
     */
    public void setTotalCreditString(String totalCreditString) {
        this.totalCreditString = totalCreditString;
    }

    /**
     * @return the totalDebitString
     */
    public String getTotalDebitString() {
        return totalDebitString;
    }

    /**
     * @param totalDebitString the totalDebitString to set
     */
    public void setTotalDebitString(String totalDebitString) {
        this.totalDebitString = totalDebitString;
    }

    /**
     * @return the totalBalanceString
     */
    public String getTotalBalanceString() {
        return totalBalanceString;
    }

    /**
     * @param totalBalanceString the totalBalanceString to set
     */
    public void setTotalBalanceString(String totalBalanceString) {
        this.totalBalanceString = totalBalanceString;
    }

    /**
     * @return the subAccountSummaryList
     */
    public List<SubAccountSummary> getSubAccountSummaryList() {
        return subAccountSummaryList;
    }

    /**
     * @param subAccountSummaryList the subAccountSummaryList to set
     */
    public void setSubAccountSummaryList(List<SubAccountSummary> subAccountSummaryList) {
        this.subAccountSummaryList = subAccountSummaryList;
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
     * @return the glAccountSelected2
     */
    public GlAccount getGlAccountSelected2() {
        return glAccountSelected2;
    }

    /**
     * @param glAccountSelected2 the glAccountSelected2 to set
     */
    public void setGlAccountSelected2(GlAccount glAccountSelected2) {
        this.glAccountSelected2 = glAccountSelected2;
    }

    public UserLoginData getUserLoginData() {
        return userLoginData;
    }

    public void setUserLoginData(UserLoginData userLoginData) {
        this.userLoginData = userLoginData;
    }
}
