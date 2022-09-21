package com.toby.reports;

import com.toby.businessservice.report.SubAccountSummaryService;
import com.toby.businessservice.CostCenterService;
import com.toby.businessservice.GlAccountService;
import com.toby.businessservice.GlAdminUnitService;
import com.toby.businessservice.GlYearService;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.bussinessservice.global.Utility;
import com.toby.converter.CostCenterConverter;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.converter.GlaccountConverter;
import com.toby.entity.CostCenter;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlYear;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.views.SubAccountSummary;
import com.toby.views.SubAccountSummaryReport;
import net.sf.jasperreports.engine.JRException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named("subAccountSummaryReportMB")
@ViewScoped
public class SubAccountSummaryReportMB extends BaseGlAccountReportBean {

    private List<GlAccount> accountLocalList = new ArrayList<>();
    private GlAccount glAccountSelected;

    private List<GlAdminUnit> glAdminUnitLocalList = new ArrayList<>();
    private GlAdminUnit glAdminUnitToSelected;
    private GlAdminUnit glAdminUnitFromSelected;

    private List<CostCenter> costCenterLocalList;
    private CostCenter costCenterToSelected;
    private CostCenter costCenterFromSelected;

    private GlaccountConverter accountConverter;
    private CostCenterConverter costCenterConverter;
    private GlAdminUnitConverter glAdminUnitConverter;

    private SubAccountSummarySearchBean subAccountSummarySearchBean;
    private List<SubAccountSummary> subAccountSummaryList;
    private List<SubAccountSummaryReport> subAccountSummaryReports;

    private BigDecimal totalCredit = BigDecimal.ZERO;
    private BigDecimal totalDebit = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;
    private String totalCreditString;
    private String totalDebitString;
    private String totalBalanceString;
    private String uri;

    @EJB
    SubAccountSummaryService subAccountSummaryService;

    @EJB
    GlAccountService glAccountService;

    @EJB
    GlYearService glYearService;

    @EJB
    CostCenterService costCenterService;

    @EJB
    private GlAdminUnitService glAdminUnitService;

    @PostConstruct
    public void init() {
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

        if (getGlYearSelection() != null) {
            load();
            subAccountSummaryReports = new ArrayList<>();

            accountLocalList = getGlAccountList();
            costCenterLocalList = getCostCenterList();
            glAdminUnitLocalList = getGlAdminUnitList();

            costCenterConverter = new CostCenterConverter(costCenterLocalList);
            glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnitLocalList);
            accountConverter = new GlaccountConverter(accountLocalList);

            reset();
            
            
        } else {
            redirectFinancailYearPage();
        }
    }

    @Override
    public void reset() {
        subAccountSummarySearchBean = new SubAccountSummarySearchBean();
        subAccountSummaryReports = new ArrayList<>();

        glAccountSelected = null;
        costCenterToSelected = null;
        costCenterFromSelected = null;
        glAdminUnitFromSelected = null;
        glAdminUnitToSelected = null;

        resetDateFrom();
        resetDateTo();
        
        subAccountSummarySearchBean.setCurrencyType(0);
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

    public List<GlAccount> completeGlAccount(String query) {
        List<GlAccount> glaccounts = getAccountLocalList();//45
        if (query == null || query.trim().equals("")) {
            accountConverter = new GlaccountConverter(glaccounts);
            return glaccounts;
        }
        List<GlAccount> filteredGlaccounts = new ArrayList<>();
        String nameAr;
        Integer code;
        GlAccount glaccount;
        for (int i = 0; i < getAccountLocalList().size(); i++) {
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

    @Override
    public void search() {
        totalDebitString = new String();
        totalCreditString = new String();
        if (glAccountSelected != null) {
            subAccountSummarySearchBean.setBranchId(getUserData().getUserBranch().getId());
            subAccountSummarySearchBean.setAccountId(glAccountSelected.getId() != null ? glAccountSelected.getId() : null);
            subAccountSummarySearchBean.setAccClass(glAccountSelected.getAccClass() != null ? glAccountSelected.getAccClass().toString() : null);
            subAccountSummarySearchBean.setCostCenterTo(costCenterToSelected != null ? costCenterToSelected.getId() : null);
            subAccountSummarySearchBean.setCostCenterFrom(costCenterFromSelected != null ? costCenterFromSelected.getId() : null);
            subAccountSummarySearchBean.setAdminUnitFrom(glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getId() : null);
            subAccountSummarySearchBean.setAdminUnitTo(glAdminUnitToSelected != null ? glAdminUnitToSelected.getId() : null);

            GlYear glYear = getGlYearSelection();
            if (uri.contains("Extended")) {
                List<GlYear> glYearOfSelectedDate = glYearService.findGlYearByDateBetween(getUserData().getUserBranch().getId(), subAccountSummarySearchBean.getDateFrom() != null ? subAccountSummarySearchBean.getDateFrom() : Utility.hourNow());
                glYear = glYearOfSelectedDate.get(0) != null ? glYearOfSelectedDate.get(0) : getGlYearSelection();
            }
            BigDecimal beforeBalance = BigDecimal.ZERO;
            if (subAccountSummarySearchBean.getDateFrom() != null && subAccountSummarySearchBean.getDateFrom().after(glYear.getDateFrom())) {
                if (uri.contains("Extended")) {
                    beforeBalance = subAccountSummaryService.getBeforeBalance(subAccountSummarySearchBean, glYear, true);
                } else {
                    beforeBalance = subAccountSummaryService.getBeforeBalance(subAccountSummarySearchBean, glYear, false);
                }
            }
            subAccountSummaryReports = new ArrayList<>();
            SubAccountSummaryReport subAccountSummaryReport = new SubAccountSummaryReport();
            subAccountSummaryReport.setBalance(beforeBalance);
            subAccountSummaryReport.setSymbolName("رصيد البداية");
            subAccountSummaryReport.setDebitAmount(BigDecimal.ZERO);
            subAccountSummaryReport.setCreditAmount(BigDecimal.ZERO);
            subAccountSummaryReports.add(subAccountSummaryReport);

            if (uri.contains("Extended")) {
                subAccountSummaryList = subAccountSummaryService.getSubAccountSummaryNew(subAccountSummarySearchBean, glYear, true);
            } else {
                subAccountSummaryList = subAccountSummaryService.getSubAccountSummaryNew(subAccountSummarySearchBean, glYear, false);
            }

             totalDebit = BigDecimal.ZERO;
            totalCredit = BigDecimal.ZERO;
            totalBalance = BigDecimal.ZERO;
            for (SubAccountSummary subAccountSummary1 : subAccountSummaryList) {
                SubAccountSummaryReport subAccountSummaryReport1 = new SubAccountSummaryReport();
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
                totalBalance = totalBalance.add(subAccountSummaryReport1.getBalance() != null ? subAccountSummaryReport1.getBalance() : BigDecimal.ZERO);
                subAccountSummaryReports.add(subAccountSummaryReport1);
            }
            DecimalFormat df = new DecimalFormat("#,##0.00");
            totalDebitString = df.format(totalDebit);
            totalCreditString = df.format(totalCredit);
            if (subAccountSummaryReports != null && subAccountSummaryReports.size() > 0) {
                setStickyHeader(true);
            }
        } else {
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار حساب اولا"));
        }
    }

    @Override
    public void checkDate(Boolean dateType
    ) {
        if (dateType) {
            if (checkFinancailYear(subAccountSummarySearchBean.getDateFrom())) {
                resetDateFrom();
            }
        } else {
            if (checkFinancailYear(subAccountSummarySearchBean.getDateTo())) {
                resetDateTo();
            }
        }
    }

    @Override
    public void resetDateFrom() {
        subAccountSummarySearchBean.setDateFrom(getGlYearSelection() != null ? getGlYearSelection().getDateFrom() : null);

    }

    @Override
    public void resetDateTo() {
        subAccountSummarySearchBean.setDateTo(getGlYearSelection() != null ? getGlYearSelection().getDateTo() : null);

    }

    public void checkFinancailYearFrom() {
        if (subAccountSummarySearchBean.getDateFrom().before(getGlYearSelection().getDateFrom()) || subAccountSummarySearchBean.getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateFrom();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    public void checkFinancailYearTo() {
        if (subAccountSummarySearchBean.getDateTo().before(getGlYearSelection().getDateFrom()) || subAccountSummarySearchBean.getDateFrom().after(getGlYearSelection().getDateTo())) {
            resetDateTo();
            setShowMessageGeneral(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "يجب اختيار تاريخ يقع بين الفترة الماليه"));
        }
    }

    public SubAccountSummarySearchBean getSubAccountSummarySearchBean() {
        return subAccountSummarySearchBean;
    }

    public void setSubAccountSummarySearchBean(SubAccountSummarySearchBean subAccountSummarySearchBean) {
        this.subAccountSummarySearchBean = subAccountSummarySearchBean;
    }

    public List<SubAccountSummary> getSubAccountSummaryList() {
        return subAccountSummaryList;
    }

    public void setSubAccountSummaryList(List<SubAccountSummary> subAccountSummaryList) {
        this.subAccountSummaryList = subAccountSummaryList;
    }

    public List<SubAccountSummaryReport> getSubAccountSummaryReports() {
        return subAccountSummaryReports;
    }

    public void setSubAccountSummaryReports(List<SubAccountSummaryReport> subAccountSummaryReports) {
        this.subAccountSummaryReports = subAccountSummaryReports;
    }

    public SubAccountSummaryService getSubAccountSummaryService() {
        return subAccountSummaryService;
    }

    public void setSubAccountSummaryService(SubAccountSummaryService subAccountSummaryService) {
        this.subAccountSummaryService = subAccountSummaryService;
    }

    public GlYearService getGlYearService() {
        return glYearService;
    }

    public void setGlYearService(GlYearService glYearService) {
        this.glYearService = glYearService;
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        //  hashMap.put("companyName", getUserData().getCompany().getName());
        hashMap.put("fromCostCenter", costCenterFromSelected != null ? costCenterFromSelected.getCode() : null);
        hashMap.put("fromCostCenterName", costCenterFromSelected != null ? costCenterFromSelected.getName() : null);
        hashMap.put("toCostCenter", costCenterToSelected != null ? costCenterToSelected.getCode() : null);
        hashMap.put("toCostCenterName", costCenterToSelected != null ? costCenterToSelected.getName() : null);
        hashMap.put("fromAdminUnit", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getCode() : null);
        hashMap.put("fromAdminUnitName", glAdminUnitFromSelected != null ? glAdminUnitFromSelected.getName() : null);
        hashMap.put("toAdminUnit", glAdminUnitToSelected != null ? glAdminUnitToSelected.getCode() : null);
        hashMap.put("toAdminUnitName", glAdminUnitToSelected != null ? glAdminUnitToSelected.getName() : null);
        hashMap.put("fromDate", getSubAccountSummarySearchBean().getDateFrom());
        hashMap.put("toDate", getSubAccountSummarySearchBean().getDateTo());
        //if (getSubAccountSummarySearchBean().getAccountId() != null) {
        //   GlAccount glAccount = glAccountService.findGlAccount(getSubAccountSummarySearchBean().getAccountId());
        hashMap.put("accountName", glAccountSelected != null ? glAccountSelected.getName() : null);
        hashMap.put("accountCode", glAccountSelected != null ? glAccountSelected.getAccNumber() : null);
        //  }

        hashMap.put("accountSummaryReportText", userDDs.get("ACCOUNT_SUMMARY_REPORT"));
        hashMap.put("fromCostCenterText", userDDs.get("COST_CENTER_FROM"));
        hashMap.put("toCostCenterText", userDDs.get("COST_CENTER_TO"));
        hashMap.put("fromAdminUnitText", userDDs.get("ADMIN_UNIT_FROM"));
        hashMap.put("toAdminUnitText", userDDs.get("ADMIN_UNIT_TO"));
        hashMap.put("accountNameText", userDDs.get("ACCOUNT_NAME"));
        hashMap.put("fromDateText", userDDs.get("YEAR_FROM"));
        hashMap.put("toDateText", userDDs.get("YEAR_TO"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("journalNumberText", userDDs.get("GENERAL_NUMBER"));
        hashMap.put("documentNumberText", userDDs.get("DOCUMENT_NUMBER"));
        hashMap.put("journalTypeText", userDDs.get("JOURNAL_TYPE"));
        hashMap.put("journalStatementText", userDDs.get("GERNAL_STATEMENT"));
        hashMap.put("debitText", userDDs.get("DEBIT"));
        hashMap.put("creditText", userDDs.get("CREDIT"));
        hashMap.put("balanceText", userDDs.get("BALANCE"));
        hashMap.put("totalText", userDDs.get("TOTAL"));
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        //hashMap.put("companyImage", getUserData().getImageReportPath());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        if (subAccountSummarySearchBean.getCurrencyType() != null
                && subAccountSummarySearchBean.getCurrencyType().compareTo(0) == 0) {
            hashMap.put("currencyName", userDDs.get("LOCAL_CURRENCY"));
        } else {
            hashMap.put("currencyName", userDDs.get("FOREIGN_CURRENCY"));
        }
        hashMap.put("currencyNameText", userDDs.get("CURRENCY_TYP"));
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (subAccountSummaryReports != null && !subAccountSummaryReports.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "subAccountsummaryReport.jasper", subAccountSummaryReports, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
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

    public GlaccountConverter getAccountConverter() {
        return accountConverter;
    }

    public void setAccountConverter(GlaccountConverter accountConverter) {
        this.accountConverter = accountConverter;
    }

    public GlAccount getGlAccountSelected() {
        return glAccountSelected;
    }

    public void setGlAccountSelected(GlAccount glAccountSelected) {
        this.glAccountSelected = glAccountSelected;
    }

    public List<GlAccount> getAccountLocalList() {
        return accountLocalList;
    }

    public void setAccountLocalList(List<GlAccount> accountLocalList) {
        this.accountLocalList = accountLocalList;
    }

    public CostCenter getCostCenterToSelected() {
        return costCenterToSelected;
    }

    public void setCostCenterToSelected(CostCenter costCenterToSelected) {
        this.costCenterToSelected = costCenterToSelected;
    }

    public CostCenter getCostCenterFromSelected() {
        return costCenterFromSelected;
    }

    public void setCostCenterFromSelected(CostCenter costCenterFromSelected) {
        this.costCenterFromSelected = costCenterFromSelected;
    }

    public CostCenterConverter getCostCenterConverter() {
        return costCenterConverter;
    }

    public void setCostCenterConverter(CostCenterConverter costCenterConverter) {
        this.costCenterConverter = costCenterConverter;
    }

    public List<CostCenter> getCostCenterLocalList() {
        return costCenterLocalList;
    }

    public void setCostCenterLocalList(List<CostCenter> costCenterLocalList) {
        this.costCenterLocalList = costCenterLocalList;
    }

    public List<GlAdminUnit> getGlAdminUnitLocalList() {
        return glAdminUnitLocalList;
    }

    public void setGlAdminUnitLocalList(List<GlAdminUnit> glAdminUnitLocalList) {
        this.glAdminUnitLocalList = glAdminUnitLocalList;
    }

    public GlAdminUnit getGlAdminUnitToSelected() {
        return glAdminUnitToSelected;
    }

    public void setGlAdminUnitToSelected(GlAdminUnit glAdminUnitToSelected) {
        this.glAdminUnitToSelected = glAdminUnitToSelected;
    }

    public GlAdminUnit getGlAdminUnitFromSelected() {
        return glAdminUnitFromSelected;
    }

    public void setGlAdminUnitFromSelected(GlAdminUnit glAdminUnitFromSelected) {
        this.glAdminUnitFromSelected = glAdminUnitFromSelected;
    }

    public GlAdminUnitConverter getGlAdminUnitConverter() {
        return glAdminUnitConverter;
    }

    public void setGlAdminUnitConverter(GlAdminUnitConverter glAdminUnitConverter) {
        this.glAdminUnitConverter = glAdminUnitConverter;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
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
}
